<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import { Carousel, ModalContainer } from "@/component/Template";
import { useRouter } from "vue-router";
import { uiStore } from "@/store/UiStore";
import { connectionStore } from "@/store/ConnectionStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
import { ls } from "@/plugin/secure";
const $cookies = inject("$cookies");
const toast = useToast();
const i18n = useI18n();
i18n.locale.value = "ko_kr";
const { t } = useI18n();
const findIdModal = ref(false);
const findPwModal = ref(false);
const useConnectionStore = connectionStore();
const saveId = ref(false);
const apiList = {
  signIn: {
    url: "/v2/auth/signin",
    method: "post",
    queryparam: {},
  },

  findMyAccount: {
    url: "/v2/user/find-account",
    method: "post",
    queryparam: {},
  },
  findMyPassword: {
    url: "/v2/user/find-password",
    method: "post",
    queryparam: {},
  },

  getMenus: {
    url: "/v2/user/current/rendering",
    method: "post",
    queryparam: {},
  },
};
const userState = reactive({
  userId: "",
  password: "",
});

const router = useRouter();

const rules = ref({
  required: (value) => !!value || "입력이 필요합니다.",

  email: (value) => {
    const pattern =
      // eslint-disable-next-line
      /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    return pattern.test(value) || "이메일을 정확하게 입력해주세요.";
  },
  phone: (value) => {
    const pattern = /[0-9]/g;

    if (pattern.test(value)) {
      let pattern = value
        .replace(/[^0-9]/g, "")
        .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
        // eslint-disable-next-line
        .replace(/(\-{1,2})$/g, "");
      userData.phonenumber = pattern;

      return (
        !(pattern && pattern.length < 13) || "핸드폰 번호를 모두 입력해주세요"
      );
    } else {
      userData.phonenumber = "";

      return "숫자를 입력해주세요";
    }
  },
});
const form = ref();
const valid = ref(false);

const userTemp = {
  userId: "",
  pw: "",
  userName: "",
  phonenumber: "",
};
const userData = reactive({});
Object.assign(userData, userTemp);

const userEvent = async (method) => {
  await form.value.validate();
  if (valid.value) {
    const request = async (params, modal, toastMessage) => {
      const { because, code, data, message, result } =
        await useConnectionStore.request(params);
      if (code === 200) {
        if (result == "success") {
          if (modal) {
            modal.value = false;
          }
          if(method == "findMyAccount")
          {
            toastMessage = data;
          }
          toast.success(toastMessage);
        } else if (result == "failure") {
          toast.warning("회원 정보가 없습니다. 입력하신 정보를 확인해주세요.");
        }
      } else {
        toast.error(t("ERROR_MESSAGE"));
      }
    };

    if (method == "findMyAccount") {
      let params = apiList["findMyAccount"];
      params.queryparam = {
        userName: userData.userName,
        // eslint-disable-next-line
        phonenumber: userData.phonenumber.replace(/\-/g, ""),
      };

      await request(
        params,
        findIdModal,
        "귀하의 이메일로 아이디를 발송했습니다."
      );
    }
    if (method == "findMyPassword") {
      let params = apiList["findMyPassword"];

      params.queryparam = {
        userId: userData.userId,
        // eslint-disable-next-line
        phonenumber: userData.phonenumber.replace(/\-/g, ""),
        // userNm: userData.userNm,
      };
      await request(
        params,
        findPwModal,
        "귀하의 이메일로 임시 패스워드를 발송했습니다."
      );
    }
  }
};

