<script setup>
import { ref, reactive, computed, onMounted, watch, provide } from "vue";
import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  SearchBarContainer,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { useRoute } from "vue-router";
import { userStore } from "@/store/UserStore";
import { useToast } from "vue-toastification";

import * as XLSX from "xlsx";
import { useI18n } from "vue-i18n";
const toast = useToast();
const route = useRoute();
const injectTableRows = ref(false);
const { t } = useI18n();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const useUserStore = userStore();
const joinUserModal = ref(false);
const updateUserModal = ref(false);
const removeUserModal = ref(false);
const resetPasswordModal = ref(false);
const approvalUserModal = ref(false);
const rejectUserModal = ref(false);
const importDetailModal = ref(false);
const importDetailModal2 = ref(false);
const copyContractDetailModal = ref(false);
const confirmCopyContractModal = ref(false);
const importTableData = ref([]);
const fromContractTableData = ref([]);
const toUserTableData = ref([]);
const toUserRowTarget = ref([]);
const copyFromSelectedUserInfo = ref();
const checkDupliImportIDModal = ref(false);
const checkDupliImportIDModal2 = ref(false);
const contractEditModal = ref(false);
const rowTarget = ref([]);
const tableData = ref([]);
const nonContractData = ref([]);
const height = 64 + 64 + 12 + 48;
const tableHeight = `calc(100vh - ${height}px)`;
const search = ref({
  searchKeyword: "",
  searchKeyword2:"",
  roleType:"",
  accountStatus: "",
  delYn: "",
  contractYn: "",
});
const search2 = ref({
  searchKeyword: "",
  searchKeyword2:"",
  roleType:"",
});
const importTableRowTarget = ref([]);
const roleTypeItem = ref([]);
const accountStatusItem = ref([]);
const file = ref("");
const selectedUserType = ref();
const sucessCheckId = ref(false);
const idCheckReady = ref(false);
const checkAlreadyId = ref(false);
const recoveryAccountModal = ref(false);
const computedRoleType = computed(() => useUserStore?.userInfo?.roleType);
const rules = ref({
  required: (value) => {
    return !!value || t("VALID_CHECK_REQUIRED");
  },
  email: (value) => {
    sucessCheckId.value = false;

    const pattern =
      // eslint-disable-next-line
      /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;

    if (pattern.test(value)) {
      idCheckReady.value = true;
    } else {
      idCheckReady.value = false;

      return t("VALID_CHECK_EMAIL");
    }
  },
  phone: (value) => {
    const pattern = /[0-9]/g;

    if (pattern.test(value)) {
      let pattern = value
        .replace(/[^0-9]/g, "")
        .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
        // eslint-disable-next-line
        .replace(/(\-{1,2})$/g, "");
      userData.value.phonenumber = pattern;

      return !(pattern && pattern.length < 13) || t("VALID_CHECK_PHONE_LENGTH");
    } else {
      userData.value.phonenumber = "";

      return t("VALID_CHECK_PHONE_NUMBER");
    }
  },
  phoneJoin: (value) => {
    const pattern = /[0-9]/g;

    if (pattern.test(value)) {
      let pattern = value
        .replace(/[^0-9]/g, "")
        .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
        // eslint-disable-next-line
        .replace(/(\-{1,2})$/g, "");
      userJoinData.phonenumber = pattern;

      return !(pattern && pattern.length < 13) || t("VALID_CHECK_PHONE_LENGTH");
    } else {
      userJoinData.phonenumber = "";

      return t("VALID_CHECK_PHONE_NUMBER");
    }
  },
});
const addNewContractModal = ref(false);
const exportCheckTableButton = ref(false);
const exportCheckData = ref(false);

const columns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      maxWidth: "70",
      headerSort: false,
      hozAlign: "center",
      vertAlign: "center",
      headerHozAlign: "center",
      download: false,
    },
    // {
    //   title: "아바타",
    //   field: "avatarUrl",
    //   headerSort: false,

    //   headerHozAlign: "center",
    //   maxWidth: 100,
    //   vertAlign: "middle",
    //   hozAlign: "center",
    //   download: false,
    //   formatter: function (cell, formatterParams) {
    //     const avatar = cell.getValue();
    //     // return '<img style='border - radius: 50 %; width: 30px'; height: 30px; src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAZCAYAAADE6YVjAAAAJUlEQVR42u3NQQEAAAQEsJNcdFLw2gqsMukcK4lEIpFIJBLJS7KG6yVo40DbTgAAAABJRU5ErkJggg==">';
    //     return avatar
    //       ? `<img style='border-radius: 50%; width: 30px; height: 30px;'
    //           src=${avatar}>`
    //       : `<img style='border-radius: 50%; width: 30px; height: 30px;'
    //           src=${avatarTemp}>`; // <img style='border-radius: 50%; width: 30px'; height: 30px; ' src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAZCAYAAADE6YVjAAAAJUlEQVR42u3NQQEAAAQEsJNcdFLw2gqsMukcK4lEIpFIJBLJS7KG6yVo40DbTgAAAABJRU5ErkJggg==">
    //   },
    // },
    {
      title: t("ID"),
      field: "userId",
      sorter: "string",
      vertAlign: "middle",
    },

    {
      title: t("NAME"),
      field: "userName",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("HP"),
      field: "phonenumber",
      sorter: "string",
      vertAlign: "middle",
    },
/* 요청으로 삭제 20230710
    {
      title: t("ADDRESS"),
      field: "address",
      download: false,
      sorter: "string",

      vertAlign: "middle",
      tooltip: true,
    }, */
    {
      title: t("ROLE"),
      field: "convertRoleType",
      sorter: "string",
      vertAlign: "middle",
      download: false,
    },
    {
      title: t("STATUS"),
      field: "accountStatusString",
      sorter: "string",
      vertAlign: "middle",
      download: false,
    },
    {
      title: t("ACCOUNT_STATUS"),
      field: "convertDelYn",
      sorter: "string",
      vertAlign: "middle",
      download: false,
    },
    {
      title: t("CONTRACT_NUMBER"),
      field: "compsCntrNo",
      sorter: "string",
      vertAlign: "middle",
      hozAlign: "center",
      download: useUserStore.userInfo.roleType.toUpperCase() === "SYSTEM" || useUserStore.userInfo.roleType.toUpperCase() === "ACCOUNT" ? true : false,
    },
    {
      title: t("BUILD_NAME"),
      field: "buildNm",
      sorter: "string",
      vertAlign: "middle",
      download: false,
    },
    {
      title: t("REGISTER_DT"),
      field: "creationDt",
      sorter: "string",
      vertAlign: "middle",
      download: false,
    },
  ];
});
const importColumns = computed(() => {
  return [
    {
      title: t("ID"),
      field: "userId",
      sorter: "string",
      vertAlign: "middle",
    },

    {
      title: t("NAME"),
      field: "userName",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("HP"),
      field: "phonenumber",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("EMPLOYEE_NUMBER"),
      field: "empId",
      sorter: "string",
      vertAlign: "middle",
      visible:
        useUserStore.userInfo.roleType.toUpperCase() === "SYSTEM"
          ? true
          : false,
    },
    {
      title: t("CONTRACT_NUMBER"),
      field: "compsCntrNo",
      sorter: "string",
      vertAlign: "middle",
      visible:
      useUserStore.userInfo.roleType.toUpperCase() === "ACCOUNT"
          ? true
          : false,
    },
    {
      title: t("TYPE"),
      field: "registType",
      sorter: "string",
      vertAlign: "middle",
      visible: false,
      download: true,
    },
  ];
});
const importColumns2 = computed(() => {
  return [
    {
      title: t("ID"),
      field: "userId",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("NAME"),
      field: "userName",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("ROLE"),
      field: "role",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title : t("CONTRACT_NUMBER"),
      field : "compsCntrNo",
      sorter : "string",
      vertAlign : "middle",
    },
    {
      title: t("TYPE"),
      field: "registType",
      sorter: "string",
      vertAlign: "middle",
      visible: false,
      download: true,
    },
  ];
});
const exportColumns = computed(() => {
  return [
  {
      title: t("ID"),
      field: "userId",
      sorter: "string",
      vertAlign: "middle",
    },

    {
      title: t("NAME"),
      field: "userName",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("HP"),
      field: "phonenumber",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("ROLE"),
      field: "roleType",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("STATUS"),
      field: "status",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("ACCOUNT_STATUS"),
      field: "delYn",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("TRLINE"),
      field: "intgPrjTrlineCdCode",
      sorter: "string",
      vertAlign: "middle",
      hozAlign: "center",
    },
    {
      title: t("CONTRACT_NUMBER"),
      field: "compsCntrNo",
      sorter: "string",
      vertAlign: "middle",
      hozAlign: "center",
      visible : false,
      download : true,
    },
  ];
});
const fromContractColumns = computed(() => {
  return [
    {
      title: t("CONTRACT_NUMBER"),
      field: "contractCode",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("TRLINE"),
      field: "intgPrjTrlineCdCode",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("MACHINE_NM"),
      field: "siteNm",
      sorter: "string",
      vertAlign: "middle",
    },
  ];
});
const toUserColumns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      maxWidth: "70",
      headerSort: false,
      hozAlign: "center",
      vertAlign: "center",
      headerHozAlign: "center",
    },
    {
      title: t("ID"),
      field: "userId",
      sorter: "string",
      vertAlign: "middle",
    },

    {
      title: t("NAME"),
      field: "userName",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("HP"),
      field: "phonenumber",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: t("ROLE"),
      field: "convertRoleType",
      sorter: "string",
      vertAlign: "middle",
    },
  ];
});
const apiList = {
  get: {
    url: "/v2/user/list",
    method: "get",
    queryparam: {},
  },
  update: {
    url: "/v2/user/manage-update",
    method: "post",
    queryparam: {},
  },
  remove: {
    url: "/v2/user/remove",
    method: "post",
    queryparam: {},
  },
  contractSearch: {
    url: "/v2/contract/auth/search",
    method: "post",
    queryparam: {},
  },
  contractCurrentManager: {
    url: "/v2/contract/current-manager/contract",
    method: "post",
    queryparam: {},
  },
  majorJoin: {
    url: "/v2/user/join/major",
    method: "post",
    queryparam: {},
  },
  managerJoin: {
    url: "/v2/user/join/manager",
    method: "post",
    queryparam: {},
  },
  userJoin: {
    url: "/v2/user/join/user",
    method: "post",
    queryparam: {},
  },
  accountJoin: {
    url: "/v2/user/join/account",
    method: "post",
    queryparam: {},
  },
  engineerJoin: {
    url: "/v2/user/join/engineer",
    method: "post",
    queryparam: {},
  },
  manyJoin: {
    url: "/v2/user/join/many",
    method: "post",
    queryparam: {},
  },
  employeeSearch: {},
  resetPassword: {
    url: "/v2/user/reset-password",
    method: "post",
    queryparam: {},
  },
  userRejectingApproval: {
    url: "/v2/user/update/manage/account-status",
    method: "post",
    queryparam: {},
  },
  addNewContract: {
    url: "/v2/contract/current/add",
    method: "post",
    queryparam: {},
  },
  removeContract: {
    url: "/v2/contract/current/lobby/delete",
    method: "post",
    queryparam: {},
  },
  recovery: {
    url: "/v2/user/recovery",
    method: "post",
    queryparam: {},
  },
  getInsertContract: {
    url: "/v2/contract/auth/compsCntrNo-search",
    method: "post",
    queryparam: {},
  },
  getNonContractList: {
    url: "/v2/user/nonContractList",
    method: "get",
    queryparam: {},
  },
  setNonContractUser: {
    url: "/v2/user/setNonContractUser",
    method: "post",
    queryparam: {},
  },
  copyFromContractTo: {
    url: "/v2/contract/copyFromContractTo",
    method: "post",
    queryparam: {},
  },
};
const contractColumns = computed(() => [
  {
    formatter: "rowSelection",
    titleFormatter: "rowSelection",
    width: "70",
    headerSort: false,
    hozAlign: "center",
    headerHozAlign: "center",
  },
  {
    title: t("PROJECT_TOTAL"),
    field: "intgPrjNo",
    sorter: "string",
  },
  {
    title: t("TRLINE"),
    field: "trlineCd",
    sorter: "string",
  },
  {
    title: t("BULD_NM"),
    field: "siteNm",
    sorter: "string",
  },
]);

