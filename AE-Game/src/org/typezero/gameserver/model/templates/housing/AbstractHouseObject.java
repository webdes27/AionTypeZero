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

package org.typezero.gameserver.model.templates.housing;

import org.typezero.gameserver.model.templates.VisibleObjectTemplate;
import org.typezero.gameserver.model.templates.item.ItemQuality;

import javax.xml.bind.annotation.*;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractHouseObject")
@XmlSeeAlso({ PlaceableHouseObject.class })
public abstract class AbstractHouseObject extends VisibleObjectTemplate {

	@XmlAttribute(name = "talking_distance", required = true)
	protected float talkingDistance;

	@XmlAttribute(required = true)
	protected ItemQuality quality;

	@XmlAttribute(required = true)
	protected HousingCategory category;

	@XmlAttribute(name = "name_id", required = true)
	protected int nameId;

	@XmlAttribute(required = true)
	protected int id;

	@XmlAttribute(name = "can_dye")
	protected boolean canDye;

	@Override
	public int getTemplateId() {
		return id;
	}

	public float getTalkingDistance() {
		return talkingDistance;
	}

	public ItemQuality getQuality() {
		return quality;
	}

	public HousingCategory getCategory() {
		return category;
	}

	public boolean getCanDye() {
		return canDye;
	}

	@Override
	public int getNameId() {
		return nameId;
	}

	@Override
	public String getName() {
		return null;
	}

}
