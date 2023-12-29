<script setup>
import { UserSettingMenu } from "@/component/Template.js";
import { ModalContainer,SearchBarContainer,Tabulator,Skeleton } from "@/component/Template";
import { ref, watch, onMounted, computed, reactive, provide } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
import { userStore } from "@/store/UserStore";
import { uiStore } from "@/store/UiStore";
const form = ref();
const valid = ref(false);
const { t } = useI18n();
const useUiStore = uiStore();
const useUserStore = userStore();
const useConnectionStore = connectionStore();
const toast = useToast();
const computedUserId = computed(() => useUserStore.userInfo.userId);
const computedRoleType = computed(() => useUserStore.userInfo.roleType);
const rules = ref({
  required: (value) => {
    return !!value || t("VALID_CHECK_REQUIRED");
  },
});
const apiList = {
  create: {
    url: "/v2/portfolio/current/create",
    method: "post",
    queryparam: {},
  },
  update: {
    url: "/v2/portfolio/current/update",
    method: "post",
    queryparam: {},
  },
  delete: {
    url: "/v2/portfolio/current/delete",
    method: "post",
    queryparam: {},
  },
  default: {
    url: "/v2/portfolio/current/default",
    method: "post",
    queryparam: {},
  },
  insertDetail: {
    url: "/v2/contract/current/insert",
    method: "post",
    queryparam: {},
  },
  deleteDetail: {
    url: "/v2/contract/current/delete",
    method: "post",
    queryparam: {},
  },
};
const originContract = ref([]);
const portfolioData = ref([]);
const portfolioDetails = ref([]);
const addContractList = ref([]);
const selectedAddContract = ref([]);
const selectedPortfolio = ref();
const selectedContract = ref([]);

const openAddPortfolio = ref();
const openUpdatePortfolio = ref();
const openDeletePortfolio = ref();
const openDefaultPortfolio = ref();
const openAddDetail = ref();
const openDeleteDetail = ref();

const tableData = ref([]);
const height = 64 + 40 + 400;
const tableHeight = `calc(100vh - ${height}px)`;

const checkAddContractList = () => {
  if (portfolioDetails.value.length > 0) {
    addContractList.value = originContract.value.contractList.filter(
      (originItem) => {
        return !portfolioDetails.value.some((userContract) => {
          return originItem.contractCode == userContract.contractCode;
        });
      }
    );
  } else {
    addContractList.value = originContract.value.contractList;
  }
};
watch(openAddDetail, (newVal, oldVal) => {
  if (newVal == false) {
    selectedAddContract.value = [];

    searchBar.inputs.find((item) => {
      if(item.name == "fieldName"  || item.name == "contractNumber" || item.name == "clientName" ||
         item.name == "clientName" || item.name == "accountName"    || item.name == "elevatorNo" )
      {
        item.target = "";
      }
    });
    searchBar2.inputs.find((item) => {
      if(item.name == "officeList")
      {
        item.target = {value1:"ALL"}
      }
      if(item.name == "miriYn" || item.name == "miriCallYn" || item.name == "miriViewYn" || item.name == "miriRobotYn")
      {
        item.target = "ALL";
      }
    });
    getAllDetails();
  }
});
watch(selectedPortfolio, async () => {
  if (selectedPortfolio.value != 0 && !!selectedPortfolio.value == false) {
    portfolioDetails.value = [];
  } else {
    selectedContract.value = [];
    await getDetails(
      useUserStore.portfolio.list[selectedPortfolio.value]
        .userPortfolioMappingId
    );
    checkAddContractList();
  }
});

