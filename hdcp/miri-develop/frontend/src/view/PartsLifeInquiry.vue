<script setup>
import {
  Tabulator,
  DefaultContainer,
  SearchBarContainer,
  ParallelSpiner,
  Skeleton,
} from "@/component/Template";
import { userStore } from "@/store/UserStore";
import { dataStore } from "@/store/DataStore";
import ReportHTML from "@/component/ReportHTML.vue";
import { ref, onMounted, onUnmounted, computed, watch } from "vue";
import { TheTreeComp } from "@daiahub/thetreecomp";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";
import { connectionStore } from "@/store/ConnectionStore";
import { ColumnChart } from "@opd/g2plot-vue";
import { useRouter } from "vue-router";
import dayjs from "dayjs";
const parallelSpiner = ref(false);
const router = useRouter();
const { t } = useI18n();
const useDataStore = dataStore();
const toast = useToast();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const useUserStore = userStore();
const useConnectionStore = connectionStore();

const TreeCompTgt = ref();
const expandAll = ref(true);
const canEdit = ref(true);
const treeHeight = `calc(100vh - ${64 + 52 + 30}px)`;
const imgHeight = `calc(100vh - ${64 + 52 + 42}px)`;
const elevator_parts = require("@/assets/img/elevator.png");
const treeData = ref([]);
const failRemarksData = ref([]);
const notOnMount = ref(true);
const columns = computed(() => {
  return [
    {
      title: t("BULD_NM"),
      field: "buldNm",
      sorter: "string",
      tooltip: true,
    },
    {
      title: t("PRJ_NO"),
      field: "projno",
      sorter: "string",
    },

    {
      title: t("OPERATION_RESULT"),
      field: "convertMove",
      sorter: "string",
    },
    {
      title: t("STATUS"),
      field: "comment",
      sorter: "string",
      widthGrow: 3,
      tooltip: true,
    },
  ];
});
const defaultConfig = ref({
  appendPadding: 10,

  yField: "value",
  xField: "type",
  isStack: true,
  radius: 0.75,
  seriesField: "name",
  label: false,
  tooltip: false,

  color: ["#EF5350", "#BDBDBD", "#0D99FF"],
  legend: false,
  minColumnWidth: 25,
  maxColumnWidth: 25,

  xAxis: [
    {
      label: {
        position: "top",
        offset: 20,
      },
    },
  ],
});
const reportData = ref({
  type: "",
  item: {},
  count1: "",
  count2: "",
});

const detailData = ref({});
const treeTarget = computed(() => TreeCompTgt?.value?.GetSelectedTarget());
let changeView = ref(false);
const getList = async (parallel) => {
  const createDataTree = (dbData) => {
    let rtnVal = {};
    let treeData = [];
    dbData.forEach((data) => {
      rtnVal[data.id] = {
        name: data.installationPlace
          ? `${data.installationPlace} - ${data.id}`
          : data.name
          ? data.name
          : data.buldNm,
        id: data.id,
        children: [],
        type: data.type,
        buldNm: data.buldNm,
      };
    });
    dbData.forEach((data) => {
      if (data.parentId == 0) {
        treeData.push(rtnVal[data.id]);
      } else {
        rtnVal[data.parentId]?.children.push(rtnVal[data.id]);
      }
    });
    return JSON.parse(JSON.stringify(treeData));
  };
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/cc/units/tree",
      method: "post",
      queryparam: {
        userPortfolioMappingId:
          useUserStore.portfolio.selected.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    if (parallel == true) {
      return createDataTree(data);
    } else {
      treeData.value = createDataTree(data);
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("MENU_PARTS_LIFEINQUIRY")]));
  }
};
const getDetails = async (projnos) => {
  const convertDataFunction = (data) => {
    const convertData = {};
    for (const item of data.projnos) {
      let materials = item.materials;
      for (const material of materials) {
        material.convertMove = `
          ${material.accumulated_move
            .toString()
            .replace(/\B(?=(\d{3})+(?!\d))/g, ",")}${
          material.standard_durability.criteria === "Count"
            ? t("USE_NUMBER")
            : t("TIME")
        }
          `;
        material.projno = item.projno;
        material.hoNo = item.projno.slice(-3);
        material.buldNm = item.buldNm;
        const mgroup = material.mgroup;
        if (!convertData[mgroup]) {
          convertData[mgroup] = [];
        }
        convertData[mgroup].push(material);
      }
    }

    if (data.projnos.length == 1 && treeTarget.value.value.type == "el") {
      changeView.value = true;
    } else if(data.projnos.length == 1 && (computedPortfolio.value?.userPortfolioMappingId && useDataStore.targetElvatorNumber)) {
      changeView.value = true;
    } else {
      changeView.value = false;
    }
    return convertData;
  };
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/unit-service/material/predict/projnos",
      method: "post",
      queryparam: {
        projnos,
      },
    });
  if (code == 200) {
    // once = false;
    if (data.projnos.length > 0) {
      let convertDeTailData = convertDataFunction(data);
      return convertDeTailData;
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_PARTS")]));

      return {};
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("MIRI_AI_CHECK")]));
  }
};
const getFailRemakers = async (projnos) => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/fail-remarks",
      method: "post",
      queryparam: {
        prjhno: projnos,
      },
    });
  if (code == 200) {
    // once = false;
    if (data.length > 0) {
      return data;
    } else {
      return [];
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("PROPOSAL")]));
  }
};

