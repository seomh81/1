<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  NAVER_AUTH_URL,
  KAKAO_AUTH_URL,
  GOOGLE_AUTH_URL,
  ACCESS_TOKEN,
  REFRESH_TOKEN,
} from "@/oauth2/oauth-url.js";
import { Carousel, ModalContainer } from "@/component/Template";
import { useRouter, useRoute } from "vue-router";
import { uiStore } from "@/store/UiStore";
import { connectionStore } from "@/store/ConnectionStore";
import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const useUiStore = uiStore();
const findIdModal = uuidv4();
const findPwModal = uuidv4();
const signUpModal = uuidv4();
const useConnectionStore = connectionStore();

const apiList = {
  signIn: {
    url: "/v2/auth/signin",
    method: "post",
    queryparam: {},
  },
  signUp: {
    url: "/v2/auth/signup",
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
};
const userState = reactive({
  userId: "test@aa.bb",
  pw: "1234",
});

const router = useRouter();

const rules = ref({
  required: (value) => !!value || "입력이 필요합니다.",

  password: (value) => {
    const pattern = value.replace(/\s/g, "");
    userData.pw = pattern;
    return !(pattern.length < 3) || "비밀번호는 3자 이상 입력해주세요.";
  },
  passwordCheck: (value) => {
    const pattern = value.replace(/\s/g, "");
    return !(userData.pw !== pattern) || "비밀번호를 확인해주세요.";
  },
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
      userData.phoneno = pattern;

      return (
        !(pattern && pattern.length < 13) || "핸드폰 번호를 모두 입력해주세요"
      );
    } else {
      userData.phoneno = "";
      console.log(value);

      return "숫자를 입력해주세요";
    }
  },
});
const form = ref();
const valid = ref(false);

const userTemp = {
  userId: "",
  pw: "",
  userNm: "",
  phoneno: "",
};
const userData = reactive({});
Object.assign(userData, userTemp);

const userEvent = async (method) => {
  await form.value.validate();
  if (valid.value) {
    const response = async (params) => {
      const { because, code, data, message, result } =
        await useConnectionStore.request(params);
      console.log(result);
      if (code === 200) {
        if (result == "success") {
          useUiStore.closeModal();
          toast.success(message);
        } else if (result == "failure") {
          console.log("정보가 잘못되었습니다");
          toast.error(t("ERROR_MESSAGE"));
        }
      } else {
        toast.error(t("ERROR_MESSAGE"));
      }
    };

    if (method == "id") {
      let params = apiList["findMyAccount"];
      params.queryparam = {
        userNm: userData.userNm,
        phoneno: userData.phoneno,
      };
      await response(params);
    }
    if (method == "password") {
      let params = apiList["findMyPassword"];
      params.queryparam = {
        userId: userData.userId,
        phoneno: userData.phoneno,
        userNm: userData.userNm,
      };
      await response(params);
    }
    if (method == "signUp") {
      let params = apiList["signUp"];
      params.queryparam = { ...userData };
      await response(params);
    }
  }
};

const loginSubmitEvent = async () => {
  await form.value.validate();
  if (valid.value) {
    const params = apiList["signIn"];
    params.queryparam = { ...userState };

    const { because, code, data, message, result } =
      await useConnectionStore.request(params);
    if (code == 200) {
      localStorage.setItem("accessToken", data.accessToken);
      toast.success(message);

      router.push("/lv3_dashboard");
    } else if (code === 400) {
      toast.error(t("ERROR_MESSAGE"));
    }
  }
};
const slidetest = ref([
  require("@/assets/img/slide1.png"),
  require("@/assets/img/slide2.png"),
  require("@/assets/img/slide3.png"),
]);

