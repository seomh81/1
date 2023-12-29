<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  InputContainer,
  Jodit,
} from "@/component/Template";
import { ColumnChart, BarChart } from "@opd/g2plot-vue";
import { useI18n } from "vue-i18n";
import { GridContainer, Button, DataField } from "@/component/Template";
import serviceDetail from "./sampleData/ServiceDetail.json";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { v4 as uuidv4 } from "uuid";
import { useToast } from "vue-toastification";

const img = require("@/assets/img/elevatorImg.png");
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
      // tableData.value = serviceDetail;
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

const dialog = ref(false);

const i18n = useI18n();
const desserts = [
  {
    name: "승강기 번호",
    calories: "a1234567",
  },
  {
    name: "건물명 ",
    calories: "현대캐슬",
  },
  {
    name: "설치일자",
    calories: "2009.08.01",
  },

  {
    row: 3,
    title: "정기검사",
    name: "유효기간",
    calories: "2022-07-27 ~ 2023-0",
  },
  {
    name: "주기",
    calories: "1년",
  },
  {
    name: "D DAY",
    calories: "D-251",
  },

  {
    row: 2,
    title: "유지보수",
    name: "최종 변경 일자",
    calories: "2000 - 10 - 25",
  },
  {
    name: "최종 점검일",
    calories: "2022 - 11 - 12",
  },

  {
    row: 2,
    title: "보험",
    name: "최정 검사일",
    calories: "2054 - 03 - 23",
  },
  {
    name: "기종",
    calories: "차세대 1호기",
  },

  {
    row: 2,
    title: "안전관리",
    name: "당일 총 운행거리",
    calories: "12:03:44",
  },
  {
    name: "당일 총 운행 횟수",
    calories: "3467",
  },
];
const test = [
  {
    name: "2022-01-01",
    calories: "a1234567",
    test: "결과",
    test2: "점검자",
  },
  {
    name: "2022-01-01",
    calories: 237,
    test: "결과",
    test2: "점검자",
  },
  {
    name: "2022-01-01",
    calories: 262,
    test: "결과",
    test2: "점검자",
  },
  {
    name: "2022-01-01",
    calories: "삼성캐슬",
    test: "결과",
    test2: "점검자",
  },
  {
    name: "2022-01-01",
    calories: "105동 1호기",
    test: "결과",
    test2: "점검자",
  },
];

