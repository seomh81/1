<script setup>
import { useI18n } from "vue-i18n";
const props = defineProps(["text", "width", "disabled"]);
const emits = defineEmits(["update:modelValue", "btnEvent"]);
const { t } = useI18n();
</script>

<template>
  <v-dialog
    persistent
    class="modal-container"
    no-click-animation
    :width="props.width ? props.width : '400px'"
  >
    <v-card class="customCard">
      <v-toolbar compact="density" color="transparent">
        <v-toolbar-title>{{ text }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn
          rounded="xl"
          icon
          dark
          @click="emits('update:modelValue', false)"
        >
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-toolbar>
      <v-divider></v-divider>

      <div class="py-4 px-8">
        <slot></slot>
      </div>
      <v-divider></v-divider>
      <div class="my-4 px-8 text-end">
        <v-btn
          class="text-white customBtn mr-0"
          color="primary"
          rounded="lg"
          size="large"
          :disabled="props.disabled"
          @click="emits('btnEvent')"
        >
          {{ t("OK") }}</v-btn
        >
        <v-btn
          class="customBtn"
          rounded="lg"
          size="large"
          color="cancle"
          :disabled="props.disabled"
          @click="emits('update:modelValue', false)"
        >
          {{ t("CANCLE") }}</v-btn
        >
      </div>
    </v-card>
  </v-dialog>
</template>

<style scoped>
.modal-container {
  z-index: 10;
  position: fixed;
  width: 100vw;
  height: 100vh;
  top: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
}
.fixedToolbar {
  width: 100%;
  position: fixed;
  top: 0;
}
.childModal {
  z-index: 5000001;
}
.modal-inbox {
  max-height: 95vh;
  position: absolute;
  min-width: 300px;
  width: auto;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  overflow-y: auto;
}

.inner-box {
  margin: 10px;
  padding: 20px;
  padding-bottom: 10px;
  /* border: 1px solid #bbb; */
  border-radius: 3px;
}
.fade-enter-from {
  opacity: 0.4;
}
.fade-enter-to {
  opacity: 1;
}
.fade-enter-active {
  transition: all 0.3s ease;
}
.fade-leave-from {
  opacity: 1;
}
.fade-leave-to {
  opacity: 0;
}
.fade-leave-active {
  transition: all 0.3s ease;
}
</style>
