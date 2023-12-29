<script setup>
import { useRouter, useRoute } from "vue-router";
import { uiStore } from "@/store/UiStore";
import { ref, computed, watch, onMounted } from "vue";
import { LeftSideBar, ToggleBtn } from "@/component/Template";
import { useDisplay } from "vuetify";
import { useI18n } from "vue-i18n";
import { userStore } from "@/store/UserStore";

const selectedPortfolio = computed({
  get() {
    return useUserStore.portfolio?.selected;
  },
  set(value) {
    useUserStore.setPortfolio({
      selected: value,
    });
  },
});
const useUiStore = uiStore();
const route = useRoute();

const useUserStore = userStore();
const { t } = useI18n();
const router = useRouter();
const menuOverlay = ref(false);
const { mdAndDown } = useDisplay();
const railDrawer = ref(true);

if (!mdAndDown.value) {
  railDrawer.value = true;

  useUiStore.drawerControll(true);
}

watch(
  () => mdAndDown,
  () => {
    if (mdAndDown.value == true) {
      railDrawer.value = false;
      useUiStore.drawerControll(true);
    } else {
      railDrawer.value = true;
      useUiStore.drawerControll(false);
    }
  },
  { deep: true }
);
onMounted(async () => {
  await useUserStore.getUserData();
});
</script>

<template>
  <LeftSideBar v-model="railDrawer" />

  <v-app-bar border="none" class="rootAppBar">
    <template v-slot:prepend>
      <v-app-bar-nav-icon
        v-if="mdAndDown"
        @click="useUiStore.drawerControll(true)"
      ></v-app-bar-nav-icon>

      <v-btn
        variant="text"
        icon="mdi-chevron-left"
        v-if="railDrawer == false && !mdAndDown"
        @click.stop="railDrawer = true"
      ></v-btn>
    </template>
    <v-app-bar-title>
      <div class="d-flex align-center">
        <v-icon size="large" :style="{ opacity: '0.7' }">{{
          route.matched[0]?.meta?.icon
        }}</v-icon>
        <v-card-subtitle :style="{ fontSize: '18px', fontWeight: '600' }">
          {{ route?.meta?.breadcrumb?.value }}
        </v-card-subtitle>
      </div>
    </v-app-bar-title>
    <VSpacer></VSpacer>
    <ToggleBtn></ToggleBtn>

    <v-divider class="ml-3 mr-6" inset vertical></v-divider>

    <div :style="{ width: '250px' }" class="mr-6">
      <v-select
        :label="t('PORTFOLIO')"
        density="compact"
        variant="solo"
        v-model="selectedPortfolio"
        :items="useUserStore?.portfolio?.list"
        item-title="portfolioName"
        return-object
        hide-details
      >
      </v-select>
    </div>

    <v-divider class="mx-3" inset vertical></v-divider>

    <v-menu :close-on-back="false" v-model="menuOverlay">
      <template v-slot:activator="{ props }">
        <v-list bg-color="transparent" density="compact" nav>
          <v-list-item color="text-white" rounded="lg" v-bind="props">
            <v-avatar rounded="lg" class="mr-1">
              <v-icon icon="mdi-account-circle" size="x-large"></v-icon>
            </v-avatar>
            <span color="text-white">
              {{ useUserStore.userInfo?.userName }}
            </span>
          </v-list-item>
        </v-list>
      </template>
      <v-card rounded="lg" min-width="330" max-width="330">
        <v-list>
          <v-list-item
            class="d-flex align-center justify-center text-center py-5"
          >
            <v-icon icon="mdi-account-circle" size="52"></v-icon>

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
          <VDivider></VDivider>
        </v-list>
        <v-card class="pa-5 d-flex justify-center align-center">
          <div class="text-center flex-grow-1">
            <v-btn icon @click.prevent="router.push('/setting/info')">
              <VIcon color="blue" size="x-large">mdi-cog-outline</VIcon>
            </v-btn>

            <v-cardSubtitle class="pt-1">{{
              t("MENU_SETTING")
            }}</v-cardSubtitle>
          </div>
          <div class="text-center flex-grow-1">
            <v-btn icon @click="useUserStore.logoutUser()">
              <VIcon color="red" size="x-large">mdi-logout</VIcon>
            </v-btn>

            <v-cardSubtitle class="pt-1">{{ t("LOGOUT") }}</v-cardSubtitle>
          </div>
        </v-card>
      </v-card>
    </v-menu>
  </v-app-bar>
</template>
<style lang="scss">
.rootAppBar {
  border: none !important;
  box-shadow: none !important;
  background: transparent !important;
}
.miriLogo {
  cursor: pointer;
}
.customeHeightInput {
  --v-input-padding-top: 2px;
  &.topSelect {
    .v-field__append-inner {
      --v-input-padding-top: 3px;
    }
  }
}
</style>
