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
  <div class="flex flex-col justify-center items-center w-fit h-fit gap-3">
    <p class="text-lg font-bold">{{props.title}}</p>
    <n-progress
        style="margin: 0 8px 12px 0"
        type="circle"
        :percentage="props.percentage"
    />
  </div>
</template>
<script setup lang="ts">
const props = defineProps<{title: string, percentage: number}>()
</script>
<style scoped>

</style>