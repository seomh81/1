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
import PartsPurchase from "./sampleData/PartsPurchase.json";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
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
      title: "부품 ID",
      field: "partsId",
      sorter: "string",
    },
    {
      title: "부품명",
      field: "partsName",
      sorter: "string",
    },
    {
      title: "크기/치수",
      field: "partsSize",
      sorter: "string",
    },
    {
      title: "규격",

      field: "partsStandard",
      sorter: "string",
    },
    {
      title: "단위",
      field: "partsUnit",
      sorter: "string",
    },
    {
      title: "자재유형",
      field: "partsType",
      sorter: "string",
    },
  ];
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
      title: "서비스 유형",
      field: "serviceType",
      sorter: "string",
    },
    {
      title: "내용",
      field: "content",
      sorter: "string",
    },
    {
      title: "상태",
      field: "partsStatus",
      sorter: "string",
    },
    {
      title: "시작일시",
      field: "partsStart",
      sorter: "string",
    },
  ];
});
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
      tableData.value = PartsPurchase;
      console.log(PartsPurchase);
    },
  },
]);
const imgHeight = `calc(100vh - ${64 + 52 + 100}px)`;

const elevator_sample = require("@/assets/img/elevator_sample.png");
const createdetailList = () => {
  let arr = [];
  let a = Math.floor(Math.random() * 15);
  if (a == 0) a++;
  for (let i = 0; i < a; i++) {
    arr.push({
      partsId: "부품 아이디",
      partsName: "부품 이름",
      partsSize: "사이즈",
      partsStandard: "규격",
      partsUnit: "단위",
      partsType: "자재유형",
    });
  }
  return arr;
};
const detailTableData = ref([]);
watch([rowTarget], (newVal, oldVal) => {
  detailTableData.value = createdetailList();
});
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
    <v-col cols="4">
      <v-card elevation="1" rounded="lg">
        <Tabulator
          :columns="columns"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </v-card>
    </v-col>
    <v-col cols="8">
      <v-card>
        <v-container>
          <v-row>
            <v-col cols="4" class="d-flex align-center">
              <v-img :height="imgHeight" :src="elevator_sample" />
            </v-col>
            <v-col cols="8">
              <v-sheet color="transparent">
                <v-card rounded="lg" elevation="1" class="mb-5">
                  <v-toolbar density="compact" color="transparent">
                    <v-toolbar-title class="text-button"
                      >장비 기본 정보</v-toolbar-title
                    >
                  </v-toolbar>
                  <v-container>
                    <v-row>
                      <v-col cols="6"> 계약번호 234324234 </v-col>
                      <v-col cols="6"> 호기번호 L0-3455 </v-col>
                      <v-col cols="6"> 장비 종류 테스트1 </v-col>
                      <v-col cols="6">설치일자 2020-02-12 </v-col>
                      <v-col cols="12"
                        >주소 서울특별시 강남구 테헤란로 123 여삼빌딩</v-col
                      >
                    </v-row>
                  </v-container>
                </v-card>
                <v-card elevation="1" rounded="lg">
                  <Tabulator
                    :columns="secondColumns"
                    :tableData="detailTableData"
                    :height="`calc(100vh - ${height + 225}px`"
                    @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
                  />
                </v-card>
              </v-sheet>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
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
