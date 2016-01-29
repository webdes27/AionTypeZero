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

package admincommands;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.typezero.gameserver.model.gameobjects.Creature;
import org.typezero.gameserver.model.gameobjects.VisibleObject;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.model.gameobjects.state.CreatureState;
import org.typezero.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import org.typezero.gameserver.utils.PacketSendUtility;
import org.typezero.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Rolandas
 */
public class State extends AdminCommand {

	public State() {
		super("state");
	}

	static final Map<Integer, CreatureState> creatureStateLookup = new HashMap<Integer, CreatureState>();
	static final Map<Integer, TestState> testStateLookup = new HashMap<Integer, TestState>();

	static {
		for (CreatureState s : EnumSet.allOf(CreatureState.class))
			creatureStateLookup.put(s.getId(), s);
		for (TestState t : EnumSet.allOf(TestState.class))
			testStateLookup.put(t.id, t);
	}

	@Override
	public void execute(Player admin, String... params) {
		VisibleObject target = admin.getTarget();

		if (target == null) {
			PacketSendUtility.sendMessage(admin, "Select a target first!!!");
			return;
		}

		if (params == null || params.length == 0) {
			PacketSendUtility.sendMessage(admin, "syntax //state <show | set | unset>");
			return;
		}

		if (!(target instanceof Creature)) {
			PacketSendUtility.sendMessage(admin, "You can select only creatures!!!");
			return;
		}

		Creature creature = (Creature) target;

		if (params[0].equals("show")) {
			if (params.length != 1) {
				PacketSendUtility.sendMessage(admin, "syntax //state show");
				return;
			}

			if (creature.equals(admin))
				PacketSendUtility.sendMessage(admin, "Your state is : " + creature.getState() + "\n"
					+ getStateDescription((short) admin.getState()));
			else
				PacketSendUtility.sendMessage(admin, "Creature state is : " + creature.getState() + "\n"
					+ getStateDescription((short) creature.getState()));
		}
		else if (params[0].equals("set") || params[0].equals("unset")) {
			if (params.length != 2) {
				PacketSendUtility.sendMessage(admin, "syntax //state set <bit number>");
				return;
			}
			int number;
			try {
				number = Integer.valueOf(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "syntax //state set <bit number>");
				return;
			}

			if (number < 1 || number > 16) {
				PacketSendUtility.sendMessage(admin, "syntax <bit number> should be in range 1-16");
				return;
			}

			short newState = 0;

			if (params[0].equals("set")) {
				newState = (short) ((creature.getState() & 0xFFFF) | 1 << (number - 1));
			}
			else {
				newState = (short) ((creature.getState() & 0xFFFF) & ~(1 << (number - 1)));
			}

			PacketSendUtility.sendMessage(admin, "New state : " + newState);
			creature.setState(newState);

			if (target.equals(admin))
				PacketSendUtility.sendPacket(admin, new SM_PLAYER_INFO(admin, false));

			admin.clearKnownlist();
			admin.updateKnownlist();

			PacketSendUtility.sendMessage(admin, "State changed to : " + creature.getState() + "\n"
				+ getStateDescription((short) creature.getState()));
		}
		else
			PacketSendUtility.sendMessage(admin, "syntax //state <show | set | unset>");
	}

	@Override
	public void onFail(Player player, String message) {
	}

	String getStateDescription(short state) {
		StringBuilder binsb = new StringBuilder(Integer.toBinaryString(state));
		StringBuilder bin = binsb.reverse();

		StringBuilder sb = new StringBuilder();
		sb.append("{\n");

		for (int i = 0; i < bin.length(); i++) {
			if (bin.charAt(i) == '1') {
				sb.append("0x");
				int value = 1 << i;
				sb.append(Integer.toHexString(value));

				sb.append(" (");
				sb.append(testStateLookup.get(value).display);
				if (creatureStateLookup.containsKey(value)) {
					sb.append('=');
					sb.append(creatureStateLookup.get(value).toString());
				}

				sb.append("),\n");
			}
		}
		if (sb.lastIndexOf(",\n") == sb.length() - 2)
			sb.setLength(sb.length() - 2);

		sb.append("\n}");
		return sb.toString();
	}

	public enum TestState {
		BIT01(1 << 0, "bit 1"),
		BIT02(1 << 1, "bit 2"),
		BIT03(1 << 2, "bit 3"),
		BIT04(1 << 3, "bit 4"),
		BIT05(1 << 4, "bit 5"),
		BIT06(1 << 5, "bit 6"),
		BIT07(1 << 6, "bit 7"),
		BIT08(1 << 7, "bit 8"),
		BIT09(1 << 8, "bit 9"),
		BIT10(1 << 9, "bit 10"),
		BIT11(1 << 10, "bit 11"),
		BIT12(1 << 11, "bit 12"),
		BIT13(1 << 12, "bit 13"),
		BIT14(1 << 13, "bit 14"),
		BIT15(1 << 14, "bit 15"),
		BIT16(1 << 15, "bit 16");

		int id;
		String display;

		TestState(int value, String s) {
			id = value;
			display = s;
		}
	}

}
