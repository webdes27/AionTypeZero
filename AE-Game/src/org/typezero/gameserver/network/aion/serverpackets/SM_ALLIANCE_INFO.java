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

package org.typezero.gameserver.network.aion.serverpackets;

import org.apache.commons.lang.StringUtils;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.model.team2.alliance.PlayerAlliance;
import org.typezero.gameserver.model.team2.common.legacy.LootGroupRules;
import org.typezero.gameserver.model.team2.league.LeagueMember;
import org.typezero.gameserver.network.aion.AionConnection;
import org.typezero.gameserver.network.aion.AionServerPacket;

import java.util.Collection;

/**
 * @author Sarynth, xTz
 */
public class SM_ALLIANCE_INFO extends AionServerPacket {

	private LootGroupRules lootRules;
	private PlayerAlliance alliance;
	private int leaderid;
	private int groupid;
	private final int messageId;
	private final String message;
	public static final int VICECAPTAIN_PROMOTE = 1300984;
	public static final int VICECAPTAIN_DEMOTE = 1300985;
	public static final int LEAGUE_ENTERED = 1400560;
	public static final int LEAGUE_LEFT = 1400572;
	public static final int LEAGUE_EXPEL = 1400574;
	public static final int LEAGUE_EXPELLED = 1400576;

	public SM_ALLIANCE_INFO(PlayerAlliance alliance) {
		this(alliance, 0, StringUtils.EMPTY);
	}

	public SM_ALLIANCE_INFO(PlayerAlliance alliance, int messageId, String message) {
		this.alliance = alliance;
		groupid = alliance.getObjectId();
		leaderid = alliance.getLeader().getObjectId();
		lootRules = alliance.getLootGroupRules();
		this.messageId = messageId;
		this.message = message;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		writeH(alliance.groupSize());
		writeD(groupid);
		writeD(leaderid);
		writeD(player.getWorldId());//4.3 mapid
		Collection<Integer> ids = alliance.getViceCaptainIds();
		for (Integer id : ids) {
			writeD(id);
		}
		for (int i = 0; i < 4 - ids.size(); i++) {
			writeD(0);
		}
		writeD(lootRules.getLootRule().getId());
		writeD(lootRules.getMisc());
		writeD(lootRules.getCommonItemAbove());
		writeD(lootRules.getSuperiorItemAbove());
		writeD(lootRules.getHeroicItemAbove());
		writeD(lootRules.getFabledItemAbove());
		writeD(lootRules.getEthernalItemAbove());
		writeD(lootRules.getAutodistribution().getId());
		writeD(0x02);
		writeC(0x00);
		writeD(alliance.getTeamType().getType());
		writeD(alliance.getTeamType().getSubType()); // 3.5
		writeD(alliance.isInLeague() ? alliance.getLeague().getTeamId() : 0);
		for (int a = 0; a < 4; a++) {
			writeD(a); // group num
			writeD(1000 + a); // group id
		}
		writeD(messageId); // System message ID
		writeS(messageId != 0 ? message : StringUtils.EMPTY); // System message
		if (alliance.isInLeague()) {
			lootRules = alliance.getLeague().getLootGroupRules();
			writeH(alliance.getLeague().size());
			writeD(lootRules.getLootRule().getId()); // loot rule type - 0 freeforall, 1 roundrobin, 2 leader
			writeD(lootRules.getAutodistribution().getId()); // autoDistribution - 0 or 1
			writeD(lootRules.getCommonItemAbove()); // this.common_item_above); - 0 normal 2 roll 3 bid
			writeD(lootRules.getSuperiorItemAbove()); // this.superior_item_above); - 0 normal 2 roll 3 bid
			writeD(lootRules.getHeroicItemAbove()); // this.heroic_item_above); - 0 normal 2 roll 3 bid
			writeD(lootRules.getFabledItemAbove()); // this.fabled_item_above); - 0 normal 2 roll 3 bid
			writeD(lootRules.getEthernalItemAbove()); // this.ethernal_item_above); - 0 normal 2 roll 3 bid
			writeD(0); // this.over_ethernal); - 0 normal 2 roll 3 bid
			writeD(0); // this.over_over_ethernal); - 0 normal 2 roll 3 bid
			for (LeagueMember leagueMember : alliance.getLeague().getSortedMembers()) {
				writeD(leagueMember.getLeaguePosition());
				writeD(leagueMember.getObjectId());
				writeD(leagueMember.getObject().size());
				writeS(leagueMember.getObject().getLeaderObject().getName());
			}
		}
	}

}