const loginSubmitEvent = async () => {
  console.log('click')
  await form.value?.validate();
  if (valid.value) {
    const params = apiList["signIn"];
    params.queryparam = { ...userState };
console.log('signin')
    const { because, code, data, message, result } =
      await useConnectionStore.request(params);
    console.log(code)
    if (code == 200) {
      if (result == "success") {
        if (saveId.value == true) {
          $cookies.set("saveId", JSON.stringify(saveId.value));
          $cookies.set("user", JSON.stringify(userState.userId));
        } else {
          $cookies.keys().forEach((cookie) => $cookies.remove(cookie));
        }
        ls.clear();
        ls.set("accessToken", data.accessToken);
        const menuParams = apiList["getMenus"];

        const menus = await useConnectionStore.request(menuParams);
        if (menus.code === 200) {
          ls.set("menus", JSON.stringify(menus.data));
          ls.set("userId", JSON.stringify(data.userId));
          ls.set("page", JSON.stringify(data.refRenderingType));
          router.push(JSON.parse(ls.get("page")));
        }
      } else {
        toast.error("아이디나 비밀번호를 확인해주세요.");
      }
    } else if (code === 400) {
      // toast.error(t("ERROR_MESSAGE"));
      toast.error(message);
    } else if (code == 401) {
      toast.warning(message);
    } else {
      toast.error(t("ERROR_API"));
    }
  }
};

