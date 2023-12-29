<script setup>
import {
  ref,
  reactive,
  inject,
  computed,
  onMounted,
  watch,
  provide,
} from "vue";
import {
  Tabulator,
  DefaultContainer,
  SearchBarContainer,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
import dayjs from "dayjs";
const startDate = dayjs().subtract(1, "year").format("YYYY-MM-DD");
const toast = useToast();
const useUserStore = userStore();
const { t } = useI18n();
const height = 64 + 64 + 12;
const tableHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();
const rowTarget = ref([]);
const tableData = ref([]);
const detailTableData = ref([]);
const selectedFirstRow = ref(false);
const year = new Date().getFullYear();
const month = new Date().getMonth() + 1;
const date = new Date().getDate();
const secondColumns = computed(() => {
  return [
    {
      title: t("PART_ID"),
      field: "materNo",
      tooltip: true,

      sorter: "string",
    },
    {
      title: t("PART_NAME"),
      field: "materCont",
      sorter: "string",
      widthGrow: 3,
      tooltip: true,
    },
    {
      title: t("PART_DETAIL"),
      field: "masterVolSize",
      sorter: "string",
      widthGrow: 2,

      tooltip: true,
    },
  ];
});
const columns = computed(() => {
  return [
    {
      title: t("PRJ_NO"),
      field: "wbsNo",
      maxWidth: 120,
      sorter: "string",
    },
    {
      title: t("BULD_NM"),
      field: "materCont",
      sorter: "string",
    },
    {
      title: t("REPLACEMENT_DT"),
      field: "convertDate",
      maxWidth: 120,
      sorter: "string",
    },
  ];
});
const partInfo = ref({});
const imgHeight = `calc(100vh - ${64 + 52 + 58}px)`;
const elevator_parts = require("@/assets/img/elevator.png");
watch(rowTarget, (newVal, oldVal) => {
  if (rowTarget.value.length == 0) {
    detailTableData.value = [];
    partInfo.value = undefined;
  }
  if (newVal[0] && newVal[0] !== oldVal[0]) {
    getDetails(rowTarget.value[0]);
  }
});
const getList = async () => {
  detailTableData.value = [];
  partInfo.value = {};
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/unit-service/purchase/list",
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
      data.forEach((item) => {
        item.convertDate = item.chngDt.replace(
          /^(\d{4})(\d{2})(\d{2})$/,
          "$1-$2-$3"
        );
      });
      selectedFirstRow.value = true;
      // await getDetails(data[0]);
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_PARTS_PURCHASE")]));
    }
    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH"[t("MENU_PARTS_PURCHASE")]));
  }
};
const getDetails = async () => {
  detailTableData.value = [];

  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/unit-service/purchase/history",
      method: "post",
      queryparam: {
        wbsNo: rowTarget.value[0].wbsNo,
        chngDt: rowTarget.value[0].chngDt,
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      data.forEach((item) => {
        item.masterVolSize = `${item.materVol} / ${item.materSize}`;
      });
    } else {
      toast.warning(t("NO_DATA_SEARCH"[t("PARTS_PURCHASE_DETAIL")]));
    }
    detailTableData.value = data;
  } else {
    detailTableData.value = [];
    toast.error(t("ERROR_SEARCH"[t("PARTS_PURCHASE_DETAIL")]));
  }
  partInfo.value = {
    siteNm: rowTarget.value[0].materCont,
    elevatorNo: rowTarget.value[0].elevatorNo,
    wbsNo: rowTarget.value[0].wbsNo,
    frstInstallationDe: rowTarget.value[0].frstInstallationDe,
    chngDt: rowTarget.value[0].chngDt.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3"),
    applcEnDt: rowTarget.value[0].applcEnDt,
  };
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
          getList();
        } else {
          toast.warning(t("WARNING_SEARCH_PRJ_NO_LENGTH"));
        }
      },
    },
    {
      cols: 2,
      name: "startDate",
      title: computed(() => t("START_DAY")),
      type: "datePicker",
      target: startDate,
    },
    {
      cols: 2,
      name: "endDate",
      title: computed(() => t("END_DAY")),
      type: "datePicker",
      target: `${year}-${month.toString().padStart(2, "0")}-${date
        .toString()
        .padStart(2, "0")}`,
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
provide("searchBar", searchBar);
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);

watch(computedPortfolio, async (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    getList();
  }
});
onMounted(() => {
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getList();
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
          :selectedFirstRow="selectedFirstRow"
          :pagination="true"
          :columns="columns"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </Skeleton>
    </v-col>
    <v-col cols="4" lg="2" class="pt-0">
      <v-card class="pa-4 customCard">
        <v-img :height="imgHeight" :src="elevator_parts" />
      </v-card>
    </v-col>
    <v-col cols="8" lg="6" class="pt-0">
      <v-sheet color="transparent">
        <v-toolbar density="compact" color="transparent">
          <v-toolbar-title class="text-button">{{
            t("MACHINE_BASIC_INFO")
          }}</v-toolbar-title>
        </v-toolbar>
        <Skeleton
          :loadingData="!!partInfo?.elevatorNo == false"
          :type="'heading,paragraph'"
          :height="'180px'"
          class="px-2"
        >
          <v-toolbar density="compact" color="transparent">
            <v-toolbar-title class="text-button">
              {{ partInfo?.siteNm ?? "" }}
            </v-toolbar-title>
          </v-toolbar>
          <v-container fluid>
            <v-row>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("ELEVATOR_NO") }}
                </v-card-subtitle>

                {{ partInfo?.elevatorNo ?? "" }}
              </v-col>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("PRJ_NO") }}
                </v-card-subtitle>
                {{ partInfo?.wbsNo ?? "" }}
              </v-col>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("INSTALL_DT") }}
                </v-card-subtitle>
                {{ partInfo?.frstInstallationDe ?? "" }}
              </v-col>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("REPLACEMENT_DT") }}
                </v-card-subtitle>
                {{ partInfo?.chngDt ?? "" }}
              </v-col>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("INSPECTION_END_DT") }}
                </v-card-subtitle>
                {{ partInfo?.applcEnDt ?? "" }}</v-col
              >
            </v-row>
          </v-container>
        </Skeleton>
        <Skeleton
          class="mt-5"
          :loadingData="detailTableData.length == 0"
          :height="`calc(100vh - 388px)`"
        >
          <Tabulator
            :pagination="true"
            :columns="secondColumns"
            :tableData="detailTableData"
            :height="`calc(100vh - 388px)`"
          />
        </Skeleton>
      </v-sheet>
    </v-col>
  </DefaultContainer>
</template>

<style scoped></style>
