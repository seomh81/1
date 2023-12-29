<script setup>
import {
  ref,
  reactive,
  inject,
  computed,
  onMounted,
  watch,
  provide,
  onUnmounted,
  onBeforeUnmount,
} from "vue";
import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  SearchBarContainer,
  Jodit,
  Skeleton,
  ParallelSpiner,
} from "@/component/Template";
import { ColumnChart, BarChart } from "@opd/g2plot-vue";
import { useI18n } from "vue-i18n";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import dayjs from "dayjs";
import { v4 as uuidv4 } from "uuid";
import { dataStore } from "@/store/DataStore";
import { useToast } from "vue-toastification";
import "dayjs/locale/ko";
import { useRouter, useRoute } from "vue-router";
dayjs.locale("ko");
const router = useRouter();
const useDataStore = dataStore();
const toast = useToast();
const { t } = useI18n();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const elData = ref({});
const parallelSpiner = ref(false);
const evlStatus = ref("default");
const failRemarksData = ref([]);
const doorfake = require("@/assets/img/elevator/door-fake.png");
const doorleft = require("@/assets/img/elevator/door-left.png");
const rightDoor = require("@/assets/img/elevator/door-right.png");
const floor = require("@/assets/img/elevator/arrival-floor.png");
const activeBody = require("@/assets/img/elevator/active-body.png");
const darkLine = require("@/assets/img/elevator/dark-line.png");
const chartData = ref({
  elAvgRunTime: [],
  elAvgRunDistance: [],
  elAvgRunNum: [],
  elAvgDocc: [],
});
const defaultStatus = computed(() => useUiStore.defaultStatus);

