<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  GridContainer,
  Button,
  DataField,
  Tabulator,
  ModalContainer,
  ButtonContainer,
  DefaultContainer,
  InputContainer,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { v4 as uuidv4 } from "uuid";
import { TheTreeComp } from "@daiahub/thetreecomp";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";

const height = 64 + 32 + 22;
const treeHeight = `calc(100vh - ${height + 46}px)`;
const tableHeight = 48 + 52;
const tableHeightCalc = `calc(50vh - ${tableHeight}px)`;
const useConnectionStore = connectionStore();
const toast = useToast();
const { t } = useI18n();
const apiList = {
  getViewMap: {
    url: "/v1/priv/listviewfuncmap",
    method: "get",
  },
  addView: {
    url: "/v1/priv/addview",
    method: "post",
    queryparam: {
      viewNm: "",
    },
  },
  updateView: {
    url: "/v1/priv/updateview",
    method: "post",
    queryparam: {
      viewId: "",
      viewNm: "",
    },
  },
  removeView: {
    url: "/v1/priv/removeview",
    method: "post",
    queryparam: {
      viewId: "",
      viewNm: "",
    },
  },

  addFuncToView: {
    url: "/v1/priv/addfunctoview",
    method: "post",
    queryparam: {
      viewId: "",
      funcIds: "",
    },
  },
  removeFuncFromView: {
    url: "/v1/priv/removefuncfromview",
    method: "post",
    queryparam: {
      viewId: "",
      funcIds: "",
    },
  },

  linkViewAndFunc: {
    url: "/v1/priv/linkviewandfunc",
    method: "post",
    queryparam: {
      viewId: "",
      vueFileUrl: "",
    },
  },
  unlinkViewAndFunc: {
    url: "/v1/priv/unlinkviewandfunc",
    method: "post",
    queryparam: {
      viewId: "",
    },
  },
  getFunc: {
    url: "/v1/priv/listfunc",
    method: "get",
    queryparam: {
      searchKeyword: "",
    },
  },
  addFunc: {
    url: "/v1/priv/addfunc",
    method: "post",
    queryparam: {
      funcNm: "",
      methodNm: "",
      note: "",
    },
  },
  updateFunc: {
    url: "/v1/priv/updatefunc",
    method: "post",
    queryparam: {
      funcId: "",
      funcNm: "",
      methodNm: "",
      note: "",
    },
  },
  removeFunc: {
    url: "/v1/priv/removefunc",
    method: "post",
    queryparam: {
      funcId: "",
    },
  },

  getVueFile: {
    url: "/v1/priv/listvuefile",
    method: "get",
    queryparam: {
      searchKeyword: "",
    },
  },
  addVueFile: {
    url: "/v1/priv/addvuefile",
    method: "post",
    queryparam: {
      vueFileId: "",
      vueNm: "",
      vueFileUrl: "",
      note: "",
    },
  },
  updateVueFile: {
    url: "/v1/priv/updatevuefile",
    method: "post",
    queryparam: {
      vueFileId: "",
      vueNm: "",
      vueFileUrl: "",
      note: "",
    },
  },
  removeVueFile: {
    url: "/v1/priv/removevuefile",
    method: "post",
    queryparam: {
      vueFileId: "",
    },
  },
};
const funcColumn = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      width: "70",
      headerSort: false,
      hozAlign: "center",
      headerHozAlign: "center",
    },
    {
      title: t("STRING_INDEX"),
      field: "FUNC_ID",
      sorter: "number",
      responsive: 0,
    },
    {
      title: t("STRING_FN_METHOD"),
      field: "METHOD_NM",
      sorter: "string",
    },
    {
      title: t("STRING_NAME"),
      field: "FUNC_NM",
      sorter: "string",
    },
    {
      title: t("STRING_NOTE"),
      field: "NOTE",
      sorter: "string",
      responsive: 2,
    },
  ];
});
const fileListColumn = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      width: "70",

      headerSort: false,
      hozAlign: "center",
      headerHozAlign: "center",
    },
    {
      title: t("STRING_INDEX"),
      field: "VUE_FILE_ID",
      sorter: "string",
    },
    {
      title: t("STRING_VUE_FILE_NAME"),
      field: "VUE_NM",
      sorter: "string",
    },
    {
      title: t("STRING_VUE_FILE_URL"),
      field: "VUE_FILE_URL",
      sorter: "string",
    },
    {
      title: t("STRING_NOTE"),
      field: "NOTE",
      sorter: "string",
    },
  ];
});
const addViewModal = uuidv4();
const updateViewModal = uuidv4();
const removeViewModal = uuidv4();
const addFuncModal = uuidv4();
const updateFuncModal = uuidv4();
const removeFuncModal = uuidv4();
const addVueFileModal = uuidv4();
const updateVueFileModal = uuidv4();
const removeVueFileModal = uuidv4();

