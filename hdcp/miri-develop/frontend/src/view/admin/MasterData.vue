<script setup>
import { ref, reactive, computed, onMounted, watch, provide } from "vue";
import {
  DefaultContainer,
  SearchBarContainer,
  Tabulator,
  ModalContainer,
  Skeleton,
} from "@/component/Template";
import * as XLSX from "xlsx";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
import { useRoute } from "vue-router";
const toast = useToast();
const route = useRoute();
const { t } = useI18n();
const useUserStore = userStore();
const useUiStore = uiStore();
const createHeadModal = ref(false);
const updateHeadModal = ref(false);
const deleteHeadModal = ref(false);
const createDetailModal = ref(false);
const updateDetailModal = ref(false);
const deleteDetailModal = ref(false);
const importDetailModal = ref(false);
const height = 64 + 64 + 12 + 48;
const tableHeight = `calc(100vh - ${height}px)`;
const searchList = ref({
  searchKeyword: "",
  locale: "",
});
const localeItem = ref([]);
const masterDataApi = {
  getHead: {
    url: "/v2/masterdata/head/list",
    method: "get",
    queryparam: {},
  },
  createHead: {
    url: "/v2/masterdata/head/create",
    method: "post",
    queryparam: {},
  },
  updateHead: {
    url: "/v2/masterdata/head/update",
    method: "post",
    queryparam: {},
  },
  deleteHead: {
    url: "/v2/masterdata/head/delete",
    method: "post",
    queryparam: {},
  },
  getDetail: {
    url: "/v2/masterdata/detail/list",
    method: "get",
    queryparam: {},
  },
  createDetail: {
    url: "/v2/masterdata/detail/create",
    method: "post",
    queryparam: {},
  },
  createDetailImport: {
    url: "/v2/masterdata/detail/import",
    method: "post",
    queryparam: {},
  },
  updateDetail: {
    url: "/v2/masterdata/detail/update",
    method: "post",
    queryparam: {},
  },
  deleteDetail: {
    url: "/v2/masterdata/detail/delete",
    method: "post",
    queryparam: {},
  },
};
let headColumn = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      width: "70",
      headerSort: false,
      headerHozAlign: "center",
      hozAlign: "center",
    },
    {
      title: t("MASTER_KEY"),
      field: "masterdataKey",
      sorter: "string",
    },
    {
      title: t("MASTER_NAME"),
      field: "masterdataName",
      sorter: "string",
    },
  ];
});

const detailColumns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      hozAlign: "center",
      width: "70",
      headerHozAlign: "center",
      headerSort: false,
    },
    {
      title: t("MASTER_CODE"),
      field: "code",
      sorter: "string",
      minWidth: 120,
      titleDownload: "code",
    },
    {
      title: t("MASTER_VALUE1"),
      field: "value1",
      sorter: "string",
      minWidth: 120,
      titleDownload: "value1",
    },
    {
      title: t("MASTER_VALUE2"),
      field: "value2",
      sorter: "string",
      minWidth: 120,
      titleDownload: "value2",
    },
    {
      title: t("MASTER_VALUE3"),
      field: "value3",
      titleDownload: "value3",

      sorter: "string",
      minWidth: 120,
    },
    {
      title: t("MASTER_VALUE4"),
      field: "value4",
      sorter: "string",
      minWidth: 120,
      titleDownload: "value4",
    },
    {
      title: t("MASTER_VALUE5"),
      field: "value5",
      sorter: "string",
      minWidth: 120,
      hozAlign: "center",
      vertAlign: "middle",
      titleDownload: "value5",
    },
    {
      title: t("SORT"),
      field: "sortSeq",
      sorter: "number",
      minWidth: 120,
      titleDownload: "sortSeq",
    },
  ];
});
const useConnectionStore = connectionStore();
const rowTargetHeadData = ref([]);
const rowTargetDetailData = ref([]);
const userLocale = computed(() => useUserStore?.userInfo?.locale);
const exportBtnWatch = ref(false);
const importTableData = ref([]);
const importTableRowTarget = ref([]);
const headTableData = ref([]);
const detailTableData = ref([]);
const form = ref();
const valid = ref(false);

