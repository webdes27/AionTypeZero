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

package org.typezero.gameserver.model.templates.spawns.beritraspawns;

import org.typezero.gameserver.model.beritra.BeritraStateType;
import org.typezero.gameserver.model.templates.spawns.Spawn;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BeritraSpawn")
public class BeritraSpawn
{
	@XmlAttribute(name = "id")
	private int id;

	public int getId() {
		return id;
	}

	@XmlElement(name = "beritra_type")
	private List<BeritraSpawn.BeritraStateTemplate> BeritraStateTemplate;

	public List<BeritraStateTemplate> getSiegeModTemplates() {
		return BeritraStateTemplate;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "BeritraStateTemplate")
	public static class BeritraStateTemplate {

		@XmlElement(name = "spawn")
		private List<Spawn> spawns;

		@XmlAttribute(name = "bstate")
		private BeritraStateType beritraType;

		public List<Spawn> getSpawns() {
			return spawns;
		}

		public BeritraStateType getBeritraType() {
			return beritraType;
		}
	}
}
