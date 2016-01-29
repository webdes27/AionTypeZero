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

package ai.portals;

import org.typezero.gameserver.ai2.AIName;
import org.typezero.gameserver.services.MuiService;
import org.typezero.gameserver.utils.PacketSendUtility;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.model.siege.SourceLocation;
import org.typezero.gameserver.services.SiegeService;


/**
 * @author Cheatkiller
 *
 */
@AIName("tiamarantaportal")
public class TiamarantaPortalsAI2 extends PortalAI2 {


	private boolean checkSourceCount(Player player) {
		int count = 0;
		for (final SourceLocation source : SiegeService.getInstance().getSources().values()) {
			if (source.getRace().getRaceId() == player.getRace().getRaceId()) {
				count++;
			}
		}
		if (count >= 2) {
			return true;
		}
		PacketSendUtility.sendBrightYellowMessageOnCenter(player, MuiService.getInstance().getMessage("T_PORTAL"));
		return false;
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		if (checkSourceCount(player)) {
			super.handleUseItemFinish(player);
		}
	}
}
