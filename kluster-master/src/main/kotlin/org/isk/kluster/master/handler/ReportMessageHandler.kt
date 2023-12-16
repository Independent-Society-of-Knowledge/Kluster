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

package org.isk.kluster.master.handler

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.websocket.Session
import org.isk.kluster.base.*
import org.isk.kluster.base.message.Message
import org.isk.kluster.base.message.SystemStatusReportMessage
import org.isk.kluster.master.SlaveSessionService

@ApplicationScoped
class ReportMessageHandler : MessageHandler {
    @Inject
    private lateinit var sessionService: SlaveSessionService
    override fun handle(s: Session, m: Message) {
        sessionService.getIdentifierBySession(s)?.status = m as SystemStatusReportMessage
        logger.debug("[{}] received report message: {}", s.stringFormat(), m)
    }

    override fun canHandle(m: Message): Boolean = m is SystemStatusReportMessage
}