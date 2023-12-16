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

package org.isk.kluster.base.message

import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.Instant
import java.util.*

@SerializableMessage
data class Memory(val available: Long, val total: Long, val load: Double)

@SerializableMessage
data class CPU(
    val processorsLoad: DoubleArray,
    val processorLoad: Double,
    val maxFreq: Long,
    val currentFreqs: LongArray,
    val currentFreq: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CPU

        if (processorLoad != other.processorLoad) return false
        if (maxFreq != other.maxFreq) return false
        if (currentFreq != other.currentFreq) return false

        return true
    }

    override fun hashCode(): Int {
        var result = processorLoad.hashCode()
        result = 31 * result + maxFreq.hashCode()
        result = 31 * result + currentFreq.hashCode()
        return result
    }
}

@SerializableMessage
data class Sensor(val cpuTemperature: Double?, val cpuFanSpeed: IntArray?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sensor

        return cpuTemperature == other.cpuTemperature
    }

    override fun hashCode(): Int {
        return cpuTemperature?.hashCode() ?: 0
    }
}

@RegisterForReflection
@SerializableMessage
data class SystemStatusReportMessage(
    val cpu: CPU,
    val sensor: Sensor,
    val memory: Memory,
    val time: Date = Date.from(Instant.now())
) : Message