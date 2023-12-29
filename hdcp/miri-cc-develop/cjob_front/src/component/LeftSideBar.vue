<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { uiStore } from "@/store/UiStore";
const useUiStore = uiStore();
const router = useRouter();
const menus = router.options.routes;
const drawer = computed({
  get() {
    return useUiStore.drawer;
  },
  set(e) {
    return useUiStore.drawerControll(e);
  },
});
</script>

<template>
  <v-navigation-drawer
    class="leftDrawer rounded"
    width="300"
    temporary
    v-model="drawer"
  >
    <v-card elevation="0" class="py-2 d-flex justify-center align-center">
      <!-- <img alt="err" :src="logo" /> -->
    </v-card>
    <v-list nav>
      <v-list-item
        min-height="50"
        active-color="primary"
        prepend-icon="mdi-home"
        title="홈"
        value="home"
        @click.prevent="router.push('/')"
        class="text-subtitle-1 font-weight-bold"
      ></v-list-item>

      <!-- <v-list-item
        min-height="50"
        active-color="indigo darken-2"
        title="엘리베이터 관리 샘플"
        value="sample34"
        @click.prevent="router.push('/testPage')"
        class="text-subtitle-1 font-weight-bold"
      ></v-list-item> -->

      <template v-for="(menu, i) in menus" :key="i">
        <template
          v-if="
            !!menu.children == false &&
            menu.meta.auth !== false &&
            menu.meta.type === 'user'
          "
        >
          <v-list-item
            active-color="primary"
            prepend-icon=""
            :value="menu.name"
            :title="menu.meta.breadcrumb.value"
            class="text-subtitle-1 font-weight-bold"
            @click.prevent="router.push(menu.path)"
          >
          </v-list-item>
        </template>
        <template
          v-if="
            menu.children?.length > 0 &&
            menu.meta.auth !== false &&
            menu.meta.type === 'user'
          "
        >
          <v-list-group class="text-subtitle-1 font-weight-bold" :key="i">
            <template v-slot:activator="{ props }">
              <v-list-item
                v-bind="props"
                :prepend-icon="menu.meta.icon"
                :title="menu.meta.breadcrumb.value"
              >
              </v-list-item>
            </template>
            <template
              v-for="(menuChild, childIdx) in menu.children"
              :key="childIdx"
            >
              <v-list-item
                active-color="primary"
                :value="menuChild.name"
                :title="menuChild.meta.breadcrumb.value"
                @click.prevent="
                  router.push(`${menu.path + '/' + menuChild.path}`)
                "
              >
              </v-list-item>
            </template>
          </v-list-group>
        </template>
      </template>

      <v-list-group class="text-subtitle-1 font-weight-bold">
        <template v-slot:activator="{ props }">
          <v-list-item
            v-bind="props"
            :prepend-icon="'mdi-cog'"
            :title="'어드민'"
          >
          </v-list-item>
        </template>
        <template v-for="(menu, i) in menus" :key="i">
          <template
            v-if="
              menu.children?.length > 1 &&
              menu.meta.auth !== false &&
              menu.meta.type !== 'user'
            "
          >
            <v-list-group class="text-subtitle-1 font-weight-bold" :key="i">
              <template v-slot:activator="{ props }">
                <v-list-item
                  v-bind="props"
                  :prepend-icon="menu.meta.icon"
                  :title="menu.meta.breadcrumb.value"
                >
                </v-list-item>
              </template>
              <template
                v-for="(menuChild, childIdx) in menu.children"
                :key="childIdx"
              >
                <v-list-item
                  active-color="primary"
                  :value="menuChild.name"
                  :title="menuChild.meta.breadcrumb.value"
                  @click.prevent="
                    router.push(`${menu.path + '/' + menuChild.path}`)
                  "
                >
                </v-list-item>
              </template>
            </v-list-group>
          </template>
        </template>
      </v-list-group>
    </v-list>
  </v-navigation-drawer>
</template>

<style>
.v-navigation-drawer .v-list .v-list-item .v-list-item__prepend > .v-icon {
  margin-inline-end: 16px;
}
.v-navigation-drawer .v-list-group__items {
  margin-left: -16px;
}
</style>
