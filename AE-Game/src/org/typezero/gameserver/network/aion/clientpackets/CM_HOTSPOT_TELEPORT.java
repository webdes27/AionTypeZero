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

package org.typezero.gameserver.network.aion.clientpackets;

import org.typezero.gameserver.dataholders.DataManager;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.model.templates.teleport.HotspotTeleportTemplate;
import org.typezero.gameserver.network.aion.AionClientPacket;
import org.typezero.gameserver.network.aion.AionConnection.State;
import org.typezero.gameserver.services.teleport.TeleportService2;
import org.slf4j.LoggerFactory;


/**
 * @author Alcapwnd
 */
public class CM_HOTSPOT_TELEPORT extends AionClientPacket {

    public int id;
    public int teleportGoal;
    public int kinah;
    public int reqLevel;

    /**
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_HOTSPOT_TELEPORT(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        id = readC();
        teleportGoal = readD();
        kinah = readD();
        reqLevel = readD();

    }


    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (player.getLifeStats().isAlreadyDead()) {
            return;
        }
        HotspotTeleportTemplate teleport = DataManager.HOTSPOT_TELEPORTER_DATA.getHotspotTemplate(teleportGoal);
        if (teleport != null) {
            TeleportService2.teleport(teleport, teleportGoal, player, kinah, reqLevel);
        } else {
            LoggerFactory.getLogger(CM_HOTSPOT_TELEPORT.class).warn("teleportation id " + teleportGoal + " was not found!");
        }

    }

}
