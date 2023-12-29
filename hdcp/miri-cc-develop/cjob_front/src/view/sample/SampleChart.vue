<script setup>
import { ref, onMounted } from "vue";
import { ColumnChart, BarChart, BulletChart } from "@opd/g2plot-vue";
import SampleMenuView from "@/view/sample/SampleMenuView.vue";
const data = [
  {
    title: "满意度",
    ranges: [100],
    measures: [80],
    target: 100,
  },
];

const bulletPlot = {
  data,
  measureField: "measures",
  rangeField: "ranges",
  targetField: "target",
  xField: "title",
  width: 300,

  height: 100,

  bulletStyle: {
    measure: {
      radius: [10,10,10,10],
      //   cursor: "pointer",
    },
  },
  color: {
    range: ["#FFbcb8", "#FFe0b0", "#bfeec8"],
    measure: "#5B8FF9",
    target: "#39a3f4",
  },
  xAxis: false,
  yAxis: false,
  label: {
    target: false,
  },
  // 自定义 legend
  legend: false,
};
const chart = ref(null);
onMounted(() => {
  console.log(chart.value);
  console.log(BulletChart);
});
</script>
<template>
  <v-container class="sample-test-container">
    <SampleMenuView></SampleMenuView>
    <bullet-chart ref="chart" v-bind="bulletPlot"></bullet-chart>
  </v-container>
</template>