const majorTemp = {
  userId: "",
  userName: "",
  phonenumber: "",
  lobby: [],
};
const managerTemp = {
  userId: "",
  phonenumber: "",
  userName: "",
  contractItems: "",
  selectContract: "",
};
const accountTemp = {
  userId: "",
  userName: "",
  phonenumber: "",
  roleType: undefined,
  empId: "",
  lobby: [],
};
const userData = ref({});
const userJoinData = reactive({});
const userJoinData2 = ref({
  userId: "",
  userName: "",
  password: "",
  note: "",
  phonenumber: "",
  termsServiceUseAg: "n",
  termsPlInfoUsingAg: "n",
  termsPlInfoStoreTimeAg: "n",
  termsAdRecvAg: "n",
  lobby: [],
});
const form = ref();
const valid = ref(false);

const searchBar = reactive({
  inputs: [
    {
      type: "field",
      label: computed(() => t("KEYWORD") + " - " + t("ID") + " / " + t("NAME")),
      target: computed({
        get() {
          return search.value.searchKeyword;
        },
        set(e) {
          search.value.searchKeyword = e;
        },
      }),
      event: () => {
        getListEvent();
      },
    },
    {
      type: "field",
      label: computed(() => t("KEYWORD") + " - " + t("CONTRACT_NUMBER") + " / " + t("BUILD_NAME")),
      target: computed({
        get() {
          return search.value.searchKeyword2;
        },
        set(e) {
          search.value.searchKeyword2 = e;
        },
      }),
      event: () => {
        getListEvent();
      },
    },
    {
      cols: 2,
      type: "select",
      title: computed(() => t("ROLE")),
      items: computed(() => roleTypeItem.value),
      itemTitle: "value1",
      target: computed({
        get() {
          return search.value.roleType;
        },
        set(e) {
          search.value.roleType = e;
        },
      }),
    },
    {
      cols: 2,
      type: "select",
      title: computed(() => t("STATUS")),

      items: computed(() =>
        accountStatusItem.value
          ? accountStatusItem.value
          : ["ALL", "0010", "0020", "0030", "0040", "4000", "9999"]
      ),
      itemTitle: "value1",
      target: computed({
        get() {
          return search.value.accountStatus;
        },
        set(e) {
          search.value.accountStatus = e;
        },
      }),
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",
      title: computed(() => t("BTN_SEARCH")),

      event: () => {
        getListEvent();
      },
    },
    {
      methodName: "BTN_CREATE",

      title: computed(() => t("BTN_CREATE")),

      event: async () => {
        openCreateModal();
        joinUserModal.value = true;
      },
    },
    {
      methodName: "BTN_UPDATE",

      title: computed(() => t("BTN_UPDATE")),

      disabled: computed(
        () => !(rowTarget.value[0]?.userId && rowTarget.value.length == 1)
      ),

      event: () => {
        userData.value = { ...rowTarget.value[0] };
        updateUserModal.value = true;
      },
    },
    {
      methodName: "BTN_DELETE",

      title: computed(() => t("BTN_DELETE")),

      disabled: computed(
        () =>
          !rowTarget.value.length >= 1 ||
          !rowTarget.value.every((item) => {
            return item.delYn === "n";
          })
      ),

      event: () => {
        removeUserModal.value = true;
      },
    },
  ],
  secondButtons: [
    {
      methodName: "BTN_MAIL_RESEND",
      title: computed(() => t("MAIL_RESEND")),
      disabled: computed(
        () =>
          !rowTarget.value.length > 0 ||
          !rowTarget.value.every((item) => {
            return item.accountStatus === "0001";
          })
      ),
      icon: "mdi-email-outline",
      event: () => {
        let sendList = {userIds : []};
        rowTarget.value.forEach((item) => {
          sendList.userIds.push(item.userId);
        });
        mailReSend(sendList);
      },
    },
    {
      methodName: "BTN_RECOVERY_ACCOUNT",
      title: computed(() => t("RECOVERY_ACCOUNT")),
      disabled: computed(
        () =>
          !rowTarget.value.length > 0 ||
          !rowTarget.value.every((item) => {
            return item.delYn === "y";
          })
      ),
      icon: "mdi-refresh-circle",
      event: () => {
        recoveryAccountModal.value = true;
      },
    },
    {
      methodName: "BTN_NEW_CONTRACT_SETTING",
      title: computed(() => t("NEW_CONTRACT_SETTING")),
      disabled: computed(() => !rowTarget.value.length > 0),
      icon: "mdi-draw",
      event: () => {
        userData.value = { ...rowTarget.value[0] };
        contractEditModal.value = true;
        contractList.value = [];
        selectedContract.value = [];
        getContractData();
      },
    },
    {
      methodName: "BTN_APPROVAL",
      icon: "mdi-check",
      title: computed(() => t("BTN_APPROVAL")),
      disabled: computed(() => !rowTarget.value.length > 0),

      event: () => {
        approvalUserModal.value = true;
      },
    },
    {
      methodName: "BTN_REJECT",
      icon: "mdi-cancel",
      title: computed(() => t("BTN_REJECT")),
      disabled: computed(() => !rowTarget.value.length >= 1),

      event: () => {
        rejectUserModal.value = true;
      },
    },
    {
      methodName: "BTN_RESET_PASSWORD",
      icon: "mdi-lock-reset",
      title: computed(() => t("BTN_RESET_PASSWORD")),
      disabled: computed(
        () => !(rowTarget.value[0]?.userId && rowTarget.value.length == 1)
      ),
      event: () => {
        userData.value = { ...rowTarget.value[0] };
        resetPasswordModal.value = true;
      },
    },

    {
      methodName: "BTN_IMPORT",
      title: computed(() => t("BTN_IMPORT")),
      icon: "mdi-import",
      event: async () => {
        openCreateModal();
        importDetailModal.value = true;
      },
    },
    {
      methodName: "BTN_EXPORT",
      icon: "mdi-export",
      title: computed(() => t("BTN_EXPORT")),
      disabled: computed(() => !(tableData.value.length >= 1)),
      event: () => {
        if (tableData.value) {
          exportBtnWatch.value = true;
        }
        setTimeout(() => {
          exportBtnWatch.value = false;
        }, 200);
      },
    },
  ],
});
searchBar.buttons = searchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});
searchBar.secondButtons = searchBar.secondButtons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});
provide("searchBar", searchBar);

