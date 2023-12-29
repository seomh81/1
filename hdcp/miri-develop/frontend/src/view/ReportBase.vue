<script setup>
import { ref, reactive, computed, onMounted, watch, provide } from "vue";
import {
  Tabulator,
  DefaultContainer,
  SearchBarContainer,
  ParallelSpiner,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
import dayjs from "dayjs";
const startDate = dayjs().subtract(3, "month").format("YYYY-MM");
const toast = useToast();
const parallelSpiner = ref(false);
const useUserStore = userStore();
const selectedFirstRow = ref(false);
const { t } = useI18n();
const height = 64 + 64 + 12;
const tableHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();
const year = new Date().getFullYear();
const month = new Date().getMonth() + 1;
const date = new Date().getDate();
const detailData = ref([]);

const rowTarget = ref([]);
const elInfoData = ref({});
const tableData = ref([]);

const columns = computed(() => {
  return [
    {
      title: t("MACHINE_TYPE"),
      field: "elvtrDivNm",
      widthShrink: 1,
      sorter: "string",
    },
    {
      title: t("BULD_NM"),
      field: "buldNm",
      sorter: "string",
      tooltip: true,
      widthGrow: 1,
    },

    {
      title: t("PRJ_NO"),
      field: "projNo",
      sorter: "string",
    },
    {
      title: t("REPORT_TYPE"),
      field: "reportType",
      sorter: "string",
      widthShrink: 1,
    },
    {
      title: t("SELF_INSPECTION_DT"),
      field: "replaceStartDate",
      sorter: "string",
      tooltip: true,
    },
    {
      title: t("INSPECTION_END_DT"),
      field: "replaceEndDate",
      sorter: "string",
    },
    {
      title: t("RESULT_YN"),
      field: "chkResult",
      widthGrow: 1,
    },
  ];
});

const inspColumns = computed(() => {
  return [
    {
      title: t("CATEGORY"),
      field: "standardArticle",
      sorter: "string",
    },
    {
      title: t("INSPECTION_CONTENTL"),
      field: "standardTitle1",
      sorter: "string",
      tooltip: true,
    },
    {
      title: t("INSPECTION_RESULT"),
      field: "failDesc",
      sorter: "string",
      tooltip: true,
    },
    {
      title: t("INSPECTION_COMMENT"),
      field: "failDescInspector",
      sorter: "string",
    },
  ];
});
const selfInspColumns = computed(() => {
  return [
    {
      title: t("CATEGORY"),
      field: "titNo",
      sorter: "string",
      width: "80",
    },
    {
      title: t("SELF_INSPECTION_CATEGORY"),
      field: "selChkItemNm",
      sorter: "string",
      widthGrow: 1,
      tooltip: true,
    },
    {
      title: t("SELF_INSPECTION_RESULT"),
      field: "selChkItemDtlNm",
      sorter: "string",
      tooltip: true,
      widthGrow: 2,
    },
    {
      title: t("SELF_INSPECTION_NOW_RESULT"),
      sorter: "string",
      field: "selchkResultNm",
      formatter: function (cell, formatterParams, onRendered) {
        let rtnVal = cell.getValue();
        switch (cell.getValue()) {
          case "D":
            rtnVal = "제외";
            break;
          case "E":
            rtnVal = "없음";
            break;
        }
        return rtnVal;
      },
    },
    // {
    //   title: " ",
    //   field: "dot",
    //   formatter: "html",
    //   width: "30",
    //   headerSort: false,
    // },
  ];
});

const changeColumn = ref(false);
watch(rowTarget, () => {
  if (rowTarget.value.length == 0) {
    changeColumn.value == false;
    detailData.value = [];
  } else {
    if (rowTarget.value[0]?.reportType === "정기검사") {
      if (rowTarget.value[0].chkResult !== "합격") {
        changeColumn.value = true;
      } else {
        toast(t("SUCCESS_INSP_RESULT_OK"));
      }
    } else {
      changeColumn.value = false;
    }
    targetInspData();
  }
});

const getIntegreatedInspData = async (startDate, endDate) => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/cc/manage/integrated/insp/list",
      method: "post",
      queryparam: {
        keyword: searchBar.inputs.find((item) => item.name == "keyword").target,
        userPortfolioMappingId:
          useUserStore.portfolio.selected.userPortfolioMappingId,
        startDate: searchBar.inputs
          .find((item) => item.name == "startDate")
          .target.replace(/-/g, ""),
        endDate: searchBar.inputs
          .find((item) => item.name == "endDate")
          .target.replace(/-/g, ""),
        inspType: searchBar.inputs.find((item) => item.name == "inspType").target.code,
      },
    });
  if (code == 200) {
    detailData.value = [];
    elInfoData.value = [];
    tableData.value = [];

    if (data.length > 0) {
      data.forEach((item) => {
        item.replaceStartDate = item.chkDate
          ? item.chkDate.replace(/(\d{4})(\d{2})(\d{2})/g, "$1-$2-$3")
          : "-";
        item.replaceEndDate = item.endDate
          ? item.endDate.replace(/(\d{4})(\d{2})(\d{2})/g, "$1-$2-$3")
          : "-";
        item.chkResult = item.chkResult ? item.chkResult : "-";
        selectedFirstRow.value = true;
      });
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_REPORT_BASE")]));
    }
    tableData.value = data;
  } else {
    toast.warning(t("ERROR_SEARCH", [t("MENU_REPORT_BASE")]));
  }
};
const targetInspData = async () => {
  detailData.value = [];
  elInfoData.value = [];
  const getSelfInspTarget = async () => {
    const { because, code, data, message, result } =
      await useConnectionStore.request({
        url: "/v2/cc/manage/car/self-insp/details",
        method: "post",
        queryparam: {
          selChkBeginDate: rowTarget.value[0].yyyymm,
          elevatorNo: rowTarget.value[0].elevatorNo,
        },
      });
    if (code == 200) {
      let selfInspData;
      if (data.length == 1) {
        if (!!data[0].selchkResultNm == false) {
          data[0].selchkResultNm = "원격 점검";
        }
      } else if (data.length > 1) {
        // data.forEach((item) => {
        //   if (item.selChkResult?.toUpperCase() == "A") {
        //     item.dot = "<div class='dot resultA'></div>";
        //   } else if (item.selChkResult?.toUpperCase() == "B") {
        //     item.dot = "<div class='dot resultB'></div>";
        //   } else if (item.selChkResult?.toUpperCase() == "C") {
        //     item.dot = "<div class='dot resultC'></div>";
        //   } else {
        //     item.dot = "<div class='dot resultOther'></div>";
        //   }
        // });
      } else {
        toast.warning(t("NO_DATA_SEARCH", [t("SELF_INSPECTION")]));
      }
      selfInspData = data;

      return selfInspData;
    } else {
      toast.error(t("ERROR_SEARCH", [t("SELF_INSPECTION")]));
    }
  };
  const getInspTarget = async () => {
    const { because, code, data, message, result } =
      await useConnectionStore.request({
        url: "/v2/cc/manage/car/insp/details",
        method: "post",
        queryparam: {
          failCd: rowTarget.value[0].failCd,
        },
      });
    if (code == 200) {
      if ((data && data.length == 0) || !!data == false) {
        toast.warning(t("NO_DATA_SEARCH", [t("INSPECTION")]));
        return [];
      } else {
        let inspData = data;
        return inspData;
      }
    } else {
      toast.error(t("ERROR_SEARCH", [t("INSPECTION")]));
    }
  };
  const getElInfo = async () => {
    const { because, code, data, message, result } =
      await useConnectionStore.request({
        url: "/v2/cc/manage/integrated/master",
        method: "post",
        queryparam: {
          elevatorNo: rowTarget.value[0].elevatorNo,
        },
      });
    if (code == 200) {
      let elData = data;
      return elData;
    } else {
      toast.error(t("ERROR_SEARCH", [t("MACHINE_BASIC_INFO")]));
    }
  };

  if (rowTarget.value[0].reportType === "정기검사") {
    if (rowTarget.value[0].chkResult !== "합격") {
      try {
        parallelSpiner.value = true;
        const [inspData, elData] = await Promise.all([
          getInspTarget(),
          getElInfo(),
        ]);
        detailData.value = inspData;
        elInfoData.value = elData;
        parallelSpiner.value = false;
      } catch {
        parallelSpiner.value = false;
      }
    } else {
      const elData = await getElInfo();

      elInfoData.value = elData;
    }
  } else if (rowTarget.value[0].reportType === "정기점검") {
    try {
      parallelSpiner.value = true;
      const [selfInspData, elData] = await Promise.all([
        getSelfInspTarget(),
        getElInfo(),
      ]);

      detailData.value = selfInspData;
      elInfoData.value = elData;
      parallelSpiner.value = false;
    } catch {
      parallelSpiner.value = false;
    }
  }
};