let headDataTemp = {
  masterdataKey: "",
  masterdataName: "",
  locale: "",
  note: "",
};
const headData = reactive({});

let detailDataTemp = {
  // masterdataId: 1,
  code: "",
  value1: "",
  value2: "",
  value3: "",
  value4: "",
  value5: "",
  sortSeq: 0,
};
const detailData = reactive({});

const sendRequest = async (params, i) => {
  return await useConnectionStore.request(params);
};

watch([rowTargetHeadData], (newVal, oldVal) => {
  if (!!rowTargetHeadData.value[0]?.masterdataId == true && newVal[0] !== oldVal[0]) {
    callAPIs("getDetail");
  }
});
const getLocaleValue = async () => {
  const data = await useUiStore.getLocale();
  searchList.value.locale = data.defaultLocale;
  localeItem.value = data.localeItem;
};
const openUpdateHeadModal = () => {
  Object.assign(headData, headDataTemp);

  headData.masterdataName = rowTargetHeadData?.value[0].masterdataName;
  headData.note = rowTargetHeadData?.value[0].note;

  updateHeadModal.value = true;
};

const callAPIs = async (method) => {
  const request = async (params, type, modal, successText, errorText) => {
    const { because, code, data, message, result } = await sendRequest(params);
    if (code == 200) {
      if (type === "detail") {
        triggerAction.getDetail();
      } else {
        triggerAction.getHead();
      }
      if (modal) {
        modal.value = false;
      }
      toast.success(successText);
    } else {
      toast.error(errorText);
    }
  };
  const triggerAction = {
    getHead: async () => {
      let params = masterDataApi["getHead"];
      params.queryparam = {
        searchKeyword: searchList.value?.searchKeyword
          ? searchList.value.searchKeyword
          : undefined,
        locale: searchList.value.locale.code
          ? searchList.value.locale?.code
          : searchList.value.locale,
      };
      const { because, code, data, message, result } = await sendRequest(params);
      if (code == 200) {
        if (data.length == 0) {
          toast.warning(t("NO_DATA_SEARCH"));
        }
        headTableData.value = data;
      } else {
        toast.error(t("ERROR_SEARCH"));
      }
      rowTargetHeadData.value = [];
      detailTableData.value = [];
    },
    createHead: () => {
      let params = masterDataApi["createHead"];
      params.queryparam = {
        masterDataKey: headData.masterDataKey,
        ...headData,
      };
      params.queryparam.locale = headData.locale.code
        ? headData.locale.code
        : headData.locale;
      request(
        params,
        "",
        createHeadModal,
        t("SUCCESS_ADD", [t("MASTER_LIST")]),
        t("ERROR_ADD", [t("MASTER_LIST")])
      );
    },
    updateHead: () => {
      delete headData["masterdataKey"];
      delete headData["locale"];
      let params = masterDataApi["updateHead"];
      params.queryparam = {
        masterdataId: rowTargetHeadData?.value[0].masterdataId,
        ...headData,
      };

      request(
        params,
        "",
        updateHeadModal,
        t("SUCCESS_UPDATE", [t("MASTER_LIST")]),
        t("ERROR_UPDATE", [t("MASTER_LIST")])
      );
    },
    deleteHead: () => {
      let params = masterDataApi["deleteHead"];

      let mapObj = {
        masterdataIds: [],
      };
      rowTargetHeadData.value.forEach((item) => {
        mapObj.masterdataIds.push(item.masterdataId);
      });
      params.queryparam = mapObj;
      request(
        params,
        "",
        deleteHeadModal,
        t("SUCCESS_REMOVE", [t("MASTER_LIST")]),
        t("ERROR_REMOVE", [t("MASTER_LIST")])
      );
    },
    getDetail: async () => {
      let params = masterDataApi["getDetail"];
      params.queryparam = {
        masterdataId: rowTargetHeadData.value[0].masterdataId,
      };
      const { because, code, data, message, result } = await sendRequest(params);
      if (code == 200) {
        detailTableData.value = data;
        if (data.length == 0) {
          toast.warning(t("NO_DATA_SEARCH", [t("MASTER_DETAIL")]));
        }
      } else {
        toast.error(t("ERROR_MESSAGE"));
      }
      rowTargetDetailData.value = [];
    },
    createDetail: () => {
      let params = masterDataApi["createDetail"];
      params.queryparam = {
        masterdataId: rowTargetHeadData.value[0].masterdataId,
        ...detailData,
      };
      request(
        params,
        "detail",
        createDetailModal,
        t("SUCCESS_ADD", [t("MASTER_DETAIL")]),
        t("ERROR_ADD", [t("MASTER_DETAIL")])
      );
    },
    updateDetail: () => {
      let params = masterDataApi["updateDetail"];
      params.queryparam = {
        masterdataDetailId: rowTargetDetailData?.value[0].masterdataDetailId,
        ...detailData,
      };
      request(
        params,
        "detail",
        updateDetailModal,
        t("SUCCESS_UPDATE", [t("MASTER_DETAIL")]),
        t("ERROR_UPDATE", [t("MASTER_DETAIL")])
      );
    },
    deleteDetail: () => {
      let params = masterDataApi["deleteDetail"];
      let mapObj = {
        masterdataDetailIds: [],
      };
      rowTargetDetailData.value.forEach((item) => {
        mapObj.masterdataDetailIds.push(item.masterdataDetailId);
      });

      params.queryparam = mapObj;
      request(
        params,
        "detail",
        deleteDetailModal,
        t("SUCCESS_REMOVE", [t("MASTER_DETAIL")]),
        t("ERROR_REMOVE", [t("MASTER_DETAIL")])
      );
    },
  };
  if (
    method === "createHead" ||
    method === "updateHead" ||
    method === "createDetail" ||
    method === "updateDetail"
  ) {
    await form?.value?.validate();
    if (valid.value) triggerAction[method]();
  } else {
    triggerAction[method]();
  }
};

