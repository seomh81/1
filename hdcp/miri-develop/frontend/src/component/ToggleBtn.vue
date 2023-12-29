<script setup>
import { computed } from "vue";
import { useTheme } from "vuetify";
import { useRouter, useRoute } from "vue-router";
import { userStore } from "@/store/UserStore";

const theme = useTheme();
const router = useRouter();
const route = useRoute();

const useUserStore = userStore();

// const toggleLocale = ref("ko");
const chagneTheme = () => {
  theme.global.name.value == "light"
    ? (theme.global.name.value = "dark")
    : (theme.global.name.value = "light");
};
const localeChange = () => {
  useUserStore.userInfo.locale == "ko_kr" ||
  !!useUserStore.userInfo.locale == false
    ? useUserStore.changeLocale("en_gn")
    : useUserStore.changeLocale("ko_kr");
};
const computedColor = computed(() => {
  return theme.global.name.value == "light" ? "black" : "white";
});
const linkToMenual = () => {
  window.open("https://www.hyundaielevator.com/upload/miri/MIRI_PC(0).pdf", "_blank");
};

const refresh = async () => {
  if (route.path == "/dashboard") {
    await router.push("/refresh");
    await router.push("/dashboard");
  } else {
    await router.push("/refresh");

    await router.push("/map_page");
  }
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
  <v-btn
    v-if="route.path == '/dashboard' || route.path == '/map_page'"
    icon="mdi-refresh"
    variant="plain"
    @click="[refresh()]"
  ></v-btn>
  <v-btn-toggle color="primary">
    <!-- <v-btn variant="plain" icon @click="localeChange">
      {{
        useUserStore.userInfo?.locale == "ko_kr" ||
        !!useUserStore.userInfo?.locale == false
          ? "KO"
          : "EN"
      }}
    </v-btn> -->

    <v-btn
      rounded="xl"
      @click="chagneTheme"
      variant="plain"
      :icon="
        theme.global.name.value == 'light'
          ? 'mdi-weather-sunny'
          : 'mdi-weather-night'
      "
    >
    </v-btn>

  </v-btn-toggle>
  <v-btn-toggle>
  <v-btn
    icon="mdi-book"
    variant="plain"
    @click="linkToMenual"
  >매뉴얼
  </v-btn>
   </v-btn-toggle>
</template>

<style lang="scss">


.g2-html-annotation {
  color: v-bind(computedColor) !important;
}
.g2-tooltip {
  background: rgb(84, 82, 82) !important;
  * {
    color: white !important;
  }
  width: 100% !important;
}
</style>
