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
import axios from "@/plugin/axios";

import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  SearchBarContainer,
  Jodit,
  Skeleton,
} from "@/component/Template";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";

import { userStore } from "@/store/UserStore";

import { connectionStore } from "@/store/ConnectionStore";
const useConnectionStore = connectionStore();
const toast = useToast();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const { t } = useI18n();
const searchBar = reactive({
  inputs: [
    {
      name: "keyword",
      type: "field",
      label: computed(() => t("CONTRACT_CODE")),
      target: "",
      event: () => {
        getContractData();
        // tableData.value = serviceDetail;
      },
    },
  ],
  buttons: [
    {
      title: computed(() => t("BTN_SEARCH")),

      methodName: "BTN_SEARCH",
      event: () => {
        getContractData();
        // tableData.value = serviceDetail;
      },
    },
  ],
});
provide("searchBar", searchBar);

const columns = computed(() => {
  return [
    // {
    //   formatter: "rowSelection",
    //   titleFormatter: "rowSelection",
    //   maxWidth: "70",
    //   headerSort: false,
    //   hozAlign: "center",
    //   headerHozAlign: "center",
    // },
    {
      title: t("BILLING_DT"),
      field: "convertDate",
      sorter: "string",
    },
    {
      title: t("CONTRACT_NUMBER"),
      field: "compsCntrNo",
      sorter: "string",
    },
    {
      title: t("VIRTUAL_ACCOUNT"),
      field: "virtualAcc",
      sorter: "string",
    },
    {
      title: t("CONTRACT_MONEY"),

      field: "invoiceAmt",
      sorter: "string",
    },
    {
      title: t("UNPAID_MONEY"),

      field: "unbillAmt",
      sorter: "string",
    },
  ];
});
const useUserStore = userStore();

const tableData = ref([]);
const contractList = ref([]);
const selectedContract = ref([]);
const contractListBox = ref(null);
const changeArrow = ref(false);