onMounted(() => {});
</script>
<template>
  <v-container class="fill-height d-flex align-center" fluid>
    <v-row align="center">
      <v-col cols="8" xl="9">
        <Carousel :img="slidetest" />
      </v-col>
      <v-col cols="4" md="4" lg="4" xl="3">
        <div class="mb-15 text-center">
          <span :style="{ fontSize: '34px' }">Login</span>
        </div>
        <v-form v-model="valid" ref="form" class="text-center">
          <v-text-field
            color="primary"
            class="fieldRadius xl"
            :label="t('STRING_ID')"
            v-model="userState.userId"
            variant="outlined"
            density="comfortable"
            :rules="[rules.required]"
            clearable
          />
          <v-text-field
            color="primary"
            class="fieldRadius xl mt-2"
            :label="t('STRING_PW')"
            v-model="userState.pw"
            variant="outlined"
            density="comfortable"
            :rules="[rules.required]"
            clearable
          />
          <v-checkbox
            color="primary"
            density="compact"
            hide-details
            :label="t('STRING_LOGIN_SAVE_ID')"
          />
          <v-btn
            block
            class="text-white mt-4"
            color="primary"
            rounded="lg"
            size="large"
            @click="loginSubmitEvent"
            >{{ t("STRING_LOGIN") }}</v-btn
          >
        </v-form>

        <v-card-actions class="justify-center mt-4">
          <v-btn
            size="small"
            class="mr-2"
            @click.prevent="
              () => {
                useUiStore.openModalEvent(findIdModal);
                Object.assign(userData, userTemp);
              }
            "
            >{{ t("STRING_LOGIN_FIND_ID") }}</v-btn
          >
          <v-divider vertical inset></v-divider>
          <v-btn
            size="small"
            class="mr-2"
            @click.prevent="
              () => {
                useUiStore.openModalEvent(findPwModal);
                Object.assign(userData, userTemp);
              }
            "
            >{{ t("STRING_LOGIN_FIND_PW") }}</v-btn
          >
          <v-divider vertical inset></v-divider>

          <v-btn
            size="small"
            @click.prevent="
              () => {
                useUiStore.openModalEvent(signUpModal);
                Object.assign(userData, userTemp);
              }
            "
            >{{ t("STRING_LOGIN_SIGN_UP") }}</v-btn
          >
        </v-card-actions>
      </v-col>
    </v-row>
  </v-container>

  <ModalContainer type="dialog" text="아이디 찾기" :id="findIdModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center find-form1">
      <v-text-field
        :label="t('STRING_NAME')"
        v-model="userData.userNm"
        variant="outlined"
        density="compact"
        class="fieldRadius xl"
        clearable
        :rules="[rules.required]"
      />
      <v-text-field
        :label="t('STRING_PHONE_NUMBER')"
        v-model="userData.phoneno"
        clearable
        variant="outlined"
        density="compact"
        class="fieldRadius xl"
        maxLength="13"
        :rules="[rules.required, rules.phone]"
      />
      <v-btn
        block
        class="text-white mt-4"
        color="primary"
        rounded="lg"
        size="large"
        @click="userEvent('id')"
        >{{ t("STRING_SAVE") }}</v-btn
      >
    </v-form>
  </ModalContainer>
  <ModalContainer type="dialog" text="비밀번호 찾기" :id="findPwModal">
    <v-form v-model="valid" ref="form" class="text-center find-form1">
      <v-text-field
        :label="t('STRING_ID')"
        v-model="userData.userId"
        variant="outlined"
        density="compact"
        class="fieldRadius xl"
        clearable
        :rules="[rules.required, rules.email]"
      />
      <v-text-field
        :label="t('STRING_NAME')"
        v-model="userData.userNm"
        variant="outlined"
        density="compact"
        class="fieldRadius xl"
        clearable
        :rules="[rules.required]"
      />
      <v-text-field
        :label="t('STRING_PHONE_NUMBER')"
        v-model="userData.phoneno"
        clearable
        variant="outlined"
        density="compact"
        class="fieldRadius xl"
        maxLength="13"
        :rules="[rules.required, rules.phone]"
      />
      <v-btn
        block
        class="text-white mt-4"
        color="primary"
        rounded="lg"
        size="large"
        @click="userEvent('password')"
        >{{ t("STRING_SAVE") }}</v-btn
      >
    </v-form>
  </ModalContainer>
</template>

<style lang="scss">
.login-box {
  border-left: 1px solid #bbb;
  margin: 30px;
  padding: 10px;
  height: 100%;
  display: flex;
  align-items: center;
  min-width: 400px;
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
