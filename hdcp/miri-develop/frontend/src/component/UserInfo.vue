<script setup>
import { ref, onMounted, reactive } from "vue";

import { useRouter, useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import { connectionStore } from "@/store/ConnectionStore";
import { userStore } from "@/store/UserStore";
import { useToast } from "vue-toastification";
const toast = useToast();
const useConnectionStore = connectionStore();
const useUserStore = userStore();
const i18n = useI18n();
const { t } = useI18n();
const router = useRouter();
const password = reactive({
  old: "",
  new: "",
  reNew: "",
});
const menuOverlay = ref(false);


</script>
<template>
  <v-card>
    <v-menu :close-on-back="false" v-model="menuOverlay">
      <template v-slot:activator="{ props }">
        <v-list density="compact" nav>
          <v-list-item rounded="lg" v-bind="props">
            <v-avatar rounded="lg" class="mr-3" size="small">
              <v-img :src="
                useUserStore.userInfo?.avatarUrl
                  ? useUserStore.userInfo?.avatarUrl
                  : 'https://cdn.vuetifyjs.com/images/john.jpg'
              " alt="John"></v-img>
            </v-avatar>
            {{ useUserStore.userInfo?.userName }}
          </v-list-item>
        </v-list>
      </template>
      <v-card rounded="lg" min-width="330" max-width="330">
        <v-list>
          <v-list-item class="d-flex align-center justify-center text-center py-5">
            <v-avatar size="x-large">
              <v-img :src="
                useUserStore.userInfo?.avatarUrl
                  ? useUserStore.userInfo?.avatarUrl
                  : 'https://cdn.vuetifyjs.com/images/john.jpg'
              " alt="Avatar"></v-img>
            </v-avatar>
            <v-list-item-title class="pt-5" :style="{ fontWeight: '400' }">
              {{ useUserStore.userInfo?.userName }}
            </v-list-item-title>
            <v-list-item-subtitle class="pt-1">{{
              useUserStore.userInfo?.userId
            }}</v-list-item-subtitle>
            <v-list-item-subtitle class="pt-2 pb-1">{{
              useUserStore.userInfo?.phonenumber
              ? useUserStore.userInfo?.phonenumber
              : "010 - 0000 - 0000"
            }}</v-list-item-subtitle>
          </v-list-item>
          <v-divider></v-divider>
        </v-list>
        <v-card class="pa-5 d-flex justify-center align-center">
          <div class="text-center flex-grow-1">
            <v-btn icon @click.prevent="router.push('/setting/info')">
              <v-icon color="blue" size="x-large">mdi-cog-outline</v-icon>
            </v-btn>

            <v-card-subtitle class="pt-1">{{
              t("SETTING")
            }}</v-card-subtitle>
          </div>
          <div class="text-center flex-grow-1">
            <v-btn icon @click="useUserStore.logoutUser()">
              <v-icon color="red" size="x-large">mdi-logout</v-icon>
            </v-btn>

            <v-card-subtitle class="pt-1">{{
              t("LOGOUT")
            }}</v-card-subtitle>
          </div>
        </v-card>
      </v-card>
    </v-menu>
  </v-card>
</template>
