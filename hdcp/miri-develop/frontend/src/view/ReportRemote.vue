<script setup>
import {
  ref,
  reactive,
  inject,
  computed,
  onMounted,
  watch,
  provide,
  onBeforeUnmount,
} from "vue";
import {
  Tabulator,
  DefaultContainer,
  SearchBarContainer,
  ParallelSpiner,
  Skeleton,
} from "@/component/Template";
import { ColumnChart, BarChart, GaugeChart } from "@opd/g2plot-vue";
import { dataStore } from "@/store/DataStore";
import { userStore } from "@/store/UserStore";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
import dayjs from "dayjs";
import axios from "@/plugin/axios";
const startDate = dayjs().subtract(2, "month").format("YYYY-MM-DD");
const selectedFirstRow = ref(false);
const toast = useToast();
const useDataStore = dataStore();
const parallelSpiner = ref(false);
const { t } = useI18n();
const height = 64 + 64 + 12;
const tableHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();
const useUserStore = userStore();
const year = new Date().getFullYear();
const month = new Date().getMonth() + 1;
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const date = new Date().getDate();
const toDay = `${year}-${month.toString().padStart(2, "0")}-${date
  .toString()
  .padStart(2, "0")}`;
const rowTarget = ref([]);
const tableData = ref([]);
const elInfoData = ref([]);
const performanceData = ref([]);
const columns = computed(() => {
  return [
    {
      title: t("MACHINE_TYPE"),
      field: "elvtrDivNm",
      sorter: "string",
    },
    {
      title: t("BULD_NM"),
      field: "buldNm",
      sorter: "string",
    },
    {
      title: t("ELEVATOR_NO"),
      field: "elevatorNo",
      sorter: "string",
    },

    {
      title: t("SELF_INSPECTION_DT"),
      field: "replaceChkDate",
      sorter: "string",
    },
  ];
});
const chartData = ref({
  elAvgRunDistance: [],
  elAvgRunTime: [],
  elAvgRunNum: [],
  elAvgDocc: [],
});
const columnsConfig = {
  appendPadding: 0,
  yField: "value",
  xField: "date",
  dodgePadding: 5,
  isGroup: true,
  seriesField: "type",
  color: ["#9bb2c2", "#00C44F"],

  legend: {
    visible: false,
  },
  columnStyle: {
    radius: [5, 5, 0, 0],
  },
  // color: ["#00C44F", "grey"],
  tooltip: {
    TextBackgroundStyle: "red",
  },
  label: false,
};

const gaugeConfig = {
  percent: 0.75,
  radius: 0.75,
  height: 125,
  type: "meter",
  range: {
    ticks: [0, 1 / 3, 2 / 3, 1],
    color: ["#30BF78", "#FAAD14", "#F4664A"],
  },
  gaugeStyle: {
    lineCap: "round",
  },
  axis: {
    label: {
      formatter(v) {
        return Number(v) * 10;
      },
    },
    subTickLine: {
      count: 1,
    },
  },
  indicator: {
    pointer: {
      style: {
        stroke: "#D0D0D0",
      },
    },
    pin: {
      style: {
        stroke: "#D0D0D0",
      },
    },
  },
  statistic: {
    content: {
      style: {
        fontSize: "12px",
        lineHeight: "16px",
      },
      formatter: () => "시간",
    },
  },
};
watch(rowTarget, () => {
  if (rowTarget.value.length !== 0) {
    getTargetData();
  }
});
const searchBar = reactive({
  inputs: [
    {
      name: "keyword",
      type: "field",
      label: computed(() => t("PRJ_NO")),
      target: useDataStore.targetElvatorNumber ?? "",
      event: () => {
        if (
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 0 ||
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 9
        ) {
          getList();
        } else {
          toast.warning(t("WARNING_SEARCH_PRJ_NO_LENGTH"));
        }
      },
    },
    {
      name: "startDate",
      cols: 2,
      title: computed(() => t("START_DAY")),
      type: "datePicker",
      target: startDate,
    },
    {
      name: "endDate",
      cols: 2,
      title: computed(() => t("END_DAY")),
      type: "datePicker",
      target: toDay,
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",
      title: computed(() => t("BTN_SEARCH")),
      event: () => {
        if (
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 0 ||
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 9
        ) {
          getList();
        } else {
          toast.warning(t("WARNING_SEARCH_PRJ_NO_LENGTH"));
        }
      },
    },
  ],
});