const config1 = {
  height: 130,

  appendPadding: 10,
  yField: "value",
  xField: "date",
  isGroup: true,
  radius: 0.75,
  seriesField: "name",

  label: {
    // 可手动配置 label 数据标签位置
    position: "middle", // 'top', 'bottom', 'middle',
    // 配置样式
    style: {
      fill: "#FFFFFF",
      opacity: 0.6,
    },
  },

  data: [
    { name: "test", date: "2022.09", value: 14 },
    { name: "test", date: "2022.10", value: 27 },
    { name: "test", date: "2022.11", value: 18 },
    { name: "test2", date: "2022.09", value: 12 },
    { name: "test2", date: "2022.10", value: 15 },
    { name: "test2", date: "2022.11", value: 11 },
  ],
};
const config2 = {
  height: 140,

  appendPadding: 10,
  yField: "value",
  xField: "date",
  isGroup: true,
  radius: 0.75,
  seriesField: "name",

  label: {
    // 可手动配置 label 数据标签位置
    position: "middle", // 'top', 'bottom', 'middle',
    // 配置样式
    style: {
      fill: "#FFFFFF",
      opacity: 0.6,
    },
  },
  data: [
    { name: "test", date: "2022.09", value: 5.66 },
    { name: "test", date: "2022.10", value: 3.56 },
    { name: "test", date: "2022.11", value: 7.54 },
    { name: "test2", date: "2022.09", value: 5.41 },
    { name: "test2", date: "2022.10", value: 6.32 },
    { name: "test2", date: "2022.11", value: 4.34 },
  ],
};
const config3 = {
  height: 140,

  appendPadding: 10,
  yField: "value",
  xField: "date",
  isGroup: true,
  radius: 0.75,
  seriesField: "name",

  label: {
    // 可手动配置 label 数据标签位置
    position: "middle", // 'top', 'bottom', 'middle',
    // 配置样式
    style: {
      fill: "#FFFFFF",
      opacity: 0.6,
    },
  },
  data: [
    { name: "test", date: "2022.09", value: 557 },
    { name: "test", date: "2022.10", value: 885 },
    { name: "test", date: "2022.11", value: 368 },
    { name: "test2", date: "2022.09", value: 774 },
    { name: "test2", date: "2022.10", value: 558 },
    { name: "test2", date: "2022.11", value: 857 },
  ],
};
const config4 = {
  height: 140,

  appendPadding: 10,
  yField: "value",
  xField: "date",
  isGroup: true,
  radius: 0.75,
  seriesField: "name",

  label: {
    // 可手动配置 label 数据标签位置
    position: "middle", // 'top', 'bottom', 'middle',
    // 配置样式
    style: {
      fill: "#FFFFFF",
      opacity: 0.6,
    },
  },
  data: [
    { name: "test", date: "2022.09", value: 654 },
    { name: "test", date: "2022.10", value: 700 },
    { name: "test", date: "2022.11", value: 885 },
    { name: "test2", date: "2022.09", value: 852 },
    { name: "test2", date: "2022.10", value: 757 },
    { name: "test2", date: "2022.11", value: 174 },
  ],
};
const data = [
  {
    year: "분류",
    value: 9,
    type: "정상",
  },
  {
    year: "분류",
    value: 3,
    type: "경고",
  },
  {
    year: "분류",
    value: 1,
    type: "고장",
  },
];
// const config3 = {
//   data,
//   height: 50,
//   isStack: true,
//   legend: {
//     position: "bottom-right",
//   },
//   autoFit: true,
//   yAxis: false,
//   xAxis: false,

//   xField: "value",
//   yField: "year",
//   seriesField: "type",
//   color: ["green", "orange", "red"],
//   label: {
//     position: "middle",
//   },

//   barStyle: ({ value }) => {
//     console.log(value);
//     return {
//       lineWidth: 1,
//       fillOpacity: 0.5,
//     };
//   },
// };

