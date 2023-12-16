<!--
  - Copyright (c) 2023, , Independent Society of Knowledge.
  - All rights reserved.
  -
  - Author: Human Ardaki
  - Contact: humanardaki@gmail.com
  -
  - This code is the intellectual property of the Independent Society of Knowledge,
  - a community of scientists and researchers in different areas. Any unauthorized use,
  - reproduction, or distribution of this code or any portion thereof is strictly
  - prohibited. Permission to use, copy, modify, or distribute this software for any
  - purpose is hereby granted, provided that the above copyright notice and this
  - permission notice appear in all copies and that the name of the Independent Society
  - of Knowledge not be used in advertising or publicity pertaining to distribution
  - of the software without specific, written prior permission.
  -
  - THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  - IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  - FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
  - OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
  - IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
  - WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
  -->

<template>
  <n-card>
    <p class="text-lg font-semibold mb-7 w-full">Time: {{Date(agData.time)}}</p>
    <div class="grid grid-cols-2 md:grid-cols-5 gap-3 gap-y-8">
      <n-table class="text-lg table table-auto col-span-2  min-h-[8rem] min-w-[10rem]">
        <n-tr>
          <n-td>CPU Temperature</n-td>
          <n-td>{{ agData.cpuTemperature }} °C</n-td>
          <n-td>
            <n-icon v-if="agData.cpuTemperature < 60" size="30" color="green"><checkmark-filled/></n-icon>
            <n-icon v-else-if="agData.cpuTemperature < 80" size="30" color="orange"><warning-filled/></n-icon>
            <n-icon v-else size="30" color="red"><close-filled/></n-icon>
          </n-td>
        </n-tr>
        <n-tr>
          <n-td>CPU Fan Speed</n-td>
          <n-td>{{ agData.cpuFanSpeed }} RPM</n-td>
          <n-td>
            <n-icon v-if="agData.cpuFanSpeed < 3000" size="30" color="green"><checkmark-filled/></n-icon>
            <n-icon v-else-if="agData.cpuFanSpeed < 5000" size="30" color="orange"><warning-filled/></n-icon>
            <n-icon v-else size="30" color="red"><close-filled/></n-icon>
          </n-td>
        </n-tr>
      </n-table>
      <LoadIndicator class="col-span-1 w-full" title="CPU Load" :percentage="parseFloat(agData.cpuLoad.toFixed(2))"/>
      <LoadIndicator class="col-span-1 w-full" title="CPU Freq"
                     :percentage="parseFloat((agData.cpuFreq*100/agData.cpuMaxFreq).toFixed(2))"/>
      <LoadIndicator class="col-span-1 w-full" title="Memory Load" :percentage="parseFloat(agData.memoryLoad.toFixed(2))"/>
    </div>
  </n-card>
</template>
<script setup lang="js">
import {computed} from "vue";
import _ from "lodash"
import LoadIndicator from "../../components/LoadIndicator.vue";
import {CheckmarkFilled, WarningFilled, CloseFilled } from "@vicons/carbon"

const props = defineProps(['data'])
const agData = computed(() => {
  const data = props.data.map(it => it.status)
  const time = _.max(data.map(it => Date.parse(it.time)))
  const cpuLoad = (_.sum(data.map(it => it.cpu.processorLoad)) / data.length) * 100
  const memoryLoad = (_.sum(data.map(it => it.memory.load)) / data.length) * 100
  const cpuFreq = _.sum(data.map(it => it.cpu.currentFreq)) / data.length
  const cpuMaxFreq = _.sum(data.map(it => it.cpu.maxFreq)) / data.length
  const cpuFanSpeed = _.sum(data.map(it => _.sum(it.sensor.cpuFanSpeed) / it.sensor.cpuFanSpeed.length)) / data.length
  const cpuTemperature = _.sum(data.map(it => it.sensor.cpuTemperature)) / data.length
  return {
    time,
    cpuLoad,
    memoryLoad,
    cpuFreq,
    cpuMaxFreq,
    cpuFanSpeed,
    cpuTemperature
  }
})

</script>
<style scoped>

</style>