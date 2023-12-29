<script setup>
import { computed } from "vue";
import { uiStore } from "@/store/UiStore";
const props = defineProps(["parallelSpiner"]);

</script>

<template>
  <div class="spiner" v-if="parallelSpiner">
    <div class="spiner-content">
      <div class="lds-ellipsis">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
      </div>
    </div>
  </div>
</template>
<style>
.spiner {
  z-index: 10000001;
  position: fixed;
  width: 100vw;
  height: 100vh;
  top: 0;
  left: 0;
}
.spiner-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
