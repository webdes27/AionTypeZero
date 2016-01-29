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

package org.typezero.gameserver.skillengine.effect;

import org.typezero.gameserver.controllers.observer.ActionObserver;
import org.typezero.gameserver.controllers.observer.AttackCalcObserver;
import org.typezero.gameserver.controllers.observer.AttackShieldObserver;
import org.typezero.gameserver.controllers.observer.ObserverType;
import org.typezero.gameserver.model.gameobjects.Creature;
import org.typezero.gameserver.model.gameobjects.Summon;
import org.typezero.gameserver.skillengine.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sippolo modified by kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProtectEffect")
public class ProtectEffect extends ShieldEffect {

	@Override
	public void startEffect(final Effect effect) {
		AttackShieldObserver asObserver = new AttackShieldObserver(value, this.hitvalue,
			radius, percent, effect, this.hitType, this.getType(), this.hitTypeProb);

		effect.getEffected().getObserveController().addAttackCalcObserver(asObserver);
		effect.setAttackShieldObserver(asObserver, position);

		if (effect.getEffector() instanceof Summon) {
			ActionObserver summonRelease = new ActionObserver(ObserverType.SUMMONRELEASE) {

				@Override
				public void summonrelease() {
					effect.endEffect();
				}

			};
			effect.getEffector().getObserveController().attach(summonRelease);
			effect.setActionObserver(summonRelease, position);
		}
		else {
			ActionObserver death = new ActionObserver(ObserverType.DEATH) {

				@Override
				public void died(Creature creature) {
					effect.endEffect();
				}

			};
			effect.getEffector().getObserveController().attach(death);
			effect.setActionObserver(death, position);
		}

	}

	@Override
	public void endEffect(Effect effect) {
		AttackCalcObserver acObserver = effect.getAttackShieldObserver(position);
		if (acObserver != null)
			effect.getEffected().getObserveController().removeAttackCalcObserver(acObserver);
		ActionObserver aObserver = effect.getActionObserver(position);
		if (aObserver != null)
			effect.getEffector().getObserveController().removeObserver(aObserver);
	}

	/**
	 * shieldType
	 * 0: convertHeal
	 * 1: reflector
	 * 2: normal shield
	 * 8: protect
	 * @return
	 */
	@Override
	public int getType() {
		return 8;
	}
}
