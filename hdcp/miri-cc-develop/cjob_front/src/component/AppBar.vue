<script setup>
import { useRouter, useRoute } from "vue-router";
import { uiStore } from "@/store/UiStore";
import { ref, computed, watch, watchEffect } from "vue";
import { UserInfo, LeftSideBar, ToggleBtn } from "@/component/Template";
import { useTheme } from "vuetify";
const theme = useTheme();

const useUiStore = uiStore();
const router = useRouter();
const route = useRoute();
const whiteLogo = require("@/assets/img/logo-white.jpg");
const darkLogo = require("@/assets/img/logo-dark.jpg");

const items = computed(() => {
  return [
    {
      title: route?.matched[0]?.meta?.breadcrumb?.value
        ? route?.matched[0]?.meta?.breadcrumb?.value
        : "테스트 페이지",
    },
    {
      title: route?.matched[1]?.meta?.breadcrumb?.value,
      disabled: false,
    },
  ];
});
const emptyItems = ["Default 포트폴리오"];
const emptyModel = ["Default 포트폴리오"];
const emptyItems2 = ["포트폴리오를 선택해주세요"];
const emptyModel2 = ["포트폴리오를 선택해주세요"];
</script>

<template>
  <LeftSideBar />

  <v-app-bar elevation="1">
    <template v-slot:prepend>
      <v-img
        class="ml-12 miriLogo"
        :src="theme.global.name.value == 'light' ? whiteLogo : darkLogo"
        width="80"
        height="25"
        @click="useUiStore.drawerControll(true)"
      />
      <!-- <v-app-bar-nav-icon
        variant="plain"
        @click="useUiStore.drawerControll(true)"
      ></v-app-bar-nav-icon> -->
    </template>
    <div>
      <v-select
        class="fieldRadius lg ml-15"
        density="compact"
        variant="outlined"
        v-model="emptyModel"
        :items="emptyItems"
        hide-details
      >
      </v-select>
    </div>
    <!-- <v-breadcrumbs
      :items="items"
      :divider="items[1].title ? '-' : ''"
    ></v-breadcrumbs> -->
    <v-spacer></v-spacer>
    <ToggleBtn></ToggleBtn>
    <!-- <v-btn
      variant="plain"
      :icon="
        useUiStore.reduceView == false
          ? 'mdi-format-horizontal-align-center'
          : 'mdi-arrow-expand-horizontal'
      "
      @click="useUiStore.toggleWide()"
      :stlye="{ marginLeft: '20px' }"
    >
    </v-btn> -->

    <!-- <v-btn @click.prevent="router.push('/')">HOME</v-btn> -->
    <!-- <v-btn @click.prevent="router.push('/page')">PAGE</v-btn> -->
    <!-- <v-btn @click.prevent="router.push('/dev/sample')">샘플</v-btn> -->
    <!-- <v-btn @click.prevent="router.push('/dev/api')">API</v-btn> -->
    <!-- <v-btn
      :color="useUiStore.testBorder ? 'indigo' : 'primary'"
      @click.prevent="useUiStore.toggleTestBorder()"
      >테두리</v-btn
    > -->

    <UserInfo />
  </v-app-bar>
</template>
<style lang="scss">
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