const searchBar2 = reactive({
  inputs: [
    {
      type: "field",
      label: computed(() => t("KEYWORD") + " - " + t("ID") + " / " + t("NAME")),
      target: computed({
        get() {
          return search2.value.searchKeyword;
        },
        set(e) {
          search2.value.searchKeyword = e;
        },
      }),
      event: () => {
        getListEvent2();
      },
    },
    {
      type: "field",
      label: computed(() => t("KEYWORD") + " - " + t("CONTRACT_NUMBER") + " / " + t("BUILD_NAME")),
      target: computed({
        get() {
          return search2.value.searchKeyword2;
        },
        set(e) {
          search2.value.searchKeyword2 = e;
        },
      }),
      event: () => {
        getListEvent2();
      },
    },
    {
      cols: 2,
      type: "select",
      title: computed(() => t("ROLE")),
      items: computed(() => roleTypeItem.value),
      itemTitle: "value1",
      target: computed({
        get() {
          return search2.value.roleType;
        },
        set(e) {
          search2.value.roleType = e;
        },
      }),
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",
      title: computed(() => t("BTN_SEARCH")),

      event: () => {
        getListEvent2();
      },
    },
  ],
});
provide("searchBar2", searchBar2);

const getRoleType = async () => {
  const data = await useUiStore.getRoleType();
  search.value.roleType = data.defaultItem;
  search2.value.roleType = data.defaultItem;
  roleTypeItem.value = data.items;
};
const getAccountStatus = async () => {
  const data = await useUiStore.getAccountStatus();
  search.value.accountStatus = data.defaultAccountStatus;
  accountStatusItem.value = data.accountStatusItem;
};
const delYnItem = ref([]);
const getDelYnStatus = async () => {
  const data = await useUiStore.getDelYn();
  search.value.delYn = data.defaultDelYn;
  delYnItem.value = data.delYnItem;
  searchBar.inputs.push({
    cols: 2,
    type: "select",
    title: computed(() => t("ACCOUNT_STATUS")),

    items: computed(() => delYnItem.value),
    itemTitle: "value1",
    target: computed({
      get() {
        return search.value.delYn;
      },
      set(e) {
        search.value.delYn = e;
      },
    }),
  });
};

const getCurentContractList = async(targetUserId) => {
  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "/v2/contract/current/details",
      method: "post",
      queryparam: {
        userId : targetUserId,
      },
    });
  if (code == 200) {
    if (data.contractList?.length > 0) {
      fromContractTableData.value = data.contractList;
    }
  } else {
    toast.warning(t("ERROR_SEARCH", [t("CONTRACT")]));
  }
};

const setContractYn = () => {
  search.value.contractYn = {code:"ALL", name:"ALL"};
  searchBar.inputs.push({
    cols: 1,
    type: "select",
    title: computed(() => t("CONTRACT_YN")),
    items: computed(() => [{code:"ALL", name:"ALL"}, {code:"Y", name:"Y"}, {code:"N", name:"N"}]),
    itemTitle: "name",
    target: computed({
      get() {
        return search.value.contractYn;
      },
      set(e) {
        search.value.contractYn = e;
      },
    }),
  });
};

const exportBtnWatchNonContract = ref(false);
const exportVisible = ref(false);

const setNonContractCustomerExport = () => {
  searchBar.secondButtons.push({
      methodName: "BTN_EXPORT",
      icon: "mdi-export",
      title: computed(() => t("BTN_NON_CONTRACT_EXPORT")),
      event: async () => {
        let params = apiList["getNonContractList"];

        const { code, data, message, result, because } = await useConnectionStore.request(params);

        if (code == 200) {
          if (data.length > 0) {
            nonContractData.value = data;
          } else {
            toast.warning(t("NO_DATA_SEARCH", [t("BTN_NON_CONTRACT_EXPORT")]));
            return false;
          }
        } else {
          toast.error(t("ERROR_SEARCH", [t("BTN_NON_CONTRACT_EXPORT")]));
          return false;
        }

        if (nonContractData.value) {
          exportVisible.value = true;
          setTimeout(() => {
            exportBtnWatchNonContract.value = true;
          }, 100);
        }
        setTimeout(() => {
          exportBtnWatchNonContract.value = false;
          exportVisible.value = false;
        }, 200);
      },
    });
};

const setNonContractCustomerImport = () => {
  searchBar.secondButtons.push({
      methodName: "BTN_IMPORT",
      title: computed(() => t("BTN_NON_CONTRACT_IMPORT")),
      icon: "mdi-import",
      event: async () => {
        importDetailModal2.value = true;
      },
    });
};

const copyContractFromTo = () => {
  searchBar.secondButtons.push({
      methodName: "BTN_COPY",
      title: computed(() => t("BTN_COPY_CONTRACT")),
      icon: "mdi-content-copy",
      disabled: computed(() =>
        rowTarget.value.length != 1 ||
        rowTarget?.value[0].compsCntrNo == null
      ),
      event: async () => {
        copyFromSelectedUserInfo.value = rowTarget?.value[0];
        copyContractDetailModal.value = true;
        getCurentContractList(copyFromSelectedUserInfo.value.userId);
      },
    });
};

const getListEvent = async () => {
  let params = apiList["get"];
  params.queryparam = {
    searchKeyword: search.value.searchKeyword,
    searchKeyword2: search.value.searchKeyword2,
    targetRoleType: search.value.roleType.code
      ? search.value.roleType.code
      : search.value.roleType,
    accountStatus: search.value.accountStatus.code
      ? search.value.accountStatus.code
      : search.value.accountStatus,
    roleType: useUserStore.userInfo.roleType,
    delYn: search.value.delYn.code
      ? search.value.delYn.code
      : search.value.delYn,
    contractYn: search.value.contractYn.code
      ? search.value.contractYn.code
      : search.value.contractYn,
  };

  const { because, code, data, message, result } =
    await useConnectionStore.request(params);

  if (code == 200) {
    if (data.length > 0) {
      if (!!accountStatusItem.value == true) {
        data.filter((x) => {
          if (x.phonenumber) {
            x.phonenumber = x.phonenumber.replace(
              /^(.{3})(.*)(.{4})$/,
              "$1-$2-$3"
            );
          }
          x.convertDelYn = x.delYn.toLowerCase() == "y" ? "삭제" : "미삭제";
          x.convertRoleType =
            x.roleType.toUpperCase() === "SYSTEM"
              ? t("ROLE_SYSTEM")
              : x.roleType.toUpperCase() === "ACCOUNT"
              ? t("ROLE_ACCOUNT")
              : x.roleType.toUpperCase() === "ENGINEER"
              ? t("ROLE_ENGINEER")
              : x.roleType.toUpperCase() === "MAJOR"
              ? t("ROLE_MAJOR")
              : x.roleType.toUpperCase() === "MANAGER"
              ? t("ROLE_MANAGER")
              : x.roleType.toUpperCase() === "USER"
              ? t("ROLE_USER")
              : "-";
          accountStatusItem.value.some((item) => {
            if (x.accountStatus === item.code) {
              x.accountStatusString = item.value1;
            }
          });
        });
      } else {
        data.filter((x) => {
          x.accountStatusString = x.accountStatus;
        });
      }
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("USER")]));
    }
    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("USER")]));
  }
  rowTarget.value = [];
};

const getListEvent2 = async () => {
  let params = apiList["get"];
  params.queryparam = {
    searchKeyword: search2.value.searchKeyword,
    searchKeyword2: search2.value.searchKeyword2,
    targetRoleType: search2.value.roleType.code
      ? search2.value.roleType.code
      : search2.value.roleType,
    accountStatus: 'ALL',
    roleType: useUserStore.userInfo.roleType,
    delYn: 'ALL',
    contractYn: 'ALL',
    selectedUserId : copyFromSelectedUserInfo.value.userId,
  };

  const { because, code, data, message, result } =
    await useConnectionStore.request(params);

  if (code == 200) {
    if (data.length > 0) {
      if (!!accountStatusItem.value == true) {
        data.filter((x) => {
          if (x.phonenumber) {
            x.phonenumber = x.phonenumber.replace(
              /^(.{3})(.*)(.{4})$/,
              "$1-$2-$3"
            );
          }
          x.convertRoleType =
            x.roleType.toUpperCase() === "SYSTEM"
              ? t("ROLE_SYSTEM")
              : x.roleType.toUpperCase() === "ACCOUNT"
              ? t("ROLE_ACCOUNT")
              : x.roleType.toUpperCase() === "ENGINEER"
              ? t("ROLE_ENGINEER")
              : x.roleType.toUpperCase() === "MAJOR"
              ? t("ROLE_MAJOR")
              : x.roleType.toUpperCase() === "MANAGER"
              ? t("ROLE_MANAGER")
              : x.roleType.toUpperCase() === "USER"
              ? t("ROLE_USER")
              : "-";
          accountStatusItem.value.some((item) => {
            if (x.accountStatus === item.code) {
              x.accountStatusString = item.value1;
            }
          });
        });
      } else {
        data.filter((x) => {
          x.accountStatusString = x.accountStatus;
        });
      }
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("USER")]));
    }
    toUserTableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("USER")]));
  }
};
const searchContractValue = ref("");
const contractData = ref([]);

