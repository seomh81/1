<script setup>
import DefaultContainer from "@/component/DefaultContainer.vue";
import { ref, onMounted, defineAsyncComponent, computed, watch } from "vue";
import { TheTreeComp } from "@daiahub/thetreecomp";
import { ColumnChart } from "@opd/g2plot-vue";

import { v4 as uuidv4 } from "uuid";

const TreeCompTgt = ref();
const changeRadio = ref(true);
const expandAll = ref(true);
const canEdit = ref(true);
const treeHeight = `calc(100vh - ${64 + 52 + 46}px)`;
const imgHeight = `calc(100vh - ${64 + 52 + 78}px)`;
const Height = `calc( (100vh - ${64 + 52 + 78}px) / 5) `;
const elevator_sample = require("@/assets/img/elevator_sample.png");
const treeData = ref([
  {
    name: "은평 3지구 D 공구",
    tree_id: 0,
    p_tree_id: 0,
    id: uuidv4(),
    children: [
      {
        name: "통합 프로젝트 번호 - 234455423",
        tree_id: 1,
        p_tree_id: 0,
        id: uuidv4(),
        children: [
          {
            name: "L01 - #001",
            tree_id: 7,
            p_tree_id: 1,
            id: uuidv4(),
            children: [],
            rope: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            two: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            three: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            four: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            five: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
          },
          {
            name: "L02 - #002",
            tree_id: 8,
            p_tree_id: 1,
            id: uuidv4(),
            children: [],
            rope: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            two: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            three: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            four: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            five: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
          },
        ],
      },
    ],
  },

  {
    name: "포트폴리오 #2",
    tree_id: 3,
    p_tree_id: 0,
    id: uuidv4(),
    children: [
      {
        name: "원 프로젝트 번호 - 3223423",
        tree_id: 5,
        p_tree_id: 3,
        children: [
          {
            name: "계약번호 -2323",
            tree_id: 10,
            p_tree_id: 5,

            children: [],
            rope: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            two: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            three: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            four: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
            five: [
              {
                buildingNm: "현대",
                number: "111111",
                name: "A동 1호기",
                status1: "100만 km",
                status2: "점검요망",
              },
            ],
          },
        ],
      },
    ],
  },
  {
    name: "포트폴리오 #3",
    tree_id: 4,
    p_tree_id: 0,
    id: uuidv4(),
  },
]);

const data = [
  {
    label: "Mon.",
    type: "series1",
    value: 2800,
  },
  {
    label: "Mon.",
    type: "series2",
    value: 2260,
  },
];
const config = {
  height: 90,
  isGroup: true,
  xField: "value",
  yField: "label",
  seriesField: "type",
  xAxis: false,
  yAxis: false,

  legend: {
    visible: false,
  },
  /** 自定义颜色 */
  // color: ['#1383ab', '#c52125'],
  marginRatio: 0,
};
const config3 = {
  height: 140,

  appendPadding: 10,
  yField: "value",
  xField: "date",
  isStack: true,
  radius: 0.75,
  seriesField: "name",

  label: {
    // 可手动配置 label 数据标签位置
    position: "middle", // 'top', 'bottom', 'middle',
    // 配置样式
    style: {
      opacity: 0.6,
    },
  },

  data: [
    { name: "test", date: "2022.09", value: 20 },
    { name: "test2", date: "2022.09", value: 80 },
    { name: "test2", date: "2022.10", value: 50 },
  ],

  legend: false,
  annotations: [
    {
      type: "line",
      /** 起始位置 */
      start: ["min", 70],
      /** 结束位置 */

      end: ["max", 70],
      text: {
        // 旅游萧条 标注
        content: "표준",
        position: ["100%"],
        rotate: 0,
        autoRotate: false,
        offsetY: 0,
        style: {
          textAlign: "center",
          fill: "blue",
        },
      },
      style: {
        fill: "blue",

        stroke: "black",
        lineWidth: 1,
        lineDash: [4, 5],
      },
    },
  ],
};
const rope1 = 49;

