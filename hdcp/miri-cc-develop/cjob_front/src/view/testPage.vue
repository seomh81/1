<script setup>
import {
  ColumnChart,
  BarChart,
} from "@opd/g2plot-vue";
import { useI18n } from "vue-i18n";
import { ref } from "vue";
import {
  GridContainer,
  Button,
  DataField,
} from "@/component/Template";
const i18n = useI18n();
const { t } = useI18n();
const desserts = [
  {
    name: "프로젝트 그룹 번호",
    calories: "a1234567",
  },
  {
    name: "프로젝트 번호",
    calories: 237,
  },
  {
    name: "호기 번호",
    calories: 262,
  },
  {
    name: "건물명",
    calories: "삼성캐슬",
  },
  {
    name: "건물 기타",
    calories: "105동 1호기",
  },
  {
    name: "설치 일자",
    calories: "2020 - 10 - 15",
  },
  {
    name: "최종 변경 일자",
    calories: "2000 - 10 - 25",
  },
  {
    name: "최종 점검일",
    calories: "2022 - 11 - 12",
  },
  {
    name: "최정 검사일",
    calories: "2054 - 03 - 23",
  },
  {
    name: "기종",
    calories: "차세대 1호기",
  },
  {
    name: "당일 총 운행거리",
    calories: "12:03:44",
  },
  {
    name: "당일 총 운행 횟수",
    calories: "3467",
  },
  {
    name: "현재 운행 대수",
    calories: "4",
  },
  {
    name: "고장 대수",
    calories: "1",
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

const config2 = {
  height: 155,

  appendPadding: 10,
  yField: "value",
  xField: "type",
  radius: 0.75,
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
    { type: "테스트", value: 27 },
    { type: "고장현황", value: 25 },
    { type: "통과", value: 18 },
    { type: "점검미실시", value: 15 },
    { type: "점검필요", value: 10 },
    { type: "승인", value: 5 },
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
const config3 = {
  data,
  height: 50,
  isStack: true,
  legend: {
    position: "bottom-right",
  },
  autoFit: true,
  yAxis: false,
  xAxis: false,

  xField: "value",
  yField: "year",
  seriesField: "type",
  color: ["green", "orange", "red"],
  label: {
    position: "middle",
  },

  barStyle: ({ value }) => {
    console.log(value);
    return {
      lineWidth: 1,
      fillOpacity: 0.5,
    };
  },
};
</script>
<template>
  <v-container fluid>
    <div >
      <div >
        <DataField
          :option="{
            type: 'input',
            label: t('STRING_KEYWORD'),
            width: '100%',
          }"
        />
        <DataField
          :option="{
            type: 'select',
            label: t('STRING_STATUS'),
            width: 160,
            fieldItems: ['KO_KR', 'EN_GN'],
          }"
        />
      </div>
      <GridContainer colType="inline-right">
        <Button
          :option="{
            append: 'mdi-magnify',
          }"
          >{{ t("STRING_SEARCH") }}</Button
        >
      </GridContainer>
    </div>

    <v-row class="pt-2">
      <v-col cols="3">
        <v-card theme="dark" elevation="5" height="84.5vh">
          <v-toolbar density="compact">
            <v-card-subtitle class="title">기본 정보</v-card-subtitle>
            <v-spacer></v-spacer>
            <v-btn>원격 점검 리포트</v-btn>
          </v-toolbar>
          <v-table>
            <tbody>
              <tr v-for="item in desserts" :key="item.name">
                <td>{{ item.name }}</td>
                <td>{{ item.calories }}</td>
              </tr>
            </tbody>
          </v-table>
        </v-card>
      </v-col>
      <v-col cols="9">
        <v-row align="center">
          <v-col cols="12">
            <v-card theme="dark" elevation="5">
              <v-toolbar density="compact">
                <v-card-subtitle>운행현황</v-card-subtitle>
                <v-btn
                  :style="{
                    width: '12px',
                    height: '12px',
                    border: '1px solid white',
                  }"
                  icon
                  color="green"
                >
                </v-btn>
                <!-- <v-item-group mandatory>
                  <v-item>
                    <v-btn
                      :style="{
                        width: '12px',
                        height: '12px',
                        border: '1px solid white',
                      }"
                      icon
                      color="green"
                    >
                    </v-btn>
                  </v-item>
                  <v-item>
                    <v-btn
                      color="orange"
                      class="mx-1"
                      :style="{
                        margin: '0 1px',
                        opacity: '0.5',
                        width: '12px',
                        height: '12px',
                        border: '1px dotted white',
                      }"
                      icon
                    >
                    </v-btn>
                  </v-item>
                  <v-item>
                    <v-btn
                      color="red"
                      :style="{
                        opacity: '0.5',
                        width: '12px',
                        height: '12px',
                        border: '1px dotted white',
                      }"
                      icon
                    >
                    </v-btn>
                  </v-item>
                </v-item-group> -->
                <v-spacer></v-spacer>
                <v-btn>
                  <span :style="{ color: 'white' }"> 상세보기 </span>
                </v-btn>
              </v-toolbar>
              <v-container fluid>
                <p class="mb-3">주요 운행 지표</p>
                <v-row>
                  <v-col cols="12" class="pb-0">
                    <v-row>
                      <v-col cols="3">
                        <column-chart v-bind="config2" />
                      </v-col>
                      <v-col cols="3">
                        <column-chart v-bind="config2" />
                      </v-col>
                      <v-col cols="3">
                        <column-chart v-bind="config2" />
                      </v-col>
                      <v-col cols="3">
                        <column-chart v-bind="config2" />
                      </v-col>
                    </v-row>
                  </v-col>

                  <v-col cols="12" class="py-0">
                    <div class="d-flex align-center justify-space-between my-5">
                      <span>주요 부품 14개 수명진단</span>
                      <DataField
                        :option="{
                          type: 'select',
                          label: t('STRING_STATUS'),
                          width: '60vw',
                          fieldItems: ['KO_KR', 'EN_GN'],
                        }"
                      />
                    </div>
                    <bar-chart
                      :style="{ width: '60vw', marginLeft: 'auto' }"
                      v-bind="config3"
                    />
                  </v-col>
                  <v-col cols="12" class="py-0">
                    <div class="d-flex align-center justify-space-between my-5">
                      <span>비 운영 계획</span>
                      <DataField
                        :option="{
                          type: 'select',
                          label: t('STRING_STATUS'),
                          width: '60vw',
                          fieldItems: ['KO_KR', 'EN_GN'],
                        }"
                      />
                    </div>
                  </v-col>
                </v-row>
              </v-container>
            </v-card>
          </v-col>

          <v-col cols="12">
            <v-row>
              <v-col cols="6">
                <v-card theme="dark" elevation="5">
                  <v-toolbar density="compact">
                    <v-card-subtitle>운행현황</v-card-subtitle>

                    <v-spacer></v-spacer>
                    <v-btn> 상세보기 </v-btn>
                  </v-toolbar>

                  <v-table theme="dark" density="compact">
                    <thead>
                      <tr>
                        <th class="text-left">점검일</th>
                        <th class="text-left">항목</th>
                        <th class="text-left">결과</th>
                        <th class="text-left">점검자(정))</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="item in test" :key="item.name">
                        <td>{{ item.name }}</td>
                        <td>{{ item.calories }}</td>
                        <td>{{ item.test }}</td>
                        <td>{{ item.test2 }}</td>
                      </tr>
                    </tbody>
                  </v-table>
                </v-card>
              </v-col>
              <v-col>
                <v-card theme="dark" elevation="5">
                  <v-toolbar density="compact">
                    <v-card-subtitle color="green">운행현황</v-card-subtitle>

                    <v-spacer></v-spacer>
                    <v-btn> 상세보기 </v-btn>
                  </v-toolbar>

                  <v-table theme="dark" density="compact">
                    <thead>
                      <tr>
                        <th class="text-left">점검일</th>
                        <th class="text-left">항목</th>
                        <th class="text-left">결과</th>
                        <th class="text-left">점검자(정))</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="item in test" :key="item.name">
                        <td>{{ item.name }}</td>
                        <td>{{ item.calories }}</td>
                        <td>{{ item.test }}</td>
                        <td>{{ item.test2 }}</td>
                      </tr>
                    </tbody>
                  </v-table>
                </v-card>
              </v-col>
            </v-row>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
  
</template>

<style scoped></style>
