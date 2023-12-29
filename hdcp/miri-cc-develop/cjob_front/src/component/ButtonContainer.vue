<script setup>
import { useI18n } from "vue-i18n";
const props = defineProps(["buttonOptions"]);
const emit = defineEmits(["update:triggerEvent"]);
</script>
<template>
  <v-card-actions class="pr-0 justify-end">
    <v-btn
      v-for="(value, key) in buttonOptions"
      :key="key"
      variant="outlined"
      :disabled="value?.disabled"
      @click="emit('update:triggerEvent', key)"
    >
      {{ value?.title }}
      <v-icon class="ml-1" v-if="value?.icon">{{ value?.icon }} </v-icon>
    </v-btn>
    <!-- <v-btn variant="outlined" @click="emit('addBtnEvent')">
      <v-icon class="mr-1"> mdi-filter-outline </v-icon>

      {{ buttonOptions.title[1] }}
    </v-btn>
    <v-btn
      variant="outlined"
      :disabled="buttonOptions.updateBtnDisabled"
      @click="emit('updateBtnEvent')"
    >
      <v-icon class="mr-1"> mdi-playlist-edit </v-icon>
      {{ buttonOptions.title[2] }}
    </v-btn>
    <v-btn
      variant="outlined"
      :disabled="buttonOptions.removeBtnDisabled"
      @click="emit('removeBtnEvent')"
    >
      <v-icon class="mr-1"> mdi-playlist-remove </v-icon>
      {{ t("STRING_REMOVE") }} -->
    <!-- </v-btn> -->
  </v-card-actions>
</template>
