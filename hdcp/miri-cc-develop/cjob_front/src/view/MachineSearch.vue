<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  DefaultContainer,
  InputContainer,
  Tabulator,
  ModalContainer,
  ButtonContainer,
  Jodit,
} from "@/component/Template";

import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";

import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = uuidv4();
const updateListModal = uuidv4();
const removeListModal = uuidv4();
const rowTarget = ref([]);
const tableData = ref([]);
const height = 64 + 64 + 16 + 20;
// 48=header, 64 최상단 search & button  / 16 컨테이너 bottom 패딩 / 4 어디서 나왔는지 모름. no scroll
const tableHeight = `calc(100vh - ${height}px)`;
const search = ref({
  searchKeyword: "",
  statusTarget: "",
  typeTarget: "",
});
const rules = ref({
  required: (value) => {
    return !!value || "입력이 필요합니다.";
  },
});
const columns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      maxWidth: "70",
      headerSort: false,
      hozAlign: "center",
      vertAlign: "middle",
      headerHozAlign: "center",
    },
    {
      field: "contractNumber",
      title: "원계약번호",
      sorter: "number",
    },
    {
      title: "기본정보",
      field: "",
      sorter: "string",
      headerHozAlign: "center",

      hozAlign: "start",
      columns: [
        {
          title: "종류",
          field: "elevatorType",
          sorter: "string",
        },
        {
          title: "호기번호",
          field: "expirationNumber",
          sorter: "string",
        },
        {
          title: "상태",
          field: "elevatorStatus",
          sorter: "string",
        },
        {
          title: "설치일",
          field: "installDate",
          sorter: "string",
        },
        {
          title: "검사유효기간",
          field: "Validity",
          sorter: "string",
        },
      ],
    },

    {
      title: "유지보수",
      headerHozAlign: "center",

      field: "CREATION_USER",
      sorter: "string",
      columns: [
        {
          title: "업체명",
          field: "maintenanceCompany",
          sorter: "string",
        },
        {
          title: "연락처",
          field: "maintenanceyNumber",
          sorter: "string",
        },
      ],
    },
    {
      title: "보험",
      headerHozAlign: "center",

      field: "CREATION_USER",
      sorter: "string",
      columns: [
        {
          title: "업체명",
          field: "insuranceCompany",
          sorter: "string",
        },
        {
          title: "연락처",
          field: "insuranceDate",
          sorter: "string",
        },
      ],
    },
    {
      title: "안전관리자",
      headerHozAlign: "center",

      field: "CREATION_USER",
      sorter: "string",
      columns: [
        {
          title: "이름",
          field: "SafetyManager",
          sorter: "string",
        },
        {
          title: "교육유효기간",
          field: "SafetyManagerNumber",
          sorter: "string",
        },
      ],
    },
  ];
});

const apiList = {
  getList: {
    url: "/v1/system/listfaq",
    method: "get",
    queryparam: {
      searchKeyword: "",
      status: 0,
    },
  },
  addList: {
    url: "/v1/system/addfaq",
    method: "post",
    queryparam: {
      faqId: "",
      question: "",
      answer: "",
    },
  },
  updateList: {
    url: "/v1/system/updatefaq",
    method: "post",
    queryparam: {
      faqId: "",
      question: "",
      answer: "",
    },
  },
  removeList: {
    url: "/v1/system/removefaq",
    method: "post",
    queryparam: {
      faqId: "",
    },
  },
};

const listTemp = {
  faqId: 0,
  question: "",
  answer: "",
  status: search.value.status,
};

const sampleData = ref([
  {
    contractNumber: 344334343,

    elevatorType: "type",
    expirationNumber: Math.floor(Math.random() * 20) + "호",
    elevatorStatus: 3,
    installDate: `2021-${Math.floor(Math.random() * 12)}-${Math.floor(
      Math.random() * 31
    )}`,
    Validity: `2023-${Math.floor(Math.random() * 12)}-${Math.floor(
      Math.random() * 31
    )}`,
    maintenanceCompany: "테스트업체",
    maintenanceyNumber: "010-2322-2333",
    insuranceCompany: "테스트보험",
    insuranceDate: +`2011-${Math.floor(Math.random() * 12)}-${Math.floor(
      Math.random() * 31
    )} - 2022-${Math.floor(Math.random() * 12)}-${Math.floor(
      Math.random() * 31
    )}`,
    SafetyManager: "홍길동",
    SafetyManagerNumber: "010-2322-2222",
  },
]);

const listData = reactive({});

const form = ref();
const valid = ref(false);
const elevatorStatusItem = ref([]);
const elevatorTypeItem = ref([]);

onMounted(() => {
  console.log(search.value);
});
const defaultStatus = computed(() => useUiStore.defaultStatus);

