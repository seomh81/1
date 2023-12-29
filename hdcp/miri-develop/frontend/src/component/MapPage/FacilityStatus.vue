<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { ColumnChart, PieChart } from "@opd/g2plot-vue";
import { useTheme } from "vuetify";
import { connectionStore } from "@/store/ConnectionStore";
import { useRouter, useRoute } from "vue-router";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";

import { userStore } from "@/store/UserStore";
const { t } = useI18n();
const useUserStore = userStore();
const toast = useToast();
const router = useRouter();

const theme = useTheme();
const useConnectionStore = connectionStore();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const elData = ref([]);
const esData = ref([]);
const columnOptions = computed(() => {
  return {
    height: 93,
    dodgePadding: 12,

    yField: "value",
    // isGroup: "true",
    xField: "type",
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
    color: ["#00C44F", "grey"],

    label: {
      visible: true,
      position: "top",
      adjustColor: true,
      style: {
        fill: theme.global.name.value == "light" ? "#000" : "#fff",
      },
    },

    tooltip: {
      domStyles: {
        'g2-tooltip-title' : {
          display : 'none'
        },
        'g2-tooltip-value': {
          marginLeft : '0px'
        }
      }
    }
  };
});

const pieOptions = {
  height: 118,
  color: ["#00C44F", "grey"],
  innerRadius: 0.64,
  tooltip: {
    formatter: (datum) => {
      return { name: datum.type, value: `${datum.value}%` };
    },
    domStyles: {
      'g2-tooltip-value': {
        marginLeft : '0px'
      }
    }
  },
  angleField: "value",
  colorField: "type",
  radius: 0.8,
  xAxis: true,
  yAxis: false,
  label: false,
  legend: true,
  interactions: [{ type: "element-active" }],
};
const getRunAvg = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/run-avg",
      method: "post",
      queryparam: {
        userPortfolioMappingId:
          computedPortfolio?.value?.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    elData.value["pieChart"] = [
      { type: t("NORAML"), value: data.elAvg, run: data.elAvg },
      { type: t("ERROR"), value: Number((100 - data.elAvg).toFixed(2)) },
    ];
    esData.value["pieChart"] = [
      { type: t("NORAML"), value: data.esAvg, run: data.esAvg },
      { type: t("ERROR"), value: Number((100 - data.esAvg).toFixed(2)) },
    ];
  } else {
    elData.value["pieChart"] = [
      { type: t("NORAML"), value: 0, run: 0 },
      { type: t("ERROR"), value: 0 },
    ];
    esData.value["pieChart"] = [
      { type: t("NORAML"), value: 0, run: 0 },
      { type: t("ERROR"), value: 0 },
    ];
  }
};

const getRunCount = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/run-count",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    elData.value["count"] = data.el;
    elData.value["columnChart"] = [
      { type: t("NORMAL_OPERATION"), value: data.el.run },
      { type: t("NOT_OPERATION"), value: data.el.notRun },
    ];
    esData.value["count"] = data.es;
    esData.value["columnChart"] = [
      { type: t("NORMAL_OPERATION"), value: data.es.run },
      { type: t("NOT_OPERATION"), value: data.es.notRun },
    ];
  } else {
    elData.value["count"] = 0;
    elData.value["columnChart"] = [
      { type: t("NORMAL_OPERATION"), value: 0 },
      { type: t("NOT_OPERATION"), value: 0 },
    ];
    esData.value["count"] = 0;
    esData.value["columnChart"] = [
      { type: t("NORMAL_OPERATION"), value: 0 },
      { type: t("NOT_OPERATION"), value: 0 },
    ];
  }
};
watch(computedPortfolio, async (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    await getRunAvg();
    await getRunCount();
  }
});
onMounted(() => {
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getRunAvg();
    getRunCount();
  }
});
</script>
<template>
  <v-card width="360" class="mt-3 customCard" elevation="0">
    <v-toolbar color="transparent" density="comfortable">
      <v-toolbar-title class="largeBoldText">{{
        t("MAP_FACILITY_STATUS")
      }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-card-actions>
        <v-btn
          rounded="lg"
          color="primary"
          variant="outlined"
          @click="router.push('/machine/search')"
          >{{ t("VIEW_DETAIL") }}</v-btn
        >
      </v-card-actions>
    </v-toolbar>
    <v-divider class="mx-4"></v-divider>

    <v-container class="py-5">
      <v-row class="smallBoldText">
        <!-- <v-col cols="6">ES (총 46대)</v-col> -->
        <v-col class="pt-0" cols="6">
          <p class="smallBoldText pt-1 pb-4">
            EL ({{ t("TOTAL") }} {{ elData?.count?.run + elData?.count?.notRun
            }}{{ t("EL_NUMBER") }})
          </p>

          <v-sheet color="transparent">
            <column-chart v-bind="columnOptions" :data="elData?.columnChart" />
          </v-sheet>
          <p class="smallBoldText pt-4 pb-b">{{ t("RUN_AVG") }}</p>
          <v-sheet>
            <pie-chart
              v-bind="{
                ...pieOptions,
                statistic: {
                  title: false,
                  content: {
                    style: {
                      fontSize: '10px',
                    },
                    customHtml: (container, view, datum, data) => {
                      if (data[0] && typeof data[0].value !== 'undefined') {
                        return `${data[0].value}%`;
                      } else {
                        return '';
                      }
                    },
                  },
                },
              }"
              :data="elData?.pieChart"
            ></pie-chart>
          </v-sheet>
        </v-col>
        <v-col class="pt-0" cols="6">
          <p class="smallBoldText pt-1 pb-4">
            ES ({{ t("TOTAL") }} {{ esData?.count?.run + esData?.count?.notRun
            }}{{ t("EL_NUMBER") }})
          </p>

          <v-sheet color="transparent">
            <column-chart v-bind="columnOptions" :data="esData?.columnChart" />
          </v-sheet>
          <p class="smallBoldText pt-4 pb-0">{{ t("RUN_AVG") }}</p>
          <v-sheet>
            <pie-chart
              v-bind="{
                ...pieOptions,
                statistic: {
                  title: false,
                  content: {
                    style: {
                      fontSize: '10px',
                    },
                    customHtml: (container, view, datum, data) => {
                      if (data[0] && typeof data[0].value !== 'undefined') {
                        return `${data[0].value}%`;
                      } else {
                        return '';
                      }
                    },
                  },
                },
              }"
              :data="esData?.pieChart"
            ></pie-chart>
          </v-sheet>
        </v-col>
        <!-- <v-col cols="6"> -->
        <!-- <v-progress-linear
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
          </v-progress-linear> -->
        <!-- </v-col> -->
        <!-- <v-col cols="6">
          <p class="smallBoldText pb-1">ES 가동률</p>
          <pie-chart v-bind="piePlot2"></pie-chart>

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
          </v-progress-linear> -->
        <!-- </v-col> -->
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