const useUiStore = uiStore();
const treeviewData = ref([]);
const TreeCompTgt = ref();
const canEdit = ref(true);
const expandAll = ref(true);
const treeEditTarget = ref([]);
const funcTableData = ref([]);
const vueFilTableData = ref([]);
const treeKey = ref(0);
const funcSearch = ref({ searchKeyword: "" });
const vueFileSearch = ref({ searchKeyword: "" });
const viewTemp = {
  viewId: "",
  viewNm: "",
  type: "view",
};
const viewData = reactive({});
Object.assign(viewData, viewTemp);

const funcTemp = {
  funcId: "",
  funcNm: "",
  methodNm: "",
  note: "",
  type: "function",
};
const funcData = reactive({});
Object.assign(funcData, funcTemp);

const vueFileTemp = {
  vueFileId: "",
  vueNm: "",
  vueFileUrl: "",
  note: "",
  type: "file",
};
const vueFileData = reactive({});
Object.assign(vueFileData, vueFileTemp);

const functionTarget = ref([]);
const vueFileTarget = ref([]);
const rules = ref({
  required: (value) => {
    return !!value || "입력이 필요합니다.";
  },
});
const form = ref();
const valid = ref(false);

watch([treeEditTarget, form], (newVal, oldVal) => {
  console.log(treeEditTarget.value);
  if (newVal[0] !== oldVal[0]) {
    viewData.viewId = treeEditTarget.value.VIEW_ID;
    viewData.viewNm = treeEditTarget.value.VIEW_NM;
    useUiStore.openModalEvent(updateViewModal);
  }
});

const sendRequest = async (params, i) => {
  return await useConnectionStore.request(params);
};

