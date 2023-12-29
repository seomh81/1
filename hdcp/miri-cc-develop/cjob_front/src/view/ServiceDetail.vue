<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  InputContainer,
  Jodit,
} from "@/component/Template";
import serviceDetail from "./sampleData/ServiceDetail.json";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const height = 64 + 64 + 16 + 22 + 50;
// 48=header, 64 최상단 search & button  / 16 컨테이너 bottom 패딩 / 4 어디서 나왔는지 모름. no scroll
const tableHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = uuidv4();
const updateListModal = uuidv4();
const removeListModal = uuidv4();
const rowTarget = ref([]);
const tableData = ref([]);
const detailTableData = ref([]);
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
      title: "서비스 유형",
      field: "serviceType",
      sorter: "string",
    },
    {
      title: "내용",
      field: "serviceContent",
      sorter: "string",
    },
    {
      title: "상태",
      field: "status",
      sorter: "string",
    },
    {
      title: "시작일시",
      field: "start",
      sorter: "string",
    },
    {
      title: "종료일시",
      field: "end",
      sorter: "string",
    },
    {
      title: "담당자",
      field: "manager",
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
const form = ref();
const valid = ref(false);

const inputItems = ref([
  {
    cols: 2,
    title: "서비스 유형",
    type: "select",
    items: useUiStore.statusItem ? useUiStore.statusItem : [0, 9],
    itemTitle: "value1",
    target: undefined,
  },
  {
    cols: 2,
    title: "상태",
    type: "select",
    items: useUiStore.statusItem ? useUiStore.statusItem : [0, 9],
    itemtTitle: "VALUE_1",
    target: undefined,
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
]);
const buttonItems = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      tableData.value = serviceDetail;
    },
  },
]);
const buttonItem2 = ref([
  {
    title: "서비스 요청",
    event: () => {
      dialog.value = true;
    },
  },
]);
const createdetailList = () => {
  let arr = [];
  let a = Math.floor(Math.random() * 15);
  if (a == 0) a++;
  for (let i = 0; i < a; i++) {
    arr.push({
      serviceType: "타입",
      serviceContent: "서비스 콘텐츠",
      status: "정상",
      start: "2022-12-13",
      end: "2023-01-12",
      manager: "홍길동55",
    });
  }
  return arr;
};

watch([rowTarget], (newVal, oldVal) => {
  detailTableData.value = createdetailList();
});
const dialog = ref(false);
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
    <v-col cols="6">
      <v-card elevation="1" rounded="lg">
        <InputContainer :buttonItems="buttonItem2"> </InputContainer>
        <Tabulator
          :columns="columns"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </v-card>
    </v-col>
    <v-col cols="6">
      <v-sheet color="transparent" :style="{ height: tableHeight }">
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
            :columns="columns"
            :tableData="detailTableData"
            :height="`calc(100vh - ${height + 142}px`"
            @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
          />
        </v-card>
      </v-sheet>
    </v-col>
  </DefaultContainer>

  <v-dialog v-model="dialog" width="30vw">
    <v-card rounded="lg" elevation="4" class="py-3">
      <v-card-subtitle class="py-4">서비스 요청</v-card-subtitle>
      <v-container>
        <v-row>
          <v-col cols="3" class="justify-center d-flex align-center">
            계약번호
          </v-col>
          <v-col cols="9"
            ><v-select
              class="customeHeightInput select fieldRadius lg"
              label="2023-14호"
              variant="outlined"
              hide-details
              return-object
              :items="['2023-1호', '2023-2호']"
            ></v-select
          ></v-col>
          <v-col cols="3" class="justify-center d-flex align-center"
            >호기번호</v-col
          >
          <v-col cols="9"
            ><v-select
              class="customeHeightInput select fieldRadius lg"
              label="X231113"
              variant="outlined"
              hide-details
              return-object
              :items="['C22322343', 'ZX222311123']"
            ></v-select
          ></v-col>
          <v-col cols="3" class="justify-center d-flex align-center"
            >서비스 종류</v-col
          >
          <v-col cols="9"
            ><v-select
              class="customeHeightInput select fieldRadius lg"
              label="고장신고"
              variant="outlined"
              hide-details
              return-object
              :items="['기사 요청', '고장 신고']"
            ></v-select
          ></v-col>
          <v-col cols="3" class="justify-center d-flex align-center"
            >상세 내역</v-col
          >
          <v-col cols="9"
            ><v-sheet class="d-flex justify-center align-center">
              Lorem Ipsum has been the industry's standard dummy text ever when
              an unknown printer took a galley of type and scrambled it to make
              a type specimen book. It has survived not only five centuries,
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>

      <v-card-actions class="justify-end">
        <v-btn color="primary">서비스 요청</v-btn>
        <v-btn @click="dialog = false">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped></style>
