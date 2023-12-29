<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { uiStore } from "@/store/UiStore";
import { useI18n } from "vue-i18n";
import { userStore } from "@/store/UserStore";
import { useToast } from "vue-toastification";
import { useRouter, useRoute } from "vue-router";
import { useDisplay } from "vuetify";
import { connectionStore } from "@/store/ConnectionStore";
import dayjs from "dayjs";
const { t } = useI18n();
const useUserStore = userStore();
const rightDrawer = ref(false);
const toast = useToast();
const route = useRoute();
const notificationData = ref([]);
const useConnectionStore = connectionStore();
const data =
  "안녕하세요. 현대엘리베이터 고객케어센터 입니다.\n\n접수되었던 서비스 요청 건이 완료되었습니다.\n\n이후로도 불편하신 사항이 있으시면\n언제든지 고객케어센터로 연락주시기 바랍니다.\n\nMIRI Service 고객포탈 바로가기\n\n감사합니다.\n현대엘리베이터 드림";

const getAlram = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/cc/alarm/search",
      method: "post",
      queryparam: {
        userId: useUserStore.userInfo.userId,
        alarmType: "ALL",
        startDate: dayjs().subtract(7, "day").format("YYYYMMDD"),
        // searchBar.inputs
        //     .find((item) => item.name == "startDate")
        //     .target.replace(/-/g, ""),
        endDate: dayjs().format("YYYYMMDD"),
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      rightDrawer.value = true;
      notificationData.value = data;
      console.log(data[0].contents);
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("ALRAM")]));
    }
  } else {
    toast.error(t("ERROR_SEARCH", t("ALRAM")));
  }
};
</script>

<template>
  <v-btn
    class="custom-btn"
    icon="mdi-bell-outline"
    v-if="!rightDrawer && route.path !== '/'"
    @click="[getAlram()]"
  >
  </v-btn>
  <v-navigation-drawer
    temporary
    class="alramSideBar"
    v-model="rightDrawer"
    location="right"
    width="350"
  >
    <v-card
      rounded="xl"
      class="pa-4 ma-4"
      :value="item"
      v-for="item in notificationData"
      :key="item"
    >
      <div class="d-flex justify-space-between">
        <h6 class="smallBoldText">{{ item.alarmEvent }}</h6>
        <v-card-subtitle>{{
          dayjs(item?.registDt).format("YYYY-MM-DD")
        }}</v-card-subtitle>
      </div>
      <pre>{{ item.contents }}</pre>
    </v-card>
  </v-navigation-drawer>
</template>

<style lang="scss">
.custom-btn {
  position: fixed;
  top: 25%;
  right: 2px;
  border-radius: 50% 50% 0px;
  z-index: 11;
  i {
    margin-left: 3px;
    margin-top: 3px;
  }
}
.alramSideBar {
  box-shadow: none;
  border: none;
  background: none;
}
/* .v-navigation-drawer .v-list .v-list-item .v-list-item__prepend > .v-icon {
  margin-inline-end: 16px;
}
.v-navigation-drawer .v-list-group__items {
  margin-left: -16px;
} */
</style>