const getList = async () => {
  const { status, code, message, data, because } =
    await useConnectionStore.request({
      url: "/v2/hrts/manage/master/list",
      method: "post",
      queryparam: {
        userPortfolioMappingId:
          useUserStore.portfolio.selected.userPortfolioMappingId,
        keyword: searchBar.inputs.find((item) => item.name == "keyword").target,
        startDate: searchBar.inputs
          .find((item) => item.name == "startDate")
          .target.replace(/-/g, ""),
        endDate: searchBar.inputs
          .find((item) => item.name == "endDate")
          .target.replace(/-/g, ""),
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      rowTarget.value = [];
      tableData.value = [];
      elInfoData.value = [];
      performanceData.value = [];
      data.forEach((item) => {
        if (item.chkDate) {
          item.replaceChkDate = item.chkDate.replace(
            /(\d{4})(\d{2})(\d{2})/g,
            "$1-$2-$3"
          );
        }
      });
      selectedFirstRow.value = true;
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_REPORT_REMOTE")]));
    }
    tableData.value = data;
  } else {
    toast.warning(t("ERROR_SEARCH", [t("MENU_REPORT_REMOTE")]));
  }
};

const getTargetData = async () => {
  elInfoData.value = [];
  chartData.value = {
    elAvgRunTime: [],
    elAvgRunDistance: [],
    elAvgRunNum: [],
    elAvgDocc: [],
  };
  const getElInfo = async () => {
    const { because, code, data, message, result } =
      await useConnectionStore.request({
        url: "/v2/hrts/manage/master/ai",
        method: "post",
        queryparam: {
          projNo: rowTarget.value[0].projNo.slice(0, -3),
          hoNo: rowTarget.value[0].projNo.slice(-3),
        },
      });
    if (code == 200) {
      return data;
    } else {
      toast.error(t("ERROR_SEARCH", [t("MACHINE_BASIC_INFO")]));
    }
  };
  const getPerformanceResult = async () => {
    performanceData.value = [];

    const { status, code, message, data, because } =
      await useConnectionStore.request({
        url: "/v2/hrts/car/performance-result",
        method: "post",
        queryparam: {
          prjNo: rowTarget.value[0].projNo.slice(0, -3),
          carNo: rowTarget.value[0].projNo.slice(-3),
          requestDate: rowTarget.value[0].chkDate,
        },
      });
    if (code == 200 && !!data) {
      return data;
    } else if (code >= 400) {
      toast.error(t("ERROR_SEARCH", [t("PERFORMANCE_RESULT")]));
    }
  };
  const getRunInfo = async () => {
    const { status, code, message, data, because } =
      await useConnectionStore.request({
        url: "/v2/hrts/car/run-info",
        method: "post",
        queryparam: {
          prjNo: rowTarget.value[0].projNo.slice(0, -3),
          carNo: rowTarget.value[0].projNo.slice(-3),
          requestDate: rowTarget.value[0].chkDate,
        },
      });
    if (code == 200 && !!data) {
      let obj = {
        type: data.type,
        target: data.target,
      };
      return obj;
    } else if (code >= 400) {
      toast.error(t("ERROR_SEARCH", [t("AVG_RUN_INFO")]));
    }
  };
  const getRunAvgTime = async () => {
    const { status, code, message, data, because } =
      await useConnectionStore.request({
        url: "/v2/hrts/car/run-time/average",
        method: "post",
        queryparam: {
          prjNo: rowTarget.value[0].projNo.slice(0, -3),
          carNo: rowTarget.value[0].projNo.slice(-3),
          requestDate: rowTarget.value[0].chkDate,
        },
      });
    if (code == 200 && !!data) {
      return data[0];
    } else if (code >= 400) {
      toast.error(t("ERROR_SEARCH", [t("NOW_MONTH_DAY_AVG_TIME")]));
    }
  };
  try {
    parallelSpiner.value = true;
    const [elInfo, PerformanceResult, runInfo, avgTime] = await Promise.all([
      getElInfo(),
      getPerformanceResult(),
      getRunInfo(),
      getRunAvgTime(),
    ]);
    elInfoData.value = elInfo;
    performanceData.value = PerformanceResult;
    gaugeConfig.percent = Number(avgTime.elAvgRunTime) / 10;
    for (const item in runInfo) {
      processingData(runInfo, item);
    }
    parallelSpiner.value = false;
  } catch {
    parallelSpiner.value = false;
  }
};

provide("searchBar", searchBar);
const processingData = (data, key) => {
  data[key].forEach((item) => {
    const date = `${item.yyyy}-${item.mm}`;
    chartData.value.elAvgRunDistance.push({
      type: key === "target" ? t("TARGET_HO") : t("AVG"),
      date: date,
      value:
        key === "target"
          ? parseFloat(item.elAvgRunDistance)
          : parseFloat(item.totAvgRunDistance),
    });
    chartData.value.elAvgRunTime.push({
      type: key === "target" ? t("TARGET_HO") : t("AVG"),
      date: date,
      value:
        key === "target"
          ? parseFloat(item.elAvgRunTime)
          : parseFloat(item.totAvgRunTime),
    });
    chartData.value.elAvgRunNum.push({
      type: key === "target" ? t("TARGET_HO") : t("AVG"),
      date: date,
      value:
        key === "target"
          ? parseFloat(item.elAvgRunNum)
          : parseFloat(item.totAvgRunNum),
    });
    chartData.value.elAvgDocc.push({
      type: key === "target" ? t("TARGET_HO") : t("AVG"),
      date: date,
      value:
        key === "target"
          ? parseFloat(item.elAvgDocc)
          : parseFloat(item.totAvgDocc),
    });
  });
};
const downloadReport = async () => {
  const data = await axios({
    url: "/v2/hrts/manage/master/ai/report",
    method: "post",
    data: {
      projNo: rowTarget.value[0].projNo.slice(0, -3),
      hoNo: rowTarget.value[0].projNo.slice(-3),
      yyyymm: rowTarget.value[0].chkDate.slice(0, -2),
    },
    responseType: "blob",
  });
  if (data.size > 0) {
    const url = window.URL.createObjectURL(
      new Blob([data], { type: "application/octet-stream" })
    );
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute(
      "download",
      `
  ${rowTarget.value[0].projNo} (${rowTarget.value[0].buldNm}) - 원격 점검 리포트.xlsx`
    );
    document.body.appendChild(link);
    link.click();
  } else {
    toast.warning(t("WARNING_NO_REPORT"));
  }
};

onMounted(() => {
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getList();
  }
});

