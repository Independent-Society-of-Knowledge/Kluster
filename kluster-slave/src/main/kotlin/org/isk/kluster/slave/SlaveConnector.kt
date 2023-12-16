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

package org.isk.kluster.slave

import io.quarkus.scheduler.Scheduled
import io.vertx.mutiny.uritemplate.UriTemplate
import io.vertx.mutiny.uritemplate.Variables
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.websocket.ContainerProvider
import jakarta.websocket.Session
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.isk.kluster.base.MessageHandler
import org.isk.kluster.base.message.Message
import org.isk.kluster.base.logger
import org.isk.kluster.base.message.objectMapper
import java.net.URI


@ApplicationScoped
class SlaveConnector {


    @ConfigProperty(name = "isk.kluster.connect.url")
    private lateinit var masterURL: String

    @ConfigProperty(name = "isk.kluster.connect.token")
    private lateinit var masterToken: String


    @Inject
    private lateinit var systemInformationService: SystemInformationService


    var session: Session? = null

    val isReady: Boolean
        get() = session?.isOpen ?: false

    @Scheduled(every = "10s")
    fun onCreated() {
       try {
           if (session == null || !session!!.isOpen) {
               tryCloseSession()
               session = ContainerProvider.getWebSocketContainer().connectToServer(Client::class.java, createURI())
           }
       }catch (e: Throwable){
           logger.warn("connection could not be established", e)
       }
    }

    private fun createURI(): URI = URI.create(
        UriTemplate.of("${masterURL}/api/v1/ws/slave/{id}/{token}")
            .expandToString(
                Variables.variables()
                    .set("id", systemInformationService.machineIdHash)
                    .set("token", masterToken)
            )
    )

    fun tryCloseSession() {
        try {
            session?.close()
        } catch (e: Throwable) {
            logger.warn("could not close session", e)
        }
    }

    fun sendMessage(m: Message) {
        if (session == null)
            throw IllegalStateException("no initialized yet")
        session?.basicRemote?.sendText(objectMapper.writeValueAsString(m))
    }
}

