<script setup>
import { useRouter, useRoute } from "vue-router";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const useUserStore = userStore();

const router = useRouter();
</script>
<template>
  <v-list bg-color="transparent" class="menuCard">
    <v-list-item class="py-5 px-4">
      <v-avatar rounded="lg" class="mr-3">
        <v-icon icon="mdi-account-circle" size="38"></v-icon>
      </v-avatar>

      {{ useUserStore.userInfo?.userId }}
    </v-list-item>
    <v-divider class="pt-5"></v-divider>
    <v-list-item class="menuBox" @click="router.push('/setting/info')">
      <v-icon class="menuBoxIcon">mdi-account-outline</v-icon>
      <span>{{ t("MENU_SETTING_INFO") }}</span>
    </v-list-item>

    <v-list-item class="menuBox" @click="router.push('/setting/portfolio')">
      <v-icon class="menuBoxIcon">mdi-folder-information-outline</v-icon>
      <span>{{ t("MENU_SETTING_PORTFOLIO") }}</span>
    </v-list-item>
    <v-list-item class="menuBox" @click="router.push('/setting/contact')">
      <v-icon class="menuBoxIcon">mdi-phone</v-icon>
      <span>{{ t("MENU_SETTING_CONTACT") }}</span>
    </v-list-item>
    <v-list-item class="menuBox" @click="router.push('/setting/service')">
      <v-icon class="menuBoxIcon">mdi-bell-outline</v-icon>
      <span>{{ t("MENU_SETTING_SERVICE") }}</span>
    </v-list-item>
  </v-list>
</template>
<style lang="scss">
.menuCard {

  .menuBox {
    display: flex;
    align-items: center;
    margin: 0px 4px;
    opacity: 0.8;
    border-radius: 10px;
    &:hover {
      transition: 0.3s ease;
      // transform: scale(1.03);
      opacity: 1;
      color: white;
      background-color: rgba(68, 35, 68, 0.7);
      cursor: pointer;
    }
    .menuBoxIcon {
      margin-right: 16px;
    }
  }
}
</style>
