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

package quest.poeta;

import org.typezero.gameserver.model.PlayerClass;
import org.typezero.gameserver.model.gameobjects.Npc;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.questEngine.handlers.QuestHandler;
import org.typezero.gameserver.model.DialogAction;
import org.typezero.gameserver.questEngine.model.QuestEnv;
import org.typezero.gameserver.questEngine.model.QuestState;
import org.typezero.gameserver.questEngine.model.QuestStatus;
import org.typezero.gameserver.services.QuestService;

/**
 * @author MrPoke
 */
public class _1205ANewSkill extends QuestHandler {

	private final static int questId = 1205;

	public _1205ANewSkill() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203087).addOnTalkEvent(questId); // Warrior
		qe.registerQuestNpc(203088).addOnTalkEvent(questId); // Scout
		qe.registerQuestNpc(203089).addOnTalkEvent(questId); // Mage
		qe.registerQuestNpc(203090).addOnTalkEvent(questId); // Priest
		qe.registerQuestNpc(801210).addOnTalkEvent(questId); // Gunner
		qe.registerQuestNpc(801211).addOnTalkEvent(questId); // Bard
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
		if (!lvlCheck)
			return false;
		if (qs != null)
			return false;
		env.setQuestId(questId);
		if (QuestService.startQuest(env)) {
			qs = player.getQuestStateList().getQuestState(questId);
			qs.setStatus(QuestStatus.REWARD);
			PlayerClass playerClass = player.getPlayerClass();
			if (!playerClass.isStartingClass())
				playerClass = PlayerClass.getStartingClassFor(playerClass);
			switch (playerClass) {
				case WARRIOR:
					qs.setQuestVar(1);
					break;
				case SCOUT:
					qs.setQuestVar(2);
					break;
				case MAGE:
					qs.setQuestVar(3);
					break;
				case PRIEST:
					qs.setQuestVar(4);
					break;
				case ENGINEER:
					qs.setQuestVar(5);
					break;
				case ARTIST:
					qs.setQuestVar(6);
					break;
			}
			updateQuestStatus(env);
		}
		return true;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.REWARD)
			return false;

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		PlayerClass playerClass = PlayerClass.getStartingClassFor(player.getCommonData().getPlayerClass());
		switch (targetId) {
			case 203087:
				if (playerClass == PlayerClass.WARRIOR) {
					if (env.getDialog() == DialogAction.USE_OBJECT)
						return sendQuestDialog(env, 1011);
					else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id())
						return sendQuestDialog(env, 5);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 203088:
				if (playerClass == PlayerClass.SCOUT) {
					if (env.getDialog() == DialogAction.USE_OBJECT)
						return sendQuestDialog(env, 1352);
					else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id())
						return sendQuestDialog(env, 6);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 203089:
				if (playerClass == PlayerClass.MAGE) {
					if (env.getDialog() == DialogAction.USE_OBJECT)
						return sendQuestDialog(env, 1693);
					else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id())
						return sendQuestDialog(env, 7);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 203090:
				if (playerClass == PlayerClass.PRIEST) {
					if (env.getDialog() == DialogAction.USE_OBJECT)
						return sendQuestDialog(env, 2034);
					else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id())
						return sendQuestDialog(env, 8);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 801210:
				if (playerClass == PlayerClass.ENGINEER) {
					if (env.getDialog() == DialogAction.USE_OBJECT)
						return sendQuestDialog(env, 2034);
					else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id())
						return sendQuestDialog(env, 45);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
			case 801211:
				if (playerClass == PlayerClass.ARTIST) {
					if (env.getDialog() == DialogAction.USE_OBJECT)
						return sendQuestDialog(env, 2034);
					else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id())
						return sendQuestDialog(env, 46);
					else
						return this.sendQuestEndDialog(env);
				}
				return false;
		}

		return false;
	}
}
