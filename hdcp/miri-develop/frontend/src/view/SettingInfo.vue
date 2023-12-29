<script setup>
import { UserSettingMenu } from "@/component/Template.js";
import { userStore } from "@/store/UserStore";
import Terms from "@/component/terms/terms.vue";
import { ref, onMounted, computed, watch } from "vue";
import { useToast } from "vue-toastification";

import { useI18n } from "vue-i18n";
import { connectionStore } from "@/store/ConnectionStore";
const useUserStore = userStore();
const toast = useToast();
const useConnectionStore = connectionStore();
const extraAddress = ref("");
const { t } = useI18n();
const form = ref();
const valid = ref(false);
const passwordCheck = ref();
const password = ref();
const openTermsDetail = ref(false);
const termsType = ref("");
const rules = ref({
  required: (value) => !!value || t("VALID_CHECK_REQUIRED"),
  email: (value) => {
    const pattern =
      // eslint-disable-next-line
      /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;

    if (pattern.test(value)) {
      // console.log("ok");
    } else {
      return t("VALID_CHECK_EMAIL");
    }
  },
  password: (value) => {
    const pattern =
      // eslint-disable-next-line
      /^(?=.*[a-zA-Z])((?=.*\d)|(?=.*\W)).{8,20}$/;
    if (pattern.test(value)) {
      userData.value.newPassword = value;
      return true;
    } else {
      return t("VALID_CHECK_PASSWORD");
    }
  },

  passwordCheck: (value) => {
    return (
      !(changePasswordData.value.newPassword !== value) ||
      t("VALID_CHECK_PASSWORD_CHECK")
    );
  },

  phone: (value) => {
    const pattern = /[0-9]/g;

    if (pattern.test(value)) {
      let pattern = value
        .replace(/[^0-9]/g, "")
        .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
        // eslint-disable-next-line
        .replace(/(\-{1,2})$/g, "");
      userData.value.phonenumber = pattern;

      return !(pattern && pattern.length < 13) || t("VALID_CHECK_PHONE_LENGTH");
    } else {
      userData.value.phonenumber = "";

      return t("VALID_CHECK_PHONE_NUMBER");
    }
  },
});
const computedUserInfo = computed(() => useUserStore?.userInfo);
const computedAdRecvAg = computed({
  get() {
    return userData.value.adRecvAg;
  },
  set(value) {
    userData.value.adRecvAg = value == false ? "n" : "y";
  },
});


const userData = ref({
  userName: "",
  postnumber: "",
  address: "",
  detailaddress: "",
  phonenumber: "",
  plInfoStoreTimeAg: "",
  adRecvAg: "",
});
const changePasswordData = ref({});
const setUserInfo = () => {
  userData.value.userName = computedUserInfo.value.userName;
  userData.value.adRecvAg = computedUserInfo.value.adRecvAg;
  userData.value.plInfoStoreTimeAg = computedUserInfo.value.plInfoStoreTimeAg;
  userData.value.phonenumber = computedUserInfo.value.phonenumber;
  userData.value.detailaddress = computedUserInfo.value.detailaddress;
  userData.value.address = computedUserInfo.value.address;
  userData.value.postnumber = computedUserInfo.value.postnumber;
};
watch(
  [computedUserInfo],
  () => {
    if (computedUserInfo.value) {
      setUserInfo();
    }
  },
  { deep: true }
);

const execDaumPostcode = () => {
  new window.daum.Postcode({
    oncomplete: (data) => {
      if (extraAddress.value !== "") {
        extraAddress.value = "";
      }
      if (data.userSelectedType === "R") {
        // 사용자가 도로명 주소를 선택했을 경우
        userData.value.address = data.roadAddress;
      } else {
        // 사용자가 지번 주소를 선택했을 경우(J)
        userData.value.address = data.jibunAddress;
      }

      userData.value.postnumber = data.zonecode;
    },
  }).open();
  userData.value.detailaddress = "";
};
const changePassword = async () => {
  const { data, because, result, code, message } =
    await useConnectionStore.request({
      url: "/v2/user/change-password",
      method: "post",
      queryparam: {
        userId: useUserStore.userInfo.userId,
        newPassword: changePasswordData.value.newPassword,
      },
    });
  if (code == 200 && result === "success") {
    passwordOutFoucsValidCheck.value = false;
    changePasswordData.value = {};
    windows.value = "info";
    toast.success(t("SUCCESS_CHANGE_PASSWORD"));
  } else {
    toast.error(t("ERROR_CHANGE_PASSWORD"));
  }
};
const updateInfo = async () => {
  userData.value.phonenumber = userData.value.phonenumber.replace(/-/g, "");
  const { data, because, result, code, message } =
    await useConnectionStore.request({
      url: "/v2/user/update",
      method: "post",
      queryparam: {
        ...userData.value,
      },
    });
  if (code == 200 && result == "success") {
    await useUserStore.getUserData();
    toast.success(t("SUCCESS_UPDATE_INFO"));
  } else {
    toast.error(t("ERROR_UPDATE_INFO"));
  }
};
const showDetailText = (showType) => {
  openTermsDetail.value = true;
  termsType.value = showType;
};
onMounted(() => {
  setUserInfo();
});
const passwordOutFoucsValidCheck = ref(false);

