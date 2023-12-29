<script setup>
import { computed } from "vue";
import { uiStore } from "@/store/UiStore";
const useUiStore = uiStore();
const spiner = computed(() => {
  return useUiStore.spiner;
});
</script>

<template>
  <div class="spiner" v-if="useUiStore.spiner">
    <div class="spiner-content">
      <v-progress-circular
        :size="100"
        :width="7"
        color="red"
        indeterminate
      ></v-progress-circular>
    </div>
  </div>
</template>
<style>
.spiner {
  z-index: 10000000;
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
