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
} from "vue";
import {
  DefaultContainer,
  SearchBarContainer,
  Tabulator,
  Skeleton,
  Jodit,
} from "@/component/Template";

import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { dataStore } from "@/store/DataStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const useDataStore = dataStore();
const useUserStore = userStore();

const rowTarget = ref([]);
const tableData = ref([]);
const height = 64 + 64 + 16;
// 48=header, 64 최상단 search & button  / 16 컨테이너 bottom 패딩 / 4 어디서 나왔는지 모름. no scroll
const tableHeight = `calc(100vh - ${height}px)`;

const columns = computed(() => {
  return [
    {
      title: t("EL_TYPE"),
      field: "elvtrDiv",
    },

    {
      title: t("BASIC_INFO"),
      field: "",
      sorter: "string",
      headerHozAlign: "center",

      hozAlign: "start",
      columns: [
        {
          field: "convertPrjNo",
          title: t("PRJ_NO"),
        },
        {
          field: "elevatorNo",
          title: t("ELEVATOR_NO"),
        },
        {
          title: t("MACHINE_NM"),
          field: "installationPlace",
          minWidth: 200,
        },

        {
          title: t("STATUS"),
          field: "elvtrSttsNm",
        },
        {
          title: t("INSTALL_DT"),
          field: "installationDe",
        },
        {
          title: t("INSPECTION_END_DT"),
          field: "applcEnDt",
          sorter: "string",
        },
      ],
    },

    {
      title: t("MAINTENANCE"),
      headerHozAlign: "center",

      field: "CREATION_USER",
      sorter: "string",
      columns: [
        {
          title: t("COMPANY_NAME"),
          field: "inspCompanyNm",
          sorter: "string",
        },
        {
          title: t("HP"),
          field: "inspCompanyTel",
          sorter: "string",
        },
      ],
    },
    {
      title: t("INSURANCE"),
      headerHozAlign: "center",

      field: "CREATION_USER",
      sorter: "string",
      columns: [
        {
          title: t("COMPANY_NAME"),
          field: "issueCompanyNm",
          sorter: "string",
        },
        {
          title: t("INSURANCE_PERIOD"),
          field: "issueContEnDe",
          sorter: "string",
        },
      ],
    },
    {
      title: t("SAFETY_MANAGER"),
      headerHozAlign: "center",

      field: "CREATION_USER",
      sorter: "string",
      columns: [
        {
          title: t("NAME"),
          field: "shuttleMngrNm",
          sorter: "string",
        },
        {
          title: t("SAFETY_MANAGER_PERIOD"),
          field: "mgrValdEndDt",
          sorter: "string",
        },
      ],
    },
  ];
});
const getData = async () => {
  const { status, code, message, data, because } =
    await useConnectionStore.request({
      url: "/v2/cc/current/elevators",
      method: "post",
      queryparam: {
        prjNo: searchBar.inputs
          .find((item) => item.name == "keyword")
          .target.slice(0, -3),
        carNo: searchBar.inputs
          .find((item) => item.name == "keyword")
          .target.slice(-3),
        userPortfolioMappingId:
          useUserStore.portfolio.selected.userPortfolioMappingId,
        type: searchBar.inputs.find((item) => item.name == "equipmentType")
          .target.code,
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      data.forEach((item) => {
        if (item.prjNo && item.hoNo) {
          item.convertPrjNo = `${item.prjNo}${item.hoNo}`;
        }
        if (item.issueContEnDe) {
          item.issueContEnDe = item.issueContEnDe.replace(
            /^(\d{4})(\d{2})(\d{2})$/,
            "$1-$2-$3"
          );
        }
      });
      // data.sort((a, b) => {
      //   // console.log(a)
      //   const aNum = a.prjNo.slice(0, 6);
      //   const bNum = b.prjNo.slice(0, 6);

      //   // 앞 6자리로 먼저 정렬합니다.
      //   if (aNum !== bNum) {
      //     return aNum.localeCompare(bNum);
      //   }

      //   // 이후 맨 뒤의 2자리로 정렬합니다.
      //   const aStr = a.prjNo.slice(-2);
      //   const bStr = b.prjNo.slice(-2);
      //   return aStr.localeCompare(bStr);
      // });
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_MACHINE")]));
    }
    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("MENU_MACHINE")]));
  }
};

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
          getData();
        } else {
          toast.warning(t("WARNING_SEARCH_PRJ_NO_LENGTH"));
        }
      },
    },
    {
      name: "equipmentType",
      cols: 3,
      type: "select",
      title: computed(() => t("MACHINE_TYPE")),

      itemTitle: "value1",

      items: [],
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
          getData();
        } else {
          toast.warning(t("WARNING_SEARCH_PRJ_NO_LENGTH"));
        }
      },
    },
  ],
});

provide("searchBar", searchBar);
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const computedTypeCode = computed(
  () =>
    searchBar.inputs.find((item) => item.name == "equipmentType").target?.code
);
onMounted(async () => {
  const { defaultItem, items } = await useUiStore.getEquipmentType();
  searchBar.inputs.find((item) => {
    if (item.name == "equipmentType") {
      item.items = items;
      item.target = defaultItem;
    }
  });

});

watch([computedPortfolio, computedTypeCode], async (newVal, oldVal) => {
  if (
    !!newVal[0] &&
    !!newVal[1] &&
    computedPortfolio.value.userPortfolioMappingId &&
    computedTypeCode.value
  ) {
    getData();
  }
});
onUnmounted(() => {
  if (useDataStore.targetElvatorNumber) {
    useDataStore.setElvatorNumber(undefined);
  }
});
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="false"> </SearchBarContainer>
    </v-col>
    <v-col cols="12" class="pt-0 pb-0">
    <v-toolbar-title :style="{textAlign : 'right'}">※ {{ t("INFO_GO_MACHINE_DETAIL") }}</v-toolbar-title>
    </v-col>
    <v-col cols="12" class="pt-0">
      <Skeleton :loadingData="tableData.length == 0" :height="tableHeight">
        <Tabulator
          :tableName="'MachineSearch'"
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
<style lang="scss">
.animation-init {
  opacity: 0;
  transform: translateY(-5px);
}

.animation-fade {
  opacity: 1;
  transform: translateY(0);
  transition: ease-in-out 0.4s;
}
.animation-leave {
  opacity: 0;
  transform: translateY(-5px);
  transition: ease-in-out 0.4s;
}
</style>