const findChildren = () => {
  let childArr = [];
  let leafItem = [];

  const findLeaf = (childItem) => {
    if (childItem.children.length == 0 && childItem.type == "el") {
      childArr.push(childItem);
    }
    if (childItem.children.length > 0) {
      childItem.children.forEach((leafItem) => {
        findLeaf(leafItem);
      });
    }
  };
  if (treeTarget.value.value.children.length > 0) {
    treeTarget.value.value.children.forEach((item) => {
      findLeaf(item);
    });
  } else if (
    treeTarget.value.value.children.length == 0 &&
    treeTarget.value.value.type == "el"
  ) {
    childArr.push(treeTarget.value.value);
  }
  if (childArr.length > 0) {
    childArr.forEach((leaf) => {
      leafItem.push(leaf.id);
    });
  }
  return leafItem;
};
watch(
  treeTarget,
  async () => {
    if (treeTarget.value.value) {
      try {
        if(notOnMount.value)
        {
          detailData.value = {};
          failRemarksData.value = [];
          parallelSpiner.value = true;
          const [convertDetailData, remarksData] = await Promise.all([
            getDetails(findChildren()),
            getFailRemakers(),
          ]);
          detailData.value = convertDetailData;
          failRemarksData.value = remarksData;
      
          parallelSpiner.value = false;
        }

        notOnMount.value = true;
      } catch {
        parallelSpiner.value = false;
        notOnMount.value = true;
      }
    }
  },
  { deep: true }
);
const tableHeight = `calc((100vh) / 2.62)`;

const dialog = ref(false);
const emptyPdfData = ref();
const openPdf = (i) => {
  if (i == 0) {
    emptyPdfData.value = "green";
  }
  if (i == 1) {
    emptyPdfData.value = "yellow";
  }
  if (i == 2) {
    emptyPdfData.value = "red";
  }
  if (i == 3) {
    emptyPdfData.value = "blue";
  }
  dialog.value = true;
};
onMounted(async (item) => {
  if (
    computedPortfolio.value?.userPortfolioMappingId &&
    useDataStore.targetElvatorNumber
  ) {
    notOnMount.value = false;
    try {
      detailData.value = {};
      failRemarksData.value = [];
      parallelSpiner.value = true;
      const listData = await getList(true);
      treeData.value = listData;
      const convertDetailData = await getDetails([useDataStore.targetElvatorNumber]);
      detailData.value = convertDetailData;
      const remarksData = await getFailRemakers();
      failRemarksData.value = remarksData;
      /*
      const [listData, convertDetailData, remarksData] = await Promise.all([
        getList(true),
        getDetails([useDataStore.targetElvatorNumber]),
        getFailRemakers(),
      ]);
      */
      parallelSpiner.value = false;
      useDataStore.setElvatorNumber(undefined);
    } catch {
      parallelSpiner.value = false;
    }
  } else if (
    computedPortfolio.value?.userPortfolioMappingId &&
    !!useDataStore.targetElvatorNumber == false
  ) {
    getList();
  }
});
watch(computedPortfolio, (newVal, oldVal) => {
  treeData.value = [];
  detailData.value = {};
  failRemarksData.value = [];
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    getList();
  }
});
onUnmounted(() => {
  if (useDataStore.targetElvatorNumber) {
    useDataStore.setElvatorNumber(undefined);
  }
});