const getListEvent = async () => {
  // let params = apiList["getList"];
  // params.queryparam = {
  //   searchKeyword: search.value.searchKeyword,
  //   status: search.value.status.CODE
  //     ? search.value.status.CODE
  //     : search.value.status,
  // };

  // const { status, code, message, resultset } = await useConnectionStore.request(
  // params
  // );

  // resultset.sort((a, b) => {
  //   return a.FAQ_ID - b.FAQ_ID;
  // });
  // if (code == 200) {
  //   resultset.forEach((x) => {
  //     x.statusString = search.value.status.VALUE_1;
  //   });
  //   tableData.value = resultset;
  // } else {
  //   toast.error(t("ERROR_MESSAGE"));
  // }
  let item = [];
  for (let i = 0; i < 15; i++) {
    item.push({
      contractNumber: Math.floor(Math.random() * 5555555),

      elevatorType: "type",
      expirationNumber: Math.floor(Math.random() * 20) + "호",
      elevatorStatus: Math.floor(Math.random() * 4),
      installDate: `2021-${Math.floor(Math.random() * 12)}-${Math.floor(
        Math.random() * 31
      )}`,
      Validity: `2023-${Math.floor(Math.random() * 12)}-${Math.floor(
        Math.random() * 31
      )}`,
      maintenanceCompany: "테스트업체",
      maintenanceyNumber: "010-2322-2333",
      insuranceCompany: "테스트보험",
      insuranceDate: +`2011-${Math.floor(Math.random() * 12)}-${Math.floor(
        Math.random() * 31
      )} - 2022-${Math.floor(Math.random() * 12)}-${Math.floor(
        Math.random() * 31
      )}`,
      SafetyManager: "홍길동",
      SafetyManagerNumber: "010-2322-2222",
    });
  }
  tableData.value = item;
  rowTarget.value = [];
};

const listEvent = async (method) => {
  const response = async (params) => {
    const { status, code, message, resultset } =
      await useConnectionStore.request(params);
    if (code == 200) {
      await getListEvent();
      useUiStore.closeModal();
      toast.success(message);
    } else {
      toast.error(t("ERROR_MESSAGE"));
    }
  };
  if (method != "remove") {
    await form.value.validate();
  }

  if (method != "remove") {
    await form.value.validate();
  }

  if (valid.value) {
    if (method == "add") {
      let params = apiList["addList"];
      params.queryparam = { ...listData };
      await response(params);
    }
    if (method == "update") {
      let params = apiList["updateList"];
      params.queryparam = { ...listData };
      await response(params);
    }
  }

  if (method == "remove") {
    let params = apiList["removeList"];

    // params.queryparam = ;
    let mapObj = {
      faqIds: [],
    };
    rowTarget.value.forEach((item) => {
      mapObj.faqIds.push(item.FAQ_ID);
    });
    params.queryparam = mapObj;
    await response(params);
  }
};

const inputItems = ref([
  {
    type: "field",
    label: computed(() => t("STRING_KEYWORD")),
    target: computed({
      get() {
        return search.value.searchKeyword;
      },
      set(e) {
        search.value.searchKeyword = e;
      },
    }),
  },
  {
    cols: 3,
    type: "select",
    title: computed(() => t("장비 종류")),

    itemTitle: "value1",

    items: [{value1:'전체'},{value1:'엘리베이터'},{value1:'에스컬레이터'},{value1:'무빙워크'}],
    // target: computed({
    //   get() {
    //     return search.value.typeTarget;
    //   },
    //   set(e) {
    //     search.value.typeTarget = e;
    //   },
    // }),
  },
  {
    cols: 3,
    type: "select",
    title: computed(() => t("STRING_STATUS")),

    items: [{value1:'전체'},{value1:'정상'},{value1:'고장'},{value1:'비가동'}],
    itemTitle: "value1",
    // target: computed({
    //   get() {
    //     return search.value.statusTarget;
    //   },
    //   set(e) {
    //     search.value.statusTarget = e;
    //   },
    // }),
  },
]);
const buttonItems = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      getListEvent();
    },
  },
]);
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-1">
      <InputContainer
        :inputItems="inputItems"
        @update:inputChange="(value, item) => (item.target = value)"
        :buttonItems="buttonItems"
      >
      </InputContainer>
    </v-col>
    <v-col cols="12">
      <v-card elevation="2" rounded="lg">
        <Tabulator
          :columns="columns"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </v-card>
    </v-col>
  </DefaultContainer>

  <ModalContainer type="dialog" :text="t('STRING_FAQ_ADD')" :id="addListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-end">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_FAQ_QUESTION')"
        v-model="listData.question"
        :rules="[rules.required]"
        clearable
      />
      <Jodit @update:contents="(returnVal) => (listData.answer = returnVal)" />
      <v-btn class="mt-3" @click="listEvent('add')">{{
        t("STRING_ADD")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_FAQ_UPDATE')"
    :id="updateListModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-end">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_FAQ_QUESTION')"
        v-model="listData.question"
        :rules="[rules.required]"
        clearable
      />
      <Jodit
        :contents="listData.answer"
        @update:contents="(returnVal) => (listData.answer = returnVal)"
      />
      <v-btn class="mt-3" @click="listEvent('update')">{{
        t("STRING_UPDATE")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_FAQ_REMOVE')"
    :id="removeListModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="listEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
</template>
<style lang="scss">
.animation-init {
  opacity: 0;
  transform: translateY(-5px);
}

.animation-fade {
  opacity: 1;
  transform: translateY(0);
  transition: ease-in-out 0.4s;
}
.animation-leave {
  opacity: 0;
  transform: translateY(-5px);
  transition: ease-in-out 0.4s;
}
</style>