onMounted(() => {
  if (
    $cookies.get("saveId") &&
    $cookies.get("saveId") == "true"
  ) {
    saveId.value = true;
    userState.userId = JSON.parse($cookies.get("user"));
  } else {
    saveId.value = false;
  }
});
</script>
<template>
  <v-container class="fill-height d-flex align-center" fluid>
    <v-row align="center">
      <v-col cols="12" md="7" lg="8" xl="9">
        <Carousel />
      </v-col>
      <v-col cols="12" md="5" lg="4" xl="3">
        <v-card class="pa-15 pb-7 customCard" rounded="xl">
          <div class="mb-15 text-center">
            <span :style="{ fontSize: '34px', fontWeight: '700' }">LOGIN</span>
          </div>

          <v-form
            v-model="valid"
            ref="form"
            class="text-center"
            @submit.prevent="loginSubmitEvent()"
          >
            <v-text-field
              color="primary"
              :label="t('ID')"
              v-model="userState.userId"
              variant="solo"
              density="compact"
              :rules="[rules.required]"
              clearable
            />
            <v-text-field
              variant="solo"
              density="compact"
              color="primary"
              type="password"
              autocomplete="off"
              class="mt-2"
              :label="t('PW')"
              v-model="userState.password"
              :rules="[rules.required]"
              clearable
            />
            <v-checkbox
              color="primary"
              v-model="saveId"
              density="compact"
              hide-details
              :label="t('LOGIN_SAVE_ID')"
            />
            <v-btn
              block
              class="text-white mt-4"
              color="primary"
              rounded="lg"
              size="large"
              type="submit"
              >{{ t("LOGIN") }}</v-btn
            >
          </v-form>

          <v-card-actions class="justify-center mt-4">
            <v-btn
              size="small"
              class="mr-2"
              @click.prevent="
                () => {
                  findIdModal = true;
                  Object.assign(userData, userTemp);
                }
              "
              >{{ t("LOGIN_FIND_ID") }}</v-btn
            >
            <v-divider vertical inset></v-divider>
            <v-btn
              size="small"
              class="mr-2"
              @click.prevent="
                () => {
                  findPwModal = true;
                  Object.assign(userData, userTemp);
                }
              "
              >{{ t("LOGIN_FIND_PW") }}</v-btn
            >
            <v-divider vertical inset></v-divider>

            <v-btn size="small" @click.prevent="router.push('/signUp')">{{
              t("LOGIN_SIGN_UP")
            }}</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

  <!-- <v-container class="fill-height d-flex align-center" fluid>
    <Carousel />
    <v-card class="pa-15 pb-7 customCard loginForm">
      <div class="mb-15 text-center">
        <span :style="{ fontSize: '34px', fontWeight: '700' }">LOGIN</span>
      </div>

      <v-form
        v-model="valid"
        ref="form"
        class="text-center"
        @submit.prevent="loginSubmitEvent()"
      >
        <v-text-field
          color="primary"
          :label="t('ID')"
          v-model="userState.userId"
          variant="solo"
          density="compact"
          :rules="[rules.required]"
          clearable
        />
        <v-text-field
          variant="solo"
          density="compact"
          color="primary"
          type="password"
          autocomplete="off"
          class="mt-2"
          :label="t('PW')"
          v-model="userState.password"
          :rules="[rules.required]"
          clearable
        />
        <v-checkbox
          color="primary"
          v-model="saveId"
          density="compact"
          hide-details
          :label="t('LOGIN_SAVE_ID')"
        />
        <v-btn
          block
          class="text-white mt-4"
          color="primary"
          rounded="lg"
          size="large"
          type="submit"
          >{{ t("LOGIN") }}</v-btn
        >
      </v-form>

      <v-card-actions class="justify-center mt-4">
        <v-btn
          size="small"
          class="mr-2"
          @click.prevent="
            () => {
              findIdModal = true;
              Object.assign(userData, userTemp);
            }
          "
          >{{ t("LOGIN_FIND_ID") }}</v-btn
        >
        <v-divider vertical inset></v-divider>
        <v-btn
          size="small"
          class="mr-2"
          @click.prevent="
            () => {
              findPwModal = true;
              Object.assign(userData, userTemp);
            }
          "
          >{{ t("LOGIN_FIND_PW") }}</v-btn
        >
        <v-divider vertical inset></v-divider>

        <v-btn size="small" @click.prevent="router.push('/signUp')">{{
          t("LOGIN_SIGN_UP")
        }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-container> -->
  <ModalContainer
    @btnEvent="userEvent('findMyAccount')"
    :text="t('LOGIN_FIND_ID')"
    v-model="findIdModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        :label="t('NAME')"
        v-model="userData.userName"
        variant="solo"
        density="compact"
        class="fieldRadius xl"
        clearable
        :rules="[rules.required]"
      />
      <v-text-field
        :label="t('HP')"
        v-model="userData.phonenumber"
        clearable
        variant="solo"
        density="compact"
        class="fieldRadius xl"
        maxLength="13"
        :rules="[rules.required, rules.phone]"
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    @btnEvent="userEvent('findMyPassword')"
    :text="t('LOGIN_FIND_PW')"
    v-model="findPwModal"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        :label="t('ID')"
        v-model="userData.userId"
        variant="solo"
        placeholder="korea@korea.com"
        density="compact"
        clearable
        :rules="[rules.required, rules.email]"
      />
      <!-- <v-text-field
        :label="t('NAME')"
        v-model="userData.userName"
        variant="solo"
        density="compact"
        class="fieldRadius xl"
        clearable
        :rules="[rules.required]"
      /> -->
      <v-text-field
        :label="t('HP')"
        v-model="userData.phonenumber"
        clearable
        variant="solo"
        density="compact"
        maxLength="13"
        :rules="[rules.required, rules.phone]"
      />
    </v-form>
  </ModalContainer>
</template>

<style lang="scss">
.loginForm {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.login-box {
  border-left: 1px solid #bbb;
  margin: 30px;
  padding: 10px;
  height: 100%;
  display: flex;
  align-items: center;
  min-width: 500px;
}
.login-form1 {
  margin: 0 20px;
}
.find-form1 {
  margin: 0 auto;
  width: 400px;
}
.socialLogo {
  width: 2rem;
  height: 2rem;
}
.find-information-box {
  margin: 0 40px;
}
.find-information-box ul {
  margin: 0;
}
.find-information-box ul li {
  margin: 10px 0;
  padding: 0;
}
.find-information-box ul li a {
  text-decoration: none;
  color: #333;
}
.find-information-box ul li a:hover {
  text-decoration: underline;
}
@media only screen and (max-width: 960px) {
  .login-box {
    border: 1px solid #bbb;
    border-radius: 3px;
    padding-top: 30px;
    padding-bottom: 30px;
    margin-bottom: 100px;
  }
}
</style>