const searchContractEvent = async () => {
  let params = apiList["contractSearch"];
  params.queryparam = {
    searchId: searchContractValue.value.replace(/-/g, ""),
  };
  const { code, data, message, result, because } =
    await useConnectionStore.request(params);
  if (code == 200) {
    if (data.length > 0) {
      contractData.value = data;
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("CONTRACT")]));
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("CONTRACT")]));
  }
};
const callAPIs = async (method) => {
  const request = async (
    params,
    modal,
    contractEvent,
    successText,
    errorText
  ) => {
    const { because, code, data, message, result } =
      await useConnectionStore.request(params);
    if (code == 200) {
      toast.success(successText);
      await getListEvent();
      if (modal) {
        modal.value = false;
      }
      if (contractEvent) {
        getContractData();
      }
    } else {
      toast.error(errorText);
    }
  };
  const triggerActions = {
    majorJoin: () => {
      let params = apiList["majorJoin"];
      params.queryparam = { ...userJoinData };

      request(
        params,
        joinUserModal,
        false,
        t("SUCCESS_ADD", [t("ROLE_MAJOR")]),
        t("ERROR_ADD", [t("ROLE_MAJOR")])
      );
    },
    managerJoin: async () => {
      let params = apiList["majorJoin"];
      params.queryparam = { ...userJoinData };
      params.queryparam.note = "manager";

      request(
        params,
        joinUserModal,
        false,
        t("SUCCESS_ADD", [t("ROLE_MANAGER")]),
        t("ERROR_ADD", [t("ROLE_MANAGER")])
      );
    },
    userJoin: () => {
      let params = apiList["userJoin"];
      params.queryparam = {
        phonenumber: userJoinData.phonenumber.replace(/\D+/g, ""),
        userId: userJoinData.userId,
        userName: userJoinData.userName,
        iuserId: useUserStore.userInfo.userId,
        lobby: [],
      };
      request(
        params,
        joinUserModal,
        false,
        t("SUCCESS_ADD", [t("ROLE_USER")]),
        t("ERROR_ADD", [t("ROLE_USER")])
      );
    },
    accountJoin: async () => {
      let params;
      if ("ACCOUNT" == userJoinData.roleType.toUpperCase()) {
        params = apiList["accountJoin"];
        params.queryparam = {
          phonenumber: userJoinData.phonenumber.replace(/\D+/g, ""),

          userId: userJoinData.userId,
          userName: userJoinData.userName,
          empId: userJoinData.empId,
          lobby: [],
        };
      } else {
        params = apiList["engineerJoin"];
        params.queryparam = {
          userId: userJoinData.userId,
          empId: userJoinData.empId,
          phonenumber: userJoinData.phonenumber.replace(/\D+/g, ""),

          userName: userJoinData.userName,
          lobby: [],
        };
      }
      await request(
        params,
        joinUserModal,
        false,
        t("SUCCESS_ADD", [t("ROLE_ACCOUNT")]),
        t("ERROR_ADD", [t("ROLE_ACCOUNT")])
      );
    },
    update: () => {
      let params = apiList["update"];
      params.queryparam = {
        userId: userData.value.userId,
        userName: userData.value.userName,
        // eslint-disable-next-line
        phonenumber: userData.value.phonenumber.replace(/\-/g, ""),
      };
      request(
        params,
        updateUserModal,
        false,
        t("SUCCESS_UPDATE", [t("USER")]),
        t("ERROR_UPDATE", [t("USER")])
      );
    },
    remove: () => {
      let params = apiList["remove"];
      let removeObj = {
        userIds: [],
      };
      rowTarget.value.forEach((item) => {
        removeObj.userIds.push(item.userId);
      });

      if (
        removeObj.userIds.every((item) => useUserStore.userInfo.userId !== item)
      ) {
        params.queryparam = removeObj;
        request(
          params,
          removeUserModal,
          false,
          t("SUCCESS_REMOVE", [t("USER")]),
          t("ERROR_REMOVE", [t("USER")])
        );
      } else {
        toast.warning(t("WARNING_LOGIN_SAME_REMOVE_ID"));
      }
    },
    recovery: () => {
      let params = apiList["recovery"];
      let obj = {
        userIds: [],
      };
      rowTarget.value.forEach((item) => {
        obj.userIds.push(item.userId);
      });

      if (obj.userIds.every((item) => useUserStore.userInfo.userId !== item)) {
        params.queryparam = obj;
        request(
          params,
          recoveryAccountModal,
          false,
          t("SUCCESS_USER_RECOVERY"),
          t("ERROR_USER_RECOVERY")
        );
      } else {
        toast.warning(t("WARNING_LOGIN_SAME_REMOVE_ID"));
      }
    },
    resetPassword: () => {
      let params = apiList["resetPassword"];
      params.queryparam = {
        userId: userData.value.userId,
      };
      request(
        params,
        resetPasswordModal,
        false,
        t("SUCCESS_RESET_PASSWORD", [t("USER")]),
        t("ERROR_RESET_PASSWORD", [t("USER")])
      );
    },
    approval: () => {
      let params = apiList["userRejectingApproval"];
      let mapObj = {
        userLocale: "",
        targetList: [],
      };
      rowTarget.value.forEach((item) => {
        if (item.accountStatus === "0010") {
          mapObj.targetList.push({
            userId: item.userId,
            accountStatus: "0040",
          });
        } else {
          toast.warning(t("WARNING_APPROVAL"));
        }
      });

      params.queryparam = mapObj;
      if (params.queryparam.targetList.length > 0) {
        request(
          params,
          approvalUserModal,
          false,
          t("SUCCESS_APPROVAL"),
          t("ERROR_APPROVAL")
        );
      }
    },
    reject: () => {
      let params = apiList["userRejectingApproval"];
      let mapObj = {
        userLocale: "",
        targetList: [],
      };
      rowTarget.value.forEach((item) => {
        if (item.accountStatus === "0010") {
          mapObj.targetList.push({
            userId: item.userId,
            accountStatus: "4000",
          });
        } else {
          toast.error(t("WARNING_APPROVAL"));
        }
      });
      params.queryparam = mapObj;
      if (params.queryparam.targetList.length > 0) {
        request(
          params,
          rejectUserModal,
          false,
          t("SUCCESS_REJECT"),
          t("ERROR_REJECT")
        );
      }
    },
    addNewContract: async () => {
      let params = apiList["addNewContract"];
      params.queryparam = {
        userId: userData.value.userId,
        lobby: userData.value.lobby,
      };
      await request(
        params,
        addNewContractModal,
        true,
        t("SUCCESS_ADD_NEW_CONTRACT"),
        t("ERROR_ADD_NEW_CONTRACT")
      );
    },
    removeContract: async () => {
      const portfolioContractMappingIds = [];

      selectedContract.value.forEach((contractItem) => {
        portfolioContractMappingIds.push(
          contractList.value[contractItem].portfolioContractMappingId
        );
      });
      const params = apiList["removeContract"];
      params.queryparam = {
        portfolioContractMappingIds,
      };
      request(
        params,
        openRemoveContract,
        true,
        t("SUCCESS_REMOVE_CONTRACT"),
        t("ERROR_REMOVE_CONTRACT")
      );
    },
    importAccount: async () => {
      let successIdx = 0;
      let failIdx = 0;

      const importAPI = async (params) => {
        const { code } = await useConnectionStore.request(params);
        if (code == 200) {
          successIdx++;
        } else {
          failIdx++;
        }
        if (importData.length == successIdx + failIdx) {
          toast.info(
            t("INFO_USER_IMPORT", [importData.length, successIdx, failIdx]),
            { timeout: false }
          );

          await getListEvent();
          checkDupliImportIDModal.value = false;
          importDetailModal.value = false;
        }
      };
      const importData = importTableData.value.filter((item) => {
        return item.registYn.toUpperCase() == "Y";
      });
      if (useUserStore.userInfo?.roleType.toUpperCase() === "SYSTEM") {
        importData.forEach((item) => {
          if (item.registYn.toUpperCase() == "Y") {
            let obj = {
              userId: item.userId,
              empId: `${item.empId}`,
              userName: item.userName,
              phonenumber: item.phonenumber.replace(/\D+/g, ""),

              lobby: [],
            };

            if ("ACCOUNT" == userJoinData.roleType.toUpperCase()) {
              let params = apiList["accountJoin"];
              params.queryparam = obj;
              importAPI(params);
            } else {
              let params = apiList["engineerJoin"];

              params.queryparam = obj;

              importAPI(params);
            }
          }
        });
      }else if(useUserStore.userInfo?.roleType.toUpperCase() === "ACCOUNT"){
        let queryparams = [];
        importData.forEach(async (item) => {
          if (item.registYn.toUpperCase() == "Y") {
            queryparams.push({ 
                        iuserId: useUserStore.userInfo.userId,
                        userId: item.userId,
                        userName: item.userName,
                        phonenumber: item.phonenumber.replace(/\D+/g, ""),
                        lobby: item.lobby,
                        note : "manager",
                      });
        }});
        let params = apiList["manyJoin"];
        params.timeout = 120000;
        params.queryparam.createUserList = queryparams;

        const { code, data } = await useConnectionStore.request(params);
        if (code == 200) {
          toast.info(
            t("INFO_USER_IMPORT", [importData.length, data.successCnt, data.failCnt]),
            { timeout: false }
          );

          await getListEvent();
          checkDupliImportIDModal.value = false;
          importDetailModal.value = false;
        } else {
          toast.info(t("ERROR_ADD", [t("USER")]));
        }
      } else {
        importData.forEach((item) => {
          if (item.registYn.toUpperCase() == "Y") {
            let obj = {
              iuserId: useUserStore.userInfo.userId,
              userId: item.userId,
              phonenumber: item.phonenumber.replace(/\D+/g, ""),

              userName: item.userName,
              lobby: [],
            };
            let params = apiList["userJoin"];
            params.queryparam = obj;
            importAPI(params);
          }
        });
      }
    },
    importUser: async () => {},
  };
  if (method === "majorJoin" || method === "managerJoin" || method === "userJoin" || method === "update") {
    await form?.value?.validate();
    if (valid.value) triggerActions[method]();
  } else {
    triggerActions[method]();
  }
};

