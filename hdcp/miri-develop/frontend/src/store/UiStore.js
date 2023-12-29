import { defineStore } from "pinia";
import { ref  } from "vue";
import { useI18n } from "vue-i18n";
import { useToast, TYPE } from "vue-toastification";
import { connectionStore } from "./ConnectionStore";
export const uiStore = defineStore("ui", () => {
  const drawer = ref(false);
  const spiner = ref(false);
  const parallelSpiner = ref(false);
  const { t } = useI18n();
  const useConnectionStore = connectionStore();
  const toast = useToast();
  const defaultLocale = ref();
  const localeItem = ref();
  const defaultDelYn = ref();
  const delYnItem = ref();
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
  const parallelSpinerControll = (params) => {
    spiner.value = params;
  };
  

  const setDelYn = (params) => {
    delYnItem.value = params.delYnItem;
    defaultDelYn.value = params.defaultDelYn;
  };
  const setLocale = (params) => {
    localeItem.value = params.localeItem;
    defaultLocale.value = params.defaultLocale;
  };

  const getDelYn = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "DEL_YN",
      },
    });
    if (code === 200) {
      let successDelYn = data
      ? data.some((obj) => obj.code == "ALL") &&

         data?.some((obj) => obj.code == "y") &&
          data?.some((obj) => obj.code == "n")
        : false;

      if (successDelYn === true) {
        const defaultDelYn = data?.find((delYnItem) => delYnItem.code == "ALL");
        const sortingData = data.sort((a, b) => a.sortSeq - b.sortSeq);

        return {
          delYnItem: sortingData,
          defaultDelYn,
        };
      } else {
        toast(t("WARNING_APP_DEL_YN_CODE"), {
          position: "bottom-right",
          type: TYPE.WARNING,
          timeout: false,
        });
        return {
          delYnItem: ['ALL','y','n'],
          defaultDelYn: "ALL",
        };
      }
    }
  };
  const getActivation = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "ACTIVATION_STATUS",
      },
    });
    if (code === 200) {
      let successActivation = data
        ? data.some((obj) => obj.code == "ALL") &&
          data?.some((obj) => obj.code == "y") &&
          data?.some((obj) => obj.code == "n")
        : false;

      if (successActivation === true) {
        const defaultActivation = data?.find(
          (activationItem) => activationItem.code == "ALL"
        );
        const sortingData = data.sort((a, b) => a.sortSeq - b.sortSeq);
        return {
          defaultActivation,
          activationItem: sortingData,
        };
      } else {
        toast(t("WARNING_APP_ACTIVATION_STATUS_CODE"), {
          position: "bottom-right",
          type: TYPE.WARNING,
          timeout: false,
        });
        return {
          defaultActivation: "y",
          activationItem: undefined,
        };
      }
    }
  };

  const getLocale = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "LOCALE",
      },
    });
    if (code === 200) {
      let successLocale = data
        ? data?.some((obj) => obj.code == "ko_kr") &&
          data?.some((obj) => obj.code == "en_gn")
        : false;

      if (successLocale == true) {
        const defaultLocale = data?.find((localeItem) => {
          if (locale) {
            return localeItem.code === locale;
          } else {
            return localeItem.code == "ko_kr";
          }
        });
        const sortingData = data.sort((a, b) => a.sortSeq - b.sortSeq);

        return {
          localeItem: sortingData,
          defaultLocale,
        };
      } else {
        toast(t("WARNING_APP_LOCALE_CODE"), {
          position: "bottom-right",
          type: TYPE.WARNING,
          timeout: false,
        });
        return {
          localeItem: undefined,
          defaultLocale: "ko_kr",
        };
      }
    }
  };
  const getAccountStatus = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "ACCOUNT_STATUS",
      },
    });
    if (code === 200) {
      let successActivation = data
        ? data.some((obj) => obj.code == "ALL") &&
          data?.some((obj) => obj.code == "0001") &&
          data?.some((obj) => obj.code == "0010") &&
          data?.some((obj) => obj.code == "0020") &&
          data?.some((obj) => obj.code == "0040") &&
          data?.some((obj) => obj.code == "4000") &&
          data?.some((obj) => obj.code == "9999")
        : false;

      if (successActivation === true) {
        const defaultAccountStatus = data?.find(
          (activationItem) => activationItem.code == "ALL"
        );
        const sortingData = data.sort((a, b) => a.sortSeq - b.sortSeq);
        return {
          defaultAccountStatus,
          accountStatusItem: sortingData,
        };
      } else {
        toast(t("WARNING_APP_ACCOUNT_STATUS_CODE"), {
          position: "bottom-right",
          type: TYPE.WARNING,
          timeout: false,
        });
        return {
          defaultAccountStatus  : "ALL",
          accountStatusItem: ["ALL", "0001", "0010", "0020", "0040", "4000","9999"],
        };
      }
    }
  };
  const getServcieType = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "SERVICE_TYPE",
      },
    });

    if (code === 200) {
      let successItems = data
        ? data?.some((obj) => obj.code == "ALL") &&
          data?.some((obj) => obj.code == "0010") &&
          data?.some((obj) => obj.code == "0020") &&
          data?.some((obj) => obj.code == "0030") &&
          data?.some((obj) => obj.code == "0040") &&
          data?.some((obj) => obj.code == "0050") &&
          data?.some((obj) => obj.code == "0060")
        : false;

      if (successItems === true) {
        const defaultItem = data?.find((item) => item.code == "ALL");
        const sortingData = data.sort((a, b) => a.sortSeq - b.sortSeq);
        return {
          defaultItem,
          items: sortingData,
        };
      } else {
        toast(t("WARNING_APP_SERVICE_TYPE_CODE"), {
          position: "bottom-right",
          type: TYPE.WARNING,
          timeout: false,
        });
        return {
          defaultItem: "ALL",

          items: ["0010", "0020", "0030", "0040", "0050", "0060"],
        };
      }
    }
  };

  const getFailType = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "FAIL_TYPE",
      },
    });

    if (code === 200) {
      let successItems = data
        ? data?.some((obj) => obj.code == "010102") &&
          data?.some((obj) => obj.code == "010201") &&
          data?.some((obj) => obj.code == "010501") &&
          data?.some((obj) => obj.code == "010601") &&
          data?.some((obj) => obj.code == "010999")
        : false;

      if (successItems === true) {

        const sortingData = data.sort((a, b) => a.sortSeq - b.sortSeq);
        return {
          items: sortingData,
        };
      } else {
        toast(t("WARNING_APP_FAIL_TYPE_CODE"), {
          position: "bottom-right",
          type: TYPE.WARNING,
          timeout: false,
        });
        return {
          items: ["010102", "010201", "010501", "010601", "010999"],
        };
      }
    }
  };
  const getEquipmentType = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "EQUIPMENT_TYPE",
      },
    });

    if (code === 200) {
      let successItems = data
        ? data?.some((obj) => obj.code == "ALL") &&
          data?.some((obj) => obj.code == "L") &&
          data?.some((obj) => obj.code == "S") &&
          data?.some((obj) => obj.code == "W")
        : false;

      if (successItems === true) {
        const defaultItem = data?.find((item) => item.code == "ALL");
        const sortingData = data.sort((a, b) => a.sortSeq - b.sortSeq);
        return {
          defaultItem,
          items: sortingData,
        };
      } else {
        toast(t("WARNING_APP_EQUIPMENT_TYPE"), {
          position: "bottom-right",
          type: TYPE.WARNING,
          timeout: false,
        });
        return {
          defaultItem: "ALL",
          items: ["ALL", "L", "S", "W"],
        };
      }
    }
  };
  const getAlarmType = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "ALARM_TYPE",
      },
    });

    if (code === 200) {
      const filterAlram = data.filter((item) => {
        return item.value2 == "0" || item.code == "ALL";
      });
      filterAlram.push({
        code: "ALL",
        value1: t("ALL"),
        sortSeq: 0,
      });

      let successItems = filterAlram
        ? filterAlram?.some((obj) => obj.code == "10") &&
          filterAlram?.some((obj) => obj.code == "20") &&
          filterAlram?.some((obj) => obj.code == "30") &&
          filterAlram?.some((obj) => obj.code == "40")
        : false;

      if (successItems === true) {
        const defaultItem = filterAlram?.find((item) => item.code == "ALL");
        const sortingData = filterAlram.sort((a, b) => a.sortSeq - b.sortSeq);
        return {
          defaultItem,
          items: sortingData,
        };
      } else {
        toast(t("WARNING_APP_ALARM_TYPE"), {
          position: "bottom-right",
          type: TYPE.WARNING,
          timeout: false,
        });
        return {
          defaultItem: "ALL",
          items: ["ALL", "10", "20", "30", "40"],
        };
      }
    }
  };

  const getOfficeList = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "ONECLICK_CONTACT",
      },
    });

    if (code === 200) {
      const resultData = [];
      resultData.push({
        sortSeq:0,
        value1:"ALL",
      });
      data.forEach((item) => {
        item.sortSeq = item.sortSeq + 1;
        resultData.push(item);
      });
      const defaultItem = resultData?.find((item) => item.value1 == "ALL");
      const sortingData = resultData.sort((a, b) => a.sortSeq - b.sortSeq);
      return {
        defaultItem,
        items: sortingData,
      };
    }
  };
  const getRoleType = async (locale) => {
    const { data, code, result, because } = await useConnectionStore.request({
      url: "/v2/masterdata/head/select",
      method: "post",
      queryparam: {
        masterdataKey: "ROLE_TYPE",
      },
    });

    if (code === 200) {
      const resultData = [];
      data.forEach((item) => {
        item.sortSeq = item.sortSeq + 1;
        resultData.push(item);
      });
      const defaultItem = resultData?.find((item) => item.code == "ALL");
      const sortingData = resultData.sort((a, b) => a.sortSeq - b.sortSeq);
      return {
        defaultItem,
        items: sortingData,
      };
    }
  };

  const rules = ref({
    required: (value) => {
      return !!value || "입력이 필요합니다.";
    },
    
  })
  return {
    drawer,
    spiner,
    openModal,

    delYnItem,
    localeItem,
    defaultLocale,
    defaultDelYn,
    parallelSpiner,
    rules,
    openModalEvent,
    closeModal,
    getServcieType,
    drawerControll,
    spinerControll,
    parallelSpinerControll,
    setDelYn,
    setLocale,
    getLocale,
    getActivation,
    getDelYn,
    getFailType,
    getAccountStatus,
    getEquipmentType,
    getAlarmType,
    getOfficeList,
    getRoleType,
  };
});