const data7 = [
  {
    text: "메인 로프",
    status: "49",
    status2: "47",
    color: "#00C34F",
  },
  {
    text: "메인 로프",
    text2: "교체 필수",
    status: "90",
    status2: "50",
    color: "#E30613",
  },
  {
    text: "인버터",
    text2: "교체 권장",

    status: "60",
    status2: "50",
    color: "#FFA800",
  },
  {
    text: "인버터",

    text2: "교체 권장",

    status: "51",
    status2: "55",
    color: "#FFA800",
  },
  {
    text: "인버터",
    text2: "교체 필수",

    status: "80",
    status2: "50",
    color: "#E30613",
  },
];
const data10 = [
  {
    text: "메인 로프",
    status: "19",
    status2: "47",
    color: "#00C34F",
  },
  {
    text: "메인 로프",
    text2: "교체 필수",
    status: "70",
    status2: "50",
    color: "#E30613",
  },
  {
    text: "인버터",
    text2: "교체 권장",

    status: "55",
    status2: "50",
    color: "#FFA800",
  },
  {
    text: "인버터",

    text2: "교체 권장",

    status: "57",
    status2: "55",
    color: "#FFA800",
  },
  {
    text: "인버터",
    text2: "교체 필수",

    status: "30",
    status2: "50",
    color: "#E30613",
  },
];
const data8 = [
  {
    text: "메인 로프",
    status: "39",
    status2: "47",
    color: "#00C34F",
  },
  {
    text: "메인 로프",
    text2: "교체 필수",
    status: "100",
    status2: "50",
    color: "#E30613",
  },
  {
    text: "인버터",
    text2: "교체 권장",

    status: "55",
    status2: "50",
    color: "#FFA800",
  },
  {
    text: "인버터",

    text2: "교체 권장",

    status: "67",
    status2: "55",
    color: "#FFA800",
  },
  {
    text: "인버터",
    text2: "교체 필수",

    status: "86",
    status2: "50",
    color: "#E30613",
  },
];
const treeTarget = computed(() => TreeCompTgt.value?.GetSelectedTarget());
const treeSource = computed(() => TreeCompTgt.value?.GetSelectedSource());
const emptyData = ref([]);
watch(
  treeTarget,
  () => {
    if (treeTarget.value?.value?.tree_id == 10) {
      emptyData.value = data10;
    }
    if (treeTarget.value?.value?.tree_id == 7) {
      emptyData.value = data7;
    }
    if (treeTarget.value?.value?.tree_id == 8) {
      emptyData.value = data8;
    }
    if (!!treeTarget.value?.value == false) {
      emptyData.value = [];
    }
  },
  { deep: true }
);
watch(
  treeSource,
  () => {
    treeSource.value.forEach((source) => {
      if (source.children.length > 0) {
        nestedData(source.children);
      }
    });
  },
  { deep: true }
);
const nestedData = (child) => {
  // if (child?.children.length > 0) {
  child.forEach((item) => {
    if (item.children.length > 0) {
      return nestedData(item.children);
    } else {
      console.log(item);
    }
  });
  // }

  // if (child.children !== 0) {
  //   child.forEach((item) => {
  //     nestedData(item);
  //   });
  // } else {
  //   console.log(child);
  // }
};
const empty1 = ref(true);
const empty2 = ref(false);
</script>
<template>
  <DefaultContainer>
    <div :style="{ flexBasis: '392px' }" class="pa-4">
      <v-card>
        <v-toolbar color="transparent" density="comfortable">
          <v-toolbar-title class="largeBoldText"
            >부품 수명 예측 조회</v-toolbar-title
          >
          <v-spacer></v-spacer>
          <v-btn size="small" variant="outlined">다시 읽기</v-btn>
        </v-toolbar>
        <TheTreeComp
          class="py-4"
          ref="TreeCompTgt"
          elementid="MainTree"
          :items="treeData"
          :canEdit="canEdit"
          :expandAll="expandAll"
          :height="treeHeight"
        >
        </TheTreeComp>
      </v-card>
    </div>
    <div :style="{ flexGrow: '1' }" class="pa-4">
      <v-card>
        <v-toolbar density="comfortable" color="transparent">
          <v-btn
            :class="{ largeBoldText: empty1 }"
            variant="outlined"
            @click="[(empty1 = true), (empty2 = false)]"
          >
            호기별 진단
          </v-btn>
          <v-btn
            class="ml-5"
            :class="{ largeBoldText: empty2 }"
            variant="outlined"
            @click="[(empty1 = false), (empty2 = true)]"
          >
            품목별 진단
          </v-btn>
        </v-toolbar>
        <Tabulator
        :columns="columns"
        :tableData="tableData"
        :height="tableHeight"
        @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
        <!-- <v-container
          fluid
          v-if="emptyData.length > 0 && empty1 == true && empty2 == false"
        >
          <v-row>
            <v-col cols="4" class="d-flex align-center">
              <v-img :height="imgHeight" :src="elevator_sample" />
            </v-col>
            <v-col cols="8">
              <v-toolbar density="compact" color="transparent">
                <v-toolbar-title>호기별 진단</v-toolbar-title>
                <v-spacer></v-spacer>
                <div class="mr-2">
                  <div></div>
                  <span>1호기</span>
                </div>
                <div class="mr-4">
                  <div></div>
                  <span>동일사양</span>
                </div>
              </v-toolbar>
              <v-divider></v-divider>
              <v-container>
                <v-row v-for="(item, i) in emptyData" :key="i">
                  <v-col cols="4">
                    <div>
                      <div class="d-flex align-center flex-wrap">
                        <span class="largeBoldText">
                          {{ item.text }}
                        </span>
                        <v-btn
                          class="ml-3"
                          disabled
                          rounded="lg"
                          size="small"
                          variant="outlined"
                        >
                          보고서
                        </v-btn>
                      </div>
                      <p class="mt-1">설치 교체일 : 2001.01.01</p>
                    </div>
                  </v-col>
                  <v-col cols="4">
                    <p>운행 횟수 120,006회</p>
                    <column-chart v-bind="config3" />
                  </v-col>
                  <v-col cols="4">
                    <p>사용기간 4년</p>
                    <v-sheet color="green">ㄴ</v-sheet></v-col
                  >
                  <v-divider></v-divider>
                </v-row>
              </v-container>
            </v-col>
          </v-row>
        </v-container>
        <v-sheet
          v-if="emptyData.length === 0 && (empty2 == false || empty1 == false)"
          :height="treeHeight"
          class="d-flex justify-center align-center"
        >
          <p class="text-button">왼쪽의 리스트에서 계약을 선택해주세요.</p>
        </v-sheet>
      </v-card>
      <v-sheet v-if="empty2 == true && emptyData.length > 0">
        <v-card
          v-for="item in 4"
          :key="item"
          rounded="lg"
          elevation="1"
          class="mb-5"
        >
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
        </v-sheet> -->
      </v-card>
    </div>
    <div :style="{ flexBasis: '392px' }" class="pa-4">
      <v-card>
        <v-toolbar color="transparent" density="comfortable">
          <v-toolbar-title class="largeBoldText">점검 특이사항</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn size="small" variant="outlined">정기점검 보고서</v-btn>
        </v-toolbar>
        <v-container>
          <v-row>
            <v-col cols="12" :style="{ fontSize: '14px', fontWeight: '500' }"
              >01. 로프 마모도 Risk상 (현장명 WBS점검결과)</v-col
            >
            <v-col cols="12" :style="{ fontSize: '14px', fontWeight: '500' }"
              >02. 인버터 SMPS 전압 불량 (현장명 WBS 점검결과)</v-col
            >
            <v-col cols="12" :style="{ fontSize: '14px', fontWeight: '500' }"
              >03. 제어반 마그네틱 컨텍터 내구연한 경과로 컨택 불량...</v-col
            >
            <v-col cols="12" :style="{ fontSize: '14px', fontWeight: '500' }"
              >04. 로프 마모도 Risk상 (현장명 WBS 점검결과)</v-col
            >
            <v-col cols="12" :style="{ fontSize: '14px', fontWeight: '500' }"
              >05. 인버터 SMPS 전압 불량 (현장명 WBS 점검결과)</v-col
            >
          </v-row>
        </v-container>
      </v-card>
    </div>
  </DefaultContainer>
</template>
<style>
.chartBox {
  position: relative;
  flex-basis: 100%;
}
.bar1 {
  position: absolute;
  left: 0;
  top: 0;
  width: 1.5px;
  height: 100%;
  z-index: 1;
  background-color: #2b2c2e;
}
.bar2 {
  position: absolute;
  left: 50%;
  top: 0;
  width: 1.5px;
  height: 100%;
  z-index: 1;
  background-color: #2b2c2e;
}
</style>
