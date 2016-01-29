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

package org.typezero.gameserver.utils.stats;

import java.util.NoSuchElementException;

/**
 * @author ATracer
 */
public enum XPRewardEnum {
	MINUS_11(-11, 0),
	MINUS_10(-10, 1),
	MINUS_9(-9, 10),
	MINUS_8(-8, 20),
	MINUS_7(-7, 30),
	MINUS_6(-6, 40),
	MINUS_5(-5, 50),
	MINUS_4(-4, 70),
	MINUS_3(-3, 90),
	MINUS_2(-2, 100),
	MINUS_1(-1, 100),
	ZERO(0, 100),
	PLUS_1(1, 105),
	PLUS_2(2, 110),
	PLUS_3(3, 115),
	PLUS_4(4, 120);

	private int xpRewardPercent;

	private int levelDifference;

	private XPRewardEnum(int levelDifference, int xpRewardPercent) {
		this.levelDifference = levelDifference;
		this.xpRewardPercent = xpRewardPercent;
	}

	public int rewardPercent() {
		return xpRewardPercent;
	}

	/**
	 * @param levelDifference
	 *          between two objects
	 * @return XP reward percentage
	 */
	public static int xpRewardFrom(int levelDifference) {
		if (levelDifference < MINUS_11.levelDifference) {
			return MINUS_11.xpRewardPercent;
		}
		if (levelDifference > PLUS_4.levelDifference) {
			return PLUS_4.xpRewardPercent;
		}

		for (XPRewardEnum xpReward : values()) {
			if (xpReward.levelDifference == levelDifference) {
				return xpReward.xpRewardPercent;
			}
		}

		throw new NoSuchElementException("XP reward for such level difference was not found");
	}
}