const searchBar = reactive({
  inputs: [
    {
      name: "keyword",
      type: "field",
      label: computed(() => t("PRJ_NO")),
      target: "",
      event: () => {
        if (
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 0 ||
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 9
        ) {
          getIntegreatedInspData();
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
      viewType: "month",
      target: startDate,
    },
    {
      name: "endDate",
      cols: 2,
      title: computed(() => t("END_DAY")),
      viewType: "month",

      type: "datePicker",
      target: dayjs().format("YYYY-MM"),
    },
    {
      name: "inspType",
      type: "select",
      title: computed(() => t("TYPE")),
      items: [],
      itemTitle: "value",
      target: undefined,
    },
  ],
  buttons: [
    {
      title: computed(() => t("BTN_SEARCH")),
      methodName: "BTN_SEARCH",
      event: () => {
        if (
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 0 ||
          searchBar.inputs.find((item) => item.name == "keyword").target
            .length == 9
        ) {
          getIntegreatedInspData();
        } else {
          toast.warning(t("WARNING_SEARCH_PRJ_NO_LENGTH"));
        }
      },
    },
  ],
});
provide("searchBar", searchBar);
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
watch(computedPortfolio, async (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    getIntegreatedInspData();
  }
});
onMounted(() => {
  const items = [{code : "all"     , value : "ALL"}
               , {code : "insp"    , value : t("INSPECTION")}
               , {code : "selfInsp", value : t("SELF_INSPECTION")}];

  searchBar.inputs.find((item) => {
    if(item.name == "inspType"){
      item.items = items;
      item.target = {"code" : "all", "value" : "ALL"};
    }
  });

  if (computedPortfolio.value?.userPortfolioMappingId) {
    getIntegreatedInspData();
  }
});
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="12" lg="7" class="pt-0">
      <Skeleton :loadingData="tableData.length == 0" :height="tableHeight">
        <Tabulator
          :pagination="true"
          :columns="columns"
          :selectedFirstRow="selectedFirstRow"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        >
        </Tabulator>
      </Skeleton>
    </v-col>
    <v-col cols="12" lg="5" class="pt-0">
      <v-sheet color="transparent" :style="{ height: tableHeight }">
        <v-toolbar density="compact" color="transparent">
          <v-toolbar-title class="text-button">{{
            t("MACHINE_BASIC_INFO")
          }}</v-toolbar-title>
        </v-toolbar>
        <Skeleton
          :loadingData="!!elInfoData?.elevatorNo == false"
          :type="'heading,paragraph'"
          :height="'181px'"
          class="px-2"
        >
          <v-toolbar density="compact" color="transparent">
            <v-toolbar-title class="text-button">
              {{ `${elInfoData?.buldNm} - ${elInfoData?.installationPlace}` }}
            </v-toolbar-title>
          </v-toolbar>

          <v-container fluid>
            <v-row>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("PRJ_NO") }}
                </v-card-subtitle>
                {{ elInfoData?.prjNo }}
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

                {{ elInfoData?.installationDe }}
              </v-col>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("MAINTENANCE_COMPANY") }}
                </v-card-subtitle>
                {{ elInfoData?.selfCompany }}
              </v-col>
              <v-col cols="8" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("SITE") }}
                </v-card-subtitle>

                {{ elInfoData?.address1 }}
              </v-col>
            </v-row>
          </v-container>
        </Skeleton>
        <Skeleton
          class="mt-5"
          :loadingData="detailData?.length == 0"
          :height="`calc(100vh - 388px)`"
        >
          <Tabulator
            :pagination="true"
            :columns="changeColumn === true ? inspColumns : selfInspColumns"
            :tableData="detailData"
            :height="`calc(100vh - 388px)`"
          />
        </Skeleton>
      </v-sheet>
    </v-col>
  </DefaultContainer>
  <teleport to="#extra-modal" :disabled="false">
    <ParallelSpiner :parallelSpiner="parallelSpiner"></ParallelSpiner>
  </teleport>
</template>

<style>
.dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
}
.resultA {
  background-color: #00c34f;
}
.resultB {
  background-color: rgb(220, 210, 26);
}
.resultC {
  background-color: rgb(175, 46, 46);
}
.resultOther {
  background-color: black;
}
</style>