const i18n = useI18n();
const searchRoleFg = ref(false);
const chartConfig = {
  height: 163,

  appendPadding: 0,
  yField: "value",
  xField: "date",
  dodgePadding: 5,
  isGroup: true,
  color: ["#9bb2c2", "#00C44F"],

  seriesField: "type",
  legend: {
    visible: false,
  },
  columnStyle: {
    radius: [5, 5, 0, 0],
  },
  tooltip: {
    fontSize: "12px",
  },
  // color: ["#00C44F", "grey"],

  label: false,
};
const statusTemp = {
  result: 0,
  doorStatus: "",
  moveDirection: "",
  currentFloor: "",
  runMode: "",
  projNo: "",
  hoNo: "",
  runStatus: "",
  monitTime: "YY.MM.DD A hh:mm:ss",
  connectTime: "YY.MM.DD A hh:mm:ss",
};
const connectionStatus = ref({});
Object.assign(connectionStatus.value, statusTemp);
const operatingStandards = ref([
  {
    title: computed(() => t("PARKING_STANDARD")),
    value: "0층",
    command: 1,
  },
  {
    title: computed(() => t("RETURN_STANDARD_TIME")),
    value: "00:00:00",
    command: 5,
  },
  {
    title: computed(() => t("FAN_TIME")),
    value: "00:00:00",
    command: 7,
  },
  {
    title: computed(() => t("LIGHT_TIME")),
    value: "00:00:00",
    command: 8,
  },
  {
    title: computed(() => t("STANDARD_DOOR_TIME")),
    value: "00:00:00",
    command: 6,
  },

  {
    title: computed(() => t("DEFECT_DOOR_TIME")),
    value: "00:00:00",
    disabled: "ST7",
    command: 9,
  },
  {
    title: computed(() => t("CAR_DOOR_TIME")),
    value: "00:00:00",
    disabled: "ST7",
    command: 10,
  },
  {
    title: computed(() => t("HALL_DOOR_TIME")),
    value: "00:00:00",
    disabled: "ST7",
    command: 11,
  },
]);
const emptyPartsLife = ref([
  {
    value: "MAIN ROPE",
    title: computed(() => t("ROPE")),
    color: "primary",
    sortingNumber: 0,
  },
  {
    value: "MAIN SHEAVE",
    title: computed(() => t("SHEVE")),
    color: "primary",
    sortingNumber: 0,
  },
  {
    value: "MAIN INV",
    title: computed(() => t("INV")),
    color: "primary",
    sortingNumber: 0,
  },
  {
    value: "DOOR INV",
    title: computed(() => t("DOOR_INV")),
    color: "primary",
    sortingNumber: 0,
  },
  {
    value: "SAFETY EDGE",
    title: computed(() => t("SAFETY_EDGE")),
    color: "primary",
    sortingNumber: 0,
  },
]);
const searchBar = reactive({
  inputs: [
    {
      name: "keyword",

      type: "field",
      label: computed(() => t("PRJ_NO")),
      target:
        useDataStore.targetMachineData.prjNo &&
        useDataStore.targetMachineData.hoNo
          ? useDataStore.targetMachineData.prjNo +
            useDataStore.targetMachineData.hoNo
          : "",
      event: async () => {
        if (
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 9
        ) {
          clearInterval(monitInterval);
          if(connectionStatus.value.result != 0)
          {
            await deconnetElvatorEvent();
          }
          Object.assign(connectionStatus.value, statusTemp);

          // try {
          parallelSpiner.value = true;
          const [returnElData, returnRunInfoData, partsData, failRemarkData] =
            await Promise.all([
              getElevator(),
              getRunInfo(),
              getParts(),
              getFailRemakers(),
            ]);
          if (returnElData && returnRunInfoData) {
            elData.value = returnElData;
            for (const item in returnRunInfoData) {
              processingData(returnRunInfoData, item);
            }
          }
          if (partsData) {
            emptyPartsLife.value.forEach((item) => {
              if (Object.keys(partsData).includes(item.value)) {
                item.color = "error";

                item.sortingNumber = 1;
              } else {
                item.color = "primary";
                item.sortingNumber = 0;
              }
            });
          }

          failRemarksData.value = failRemarkData;

          parallelSpiner.value = false;
          connectionElvatorEvent();
        } else {
          toast.warning(t("WARNING_SEARCH_PRJ_NO_LENGTH"));
        }
      },
    },
  ],
  buttons: [
    {
      name: "search",
      title: computed(() => t("BTN_SEARCH")),
      methodName: "BTN_SEARCH",

      event: async () => {
        if (
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 9
        ) {
          clearInterval(monitInterval);
          if(connectionStatus.value.result != 0)
          {
            await deconnetElvatorEvent();
          }
          Object.assign(connectionStatus.value, statusTemp);

          // try {
          parallelSpiner.value = true;
          const [returnElData, returnRunInfoData, partsData, failRemarkData] =
            await Promise.all([
              getElevator(),
              getRunInfo(),
              getParts(),
              getFailRemakers(),
            ]);

          if (returnElData && returnRunInfoData) {
            elData.value = returnElData;
            for (const item in returnRunInfoData) {
              processingData(returnRunInfoData, item);
            }
          }
          if (partsData) {
            emptyPartsLife.value.forEach((item) => {
              if (Object.keys(partsData).includes(item.value)) {
                item.color = "error";
                item.sortingNumber = 1;
              } else {
                item.color = "primary";
                item.sortingNumber = 0;
              }
            });
          }

          failRemarksData.value = failRemarkData;

          parallelSpiner.value = false;

          connectionElvatorEvent();
        } else {
          toast.warning(t("WARNING_SEARCH_PRJ_NO_LENGTH"));
        }
      },
    },
  ],
});

