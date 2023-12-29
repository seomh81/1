<script setup>
import { ref, onMounted, reactive } from "vue";

import { useRouter, useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import { connectionStore } from "@/store/ConnectionStore";
import { userStore } from "@/store/UserStore";
import { useToast } from "vue-toastification";
const toast = useToast();
const useConnectionStore = connectionStore();
const useUserStore = userStore();
const i18n = useI18n();
const { t } = useI18n();
const router = useRouter();
const password = reactive({
  old: "",
  new: "",
  reNew: "",
});
const menuOverlay = ref(false);
const locale = ref();
const localeMenu = ref(false);
const logoutOverlay = ref(false);
const form = ref();
const valid = ref(false);

const logoutEvent = async () => {
  logoutOverlay.value = false;
  menuOverlay.value = false;
  localStorage.clear();
  router.push("/");
};
const localeChange = () => {
  if (locale.value == "KO_KR") {
    i18n.locale.value = "ko";
  } else {
    i18n.locale.value = "en";
  }
  toast.info(`${locale.value}로 변경되었습니다.`);
  localeMenu.value = false;
  menuOverlay.value = false;
};

const getUserData = async () => {
  const { data } = await useConnectionStore.request({
    url: "/v2/auth/my",
    method: "post",
    queryparam: {
      userLocale: "ko_kr",
    },
  });
  await useUserStore.setUser(data);
};

const changePassword = async () => {
  await form.value.validate();
  if (valid.value) {
    console.log("hi");
  }
  // const { message, code, resultset } = await useConnectionStore.request({
  //   url: "/v1/auth/changepassword",
  //   method: "post",
  //   queryparam: {
  //     userId: "aa",
  //     oldPassword: "string",
  //     newPassword: "string123",
  //     reNewPassword: "string123",
  //   },
  // });
};
onMounted(() => {
  getUserData();
});
const rules = ref({
  required: (value) => !!value || t("STRING_RULE_REQUIRED"),
  passwordOld: (value) => {
    const pattern = value.replace(/\s/g, "");
    password.old = pattern;
    return !(pattern.length < 3) || t("STRING_RULE_PASSWORD");
  },
  passwordNew: (value) => {
    const pattern = value.replace(/\s/g, "");
    password.new = pattern;
    return !(pattern.length < 3) || t("STRING_RULE_PASSWORD");
  },
  passwordReNew: (value) => {
    const pattern = value.replace(/\s/g, "");
    return !(password.new !== pattern) || t("STRING_RULE_CHECK_PASSWORD");
  },
});
</script>
<template>
  <v-menu :close-on-back="false" v-model="menuOverlay">
    <template v-slot:activator="{ props }">
      <v-list density="compact" nav>
        <v-list-item rounded="lg" v-bind="props">
          <v-avatar rounded="lg" class="mr-3" size="small">
            <v-img
              :src="
                useUserStore.userInfo?.avatarUrl
                  ? useUserStore.userInfo?.avatarUrl
                  : 'https://cdn.vuetifyjs.com/images/john.jpg'
              "
              alt="John"
            ></v-img>
          </v-avatar>
          {{ useUserStore.userInfo?.userNm }}
        </v-list-item>
      </v-list>
    </template>
    <v-card rounded="lg" min-width="330" max-width="330">
      <v-list>
        <v-list-item
          class="d-flex align-center justify-center text-center py-5"
        >
          <v-avatar size="x-large">
            <v-img
              :src="
                useUserStore.userInfo?.avatarUrl
                  ? useUserStore.userInfo?.avatarUrl
                  : 'https://cdn.vuetifyjs.com/images/john.jpg'
              "
              alt="Avatar"
            ></v-img>
          </v-avatar>
          <v-list-item-title class="pt-5" :style="{ fontWeight: '400' }">
            {{ useUserStore.userInfo?.userNm }}
          </v-list-item-title>
          <v-list-item-subtitle class="pt-1">{{
            useUserStore.userInfo?.userId
          }}</v-list-item-subtitle>
          <v-list-item-subtitle class="pt-2 pb-1">{{
            useUserStore.userInfo?.phoneno
              ? useUserStore.userInfo?.phoneno
              : "010 - 0000 - 0000"
          }}</v-list-item-subtitle>
        </v-list-item>
        <v-divider></v-divider>
      </v-list>
      <v-card class="pa-5 d-flex justify-center align-center">
        <div class="text-center flex-grow-1">
          <v-btn icon @click.prevent="router.push('/setting/info')">
            <v-icon color="blue" size="x-large">mdi-cog-outline</v-icon>
          </v-btn>

          <v-card-subtitle class="pt-1">{{
            t("STRING_SETTING")
          }}</v-card-subtitle>
        </div>
        <div class="text-center flex-grow-1">
          <v-btn icon @click="useUserStore.logoutUser()">
            <v-icon color="red" size="x-large">mdi-logout</v-icon>
          </v-btn>

          <v-card-subtitle class="pt-1">{{
            t("STRING_LOGOUT")
          }}</v-card-subtitle>
        </div>
      </v-card>
    </v-card>
  </v-menu>
</template>