const openUpdateDetailModal = () => {
  detailData.code = rowTargetDetailData?.value[0].code;
  detailData.value1 = rowTargetDetailData?.value[0].value1;
  detailData.value2 = rowTargetDetailData?.value[0].value2;
  detailData.value3 = rowTargetDetailData?.value[0].value3;
  detailData.value4 = rowTargetDetailData?.value[0].value4;
  detailData.value5 = rowTargetDetailData?.value[0].value5;
  detailData.sortSeq = rowTargetDetailData?.value[0].sortSeq;
  updateDetailModal.value = true;
};

const onFileSelected = (event) => {
  const file = event.target.files[0];
  const reader = new FileReader();
  reader.onload = (event) => {
    const data = new Uint8Array(event.target.result);
    const workbook = XLSX.read(data, { type: "array" });
    const sheet = workbook.Sheets[workbook.SheetNames[0]];
    const sheetData = XLSX.utils.sheet_to_json(sheet);
    importTableData.value = sheetData.map((row) => ({
      code: row["code"],
      value1: row["value1"],
      value2: row["value2"],
      value3: row["value3"],
      value4: row["value4"],
      value5: row["value5"],
      sortSeq: row["sortSeq"],
    }));
  };
  reader.readAsArrayBuffer(file);
};
const searchBar = reactive({
  inputs: [
    {
      type: "field",
      label: computed(() => t("KEYWORD")),
      target: computed({
        get() {
          return searchList.value.searchKeyword;
        },
        set(e) {
          searchList.value.searchKeyword = e;
        },
      }),
      event: () => {
        callAPIs("getHead");
      },
    },
    {
      cols: 3,
      title: computed(() => t("LOCALE")),
      type: "select",
      items: computed(() => (localeItem.value ? localeItem.value : ["ko_kr", "en_gn"])),
      itemTitle: "value1",
      target: computed({
        get() {
          return searchList.value.locale;
        },
        set(e) {
          searchList.value.locale = e;
        },
      }),
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",

      title: computed(() => t("BTN_SEARCH")),

      event: () => {
        callAPIs("getHead");
      },
    },
    {
      methodName: "BTN_CREATE",

      title: computed(() => t("BTN_CREATE")),

      event: () => {
        Object.assign(headData, headDataTemp);
        headData.locale = searchList.value.locale;
        createHeadModal.value = true;
      },
    },
    {
      methodName: "BTN_UPDATE",

      title: computed(() => t("BTN_UPDATE")),

      disabled: computed(
        () =>
          !(
            rowTargetHeadData.value[0]?.masterdataId &&
            rowTargetHeadData.value.length == 1
          )
      ),

      event: () => {
        openUpdateHeadModal();
      },
    },
    {
      methodName: "BTN_DELETE",

      title: computed(() => t("BTN_DELETE")),

      disabled: computed(() => !rowTargetHeadData.value.length >= 1),

      event: () => {
        deleteHeadModal.value = true;
      },
    },
  ],
  secondButtons: [
    {
      methodName: "BTN_CREATE",

      title: computed(() => t("BTN_CREATE")),

      disabled: computed(() => !rowTargetHeadData.value[0]?.masterdataId),

      event: () => {
        Object.assign(detailData, detailDataTemp);
        createDetailModal.value = true;
      },
    },
    {
      methodName: "BTN_UPDATE",

      title: computed(() => t("BTN_UPDATE")),

      disabled: computed(
        () =>
          !(
            rowTargetDetailData.value[0]?.masterdataDetailId &&
            rowTargetDetailData.value.length == 1
          )
      ),
      event: () => {
        openUpdateDetailModal();
      },
    },
    {
      methodName: "BTN_DELETE",

      title: computed(() => t("BTN_DELETE")),

      disabled: computed(() => !rowTargetDetailData.value.length >= 1),

      event: () => {
        deleteDetailModal.value = true;
      },
    },
    {
      methodName: "BTN_IMPORT",

      title: computed(() => t("BTN_IMPORT")),
      disabled: computed(() => !rowTargetHeadData.value[0]?.masterdataId),
      icon: "mdi-import",
      event: () => {
        importDetailModal.value = true;
      },
    },
    {
      methodName: "BTN_EXPORT",
      title: computed(() => t("BTN_EXPORT")),
      disabled: computed(() => !(detailTableData.value.length >= 1)),
      icon: "mdi-export",

      event: () => {
        if (detailTableData.value) {
          exportBtnWatch.value = true;
        }
        setTimeout(() => {
          exportBtnWatch.value = false;
        }, 100);
      },
    },
  ],
});