const inputItems2 = ref([
  {
    type: "select",
    // title: computed(() => t("계약 #01876")),

    itemTitle: "value1",
    variant: "plain",
    items: [{ value1: "계약 #01876" }, { value1: "계약 #02467" }],
    target: "계약 #01876",
  },
  {
    type: "select",
    // title: computed(() => t("1호기")),

    itemTitle: "value1",
    variant: "plain",

    items: [{ value1: "1호기" }, { value1: "2호기" }],
    target: "1호기",
  },
]);
const buttonItems2 = ref([
  {
    size: "small",
    color: "info",
    variant: "elevated",
    title: computed(() => t("접속 중")),
    event: () => {
      // tableData.value = serviceDetail;
    },
  },
]);
const operatingStandards = [
  {
    title: "파킹 기준 층",
    value: "5층",
  },
  {
    title: "기준 층 복귀 대기시간",
    value: "0분 0초",
  },
  {
    title: "도어 대기시간",
    value: "0분 00초",
  },
  {
    title: "조명 종료 대기시간",
    value: "0시간 0분 00초",
  },
  {
    title: "팬 종료 대기시간",
    value: "0시간 0분 00초",
  },
  {
    title: "기준 층 복귀 대기층",
    value: "4층",
  },
  {
    title: "파킹 제어",
    value: "미확인",
  },
  {
    title: "장애우 도어 대기시간",
    value: "0분 00초",
  },
  {
    title: "카클 도어 대기시간",
    value: "0분 00초",
  },
  {
    title: "올클 도어 대기시간",
    value: "0분 00초",
  },
];
const emptyLife = [
  {
    title: "메인 로프",
    color: "primary",
  },
  {
    title: "메인 쉬브",
    color: "primary",
  },
  {
    title: "인버터",
    color: "primary",
  },
  {
    title: "도어 인버터",
    color: "orange",
  },
  {
    title: "도어 문닫힘 안전 장치",
    color: "red",
  },
];
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
      <v-card elevation="5" rounded="lg">
        <v-toolbar color="transparent">
          <v-toolbar-title>기본 정보</v-toolbar-title>
        </v-toolbar>
        <table class="px-1">
          <tr v-for="item in desserts" :key="item.name">
            <td v-if="item.row" :rowspan="item.row">
              {{ item.title }}
            </td>

            <td>{{ item.name }}</td>
            <td>{{ item.calories }}</td>
          </tr>
        </table>
      </v-card>
    </v-col>
    <v-col cols="8">
      <v-row>
        <v-col cols="4">
          <v-card class="elevatorBox" elevation="5" rounded="lg">
            <div class="py-2 px-4" :style="{ background: 'white' }">
              <InputContainer
                :inputItems="inputItems2"
                @update:inputChange="(value, item) => (item.target = value)"
                :buttonItems="buttonItems2"
              >
              </InputContainer>
            </div>

            <p class="floorNumber">3</p>
            <!-- <v-img :src="img" cover max-height="460" /> -->

            <div class="px-5 btnBox htrtsBtn d-flex" rounded="lg">
              <p :style="{ width: '50%' }">HTRTS 접속</p>
              <p class="text-end" :style="{ width: '50%' }">04:23:09:110</p>
            </div>
            <div class="btnBox px-5 d-flex statusBtn" rounded="lg">
              <p class="text-start operation" :style="{ width: '33.333%' }">
                올라가는 중
              </p>
              <p class="text-center" :style="{ width: '33.333%' }">멈춤</p>
              <p class="text-end" :style="{ width: '33.333%' }">내려가는 중</p>
            </div>
            <div class="d-flex btnBox2 lastBtn">
              <div
                class="btnBoxItem mr-1 px-5 d-flex align-center justify-space-between"
                rounded="lg"
              >
                <span> 출입문 </span>
                <span class="operation d-flex align-center"
                  >닫힘 <v-icon>mdi-door-closed</v-icon></span
                >
              </div>
              <div
                class="btnBoxItem ml-1 px-5 d-flex align-center justify-space-between"
                rounded="lg"
              >
                <span> 운행모드 </span>
                <span class="operation d-flex align-center"
                  >자동 <v-icon>mdi-play-box-outline</v-icon></span
                >
              </div>
            </div>
          </v-card>
        </v-col>
        <v-col cols="8">
          <v-card elevation="5" rounded="lg">
            <v-toolbar color="transparent" density="compact">
              <v-toolbar-title>운행 기준</v-toolbar-title>
            </v-toolbar>
            <v-container>
              <v-row v-for="item in operatingStandards" :key="item">
                <v-col
                  cols="4"
                  class="d-flex align-center justify-center py-0"
                  :style="{ height: '4.2vh' }"
                >
                  <p>{{ item.title }}</p>
                </v-col>
                <v-col
                  cols="4"
                  class="d-flex align-center justify-center py-0"
                  :style="{ height: '4.2vh' }"
                >
                  <v-btn size="small">조회</v-btn>
                </v-col>
                <v-col
                  cols="4"
                  class="d-flex align-center justify-center py-0"
                  :style="{ height: '4.2vh' }"
                >
                  <p>{{ item.value }}</p>
                </v-col>
              </v-row>
            </v-container>
          </v-card>
        </v-col>
      </v-row>
    </v-col>
    <v-col cols="7">
      <v-card elevation="5" rounded="lg">
        <v-toolbar color="transparent" density="compact">
          <v-toolbar-title>운행 지표</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn class="text-end">자세히 보기</v-btn>
        </v-toolbar>
        <v-container>
          <v-row>
            <v-col cols="3">
              <p class="pb-3">평균 운행 거리 (Km)</p>
              <column-chart v-bind="config1" />
            </v-col>
            <v-col cols="3">
              <p class="pb-3">평균 운행 시간 (Ms)</p>
              <column-chart v-bind="config2" />
            </v-col>
            <v-col cols="3">
              <p class="pb-3">평균 운행 횟수 (회)</p>

              <column-chart v-bind="config3" />
            </v-col>
            <v-col cols="3">
              <p class="pb-3">평균 도어 개폐 횟수 (회)</p>

              <column-chart v-bind="config4" />
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-col>
    <v-col cols="5">
      <v-card class="pb-3" elevation="5" rounded="lg">
        <v-toolbar color="transparent" density="compact">
          <v-toolbar-title>주요 부품 수명 진단</v-toolbar-title>
        </v-toolbar>
        <v-container>
          <v-row>
            <v-col v-for="item in emptyLife" :key="item">
              <v-sheet
                :color="item.color"
                rounded="lg"
                height="70px"
                class="d-flex justify-center align-center"
                ><span class="text-white">
                  {{ item.title }}
                </span>
              </v-sheet>
            </v-col>
          </v-row>
        </v-container>
        <v-toolbar color="transparent" density="compact">
          <v-toolbar-title>점검원 제안 사항</v-toolbar-title>
        </v-toolbar>
        <v-sheet class="d-flex justify-center align-center flex-wrap">
          <div>
            <p>2022-12-31일 점검 결과 도어인버터 전압 불안정 발견.</p>
            <p>도어 문닫힘 안전장치 녹발생 확인함.</p>
          </div>
        </v-sheet>
      </v-card>
    </v-col>
  </DefaultContainer>
