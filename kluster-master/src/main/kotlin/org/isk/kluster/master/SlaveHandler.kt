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

import jakarta.inject.Inject
import jakarta.websocket.*
import jakarta.websocket.CloseReason.CloseCodes
import jakarta.websocket.server.PathParam
import jakarta.websocket.server.ServerEndpoint
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.isk.kluster.base.*
import org.isk.kluster.base.message.Message
import org.isk.kluster.base.message.objectMapper


@ServerEndpoint("/api/v1/ws/slave/{id}/{token}")
class SlaveHandler {

    @ConfigProperty(name = "isk.cluster.token")
    lateinit var token: String

    @Inject
    private lateinit var messageHandlerService: MessageHandlerService

    @Inject
    private lateinit var slaveSessionService: SlaveSessionService

    @OnOpen
    fun onInitiate(s: Session, @PathParam("id") id: String, @PathParam("token") token: String) {
        if (token != this.token) {
            logger.warn("[${s.stringFormat()}] received token: $token")
            s.close(CloseReason(CloseCodes.VIOLATED_POLICY, "token doesn't match"))
        } else
            slaveSessionService.onInitiate(s, id)
    }

    @OnMessage
    fun onMessage(s: Session, messageString: String) {
        val message = objectMapper.readValue(messageString, Message::class.java)
        messageHandlerService.handleMessage(s, message)
    }

    @OnClose
    fun onClose(s: Session) {
        slaveSessionService.onClose(s)
    }

    @OnError
    fun onError(s: Session, err: Throwable) {
        logger.warn("[${s.stringFormat()}] received error", err)
        onClose(s)
    }

}