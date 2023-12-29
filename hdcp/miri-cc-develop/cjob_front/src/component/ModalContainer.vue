<script setup>
import { uiStore } from "@/store/UiStore";
const useUiStore = uiStore();
const props = defineProps(["trigger", "type", "id", "text"]);
const emits = defineEmits(["resetStep"]);
</script>

<template>
  <transition name="fade">
    <div class="modal-container" v-if="useUiStore.openModal === props.id">
      <v-card class="modal-inbox rounded-lg">
        <v-toolbar dense dark color="indigo-lighten-5" height="50">
          <v-card-subtitle class="title">{{ text }}</v-card-subtitle>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn
              icon
              dark
              @click="[useUiStore.closeModal(), emits('resetStep')]"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <div class="inner-box">
          <slot></slot>
        </div>
      </v-card>
    </div>
  </transition>

  <transition name="fade">
    <div
      class="modal-container childModal"
      v-if="props.type == 'child' && useUiStore.childModal === props.id"
    >
      <v-card class="modal-inbox rounded-lg">
        <v-toolbar dense dark color="indigo-lighten-5" height="50">
          <v-card-subtitle class="title">{{ text }}</v-card-subtitle>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn
              icon
              dark
              @click="[useUiStore.closeChildModal(), emits('resetStep')]"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <div class="inner-box">
          <slot></slot>
        </div>
      </v-card>
    </div>
  </transition>
</template>

<style scoped>
.modal-container {
  z-index: 1000000;
  position: fixed;
  width: 100vw;
  height: 100vh;
  top: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
}
.childModal {
  z-index: 5000001;
}
.modal-inbox {
  position: absolute;
  min-width: 300px;
  width: auto;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  overflow-y: auto;
}
.title {
  margin-top: 4px;
  font-weight: bold;
  font-size: 1.2em;
  color: #333;
  opacity: 1;
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
