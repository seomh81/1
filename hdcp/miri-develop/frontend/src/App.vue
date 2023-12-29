<script setup>
import { AppBar, Spiner } from "./component/Template";
import { useRoute } from "vue-router";
import { uiStore } from "@/store/UiStore";
import AlramSideBar from "@/component/AlramSideBar.vue";

const useUiStore = uiStore();
const route = useRoute();
</script>

<template>
  <v-app class="app" :class="`${useUiStore.font2 ? 'font2' : ''}`">
    <v-layout>
      <AlramSideBar></AlramSideBar>

      <AppBar v-if="route.path !== '/' && route.path !== '/signUp'" />
      <v-main>
        <router-view />
      </v-main>
    </v-layout>
  </v-app>
  <teleport to="#extra-modal" :disabled="false">
    <Spiner />
  </teleport>
</template>

<style lang="scss">
// jodit

.Vue-Toastification__toast--warning {
  color: #5f5f5f;
}
</style>
