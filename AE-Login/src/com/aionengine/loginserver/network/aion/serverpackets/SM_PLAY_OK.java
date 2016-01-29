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

package com.aionengine.loginserver.network.aion.serverpackets;

import com.aionengine.loginserver.network.aion.AionServerPacket;
import com.aionengine.loginserver.network.aion.LoginConnection;
import com.aionengine.loginserver.network.aion.SessionKey;

/**
 * @author -Nemesiss-
 */
public class SM_PLAY_OK extends AionServerPacket {

    /**
     * playOk1 is part of session key - its used for security purposes [checked at game server side]
     */
    private final int playOk1;
    /**
     * playOk2 is part of session key - its used for security purposes [checked at game server side]
     */
    private final int playOk2;
    private int serverId;

    /**
     * Constructs new instance of <tt>SM_PLAY_OK </tt> packet.
     *
     * @param key session key
     */
    public SM_PLAY_OK(SessionKey key, byte serverId) {
        super(0x07);
        this.playOk1 = key.playOk1;
        this.playOk2 = key.playOk2;
        this.serverId = serverId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(LoginConnection con) {
        writeD(playOk1);
        writeD(playOk2);
        writeC(serverId);
        writeB(new byte[0x0E]);
    }
}