onMounted(async () => {
  await getRoleType();
  await getAccountStatus();
  if (computedRoleType.value == "SYSTEM") {
    await getDelYnStatus();
    setContractYn();
    setNonContractCustomerExport();
    setNonContractCustomerImport();
    copyContractFromTo();
  }
  if (computedRoleType.value) {
    await getListEvent();
  }

  selectedUserType.value = {code : 'MAJOR',name : t('ROLE_MAJOR')};
});
watch(computedRoleType, async (newVal) => {
  if (newVal && computedRoleType.value.toUpperCase() == "SYSTEM") {
    await getDelYnStatus();
    setContractYn();
    setNonContractCustomerExport();
    setNonContractCustomerImport();
    copyContractFromTo();
    await getListEvent();
  } else if (newVal && computedRoleType.value.toUpperCase() != "SYSTEM") {
    await getListEvent();
  }
});
watch(copyContractDetailModal, (newVal, oldVal) => {
  if(!newVal)
  {
    toUserTableData.value = [];
    toUserRowTarget.value = [];

    search2.value.searchKeyword = "";
    search2.value.searchKeyword2 = "";
  }
});
const openCreateModal = async () => {
  // userJoinData = {};
  idCheckReady.value = false;
  if (
    useUserStore.userInfo?.roleType?.toUpperCase() === "MANAGER" ||
    useUserStore.userInfo?.roleType?.toUpperCase() === "MAJOR"
  ) {
    Object.assign(userJoinData, managerTemp);
  } else if (
    useUserStore.userInfo.roleType.toUpperCase() === "ACCOUNT"
    // ||
    // useUserStore.userInfo.roleType.toUpperCase() === "ENGINEER"
  ) {
    contractData.value = [];
    Object.assign(userJoinData, majorTemp);
    searchContractValue.value = "";
    userJoinData.lobby = [];
    selectedUserType.value = {code : 'MAJOR',name : t('ROLE_MAJOR')};
  } else if (useUserStore.userInfo.roleType.toUpperCase() === "SYSTEM") {
    Object.assign(userJoinData, accountTemp);
    userJoinData.lobby = [];
  }
};
const checkUserId = async () => {
  const { code, data, message, result, because } =
    await useConnectionStore.request({
      url: "/v2/user/manage-already",
      method: "post",
      queryparam: { list: [{ userId: userJoinData.userId }] },
    });
  if (code == 200) {
    checkAlreadyId.value = data[0].result;

    if (data[0].result == false) {
      toast.success(t("SUCCESS_ALREADY_ID_GO"));
      sucessCheckId.value = true;
    }
  } else {
    toast.error(t("ERROR_ALREADY_ID"));
  }
};

const exportBtnWatch = ref(false);

const saveLobbyData = (val) => {
  if (userJoinData.lobby.length > 0) {
    if (
      userJoinData.lobby.some((item) => item.intgPrjNo === val.intgPrjNo) ==
      false
    ) {
      userJoinData.lobby.push(val);
    }
  } else {
    userJoinData.lobby.push(val);
  }
};
const saveNewContractLobbyData = (val) => {
  if (userData.value.lobby && userData.value.lobby.length > 0) {
    if (
      userData.value.lobby.some((item) => item.intgPrjNo === val.intgPrjNo) ==
      false
    ) {
      userData.value.lobby.push(val);
    }
  } else {
    userData.value.lobby = [];
    userData.value.lobby.push(val);
  }
};
// const
const onFileSelected = (event, type) => {
  const file = event.target.files[0];
  const reader = new FileReader();
  reader.onload = (event) => {
    let intRowCnt = 0;
    const data = new Uint8Array(event.target.result);
    const workbook = XLSX.read(data, { type: "array" });
    const sheet = workbook.Sheets[workbook.SheetNames[0]];
    const sheetData = XLSX.utils.sheet_to_json(sheet);
    const converSheetData = sheetData.map((row) => {
      intRowCnt++;
      if (row["연락처"] == "null") {
        row["연락처"] = "";
      }
      if (useUserStore.userInfo.roleType.toUpperCase() == "SYSTEM") {
        if(type == "nonContract")
        {
          return {
            userId: row["아이디"].trim(),
            userName: row["이름"],
            role: row["권한"],
            compsCntrNo: row["유상 계약 번호"],
            rowCnt : intRowCnt.toString(),
          };
        }
        else{
          return {
            userId: row["아이디"].trim(),
            userName: row["이름"],
            empId: row["사번"],
            phonenumber: row["연락처"],
            rowCnt : intRowCnt.toString(),
          };
        }
      }
      else if(useUserStore.userInfo.roleType.toUpperCase() == "ACCOUNT"){
        return {
          userId: row["아이디"].trim(),
          userName: row["이름"],
          phonenumber: row["연락처"],
          compsCntrNo: row["유상 계약 번호"].trim(),
          rowCnt : intRowCnt.toString(),
        };
      } else {
        return {
          userId: row["아이디"].trim(),
          userName: row["이름"],
          phonenumber: row["연락처"],
          rowCnt : intRowCnt.toString(),
        };
      }
    });
    importTableData.value = converSheetData;
  };
  reader.readAsArrayBuffer(file);
};

const checkUserValidate = async () => {
  const formatPhoneNumber = (phone) => {
    const checkNumber = phone.substr(0, 2);
    if (checkNumber[0] !== "0") {
      return ("0" + phone)
        .replace(/\D+/g, "")
        .replace(/^(.{3})(.*)(.{4})$/, "$1-$2-$3");
    } else {
      return phone
        .replace(/\D+/g, "")
        .replace(/^(.{3})(.*)(.{4})$/, "$1-$2-$3");
    }
  };

  const userIds = [];
  importTableData.value.forEach((item) => {
      userIds.push({
        userId:item.userId,
        compsCntrNo: useUserStore.userInfo.roleType.toUpperCase() == "ACCOUNT" ? item.compsCntrNo : null,
        rowCnt : item.rowCnt,
      });
  });
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/auth/account/validate",
      method: "post",
      queryparam: {
        userIds,
      },
    });
  if (code == 200) {
    importTableData.value = importTableData.value
      .map((item) => {
        const matchedItem = data.find((item2) => item.userId === item2.userId && item.rowCnt === item2.rowCnt);
        if (matchedItem) {
          item.registYn = matchedItem.registYn;
          item.sortIdx = matchedItem.registYn.toUpperCase() == "Y" ? 2 : 0;

          item.registType = matchedItem.registYn.toUpperCase() == "Y" ? "가능" : "불가능";
        } else {
          item.registYn = "Y"; // 기본값 설정
          item.sortIdx = 2;

          item.registType = "가능";
        }

        if (
          !!item.phonenumber == true &&
          item.phonenumber != null &&
          item.phonenumber != "null" &&
          item.phonenumber != undefined
        ) {
          item.phonenumber = formatPhoneNumber(item.phonenumber.toString());
        } else {
          item.registYn = "N";
          item.sortIdx = 0;

          item.registType = "불가능";
        }
        
        if (useUserStore.userInfo.roleType.toUpperCase() == "SYSTEM") {
          if (!!item.empId == false) {
            item.registYn = "N";
            item.sortIdx = 0;

            item.registType = "불가능";
          }
        }

        if(useUserStore.userInfo.roleType.toUpperCase() == "ACCOUNT"){
          if (!!item.compsCntrNo == false) {
            item.registYn = "N";
            item.sortIdx = 0;

            item.registType = "불가능";
          }
          else if(matchedItem?.contractList.length <= 0){
            item.registYn = "N";
            item.sortIdx = 0;

            item.registType = "불가능";
          }

          item.lobby = matchedItem?.contractList;
        }
        return item;
      })
      .sort((a, b) => a.sortIdx - b.sortIdx);
      exportCheckTableButton.value = true;
  } else {
    toast.error(t("ERROR_ALREADY_ID"));
  }
};
watch([importDetailModal, addNewContractModal], (newVal, oldVal) => {
  if (
    importDetailModal.value == false &&
    newVal[0] !== oldVal[0] &&
    newVal[0] == false
  ) {
    importTableData.value = [];
    file.value = [];

    exportCheckTableButton.value = false;
  }
  if (
    addNewContractModal.value == false &&
    newVal[1] !== oldVal[1] &&
    newVal[1] == false
  ) {
    userData.value.lobby = [];
    contractData.value = [];
    searchContractValue.value = "";
  }
});
const selectedContract = ref([]);
const contractList = ref([]);
const getContractData = async () => {
  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "v2/contract/current/details",
      method: "post",
      queryparam: {
        userId: userData.value.userId,
      },
    });

  if (code == 200) {
    if (data.contractList.length > 0) {
      selectedContract.value = undefined;
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("CONTRACT")]));
    }
    contractList.value = data.contractList;
  } else {
    toast.warning(t("NO_DATA_SEARCH", [t("CONTRACT")]));

    toast.error(t("ERROR_SEARCH", [t("CONTRACT")]));
  }
};
const openRemoveContract = ref(false);
watch(joinUserModal, (newVal, oldVal) => {
  if (newVal == false && newVal != oldVal) checkAlreadyId.value = false;
});
const mailReSend = async(sendList) => {
  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "/v2/user/mail/resend",
      method: "post",
      queryparam: sendList,
    });

  if (code == 200) {
    toast.success(t("SUCCESS_EMAIL_SEND"))
  } else {
    toast.warning(t("WARNING_EMAIL_SEND_FAIL"));
  }
};
const exportCheckDataFnc = () => {
  exportCheckData.value = true;
  setTimeout(() => {
    exportCheckData.value = false;
  }, 200);
};

