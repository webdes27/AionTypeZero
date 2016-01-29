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

package org.typezero.gameserver.taskmanager.fromdb.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.typezero.gameserver.ShutdownHook;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.utils.PacketSendUtility;
import org.typezero.gameserver.utils.ThreadPoolManager;
import org.typezero.gameserver.world.World;
import org.typezero.gameserver.world.knownlist.Visitor;

/**
 * @author Divinity, nrg
 */
public class ShutdownHandler extends TaskFromDBHandler {

	private static final Logger log = LoggerFactory.getLogger(ShutdownHandler.class);
	private int countDown;
	private int announceInterval;
	private int warnCountDown;

	@Override
	public boolean isValid() {
		if (params.length == 3) {
			try {
				countDown = Integer.parseInt(params[0]);
				announceInterval = Integer.parseInt(params[1]);
				warnCountDown = Integer.parseInt(params[2]);

				return true;
			} catch (NumberFormatException e) {
				log.warn("Invalid parameters for ShutdownHandler. Only valid integers allowed - not registered", e);
			}
		}
		log.warn("ShutdownHandler has more or less than 3 parameters - not registered");
		return false;
	}

	@Override
	public void trigger() {
		log.info("Task[" + taskId + "] launched : shuting down the server !");


		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Automatic Task: The server will shutdown in " + warnCountDown
						+ " seconds ! Please find a peace place and disconnect your character.");
			}
		});

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				ShutdownHook.getInstance().doShutdown(countDown, announceInterval, ShutdownHook.ShutdownMode.SHUTDOWN);
			}
		}, warnCountDown * 1000);
	}
}