</template>

<style scoped lang="scss">
td {
}

table {
  font-size: 0.9em;
  // box-shadow: 0 2px 5px rgba(0, 0, 0, 0.25);
  width: 100%;
  border-collapse: collapse;
  border-radius: 5px;
  overflow: hidden;
}

th {
  text-align: left;
}

thead {
}

td,
th {
  height: 3.45vh;
  vertical-align: middle;
}

.elevatorBox {
  height: 48vh;
  position: relative;
  background-image: url("@/assets/img/elevatorImg.png");
  background-size: 70% 100%;
  background-position: center;
  background-repeat: no-repeat;
  font-weight: 700;
  // background: #d9d9d9; /* fallback for old browsers */
  background-color: linear-gradient(
    to right,
    #d9d9d9,
    rgb(197, 197, 198)
  ); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

  .floorNumber {
    position: absolute;
    left: 52%;
    top: 35%;
    font-weight: 500;
    font-size: 60px;
    line-height: 100%;
    /* identical to box height, or 70px */

    text-align: center;

    color: #393939;
  }
  .btnBox {
    z-index: 2;
    padding: 18px 2px;
    background: #fff;
    opacity: 0.9;
    border: 1px solid rgba(181, 181, 181, 0.7);

    border-radius: 14px;
    width: 90%;

    &.htrtsBtn {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
      opacity: 0.3;
      bottom: 13.5vh;
    }
    &.statusBtn {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
      bottom: 7vh;
    }
  }
  .btnBox2 {
    z-index: 2;
    // padding: 14px 2px;

    border-radius: 12px;
    width: 90%;
    &.lastBtn {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
      bottom: 1vh;
    }
    .btnBoxItem {
      width: 50%;
      background: #fff;
      padding: 14px 0;
      border-radius: 14px;
      opacity: 0.9;
      border: 1px solid rgba(181, 181, 181, 0.7);
    }
  }
  .operation {
    color: #53b3f9;
  }
}
</style>
