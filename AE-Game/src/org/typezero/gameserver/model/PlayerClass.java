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


package org.typezero.gameserver.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * This enum represent class that a player may belong to.
 *
 * @author Luno
 */
@XmlEnum
public enum PlayerClass {
	WARRIOR(0, true),
	GLADIATOR(1), // fighter
	TEMPLAR(2), // knight
	SCOUT(3, true),
	ASSASSIN(4),
	RANGER(5),
	MAGE(6, true),
	SORCERER(7), // wizard
	SPIRIT_MASTER(8), // elementalist
	PRIEST(9, true),
	CLERIC(10),
	CHANTER(11),
	ENGINEER(12, true),
	RIDER(13),
	GUNNER(14),
	ARTIST(15, true),
	BARD(16),
	ALL(17);


	/** This id is used on client side */
	private byte classId;

	/** This is the mask for this class id, used with bitwise AND in arguments that contain more than one possible class */
	private int idMask;

	/** Tells whether player can create new character with this class */
	private boolean startingClass;

	private PlayerClass(int classId) {
		this(classId, false);
	}

	private PlayerClass(int classId, boolean startingClass) {
		this.classId = (byte) classId;
		this.startingClass = startingClass;
		this.idMask = (int) Math.pow(2, classId);
	}

	/**
	 * Returns client-side id for this PlayerClass
	 *
	 * @return classID
	 */
	public byte getClassId() {
		return classId;
	}

	/**
	 * Returns <tt>PlayerClass</tt> object correlating with given classId.
	 *
	 * @param classId
	 *          - id of player class
	 * @return PlayerClass objects that matches the given classId. If there isn't any objects that matches given id, then
	 *         <b>IllegalArgumentException</b> is being thrown.
	 */
	public static PlayerClass getPlayerClassById(byte classId) {
		for (PlayerClass pc : values()) {
			if (pc.getClassId() == classId)
				return pc;
		}

		throw new IllegalArgumentException("There is no player class with id " + classId);
	}

	/**
	 * @return true if this is one of starting classes ( player can create char with this class )
	 */
	public boolean isStartingClass() {
		return startingClass;
	}

	/**
	 * @param pc
	 * @return starting class for second class
	 */
	public static PlayerClass getStartingClassFor(PlayerClass pc) {
		// TODO: remove that shit, we already have everything in the enum itself!
		switch (pc) {
			case ASSASSIN:
			case RANGER:
				return SCOUT;
			case GLADIATOR:
			case TEMPLAR:
				return WARRIOR;
			case CHANTER:
			case CLERIC:
				return PRIEST;
			case SORCERER:
			case SPIRIT_MASTER:
				return MAGE;
			case GUNNER:
			case RIDER:
				return ENGINEER;
			case BARD:
				return ARTIST;
			case SCOUT:
			case WARRIOR:
			case PRIEST:
			case MAGE:
			case ENGINEER:
			case ARTIST:
				return pc;
			default:
				throw new IllegalArgumentException("Given player class is starting class: " + pc);
		}
	}

	public static PlayerClass getPlayerClassByString(String fieldName) {
		for (PlayerClass pc : values()) {
			if (pc.toString().equals(fieldName))
				return pc;
		}
		return null;
	}

	public int getMask() {
		return idMask;
	}
}
