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

package org.typezero.gameserver.world.exceptions;

/**
 * This exception will be thrown when object without set position will be spawned or despawned. This exception
 * indicating error when coder forget to set position but is spawning or despawning object.
 *
 * @author -Nemesiss-
 */
@SuppressWarnings("serial")
public class NotSetPositionException extends RuntimeException {

	/**
	 * Constructs an <code>NotSetPositionException</code> with no detail message.
	 */
	public NotSetPositionException() {
		super();
	}

	/**
	 * Constructs an <code>NotSetPositionException</code> with the specified detail message.
	 *
	 * @param s
	 *          the detail message.
	 */
	public NotSetPositionException(String s) {
		super(s);
	}

	/**
	 * Creates new error
	 *
	 * @param message
	 *          exception description
	 * @param cause
	 *          reason of this exception
	 */
	public NotSetPositionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates new error
	 *
	 * @param cause
	 *          reason of this exception
	 */
	public NotSetPositionException(Throwable cause) {
		super(cause);
	}
}
