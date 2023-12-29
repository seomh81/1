<script setup>
import { ref, reactive, computed, onMounted, watch, provide } from "vue";
import {
  Tabulator,
  DefaultContainer,
  SearchBarContainer,
  Skeleton,
} from "@/component/Template";
import ServiceRequestModal from "@/component/ServiceRequestModal.vue";
import { userStore } from "@/store/UserStore";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { useI18n } from "vue-i18n";
import dayjs from "dayjs";

import { useToast } from "vue-toastification";
const year = new Date().getFullYear();
const month = new Date().getMonth() + 1;
const date = new Date().getDate();
const startDate = dayjs().subtract(3, "month").format("YYYY-MM-DD");
const useUiStore = uiStore();
const toast = useToast();
const { t } = useI18n();
const useUserStore = userStore();
const height = 64 + 64 + 12;
const tableHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();

const tableData = ref([]);
const dialog = ref(false);
const columns = computed(() => {
  return [

    {
      title: t("REGISTER_DT"),
      field: "registDt",
      minWidth: 180,
      sorter: "string",
    },
    {
      title: t("SERVICE_TYPE"),
      field: "serviceNm",
      sorter: "string",
    },
    {
      title: t("DETAIL"),
      field: "serviceContents",
      tooltip: true,

      sorter: "string",
      widthGrow: 4,
    },
    {
      title: t("RECEIVER"),
      sorter: "string",
      headerHozAlign: "center",

      hozAlign: "start",
      columns: [
        {
          title: t("NAME"),

          field: "receiverNm",
          sorter: "string",
        },
        {
          title: t("EMAIL"),

          field: "receiverEmail",
          sorter: "string",
        },
      ],
    },
    {
      title: t("EL"),
      sorter: "string",
      headerHozAlign: "center",

      hozAlign: "start",
      columns: [
        {
          title: t("PRJ_NO"),
          field: "convertPrjNo",
        },

        {
          title: t("ELEVATOR_NO"),
          field: "elevatorNo",
        },
        {
          title: t("INSTALL_DT"),
          field: "frstInstallationDe",
        },
      ],
    },
  ];
});

const searchBar = reactive({
  inputs: [
    {
      name: "serviceType",
      cols: 2,
      title: computed(() => t("SERVICE_TYPE")),
      type: "select",
      items: [],
      itemTitle: "value1",
      target: undefined,
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
      target: `${year}-${month.toString().padStart(2, "0")}-${date
        .toString()
        .padStart(2, "0")}`,
    },

    {
      name: "keyword",

      type: "field",
      label: computed(() => t("DETAIL")),
      target: "",
      event: () => {
        getServiceData();
      },
    },
  ],
  buttons: [
    {
      title: computed(() => t("BTN_SEARCH")),
      methodName: "BTN_SEARCH",

      event: () => {
        getServiceData();
      },
    },
    {
      title: computed(() => t("SERVICE_REQUEST")),
      icon: "mdi-message-outline",
      event: () => {
        dialog.value = true;
      },
    },
  ],
});
provide("searchBar", searchBar);
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);

const getServiceData = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/cc/current/service",
      method: "post",
      queryparam: {
        startDate: searchBar.inputs
          .find((item) => item.name == "startDate")
          .target.replace(/-/g, ""),
        endDate: searchBar.inputs
          .find((item) => item.name == "endDate")
          .target.replace(/-/g, ""),
        userPortfolioMappingId:
          useUserStore.portfolio.selected.userPortfolioMappingId,
        serviceType: searchBar.inputs.find((item) => item.name == "serviceType")
          .target.code,
        searchKeyword: searchBar.inputs.find((item) => item.name == "keyword")
          .target,
      },
    });
  if (code == 200) {
    tableData.value = data;

    if (data.length == 0) {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_SERVICE_DETAIL")]));
    } else {
      data.forEach((item) => {
        item.convertPrjNo = item.prjNo + item.hoNo;
      });
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("MENU_SERVICE_DETAIL")]));
  }
};
watch(computedPortfolio, async (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    await getServiceData();
  }
});
watch(dialog, (newVal) => {
  if (newVal == false) {
    getServiceData();
  }
});
onMounted(async () => {
  const { defaultItem, items } = await useUiStore.getServcieType();
  searchBar.inputs.find((item) => {
    if (item.name == "serviceType") {
      item.items = items;
      item.target = defaultItem;
    }
  });
  if (computedPortfolio.value?.userPortfolioMappingId) {
    await getServiceData();
  }
});
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="12" class="pt-0">
      <Skeleton :loadingData="tableData.length == 0" :height="tableHeight">
        <Tabulator
          :pagination="true"
          :columns="columns"
          :tableData="tableData"
          :height="tableHeight"
          :selectable="false"
        >
        </Tabulator>
      </Skeleton>
    </v-col>
  </DefaultContainer>
  <ServiceRequestModal v-model="dialog" v-if="dialog"></ServiceRequestModal>
</template>

<style scoped></style>