const getMappingData = async () => {
  // const data = axios.get("/v1/priv/listviewfuncmap");

  const createDataTree = (dbData) => {
    let rtnVal = {};
    let treeData = [];
    dbData.forEach((data) => {
      rtnVal[data.TREE_ID] = {
        name: data.VIEW_NM ? data.VIEW_NM : data.FUNC_NM,
        tree_id: data.TREE_ID,
        p_tree_id: data.PARENT_ID,
        type: data.TYPE,
        id: uuidv4(),
        children: [],
        ...data,
      };
    });
    dbData.forEach((data) => {
      if (data.PARENT_ID == 0) {
        treeData.push(rtnVal[data.TREE_ID]);
      } else {
        rtnVal[data.PARENT_ID]?.children.push(rtnVal[data.TREE_ID]);
      }
    });
    return JSON.parse(JSON.stringify(treeData));
  };
  const { status, code, message, resultset } = await sendRequest(
    apiList["getViewMap"]
  );
  if (code == 200) {
    treeviewData.value = createDataTree(resultset);
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
};
const getFuncListEvent = async () => {
  let params = apiList["getFunc"];
  params.queryparam = {
    searchKeyword: funcSearch.value.searchKeyword,
  };

  const { status, code, message, resultset } = await sendRequest(params);
  if (code == 200) {
    funcTableData.value = resultset;
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
};
const getVueFileListEvent = async () => {
  let params = apiList["getVueFile"];
  params.queryparam = {
    searchKeyword: vueFileSearch.value.searchKeyword,
  };

  const { status, code, message, resultset } = await sendRequest(params);
  if (code == 200) {
    vueFilTableData.value = resultset;
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
};

const viewEvent = async (method) => {
  const response = async (params) => {
    const { status, code, message, resultset } = await sendRequest(params);
    if (code == 200) {
      await getMappingData();
      useUiStore.closeModal();
      toast.success(message);
    } else {
      toast.error(t("ERROR_MESSAGE"));
    }
  };
  if (method != "remove") {
    await form.value.validate();
  }

  if (valid.value) {
    if (method == "add") {
      let target = TreeCompTgt.value.GetSelectedTarget();
      if (!!target.value.VIEW_ID == false) {
        let params = apiList["addView"];
        params.queryparam = { ...viewData };
        await response(params);
      } else {
        toast.warning(t("WARNING_CHECK_VIEW_ADD"));
      }
    }
    if (method == "update") {
      let params = apiList["updateView"];
      params.queryparam = { ...viewData };
      await response(params);
      useUiStore.closeModal();
    }
  }

  if (method == "remove") {
    const source = TreeCompTgt.value.GetSelectedSource();

    let params = apiList["removeView"];
    if (source.length === 1) {
      console.log(source);
      if (source[0].VIEW_ID) {
        const removeArr = [];
        source.forEach((x) => {
          removeArr.push({
            viewId: x.VIEW_ID,
          });
        });
        params.queryparam = { ...removeArr[0] };
        console.log(params.queryparam);
        await response(params);
      } else {
        toast.warning(t("WARNING_CHECK_REMOVE_MAPPING"));
      }
      // 멀티선택 안하게 하기 위해서 0번으로 처리함.
    } else {
      toast.warning(t("WARNING_MAPPING_ONLY_ONE"));
    }
  }
};

const mappingEvent = async (method) => {
  const response = async (params, key) => {
    const { status, code, message, resultset } = await sendRequest(params);
    if (code == 200) {
      await getMappingData();
      treeKey.value++;
      toast.success(message);
    } else {
      toast.error(t("ERROR_MESSAGE"));
    }
  };

  if (method == "addFuncToView") {
    let params = apiList["addFuncToView"];
    let treeTarget = TreeCompTgt.value.GetSelectedTarget();
    let checkDupli = true;

    let addArr = [];

    if (treeTarget.value?.VIEW_ID && functionTarget.value.length > 0) {
      const checkValue = (target, item) => {
        let check = false;

        target.forEach((treeChildItem) => {
          if (treeChildItem.FUNC_ID == item.FUNC_ID) {
            check = true;
          }
        });
        return check;
      };

      let rows = functionTarget.value;
      rows.forEach((fnItem) => {
        checkDupli = checkValue(treeTarget.value.children, fnItem);
        if (checkDupli == true) {
          toast.warning(t("WARNING_MAPPING_DUPLI"));
        } else {
          addArr.push(fnItem.FUNC_ID);
        }
      });
      if (addArr.length > 0) {
        params.queryparam = {
          viewId: treeTarget.value.VIEW_ID,
          funcIds: addArr,
        };
        await response(params);
      }
    } else {
      toast.warning(t("WARNING_MAPPING_SELECT"));
    }
  }
  if (method == "removeFuncFromView") {
    let params = apiList["removeFuncFromView"];
    const removeSource = TreeCompTgt.value.GetSelectedSource();
    if (removeSource.length > 0) {
      let mapObj = {
        viewId: "",
        funcIds: [],
      };
      const arrForEach = (arr) => {
        arr.forEach((fnItem) => {
          mapObj.viewId = fnItem.stats.getParent(fnItem.id).VIEW_ID;
          mapObj.funcIds.push(fnItem.FUNC_ID);
        });
        return mapObj;
      };
      if (removeSource.length == 1 && removeSource[0].PARENT_ID == "0") {
        params.queryparam = arrForEach(removeSource[0].children);
      }
      if (removeSource.length > 0 && removeSource[0].PARENT_ID != 0) {
        params.queryparam = arrForEach(removeSource);
      }
      await response(params, treeKey.value);
    } else {
      toast.warning(t("WARNING_MAPPING_REMOVE"));
    }
  }
  if (method == "linkViewAndFunc") {
    let params = apiList["linkViewAndFunc"];

    let treeTarget = TreeCompTgt.value.GetSelectedTarget();

    if (treeTarget.value?.VIEW_ID && vueFileTarget.value.length === 1) {
      params.queryparam = {
        viewId: treeTarget.value.VIEW_ID,
        vueFileUrl: vueFileTarget.value[0].VUE_FILE_URL,
      };
      await response(params, treeKey.value);
      treeTarget = null;
    } else {
      toast.warning(t("WARNING_MAPPING_ONLY_ONE"));
    }
  }
  if (method == "unlinkViewAndFunc") {
    const treeTarget = TreeCompTgt.value.GetSelectedTarget();
    let params = apiList["unlinkViewAndFunc"];

    if (treeTarget.value?.VIEW_ID) {
      params.queryparam = { viewId: treeTarget.value.VIEW_ID };
      await response(params, treeKey.value);
    } else {
      toast.warning(t("WARNING_MAPPING_SELECT_FILE"));
    }
  }
};
const FuncEvent = async (method) => {
  const response = async (params) => {
    const { status, code, message, resultset } = await sendRequest(params);
    if (code == 200) {
      await getFuncListEvent();
      useUiStore.closeModal();
      toast.success(message);
    } else {
      toast.error(t("ERROR_MESSAGE"));
    }
  };
  if (method != "remove") {
    await form.value.validate();
  }

  if (valid.value) {
    if (method == "add") {
      let params = apiList["addFunc"];
      params.queryparam = { ...funcData };
      await response(params);
    }
    if (method == "update") {
      let params = apiList["updateFunc"];
      params.queryparam = { ...funcData };
      await response(params);
    }
  }

  if (method == "remove") {
    let params = apiList["removeFunc"];
    let removeArr = [];
    functionTarget.value.forEach((item) => {
      removeArr.push({
        funcId: item.FUNC_ID,
      });
    });
    params.queryparam = [...removeArr];
    await response(params);
  }
};
const vueFileEvent = async (method) => {
  const response = async (params) => {
    const { status, code, message, resultset } = await sendRequest(params);
    console.log(params);
    if (code == 200) {
      await getVueFileListEvent();
      useUiStore.closeModal();
      toast.success(message);
    } else {
      toast.error(t("ERROR_MESSAGE"));
    }
  };
  if (method != "remove") {
    await form.value.validate();
  }

  if (valid.value) {
    if (method == "add") {
      let params = apiList["addVueFile"];
      params.queryparam = { ...vueFileData };
      await response(params);
    }
    if (method == "update") {
      let params = apiList["updateVueFile"];
      params.queryparam = { ...vueFileData };
      await response(params);
    }
  }
  if (method == "remove") {
    let params = apiList["removeVueFile"];
    let removeArr = [];
    vueFileTarget.value.forEach((item) => {
      removeArr.push({
        vueFileId: item.VUE_FILE_ID,
      });
    });
    params.queryparam = [...removeArr];
    await response(params);
  }
};

const triggerEvent = (CRUD, type) => {
  const triggers = {
    get: () => {
      if (type === "function") {
        getFuncListEvent();
      } else if (type === "vueFile") {
        getVueFileListEvent();
      }
    },
    add: () => {
      if (type === "function") {
        useUiStore.openModalEvent(addFuncModal);
        Object.assign(funcData, funcTemp);
      } else if (type === "vueFile") {
        useUiStore.openModalEvent(addVueFileModal);
        Object.assign(vueFileData, vueFileTemp);
      }
    },
    update: () => {
      if (type === "tree") {
        useUiStore.openModalEvent(addViewModal);
        Object.assign(viewData, viewTemp);
      } else if (type === "function") {
        useUiStore.openModalEvent(updateFuncModal);
        funcData.funcId = functionTarget.value[0].FUNC_ID;
        funcData.funcNm = functionTarget.value[0].FUNC_NM;
        funcData.methodNm = functionTarget.value[0].METHOD_NM;
        funcData.note = functionTarget.value[0].NOTE;
      } else if (type === "vueFile") {
        useUiStore.openModalEvent(updateVueFileModal);
        vueFileData.vueFileId = vueFileTarget.value[0].VUE_FILE_ID;
        vueFileData.vueNm = vueFileTarget.value[0].VUE_NM;
        vueFileData.vueFileUrl = vueFileTarget.value[0].VUE_FILE_URL;
        vueFileData.note = vueFileTarget.value[0].NOTE;
      }
    },
    remove: () => {
      if (type == "tree") {
        useUiStore.openModalEvent(removeViewModal);
      } else if (type === " function") {
        useUiStore.openModalEvent(removeFuncModal);
      } else if (type === "vueFile") {
        useUiStore.openModalEvent(removeVueFileModal);
      }
    },
  };
  triggers[CRUD]();
};

const treeButtons = ref([
  {
    title: computed(() => t("STRING_ADD")),
    icon: "mdi-magnify",
    event: () => {
      useUiStore.openModalEvent(addViewModal);
      Object.assign(viewData, viewTemp);
    },
  },
  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",

    event: () => {
      useUiStore.openModalEvent(removeViewModal);
    },
  },
]);
const functionInput = ref([
  {
    type: "field",
    label: t("STRING_KEYWORD"),
    target: computed({
      get() {
        return funcSearch.value.searchKeyword;
      },
      set(e) {
        funcSearch.value.searchKeyword = e;
      },
    }),
  },
]);
const funcButtons = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      getFuncListEvent();
    },
  },
  {
    title: computed(() => t("STRING_ADD")),
    icon: "mdi-magnify",
    event: () => {
      useUiStore.openModalEvent(addFuncModal);
      Object.assign(funcData, funcTemp);
    },
  },
  {
    title: computed(() => t("STRING_UPDATE")),
    icon: "mdi-magnify",
    disabled: computed(
      () =>
        !(functionTarget.value[0]?.AD_ID && functionTarget.value.length == 1)
    ),

    event: () => {
      useUiStore.openModalEvent(updateFuncModal);
      funcData.funcId = functionTarget.value[0].FUNC_ID;
      funcData.funcNm = functionTarget.value[0].FUNC_NM;
      funcData.methodNm = functionTarget.value[0].METHOD_NM;
      funcData.note = functionTarget.value[0].NOTE;
    },
  },
  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",
    disabled: computed(() => !functionTarget.value.length >= 1),

    event: () => {
      useUiStore.openModalEvent(removeFuncModal);
    },
  },
]);
const vueFileInput = ref([
  {
    type: "field",
    label: computed(() => t("STRING_KEYWORD")),
    target: computed({
      get() {
        return vueFileInput.value.searchKeyword;
      },
      set(e) {
        vueFileInput.value.searchKeyword = e;
      },
    }),
  },
]);
const vueFileButtons = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      getVueFileListEvent();
    },
  },
  {
    title: computed(() => t("STRING_ADD")),
    icon: "mdi-magnify",
    event: () => {
      useUiStore.openModalEvent(addVueFileModal);
      Object.assign(vueFileData, vueFileTemp);
    },
  },
  {
    title: computed(() => t("STRING_UPDATE")),
    icon: "mdi-magnify",
    disabled: computed(
      () =>
        !(functionTarget.value[0]?.AD_ID && functionTarget.value.length == 1)
    ),

    event: () => {
      useUiStore.openModalEvent(updateVueFileModal);
      vueFileData.vueFileId = vueFileTarget.value[0].VUE_FILE_ID;
      vueFileData.vueNm = vueFileTarget.value[0].VUE_NM;
      vueFileData.vueFileUrl = vueFileTarget.value[0].VUE_FILE_URL;
      vueFileData.note = vueFileTarget.value[0].NOTE;
    },
  },
  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",
    disabled: computed(() => !functionTarget.value.length >= 1),

    event: () => {
      useUiStore.openModalEvent(removeVueFileModal);
    },
  },
]);