const getElevator = async () => {
  const { status, code, message, data, because } =
    await useConnectionStore.request({
      url: "/v2/cc/current/elevator",
      method: "post",
      queryparam: {
        prjNo: searchBar.inputs
          .find((item) => item.name == "keyword")
          .target.slice(0, -3),
        carNo: searchBar.inputs
          .find((item) => item.name == "keyword")
          .target.slice(-3),
      },
    });
  if (code == 200 && !!data) {
    // await getRunInfo();
    operatingStandards.value.forEach((item) => {
      if(item.command == "1")
      {
        item.value = "0층";
      }
      else
      {
        item.value = "00:00:00";
      }
    });
    return data;
  } else if (code >= 400) {
    toast.error(t("ERROR_SEARCH", [t("EL_INFO")]));
  }
};
const getRunInfo = async () => {
  const year = new Date().getFullYear();
  const month = new Date().getMonth() + 1;
  const date = new Date().getDate();
  const day = `${year}${month.toString().padStart(2, "0")}${date
    .toString()
    .padStart(2, "0")}`;

  const { status, code, message, data, because } =
    await useConnectionStore.request({
      url: "/v2/hrts/car/run-info",
      method: "post",
      queryparam: {
        prjNo: searchBar.inputs
          .find((item) => item.name == "keyword")
          .target.slice(0, -3),
        carNo: searchBar.inputs
          .find((item) => item.name == "keyword")
          .target.slice(-3),
        requestDate: `${year}${month.toString().padStart(2, "0")}${date
          .toString()
          .padStart(2, "0")}`,
      },
    });
  if (code == 200 && !!data) {
    let obj = {
      type: data.type,
      target: data.target,
    };
    return obj;
  } else if (code >= 400) {
    toast.error(t("ERROR_SEARCH", [t("MENU_MACHINE")]));
  }
};

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
const getParts = async (projnos) => {
  // try {
  const convertDataFunction = (data) => {
    const convertData = {};

    for (const item of data.projnos) {
      let materials = item.materials;
      for (const material of materials) {
        material.projno = item.projno;
        material.hoNo = item.projno.slice(-3);
        const mgroup = material.mgroup;
        if (!convertData[mgroup]) {
          convertData[mgroup] = [];
        }
        convertData[mgroup].push(material);
      }
    }

    return convertData;
  };
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/unit-service/material/predict/projnos",
      method: "post",
      queryparam: {
        projnos: [
          searchBar.inputs.find((item) => item.name == "keyword").target,
        ],
      },
    });
  if (code == 200) {
    if (data.projnos.length > 0) {
      const partsData = convertDataFunction(data);

      return partsData;
    }
  } else if (code >= 400) {
    toast.error(t("ERROR_SEARCH", [t("MIRI_AI_CHECK")]));
  }
};
const getFailRemakers = async (projnos) => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/fail-remarks",
      method: "post",
      queryparam: {
        // visible: true,
        prjhno: searchBar.inputs.find((item) => item.name == "keyword").target,
      },
    });
  if (code == 200) {
    // once = false;
    if (data.length > 0) {
      return data;
    } else {
      return [];
      // toast.warning("점검 특이사항 데이터가 없습니다");
    }
    // else {
    //   toast.warning("데이터가 없습니다");
    // }
    // if (data.projnos.length > 0) {
    // detailData.value = convertDataFunction(data);
    // } else {
    // toast.warning("데이터가 없습니다.");
    // }
    // treeData.value = createDataTree(data);
  } else if (code >= 400) {
    toast.error(t("ERROR_SEARCH", [t("PROPOSAL")]));
  }
};

let monitInterval;

