<script setup>
import { ref, watch,  } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
import Terms from "@/component/terms/terms.vue";
import {
  Tabulator,

} from "@/component/Template";
import { useRouter } from "vue-router";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";
const toast = useToast();
const { t } = useI18n();
const router = useRouter();
const i18n = useI18n();
i18n.locale.value = "ko_kr";
const useConnectionStore = connectionStore();
const passwordCheck = ref();
const password = ref();
const rules = ref({
  required: (value) => !!value || t("VALID_CHECK_REQUIRED"),

  password: (value) => {
    const pattern =
      // eslint-disable-next-line
      /^(?=.*[a-zA-Z])((?=.*\d)|(?=.*\W)).{8,20}$/;
 
    if (pattern.test(value)) {
      userJoinData.value.password = value;
      return true;
    } else {
      return t("VALID_CHECK_PASSWORD");
    }
  },
  passwordCheck: (value) => {
    return (
      !(userJoinData.value.password !== value) ||
      t("VALID_CHECK_PASSWORD_CHECK")
    );
  },
  email: (value) => {
    sucessCheckId.value = false;

    const pattern =
      // eslint-disable-next-line
      /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;

    if (pattern.test(value)) {
      idCheckReady.value = true;
    } else {
      idCheckReady.value = false;

      return t("VALID_CHECK_EMAIL");
    }

  },
  phone: (value) => {
    const pattern = /[0-9]/g;

    if (pattern.test(value)) {
      let pattern = value
        .replace(/[^0-9]/g, "")
        .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
        // eslint-disable-next-line
        .replace(/(\-{1,2})$/g, "");
      userJoinData.value.phonenumber = pattern;

      return !(pattern && pattern.length < 13) || t("VALID_CHECK_PHONE_LENGTH");
    } else {
      userJoinData.value.phonenumber = "";

      return t("VALID_CHECK_PHONE_NUMBER");
    }
  },
});
const idCheckReady = ref(false);
const termsType = ref("");
const form = ref();
const valid = ref(false);
const step = ref(1);
const timeLineData = ref([
  {
    step: "STEP 1",
    title: "약관 동의",
  },
  {
    step: "STEP 2",
    title: "기본 정보",
  },
  {
    step: "STEP 3",
    title: "계약 확인",
  },
  {
    step: "STEP 4",
    title: "신청 완료",
  },
]);
const columns = [
  {
    formatter: "rowSelection",
    titleFormatter: "rowSelection",
    width: "70",
    headerSort: false,
    hozAlign: "center",
    headerHozAlign: "center",
  },
  {
    title: "프로젝트",
    field: "intgPrjNo",
    maxWidth: 120,
    sorter: "string",
  },
  {
    title: "거래선",
    field: "trlineCd",
    maxWidth: 100,
    sorter: "string",
  },
  {
    title: "건물 명",
    field: "siteNm",
    maxWidth: 150,
    sorter: "string",
  },
  {
    title: "주소",
    field: "addr",
    sorter: "string",
  },
];
const tableData = ref([]);
const rowTarget = ref([]);

