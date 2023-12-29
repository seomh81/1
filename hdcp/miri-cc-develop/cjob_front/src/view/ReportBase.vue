<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  InputContainer,
  ButtonContainer,
  Jodit,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import ReportBase from "./sampleData/ReportBase.json";
import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const height = 64 + 64 + 16 + 22;
// 48=header, 64 최상단 search & button  / 16 컨테이너 bottom 패딩 / 4 어디서 나왔는지 모름. no scroll
const tableHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = uuidv4();
const updateListModal = uuidv4();
const removeListModal = uuidv4();
const rowTarget = ref([]);
const tableData = ref([]);
const contentsDiv = ref(null);
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
      width: "70",
      headerSort: false,
      hozAlign: "center",
      headerHozAlign: "center",
    },
    {
      title: "장비구분",
      field: "equipmentType",
      sorter: "string",
    },
    {
      title: "건물명",
      field: "buildingName",
      sorter: "string",
    },
    {
      title: "호기번호",
      field: "number",
      sorter: "string",
    },
    {
      title: "보고서 종류",
      field: "reportType",
      sorter: "string",
    },
    {
      title: "점검일자",
      field: "inspectionDate",
      sorter: "string",
    },
    {
      title: "유효기간",
      field: "validity",
      sorter: "string",
      hozAlign: "center",
    },
    {
      title: "합격유무",
      field: "passYn",
      sorter: "string",
    },
  ];
});

const secondColumns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      width: "70",
      headerSort: false,
      hozAlign: "center",
      headerHozAlign: "center",
    },
    {
      title: "점검항목",
      field: "inspectionItems",
      sorter: "string",
    },
    {
      title: "점검내용",
      field: "InspectionDetails",
      sorter: "string",
    },
    {
      title: "점검주기",
      field: "inspectionDate",
      sorter: "string",
    },
    {
      title: "점검결과",
      headerHozAlign: "center",
      sorter: "string",
      columns: [
        {
          title: "직전",
          headerHozAlign: "center",
          field: "privMonth",
        },
        {
          title: "당월",
          headerHozAlign: "center",
          field: "nowMonth",
        },
      ],
    },
  ];
});
const changeSecondColumns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      width: "70",
      headerSort: false,
      hozAlign: "center",
      headerHozAlign: "center",
    },
    {
      title: "점검일자",
      field: "inspectionDate",
      sorter: "string",
    },
    {
      title: "항목",
      field: "category",
      sorter: "string",
    },
    {
      title: "검사항목명",
      field: "inspectionItems",
      sorter: "string",
    },
    {
      title: "불합격내역",
      field: "failureDetails",
      sorter: "string",
    },
    {
      title: "검사자 의견",
      field: "inspectorDetail",
      sorter: "string",
    },
  ];
});
const change = ref(false);
watch(rowTarget, () => {
  if (rowTarget.value.length == 0) {
    change.value == false;
    detailData.value = [];
  } else {
    rowTarget.value[0]?.passYn === "합격"
      ? (change.value = false)
      : (change.value = true);

    detailData.value = createdetailList();
  }
  console.log(rowTarget.value);
});
const createdetailList = () => {
  let arr = [];
  let a = Math.floor(Math.random() * 15);
  if (a == 0) a++;
  for (let i = 0; i < a; i++) {
    if (change.value == false) {
      arr.push({
        inspectionItems: "점검 항목 로우",
        InspectionDetails: "점검 내용 로우",
        inspectionDate: "점검주기 로우",
        privMonth: "직전월 로우",
        nowMonth: "당월 로우",
      });
    } else {
      arr.push({
        inspectionDate: "점검일자 로우",
        category: "항목 로우",
        inspectionItems: "검사항목명 로우",
        failureDetails: "불합격내역 로우",
        inspectorDetail: "검사자 의견 로우",
      });
    }
  }
  return arr;
};
const apiList = {
  getList: {
    url: "/v1/ad/listvideoad",
    method: "get",
    queryparam: {
      searchKeyword: "",
      status: 0,
    },
  },
  addList: {
    url: "/v1/ad/addvideoad",
    method: "post",
    queryparam: {
      title: "",
      contents: "",
    },
  },
  updateList: {
    url: "/v1/ad/updatevideoad",
    method: "post",
    queryparam: {
      videoAdId: "",
      title: "",
      contents: "",
    },
  },
  removeList: {
    url: "/v1/ad/removevideoad",
    method: "post",
    queryparam: {
      videoAdId: "",
    },
  },
};
const listTemp = {
  videoAdId: 0,
  title: "",
  contents: "",
  status: 0,
};
const listData = reactive({});
Object.assign(listData, listTemp);
const defaultStatus = computed(() => useUiStore.defaultStatus);