const connectionElvatorEvent = async () => {
  const connectData = await useConnectionStore.request({
    url: "/v2/hrts/realtime/connect",
    method: "post",
    queryparam: {
      projNo: elData.value.prjNo,
      hoNo: elData.value.hoNo,
      command: 1,
    },
    timeout: 60000,
  });
  if (connectData && connectData.code == 200) {
    connectionStatus.value.projNo = connectData.data.projNo;
    connectionStatus.value.hoNo = connectData.data.hoNo;
    if (connectData.data.result == 1) {
      connectionStatus.value.connectTime = dayjs().format(
        "YY.MM.DD A HH:mm:ss"
      );
      connectionStatus.value.result = connectData.data.result;
      monitInterval = setInterval(async () => {
        const { status, code, message, data, because } =
          await useConnectionStore.request({
            url: "/v2/hrts/realtime/monit",
            method: "post",
            queryparam: {
              projNo: connectionStatus.value.projNo,
              hoNo: connectionStatus.value.hoNo,
            },
            spinerFg : false,
          });

        if (code == 200 && data.result == 1) {
          console.log(
            `런스테이터스',${data.runStatus}, 무브디렉션,${data.moveDirection}, 도어스테이터스:${data.doorStatus}`
          );
          if (connectionStatus.value.monitTime !== data.timeStamp) {
            connectionStatus.value.monitTime = dayjs(
              data.timeStamp,
              "YYMMDDHHmmss"
            ).format("YY.MM.DD A HH:mm:ss");
          }

          connectionStatus.value.currentFloor = parseInt(data.currentFloor) == 0 ? "" :  data.currentFloor;
          connectionStatus.value.runMode = data.runMode;
          connectionStatus.value.doorStatus = data.doorStatus;
          connectionStatus.value.moveDirection = data.moveDirection;

          if (evlStatus.value === "default") {
            // 서있음
            if (data.runStatus == 0) {
              evlStatus.value = data.doorStatus == 1 ? "open" : "default";
            } else {
              evlStatus.value = data.moveDirection == 1 ? "up" : "down";
            }
          } else if (evlStatus.value == "open") {
            if (data.runStatus == 0 && data.doorStatus == 0) {
              evlStatus.value = "close";
            } else if (data.runStatus != 0) {
              evlStatus.value = "close";
              setTimeout(() => {
                evlStatus.value =
                  data.moveDirection == 1
                    ? "up"
                    : data.moveDirection == 2
                    ? "down"
                    : "default";
              }, 1000);
            }
          } else if (evlStatus.value == "close") {
            if (data.runStatus == 0) {
              evlStatus.value = data.doorStatus == 1 ? "open" : "default";
            } else {
              evlStatus.value =
                data.moveDirection == 1
                  ? "up"
                  : data.moveDirection == 2
                  ? "down"
                  : "default";
            }
          } else if (evlStatus.value == "up" || evlStatus.value == "down") {
            if (data.runStatus == 0) {
              evlStatus.value = evlStatus.value == "up" ? "upStop" : "downStop";

              setTimeout(() => {
                evlStatus.value = data.doorStatus == 1 ? "open" : "default";
              }, 1900);
            }
          }
        } else {
          deconnetElvatorEvent();
          toast.error(t("ERROR_REAL_TIME"));
        }
      }, 2000);
    }
    // return data;
  } else {
    toast.error(t("ERROR_CONNECT"));
  }
};
const deconnetElvatorEvent = async () => {
  clearInterval(monitInterval);
  const connectData = await useConnectionStore.request({
    url: "/v2/hrts/realtime/connect",
    method: "post",
    queryparam: {
      projNo: elData.value.prjNo,
      hoNo: elData.value.hoNo,
      command: 0,
    },

    timeout: 60000,
  });
  evlStatus.value = "default";
  connectionStatus.value.result = 0;
  connectionStatus.value.currentFloor = "";
  connectionStatus.value.connectTime = "YY.MM.DD A hh:mm:ss";
  connectionStatus.value.monitTime = "YY.MM.DD A hh:mm:ss";
  connectionStatus.value.runMode = 0;
  connectionStatus.value.doorStatus = 0;
  connectionStatus.value.moveDirection = 0;
};
const connectionRulesSearchEvent = async (command, item) => {
  const paramData = {
    projNo: connectionStatus.value.projNo,
    hoNo: connectionStatus.value.hoNo,
    command: command,
  };

//  searchRoleFg.value = true;
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/hrts/realtime/rules",
      method: "post",
      queryparam: paramData,
    });
  searchRoleFg.value = false;
  if (code == 200) {
    if (parseInt(data.command) <= 4) {
      item.value = `${data.value}F`;
    } else {
      const hours = Math.floor(data.value / 3600);
      const minutes = Math.floor((data.value % 3600) / 60);
      const secondsRemainder = data.value % 60;
      item.value = `${hours.toString().padStart(2, "0")}:${minutes
        .toString()
        .padStart(2, "0")}:${secondsRemainder.toString().padStart(2, "0")}`;
    }
    // if(data.command)
  } else {
    toast.error(t("ERROR_DECONNECT"));
  }
};
onMounted(async () => {
  if (
    useDataStore.targetMachineData.prjNo &&
    useDataStore.targetMachineData.hoNo
  ) {
    searchBar.buttons.find((item) => item.name == "search").event();
  }
});
onBeforeUnmount(() => {
  if (useDataStore.targetMachineData) {
    useDataStore.setMachineData({});
  }
  clearInterval(monitInterval);
});
// getRunInfo();
const criteriaModal = ref(false);
provide("searchBar", searchBar);
watch ([criteriaModal],  async (newVal, oldVal) => {
  if(newVal[0] == true)
  {
    clearInterval(monitInterval);
  }
  else if(newVal[0] == false)
  {
    monitInterval = setInterval(async () => {
        const { status, code, message, data, because } =
          await useConnectionStore.request({
            url: "/v2/hrts/realtime/monit",
            method: "post",
            queryparam: {
              projNo: connectionStatus.value.projNo,
              hoNo: connectionStatus.value.hoNo,
            },
            spinerFg : false,
          });

        if (code == 200 && data.result == 1) {
          console.log(
            `런스테이터스',${data.runStatus}, 무브디렉션,${data.moveDirection}, 도어스테이터스:${data.doorStatus}`
          );
          if (connectionStatus.value.monitTime !== data.timeStamp) {
            connectionStatus.value.monitTime = dayjs(
              data.timeStamp,
              "YYMMDDHHmmss"
            ).format("YY.MM.DD A HH:mm:ss");
          }
          //connectionStatus.value.result = connectData.data.result;
          connectionStatus.value.currentFloor = parseInt(data.currentFloor) == 0 ? "" :  data.currentFloor;
          connectionStatus.value.runMode = data.runMode;
          connectionStatus.value.doorStatus = data.doorStatus;
          connectionStatus.value.moveDirection = data.moveDirection;

          if (evlStatus.value === "default") {
            // 서있음
            if (data.runStatus == 0) {
              evlStatus.value = data.doorStatus == 1 ? "open" : "default";
            } else {
              evlStatus.value = data.moveDirection == 1 ? "up" : "down";
            }
          } else if (evlStatus.value == "open") {
            if (data.runStatus == 0 && data.doorStatus == 0) {
              evlStatus.value = "close";
            } else if (data.runStatus != 0) {
              evlStatus.value = "close";
              setTimeout(() => {
                evlStatus.value =
                  data.moveDirection == 1
                    ? "up"
                    : data.moveDirection == 2
                    ? "down"
                    : "default";
              }, 1000);
            }
          } else if (evlStatus.value == "close") {
            if (data.runStatus == 0) {
              evlStatus.value = data.doorStatus == 1 ? "open" : "default";
            } else {
              evlStatus.value =
                data.moveDirection == 1
                  ? "up"
                  : data.moveDirection == 2
                  ? "down"
                  : "default";
            }
          } else if (evlStatus.value == "up" || evlStatus.value == "down") {
            if (data.runStatus == 0) {
              evlStatus.value = evlStatus.value == "up" ? "upStop" : "downStop";

              setTimeout(() => {
                evlStatus.value = data.doorStatus == 1 ? "open" : "default";
              }, 1900);
            }
          }
        } else {
          deconnetElvatorEvent();
          toast.error(t("ERROR_REAL_TIME"));
        }
      }, 2000);
  }
});
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="6" lg="2" class="pt-0">
      <Skeleton
        :loadingData="!!elData.prjNo == false"
        :height="'calc(100vh - 140px)'"
        :type="'heading,paragraph,paragraph,paragraph,paragraph'"
      >
        <v-list density="compact">
          <v-list-subheader class="mb-1">{{ t("EL_INFO") }}</v-list-subheader>

          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("PRJ_NO") }}</p>
            <v-list-item-subtitle>
              {{
                elData.prjNo && elData.hoNo ? elData.prjNo + elData.hoNo : ""
              }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("ELEVATOR_NO") }}</p>

            <v-list-item-subtitle>
              {{ elData.elevatorNo }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("BULD_NM") }}</p>
            <v-list-item-subtitle>
              {{ elData.buildNm ?? "" }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("INSTALL_SITE") }}</p>
            <v-list-item-subtitle>
              {{ elData.installationPlace ?? "" }}
            </v-list-item-subtitle>
          </v-list-item>
          <!-- <v-list-item>
            <p class="smallBoldText mb-1">{{ t("MODEL") }}</p>
            <v-list-item-subtitle>
              {{ elData.model ?? "" }}
            </v-list-item-subtitle>
          </v-list-item> -->

          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("INSTALL_DT") }}</p>

            <v-list-item-subtitle>
              {{ elData.installationDe ?? "" }}
            </v-list-item-subtitle>
          </v-list-item>

          <v-divider class="my-1"></v-divider>

          <v-list-subheader class="mb-1">{{
            t("INSPECTION")
          }}</v-list-subheader>

          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("INSPECTION_END_DT") }}</p>

            <v-list-item-subtitle>
              {{
                elData.mgrValdStrDt && elData.mgrValdEndDt
                  ? elData.applcBeDt + " - " + elData.applcEnDt
                  : ""
              }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("CYCLE_DT") }}</p>

            <v-list-item-subtitle>
              {{ elData.inspctCycle ?? "" }}{{ t("YEAR") }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-list-item>
            <p class="smallBoldText mb-1">D Day</p>

            <v-list-item-subtitle>
              {{ elData.inspctDays ?? "" }}{{ t("DAY") }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-divider class="my-1"></v-divider>

          <v-list-subheader class="mb-1">{{
            t("MAINTENANCE")
          }}</v-list-subheader>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("COMPANY_NAME") }}</p>

            <v-list-item-subtitle>
              {{ elData.inspCompanyNm ?? "" }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("HP") }}</p>

            <v-list-item-subtitle>
              {{ elData.inspCompanyTel ?? "" }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-divider class="my-1"></v-divider>

          <v-list-subheader class="mb-1">{{ t("INSURANCE") }}</v-list-subheader>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("COMPANY_NAME") }}</p>

            <v-list-item-subtitle>
              {{ elData.issueCompanyNm ?? "" }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("INSURANCE_PERIOD") }}</p>

            <v-list-item-subtitle>
              {{
                elData.issueContStDe && elData.issueContEnDe
                  ? elData.issueContStDe + " - " + elData.issueContEnDe
                  : ""
              }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-divider class="my-1"></v-divider>

          <v-list-subheader class="mb-1">{{
            t("SAFETY_MANAGER")
          }}</v-list-subheader>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("NAME") }}</p>

            <v-list-item-subtitle>
              {{ elData.shuttleMngrNm ?? "" }}
            </v-list-item-subtitle>
          </v-list-item>
          <v-list-item>
            <p class="smallBoldText mb-1">{{ t("SAFETY_MANAGER_PERIOD") }}</p>

            <v-list-item-subtitle>
              {{ elData.mgrValdEndDt ?? "" }}
            </v-list-item-subtitle>
          </v-list-item>
        </v-list>
      </Skeleton>
    </v-col>
    <v-col cols="6" lg="3" class="pt-0 align-self-center">
      <v-toolbar color="transparent" density="compact">
        <v-toolbar-title>{{ t("REAL_TIME") }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn
          class="mr-2"
          variant="outlined"
          :disabled="connectionStatus.result == 1 ? false : true"
          rounded="lg"
          @click="criteriaModal = true"
          >{{ t("REFERENCE_SEARCH") }}</v-btn
        >
        <v-btn
          variant="outlined"
          rounded="lg"
          :disabled="!!elData.prjNo == false"
          v-if="connectionStatus.result != 1"
          @click="connectionElvatorEvent"
          >{{ t("COONECT") }}</v-btn
        >
        <v-btn
          variant="outlined"
          rounded="lg"
          :disabled="!!elData.prjNo == false"
          v-if="connectionStatus.result === 1"
          @click="deconnetElvatorEvent"
          >{{ t("DISCOONECT") }}</v-btn
        >
      </v-toolbar>
      <v-container class="py-0" fluid>
        <v-row>
          <v-col cols="12" class="pr-0">
            <Skeleton
              :loadingData="!!elData.prjNo == false"
              :height="'calc(100vh - 188px)'"
              :type="'actions,heading,image,list-item,list-item'"
            >
              <div class="live-container basic-container">
                <div class="top">
                  <!-- 층수 -->
                  <p class="floor-text">
                    <!-- currentFloor -->

                    {{ connectionStatus.currentFloor ?? "" }}
                  </p>
                  <div class="animation-wrap">
                    <!-- 애니메이션 시작 -->
                    <div class="absolute-wrap">
                      <!-- 0. 엘베 닫혀있음 -->
                      <div class="wrap">
                        <!-- 벽 -->
                        <div class="fake-door">
                          <img :src="doorfake" alt="" />
                        </div>
                        <!-- 문 -->
                        <div class="door-wrap">
                          <div class="door">
                            <div
                              id="left-door"
                              class="left-door"
                              :class="{
                                'left-open': evlStatus == 'open',
                                'left-close': evlStatus == 'close',
                              }"
                            >
                              <img :src="doorleft" alt="" />
                            </div>
                            <div
                              id="right-door"
                              class="right-door"
                              :class="{
                                'right-open': evlStatus == 'open',
                                'right-close': evlStatus == 'close',
                              }"
                            >
                              <img :src="rightDoor" alt="" />
                            </div>
                          </div>
                        </div>
                        <div class="body">
                          <img :src="floor" alt="" />
                        </div>
                      </div>

                      <!-- 1. 화면전환되면서 움직이는 장면 -->
                      <div
                        class="wrap"
                        v-if="
                          evlStatus == 'up' ||
                          evlStatus == 'down' ||
                          evlStatus == 'downStop' ||
                          evlStatus == 'upStop'
                        "
                      >
                        <!-- 올라가는 어두운 배경 1초-->
                        <!-- 올라가는 화면 -->
                        <div
                          class="dark-background-up"
                          v-if="evlStatus == 'up' || evlStatus == 'upStop'"
                        ></div>
                        <!-- 내려가는 화면 -->
                        <div
                          class="dark-background-down"
                          v-if="evlStatus == 'down' || evlStatus == 'downStop'"
                        ></div>

                        <!-- 올라갈때 : (어두워지면서 나타나는 문) 1초-->
                        <div class="elevator-active">
                          <img :src="activeBody" alt="" />
                        </div>
                        <div
                          class="arrivar-floor"
                          v-if="evlStatus == 'upStop'"
                        ></div>
                        <div
                          class="arrivar-floor-down"
                          v-if="evlStatus == 'downStop'"
                        ></div>

                        <!-- 올라갈 때 : 줄 1초-->
                        <div
                          class="dark-line"
                          v-if="evlStatus == 'up' || evlStatus == 'down'"
                        >
                          <img :src="darkLine" alt="" />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 하단 상황 wrap -->

                <div
                  class="realTimeFooter"
                  :style="{ background: 'rgb(79, 85, 110)' }"
                >
                  <v-sheet
                    class="d-flex justify-space-around align-center"
                    color="rgb(52, 138, 252)"
                    height="60px"
                  >
                    <div class="d-flex align-center text-white largeBoldText">
                      <span>{{
                        connectionStatus.doorStatus == 0
                          ? t("DOOR_CLOSE")
                          : t("DOOR_OPEN")
                      }}</span>
                      <v-icon class="ml-2">{{
                        connectionStatus.doorStatus == 0
                          ? "mdi-door-closed"
                          : "mdi-door-open"
                      }}</v-icon>
                    </div>
                    <div class="d-flex align-center text-white largeBoldText">
                      <span>{{
                        connectionStatus.moveDirection == 0
                          ? t("STANDBY")
                          : connectionStatus.moveDirection == 1
                          ? t("UP")
                          : t("DOWN")
                      }}</span>
                      <v-icon class="ml-2">
                        {{
                          connectionStatus.moveDirection == 0
                            ? "mdi-minus-circle-outline"
                            : connectionStatus.moveDirection == 1
                            ? "mdi-arrow-up-thin-circle-outline"
                            : "mdi-arrow-down-thin-circle-outline"
                        }}
                      </v-icon>
                    </div>
                    <div class="d-flex align-center text-white largeBoldText">
                      <span>{{
                        connectionStatus.runMode == 0
                          ? t("MANUAL_OPERATION")
                          : t("AUTO_OPERATION")
                      }}</span>
                      <v-icon class="ml-2">{{
                        connectionStatus.runMode == 0
                          ? "mdi-motion-pause-outline"
                          : "mdi-motion-play-outline"
                      }}</v-icon>
                    </div>
                  </v-sheet>
                  <v-container class="px-4">
                    <v-row>
                      <v-col
                        cols="12"
                        :style="{ color: '#e2e2e2' }"
                        class="text-end px-0"
                      >
                        <v-card-subtitle>{{
                          connectionStatus.result == 1
                            ? t("NORMAL_OPERATION")
                            : t("DECONNECT_OPERATION")
                        }}</v-card-subtitle>
                        <v-card-subtitle>{{
                          connectionStatus.monitTime
                        }}</v-card-subtitle>
                      </v-col>
                      <v-col
                        cols="12"
                        :style="{ color: '#e2e2e2' }"
                        class="text-end px-0"
                      >
                        <v-card-subtitle>{{
                          connectionStatus.result == 1
                            ? `MIRI  ${t("CONNECT")}`
                            : "&nbsp;"
                        }}</v-card-subtitle>
                        <v-card-subtitle>{{
                          connectionStatus.result == 1
                            ? connectionStatus.connectTime
                            : "&nbsp;"
                        }}</v-card-subtitle>
                      </v-col>
                    </v-row>
                  </v-container>
                </div>
              </div>
            </Skeleton>
          </v-col>
        </v-row>
      </v-container>
    </v-col>
    <v-col cols="12" lg="7" class="pt-0">
      <v-toolbar color="transparent" density="compact">
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
        <v-spacer></v-spacer>
        <v-btn
          variant="outlined"
          rounded="lg"
          color="primary"
          class="text-end"
          :disabled="!!elData.prjNo == false"
          @click="
            [
              router.push('/report/remote'),
              useDataStore.setElvatorNumber(elData.prjNo + elData.hoNo),
            ]
          "
          >{{ t("VIEW_DETAIL") }}</v-btn
        >
      </v-toolbar>
      <v-container fluid class="px-0 pt-0">
        <v-row align="center">
          <v-col cols="3" v-for="(item, key, idx) in chartData" :key="idx">
            <Skeleton
              :loadingData="!!elData.prjNo == false"
              :padding="true"
              :type="'image'"
              :height="'226px'"
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
              <column-chart :data="[...item]" v-bind="chartConfig" />
            </Skeleton>
          </v-col>
        </v-row>
      </v-container>
      <v-toolbar color="transparent" density="compact">
        <v-toolbar-title>{{ t("MIRI_AI_CHECK") }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn
          variant="outlined"
          rounded="lg"
          color="primary"
          :disabled="!!elData.prjNo == false"
          @click="
            [
              useDataStore.setElvatorNumber(elData.prjNo + elData.hoNo),
              router.push('/parts/lifeInquiry'),
            ]
          "
          >{{ t("VIEW_DETAIL") }}</v-btn
        >
      </v-toolbar>
      <v-container fluid class="px-0 py-0">
        <Skeleton
          :loadingData="!!elData.prjNo == false"
          :height="'calc(100vh - 478px)'"
          :type="'image,image'"
          :padding="true"
        >
          <v-row>
            <v-col class="pb-5" v-for="item in emptyPartsLife" :key="item">
              <v-card
                :color="item.color"
                rounded="lg"
                height="65px"
                class="customCard d-flex justify-center align-center"
                ><span class="text-white">
                  {{ item.title }}
                </span>
              </v-card>
            </v-col>
            <v-divider></v-divider>
          </v-row>
          <v-row>
            <v-col
              cols="auto"
              class="py-0"
              v-if="failRemarksData?.length !== 0"
            >
              <v-list lines="two">
                <v-list-item v-for="item in failRemarksData" :key="item">
                  <v-list-item-title>
                    {{ item.MA_MONTH.replace(/^(\d{4})(\d{2})$/, "$1-$2") }}
                  </v-list-item-title>
                  <v-list-item-subtitle>
                    {{ item.REMARK }}
                  </v-list-item-subtitle>
                </v-list-item>
              </v-list>
            </v-col>
            <v-col cols="12" class="py-0" v-if="failRemarksData?.length == 0">
              <v-sheet
                v-if="failRemarksData?.length == 0"
                height="270px"
                class="d-flex justify-center align-center"
              >
                <p>
                  {{ t("NO_PROPOSAL") }}
                </p>
              </v-sheet>
            </v-col>
          </v-row>
        </Skeleton>
      </v-container>
    </v-col>

    <v-dialog
      no-click-animation
      persistent
      v-model="criteriaModal"
      width="550px"
    >
      <v-card class="customCard">
        <v-toolbar color="transparent" density="compact">
          <v-toolbar-title>{{ t("OPERATION_REFERENCE") }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn @click="criteriaModal = false"
            ><v-icon>mdi-close</v-icon></v-btn
          >
        </v-toolbar>
        <v-container class="pb-5">
          <v-row>
            <v-col
              cols="12"
              class="d-flex justify-center text-center align-center"
              v-for="item in operatingStandards"
              :key="item"
            >
              <p :style="{ flexBasis: '33.333%' }">{{ item.title }}</p>
              <v-btn
                size="small"
                :disabled="elData.model === item.disabled ? true : searchRoleFg"
                @click="connectionRulesSearchEvent(item.command, item)"
                >{{ t("BTN_SEARCH") }}</v-btn
              >
              <p :style="{ flexBasis: '33.333%' }">{{ item.value }}</p>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-dialog>
  </DefaultContainer>

  <teleport to="#extra-modal" :disabled="false">
    <ParallelSpiner :parallelSpiner="parallelSpiner"></ParallelSpiner>
  </teleport>
</template>
<style lang="scss"></style>
