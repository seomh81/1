<script setup>
import {
  onMounted,
  useSlots,
  defineComponent,
  ref,
  computed,
  reactive,
  watch,
} from "vue";
import { uiStore } from "@/store/UiStore";

const useUiStore = uiStore();
</script>

<template>
  <v-container :fluid="useUiStore.reduceView == true ? false : true">
    <v-row>
      <slot></slot>
    </v-row>
  </v-container>
</template>
<style scoped>
@media (min-width: 1280px) {
  .v-container {
    max-width: 80%;
  }
  .v-container--fluid {
    max-width: 100%;
  }
}
@media (min-width: 1920px) {
  .v-container {
    max-width: 80%;
  }
  .v-container--fluid {
    max-width: 100%;
  }
}
</style>
