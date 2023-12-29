import { defineStore } from "pinia";
import { ref, computed, reactive, watch } from "vue";
import { useTheme } from "vuetify";
import { connectionStore } from "./ConnectionStore";
import { uiStore } from "./UiStore";
// import { useRouter } from "vue-router";
import { ls } from "@/plugin/secure";
import { useToast, TYPE } from "vue-toastification";
import { useI18n } from "vue-i18n";
import { useRouter, useRoute } from "vue-router";

export const userStore = defineStore("user", () => {
  const currentUser = ref(null);
  const userInfo = ref([]);
  const router = useRouter();
  const theme = useTheme();
  const menus = ref([]);
  const { t } = useI18n();
  const i18n = useI18n();
  const portfolio = ref([]);
  const selectedPortfolio = ref([]);
  const useUiStore = uiStore();
  const toast = useToast();
  const useConnectionStore = connectionStore();

  const setMenus = (params) => {
    menus.value = params;
  };
  const setUser = async (params) => {
    userInfo.value = params;
  };
  const setPortfolio = (params) => {
    if (params.selected) {
      portfolio.value.selected = params.selected;
    }
    if (params.portfolio) {
      portfolio.value.list = params.portfolio;
    }
  };
  const changeLocale = async (params) => {
    userInfo.value.locale = params;

    i18n.locale.value = userInfo.value.locale;
  };
  const getUserData = async () => {
    const { data, code } = await useConnectionStore.request({
      url: "/v2/auth/my",
      method: "post",
      queryparam: {},
    });

    if (code === 200) {
      i18n.locale.value = data.locale;
      data.theme == "light"
        ? (theme.global.name.value = "light")
        : (theme.global.name.value = "dark");

      if (data.phonenumber) {
        data.phonenumber = data.phonenumber.replace(
          /^(.{3})(.*)(.{4})$/,
          "$1-$2-$3"
        );
      }
      await getPortfolioList();
      await setUser({
        ...data,
      });
    }
  };
  const getPortfolioList = async () => {
    const { because, code, data, message, result } =
      await useConnectionStore.request({
        url: "/v2/portfolio/current/list",
        method: "get",
      });
    if (code == 200) {
      if (data.length > 0) {
        const returnPortfolio = {
          selected: data.find((item) => item.defaultYn === "y"),
          portfolio: data.sort((a, b) => {
            if (a.portfolioName === "lobby") return -1;
            if (b.portfolioName === "lobby") return 1;
            return a.portfolioName.localeCompare(b.portfolioName);
          }),
        };

        setPortfolio(returnPortfolio);
      } else {
        toast.warning(t("WARNING_PORTFOLIO_NO_RESULT"));
      }
    } else {
      toast.warning(t("ERROR_PORTFOLIO_API_ERROR"));
    }
  };

  const logoutUser = async () => {
    const { code } = await useConnectionStore.request({
      url: "/v2/auth/signout",
      method: "post",
      queryparam: {},
    });
    if (code == 200) {
      changeLocale("ko_kr");
      userInfo.value = [];
      portfolio.value = [];
      ls.clear();
      router.push("/");

      userInfo.value = undefined;
      toast.success(t("SUCCESS_LOGOUT"));
    } else {
      toast.error(t("ERROR_LOGOUT"));
    }
  };

  return {
    currentUser,
    userInfo,
    portfolio,
    menus,
    setMenus,
    changeLocale,
    setPortfolio,
    setUser,
    getUserData,
    logoutUser,
  };
});
