import { defineStore } from "pinia";
import { ref, computed, reactive, watch } from "vue";
import { connectionStore } from "./ConnectionStore";
import { uiStore } from "./UiStore";
// import { useRouter } from "vue-router";
import { useToast, TYPE } from "vue-toastification";
import { useI18n } from "vue-i18n";
import { useRouter, useRoute } from "vue-router";

export const userStore = defineStore("user", () => {
  const currentUser = ref(null);
  const userInfo = ref([]);
  const router = useRouter();

  const { t } = useI18n();
  const i18n = useI18n();

  const useUiStore = uiStore();
  const toast = useToast();
  // const router = useRouter();
  const useConnectionStore = connectionStore();

  const setUser = async (params) => {
    userInfo.value = params;
    await useUiStore.getStatus(userInfo.value.locale);
    await useUiStore.getLocale(userInfo.value.locale);
  };
  const changeLocale = async (params) => {
    userInfo.value.locale = params;
    console.log(userInfo.value.locale);
    await useUiStore.getStatus(userInfo.value.locale);
    await useUiStore.getLocale(userInfo.value.locale);
    i18n.locale.value = userInfo.value.locale;
  };
  const logoutUser = async () => {
    const data = await useConnectionStore.request({
      url: "/v2/auth/signout",
      method: "post",
    });
    localStorage.removeItem("accessToken");
    router.push("/");
    userInfo.value = undefined;
    toast.success("로그아웃 되었습니다.");
  };

  return {
    currentUser,
    userInfo,
    changeLocale,
    setUser,
    logoutUser,
  };
});