const checkUserValidate2 = async () => {
  const userIds = [];
  importTableData.value.forEach((item) => {
      userIds.push({
        userId:item.userId,
        compsCntrNo: item.compsCntrNo,
        rowCnt : item.rowCnt,
      });
  });
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/auth/account/validate/nonContract",
      method: "post",
      queryparam: {
        userIds,
      },
    });
  if (code == 200) {
    importTableData.value = importTableData.value
      .map((item) => {
        const matchedItem = data.find((item2) => item.userId === item2.userId && item.rowCnt === item2.rowCnt);
        if (matchedItem) {
          item.registYn = matchedItem.registYn;
          item.sortIdx = matchedItem.registYn.toUpperCase() == "Y" ? 2 : 0;

          item.registType = matchedItem.registYn.toUpperCase() == "Y" ? "가능" : "불가능";
        } else {
          item.registYn = "Y"; // 기본값 설정
          item.sortIdx = 2;

          item.registType = "가능";
        }
        
        item.lobby = matchedItem?.contractList;

        return item;
      })
      .sort((a, b) => a.sortIdx - b.sortIdx);
      exportCheckTableButton.value = true;
  } else {
    toast.error(t("ERROR_VALIDATE_CHECK"));
  }
};
const setNonContractUser = async () => {
  let queryparams = [];
  importTableData.value.forEach(async (item) => {
    if (item.registYn.toUpperCase() == "Y") {
      queryparams.push({ 
                  userId: item.userId,
                  lobby: item.lobby,
                });
  }});
  let params = apiList["setNonContractUser"];
  params.queryparam.setUserList = queryparams;
  const { code, data } = await useConnectionStore.request(params);
  if (code == 200) {
    toast.info(t("INFO_USER_CONTRACT_MAPPING", [parseInt(data.successCnt) + parseInt(data.failCnt), data.successCnt, data.failCnt]));

    await getListEvent();
    checkDupliImportIDModal2.value = false;
    importDetailModal2.value = false;
  } else {
    toast.info(t("ERROR_MAPPING", [t("USER"), t("MENU_CONTRACTINFO")]));
  }
};