onMounted(() => {
  getMappingData();
});
</script>

<template>
  <DefaultContainer>
    <v-col cols="6">
      <InputContainer :buttonItems="treeButtons"> </InputContainer>
      <v-card class="pa-2" elevation="2" rounded="lg">
        <TheTreeComp
          ref="TreeCompTgt"
          elementid="MainTree"
          :items="treeviewData"
          :canEdit="canEdit"
          :expandAll="expandAll"
          :key="treeKey"
          :height="treeHeight"
          @update:onGoingEdit="(returnVal) => (treeEditTarget = returnVal)"
        >
        </TheTreeComp>
      </v-card>
    </v-col>
    <v-col cols="1" class="d-flex flex-column justify-center align-center">
      <v-btn
        variant="text"
        icon="mdi-arrow-left-bold"
        color="grey"
        @click.prevent="mappingEvent('addFuncToView')"
      ></v-btn>
      <v-btn
        variant="text"
        icon="mdi-arrow-right-bold"
        color="grey"
        @click.prevent="mappingEvent('removeFuncFromView')"
      ></v-btn>
      <v-btn
        color="grey"
        variant="text"
        @click.prevent="mappingEvent('linkViewAndFunc')"
        icon="mdi-link"
      >
      </v-btn>
      <v-btn
        color="grey"
        variant="text"
        icon="mdi-link-off"
        @click.prevent="mappingEvent('unlinkViewAndFunc')"
      >
      </v-btn>
    </v-col>
    <v-col cols="5">
      <InputContainer
        :inputItems="functionInput"
        @update:inputChange="(value, item) => (item.target = value)"
        :buttonItems="funcButtons"
      >
      </InputContainer>
      <v-card>
        <v-card elevation="2" rounded="lg">
          <Tabulator
            :columns="funcColumn"
            :tableName="'table'"
            :tableData="funcTableData"
            :height="tableHeightCalc"
            @update:rowTarget="(returnVal) => (functionTarget = returnVal)"
          />
        </v-card>
      </v-card>
      <InputContainer
        :inputItems="vueFileInput"
        @update:inputChange="(value, item) => (item.target = value)"
        :buttonItems="vueFileButtons"
      >
      </InputContainer>
      <v-card elevation="2" rounded="lg">
        <Tabulator
          :columns="fileListColumn"
          :tableName="'table'"
          :tableData="vueFilTableData"
          :height="tableHeightCalc"
          @update:rowTarget="(returnVal) => (vueFileTarget = returnVal)"
        />
      </v-card>
    </v-col>
  </DefaultContainer>

  <ModalContainer type="dialog" :text="t('STRING_VIEW_ADD')" :id="addViewModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="viewData.viewNm"
        :rules="[rules.required]"
        clearable
      />

      <v-btn :disabled="!valid" @click="viewEvent('add')">
        {{ t("STRING_SAVE") }}</v-btn
      >
    </v-form>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_VIEW_UPDATE')"
    :id="updateViewModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="viewData.viewNm"
        :rules="[rules.required]"
        clearable
      />

      <v-btn class="mt-3" :disabled="!valid" @click="viewEvent('update')">
        {{ t("STRING_SAVE") }}</v-btn
      >
    </v-form>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_VIEW_REMOVE')"
    :id="removeViewModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="viewEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
  <ModalContainer type="dialog" :text="t('STRING_FN_ADD')" :id="addFuncModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="funcData.funcNm"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_FN_METHOD')"
        v-model="funcData.methodNm"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NOTE')"
        v-model="funcData.note"
        clearable
      />

      <v-btn class="mt-3" :disabled="!valid" @click="FuncEvent('add')">{{
        t("STRING_SAVE")
      }}</v-btn>
    </v-form>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_FN_UPDATE')"
    :id="updateFuncModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="funcData.funcNm"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_FN_METHOD')"
        v-model="funcData.methodNm"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NOTE')"
        v-model="funcData.note"
        clearable
      />

      <v-btn class="mt-3" :disabled="!valid" @click="FuncEvent('update')">{{
        t("STRING_SAVE")
      }}</v-btn>
    </v-form>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_FN_REMOVE')"
    :id="removeFuncModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="FuncEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_VUE_FILE_ADD')"
    :id="addVueFileModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_VUE_FILE_NAME')"
        v-model="vueFileData.vueNm"
        clearable
      />

      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_VUE_FILE_URL')"
        v-model="vueFileData.vueFileUrl"
        clearable
      />

      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NOTE')"
        v-model="vueFileData.note"
        clearable
      />

      <v-btn class="mt-3" :disabled="!valid" @click="vueFileEvent('add')">{{
        t("STRING_SAVE")
      }}</v-btn>
    </v-form>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_VUE_FILE_UPDATE')"
    :id="updateVueFileModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_VUE_FILE_NAME')"
        v-model="vueFileData.vueNm"
        clearable
      />

      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_VUE_FILE_URL')"
        v-model="vueFileData.vueFileUrl"
        clearable
      />

      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NOTE')"
        v-model="vueFileData.note"
        clearable
      />

      <v-btn class="mt-3" :disabled="!valid" @click="vueFileEvent('update')">{{
        t("STRING_SAVE")
      }}</v-btn>
    </v-form>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_VUE_FILE_REMOVE')"
    :id="removeVueFileModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="vueFileEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
</template>
