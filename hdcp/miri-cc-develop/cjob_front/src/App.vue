<script setup>
import { ref, reactive, inject, computed, watch, onMounted } from "vue";
import { AppBar, Spiner } from "./component/Template";
import { useRouter, useRoute } from "vue-router";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { useI18n } from "vue-i18n";
import { useToast, TYPE } from "vue-toastification";
const toast = useToast();

const i18n = useI18n();
const { t } = useI18n();
const useUiStore = uiStore();
const useConnectionStore = connectionStore();

const router = useRouter();
const route = useRoute();
// watch(i18n.locale, (newVal, oldVal) => {
//   if (i18n.locale.value == "en") {
//     getLocale("en_gn");
//     getStatus("en_gn");
//   } else {
//     getLocale("ko_kr");
//     getStatus("ko_kr");
//   }
// });
// const getData = async (params) => {
//   const headResult = await useConnectionStore.request({
//     url: "/v2/masterdata/head/list",
//     method: "get",
//     queryparam: {
//       searchKeyword: params.keyword,
//       locale: params.locale,
//     },
//   });
//   console.log(headResult);
//   if (headResult.data.length > 0) {
//     const { code, data } = await useConnectionStore.request({
//       url: "/v2/masterdata/detail/list",
//       method: "get",
//       queryparam: {
//         masterdataId: headResult?.data[0]?.MASTERDATA_ID,
//       },
//     });
//     console.log(data);
//     if (code == 200) {
//       return data;
//     } else {
//       console.log("err");
//     }
//   } else {
//     toast(t("WARNING_APP_KEY"), {
//       type: TYPE.WARNING,
//       position: "bottom-right",
//       timeout: false,
//     });
//   }
// };

// const getStatus = async (locale) => {
//   const result = await getData({
//     keyword: "STATUS",
//     locale: locale ? locale : "ko_kr",
//   });

//   let successStatus =
//     result.some((obj) => obj.CODE == 0) && result.some((obj) => obj.CODE == 9);

//   if (successStatus === true) {
//     const defaultStatus = result?.find((statusItem) => statusItem.CODE == 0);

//     useUiStore.setStatus({
//       statusItem: result,
//       defaultStatus,
//     });
//   } else {
//     toast(t("WARNING_APP_STATUS_CODE"), {
//       position: "bottom-right",
//       type: TYPE.WARNING,
//       timeout: false,
//     });
//   }
// };

// const getLocale = async (locale) => {
//   const result = await getData({
//     keyword: "LOCALE",
//     locale: locale ? locale : "ko_kr",
//   });

//   let successLocale =
//     result.some((obj) => obj.CODE == 'ko_kr') && result.some((obj) => obj.CODE == 'en_gn');

//   if (successLocale == true) {
//     const defaultLocale = result?.find((localeItem) => {
//       if (locale) {
//         return localeItem.CODE === locale;
//       } else {
//         return localeItem.CODE == "ko_kr";
//       }
//     });
//     useUiStore.setLocale({
//       localeItem: result,
//       defaultLocale,
//     });
//   } else {
//     toast(t("WARNING_APP_LOCALE_CODE"), {
//       position: "bottom-right",
//       type: TYPE.WARNING,
//       timeout: false,
//     });
//   }
// };

// getLocale();
// getStatus();
</script>

<template>
  <v-app class="app" :class="`${useUiStore.font2 ? 'font2' : ''}`">
    <v-layout>
      <AppBar v-if="route.path !== '/'" />
      <!-- <AppBar /> -->
      <v-main>
        <router-view />
        <Spiner />
      </v-main>
    </v-layout>
  </v-app>
  <teleport to="#extra-modal" :disabled="false">
    <!-- <div class="modal">
      <v-row>
        <v-col cols="auto" class="px-1">
          <Info />
          <FacilityStatus />
          <Notice />
        </v-col>
        <v-col cols="auto" class="px-1">
          <ConnettingService />
          <AiDiagnosis />
          <Inquiry />
        </v-col>
      </v-row>
    </div> -->
  </teleport>
</template>

<style lang="scss">
// .v-application.app {
// font-family: NanumGothic, Helvetica, Arial, sans-serif;
// -webkit-font-smoothing: antialiased;
// -moz-osx-font-smoothing: grayscale;
// color: #2c3e50;

// min-width: 600px;
// }

// jodit
.jodit-add-new-line {
  display: none;
}

.Vue-Toastification__toast--warning {
  color: #5f5f5f;
}
</style>
