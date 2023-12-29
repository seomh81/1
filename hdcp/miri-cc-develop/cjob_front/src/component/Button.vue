<script setup>
import { ref, computed } from "vue";
import { useI18n } from "vue-i18n";
import { useDisplay } from "vuetify";
const props = defineProps(["item"]);
const emit = defineEmits(["update:buttonEvent"]);
</script>
<template>
  <v-btn
    :variant="item?.variant ? item.variant : 'outlined'"
    :disabled="item?.disabled"
    @click="item.event()"
    :size="item?.size"
    :color="item?.color"
  >
    {{ item?.title }}
    <v-icon class="ml-1" v-if="item?.icon">{{ item?.icon }} </v-icon>
  </v-btn>
</template>