const copyFromContractTo = async () => {
  let toUserList = [];
  toUserRowTarget.value.forEach((item) => {
    toUserList.push(item.userId);
  });
  let params = apiList["copyFromContractTo"];
  params.queryparam = {
    fromUserId: copyFromSelectedUserInfo.value.userId,
    toUserList : toUserList,
  };
  
  const { code, data } = await useConnectionStore.request(params);
  if (code == 200) {
    toast.info(t("INFO_COPY_CONTRACT"));

    await getListEvent();
    confirmCopyContractModal.value = false;
    copyContractDetailModal.value = false;
  } else {
    toast.info(t("ERROR_COPY_CONTRACT"));
  }
};
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true" name="searchBar"> </SearchBarContainer>
    </v-col>
    <v-col cols="12" class="pt-0">
      <Skeleton :loadingData="tableData.length == 0" :height="tableHeight">
        <Tabulator
          :columns="columns"
          :tableData="tableData"
          :pagination="true"
          :height="tableHeight"
          :export="exportBtnWatch"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
        <Tabulator
          :columns="exportColumns"
          :tableData="nonContractData"
          :height="tableHeight"
          :export="exportBtnWatchNonContract"
          :exportFileName="'계약 누락 목록'"
          v-if="exportVisible"
        />
      </Skeleton>
    </v-col>
  </DefaultContainer>
  <ModalContainer
    v-if="
      useUserStore.userInfo?.roleType?.toUpperCase() === 'ACCOUNT'
      // ||
      //  useUserStore.userInfo?.roleType?.toUpperCase() === 'ENGINEER'
    "
    :text="t('JOIN_MAJOR')"
    @btnEvent="{
      if(selectedUserType.code == 'MAJOR')
      {
        callAPIs('majorJoin');
      }
      else if(selectedUserType.code == 'MANAGER')
      {
        callAPIs('managerJoin');
      }
      }"
    :width="'500px'"
    :disabled="
      valid && sucessCheckId === true && userJoinData?.lobby?.length > 0
        ? false
        : true
    "
    v-model="joinUserModal"
  >
    <v-form v-model="valid" ref="form" @submit.prevent="">
      <v-select
        :label="t('TYPE')"
        density="compact"
        variant="solo"
        return-object
        hide-details
        :items="[{
          code : 'MAJOR',
          name : t('ROLE_MAJOR')
        },
        {
          code : 'MANAGER',
          name : t('ROLE_MANAGER')
        }]"
        item-title="name"
        item-value="code"
        v-model="selectedUserType"
        :style="{marginBottom:'25px'}"
      ></v-select>
      <v-text-field
        :label="t('NAME')"
        v-model="userJoinData.userName"
        density="compact"
        variant="solo"
        :rules="[rules.required]"
        clearable
      >
      </v-text-field>
      <div class="d-flex">
        <v-text-field
          :label="t('ID')"
          density="compact"
          variant="solo"
          v-model="userJoinData.userId"
          :rules="[rules.required, rules.email]"
          clearable
          :error-messages="
            checkAlreadyId == true ? t('SUCCESS_ALREADY_ID_DUPLI') : ''
          "
        />
        <v-btn
          :disabled="!idCheckReady"
          class="ml-2 customBtn"
          @click="checkUserId"
          height="40"
          rounded="lg"
        >
          {{ t("CHECK_ALREADY") }}
        </v-btn>
      </div>
      <v-text-field
        v-model="userJoinData.phonenumber"
        clearable
        :label="t('HP')"
        maxLength="13"
        type="text"
        variant="solo"
        density="compact"
        :rules="[rules.required, rules.phoneJoin]"
      />
      <div class="d-flex">
        <v-text-field
          :label="t('BUSINESS_ELEVATOR_NUMBER')"
          v-model="searchContractValue"
          @keydown.enter.prevent="searchContractEvent()"
          density="compact"
          variant="solo"
          :rules="[rules.required]"
          clearable
        >
        </v-text-field>
        <v-btn
          class="ml-2 customBtn"
          height="40"
          prepend-icon="mdi-magnify"
          rounded="lg"
          :disabled="searchContractValue ? false : true"
          @click="searchContractEvent"
        >
          {{ t("CONTRACT_SEARCH") }}
        </v-btn>
      </div>
      <div>
        <Tabulator
          class="mt-1"
          :columns="contractColumns"
          :tableData="contractData"
          :pagination="true"
          :keepData="true"
          :height="'34.5vh'"
          @update:rowTarget="(returnVal) => saveLobbyData(returnVal)"
        />
        <v-sheet maxHeight="120px" class="mt-3 overflow-y-auto">
          <v-chip
            color="primary"
            class="mt-1"
            closable
            v-for="(item, i) in userJoinData.lobby"
            @click:close="userJoinData.lobby.splice(i, 1)"
            :key="item"
          >
            {{ item.siteNm ? item.siteNm : item.addr }}
          </v-chip>
        </v-sheet>
      </div>
    </v-form>
  </ModalContainer>
  <ModalContainer
    v-if="
      useUserStore.userInfo?.roleType?.toUpperCase() === 'MANAGER' ||
      useUserStore.userInfo?.roleType?.toUpperCase() === 'MAJOR'
    "
    :width="'500px'"
    :text="t('JOIN_USER')"
    :disabled="valid && sucessCheckId === true ? false : true"
    v-model="joinUserModal"
    @btnEvent="callAPIs('userJoin')"
  >
    <v-form v-model="valid" ref="form" @submit.prevent="">
      <v-sheet>
        <!-- <v-select
          label="내 계약번호"
          density="compact"
          variant="solo"
          :items="userJoinData.contractItems"
          v-model="userJoinData.selectContract"
        >
        </v-select> -->
        <div class="d-flex">
          <v-text-field
            :label="t('ID')"
            density="compact"
            variant="solo"
            v-model="userJoinData.userId"
            :rules="[rules.required, rules.email]"
            clearable
            :error-messages="
              checkAlreadyId == true ? t('SUCCESS_ALREADY_ID_DUPLI') : ''
            "
          />
          <v-btn
            :disabled="!idCheckReady"
            class="ml-2 customBtn"
            @click="checkUserId"
            height="40"
            rounded="lg"
          >
            {{ t("CHECK_ALREADY") }}
          </v-btn>
        </div>

        <div class="d-flex">
          <v-text-field
            :label="t('NAME')"
            v-model="userJoinData.userName"
            density="compact"
            variant="solo"
            :rules="[rules.required]"
            clearable
          >
          </v-text-field>
        </div>
        <v-text-field
          v-model="userJoinData.phonenumber"
          clearable
          :label="t('HP')"
          maxLength="13"
          type="text"
          variant="solo"
          density="compact"
          :rules="[rules.required, rules.phoneJoin]"
        />
      </v-sheet>
    </v-form>
  </ModalContainer>

  <ModalContainer
    :width="'500px'"
    v-if="useUserStore?.userInfo?.roleType?.toUpperCase() === 'SYSTEM'"
    :text="t('JOIN_ACCOUNT')"
    :disabled="valid && sucessCheckId === true ? false : true"
    @btnEvent="callAPIs('accountJoin')"
    v-model="joinUserModal"
  >
    <v-form v-model="valid" ref="form" @submit.prevent="">
      <v-sheet class="text-center">
        <div class="d-flex">
          <v-text-field
            :label="t('ID')"
            density="compact"
            variant="solo"
            v-model="userJoinData.userId"
            :rules="[rules.required, rules.email]"
            clearable
            :error-messages="
              checkAlreadyId == true ? t('SUCCESS_ALREADY_ID_DUPLI') : ''
            "
          />

          <v-btn
            :disabled="!idCheckReady"
            class="ml-2 customBtn"
            height="40"
            rounded="lg"
            @click="checkUserId"
          >
            {{ t("CHECK_ALREADY") }}
          </v-btn>
        </div>
        <v-text-field
          :label="t('NAME')"
          density="compact"
          variant="solo"
          v-model="userJoinData.userName"
          :rules="[rules.required]"
          clearable
        />
        <v-text-field
          :label="t('EMPLOYEE_NUMBER')"
          v-model="userJoinData.empId"
          density="compact"
          variant="solo"
          :rules="[rules.required]"
          clearable
        >
        </v-text-field>
        <v-text-field
          v-model="userJoinData.phonenumber"
          clearable
          :label="t('HP')"
          maxLength="13"
          type="text"
          variant="solo"
          density="compact"
          :rules="[rules.required, rules.phoneJoin]"
        />
        <v-select
          :label="t('ROLE')"
          variant="solo"
          :rules="[rules.required]"
          density="compact"
          v-model="userJoinData.roleType"
          :items="[
            {
              title: t('ACCOUNT'),
              value: 'account',
            },
            {
              title: t('ENGINEER'),
              value: 'engineer',
            },
          ]"
          item-title="title"
          item-value="value"
        >
        </v-select>
      </v-sheet>
    </v-form>
  </ModalContainer>

  <ModalContainer
    :text="t('UPDATE')"
    v-model="updateUserModal"
    @btnEvent="callAPIs('update')"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        variant="solo"
        disabled
        readonly
        class="pb-1"
        :label="t('ID')"
        density="compact"
        v-model="userData.userId"
        :rules="[rules.required]"
      />
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('NAME')"
        v-model="userData.userName"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        v-model="userData.phonenumber"
        clearable
        :label="t('HP')"
        maxLength="13"
        type="text"
        variant="solo"
        density="compact"
        :rules="[rules.required, rules.phone]"
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('REMOVE')"
    v-model="removeUserModal"
    @btnEvent="callAPIs('remove')"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
  <ModalContainer
    :text="t('BTN_RESET_PASSWORD')"
    v-model="resetPasswordModal"
    @btnEvent="callAPIs('resetPassword')"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        variant="solo"
        readonly
        density="compact"
        class="pb-1"
        :label="t('ID')"
        bg-color="#eee"
        v-model="userData.userId"
        :rules="[rules.required]"
      />
      <v-text-field
        variant="solo"
        readonly
        density="compact"
        class="pb-1"
        :label="t('HP')"
        bg-color="#eee"
        maxLength="13"
        type="text"
        v-model="userData.phonenumber"
        :rules="[rules.required, rules.phone]"
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('NEW_CONTRACT_ADD')"
    v-model="addNewContractModal"
    :width="'500px'"
    @btnEvent="callAPIs('addNewContract')"
    :disabled="
      (valid && sucessCheckId === true) || userData?.lobby?.length > 0
        ? false
        : true
    "
  >
    <v-form v-model="valid" ref="form" @submit.prevent="">
      <v-text-field
        :label="t('NAME')"
        v-model="userData.userName"
        density="compact"
        variant="solo"
        :rules="[rules.required]"
        clearable
        disabled
      >
      </v-text-field>
      <v-text-field
        :label="t('ID')"
        density="compact"
        variant="solo"
        v-model="userData.userId"
        :rules="[rules.required, rules.email]"
        clearable
        disabled
      />

      <div class="d-flex">
        <v-text-field
          :label="t('BUSINESS_ELEVATOR_NUMBER')"
          v-model="searchContractValue"
          density="compact"
          variant="solo"
          @keydown.enter.prevent="searchContractEvent()"
          :rules="[rules.required]"
          clearable
        >
        </v-text-field>
        <v-btn
          class="ml-2 customBtn"
          height="40"
          rounded="lg"
          prepend-icon="mdi-magnify"
          :disabled="searchContractValue ? false : true"
          @click="searchContractEvent"
        >
          {{ t("CONTRACT_SEARCH") }}
        </v-btn>
      </div>
      <div>
        <Tabulator
          class="mt-2"
          :columns="contractColumns"
          :tableData="contractData"
          :pagination="true"
          :keepData="true"
          :height="'40vh'"
          @update:rowTarget="(returnVal) => saveNewContractLobbyData(returnVal)"
        />
        <v-sheet maxHeight="120px" class="mt-3 overflow-y-auto">
          <v-chip
            color="primary"
            class="mt-2"
            closable
            v-for="(item, i) in userData.lobby"
            @click:close="userData.lobby.splice(i, 1)"
            :key="item"
          >
            {{ item.siteNm ? item.siteNm : item.addr }}
          </v-chip>
        </v-sheet>
      </div>
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('USER_APPROVAL')"
    v-model="approvalUserModal"
    @btnEvent="callAPIs('approval')"
  >
    <v-card-text>{{ t("CHECK_APPROVAL") }}</v-card-text>
  </ModalContainer>
  <ModalContainer
    :text="t('USER_REJECT')"
    v-model="rejectUserModal"
    @btnEvent="callAPIs('reject')"
  >
    <v-card-text>{{ t("CHECK_REJECT") }}</v-card-text>
  </ModalContainer>
  <ModalContainer
    :text="t('IMPORT')"
    v-model="importDetailModal"
    :width="'80vw'"
    v-if="useUserStore.userInfo?.roleType?.toUpperCase() === 'SYSTEM' || useUserStore.userInfo?.roleType?.toUpperCase() === 'ACCOUNT'"
    :disabled="
      importTableData?.some((item) => item.registYn?.toUpperCase() === 'Y') &&
      ((userJoinData.roleType && useUserStore.userInfo?.roleType?.toUpperCase() === 'SYSTEM') || useUserStore.userInfo?.roleType?.toUpperCase() === 'ACCOUNT')
        ? false
        : true
    "
    @btnEvent="checkDupliImportIDModal = true"
  >
      <div class="d-flex mb-4">
        <v-file-input
          :label="t('FILE')"
          color="primary"
          hide-details
          v-model="file"
          type="file"
          variant="solo"
          accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
          counter
          density="compact"
          @click="[(importTableData = []), (file = [])]"
          @change="onFileSelected($event)"
        >
        </v-file-input>

        <v-btn
          :disabled="importTableData.length == 0"
          class="ml-2 customBtn"
          rounded="lg"
          height="40"
          @click="checkUserValidate"
        >
          {{ t("CHECK_ALREADY") }}
        </v-btn>
        <v-btn
          :disabled="importTableData.length == 0 || !exportCheckTableButton"
          class="ml-2 customBtn"
          rounded="lg"
          height="40"
          @click="exportCheckDataFnc"
        >
          {{ t("BTN_EXPORT") }}
        </v-btn>
      </div>
      <v-select
        :label="t('ROLE')"
        density="compact"
        variant="solo"
        :items="[
          { title: t('ACCOUNT'), value: 'account' },
          { title: t('ENGINEER'), value: 'engineer' },
        ]"
        v-model="userJoinData.roleType"
        item-title="title"
        item-value="value"
        v-if="useUserStore.userInfo?.roleType?.toUpperCase() === 'SYSTEM'"
        hide-details
      >
      </v-select>
      <Tabulator
        class="my-4"
        :columns="importColumns"
        :selectable="false"
        :tableName="'table'"
        :injectTableData="injectTableRows"
        :tableData="importTableData"
        :height="'59vh'"
        :export="exportCheckData"
        @update:rowTarget="(returnVal) => (importTableRowTarget = returnVal)"
      />
  </ModalContainer>
  <ModalContainer
    :text="t('IMPORT')"
    v-model="importDetailModal"
    :width="'80vw'"
    v-if="
      useUserStore.userInfo?.roleType?.toUpperCase() === 'MANAGER' ||
      useUserStore.userInfo?.roleType?.toUpperCase() === 'MAJOR'
    "
    :disabled="
      importTableData?.some((item) => item.registYn?.toUpperCase() === 'Y')
        ? false
        : true
    "
    @btnEvent="checkDupliImportIDModal = true"
  >
    <div class="d-flex">
      <v-file-input
        :label="t('FILE')"
        color="primary"
        hide-details
        v-model="file"
        type="file"
        variant="solo"
        accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        counter
        density="compact"
        @click="[(importTableData = []), (file = [])]"
        @change="onFileSelected($event)"
      >
      </v-file-input>
      <v-btn
        :disabled="importTableData.length == 0"
        class="ml-2 customBtn"
        rounded="lg"
        height="40"
        @click="checkUserValidate"
      >
        {{ t("CHECK_ALREADY") }}
      </v-btn>
    </div>
    <!-- <v-select
      class="mt-2"
      :label="t('MY_CONTRACT_NUMBER')"
      density="compact"
      variant="solo"
      :items="userJoinData.contractItems"
      v-model="userJoinData.selectContract"
      hide-details
    >
    </v-select> -->
    <Tabulator
      class="my-4"
      :columns="importColumns"
      :selectable="false"
      :tableName="'table'"
      :injectTableData="injectTableRows"
      :tableData="importTableData"
      :height="'59vh'"
      @update:rowTarget="(returnVal) => (importTableRowTarget = returnVal)"
    />
  </ModalContainer>
  <ModalContainer
    @btnEvent="callAPIs('importAccount')"
    :text="
      useUserStore.userInfo?.roleType?.toUpperCase() === 'ACCOUNT' 
        ? t('ROLE_MANAGER_ADD') 
        : 'ACCOUNT' == userJoinData?.roleType?.toUpperCase()
        ? t('ROLE_ACCOUNT_ADD')
        : 'ENGINEER' == userJoinData?.roleType?.toUpperCase()
        ? t('ROLE_ENGINEER_ADD')
        : t('ROLE_USER_ADD')
    "
    v-model="checkDupliImportIDModal"
  >
    {{ t("CHECK_IMPORT_USER") }}
  </ModalContainer>
  <ModalContainer
    :text="t('BTN_NON_CONTRACT_IMPORT')"
    v-model="importDetailModal2"
    :width="'40vw'"
    v-if="useUserStore.userInfo?.roleType?.toUpperCase() === 'SYSTEM'"
    :disabled="
      importTableData?.some((item) => item.registYn?.toUpperCase() === 'Y') &&
      useUserStore.userInfo?.roleType?.toUpperCase() === 'SYSTEM'
        ? false
        : true
    "
    @btnEvent="checkDupliImportIDModal2 = true"
  >
      <div class="d-flex mb-4">
        <v-file-input
          :label="t('FILE')"
          color="primary"
          hide-details
          v-model="file"
          type="file"
          variant="solo"
          accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
          counter
          density="compact"
          @click="[(importTableData = []), (file = [])]"
          @change="onFileSelected($event, 'nonContract')"
        >
        </v-file-input>

        <v-btn
          :disabled="importTableData.length == 0"
          class="ml-2 customBtn"
          rounded="lg"
          height="40"
          @click="checkUserValidate2()"
        >
          {{ t("VALIDATE_CHECK") }}
        </v-btn>
        <v-btn
          :disabled="importTableData.length == 0 || !exportCheckTableButton"
          class="ml-2 customBtn"
          rounded="lg"
          height="40"
          @click="exportCheckDataFnc"
        >
          {{ t("BTN_EXPORT") }}
        </v-btn>
      </div>
      <Tabulator
        class="my-4"
        :columns="importColumns2"
        :selectable="false"
        :tableName="'table'"
        :tableData="importTableData"
        :height="'59vh'"
        :export="exportCheckData"
        :exportFileName="'계약누락복원_유효성확인'"
        @update:rowTarget="(returnVal) => (importTableRowTarget = returnVal)"
      />
  </ModalContainer>
  <ModalContainer
    @btnEvent="setNonContractUser"
    :text="t('BTN_NON_CONTRACT_IMPORT')"
    v-model="checkDupliImportIDModal2"
  >
    {{ t("IMPORT_VALIDATE_USER") }}
  </ModalContainer>
  <ModalContainer
    :text="t('BTN_COPY_CONTRACT')"
    v-model="copyContractDetailModal"
    :width="'50vw'"
    v-if="useUserStore.userInfo?.roleType?.toUpperCase() === 'SYSTEM' || useUserStore.userInfo?.roleType?.toUpperCase() === 'ACCOUNT'"
    :disabled="
      toUserRowTarget?.length >= 1 && copyFromSelectedUserInfo != null &&
      (useUserStore.userInfo?.roleType?.toUpperCase() === 'SYSTEM' || useUserStore.userInfo?.roleType?.toUpperCase() === 'ACCOUNT')
        ? false
        : true
    "
    @btnEvent="confirmCopyContractModal = true"
  >
        <v-toolbar density="compact" color="transparent">
            <v-toolbar-title class="text-button">
              {{ copyFromSelectedUserInfo.userName ?? "" }}
            </v-toolbar-title>
          </v-toolbar>
          <v-container fluid>
            <v-row>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("ID") }}
                </v-card-subtitle>
                {{ copyFromSelectedUserInfo.userId ?? "" }}
              </v-col>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("HP") }}
                </v-card-subtitle>
                {{ copyFromSelectedUserInfo.phonenumber ?? "" }}
              </v-col>
              <v-col cols="4" class="py-2">
                <v-card-subtitle class="pl-0 mb-1">
                  {{ t("ROLE") }}
                </v-card-subtitle>
                {{ copyFromSelectedUserInfo.convertRoleType ?? ""}}
              </v-col>
            </v-row>
          </v-container>
      <Tabulator
        class="my-4"
        :columns="fromContractColumns"
        :selectable="false"
        :tableName="'table'"
        :tableData="fromContractTableData"
        :height="'20vh'"
      />
      <div style="display:flex;justify-content: center; margin-bottom: 10px;">
        <v-icon>mdi-arrow-down-bold</v-icon><br/>
      </div>
      <SearchBarContainer :breadcrumb="true" name="searchBar2"> </SearchBarContainer>
      <Tabulator
        class="my-4"
        :columns="toUserColumns"
        :selectable="true"
        :tableName="'table'"
        :tableData="toUserTableData"
        :height="'30vh'"
        @update:rowTarget="(returnVal) => (toUserRowTarget = returnVal)"
      />
  </ModalContainer>
  <ModalContainer
    @btnEvent="copyFromContractTo"
    :text="t('BTN_COPY_CONTRACT')"
    v-model="confirmCopyContractModal"
  >
    {{ t("INFO_CLEAR_CONTACT") }}
  </ModalContainer>
  <v-dialog
    v-model="contractEditModal"
    fullscreen
    no-click-animation
    :scrim="false"
    persistent
    transition="dialog-bottom-transition"
  >
    <v-card>
      <v-toolbar dark color="transparent">
        <v-toolbar-title>{{ t("NEW_CONTRACT_SETTING") }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon dark @click="contractEditModal = false">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-toolbar>
      <v-divider></v-divider>
      <v-container>
        <v-row>
          <v-col cols="6">
            <v-card class="customCard gradientCard" height="calc(100vh - 97px)">
              <v-card-title>{{ userData.userName }}</v-card-title>
              <v-card-subtitle>{{ userData.userId }}</v-card-subtitle>
              <v-list>
                <v-list-item
                  class="py-2"
                  :title="t('ROLE')"
                  :subtitle="userData.convertRoleType"
                >
                </v-list-item>
                <v-list-item
                  class="py-2"
                  :title="t('HP')"
                  :subtitle="userData.phonenumber ?? '010-0000-0000'"
                >
                </v-list-item>

                <v-list-item
                  class="py-2"
                  :title="t('ADDRESS')"
                  :subtitle="userData.address ?? '-'"
                >
                </v-list-item>

                <v-list-item
                  class="py-2"
                  :title="t('STATUS')"
                  :subtitle="userData.accountStatusString"
                >
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>
          <v-col cols="6">
            <v-toolbar density="compact" color="transparent">
              <v-toolbar-title
                >{{ t("CONTRACT_DETAIL") }}
                {{ `(${contractList?.length ?? 0})` }}</v-toolbar-title
              >
              <v-spacer></v-spacer>
              <v-btn @click="[(addNewContractModal = true)]"
                ><v-icon>mdi-plus</v-icon></v-btn
              >
              <v-btn @click="openRemoveContract = true"
                ><v-icon>mdi-minus</v-icon></v-btn
              >
            </v-toolbar>

            <v-divider></v-divider>
            <v-item-group multiple v-model="selectedContract">
              <v-container
                fluid
                :style="{
                  maxHeight: 'calc(100vh - 146px)',
                }"
                class="overflow-y-auto"
              >
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
                              {{ item.custNm }}
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
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-dialog>

  <ModalContainer
    @btnEvent="callAPIs('removeContract')"
    :text="t('CONTRACT_REMOVE')"
    v-model="openRemoveContract"
  >
    {{ t("CHECK_LOBBY_CONTRACT_REMOVE", [selectedContract.length]) }}
  </ModalContainer>
  <ModalContainer
    @btnEvent="callAPIs('recovery')"
    :text="t('RECOVERY_ACCOUNT')"
    v-model="recoveryAccountModal"
  >
    {{ t("CHECK_USER_RECOVERY", [rowTarget.length]) }}
  </ModalContainer>
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