const getPortfolioList = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/portfolio/current/list",
      method: "get",
    });
  if (code == 200) {
    if (data.length > 0) {
      const returnPortfolio = {
        selected: data.find((item) => item.defaultYn === "y"),
        portfolio: data,
      };

      useUserStore.setPortfolio(returnPortfolio);
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("PORTFOLIO")]));
    }
  } else {
    toast.warning(t("ERROR_SEARCH", [t("PORTFOLIO")]));
  }
};
const getDetails = async (userPortfolioMappingId) => {
  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "/v2/contract/portfolio/details",
      method: "post",
      queryparam: {
        userPortfolioMappingId,
      },
    });
  if (code == 200) {
    if (data.contractList?.length > 0) {
      portfolioDetails.value = data.contractList;
      checkAddContractList();
    } else {
      portfolioDetails.value = [];
    }
  } else {
    toast.warning(t("ERROR_SEARCH", [t("CONTRACT")]));
  }
};

const getAllDetails = async () => {
  selectedAddContract.value = [];
  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "v2/contract/current/details",
      method: "post",
      queryparam: {
        userId: useUserStore.userInfo.userId,
        fieldName : searchBar.inputs.find((item) => item.name == "fieldName").target,
        keyword : searchBar.inputs.find((item) => item.name == "contractNumber").target,
        clientName : searchBar.inputs.find((item) => item.name == "clientName").target,
        accountName : searchBar.inputs.find((item) => item.name == "accountName").target,
        elevatorNo : searchBar.inputs.find((item) => item.name == "elevatorNo").target,
        officeName : searchBar2.inputs.find((item) => item.name == "officeList").target.value1,
        miriYn : searchBar2.inputs.find((item) => item.name == "miriYn").target,
        miriCallYn : searchBar2.inputs.find((item) => item.name == "miriCallYn").target,
        miriViewYn : searchBar2.inputs.find((item) => item.name == "miriViewYn").target,
        miriRobotYn : searchBar2.inputs.find((item) => item.name == "miriRobotYn").target,
      },
    });
  if (code == 200) {
    originContract.value = data;
    checkAddContractList();
  } else {
    toast.error(t("ERROR_PORTFOLIO_API_ERROR"));
  }
};

