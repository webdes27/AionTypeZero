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

package quest.heiron;

import org.typezero.gameserver.model.gameobjects.Npc;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.questEngine.handlers.QuestHandler;
import org.typezero.gameserver.model.DialogAction;
import org.typezero.gameserver.questEngine.model.QuestEnv;
import org.typezero.gameserver.questEngine.model.QuestState;
import org.typezero.gameserver.questEngine.model.QuestStatus;

/**
 * @author Balthazar
 */

public class _1626LightThePath extends QuestHandler {

	private final static int questId = 1626;

	public _1626LightThePath() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204592).addOnQuestStart(questId);
		qe.registerQuestNpc(204592).addOnTalkEvent(questId);
		qe.registerQuestNpc(700221).addOnTalkEvent(questId);
		qe.registerQuestNpc(700222).addOnTalkEvent(questId);
		qe.registerQuestNpc(700223).addOnTalkEvent(questId);
		qe.registerQuestNpc(700224).addOnTalkEvent(questId);
		qe.registerQuestNpc(700225).addOnTalkEvent(questId);
		qe.registerQuestNpc(700226).addOnTalkEvent(questId);
		qe.registerQuestNpc(700227).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204592) {
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 4762);
					}
					case QUEST_ACCEPT_1: {
						if (player.getInventory().getItemCountByItemId(182201788) == 0) {
							if (!giveQuestItem(env, 182201788, 1)) {
								return true;
							}
						}
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null)
			return false;

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 700221: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 0, 1, false, 0); // 1
							}
						}
					}
					break;
				}
				case 700222: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 1, 2, false, 0); // 2
							}
						}
					}
					break;
				}
				case 700223: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 2, 3, false, 0); // 3
							}
						}
					}
				}
				case 700224: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 3, 4, false, 0); // 4
							}
						}
					}
				}
				case 700225: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 4, 5, false, 0); // 5
							}
						}
					}
				}
				case 700226: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 5, 6, false, 0); // 6
							}
						}
					}
				}
				case 700227: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 6, 6, true, 0); // reward
							}
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204592) {
				if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id())
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
