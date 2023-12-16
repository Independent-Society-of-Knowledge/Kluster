/*
 * Copyright (c) 2023, , Independent Society of Knowledge.
 * All rights reserved.
 *
 * Author: Human Ardaki
 * Contact: humanardaki@gmail.com
 *
 * This code is the intellectual property of the Independent Society of Knowledge,
 * a community of scientists and researchers in different areas. Any unauthorized use,
 * reproduction, or distribution of this code or any portion thereof is strictly
 * prohibited. Permission to use, copy, modify, or distribute this software for any
 * purpose is hereby granted, provided that the above copyright notice and this
 * permission notice appear in all copies and that the name of the Independent Society
 * of Knowledge not be used in advertising or publicity pertaining to distribution
 * of the software without specific, written prior permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.isk.kluster.master

import jakarta.enterprise.context.ApplicationScoped
import jakarta.websocket.Session
import jakarta.websocket.server.PathParam
import org.isk.kluster.base.message.Message
import org.isk.kluster.base.logger
import org.isk.kluster.base.stringFormat
import java.util.concurrent.ConcurrentHashMap

@ApplicationScoped
class SlaveSessionService {

    val sessions: MutableMap<Session, SlaveIdentifier> = ConcurrentHashMap()

    fun onInitiate(s: Session, @PathParam("id") id: String) {
        logger.info("[{}] initiated connection", s.stringFormat())
        sessions[s] = SlaveIdentifier(id)
    }


    fun onClose(s: Session) {
        sessions.remove(s)
        logger.info("[{}] closing session", s.stringFormat())
        try {
            s.close()
        } catch (e: Throwable) {
            logger.warn("[{}]  closing failed", s.stringFormat(), e)
        }
    }

    fun getIdentifierById(id: String) = sessions.entries.first { it.value.id == id }.value
    fun getSessionById(id: String) = sessions.entries.first { it.value.id == id }.key

    fun sendMessage(m: Message, id: String) {
        getSessionById(id).basicRemote.sendText(m.serialize())
    }

    fun sendMessageToAll(m: Message): Pair<Int, List<Throwable>> {
        val sessionCount = sessions.size
        val errors = sessions
            .keys
            .map { it.asyncRemote.sendText(m.serialize()) }
            .mapNotNull {
                try {
                    it.get()
                    null
                } catch (e: Throwable) {
                    e
                }
            }
            .toList()

        return sessionCount to errors
    }

    fun getIdentifierBySession(s: Session) = sessions[s]
}