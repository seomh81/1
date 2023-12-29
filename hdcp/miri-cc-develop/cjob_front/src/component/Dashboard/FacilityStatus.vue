<script setup>
import { ref, computed } from "vue";
import { ColumnChart } from "@opd/g2plot-vue";
import { useTheme } from "vuetify";
const theme = useTheme();

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
const config2 = computed(() => {
  return {
    height: 102,
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
    color: ["#00C44F", "orange", "red"],

    label: {
      visible: true,
      position: "top",
      adjustColor: true,
      style: {
        fill: theme.global.name.value == "light" ? "#000" : "#fff",
      },
    },
  };
});
</script>
<template>
  <v-card width="360" class="mt-3" elevation="0" rounded="lg">
    <v-toolbar color="transparent" density="comfortable">
      <v-toolbar-title class="largeBoldText">설비 상태</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-card-actions>
        <v-btn rounded="lg" color="primary" variant="outlined">상세보기</v-btn>
      </v-card-actions>
    </v-toolbar>
    <v-divider class="mx-4"></v-divider>

    <v-container class="py-5">
      <v-row class="smallBoldText">
        <v-col cols="6">EL (총 45대)</v-col>
        <v-col cols="6">ES (총 46대)</v-col>
        <v-col class="pt-0" cols="12">
          <v-sheet color="transparent">
            <column-chart v-bind="config2" :data="chartData" />
          </v-sheet>
        </v-col>
        <v-col cols="12">
          <p class="smallBoldText pb-1">EL 가동률</p>
          <v-progress-linear
            v-model="elPercentage"
            max="45"
            class="FacilityProgress"
            height="25"
            color="#00C44F"
            rounded="xl"
          >
            <p class="progressText">
              {{
                Math.round(
                  ((data1.normal + data1.warning + data1.error - data1.error) /
                    (data1.normal + data1.warning + data1.error)) *
                    100
                )
              }}%
            </p>
          </v-progress-linear>
        </v-col>
        <v-col>
          <p class="smallBoldText pb-1">ES 가동률</p>
          <v-progress-linear
            class="FacilityProgress"
            v-model="esPercentage"
            max="45"
            height="25"
            color="#00C44F"
            rounded="xl"
          >
            <p class="progressText">
              {{
                Math.round(
                  ((data2.normal + data2.warning + data2.error - data2.error) /
                    (data2.normal + data2.warning + data2.error)) *
                    100
                )
              }}%
            </p>
          </v-progress-linear>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>
<style lang="scss">
.FacilityProgress {
  .v-progress-linear__background {
    display: none;
  }
  .v-progress-linear__determinate {
    border-radius: 20px;
  }
}
</style>
