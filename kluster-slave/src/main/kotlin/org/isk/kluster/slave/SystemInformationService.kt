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

import io.quarkus.runtime.util.HashUtil
import jakarta.enterprise.context.ApplicationScoped
import oshi.SystemInfo

@ApplicationScoped
class SystemInformationService {
    private val sys = SystemInfo()

    val machineId: String by lazy { "pid=${sys.hardware.processor.processorIdentifier.processorID}:did=${sys.hardware.diskStores.firstOrNull()?.serial}" }

    val machineIdHash: String by lazy { HashUtil.sha256(machineId).substring(0, 10) }

    val memoryTotal: Long
        get() = sys.hardware.memory.total
    val memoryAvailable: Long
        get() = sys.hardware.memory.available

    val memoryLoad: Double get() = 1.0 - (memoryAvailable.toDouble() / memoryTotal)


    val processorsLoad: DoubleArray =
        sys.hardware.processor.getProcessorCpuLoad(300)
    val processorLoad: Double get() = processorsLoad.average()

    val processorMaxFreq: Long get() = sys.hardware.processor.maxFreq
    val processorsCurrentFreq: LongArray get() = sys.hardware.processor.currentFreq
    val processorCurrentFreq: Double get() = processorsCurrentFreq.average()

    val cpuTemperature: Double?
        get() = try {
            sys.hardware.sensors.cpuTemperature
        } catch (e: Throwable) {
            null
        }
    val cpuFanSpeed: IntArray?
        get() = try {
            sys.hardware.sensors.fanSpeeds
        } catch (e: Throwable) {
            null
        }
}

fun main() {

}