<script setup>
import { ColumnChart, BarChart } from "@opd/g2plot-vue";
import { useI18n } from "vue-i18n";
import { ref } from "vue";
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
  height: 150,

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
</script>
<template>
  <column-chart  v-bind="config2" />
</template>

<style scoped></style>
