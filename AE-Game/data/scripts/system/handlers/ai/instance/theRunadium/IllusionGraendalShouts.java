package ai.instance.theRunadium;

import ai.AggressiveNpcAI2;

import org.typezero.gameserver.ai2.AIName;
import org.typezero.gameserver.services.NpcShoutsService;

import java.util.ArrayList;
import java.util.List;
/**
 * M.O.G. Devs Team
 */

@AIName("illusion_graendal_shount")
public class IllusionGraendalShouts extends AggressiveNpcAI2 {

	private List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleSpawned() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500744, getObjectId(), 0, 3000);
		super.handleSpawned();
	}

	@Override
	protected void handleDespawned() {
		percents.clear();
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		percents.clear();
		super.handleDied();
	}
}