const searchContractValue = ref("");
const seletedAll = ref(false);
const checkSelect1 = ref(false);
const checkSelect2 = ref(false);
const checkSelect3 = ref(false);
const checkSelect4 = ref(false);
const checkAlreadyId = ref(false);
const sucessCheckId = ref(false);
watch([checkSelect1, checkSelect2, checkSelect3, checkSelect4], () => {
  if (
    checkSelect1.value == false ||
    checkSelect2.value == false ||
    checkSelect3.value == false ||
    checkSelect4.value == false
  ) {
    seletedAll.value = false;
  }
  if (checkSelect1.value == true) {
    userJoinData.value.termsServiceUseAg = "y";
  } else {
    userJoinData.value.termsServiceUseAg = "n";
  }
  if (checkSelect2.value == true) {
    userJoinData.value.termsPlInfoUsingAg = "y";
  } else {
    userJoinData.value.termsPlInfoUsingAg = "n";
  }
  if (checkSelect3.value == true) {
    userJoinData.value.termsPlInfoStoreTimeAg = "y";
  } else {
    userJoinData.value.termsPlInfoStoreTimeAg = "n";
  }
  if (checkSelect4.value == true) {
    userJoinData.value.termsAdRecvAg = "y";
  } else {
    userJoinData.value.termsAdRecvAg = "n";
  }
});
const searchContractEvent = async () => {
  const { code, data, message, result, because } =
    await useConnectionStore.request({
      url: "/v2/contract/anonymous/search",
      method: "post",
      queryparam: {
        searchId: searchContractValue.value.replace(/-/g, ""),
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      toast.success("계약 조회에 성공했습니다.");
    } else if (data.length === 0) {
      tableData.value = data;

      toast.warning("조회된 계약 결과가 없습니다. ");
    }
    tableData.value = data;
  } else {
    toast.error(t("계약 조회에 실패했습니다."));
  }
};
const userJoinEvent = async () => {
  // eslint-disable-next-line
  let replaceNumber = userJoinData.value.phonenumber.replace(/\-/g, "");
  userJoinData.value.phonenumber = replaceNumber;
  const { code, data, message, result, because } =
    await useConnectionStore.request({
      url: "/v2/user/join/manager",
      method: "post",
      queryparam: userJoinData.value,
    });
  if (code == 200) {
    passwordOutFoucsValidCheck.value = false;
    step.value++;
    toast.success("가입 신청이 완료 되었습니다.");
  } else {
    toast.error(t("가입 신청에 실패 했습니다."));
  }
};
const userJoinData = ref({
  userId: "",
  userName: "",
  password: "",
  note: "",
  phonenumber: "",
  termsServiceUseAg: "n",
  termsPlInfoUsingAg: "n",
  termsPlInfoStoreTimeAg: "n",
  termsAdRecvAg: "n",
  lobby: [],
});
const a = () => {};
const seletedAllEvent = () => {
  if (seletedAll.value == false) {
    seletedAll.value = true;

    checkSelect1.value = true;
    checkSelect2.value = true;
    checkSelect3.value = true;
    checkSelect4.value = true;
    userJoinData.value.termsServiceUseAg = "y";
    userJoinData.value.termsPlInfoUsingAg = "y";
    userJoinData.value.termsPlInfoStoreTimeAg = "y";
    userJoinData.value.termsAdRecvAg = "y";
  } else if (seletedAll.value == true) {
    seletedAll.value = false;

    checkSelect1.value = false;
    checkSelect2.value = false;
    checkSelect3.value = false;
    checkSelect4.value = false;
    userJoinData.value.termsServiceUseAg = "n";
    userJoinData.value.termsPlInfoUsingAg = "n";
    userJoinData.value.termsPlInfoStoreTimeAg = "n";
    userJoinData.value.termsAdRecvAg = "n";
  }
};
const openTermsDetail = ref(false);
const checkUserId = async () => {
  const { code, data, message, result, because } =
    await useConnectionStore.request({
      url: "/v2/user/already",
      method: "post",
      queryparam: { userId: userJoinData.value.userId },
    });
  if (code == 200) {
    checkAlreadyId.value = data;

    if (data == false) {
      toast.success(t("SUCCESS_ALREADY_ID_GO"));
      sucessCheckId.value = true;
    }
  } else {
    toast.error(t("ERROR_ALREADY_ID"));
  }
};
const deselectedRow = ref(false);

const saveLobbyData = (val) => {
  if (userJoinData.value.lobby.length > 0) {
    if (
      userJoinData.value.lobby.some(
        (item) => item.intgPrjNo === val.intgPrjNo
      ) == false
    ) {
      userJoinData.value.lobby.push(val);
    }
  } else {
    userJoinData.value.lobby.push(val);
  }
};

const passwordOutFoucsValidCheck = ref(false);

const passwordFoucsOutEvent = async () => {
  const passwordChk = await password.value.validate();
  if (passwordChk.length == 0) {
    passwordOutFoucsValidCheck.value = true;
  } else {
    passwordOutFoucsValidCheck.value = false;
  }
};
</script>
<template>
  <v-container class="signUpContainer">
    <v-timeline side="end" direction="horizontal" line-inset="12">
      <v-timeline-item
        :dot-color="step === i + 1 ? 'primary' : ''"
        v-for="(item, i) in timeLineData"
        :key="i"
        size="small"
      >
        <template v-slot:opposite> {{ item.step }} </template>
        {{ item.title }}
      </v-timeline-item>
    </v-timeline>
    <v-card rounded="lg" class="pa-4 customCard cardPosition" max-width="900">
      <v-window v-model="step">
        <v-window-item :value="1">
          <v-card-text>
            <v-list>
              <v-list-item value="selectAll" @click="seletedAllEvent">
                <template v-slot:prepend>
                  <v-list-item-action start>
                    <v-checkbox-btn v-model="seletedAll"></v-checkbox-btn>
                  </v-list-item-action>
                </template>

                <v-list-item-title>모두 동의</v-list-item-title>
              </v-list-item>
            </v-list>
            <v-list>
              <v-list-item>
                <template v-slot:prepend>
                  <v-list-item-action start>
                    <v-checkbox-btn v-model="checkSelect1"></v-checkbox-btn>
                  </v-list-item-action>
                </template>
                <v-list-item-title> 서비스 이용 약관 </v-list-item-title>
                <template v-slot:append>
                  <v-list-item-action end>
                    <v-btn
                      icon="mdi-chevron-right"
                      @click="
                        [
                          (openTermsDetail = true),
                          (termsType = 'termsServiceUseAg'),
                        ]
                      "
                      variant="plain"
                    >
                    </v-btn>
                  </v-list-item-action>
                </template>
              </v-list-item>
              <v-list-item>
                <template v-slot:prepend>
                  <v-list-item-action start>
                    <v-checkbox-btn v-model="checkSelect2"></v-checkbox-btn>
                  </v-list-item-action>
                </template>
                <v-list-item-title>개인 정보 처리 동의서 </v-list-item-title>
                <template v-slot:append>
                  <v-list-item-action end>
                    <v-btn
                      @click="
                        [
                          (openTermsDetail = true),
                          (termsType = 'termsPlInfoUsingAg'),
                        ]
                      "
                      icon="mdi-chevron-right"
                      variant="plain"
                    >
                    </v-btn>
                  </v-list-item-action>
                </template>
              </v-list-item>
              <v-list-item>
                <template v-slot:prepend>
                  <v-list-item-action start>
                    <v-checkbox-btn v-model="checkSelect3"></v-checkbox-btn>
                  </v-list-item-action>
                </template>
                <v-list-item-title>개인 정보 처리 방침</v-list-item-title>
                <template v-slot:append>
                  <v-list-item-action end>
                    <v-btn
                      @click="
                        [
                          (openTermsDetail = true),
                          (termsType = 'termsPlInfoStoreTimeAg'),
                        ]
                      "
                      icon="mdi-chevron-right"
                      variant="plain"
                    >
                    </v-btn>
                  </v-list-item-action>
                </template>
              </v-list-item>
              <v-list-item>
                <template v-slot:prepend>
                  <v-list-item-action start>
                    <v-checkbox-btn v-model="checkSelect4"></v-checkbox-btn>
                  </v-list-item-action>
                </template>
                <v-list-item-title
                  >광고 및 마케팅 수신 동의 약관 (선택)</v-list-item-title
                >
                <template v-slot:append>
                  <v-list-item-action end>
                    <v-btn
                      @click="
                        [
                          (openTermsDetail = true),
                          (termsType = 'termsAdRecvAg'),
                        ]
                      "
                      icon="mdi-chevron-right"
                      variant="plain"
                    >
                    </v-btn>
                  </v-list-item-action>
                </template>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-window-item>

        <v-window-item :value="2">
          <v-card-text>
            <v-form v-model="valid" ref="form" @submit.prevent="">
              <div>
                <v-text-field
                  label="이름"
                  variant="solo"
                  density="compact"
                  v-model="userJoinData.userName"
                  :rules="[rules.required]"
                >
                </v-text-field>
              </div>
              <div class="d-flex mb-2">
                <v-text-field
                  clearable
                  label="아이디"
                  :error-messages="
                    checkAlreadyId == true ? t('SUCCESS_ALREADY_ID_DUPLI') : ''
                  "
                  variant="solo"
                  density="compact"
                  v-model="userJoinData.userId"
                  :rules="[rules.required, rules.email]"
                >
                </v-text-field>
                <v-btn
                  :disabled="!idCheckReady"
                  class="ml-2 customBtn"
                  rounded="lg"
                  height="40"
                  @click="checkUserId"
                >
                  {{ t("CHECK_ALREADY") }}
                </v-btn>
              </div>
              <div class="mt-2">
                <v-text-field
                  v-model="userJoinData.password"
                  clearable
                  label="비밀번호"
                  type="password"
                  ref="password"
                  variant="solo"
                  density="compact"
                  @copy="false"
                  @focusout="passwordFoucsOutEvent()"
                  :rules="[rules.required, rules.password]"
                  @click:clear="
                    passwordOutFoucsValidCheck ? passwordCheck.validate() : ''
                  "
                  @keyup="
                    passwordOutFoucsValidCheck ? passwordCheck.validate() : ''
                  "
                >
                </v-text-field>
              </div>
              <div class="mt-2">
                <v-text-field
                  clearable
                  label="비밀번호 확인"
                  type="password"
                  variant="solo"
                  @copy="false"
                  density="compact"
                  ref="passwordCheck"
                  :rules="[rules.required, rules.passwordCheck]"
                >
                </v-text-field>
              </div>
              <div class="mt-2">
                <v-text-field
                  v-model="userJoinData.phonenumber"
                  clearable
                  label="전화번호"
                  maxLength="13"
                  type="text"
                  variant="solo"
                  density="compact"
                  :rules="[rules.required, rules.phone]"
                >
                </v-text-field>
              </div>
            </v-form>
          </v-card-text>
        </v-window-item>

        <v-window-item :value="3">
          <v-card-text>
            <div class="d-flex">
              <v-text-field
                label="사업자 번호 / 승강기 번호"
                v-model="searchContractValue"
                clearable
                @keydown.enter.prevent="searchContractEvent()"
                prepend-icon="mdi-magnify"
                type="text"
                variant="solo"
                density="compact"
              >
              </v-text-field>
              <v-btn
                :disabled="searchContractValue ? false : true"
                @click="searchContractEvent"
                class="ml-2 customBtn"
                height="40"
                rounded="lg"
              >
                계약 조회
              </v-btn>
            </div>
            <Tabulator
              :pagination="true"
              :columns="columns"
              :tableData="tableData"
              :height="'40vh'"
              :keepData="true"
              @update:rowTarget="(returnVal) => saveLobbyData(returnVal)"
            />
            <v-sheet maxHeight="120px" class="mt-3 overflow-y-auto">
              <v-chip
                class="mt-2"
                color="primary"
                closable
                v-for="(item, i) in userJoinData.lobby"
                @click:close="userJoinData.lobby.splice(i, 1)"
                :key="item"
              >
                {{ item.siteNm ? item.siteNm : item.addr }}
              </v-chip>
            </v-sheet>
          </v-card-text>
        </v-window-item>
        <v-window-item :value="4">
          <v-card-text>
            <v-card-title>
              <p class="py-1">
                회원 가입 신청이 완료 되었습니다. 담당자의 승인을 대기중입니다.
              </p>
              <p class="py-1">이메일을 확인하여 주시기 바랍니다.</p>
            </v-card-title>
            <v-card-subtitle class="pt-3">
              <p class="py-1">사용 신청을 해주셔서 감사합니다.</p>
              <p class="py-1">
                본 서비스는 입주민과 건물관리자가 사용하는 서비스입니다. 서비스
                사용에는 담당자의 승인이 필요합니다.
              </p>
              <p class="py-1">
                승인이 완료되면 메일로 승인 여부를 알려드리겠습니다.
              </p>
              <p class="py-1">감사합니다.</p>
            </v-card-subtitle>
          </v-card-text>
        </v-window-item>
      </v-window>

      <v-divider></v-divider>

      <v-card-actions class="mt-2 px-4">
        <v-btn v-if="step > 1 && step !== 4" variant="text" @click="step--">
          뒤로
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
          :disabled="
            userJoinData.termsServiceUseAg === 'y' &&
            userJoinData.termsPlInfoUsingAg === 'y' &&
            userJoinData.termsPlInfoStoreTimeAg === 'y'
              ? false
              : true
          "
          v-if="step === 1"
          :color="
            userJoinData.termsServiceUseAg === 'y' &&
            userJoinData.termsPlInfoUsingAg === 'y'
              ? 'primary'
              : 'black'
          "
          @click="step++"
        >
          다음
        </v-btn>
        <v-btn
          :disabled="valid == true && sucessCheckId == true ? false : true"
          :color="valid == true && sucessCheckId == true ? 'primary' : 'black'"
          v-if="step === 2"
          @click="step++"
        >
          다음
        </v-btn>
        <v-btn
          :disabled="userJoinData.lobby.length == 0 ? true : false"
          v-if="step == 3"
          :color="userJoinData.lobby.length == 0 ? 'black' : 'primary'"
          @click="userJoinEvent"
          >회원 가입 ({{
            userJoinData.lobby.length ? userJoinData.lobby.length : "0"
          }})</v-btn
        >
        <v-btn block v-if="step == 4" color="primary" @click="router.push('/')">
          메인으로 가기
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
  <v-dialog width="70vw" v-model="openTermsDetail">
    <v-card class="customCard">
      <Terms :termsType="termsType"></Terms>
    </v-card>
  </v-dialog>
</template>
<style scoped lang="scss">
.signUpContainer {
  height: 100%;
  position: relative;
  .cardPosition {
    width: 100%;
    position: absolute;
    left: 50%;
    top: 20%;
    overflow-y: auto;
    transform: translateX(-50%);
    .firstItem {
      height: 365px;
    }
  }
}
</style>
