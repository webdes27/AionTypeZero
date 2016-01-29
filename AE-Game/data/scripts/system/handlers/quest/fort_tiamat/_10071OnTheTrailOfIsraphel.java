package quest.fort_tiamat;

import org.typezero.gameserver.model.DialogAction;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.questEngine.handlers.QuestHandler;
import org.typezero.gameserver.questEngine.model.QuestEnv;
import org.typezero.gameserver.questEngine.model.QuestState;
import org.typezero.gameserver.questEngine.model.QuestStatus;


/**
 * @author Romanz
 *
 */
public class _10071OnTheTrailOfIsraphel extends QuestHandler {

	private final static int questId = 10071;

	public _10071OnTheTrailOfIsraphel() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 205579, 205535, 730465, 205987};
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 205579) {
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					}
					case SETPRO1: {
						return defaultCloseDialog(env, 0, 1); // 1
					}
				}
			}
			else if (targetId == 205535) {
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					}
					case SETPRO2: {
						if (giveQuestItem(env, 182213242, 1))
						return defaultCloseDialog(env, 1, 2);
					}
				}
			}
			else if (targetId == 205987) {
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 4)
							return sendQuestDialog(env, 2375);
						else if (var == 5)
							return sendQuestDialog(env, 2716);
					}
					case SETPRO3: {
						removeQuestItem(env, 182213242, 1);
						return defaultCloseDialog(env, 2, 3);
						}
					case SETPRO5: {
						//remove course
						return defaultCloseDialog(env, 4, 5);
						}
					case SET_SUCCEED: {
						giveQuestItem(env, 182213243, 1);
						return defaultCloseDialog(env, 5, 6, true, false);
					}
				}
			}
			else if (targetId == 730465) { // Mysterious Orb
				switch (dialog) {
					case USE_OBJECT: {
						if (var == 3) {
							//apply course
							changeQuestStep(env, 3, 4, false);
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205535) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10070);
	}
}