const callAPIs = async (method) => {
  const request = async (params, modal, type, successText, errorText) => {
    const { because, code, data, message, result } =
      await useConnectionStore.request(params);
    if (code == 200) {
      if (type == "detail") {
        await getPortfolioList();
        await getDetails(
          useUserStore.portfolio.list[selectedPortfolio.value]
            .userPortfolioMappingId
        );
      } else {
        selectedPortfolio.value = undefined;
        await getPortfolioList();
      }

      portfolioData.value = [];

      selectedContract.value = [];
      selectedAddContract.value = [];

      if (modal) {
        modal.value = false;
      }

      toast.success(successText);
    } else {
      toast.error(errorText);
    }
  };
  const triggerAction = {
    create: () => {
      const params = apiList["create"];
      params.queryparam = {
        portfolioName: portfolioData.value.portfolioName,
      };
      request(
        params,
        openAddPortfolio,
        "",
        t("SUCCESS_ADD", [t("PORTFOLIO")]),
        t("ERROR_ADD", [t("PORTFOLIO")])
      );
    },
    update: () => {
      const params = apiList["update"];
      params.queryparam = {
        portfolioName: portfolioData.value.portfolioName,
        userPortfolioMappingId: portfolioData.value.userPortfolioMappingId,
      };
      request(
        params,
        openUpdatePortfolio,
        "",
        t("SUCCESS_UPDATE", [t("PORTFOLIO")]),
        t("ERROR_UPDATE", [t("PORTFOLIO")])
      );
    },
    default: () => {
      const params = apiList["default"];
      if (portfolioData.value.portfolioInContractEa > 0) {
        params.queryparam = {
          userPortfolioMappingId: portfolioData.value.userPortfolioMappingId,
        };
        request(
          params,
          openDefaultPortfolio,
          "",
          t("SUCCESS_DEFAULT_PORTFOLIO"),
          t("ERROR_DEFAULT_PORTFOLIO")
        );
      } else {
        toast.warning(t("WARNING_PORTFOLIO_CONTRACT_EA"));
      }
    },
    delete: () => {
      if (portfolioData.value.defaultYn === "y") {
        toast.warning(t("NO_DEFAULT_PORTFOLIO_REMOVE"));
      } else {
        const params = apiList["delete"];
        params.queryparam = {
          userPortfolioMappingId: portfolioData.value.userPortfolioMappingId,
        };
        request(
          params,
          openDeletePortfolio,
          "",
          t("SUCCESS_REMOVE", [t("PORTFOLIO")]),
          t("ERROR_REMOVE", [t("PORTFOLIO")])
        );
      }
    },
    insertDetail: () => {
      const params = apiList["insertDetail"];
      const lobby = [];
      if(useUserStore.portfolio.list[selectedPortfolio.value].portfolioInContractEa + selectedAddContract.value.length >= 100)
      {
        toast.warning(t("WARNING_PORTFOLIO_CONTRACT_ADD_CNT"));
      } 
      else
      {
        selectedAddContract.value.forEach((contractItem) => {
          lobby.push({
            intgPrjNo: addContractList.value[contractItem].intgPrjNo,
            trlineCd: addContractList.value[contractItem].trlineCd,
          });
        });
        params.queryparam = {
          userPortfolioMappingId:
            useUserStore.portfolio.list[selectedPortfolio.value]
              .userPortfolioMappingId,
          lobby,
        };
        request(
          params,
          openAddDetail,
          "detail",
          t("SUCCESS_PORTOFLIO_ADD", [selectedAddContract.value.length]),
          t("ERROR_PORTOFLIO_ADD")
        );
      }
    },
    deleteDetail: () => {
      if (
        useUserStore.portfolio.list[
          selectedPortfolio.value
        ].portfolioName.toUpperCase() === "LOBBY"
      ) {
        toast.warning(t("NO_LOBBY_CONTRACT_REMOVE"));
      } else {
        const portfolioContractMappingIds = [];

        selectedContract.value.forEach((contractItem) => {
          portfolioContractMappingIds.push(
            portfolioDetails.value[contractItem].portfolioContractMappingId
          );
        });
        const params = apiList["deleteDetail"];
        params.queryparam = {
          portfolioContractMappingIds,
        };
        if (
          useUserStore.portfolio.list[
            selectedPortfolio.value
          ].defaultYn.toUpperCase() === "Y"
        ) {

          console.log("selectedPortfolio.value",useUserStore.portfolio.list[selectedPortfolio.value]);
          if (
            useUserStore.portfolio.list[selectedPortfolio.value]
              .portfolioInContractEa -
              selectedContract.value.length >
            0 || useUserStore.portfolio.list[selectedPortfolio.value].portfolioName != 'lobby'
          ) {
            request(
              params,
              openDeleteDetail,
              "detail",
              t("SUCCESS_PORTOFLIO_REMOVE", [selectedContract.value.length]),
              t("ERROR_PORTOFLIO_REMOVE")
            );
          } else {
            toast.warning(t("WARNING_PORTFOLIO_CONTRACT_REMOVE_EA"));
          }
        } else {
          request(
            params,
            openDeleteDetail,
            "detail",
            t("SUCCESS_PORTOFLIO_REMOVE", [selectedContract.value.length]),
            t("ERROR_PORTOFLIO_REMOVE")
          );
        }
      }
    },
  };
  if (method === "create" || method === "update") {
    await form?.value?.validate();
    if (valid.value) triggerAction[method]();
  } else {
    triggerAction[method]();
  }
};
const searchBar = reactive({
  inputs: [
    {
      name: "fieldName",
      type: "field",
      label: computed(() => t("MAP_FILED_NAME")),
      target: "",
      event: () => {
        getAllDetails();
      },
    },
    {
      name: "contractNumber",
      type: "field",
      label: computed(() => t("CONTRACT_NUMBER")),
      target: "",
      event: () => {
        getAllDetails();
      },
    },
    {
      name: "clientName",
      type: "field",
      label: computed(() => t("CLIENT_NAME")),
      target: "",
      event: () => {
        getAllDetails();
      },
    },
    {
      name: "accountName",
      type: "field",
      label: computed(() => t("ROLE_ACCOUNT")),
      target: "",
      event: () => {
        getAllDetails();
      },
    },
    {
      name: "elevatorNo",
      type: "field",
      label: computed(() => t("ELEVATOR_NO")),
      target: "",
      event: () => {
        if (
          searchBar.inputs.find((item) => item.name == "elevatorNo").target
            .length <= 7
        ) {
          getAllDetails();
        } else {
          toast.warning(t("WARNING_ELEVATOR_NO_OVER_LENGTH"));
        }
      },
    },
  ],
});
provide("searchBar", searchBar);
const searchBar2 = reactive({
  inputs: [
    {
      name: "officeList",
      type: "select",
      title: computed(() => t("BRANCH_OFFICE")),
      items: [],
      itemTitle: "value1",
      target: undefined,
    },
    {
      name: "miriYn",
      type: "select",
      title: computed(() => t("MIRI")),
      items: [],
      itemTitle: "value1",
      target: undefined,
    },
    {
      name: "miriCallYn",
      type: "select",
      title: computed(() => t("MIRI_CALL")),
      items: [],
      itemTitle: "value1",
      target: undefined,
    },
    {
      name: "miriViewYn",
      type: "select",
      title: computed(() => t("MIRI_VIEW")),
      items: [],
      itemTitle: "value1",
      target: undefined,
    },
    {
      name: "miriRobotYn",
      type: "select",
      title: computed(() => t("MIRI_ROBOT")),
      items: [],
      itemTitle: "value1",
      target: undefined,
    },
  ],
  buttons: [
    {
      title: computed(() => t("BTN_SEARCH")),
      methodName: "BTN_SEARCH",
      event: () => {
        if (searchBar.inputs.find((item) => item.name == "elevatorNo").target.length <= 7)
        {
          getAllDetails();
        }
        else
        {
          toast.warning(t("WARNING_ELEVATOR_NO_OVER_LENGTH"));
        }
      },
    },
  ],
});
provide("searchBar2", searchBar2);
const columns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      width: "40",
      headerSort: false,
      hozAlign: "center",
      headerHozAlign: "center",
      resizable : false,
    },
    {
      title: t("MAP_FILED_NAME"),
      field: "siteNm",
      headerHozAlign: "center",
      width:"180"
    },
    {
      title: t("CONTRACT_NUMBER"),
      field: "contractCode",
      headerHozAlign: "center",
      hozAlign: "center",
      width:"150"
    },
    {
      title: t("CLIENT_NAME"),
      field: "custNm",
      headerHozAlign: "center",
      width:"150"
    },
    {
      title: t("PROJECT_TOTAL"),
      field: "intgPrjTrlineCdCode",
      headerHozAlign: "center",
      hozAlign: "center",
      width:"130"
    },
    {
      title: t("ELEVATOR_NO"),
      field: "elevatorNo",
      headerHozAlign: "center",
      hozAlign: "center",
      width:"120"
    },
    {
      title: t("CONTRACT_DATE"),
      field: "elevatorNo",
      headerHozAlign: "center",
      width:"190",
      formatter:function(cell, formatterParams, onRendered){
        let stDt = cell.getData().bgnDt;
        let enDt = cell.getData().xpirDt;
        return stDt.substr(0, 4) + "-" + stDt.substr(4, 2) + "-" + stDt.substr(6, 2) 
              + " ~ " + 
               enDt.substr(0, 4) + "-" + enDt.substr(4, 2) + "-" + enDt.substr(6, 2);
      }
    },
    {
      title: t("ROLE_ACCOUNT"),
      field: "salesEmplNm",
      headerHozAlign: "center",
      width:"110"
    },
    {
      title: t("BRANCH_OFFICE"),
      field: "officeNm",
      headerHozAlign: "center",
      width:"150"
    },
    {
      title: t("MIRI"),
      field: "miriYn",
      headerHozAlign: "center",
      hozAlign: "center",
      width:"80"
    },
    {
      title: t("MIRI_CALL"),
      field: "miriCallYn",
      headerHozAlign: "center",
      hozAlign: "center",
      width:"90"
    },
    {
      title: t("MIRI_VIEW"),
      field: "miriViewYn",
      headerHozAlign: "center",
      hozAlign: "center",
      width:"90"
    },
    {
      title: t("MIRI_ROBOT"),
      field: "miriRobotYn",
      headerHozAlign: "center",
      hozAlign: "center",
      width:"100"
    },
  ];
});
onMounted(async () => {
  const { defaultItem, items } = await useUiStore.getOfficeList();
  const items2 = ['ALL', 'Y', 'N'];

  searchBar2.inputs.find((item) => {
    if (item.name == "officeList") {
      item.items = items;
      item.target = defaultItem;
    }
    if(item.name == "miriYn" || item.name == "miriCallYn" || item.name == "miriViewYn" || item.name == "miriRobotYn"){
      item.items = items2;
      item.target = "ALL";
    }
  });

  if (computedUserId.value) {
    getAllDetails();
  }
});
watch(computedUserId, (newVal, oldVal) => {
  if (!!newVal && computedUserId.value) {
    getAllDetails();
  }
});
</script>
<template>
  <v-container class="settingMenu" fluid>
    <v-row justify="center">
      <v-col sm="4" lg="3" class="pr-0">
        <v-card class="settingMenuCard menuCard rounded-s-lg">
          <UserSettingMenu></UserSettingMenu>
        </v-card>
      </v-col>
      <v-col sm="8" lg="7" class="pl-0">
        <v-card class="settingContentCard rounded-e-lg">
          <v-toolbar class="contentToolbar" color="transparent">
            <v-toolbar-title>{{ t("MENU_SETTING_PORTFOLIO") }}</v-toolbar-title>
          </v-toolbar>
          <v-container height="100%" class="contentInner">
            <v-row>
              <v-col cols="4" class="pa-0">
                <v-toolbar density="compact" color="transparent">
                  <v-toolbar-title>{{ t("PORTFOLIO") }}</v-toolbar-title>
                  <v-spacer></v-spacer>
                  <v-btn
                    @click="[(portfolioData = []), (openAddPortfolio = true)]"
                    ><v-icon>mdi-plus</v-icon></v-btn
                  >
                </v-toolbar>
                <v-divider></v-divider>
                <v-item-group
                  v-model="selectedPortfolio"
                  class="overflow-y-auto pt-2"
                  :style="{
                    height: 'calc(100vh - 245px)',
                  }"
                >
                  <v-item
                    v-slot="{ isSelected, toggle }"
                    v-for="item in useUserStore.portfolio.list"
                    :key="item"
                  >
                    <v-card
                      :color="isSelected ? 'info' : ''"
                      :value="item"
                      rounded="lg"
                      :style="{
                        background:
                          item.defaultYn?.toUpperCase() == 'Y' ? '#00C44F' : '',
                      }"
                      elevation="0"
                      class="d-flex align-center px-4 my-2"
                      @click="toggle"
                    >
                      <span
                        :style="{
                          color:
                            item.defaultYn?.toUpperCase() == 'Y' ? 'white' : '',
                        }"
                      >
                        {{ item.portfolioName }} ({{
                          item.portfolioInContractEa
                        }})
                      </span>
                      <v-spacer></v-spacer>
                      <v-menu location="end">
                        <template v-slot:activator="{ props }">
                          <v-btn
                            :style="{
                              color:
                                item.defaultYn?.toUpperCase() == 'Y'
                                  ? 'white'
                                  : '',
                            }"
                            variant="plain"
                            icon="mdi-dots-vertical"
                            v-bind="props"
                          ></v-btn>
                        </template>
                        <v-card rounded="lg" width="11vw" class="pa-2">
                          <v-list>
                            <v-list-item
                              rounded="lg"
                              value="reset"
                              active-color="primary"
                              @click="
                                [
                                  (portfolioData.portfolioInContractEa =
                                    item.portfolioInContractEa),
                                  (portfolioData.userPortfolioMappingId =
                                    item.userPortfolioMappingId),
                                  (portfolioData.portfolioName =
                                    item.portfolioName),
                                  (openDefaultPortfolio = true),
                                ]
                              "
                            >
                              <template v-slot:prepend>
                                <v-icon icon="mdi-refresh"></v-icon>
                              </template>

                              <v-list-item-title>{{
                                t("SET_DEFAULT")
                              }}</v-list-item-title>
                            </v-list-item>
                            <v-list-item
                              rounded="lg"
                              value="expand"
                              :disabled="item.portfolioName == 'lobby' || (item.portfolioName == 'default' && (computedRoleType == 'ACCOUNT' || computedRoleType == 'ENGINEER'))"
                              active-color="primary"
                              @click="
                                [
                                  (portfolioData.portfolioName =
                                    item.portfolioName),
                                  (portfolioData.userPortfolioMappingId =
                                    item.userPortfolioMappingId),
                                  (openUpdatePortfolio = true),
                                ]
                              "
                            >
                              <template v-slot:prepend>
                                <v-icon icon="mdi-arrow-expand"></v-icon>
                              </template>

                              <v-list-item-title>{{
                                t("CHANGE_NAME")
                              }}</v-list-item-title>
                            </v-list-item>
                            <v-list-item
                              value="delete"
                              :disabled="item.portfolioName == 'lobby' || (item.portfolioName == 'default' && (computedRoleType == 'ACCOUNT' || computedRoleType == 'ENGINEER'))"
                              active-color="primary"
                              rounded="lg"
                              @click="
                                [
                                  (portfolioData.userPortfolioMappingId =
                                    item.userPortfolioMappingId),
                                  (portfolioData.portfolioName =
                                    item.portfolioName),
                                  (portfolioData.defaultYn = item.defaultYn),
                                  (openDeletePortfolio = true),
                                ]
                              "
                            >
                              <template v-slot:prepend>
                                <v-icon
                                  icon="mdi-minus-circle-outline"
                                  color="red"
                                ></v-icon>
                              </template>

                              <v-list-item-title>{{
                                t("REMOVE")
                              }}</v-list-item-title>
                            </v-list-item>
                          </v-list>
                        </v-card>
                      </v-menu>
                    </v-card>
                  </v-item>
                </v-item-group>
              </v-col>
              <v-col cols="8" class="pa-0">
                <v-toolbar density="compact" color="transparent">
                  <v-toolbar-title>{{ t("CONTRACT_DETAIL") }} </v-toolbar-title>
                  <v-spacer></v-spacer>
                  <v-btn
                    @click="openAddDetail = true"
                    :disabled="addContractList?.length === 0 || selectedPortfolio === undefined"
                    ><v-icon>mdi-plus</v-icon></v-btn
                  >
                  <v-btn
                    @click="openDeleteDetail = true"
                    :disabled="
                      (selectedPortfolio &&
                        useUserStore?.portfolio?.list[selectedPortfolio]
                          ?.portfolioName == 'lobby') ||
                      selectedContract?.length === 0
                        ? true
                        : false
                    "
                    ><v-icon>mdi-minus</v-icon></v-btn
                  >
                </v-toolbar>

                <v-divider></v-divider>

                <v-sheet
                  v-show="portfolioDetails?.length > 0"
                  height="71.3vh"
                  color="transparent"
                  class="contractListBox"
                  ref="contractListBox"
                >
                  <v-item-group v-model="selectedContract" multiple>
                    <v-container fluid>
                      <v-row>
                        <v-col
                          cols="12"
                          v-for="(item, i) in portfolioDetails"
                          :key="i"
                        >
                          <v-item v-slot="{ isSelected, toggle }">
                            <v-card
                              :color="isSelected ? 'info' : ''"
                              @click="toggle"
                              class="ma-1 customCard"
                              rounded="lg"
                            >
                              <v-toolbar density="compact" color="transparent">
                                <v-toolbar-title class="text-button">{{
                                  item.siteNm
                                }}</v-toolbar-title>
                              </v-toolbar>
                              <v-container>
                                <v-row>
                                  <v-col cols="6" class="py-2">
                                    <v-card-subtitle class="pl-0 mb-1">{{
                                      t("CONTRACT_CODE")
                                    }}</v-card-subtitle>
                                    {{ item.contractCode }}
                                  </v-col>
                                  <v-col cols="6" class="py-2">
                                    <v-card-subtitle class="pl-0 mb-1">{{
                                      t("CLIENT_NAME")
                                    }}</v-card-subtitle>
                                    <p class="ellipsisText">
                                      {{ item.custNm }}
                                      <v-tooltip
                                        activator="parent"
                                        location="top"
                                      >
                                        {{ item?.custNm }}
                                      </v-tooltip>
                                    </p>
                                  </v-col>
                                  <v-col cols="6" class="py-2">
                                    <v-card-subtitle class="pl-0 mb-1">
                                      {{ t("PROJECT_TOTAL") }}
                                    </v-card-subtitle>
                                    {{ item.intgPrjTrlineCdCode }}
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
                                  <!-- <v-col cols="6" class="py-2">
                                    <v-card-subtitle class="pl-0 mb-1">
                                      {{ t("PROJECT_TOTAL") }}
                                    </v-card-subtitle>
                                    {{ item.prjNo }}</v-col
                                  > -->
                                  <!-- <v-col cols="6" class="py-2">
                                    <v-card-subtitle class="pl-0 mb-1">
                                      {{ t("MODEL") }}
                                    </v-card-subtitle>
                                    {{ item.model }}</v-col
                                  > -->
                                </v-row>
                              </v-container>
                            </v-card>
                          </v-item>
                        </v-col>
                      </v-row>
                    </v-container>
                  </v-item-group>
                </v-sheet>
                <v-sheet
                  color="transparent"
                  height="60vh"
                  class="d-flex justify-center align-center"
                  v-if="portfolioDetails.length == 0"
                >
                  <p class="text-button" v-if="!!selectedPortfolio == false">
                    {{ t("SELECT_PORTFOLIO") }}
                  </p>
                  <p class="text-button" v-if="!!selectedPortfolio">
                    {{ t("NO_CONTRACT_PORTFOLIO") }}
                  </p>
                </v-sheet>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  <ModalContainer
    @btnEvent="callAPIs('create')"
    :text="t('PORTFOLIO_ADD')"
    v-model="openAddPortfolio"
  >
    <v-form v-model="valid" ref="form" class="" @submit.prevent="">
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('PORTFOLIO_NAME')"
        v-model="portfolioData.portfolioName"
        :rules="[rules.required]"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('PORTFOLIO_UPDATE')"
    v-model="openUpdatePortfolio"
    @btnEvent="callAPIs('update')"
  >
    <v-form v-model="valid" ref="form" class="" @submit.prevent="">
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('PORTFOLIO_NAME')"
        v-model="portfolioData.portfolioName"
        :rules="[rules.required]"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    @btnEvent="callAPIs('default')"
    :text="t('PORTFOLIO_DEFAULT')"
    v-model="openDefaultPortfolio"
  >
    <v-card-text>
      '{{ portfolioData.portfolioName }}' {{ t("PORTFOLIO_CHECK_DEFAULT") }}
    </v-card-text>
  </ModalContainer>
  <ModalContainer
    @btnEvent="callAPIs('delete')"
    :text="t('PORTFOLIO_REMOVE')"
    v-model="openDeletePortfolio"
  >
    <v-card-text>
      '{{ portfolioData.portfolioName }}' {{ t("PORTFOLIO_CHECK_REMOVE") }}
    </v-card-text>
  </ModalContainer>

  <ModalContainer
    @btnEvent="callAPIs('insertDetail')"
    :disabled="selectedAddContract.length === 0"
    :text="t('CONTRACT_ADD')"
    v-model="openAddDetail"
    :width="'90vw'"
  >
  <v-item-group 
        v-model="selectedAddContract" 
        multiple 
        v-if="computedRoleType == 'SYSTEM' || computedRoleType == 'ACCOUNT' || computedRoleType == 'ENGINEER'"
    >
      <v-container fluid>
        <v-col cols="12" class="pt-0">
          <SearchBarContainer :breadcrumb="true" :name="'searchBar'"> </SearchBarContainer>
          <SearchBarContainer :breadcrumb="true" :name="'searchBar2'" :style="{marginTop:'5px'}"> </SearchBarContainer>
        </v-col>
        <v-col cols="12" class="pt-0">
          <Tabulator
            :selectable="99"
            :pagination="true"
            :columns="columns"
            :tableData="addContractList"
            :height="tableHeight"
            @update:rowTarget="(returnVal) => {
              const selectedRowIdx = [];
              returnVal.forEach((selectedRowData) => {
                addContractList.forEach((addContractData, dataIdx) => {
                  if(addContractData.elevatorNo == selectedRowData.elevatorNo)
                  {
                    selectedRowIdx.push(dataIdx);
                  }
                });
              });
              selectedAddContract = selectedRowIdx;
              }"
          />
        </v-col>
      </v-container>
    </v-item-group>
    <v-item-group 
        v-model="selectedAddContract" 
        multiple 
        v-if="computedRoleType == 'MAJOR' || computedRoleType == 'MANAGER' || computedRoleType == 'USER'"
    >
      <v-container fluid>
        <v-row>
        </v-row>
        <v-row>
          <v-col cols="4" v-for="(item, i) in addContractList" :key="i">
            <v-item v-slot="{ isSelected, toggle }">
              <v-card
                :color="isSelected ? 'info' : ''"
                @click="toggle"
                class="ma-1 customCard"
                rounded="lg"
              >
                <v-toolbar density="compact" color="transparent">
                  <v-toolbar-title class="text-button">{{
                    item.siteNm
                  }}</v-toolbar-title>
                </v-toolbar>
                <v-container>
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
                      </p>
                    </v-col>
                    <v-col cols="6" class="py-2">
                      <v-card-subtitle class="pl-0 mb-1">
                        {{ t("PROJECT_TOTAL") }}
                      </v-card-subtitle>
                      {{ item.intgPrjTrlineCdCode }}</v-col
                    >
                    <!-- <v-col cols="6" class="py-2">
                      <v-card-subtitle class="pl-0 mb-1">
                        {{ t("PROJECT") }}
                      </v-card-subtitle>
                      {{ item.intgPrjTrlineCdCode }}
                    </v-col> -->
                    <v-col cols="6" class="py-2">
                      <v-card-subtitle class="pl-0 mb-1">
                        {{ t("CONTRACT_DATE") }}
                      </v-card-subtitle>
                      {{
                        item.bgnDt.replace(/(\d{4})(\d{2})(\d{2})/g, "$1-$2-$3")
                      }}
                      -
                      {{
                        item.xpirDt.replace(
                          /(\d{4})(\d{2})(\d{2})/g,
                          "$1-$2-$3"
                        )
                      }}</v-col
                    >
                    <!-- <v-col cols="6" class="py-2">
                      <v-card-subtitle class="pl-0 mb-1">
                        {{ t("PROJECT_TOTAL") }}
                      </v-card-subtitle>
                      {{ item.intgPrjNo }}</v-col
                    >
                    <v-col cols="6" class="py-2">
                      <v-card-subtitle class="pl-0 mb-1">
                        {{ t("MODEL") }}
                      </v-card-subtitle>
                      {{ item.model }}</v-col
                    > -->
                  </v-row>
                </v-container>
              </v-card>
            </v-item>
          </v-col>
        </v-row>
      </v-container>
    </v-item-group>
   <!-- <v-card-actions class="justify-end"> </v-card-actions> -->
  </ModalContainer>
  <ModalContainer
    @btnEvent="callAPIs('deleteDetail')"
    :text="'계약 삭제'"
    v-model="openDeleteDetail"
  >
    <v-card-text>
      {{ t("CHECK_PORTFOLIO_CONTRACT_REMOVE", [selectedContract.length]) }}
    </v-card-text>
  </ModalContainer>
</template>
<style lang="scss"></style>