searchBar.buttons = searchBar.buttons.filter((button) => {
  return route.meta.methods?.some((item2) => button.methodName === item2.methodName);
});
searchBar.secondButtons = searchBar.secondButtons.filter((button) => {
  return route.meta.methods?.some((item2) => button.methodName === item2.methodName);
});
provide("searchBar", searchBar);
onMounted(async () => {
  await getLocaleValue();
  await callAPIs("getHead");
});

const importDetail = async () => {
  let idx = 0;
  let successIdx = 0;
  let failIdx = 0;
  const importAPI = async (params) => {
    const { code } = await useConnectionStore.request(params);
    if (code == 200) {
      toast.info(
        t("INFO_MASTER_DETAIL_IMPORT", [
          importTableData.value.length,
          successIdx,
          failIdx,
        ]),
        { timeout: false }
      );

      await callAPIs("getDetail");
      importDetailModal.value = false;
    } else {
      console.log('Import 실패');
    }
  };

  // importTableData.value.forEach(async (item) => {
    let params = masterDataApi["createDetailImport"];

    params.queryparam = {
      masterdataId: rowTargetHeadData.value[0].masterdataId,
      detailList: importTableData.value
    };
    importAPI(params);
  // });
};
</script>

<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="12" lg="4" class="pt-0">
      <Skeleton :loadingData="headTableData == 0" :height="tableHeight">
        <Tabulator
          :pagination="true"
          :columns="headColumn"
          :tableName="'table'"
          :tableData="headTableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTargetHeadData = returnVal)"
        />
      </Skeleton>
    </v-col>
    <v-col cols="12" lg="8" class="pt-0">
      <Skeleton :loadingData="detailTableData == 0" :height="tableHeight">
        <Tabulator
          :pagination="true"
          :columns="detailColumns"
          :tableName="'table'"
          :export="exportBtnWatch"
          :tableData="detailTableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTargetDetailData = returnVal)"
        />
      </Skeleton>
    </v-col>
  </DefaultContainer>

  <ModalContainer
    :text="t('MASTER_LIST_ADD')"
    v-model="createHeadModal"
    @btnEvent="callAPIs('createHead')"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('MASTER_KEY')"
        v-model="headData.masterdataKey"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('MASTER_NAME')"
        v-model="headData.masterdataName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('NOTE')"
        v-model="headData.note"
        clearable
      />
      <v-select
        class="pb-1"
        density="compact"
        return-object
        hide-details
        variant="solo"
        :items="localeItem ? localeItem : ['ko_kr', 'en_gn']"
        item-title="value1"
        v-model="headData.locale"
        :label="t('LOCALE')"
      >
      </v-select>
    </v-form>
  </ModalContainer>

  <ModalContainer
    :text="t('MASTER_LIST_UPDATE')"
    v-model="updateHeadModal"
    @btnEvent="callAPIs('updateHead')"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        variant="solo"
        disabled
        density="compact"
        :label="t('MASTER_KEY')"
        v-model="rowTargetHeadData[0].masterdataKey"
        class="pb-1"
        :rules="[useUiStore.rules.required]"
        readonly
      />
      <v-text-field
        variant="solo"
        density="compact"
        :label="t('MASTER_NAME')"
        v-model="headData.masterdataName"
        class="pb-1"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        :label="t('NOTE')"
        class="pb-1"
        v-model="headData.note"
        clearable
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    :text="t('MASTER_LIST_REMOVE')"
    v-model="deleteHeadModal"
    @btnEvent="callAPIs('deleteHead')"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
  <ModalContainer
    :text="t('MASTER_DETAIL_ADD')"
    v-model="createDetailModal"
    @btnEvent="callAPIs('createDetail')"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('MASTER_CODE')"
        v-model="detailData.code"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('MASTER_VALUE1')"
        v-model="detailData.value1"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('MASTER_VALUE2')"
        v-model="detailData.value2"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('MASTER_VALUE3')"
        v-model="detailData.value3"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('MASTER_VALUE4')"
        v-model="detailData.value4"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('MASTER_VALUE5')"
        v-model="detailData.value5"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('SORT')"
        v-model="detailData.sortSeq"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('MASTER_DETAIL_UPDATE')"
    v-model="updateDetailModal"
    @btnEvent="callAPIs('updateDetail')"
  >
    <v-form ref="form" v-model="valid" class="text-center" @submit.prevent="">
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('MASTER_CODE')"
        v-model="detailData.code"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('MASTER_VALUE1')"
        v-model="detailData.value1"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('MASTER_VALUE2')"
        v-model="detailData.value2"
        clearable
      />
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('MASTER_VALUE3')"
        v-model="detailData.value3"
        clearable
      />
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('MASTER_VALUE4')"
        v-model="detailData.value4"
        clearable
      />
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('MASTER_VALUE5')"
        v-model="detailData.value5"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('SORT')"
        v-model="detailData.sortSeq"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    @btnEvent="callAPIs('deleteDetail')"
    :text="t('MASTER_DETAIL_REMOVE')"
    v-model="deleteDetailModal"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
  <ModalContainer
    :width="'80vw'"
    :text="'가져오기'"
    v-model="importDetailModal"
    @btnEvent="importDetail()"
  >
    <v-file-input
      :label="t('FILE')"
      color="primary"
      clearable
      hide-details
      type="file"
      variant="solo"
      accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
      counter
      density="compact"
      @change="onFileSelected($event)"
    >
    </v-file-input>
    <Tabulator
      class="my-4"
      :columns="detailColumns"
      :tableName="'table'"
      :tableData="importTableData"
      :height="'59vh'"
      @update:rowTarget="(returnVal) => (importTableRowTarget = returnVal)"
    />
  </ModalContainer>
</template>