const getYearDiff = (d1, d2, cycle, type) => {
  const date1 = dayjs(d1);
  const date2 = dayjs(d2);
  const date3 = date1.add(cycle, "year");

  if (date3.isBefore(date2)) {
    if (type == "over") {
      return date2.diff(date3, "year");
    } else {
      return date3.diff(date1, "year");
    }
  }
};
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" lg="3" class="pt-1">
      <v-card class="customCard">
        <v-toolbar color="transparent" density="comfortable">
          <v-spacer></v-spacer>
          <v-btn
            size="small"
            @click="getList"
            variant="outlined"
            rounded="lg"
            >{{ t("RESEARCH") }}</v-btn
          >
        </v-toolbar>
        <v-sheet class="px-4">
          <TheTreeComp
            :readOnly="true"
            ref="TreeCompTgt"
            elementid="MainTree"
            :items="treeData"
            :injectItem="
              useDataStore.targetElvatorNumber
                ? { id: useDataStore.targetElvatorNumber }
                : {}
            "
            :canEdit="canEdit"
            :expandAll="expandAll"
            :height="treeHeight"
          >
          </TheTreeComp>
        </v-sheet>
      </v-card>
    </v-col>
    <v-col
      class="pt-1"
      :cols="changeView && failRemarksData?.length > 0 ? 9 : 12"
      :lg="changeView && failRemarksData?.length > 0 ? 6 : 9"
    >
      <Skeleton
        :loadingData="detailData && Object.keys(detailData)?.length == 0"
        :height="'calc(100vh - 90px)'"
        type="image,image,image,image"
      >
        <v-toolbar density="comfortable" color="transparent">
          <p class="px-8 smallBoldText" v-if="!changeView">
            {{ t("PART_DIA") }}
          </p>
          <p v-if="changeView" class="px-8 smallBoldText">
            {{ t("HO_DIA") }}
          </p>
        </v-toolbar>
        <div v-if="changeView == false">
          <v-sheet height="calc(100vh - 146px)" class="overflow-y-auto">
            <div
              class="pb-4 pt-0 px-4"
              v-for="(item, key) in detailData"
              :key="item"
            >
              <v-toolbar density="compact" color="transparent">
                <v-toolbar-title>
                  <span class="smallBoldText">
                    {{
                      key == "MAIN ROPE"
                        ? t("ROPE")
                        : key === "MAIN INV"
                        ? t("INV")
                        : key === "MAIN SHEAVE"
                        ? t("SHEVE")
                        : key === "SAFETY EDGE"
                        ? t("SAFETY_EDGE")
                        : key === "DOOR INV"
                        ? t("DOOR_INV")
                        : ""
                    }}<span class="xsmallOpacityText">
                      &nbsp;&nbsp; {{ t("REFERENCE") }} :
                      {{
                        item[0].standard_durability.limit
                          .toString()
                          .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                      }}&nbsp;{{
                        item[0].standard_durability.criteria === "Count"
                          ? t("USE_NUMBER")
                          : t("TIME")
                      }}
                      /
                      {{
                        item[0].standard_durability.replacement_cycle
                      }}&nbsp;{{ t("YEAR") }}
                    </span>
                  </span>
                </v-toolbar-title>
              </v-toolbar>
              <Tabulator
                :columns="columns"
                :tableData="item"
                :height="tableHeight"
                :pagination="false"
                @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
              >
              </Tabulator>
            </div>
          </v-sheet>
        </div>

        <v-container class="py-0" fluid v-if="changeView == true">
          <v-row>
            <v-col cols="3" lg="4" class="d-flex align-center">
              <v-img :height="imgHeight" :src="elevator_parts" />
            </v-col>
            <v-col cols="9" lg="8">
              <v-sheet
                height="calc(100vh - 224px)"
                :style="{ overflow: 'auto' }"
              >
                <v-divider></v-divider>
                <v-container>
                  <v-row v-for="(item, key, i) in detailData" :key="key">
                    <v-col cols="4">
                      <div>
                        <div class="d-flex align-center flex-wrap">
                          <span class="largeBoldText">
                            {{
                              key == "MAIN ROPE"
                                ? t("ROPE")
                                : key === "MAIN INV"
                                ? t("INV")
                                : key === "MAIN SHEAVE"
                                ? t("SHEVE")
                                : key === "SAFETY EDGE"
                                ? t("SAFETY_EDGE")
                                : key === "DOOR INV"
                                ? t("DOOR_INV")
                                : ""
                            }}
                          </span>
                          <v-btn
                            class="ml-3"
                            rounded="lg"
                            size="small"
                            variant="outlined"
                            @click="
                              [
                                (reportData.type = key),
                                (reportData.comment = item[0].comment),
                                (reportData.item = item[0]),
                                (reportData.count1 = `${
                                  item[0].standard_durability.criteria ===
                                  'Count'
                                    ? '운행 현황'
                                    : item[0].standard_durability.criteria ===
                                      'ElapsedTime'
                                    ? '설치 시간'
                                    : item[0].standard_durability.criteria ===
                                      'RunTime'
                                    ? '운행 시간'
                                    : ''
                                }
                        
                        ${item[0].convertMove}
                          `),
                                (reportData.count2 = `사용 년수 ${Math.floor(
                                  item[0].days_elapsed_installation / 365
                                )}년`),
                                openPdf(i),
                              ]
                            "
                          >
                            보고서
                          </v-btn>
                        </div>
                        <p class="mt-1">
                          {{ t("INSTALL_REPLACEMENT_DAY") }} :
                          {{ item[0].replacement_date }}
                        </p>
                      </div>
                    </v-col>
                    <v-col cols="4">
                      <span class="largeBoldText mb-2">{{
                        `${
                          item[0].standard_durability.criteria === "Count"
                            ? t("OPERATION_RESULT")
                            : item[0].standard_durability.criteria ===
                              "ElapsedTime"
                            ? t("INSTALL_TIME")
                            : item[0].standard_durability.criteria === "RunTime"
                            ? t("OPERATION_TIME")
                            : ""
                        }                          ${item[0].convertMove}

                        `
                      }}</span>
                      <column-chart
                        class="myChart"
                        v-bind="{
                          ...defaultConfig,

                          yAxis: {
                            label: {
                              formatter: (value) =>
                                `${
                                  value == 0
                                    ? '0'
                                    : value.toString().slice(0, -4)
                                }만`,
                            },
                          },
                          annotations: [
                            {
                              type: 'line',
                              /** 起始位置 */
                              start: [
                                t('MY_HO'),
                                item[0].standard_durability?.limit,
                              ],
                              end: [
                                t('AVG_HO'),
                                item[0].standard_durability?.limit,
                              ],
                              text: {
                                // 旅游萧条 标注
                                content: t('STANDARD'),
                                rotate: 0,
                                autoRotate: false,
                                position: ['right'],

                                style: {
                                  fontWeight: '700',
                                  textAlign: 'center',
                                  fill: '#0D99FF',
                                },
                              },
                              style: {
                                stroke: '#0D99FF',
                                lineWidth: 0.5,
                              },
                            },
                          ],
                        }"
                        :data="[
                          {
                            name: t('OVER_DISTANCE'),
                            value:
                              item[0].standard_durability.limit -
                                item[0].accumulated_move <
                              0
                                ? Math.abs(
                                    item[0].standard_durability.limit -
                                      item[0].accumulated_move
                                  )
                                : 0,
                            type: t('MY_HO'),
                          },
                          {
                            name: t('RIMIT'),
                            value: item[0].standard_durability?.average_limit,
                            type: t('AVG_HO'),
                          },

                          {
                            name: t('NOW_DISTANCE'),
                            value:
                              item[0].accumulated_move >
                              item[0].standard_durability.limit
                                ? item[0].standard_durability.limit
                                : item[0].accumulated_move,
                            type: t('MY_HO'),
                          },
                        ]"
                      />
                    </v-col>
                    <v-col cols="4">
                      <div class="mb-2">
                        <span class="largeBoldText"
                          >{{ t("PEROID_USE") }}
                          {{
                            Math.floor(item[0].days_elapsed_installation / 365)
                          }}{{ t("YEAR") }}
                        </span>
                      </div>
                      <column-chart
                        class="myChart"
                        v-bind="{
                          ...defaultConfig,
                          yAxis: {
                            label: {
                              formatter: (value) => `${value}y`,
                            },
                          },
                          annotations: [
                            {
                              type: 'line',
                              /** 起始位置 */
                              start: [
                                t('MY_HO'),
                                item[0].standard_durability?.replacement_cycle,
                              ],
                              end: [
                                t('AVG_HO'),
                                item[0].standard_durability?.replacement_cycle,
                                ,
                              ],
                              text: {
                                // 旅游萧条 标注
                                content: t('STANDARD'),
                                rotate: 0,
                                autoRotate: false,
                                position: ['right'],

                                style: {
                                  textAlign: 'center',
                                  fill: '#0D99FF',
                                },
                              },
                              style: {
                                stroke: '#0D99FF',
                                lineWidth: 0.5,
                              },
                            },
                          ],
                        }"
                        :data="[
                          {
                            name: t('OVER_YEARS'),
                            value: getYearDiff(
                              item[0].replacement_date,
                              new Date(),
                              item[0].standard_durability?.replacement_cycle,
                              'over'
                            ),

                            type: t('MY_HO'),
                          },
                          {
                            name: t('REPLACEMENT_CYCLE'),
                            value:
                              item[0].standard_durability?.replacement_cycle,

                            type: t('AVG_HO'),
                          },

                          {
                            name: t('USE_YEARS'),
                            value: getYearDiff(
                              item[0].replacement_date,
                              new Date(),
                              item[0].standard_durability?.replacement_cycle
                            ),
                            type: t('MY_HO'),
                          },
                        ]"
                      />
                    </v-col>
                    <v-divider></v-divider>
                  </v-row>
                </v-container>
              </v-sheet>
            </v-col>
          </v-row>
        </v-container>
      </Skeleton>
    </v-col>
    <v-col
      v-if="changeView == true && failRemarksData?.length > 0"
      cols="3"
      class="pt-0"
    >
      <v-card class="customCard">
        <v-toolbar color="transparent" density="comfortable">
          <v-toolbar-title class="largeBoldText">{{
            t("INSPECTION_SPECIAL")
          }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn
            size="small"
            variant="outlined"
            rounded="lg"
            @click="router.push('/report/base')"
            >{{ t("SELF_INSPECTION_REPORT") }}</v-btn
          >
        </v-toolbar>
        <v-container>
          <v-row>
            <v-col
              v-for="(item, i) in failRemarksData"
              :key="item"
              cols="12"
              :style="{ fontSize: '14px', fontWeight: '500' }"
              >{{ i + 1 }}. item.REMAKER</v-col
            >
          </v-row>
        </v-container>
      </v-card>
    </v-col>
  </DefaultContainer>
  <v-dialog v-model="dialog" width="40vw">
    <ReportHTML
      :data="reportData"
      class="mx-auto"
      :text="emptyPdfData"
    ></ReportHTML>
  </v-dialog>

  <teleport to="#extra-modal" :disabled="false">
    <ParallelSpiner :parallelSpiner="parallelSpiner"></ParallelSpiner>
  </teleport>
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
.myChart canvas {
  height: calc((100vh - 200px) / 5) !important;
}
</style>
