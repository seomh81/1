import { createRouter, createWebHistory } from "vue-router";
import { computed } from "vue";
import { useToast, TYPE } from "vue-toastification";
import { ls } from "@/plugin/secure";
import { userStore } from "@/store/UserStore";
import menus from "./menus.json";
import i18n from "../plugin/i18n";

const routes = [
  {
    path: "/",
    name: "login",
    component: () => import("@/view/Login.vue"),
    meta: {},
  },
  {
    path: "/signUp",
    name: "signUp",
    component: () => import("@/view/SignUp.vue"),
    meta: {},
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

function filteringMenus(defaultMenu, userMenu) {
  function filterMenu(menu) {
    const isMenuInUserMenu = userMenu.some(
      (item) => item.vueFileUrl === menu.vueFileUrl
    );
    if (isMenuInUserMenu) {
      menu.component = () => import(`@/${menu.vueFileUrl.replace("_copy", "")}`);
      menu.meta.breadcrumb = computed(() =>
        i18n.global.t(`MENU_${menu.name.toUpperCase()}`)
      );
      const target = userMenu.find(
        (item) => item.vueFileUrl === menu.vueFileUrl
      );
      menu.meta.methods = target.methods;
    }
    if (menu.children) {
      menu.meta.breadcrumb = computed(() =>
        i18n.global.t(`MENU_${menu.name.toUpperCase()}`)
      );
      menu.children = menu.children.filter(filterMenu);
    } else {
      if (menu.vueFileUrl) {
        return isMenuInUserMenu;
      }
    }
    return (
      !menu.vueFileUrl ||
      isMenuInUserMenu ||
      (menu.children && menu.children.length > 0)
    );
  }

  return defaultMenu.filter(filterMenu);
}

let once = false;
router.beforeEach((to, from) => {
  const toast = useToast();
  const useUserStore = userStore();

  if (once == false && ls.get("menus")) {
    try {
      const menuData = filteringMenus(
        JSON.parse(JSON.stringify(menus)),
        JSON.parse(ls.get("menus"))
      );
      menuData.forEach((route) => {
        router.addRoute(route);
      });
      useUserStore.setMenus(menuData);
      router.push(to.path);
    } catch {
      ls.clear();
      router.push("/");
    }
    once = true;
  } else {
    once = false;
  }

  if (!!ls.get("accessToken") == false && to.name !== "login") {
    if (to.name !== "signUp") {
      toast.warning(i18n.global.t("WARNING_NO_ACCESS_TOKEN"));
      router.push("/");
    }
  } else if (!!ls.get("accessToken") == true && to.name === "login") {
    try {
      router.push(JSON.parse(ls.get("page")));
    } catch {
      ls.clear();
      router.push("/");
    }
  }
});
export default router;
