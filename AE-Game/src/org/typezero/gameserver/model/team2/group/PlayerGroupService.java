/*
 * Copyright (c) 2015, TypeZero Engine (game.developpers.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of TypeZero Engine nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.typezero.gameserver.model.team2.group;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.callbacks.metadata.GlobalCallback;
import com.aionemu.commons.utils.internal.chmv8.PlatformDependent;
import org.typezero.gameserver.configs.main.GroupConfig;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.model.team2.TeamType;
import org.typezero.gameserver.model.team2.common.events.PlayerLeavedEvent.LeaveReson;
import org.typezero.gameserver.model.team2.common.events.ShowBrandEvent;
import org.typezero.gameserver.model.team2.common.events.TeamKinahDistributionEvent;
import org.typezero.gameserver.model.team2.common.legacy.GroupEvent;
import org.typezero.gameserver.model.team2.common.legacy.LootGroupRules;
import org.typezero.gameserver.model.team2.group.callback.AddPlayerToGroupCallback;
import org.typezero.gameserver.model.team2.group.callback.PlayerGroupCreateCallback;
import org.typezero.gameserver.model.team2.group.callback.PlayerGroupDisbandCallback;
import org.typezero.gameserver.model.team2.group.events.ChangeGroupLeaderEvent;
import org.typezero.gameserver.model.team2.group.events.ChangeGroupLootRulesEvent;
import org.typezero.gameserver.model.team2.group.events.GroupDisbandEvent;
import org.typezero.gameserver.model.team2.group.events.PlayerConnectedEvent;
import org.typezero.gameserver.model.team2.group.events.PlayerDisconnectedEvent;
import org.typezero.gameserver.model.team2.group.events.PlayerEnteredEvent;
import org.typezero.gameserver.model.team2.group.events.PlayerGroupInvite;
import org.typezero.gameserver.model.team2.group.events.PlayerGroupLeavedEvent;
import org.typezero.gameserver.model.team2.group.events.PlayerGroupStopMentoringEvent;
import org.typezero.gameserver.model.team2.group.events.PlayerGroupUpdateEvent;
import org.typezero.gameserver.model.team2.group.events.PlayerStartMentoringEvent;
import org.typezero.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import org.typezero.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.typezero.gameserver.restrictions.RestrictionsManager;
import org.typezero.gameserver.services.AutoGroupService;
import org.typezero.gameserver.utils.PacketSendUtility;
import org.typezero.gameserver.utils.ThreadPoolManager;
import org.typezero.gameserver.utils.TimeUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class PlayerGroupService {

	private static final Logger log = LoggerFactory.getLogger(PlayerGroupService.class);

	private static final Map<Integer, PlayerGroup> groups = PlatformDependent.newConcurrentHashMap();
	private static final AtomicBoolean offlineCheckStarted = new AtomicBoolean();

	public static final void inviteToGroup(final Player inviter, final Player invited) {
		if (canInvite(inviter, invited)) {
			PlayerGroupInvite invite = new PlayerGroupInvite(inviter, invited);
			if (invited.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_PARTY_DO_YOU_ACCEPT_INVITATION, invite)) {
				PacketSendUtility.sendPacket(invited, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_PARTY_DO_YOU_ACCEPT_INVITATION, 0, 0,
					inviter.getName()));
			}
		}
	}

	public static final boolean canInvite(Player inviter, Player invited) {
		if (inviter.isInInstance()) {
			if (AutoGroupService.getInstance().isAutoInstance(inviter.getInstanceId())) {
				PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_CANT_INVITE_PARTY_COMMAND);
				return false;
			}
		}
		if (invited.isInInstance()) {
			if (AutoGroupService.getInstance().isAutoInstance(invited.getInstanceId())) {
				PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_CANT_INVITE_PARTY_COMMAND);
				return false;
			}
		}
		return RestrictionsManager.canInviteToGroup(inviter, invited);
	}

	@GlobalCallback(PlayerGroupCreateCallback.class)
	public static final PlayerGroup createGroup(Player leader, Player invited, TeamType type) {
		PlayerGroup newGroup = new PlayerGroup(new PlayerGroupMember(leader), type);
		groups.put(newGroup.getTeamId(), newGroup);
		addPlayer(newGroup, leader);
		addPlayer(newGroup, invited);
		if (offlineCheckStarted.compareAndSet(false, true)) {
			initializeOfflineCheck();
		}
		return newGroup;
	}

	private static void initializeOfflineCheck() {
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new OfflinePlayerChecker(), 1000, 30 * 1000);
	}

	@GlobalCallback(AddPlayerToGroupCallback.class)
	public static final void addPlayerToGroup(PlayerGroup group, Player invited) {
		group.addMember(new PlayerGroupMember(invited));
	}

	/**
	 * Change group's loot rules and notify team members
	 */
	public static final void changeGroupRules(PlayerGroup group, LootGroupRules lootRules) {
		group.onEvent(new ChangeGroupLootRulesEvent(group, lootRules));
	}

	/**
	 * Player entered world - search for non expired group
	 */
	public static final void onPlayerLogin(Player player) {
		for (PlayerGroup group : groups.values()) {
			PlayerGroupMember member = group.getMember(player.getObjectId());
			if (member != null) {
				group.onEvent(new PlayerConnectedEvent(group, player));
			}
		}
	}

	/**
	 * Player leaved world - set last online on member
	 */
	public static final void onPlayerLogout(Player player) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group != null) {
			PlayerGroupMember member = group.getMember(player.getObjectId());
			member.updateLastOnlineTime();
			group.onEvent(new PlayerDisconnectedEvent(group, player));
		}
	}

	/**
	 * Update group members to some event of player
	 */
	public static final void updateGroup(Player player, GroupEvent groupEvent) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group != null) {
			group.onEvent(new PlayerGroupUpdateEvent(group, player, groupEvent));
		}
	}

	/**
	 * Add player to group
	 */
	public static final void addPlayer(PlayerGroup group, Player player) {
		Preconditions.checkNotNull(group, "Group should not be null");
		group.onEvent(new PlayerEnteredEvent(group, player));
	}

	/**
	 * Remove player from group (normal leave, or kick offline player)
	 */
	public static final void removePlayer(Player player) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group != null) {
			group.onEvent(new PlayerGroupLeavedEvent(group, player));
		}
	}

	/**
	 * Remove player from group (ban)
	 */
	public static final void banPlayer(Player bannedPlayer, Player banGiver) {
		Preconditions.checkNotNull(bannedPlayer, "Banned player should not be null");
		Preconditions.checkNotNull(banGiver, "Bangiver player should not be null");
		PlayerGroup group = banGiver.getPlayerGroup2();
		if (group != null) {
			if (group.hasMember(bannedPlayer.getObjectId())) {
				group.onEvent(new PlayerGroupLeavedEvent(group, bannedPlayer, LeaveReson.BAN, banGiver.getName()));
			}
			else {
				log.warn("TEAM2: banning player not in group {}", group.onlineMembers());
			}
		}
	}

	/**
	 * Disband group by removing all players one by one
	 */
	@GlobalCallback(PlayerGroupDisbandCallback.class)
	public static void disband(PlayerGroup group) {
		Preconditions.checkState(group.onlineMembers() <= 1, "Can't disband group with more than one online member");
		groups.remove(group.getTeamId());
		group.onEvent(new GroupDisbandEvent(group));
	}

	/**
	 * Share specific amount of kinah between group members
	 */
	public static void distributeKinah(Player player, long kinah) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group != null) {
			group.onEvent(new TeamKinahDistributionEvent<PlayerGroup>(group, player, kinah));
		}
	}

	/**
	 * Show specific mark on top of player
	 */
	public static void showBrand(Player player, int targetObjId, int brandId) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group != null) {
			group.onEvent(new ShowBrandEvent<PlayerGroup>(group, targetObjId, brandId));
		}
	}

	public static void changeLeader(Player player) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group != null) {
			group.onEvent(new ChangeGroupLeaderEvent(group, player));
		}
	}

	/**
	 * Start mentoring in group
	 */
	public static void startMentoring(Player player) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group != null) {
			group.onEvent(new PlayerStartMentoringEvent(group, player));
		}
	}

	/**
	 * Stop mentoring in group
	 */
	public static void stopMentoring(Player player) {
		PlayerGroup group = player.getPlayerGroup2();
		if (group != null) {
			group.onEvent(new PlayerGroupStopMentoringEvent(group, player));
		}
	}

	public static final void cleanup() {
		log.info(getServiceStatus());
		groups.clear();
	}

	public static final String getServiceStatus() {
		return "Number of groups: " + groups.size();
	}

	public static final PlayerGroup searchGroup(Integer playerObjId) {
		for (PlayerGroup group : groups.values()) {
			if (group.hasMember(playerObjId)) {
				return group;
			}
		}
		return null;
	}

	public static class OfflinePlayerChecker implements Runnable, Predicate<PlayerGroupMember> {

		private PlayerGroup currentGroup;

		@Override
		public void run() {
			for (PlayerGroup group : groups.values()) {
				currentGroup = group;
				group.apply(this);
			}
			currentGroup = null;
		}

		@Override
		public boolean apply(PlayerGroupMember member) {
			if (!member.isOnline() && TimeUtil.isExpired(member.getLastOnlineTime() + GroupConfig.GROUP_REMOVE_TIME * 1000)) {
				// TODO LEAVE_TIMEOUT type
				currentGroup.onEvent(new PlayerGroupLeavedEvent(currentGroup, member.getObject()));
			}
			return true;
		}
	}

}