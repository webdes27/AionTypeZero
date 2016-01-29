package ai.siege.katalam;

import ai.ActionItemNpcAI2;
import org.typezero.gameserver.ai2.AIName;
import org.typezero.gameserver.model.gameobjects.Npc;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.services.NpcShoutsService;
import org.typezero.gameserver.skillengine.SkillEngine;
import org.typezero.gameserver.utils.PacketSendUtility;

@AIName("buff_parades_elyos")
public class ParadesBuffElyosAI2 extends ActionItemNpcAI2 {


	@Override
	public void handleUseItemFinish(Player player) {
		  Npc owner = getOwner();
		  player.getController().stopProtectionActiveTask();
		  SkillEngine.getInstance().getSkill(getOwner(), 21091, 1, getOwner()).useNoAnimationSkill();
		  SkillEngine.getInstance().getSkill(getOwner(), 12139, 65, player).useNoAnimationSkill();
		  NpcShoutsService.getInstance().sendMsg(getOwner(), 1500686, getObjectId(), 0, 1000);
			PacketSendUtility.sendBrightYellowMessageOnCenter(player, "You blessed Parades Siege.");
		}
}
