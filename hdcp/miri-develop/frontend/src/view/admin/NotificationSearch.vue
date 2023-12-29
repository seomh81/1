<script setup>
import { ref, reactive, computed, onMounted, provide } from "vue";
import {
  DefaultContainer,
  SearchBarContainer,
  Tabulator,
  Skeleton,
} from "@/component/Template";

import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const useUserStore = userStore();
const year = new Date().getFullYear();
const month = new Date().getMonth() + 1;
const date = new Date().getDate();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const rowTarget = ref([]);
const tableData = ref([]);
const height = 64 + 64 + 12;
const tableHeight = `calc(100vh - ${height}px)`;

const columns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      maxWidth: "70",
      headerSort: false,
      hozAlign: "center",
      vertAlign: "middle",
      headerHozAlign: "center",
    },
    {
      field: "registDt",
      title: t("NOTIFICATION_DT"),
      sorter: "string",
    },
    {
      title: t("NOTIFICATION_TYPE"),
      field: "alarmType",
      sorter: "string",
    },
    {
      title: t("EVENT"),
      field: "contents",
      sorter: "string",
    },
    {
      title: t("NOTIFICATION_METHOD"),
      field: "alarmMethod",
      sorter: "string",
    },
    {
      title: t("RECEVIER_ID"),
      field: "receiverId",
      sorter: "string",
    },
    {
      title: t("RECEVIER_HP"),
      field: "replacePhoneNo",

      sorter: "string",
    },
  ];
});

const getList = async () => {
  tableData.value = [];
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/cc/alarm/search",
      method: "post",
      queryparam: {
        userId: useUserStore.userInfo.userId,
        alarmType: searchBar.inputs.find((item) => item.name == "serviceType")
          .target.code,
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
        if (item.receiverPhoneNo) {
          item.replacePhoneNo = item.receiverPhoneNo
            .replace(/[^0-9]/g, "")
            .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
            // eslint-disable-next-line
            .replace(/(\-{1,2})$/g, "");
        }
      });
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_SYSTEM_NOTIFICATION")]));
    }

    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("MENU_SYSTEM_NOTIFICATION")]));
  }
};

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
      target: `${year}-${month.toString().padStart(2, "0")}-01`,
    },
    {
      name: "endDate",
      cols: 2,
      title: computed(() => t("START_END")),
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
        getList();
      },
    },
  ],
});
onMounted(async () => {
  const { defaultItem, items } = await useUiStore.getAlarmType();
  searchBar.inputs.find((item) => {
    if (item.name == "serviceType") {
      item.items = items;
      item.target = defaultItem;
    }
  });
  await getList();
});
provide("searchBar", searchBar);
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="12" class="pt-0">
      <Skeleton :loadingData="tableData.length == 0" :height="tableHeight">
        <Tabulator
          :columns="columns"
          :pagination="true"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </Skeleton>
    </v-col>
  </DefaultContainer>
</template>