const getContractData = async () => {
  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "/v2/contract/portfolio/details",
      method: "post",
      queryparam: {
        keyword: searchBar.inputs.find((item) => item.name == "keyword").target,
        userPortfolioMappingId:
          useUserStore.portfolio.selected.userPortfolioMappingId,
      },
    });

  if (code == 200) {
    if (data.contractList.length > 0) {
      selectedContract.value = 0;
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("CONTRACT")]));
    }
    contractList.value = data.contractList;
  } else {
    toast.error(t("ERROR_SEARCH", ["CONTRACT"]));
  }
};
const getContractDetail = async (intgPrjNo, trlineCd) => {
  tableData.value = [];

  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "/v2/cc/bill/history",
      method: "post",
      queryparam: {
        intgPrjNo,
        trlineCd,
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      data.forEach((item) => {
        item.convertDate = item.invoiceDt.replace(
          /(\d{4})(\d{2})(\d{2})/g,
          "$1-$2-$3"
        );
        item.virtualAcc = `${t("NONGHYUP")} ${item.virtualAccNoNh} / ${t(
          "SHINHAN"
        )} ${item.virtualAccNoSh}`;
        item.invoiceAmt = `₩ ${item.invoiceAmt
          .toString()
          .replace(/\B(?=(\d{3})+(?!\d))/g, ",")}`;
        item.unbillAmt = `₩ ${item.unbillAmt
          .toString()
          .replace(/\B(?=(\d{3})+(?!\d))/g, ",")}`;
      });
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("BILL")]));
    }
    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("BILL")]));
  }
};
watch(selectedContract, () => {
  if (selectedContract.value != 0 && !!selectedContract.value == false) {
    tableData.value = [];
  } else {
    getContractDetail(
      contractList.value[selectedContract.value].intgPrjNo,
      contractList.value[selectedContract.value].trlineCd
    );
  }
});
watch(computedPortfolio, (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    getContractData();
  }
});
onMounted(() => {
  // getCompanionDays();
  // getRunAvg();
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getContractData();
  }
});
</script>
<template>
  <DefaultContainer>
    <v-col cols="12">
      <SearchBarContainer
        :breadcrumb="true"
        @update:inputChange="(value, item) => (item.target = value)"
      ></SearchBarContainer>
    </v-col>
    <v-col cols="12" lg="4">
      <Skeleton
        class="contractListBox"
        ref="contractListBox"
        :loadingData="contractList.length == 0"
        :height="'calc(100vh - 164px)'"
        :type="'image,image,image,image'"
      >
        <v-item-group v-model="selectedContract">
          <v-container fluid>
            <v-row>
              <v-col cols="12" v-for="(item, i) in contractList" :key="i">
                <v-item v-slot="{ isSelected, toggle }">
                  <v-card
                    :color="isSelected ? 'info' : ''"
                    @click="toggle"
                    class="ma-1 customCard pb-1"
                  >
                    <v-toolbar density="compact" color="transparent">
                      <v-toolbar-title class="text-button">{{
                        item.siteNm
                      }}</v-toolbar-title>
                    </v-toolbar>
                    <v-container fluid>
                      <v-row>
                        <v-col cols="6" class="py-2">
                          <v-card-subtitle class="pl-0 mb-1">{{
                            t("CONTRACT_NUMBER")
                          }}</v-card-subtitle>
                          {{ item.contractCode }}
                        </v-col>
                        <v-col cols="6" class="py-2">
                          <v-card-subtitle class="pl-0 mb-1">{{
                            t("CLIENT_NAME")
                          }}</v-card-subtitle>
                          <p class="ellipsisText">
                            {{ item.custNm }}
                            <v-tooltip activator="parent" location="top">
                              {{ item?.custNm }}
                            </v-tooltip>
                          </p>
                        </v-col>

                        <v-col cols="6" class="py-2">
                          <v-card-subtitle class="pl-0 mb-1">
                            {{ t("CONTRACT_DATE") }}
                          </v-card-subtitle>
                          {{
                            item.bgnDt.replace(
                              /(\d{4})(\d{2})(\d{2})/g,
                              "$1-$2-$3"
                            )
                          }}
                          -
                          {{
                            item.xpirDt.replace(
                              /(\d{4})(\d{2})(\d{2})/g,
                              "$1-$2-$3"
                            )
                          }}</v-col
                        >
                        <v-col cols="6" class="py-2">
                          <v-card-subtitle class="pl-0 mb-1">
                            {{ t("PROJECT_TOTAL") }}
                          </v-card-subtitle>
                          {{ item.intgPrjTrlineCdCode }}</v-col
                        >
                      </v-row>
                    </v-container>
                  </v-card>
                </v-item>
              </v-col>
            </v-row>
          </v-container>
        </v-item-group>
      </Skeleton>
    </v-col>
    <v-col cols="12" lg="8">
      <Skeleton
        :loadingData="tableData.length == 0"
        :height="'calc(100vh - 426px)'"
      >
        <Tabulator
          :columns="columns"
          :pagination="true"
          :tableData="tableData"
          :height="'calc(100vh - 426px)'"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </Skeleton>
      <v-card class="mt-4 pb-4 customCard">
        <v-card-title class="pt-3"> 안내사항 </v-card-title>
        <v-list lines="3">
          <v-list-item>
            <v-list-item-title>1) 분담이행에 관한 안내</v-list-item-title>
            <v-list-item-subtitle>
              분담이행계약의 경우, 고객님께서 직영(당사) 및 협력사에게
              유지관리료를 각각 지급하는 계약입니다. 이에 본 안내에서는 직영
              대금에 한하여, 안내드리오니 참고해주시기
              바랍니다.</v-list-item-subtitle
            >
            <!-- <template v-slot:append>
              <v-list-item-action end>
                <v-btn icon="mdi-chevron-right" variant="plain"> </v-btn>
              </v-list-item-action>
            </template> -->
          </v-list-item>
          <v-list-item class="py-3">
            <v-list-item-title>2) 가상계좌 입금 요청</v-list-item-title>
            <v-list-item-subtitle
              >원할한 입금 확인을 위해, 반드시 고객전용 가상계좌로 입금
              부탁드립니다.</v-list-item-subtitle
            >
            <!-- <template v-slot:append>
              <v-list-item-action end>
                <v-btn icon="mdi-chevron-right" variant="plain"> </v-btn>
              </v-list-item-action>
            </template> -->
          </v-list-item>
          <v-list-item>
            <v-list-item-title>3) 발송기준과 수금 차이 안내</v-list-item-title>
            <v-list-item-subtitle
              >발송기준일에 따라, 납부금액이 일부 차이가 있을 수 있는 점 양해
              부탁드립니다.</v-list-item-subtitle
            >
            <!-- <template v-slot:append>
              <v-list-item-action end>
                <v-btn icon="mdi-chevron-right" variant="plain"> </v-btn>
              </v-list-item-action>
            </template> -->
          </v-list-item>
        </v-list>
      </v-card>
    </v-col>
  </DefaultContainer>
</template>
