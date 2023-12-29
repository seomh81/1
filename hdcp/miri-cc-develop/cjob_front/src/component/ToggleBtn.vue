<script setup>
import { ref, watch, onMounted } from "vue";
import { useTheme } from "vuetify";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";

const i18n = useI18n();
const useUiStore = uiStore();
const theme = useTheme();
// const toggleTheme = ref("light");
const useUserStore = userStore();

// const toggleWidth = ref("false");
// const toggleLocale = ref("ko");
const chagneTheme = () => {
  theme.global.name.value == "light"
    ? (theme.global.name.value = "dark")
    : (theme.global.name.value = "light");
  console.log(theme.global.name.value);
};
const localeChange = () => {
  useUserStore.userInfo.locale == "ko_kr" ||
  !!useUserStore.userInfo.locale == false
    ? useUserStore.changeLocale("en_gn")
    : useUserStore.changeLocale("ko_kr");
};

// watch(i18n.locale, (newVal, oldVal) => {
//   if (i18n.locale.value == "en") {
//     getLocale("en_gn");
//     getStatus("en_gn");
//   } else {
//     getLocale("ko_kr");
//     getStatus("ko_kr");
//   }
// });
</script>

<template>
  <v-btn-toggle color="primary">
    <v-btn variant="plain" icon @click="localeChange">
      {{
        useUserStore.userInfo?.locale == "ko_kr" ||
        !!useUserStore.userInfo?.locale == false
          ? "KO"
          : "EN"
      }}
    </v-btn>
    <v-btn
      @click="chagneTheme"
      variant="plain"
      :icon="
        theme.global.name.value == 'light'
          ? 'mdi-weather-sunny'
          : 'mdi-weather-night'
      "
    >
    </v-btn>
    <v-btn
      value="false"
      variant="plain"
      :icon="
        useUiStore.reduceView == false
          ? 'mdi-format-horizontal-align-center'
          : 'mdi-arrow-expand-horizontal'
      "
      @click="useUiStore.toggleWide()"
      :stlye="{ marginLeft: '20px' }"
    >
    </v-btn>
  </v-btn-toggle>
  <v-btn-toggle> </v-btn-toggle>
</template>

<style></style>
