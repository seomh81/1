import axios from "@/plugin/axios";
import { defineStore, storeToRefs } from "pinia";
import { ref, computed, reactive, watch } from "vue";
import { useI18n } from "vue-i18n";
import { useToast, TYPE } from "vue-toastification";
import { connectionStore } from "./ConnectionStore";
export const uiStore = defineStore("ui", () => {
  const drawer = ref(false);
  const spiner = ref(false);
  const { t } = useI18n();
  const useConnectionStore = connectionStore();
  const toast = useToast();

  const reduceView = ref(false);
  const defaultLocale = ref();
  const localeItem = ref();
  const defaultStatus = ref();
  const statusItem = ref();
  const openModal = ref();

  const openModalEvent = (payload) => {
    openModal.value = payload;
  };
  const closeModal = () => {
    openModal.value = undefined;
  };

  const drawerControll = (params) => {
    drawer.value = params;
  };
  const spinerControll = (params) => {
    spiner.value = params;
  };
  const toggleWide = () => {
    reduceView.value = !reduceView.value;
  };

  const setStatus = (params) => {
    statusItem.value = params.statusItem;
    defaultStatus.value = params.defaultStatus;
  };
  const setLocale = (params) => {
    localeItem.value = params.localeItem;

    defaultLocale.value = params.defaultLocale;
  };
  const getData = async (params) => {
    const headResult = await useConnectionStore.request({
      url: "/v2/masterdata/head/list",
      method: "get",
      queryparam: {
        searchKeyword: params.keyword,
        userLocale: params.locale,
      },
    });
    if (headResult.data.length > 0) {
      const { code, data } = await useConnectionStore.request({
        url: "/v2/masterdata/detail/list",
        method: "get",
        queryparam: {
          masterdataId: headResult?.data[0]?.masterdataId,
        },
      });
      if (code == 200) {
        return data;
      } else {
        console.log("err");
      }
    } else {
      toast(t("WARNING_APP_KEY"), {
        type: TYPE.WARNING,
        position: "bottom-right",
        timeout: false,
      });
    }
  };
  const getStatus = async (locale) => {
    const result = await getData({
      keyword: "STATUS",
      locale: locale ? locale : "ko_kr",
    });
    let successStatus =
      result.some((obj) => obj.code == 0) &&
      result.some((obj) => obj.code == 9);

    if (successStatus === true) {
      const defaultStatus = result?.find((statusItem) => statusItem.code == 0);

      setStatus({
        statusItem: result,
        defaultStatus,
      });
    } else {
      toast(t("WARNING_APP_STATUS_CODE"), {
        position: "bottom-right",
        type: TYPE.WARNING,
        timeout: false,
      });
    }
  };

  const getLocale = async (locale) => {
    const result = await getData({
      keyword: "LOCALE",
      locale: locale ? locale : "ko_kr",
    });
    let successLocale =
      result.some((obj) => obj.code == "ko_kr") &&
      result.some((obj) => obj.code == "en_gn");

    if (successLocale == true) {
      const defaultLocale = result?.find((localeItem) => {
        console.log(localeItem);
        if (locale) {
          return localeItem.code === locale;
        } else {
          return localeItem.code == "ko_kr";
        }
      });
      setLocale({
        localeItem: result,
        defaultLocale,
      });
    } else {
      toast(t("WARNING_APP_LOCALE_CODE"), {
        position: "bottom-right",
        type: TYPE.WARNING,
        timeout: false,
      });
    }
  };
  return {
    drawer,
    spiner,
    openModal,

    reduceView,
    statusItem,
    localeItem,
    defaultLocale,
    defaultStatus,
    openModalEvent,
    closeModal,
    drawerControll,
    toggleWide,
    spinerControll,
    setStatus,
    setLocale,
    getLocale,
    getStatus,
  };
});
