import { createRouter, createWebHistory } from "vue-router";
import { computed } from "vue";
import { useToast, TYPE } from "vue-toastification";

import i18n from "../plugin/i18n";

const routes = [
  {
    path: "/",
    name: "login",
    component: () => import("@/view/Login.vue"),
    meta: {
      auth: false,
    },
  },
  {
    path: "/oauth2/redirect",
    name: "oauth",
    component: () => import("@/oauth2/OAuth2RedirectHandler.vue"),
    meta: {
      auth: false,
    },
  },

  {
    path: "/testPage",
    name: "testPage",
    component: () => import("@/view/testPage.vue"),
    meta: {
      auth: false,
    },
  },
  {
    path: "/lv2_dashboard",
    name: "/lv2_dashboard",
    component: () => import("@/view/Lv2DashBoard.vue"),
    meta: {
      type: "user",

      breadcrumb: computed(() => i18n.global.t("LV2 대시보드")),
    },
  },
  {
    path: "/lv3_dashboard",
    name: "/lv3_dashboard",
    component: () => import("@/view/Lv3DashBoard.vue"),
    meta: {
      type: "user",

      breadcrumb: computed(() => i18n.global.t("LV3 대시보드")),
    },
  },

  {
    path: "/service",
    name: "service",
    meta: {
      type: "user",
      breadcrumb: computed(() => i18n.global.t("서비스 현황")),
    },
    children: [
      {
        path: "calendar",
        name: "ServiceCalendar",
        component: () => import("@/view/ServiceCalendar.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("한눈에 보기")),
        },
      },
      {
        path: "detail",
        name: "ServiceDetail",
        component: () => import("@/view/ServiceDetail.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("자세히 보기")),
        },
      },
    ],
  },
  {
    path: "/machine",
    name: "machine",
    meta: {
      type: "user",
      breadcrumb: computed(() => i18n.global.t("장비")),
    },
    children: [
      {
        path: "search",
        name: "MachineSearch",
        component: () => import("@/view/MachineSearch.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("장비 조회")),
        },
      },
      {
        path: "detail",
        name: "MachineDetail",
        component: () => import("@/view/MachineDetail.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("자세히 보기")),
        },
      },
    ],
  },
  {
    path: "/parts",
    name: "part",
    meta: {
      type: "user",
      breadcrumb: computed(() => i18n.global.t("부품")),
    },
    children: [
      {
        path: "purchase",
        name: "purchase",
        component: () => import("@/view/PartsPurchase.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("부품 구매 이력")),
        },
      },

      {
        path: "lifeInquiry",
        name: "lifeInquiry",
        component: () => import("@/view/PartsLifeInquiry.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("부품 수명 예측 조회")),
        },
      },
    ],
  },
  {
    path: "/report",
    name: "/report",
    meta: {
      type: "user",

      breadcrumb: computed(() => i18n.global.t("보고서")),
    },
    children: [
      {
        path: "base",
        name: "base",
        component: () => import("@/view/ReportBase.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("기본 보고서")),
        },
      },
      {
        path: "remote",
        name: "remote",
        component: () => import("@/view/ReportRemote.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("원격 점검 보고서")),
        },
      },
    ],
  },
  {
    path: "/setting",
    name: "setting",
    meta: {
      auth: false,

      breadcrumb: computed(() => i18n.global.t("STRING_SETTING")),
    },
    children: [
      {
        path: "info",
        name: "info",
        component: () => import("@/view/SettingInfo.vue"),
        meta: {
          auth: false,

          breadcrumb: computed(() => i18n.global.t("STRING_SETTING_INFO")),
        },
      },
      {
        path: "portfolio",
        name: "portfolio",
        component: () => import("@/view/SettingPortfolio.vue"),
        meta: {
          auth: false,
          breadcrumb: computed(() => i18n.global.t("STRING_SETTING_PORTFOLIO")),
        },
      },
    ],
  },
  {
    path: "/system",
    name: "system",
    meta: {
      type: "admin",

      breadcrumb: computed(() => i18n.global.t("MENU_SYSTEM")),
      icon: "mdi-cog",
    },
    children: [
      {
        path: "faq",
        name: "faq",
        component: () => import("@/view/admin/SetFAQ.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_FAQ")),
        },
      },
      {
        path: "notice",
        name: "notice",
        component: () => import("@/view/admin/SetNotice.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_NOTICE")),
        },
      },
      {
        path: "masterdata",
        name: "masterdata",
        component: () => import("@/view/admin/MasterData.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_MASTERDATA")),
        },
      },
    ],
  },
  {
    path: "/ad",
    name: "ad",
    meta: {
      breadcrumb: computed(() => i18n.global.t("MENU_AD")),
      icon: "mdi-camera-image",
      type: "admin",
    },
    children: [
      {
        path: "interim_ad",
        name: "interim_ad",
        component: () => import("@/view/admin/SetInterimAd.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_INTERIM_AD")),
        },
      },
      {
        path: "login_ad",
        name: "login_ad",
        component: () => import("@/view/admin/SetLoginAd.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_LOGIN_AD")),
        },
      },
      {
        path: "popup_ad",
        name: "popup_ad",
        component: () => import("@/view/admin/SetPopupAd.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_POPUP_AD")),
        },
      },

      {
        path: "video_ad",
        name: "video_ad",
        component: () => import("@/view/admin/SetVideoAd.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_VIDEO_AD")),
        },
      },
    ],
  },
  {
    path: "/priv",
    name: "priv",
    meta: {
      type: "admin",

      breadcrumb: computed(() => i18n.global.t("MENU_PRIV")),
      icon: "mdi-briefcase-account-outline",
    },
    children: [
      {
        path: "user",
        name: "user",
        component: () => import("@/view/admin/SetUser.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_USER")),
        },
      },
      {
        path: "map_view_function",
        name: "map_view_function",
        component: () => import("@/view/admin/MapViewFunction.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_VIEW_FUNCTION")),
        },
      },
      {
        path: "map_menu_view",
        name: "map_menu_view",
        component: () => import("@/view/admin/MapMenuView.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_MENU_VIEW")),
        },
      },
      {
        path: "map_role_user",
        name: "map_role_user",
        component: () => import("@/view/admin/MapRoleUser.vue"),
        meta: {
          breadcrumb: computed(() => i18n.global.t("MENU_ROLE_USER")),
        },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});
router.beforeEach((to, from) => {
  const toast = useToast();

  if (!!localStorage.getItem("accessToken") == false && to.name !== "login") {
    // if (to.name !== "oauth") {
    toast.warning("접근할 수 없습니다. 로그인 해주세요.");
    router.push("/");
    // }
  } else if (
    !!localStorage.getItem("accessToken") == true &&
    to.name === "login"
  ) {
    router.push("/lv3_dashboard");
  }
});
export default router;
