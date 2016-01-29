package quest.morheim;

import org.typezero.gameserver.model.DialogAction;
import org.typezero.gameserver.model.gameobjects.Item;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.questEngine.handlers.HandlerResult;
import org.typezero.gameserver.questEngine.handlers.QuestHandler;
import org.typezero.gameserver.questEngine.model.QuestEnv;
import org.typezero.gameserver.questEngine.model.QuestState;
import org.typezero.gameserver.questEngine.model.QuestStatus;
import org.typezero.gameserver.world.zone.ZoneName;

/**
 * @author Romanz
 */
public class _24021GhostsintheDesert extends QuestHandler {

	private final static int questId = 24021;
	private int itemId = 182215363;

	public _24021GhostsintheDesert() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(itemId, questId);
		qe.registerQuestNpc(204302).addOnTalkEvent(questId);
		qe.registerQuestNpc(204329).addOnTalkEvent(questId);
		qe.registerQuestNpc(802046).addOnTalkEvent(questId);
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 24020, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204302: { // Bragi
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SETPRO1: {
							return defaultCloseDialog(env, 0, 1); // 1
						}
					}
					break;
				}
				case 802046: { // Tauta
					switch (dialog) {
						case QUEST_SELECT: {
							switch (var) {
								case 2: {
									return sendQuestDialog(env, 1693);
								}
								case 3: {
									return sendQuestDialog(env, 10000);
								}
							}
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 2, 3, false, 10000, 10001); // 3
						}
						case SETPRO4: {
							if (!player.getInventory().isFullSpecialCube()) {
								return defaultCloseDialog(env, 3, 4, 182215363, 1, 0, 0); // 4
							}
							else {
								return sendQuestSelectionDialog(env);
							}
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				}
				case 204329: { // Tofa
					switch (dialog) {
						case QUEST_SELECT: {
							switch (var) {
								case 1: {
									return sendQuestDialog(env, 1352);
								}
							}
						}
						case SELECT_ACTION_1353: {
							if (var == 1) {
								playQuestMovie(env, 73);
								return sendQuestDialog(env, 1353);
							}
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 1, 2); // 2
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204329) {  // Tofa
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
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (item.getItemId() == 182215363 && player.isInsideZone(ZoneName.get("DF2_ITEMUSEAREA_Q2032"))) {
				return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 4, true, 88)); // reward
			}
		}
		return HandlerResult.FAILED;
	}
}
