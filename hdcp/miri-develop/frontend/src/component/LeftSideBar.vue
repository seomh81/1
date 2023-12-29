<script setup>
import { ref, computed, watch, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { useDisplay } from "vuetify";
import { useTheme } from "vuetify";
const useUserStore = userStore();
const useUiStore = uiStore();
const theme = useTheme();
const props = defineProps(["modelValue"]);
const emits = defineEmits(["update:modelValue"]);
const routes = useRoute();
const router = useRouter();
const { mdAndDown } = useDisplay();
const whiteLogo = require("@/assets/img/logo-white.png");
const blackLogo = require("@/assets/img/logo-black.png");
const computedLogo = computed(() =>
  theme.global.name.value == "light" ? blackLogo : whiteLogo
);
const smallLeftSideBarImg = require("@/assets/img/smallLeftSideBarImg.png");
const drawer = computed({
  get() {
    return useUiStore.drawer;
  },
  set(e) {
    return useUiStore.drawerControll(e);
  },
});
const computedRail = computed(() => props?.modelValue);
const saveOpenedMenus = ref([]);
watch(computedRail, (newVal, oldVal) => {
  if (newVal) {
    saveOpenedMenus.value = opened.value;
    opened.value = [];
  } else if (!newVal) {
    opened.value = saveOpenedMenus.value;
  }
});

const opened = ref([]);
</script>

<template>
  <v-navigation-drawer
    v-model="drawer"
    :rail="modelValue"
    @mouseenter="emits('update:modelValue', false)"
    @mouseleave="mdAndDown == false ? emits('update:modelValue', true) : ''"
    class="leftDrawer"
    width="250"
  >
    <v-avatar class="my-4 ml-2" v-if="computedRail">
      <v-img :src="smallLeftSideBarImg"></v-img>
    </v-avatar>
 
    <v-sheet
      color="transparent"
      height="100px"
      class="pa-9 d-flex justify-center align-center"
      v-if="!computedRail"
    >
      <v-img alt="logo" :src="computedLogo" />
    </v-sheet>
    <v-list nav v-model:opened="opened">

      <template v-for="(menu, i) in useUserStore?.menus" :key="i">
        <template
          v-if="!!menu.children == false && menu?.meta?.type === 'user'"
        >
          <v-list-item
            active-color="primary"
            :prepend-icon="menu.meta.icon"
            :value="menu.name"
            :title="menu.meta.breadcrumb"
            class="text-subtitle-1 font-weight-bold"
            :active="routes.fullPath == menu.path"
            @click.prevent="router.push(menu.path)"
          >
          </v-list-item>
        </template>
        <template
          v-if="
            menu.children?.length > 0 &&
            menu.meta.type === 'user' &&
            menu.meta.hide !== true
          "
        >
          <v-list-group
            class="text-subtitle-1 font-weight-bold"
            :value="menu.name"
            :key="i"
          >
            <template v-slot:activator="{ props }">
              <v-list-item
                v-bind="props"
                :prepend-icon="menu.meta.icon"
                :title="menu.meta.breadcrumb"
              >
              </v-list-item>
            </template>
            <template
              v-for="(menuChild, childIdx) in menu.children"
              :key="childIdx"
            >
              <v-list-item
                v-if="menuChild.meta.hide !== true"
                :active="
                  routes.fullPath == `${menu.path + '/' + menuChild.path}`
                "
                active-color="primary"
                :value="menuChild.name"
                :title="menuChild.meta.breadcrumb"
                @click.prevent="
                  router.push(`${menu.path + '/' + menuChild.path}`)
                "
              >
              </v-list-item>
            </template>
          </v-list-group>
        </template>
      </template>
    </v-list>
    <v-list nav v-model:opened="opened">
      <template v-for="(menu, i) in useUserStore?.menus" :key="i">
        <template
          v-if="
            menu.children?.length > 0 &&
            menu.meta.type === 'admin' &&
            menu.meta.hide !== true
          "
        >
          <v-list-group
            :value="menu.name"
            class="text-subtitle-1 font-weight-bold"
            :key="i"
          >
            <template v-slot:activator="{ props }">
              <v-list-item
                v-bind="props"
                :prepend-icon="menu.meta.icon"
                :title="menu.meta.breadcrumb"
              >
              </v-list-item>
            </template>
            <template
              v-for="(menuChild, childIdx) in menu.children"
              :key="childIdx"
            >
              <v-list-item
                :active="
                  routes.fullPath == `${menu.path + '/' + menuChild.path}`
                "
                active-color="primary"
                :value="menuChild.name"
                :title="menuChild.meta.breadcrumb"
                @click.prevent="
                  router.push(`${menu.path + '/' + menuChild.path}`)
                "
              >
              </v-list-item>
            </template>
          </v-list-group>
        </template>
      </template>
    </v-list>
  </v-navigation-drawer>
</template>