watch(computedPortfolio, async (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    getList();
  }
});
onBeforeUnmount(() => {
  if (useDataStore.targetElvatorNumber) {
    useDataStore.setElvatorNumber("");
  }
});
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="12" lg="4" class="pt-0">
      <Skeleton :loadingData="tableData.length == 0" :height="tableHeight">
        <Tabulator
          :pagination="true"
          :columns="columns"
          :selectedFirstRow="selectedFirstRow"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </Skeleton>
    </v-col>
    <v-col cols="12" lg="8" class="pt-0 overflow-y-auto">
      <v-toolbar density="compact" color="transparent" >
        <div style="display: inline-block; width:100%;">
        <v-toolbar-title style="float:left;" class="d-flex align-center" >
          <span>
            {{ t("REMOTE_REPORT") }}
          </span>
        </v-toolbar-title>
          <div style="float:right;vertical-align: center;">
            <v-tooltip location="left">
            <template v-slot:activator="{ props }">
              <v-icon v-bind="props" style="margin:0 10px 0 0;" class="ml-1">mdi-help-circle-outline</v-icon>
            </template>
            <span>{{ t("REPORT_REMOTE_TOOLTIP") }}</span>
            </v-tooltip>
            <v-btn
              variant="outlined"
              color="primary"
              rounded="lg"
              :disabled="!rowTarget?.length == 1"
              @click="downloadReport"
              >{{ t("DOWNLOAD") }}</v-btn
            >
          </div>
        </div>
      </v-toolbar>
      <v-container fluid class="px-0">
        <v-row align="center">
          <v-col cols="8" class="pt-0">
            <Skeleton
              :loadingData="!!elInfoData?.elProjNo == false"
              :height="'180px'"
              class="px-2"
              :type="'heading,paragraph'"
            >
              <v-toolbar density="compact" color="transparent">
                <v-toolbar-title class="text-button">{{
                  `
                  
                  ${rowTarget[0]?.buldNm} - ${elInfoData?.dongcarNm ?? ""} 
                  `
                }}</v-toolbar-title>
              </v-toolbar>
              <v-container>
                <v-row>
                  <v-col cols="4" class="py-2">
                    <v-card-subtitle class="pl-0 mb-1">
                      {{ t("PRJ_NO") }}
                    </v-card-subtitle>

                    {{ elInfoData?.elProjNo ?? "" }}
                  </v-col>
                  <v-col cols="4" class="py-2">
                    <v-card-subtitle class="pl-0 mb-1">
                      {{ t("ELEVATOR_NO") }}
                    </v-card-subtitle>
                    {{ rowTarget[0]?.elevatorNo }}
                  </v-col>

                  <v-col cols="4" class="py-2">
                    <v-card-subtitle class="pl-0 mb-1">
                      {{ t("INSTALL_DT") }}
                    </v-card-subtitle>

                    {{
                      elInfoData?.setupDate.replace(
                        /^(\d{4})(\d{2})(\d{2})$/,
                        "$1-$2-$3"
                      ) ?? ""
                    }}
                  </v-col>
                  <v-col cols="4" class="py-2">
                    <v-card-subtitle class="pl-0 mb-1">
                      {{ t("MODEL") }}
                    </v-card-subtitle>

                    {{ elInfoData?.model ?? "" }}
                  </v-col>

                  <v-col cols="8" class="py-2">
                    <v-card-subtitle class="pl-0 mb-1">
                      {{ t("SITE") }}
                    </v-card-subtitle>

                    {{ elInfoData?.address ?? "" }}
                  </v-col>
                </v-row>
              </v-container>
            </Skeleton>
          </v-col>
          <v-col cols="4" class="pt-0">
            <Skeleton
              :loadingData="!!elInfoData?.elProjNo == false"
              type="image"
              :height="'180px'"
              :padding="true"
            >
              <p>{{ t("NOW_MONTH_DAY_AVG_TIME") }}</p>

              <gauge-chart class="test" v-bind="gaugeConfig" />
            </Skeleton>
          </v-col>
        </v-row>
      </v-container>

      <v-toolbar density="compact" color="transparent">
        <span class="ml-4">
          {{ t("AVG_RUN_INFO") }}
        </span>
        <span class="d-flex ml-8">
          <div class="d-flex align-center">
            <div
              class="mr-2"
              :style="{
                borderRadius: '50%',
                width: '8px',
                height: '8px',
                background: '#9bb2c2',
              }"
            ></div>
            {{ t("AVG") }}
          </div>
          <div class="ml-8 d-flex align-center">
            <div
              class="mr-2"
              :style="{
                borderRadius: '50%',
                width: '8px',
                height: '8px',
                background: '#00C44F',
              }"
            ></div>
            {{ t("TARGET_HO") }}
          </div>
        </span>
      </v-toolbar>
      <v-container fluid class="px-0">
        <v-row align="center">
          <v-col
            class="pt-0"
            cols="3"
            v-for="(item, key, idx) in chartData"
            :key="idx"
          >
            <Skeleton
              :padding="true"
              :loadingData="!!elInfoData?.elProjNo == false"
              :height="'calc(100vh - 669px)'"
              type="image"
            >
              <p>
                {{
                  key == "elAvgRunDistance"
                    ? t("DAY_AVG_DISTANCE")
                    : key == "elAvgRunTime"
                    ? t("DAY_AVG_TIME")
                    : key == "elAvgRunNum"
                    ? t("DAY_AVG_RUN_NUM")
                    : key == "elAvgDocc"
                    ? t("DAY_AVG_DOOR_OPEN_CLOSE")
                    : ""
                }}
              </p>
              <column-chart
                class="myCustomChart2"
                :data="[...item]"
                v-bind="columnsConfig"
              />
            </Skeleton>
          </v-col>
        </v-row>
      </v-container>

      <v-toolbar density="compact" color="transparent">
        <v-toolbar-title>{{ t("PERFORMANCE_RESULT") }}</v-toolbar-title>
      </v-toolbar>
      <Skeleton
        :loadingData="!performanceData.ftotalExe"
        type="table-row,table-row,table-row"
        :height="'165px'"
      >
        <v-table density="compact" class="reportTable">
          <thead>
            <tr>
              <th class="text-left">{{ t("TYPE") }}</th>
              <th class="text-left">{{ t("RESULT_FORWARD_BACK") }}</th>
              <th class="text-left">{{ t("OTHERS_NOTE") }}</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{{ t("REMOTE_SYSTEM") }}</td>
              <td class="d-flex justify-space-between align-center">
                {{ t("AVG_STABIL") }} {{ `${performanceData.ftotalExe}%` }} /
                {{ `${performanceData.stotalExe}%` }}
                <div
                  :style="{ height: '100%' }"
                  class="d-flex justify-space-between align-center"
                >
                  <span class="tableText" :class="performanceData.frst">
                    {{
                      performanceData.frst && performanceData.frst == "A"
                        ? t("EXCELLENT")
                        : performanceData.frst == "B"
                        ? t("SUITABLE")
                        : performanceData.frst == "C"
                        ? t("GOOD")
                        : performanceData.frst == "D"
                        ? t("SELF_INSPECTION_CHECK")
                        : t("OTHER")
                    }}
                  </span>
                  <span class="tableText" :class="performanceData.srst">
                    {{
                      performanceData.srst && performanceData.srst == "A"
                        ? t("EXCELLENT")
                        : performanceData.srst == "B"
                        ? t("SUITABLE")
                        : performanceData.srst == "C"
                        ? t("GOOD")
                        : performanceData.srst == "D"
                        ? t("SELF_INSPECTION_CHECK")
                        : t("OTHER")
                    }}
                  </span>
                </div>
              </td>
              <td>{{ t("PERFORMANCE_RESULT_TYPE1") }}</td>
            </tr>
            <tr>
              <td>{{ t("TOTAL_LEVEL") }}</td>
              <td class="d-flex justify-space-between align-center">
                {{ t("AVG_NORMAL") }}
                {{ `${performanceData.flevelNr}%` ?? "" }} /
                {{ `${performanceData.slevelNr}%` }}
                <div
                  :style="{ height: '100%' }"
                  class="d-flex justify-space-between align-center"
                >
                  <span class="tableText" :class="performanceData.frst">
                    {{
                      performanceData.frst && performanceData.frst == "A"
                        ? t("EXCELLENT")
                        : performanceData.frst == "B"
                        ? t("SUITABLE")
                        : performanceData.frst == "C"
                        ? t("GOOD")
                        : performanceData.frst == "D"
                        ? t("SELF_INSPECTION_CHECK")
                        : t("OTHER")
                    }}
                  </span>
                  <span class="tableText" :class="performanceData.srst">
                    {{
                      performanceData.srst && performanceData.srst == "A"
                        ? t("EXCELLENT")
                        : performanceData.srst == "B"
                        ? t("SUITABLE")
                        : performanceData.srst == "C"
                        ? t("GOOD")
                        : performanceData.srst == "D"
                        ? t("SELF_INSPECTION_CHECK")
                        : t("OTHER")
                    }}
                  </span>
                </div>
              </td>
              <td>{{ t("PERFORMANCE_RESULT_TYPE2") }}</td>
            </tr>
            <tr>
              <td>{{ t("DOOR_OPEN_CLOSE_STATUS") }}</td>
              <td class="d-flex justify-space-between align-center">
                {{ t("AVG_STABIL") }} {{ `${performanceData.fopenCv}%` }} /
                {{ `${performanceData.sopenCv}%` }}
                <div
                  :style="{ height: '100%' }"
                  class="d-flex justify-space-between align-center"
                >
                  <span class="tableText" :class="performanceData.frst">
                    {{
                      performanceData.frst && performanceData.frst == "A"
                        ? t("EXCELLENT")
                        : performanceData.frst == "B"
                        ? t("SUITABLE")
                        : performanceData.frst == "C"
                        ? t("GOOD")
                        : performanceData.frst == "D"
                        ? t("SELF_INSPECTION_CHECK")
                        : t("OTHER")
                    }}
                  </span>
                  <span class="tableText" :class="performanceData.srst">
                    {{
                      performanceData.srst && performanceData.srst == "A"
                        ? t("EXCELLENT")
                        : performanceData.srst == "B"
                        ? t("SUITABLE")
                        : performanceData.srst == "C"
                        ? t("GOOD")
                        : performanceData.srst == "D"
                        ? t("SELF_INSPECTION_CHECK")
                        : t("OTHER")
                    }}
                  </span>
                </div>
              </td>
              <td>{{ t("PERFORMANCE_RESULT_TYPE3") }}</td>
            </tr>
            <tr>
              <td>{{ t("DOOR_CLOSE_CLOSE_STATUS") }}</td>
              <td class="d-flex justify-space-between align-center">
                {{ t("AVG_STABIL") }} {{ `${performanceData.fcloseCv}%` }} /
                {{ `${performanceData.scloseCv}%` }}
                <div
                  :style="{ height: '100%' }"
                  class="d-flex justify-space-between align-center"
                >
                  <span class="tableText" :class="performanceData.frst">
                    {{
                      performanceData.frst && performanceData.frst == "A"
                        ? t("EXCELLENT")
                        : performanceData.frst == "B"
                        ? t("SUITABLE")
                        : performanceData.frst == "C"
                        ? t("GOOD")
                        : performanceData.frst == "D"
                        ? t("SELF_INSPECTION_CHECK")
                        : t("OTHER")
                    }}
                  </span>
                  <span class="tableText" :class="performanceData.srst">
                    {{
                      performanceData.srst && performanceData.srst == "A"
                        ? t("EXCELLENT")
                        : performanceData.srst == "B"
                        ? t("SUITABLE")
                        : performanceData.srst == "C"
                        ? t("GOOD")
                        : performanceData.srst == "D"
                        ? t("SELF_INSPECTION_CHECK")
                        : t("OTHER")
                    }}
                  </span>
                </div>
              </td>
              <td>{{ t("PERFORMANCE_RESULT_TYPE4") }}</td>
            </tr>
          </tbody>
        </v-table>
      </Skeleton>
    </v-col>
  </DefaultContainer>
  <teleport to="#extra-modal" :disabled="false">
    <ParallelSpiner :parallelSpiner="parallelSpiner"></ParallelSpiner>
  </teleport>
</template>

<style lang="scss">
.tableText {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 56px;
  font-size: 12px;
  font-weight: 700;
  height: 70%;
  border-radius: 28px;
  color: white;
  &.A {
    background-color: #0d99ff;
  }
  &.B {
    background-color: #00c34f;
  }
  &.C {
    background-color: rgb(220, 210, 26);
  }
  &.D {
    background-color: rgb(175, 46, 46);
  }
  &.Z {
    background-color: rgb(75, 75, 75);
  }
}
.myCustomChart2 canvas {
  height: calc(100vh - 726px) !important;
}
.reportTable {
  th {
    height: 33px !important;
  }
  td {
    height: 33px !important;
  }
}
</style>
