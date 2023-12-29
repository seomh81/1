<script setup>
import { ColumnChart, BarChart } from "@opd/g2plot-vue";

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
    return {
      lineWidth: 1,
      fillOpacity: 0.5,
    };
  },
};
</script>

<template>
  <bar-chart v-bind="config3" />
</template>