const search = ref({
  searchKeyword: "",
  status: useUiStore.defaultStatus ? useUiStore.defaultStatus : 0,
});
const form = ref();
const valid = ref(false);

const triggerEvent = (CRUD, type) => {
  const triggers = {
    get: () => {},
  };
  triggers[CRUD]();
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
    cols: 2,
    title: "시작일",
    type: "datePicker",
    target: undefined,
  },
  {
    cols: 2,
    title: "종료일",
    type: "datePicker",
    target: undefined,
  },
]);
const buttonItems = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      tableData.value = ReportBase;
    },
  },
]);
const detailData = ref();
const detailData2 = ref();
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
    <v-col cols="7">
      <v-card elevation="1" rounded="lg">
        <Tabulator
          :columns="columns"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </v-card>
    </v-col>
    <v-col cols="5">
      <v-sheet color="transparent" :style="{ height: tableHeight }">
        <v-card rounded="lg" elevation="1" class="mb-5">
          <v-toolbar density="compact" color="transparent">
            <v-toolbar-title class="text-button"
              >장비 기본 정보</v-toolbar-title
            >
          </v-toolbar>
          <v-container>
            <v-row>
              <v-col cols="6"> 건물명 234324234 </v-col>
              <v-col cols="6"> 소재지 테스트 </v-col>
              <v-col cols="6"> 승강기 고유번호 L0-3455 </v-col>
              <v-col cols="6"> 호기 / 설치장소 2호기 / A동 </v-col>
              <v-col cols="6"> 제조업체 테스트1 </v-col>
              <v-col cols="6"> 유지관리업체 테스트 2 </v-col>
              <v-col cols="6"> 종류 및 형식 테스트3 </v-col>
              <v-col cols="6"> 운행구간층수 1F-20F </v-col>
              <v-col cols="12">검사기관 한국</v-col>
            </v-row>
          </v-container>
        </v-card>
        <v-card elevation="1" rounded="lg">
          <Tabulator
            :columns="secondColumns"
            :tableData="detailData"
            v-if="change == false"
            :height="`calc(100vh - ${height + 272}px`"
            @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
          />
          <Tabulator
            v-if="change == true"
            :columns="changeSecondColumns"
            :tableData="detailData"
            :height="`calc(100vh - ${height + 272}px`"
            @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
          />
        </v-card>
      </v-sheet>
    </v-col>
  </DefaultContainer>

  <ModalContainer type="dialog" :text="t('STRING_AD_ADD')" :id="addListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_AD_NAME')"
        v-model="listData.title"
        :rules="[rules.required]"
        clearable
      />

      <Jodit
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
      <v-btn class="mt-3" @click="listEvent('add')">{{
        t("STRING_ADD")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_AD_UPDATE')"
    :id="updateListModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_AD_NAME')"
        v-model="listData.title"
        :rules="[rules.required]"
        clearable
      />

      <Jodit
        :contents="listData.contents"
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
      <v-btn class="mt-3" @click="listEvent('update')">{{
        t("STRING_SAVE")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer type="dialog" text="비디오 광고 삭제" :id="removeListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="listEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
</template>

<style scoped></style>