const passwordFoucsOutEvent = async () => {
  const passwordChk = await password.value?.validate();
  if (passwordChk.length == 0) {
    passwordOutFoucsValidCheck.value = true;
  } else {
    passwordOutFoucsValidCheck.value = false;
  }
};
const windows = ref("info");
</script>
<template>
  <v-container class="settingMenu" fluid>
    <v-row justify="center">
      <v-col sm="4" lg="3" class="pr-0">
        <v-card class="settingMenuCard menuCard rounded-s-lg">
          <UserSettingMenu></UserSettingMenu>
        </v-card>
      </v-col>
      <v-col sm="8" lg="7" class="pl-0">
        <v-card class="settingContentCard contentCard rounded-e-lg">
          <v-toolbar class="contentToolbar" color="transparent">
            <v-toolbar-title v-if="windows == 'info'">{{
              t("MENU_SETTING_INFO")
            }}</v-toolbar-title>
            <v-btn
              icon="mdi-chevron-left"
              @click="[(windows = 'info'), (changePasswordData = {})]"
              v-if="windows !== 'info'"
            ></v-btn>

            <v-toolbar-title v-if="windows !== 'info'">{{
              t("PW_CHANGE")
            }}</v-toolbar-title>
            <v-btn
              v-if="windows == 'info'"
              width="5vw"
              :disabled="!valid"
              rounded="lg"
              variant="elevated"
              color="info"
              @click="updateInfo"
              >{{ t("SAVE") }}</v-btn
            >
            <v-btn
              v-if="windows !== 'info'"
              :disabled="!valid"
              width="5vw"
              rounded="lg"
              variant="elevated"
              @click="changePassword"
              :color="valid ? 'info' : ''"
              >{{ t("SAVE") }}</v-btn
            >
          </v-toolbar>

          <v-window v-model="windows">
            <v-window-item value="info">
              <v-card-text class="pa-0">
                <v-sheet class="contentInner">
                  <div class="d-flex">
                    <v-text-field
                      :label="t('NAME')"
                      variant="solo"
                      density="compact"
                      v-model="userData.userName"
                    >
                    </v-text-field>
                  </div>
                  <div>
                    <v-text-field
                      :label="t('ID')"
                      readonly
                      variant="solo"
                      density="compact"
                      disabled
                      :model-value="useUserStore?.userInfo?.userId"
                    >
                    </v-text-field>
                  </div>
                  <div class="d-flex">
                    <v-text-field
                      :label="t('PW')"
                      readonly
                      disabled
                      :model-value="''"
                      variant="solo"
                      density="compact"
                    >
                    </v-text-field>
                    <v-btn
                      @click="windows = 'changePassword'"
                      class="customBtn"
                      rounded="lg"
                    >
                      {{ t("CHANGE") }}
                    </v-btn>
                  </div>
                  <v-form v-model="valid" ref="form" @submit.prevent="">
                    <div class="d-flex">
                      <v-text-field
                        :label="t('HP')"
                        maxLength="13"
                        variant="solo"
                        density="compact"
                        clearable
                        v-model="userData.phonenumber"
                        :rules="[rules.required, rules.phone]"
                      >
                      </v-text-field>
                    </div>
                  </v-form>
                  <div @click="execDaumPostcode()">
                    <div class="d-flex">
                      <v-text-field
                        :label="t('ZIP_CODE')"
                        readonly
                        variant="solo"
                        class="flex-grow-0 mr-2"
                        :style="{ flexBasis: '90px' }"
                        density="compact"
                        hide-details
                        v-model="userData.postnumber"
                      >
                      </v-text-field>
                      <v-text-field
                        :label="t('ADDRESS')"
                        readonly
                        variant="solo"
                        density="compact"
                        hide-details
                        no-ripple
                        v-model="userData.address"
                      >
                      </v-text-field>
                      <v-btn class="customBtn" rounded="lg"> 주소 검색 </v-btn>
                    </div>
                  </div>
                  <v-text-field
                    class="my-2"
                    :label="t('DETAIL_ADDRESS')"
                    variant="solo"
                    density="compact"
                    clearable
                    hide-details
                    v-model="userData.detailaddress"
                  >
                  </v-text-field>
                </v-sheet>

                <v-toolbar class="contentToolbar" color="transparent">
                  <v-toolbar-title>{{
                    t("TERMS_ACCEPT_STATUS")
                  }}</v-toolbar-title>
                </v-toolbar>
                <v-sheet class="contentInner">
                  <v-list
                    :style="{ marginLeft: '-8px' }"
                    density="compact"
                    class="pt-0"
                  >
                    <v-list-item density="compact" class="pl-0">
                      <template v-slot:prepend>
                        <v-list-item-action start>
                          <v-checkbox-btn
                            disabled
                            readonly
                            :model-value="true"
                          ></v-checkbox-btn>
                        </v-list-item-action>
                      </template>
                      <v-list-item-title>{{
                        t("TERMS_SERVICE")
                      }}
                      <v-btn
                          icon="mdi-text-box-outline"
                          variant="plain"
                          @click="showDetailText('termsServiceUseAg');"
                        ></v-btn></v-list-item-title>
                      <template v-slot:append>
                        
                        <v-list-item-subtitle>{{
                          useUserStore.userInfo?.serviceUseAgDt
                        }}</v-list-item-subtitle>
                      </template>
                    </v-list-item>
                    <v-list-item class="pl-0">
                      <template v-slot:prepend>
                        <v-list-item-action start>
                          <v-checkbox-btn
                            disabled
                            readonly
                            :model-value="true"
                          ></v-checkbox-btn>
                        </v-list-item-action>
                      </template>

                      <v-list-item-title>{{
                        t("TERMS_PERSONAL")
                      }}
                      <v-btn
                          icon="mdi-text-box-outline"
                          variant="plain"
                          @click="showDetailText('termsPlInfoUsingAg');"
                        ></v-btn></v-list-item-title>
                      <template v-slot:append>
                        <v-list-item-subtitle>{{
                          useUserStore.userInfo?.plInfoUsingAgDt
                        }}</v-list-item-subtitle>
                      </template>
                    </v-list-item>
                    <v-list-item class="pl-0">
                      <template v-slot:prepend>
                        <v-list-item-action start>
                          <v-checkbox-btn
                            disabled
                            readonly
                            :model-value="true"
                          ></v-checkbox-btn>
                        </v-list-item-action>
                      </template>

                      <v-list-item-title>
                        {{ t("TERMS_PERSONAL_USE") }}
                        <v-btn
                          icon="mdi-text-box-outline"
                          variant="plain"
                          @click="showDetailText('termsPlInfoStoreTimeAg');"
                        ></v-btn>
                      </v-list-item-title>
                      <template v-slot:append>
                        <v-list-item-subtitle>{{
                          useUserStore.userInfo?.plInfoUsingAgDt
                        }}</v-list-item-subtitle>
                      </template>
                    </v-list-item>
                    <v-list-item class="pl-0">
                      <template v-slot:prepend>
                        <v-list-item-action start>
                          <v-checkbox-btn
                            value="y"
                            v-model="computedAdRecvAg"
                          ></v-checkbox-btn>
                        </v-list-item-action>
                      </template>

                      <v-list-item-title>
                        {{ t("TERMS_AD") }}
                        <v-btn
                          icon="mdi-text-box-outline"
                          variant="plain"
                          @click="showDetailText('termsAdRecvAg');"
                        ></v-btn>
                      </v-list-item-title>
                      <template v-slot:append>
                        <v-list-item-subtitle>{{
                          useUserStore.userInfo?.adRecvAg === "y"
                            ? useUserStore.userInfo?.adRecvAgDt
                            : ""
                        }}</v-list-item-subtitle>
                      </template>
                    </v-list-item>
                  </v-list>
                </v-sheet>
              </v-card-text>
            </v-window-item>
            <v-window-item value="changePassword">
              <v-card-text class="pa-0">
                <v-sheet class="contentInner">
                  <v-form v-model="valid" ref="form" @submit.prevent="">
                    <v-text-field
                      v-model="changePasswordData.newPassword"
                      clearable
                      :label="t('USER_NEW_PASSWORD')"
                      type="password"
                      ref="password"
                      variant="solo"
                      @copy="false"
                      @focusout="passwordFoucsOutEvent()"
                      @click:clear="
                        passwordOutFoucsValidCheck
                          ? passwordCheck.validate()
                          : ''
                      "
                      @keyup="
                        passwordOutFoucsValidCheck
                          ? passwordCheck.validate()
                          : ''
                      "
                      density="compact"
                      :rules="[rules.required, rules.password]"
                    >
                    </v-text-field>
                    <v-text-field
                      clearable
                      :label="t('USER_CHECK_PASSWORD')"
                      type="password"
                      ref="passwordCheck"
                      variant="solo"
                      @copy="false"
                      density="compact"
                      :rules="[rules.required, rules.passwordCheck]"
                    >
                    </v-text-field>
                  </v-form>
                </v-sheet>
              </v-card-text>
            </v-window-item>
          </v-window>
          <v-sheet class="pt-7 text-center contentInner"> </v-sheet>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  <v-dialog width="70vw" v-model="openTermsDetail">
    <v-card class="customCard">
      <Terms :termsType="termsType"></Terms>
    </v-card>
  </v-dialog>
</template>
<style lang="scss"></style>
