<script setup>
import { ref } from "vue";
import { ColumnChart } from "@opd/g2plot-vue";
const data1 = {
  normal: 34,
  warning: 10,
  error: 1,
};
const data2 = {
  normal: 20,
  warning: 12,
  error: 14,
};
const elPercentage = ref(
  data1.normal + data1.warning + data1.error - data1.error
);
const esPercentage = ref(
  data2.normal + data2.warning + data2.error - data2.error
);

const chartData = [
  { type: "테스트", value: 34, a: "el" },
  { type: "고장현황", value: 10, a: "el" },
  { type: "통과", value: 1, a: "el" },
  { type: "테스트", value: 20, a: "es" },
  { type: "고장현황", value: 12, a: "es" },
  { type: "통과", value: 14, a: "es" },
];
const config2 = {
  height: 93,
  dodgePadding: 12,

  yField: "value",
  isGroup: "true",
  xField: "a",
  xAxis: false,
  yAxis: false,
  radius: 0.75,
  legend: {
    visible: false,
  },
  columnStyle: {
    radius: [5, 5, 0, 0],
  },
  seriesField: "type",
  color: ["#00C44F", "#000000", "#B5B5B5"],

  label: {
    visible: true,
    position: "top",
    adjustColor: true,
  },
};
</script>
<template>
  <v-card width="360" class="mt-3 noticeCard" elevation="0" rounded="lg">
    <v-toolbar color="transparent" density="comfortable">
      <v-toolbar-title class="largeBoldText">설비 상태</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-card-actions>
        <v-btn rounded="lg" color="primary" variant="outlined">상세보기</v-btn>
      </v-card-actions>
    </v-toolbar>
    <v-divider class="mx-4"></v-divider>

    <v-container class="py-5">
      <v-row>
        <v-col cols="12" class="d-flex justify-space-between align-center">
          <p class="noticeText">[롯데시네마 에비뉴엘] 3호기 수리 중</p>
          <p class="text-disabled">23.01.12</p>
        </v-col>
        <v-col cols="12" class="d-flex justify-space-between align-center">
          <p class="noticeText">[롯데시네마 에비뉴엘] 11호기 민원 처리 완료</p>
          <p class="text-disabled">23.01.09</p>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>
<style lang="scss">
.noticeText {
  font-size: 14px;
}
</style>
