<script setup>
import {
  ref,
  reactive,
  computed,
  onMounted,
  watch,
  provide,
} from "vue";
import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  SearchBarContainer,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { TheTreeComp } from "@daiahub/thetreecomp";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
import {  useRoute } from "vue-router";
import { useDisplay } from "vuetify";
const { mdAndDown } = useDisplay();
const tableHeight = 48 + 56;
const tableHeightCalc = `calc(50vh - ${tableHeight}px)`;
const route = useRoute();
const useConnectionStore = connectionStore();
const toast = useToast();
const { t } = useI18n();
const apiList = {
  getViewMap: {
    url: "/v2/privilege/policy/view/funcs",
    method: "get",
  },
  createView: {
    url: "/v2/privilege/view/create",
    method: "post",
    queryparam: {
      viewName: "",
    },
  },
  updateView: {
    url: "/v2/privilege/view/update",
    method: "post",
    queryparam: {
      viewId: "",
      viewName: "",
    },
  },
  deleteView: {
    url: "/v2/privilege/view/delete",
    method: "post",
    queryparam: {
      viewId: "",
      viewName: "",
    },
  },

  createFuncToView: {
    url: "/v2/privilege/policy/view/funcs/create",
    method: "post",
    queryparam: {
      viewId: "",
      functionIds: "",
    },
  },
  deleteFuncFromView: {
    url: "/v2/privilege/policy/view/funcs/delete",
    method: "post",
    queryparam: {
      viewId: "",
      functionIds: "",
    },
  },

  linkViewAndFile: {
    url: "/v2/privilege/policy/view/vue/link",
    method: "post",
    queryparam: {
      viewId: "",
      vueFileUrl: "",
    },
  },
  unlinkViewAndFile: {
    url: "/v2/privilege/policy/view/vue/unlink",
    method: "post",
    queryparam: {
      viewId: "",
    },
  },
  getFunc: {
    url: "/v2/privilege/func/list",
    method: "get",
    queryparam: {
      searchKeyword: "",
    },
  },
  createFunc: {
    url: "/v2/privilege/func/create",
    method: "post",
    queryparam: {
      functionName: "",
      methodName: "",
      note: "",
    },
  },
  updateFunc: {
    url: "/v2/privilege/func/update",
    method: "post",
    queryparam: {
      functionId: "",
      functionName: "",
      methodName: "",
      note: "",
    },
  },
  deleteFunc: {
    url: "/v2/privilege/func/delete",
    method: "post",
    queryparam: {
      functionId: "",
    },
  },

  getVueFile: {
    url: "/v2/privilege/vue/list",
    method: "get",
    queryparam: {
      searchKeyword: "",
    },
  },
  createVueFile: {
    url: "/v2/privilege/vue/create",
    method: "post",
    queryparam: {
      vueFileId: "",
      vueName: "",
      vueFileUrl: "",
      note: "",
    },
  },
  updateVueFile: {
    url: "/v2/privilege/vue/update",
    method: "post",
    queryparam: {
      vueFileId: "",
      vueName: "",
      vueFileUrl: "",
      note: "",
    },
  },
  deleteVueFile: {
    url: "/v2/privilege/vue/delete",
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
      title: t("INDEX"),
      field: "functionId",
      sorter: "number",
      responsive: 0,
    },

    {
      title: t("NAME"),
      field: "functionName",
      sorter: "string",
    },
    {
      title: t("FN_METHOD"),
      field: "methodName",
      sorter: "string",
    },
    {
      title: t("NOTE"),
      field: "note",
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
      title: t("INDEX"),
      field: "vueFileId",
      sorter: "string",
    },
    {
      title: t("VUE_FILE_NAME"),
      field: "vueName",
      sorter: "string",
    },
    {
      title: t("VUE_FILE_URL"),
      field: "vueFileUrl",
      sorter: "string",
    },
    {
      title: t("NOTE"),
      field: "note",
      sorter: "string",
    },
  ];
});
const createViewModal = ref(false);
const updateViewModal = ref(false);
const deleteViewModal = ref(false);
const createFuncModal = ref(false);
const updateFuncModal = ref(false);
const deleteFuncModal = ref(false);
const createVueFileModal = ref(false);
const updateVueFileModal = ref(false);
const deleteVueFileModal = ref(false);

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
  viewName: "",
  type: "view",
};
const viewData = reactive({});
Object.assign(viewData, viewTemp);

const funcTemp = {
  functionName: "",
  methodName: "",
  note: "",
  type: "function",
};
const funcData = reactive({});
Object.assign(funcData, funcTemp);

const vueFileTemp = {
  vueName: "",
  vueFileUrl: "",
  note: "",
  type: "file",
};
const vueFileData = reactive({});
Object.assign(vueFileData, vueFileTemp);

const functionTarget = ref([]);
const vueFileTarget = ref([]);

const form = ref();
const valid = ref(false);

watch([treeEditTarget, updateViewModal], (newVal, oldVal) => {
  if (newVal[0].viewId && newVal[0] !== oldVal[0]) {
    viewData.viewId = treeEditTarget.value.viewId;
    viewData.viewName = treeEditTarget.value.viewName;
    updateViewModal.value = true;
  }
  if (oldVal[1] == true && newVal[1] == false) {
    treeEditTarget.value = [];
  }
});

const sendRequest = async (params, i) => {
  return await useConnectionStore.request(params);
};

const getMappingData = async () => {
  // const data = axios.get("/v2/privilege/listviewfuncmap");

  const createDataTree = (dbData) => {
    let rtnVal = {};
    let treeData = [];
    dbData.forEach((data) => {
      rtnVal[data.treeId] = {
        name: data.viewName ? data.viewName : data.functionName,
        tree_id: data.treeId,
        p_tree_id: data.parentId,
        type: data.type,
        url: data.vueFileUrl,
        id: ref(false),
        children: [],
        ...data,
      };
    });
    dbData.forEach((data) => {
      if (data.parentId == 0) {
        treeData.push(rtnVal[data.treeId]);
      } else {
        rtnVal[data.parentId]?.children.push(rtnVal[data.treeId]);
      }
    });
    return JSON.parse(JSON.stringify(treeData));
  };
  const { data, code, result, because } = await sendRequest(
    apiList["getViewMap"]
  );
  if (code == 200) {
    if (data.length > 0) {
      treeviewData.value = createDataTree(data);
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("VIEW")]));
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("VIEW")]));
  }
};
const text = t("VIEW");
const getFuncListEvent = async () => {
  let params = apiList["getFunc"];
  params.queryparam = {
    searchKeyword: funcSearch.value.searchKeyword,
  };

  const { data, code, result, because } = await sendRequest(params);
  if (code == 200) {
    if (data.length == 0) {
      toast.warning(t("NO_DATA_SEARCH", [t("FN")]));
    }
    funcTableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("FN")]));
  }
};
const getVueFileListEvent = async () => {
  let params = apiList["getVueFile"];
  params.queryparam = {
    searchKeyword: vueFileSearch.value.searchKeyword,
  };

  const { data, code, result, because } = await sendRequest(params);
  if (code == 200) {
    if (data.length == 0) {
      toast.warning(t("NO_DATA_SEARCH", [t("FILE")]));
    }
    vueFilTableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("FILE")]));
  }
};

const viewEvent = async (method) => {
  const target = TreeCompTgt.value.GetSelectedTarget();
  const source = TreeCompTgt.value.GetSelectedSource();

  const request = async (params, modal, successText, errorText) => {
    const { data, code, result, because, message } = await sendRequest(params);
    if (code == 200) {
      await getMappingData();
      if (modal) {
        modal.value = false;
      }
      treeKey.value++;

      source.value = [];
      target.value = [];
      toast.success(successText);
    } else {
      toast.error(errorText);
    }
  };
  if (method != "delete") {
    await form.value.validate();
  }
  const triggerAction = {
    create: () => {
      if (!!target.value?.viewId == false) {
        let params = apiList["createView"];
        params.queryparam = { ...viewData };
        request(
          params,
          createViewModal,
          t("SUCCESS_ADD", [t("VIEW")]),
          t("ERROR_ADD", [t("VIEW")])
        );
      } else {
        toast.warning(t("WARNING_CHECK_VIEW_ADD"));
      }
    },
    update: () => {
      let params = apiList["updateView"];
      params.queryparam = { ...viewData };
      request(
        params,
        updateViewModal,
        t("SUCCESS_UPDATE", [t("VIEW")]),
        t("ERROR_UPDATE", [t("VIEW")])
      );
    },
    delete: () => {
      let params = apiList["deleteView"];
      if (source.length === 1) {
        if (source[0].viewId) {
          const deleteArr = [];
          source.forEach((x) => {
            deleteArr.push({
              viewId: x.viewId,
            });
          });
          params.queryparam = { ...deleteArr[0] };
          request(
            params,
            deleteViewModal,
            t("SUCCESS_REMOVE", [t("VIEW")]),
            t("ERROR_REMOVE", [t("VIEW")])
          );
        } else {
          toast.warning(t("WARNING_CHECK_REMOVE_MAPPING"));
        }
        // 멀티선택 안하게 하기 위해서 0번으로 처리함.
      } else {
        toast.warning(t("WARNING_MAPPING_ONLY_ONE"));
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

const mappingEvent = async (method) => {
  const request = async (params, successText, errorText) => {
    const { data, code, result, because, message } = await sendRequest(params);
    if (code == 200) {
      await getMappingData();
      treeKey.value++;
      toast.success(successText);
    } else {
      toast.error(errorText);
    }
  };
  const triggerAction = {
    createFuncToView: () => {
      let params = apiList["createFuncToView"];
      let treeTarget = TreeCompTgt.value.GetSelectedTarget();
      let checkDupli = true;

      let createArr = [];

      if (treeTarget.value?.viewId && functionTarget.value.length > 0) {
        const checkValue = (target, item) => {
          let check = false;

          target.forEach((treeChildItem) => {
            if (treeChildItem.functionId == item.functionId) {
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
            createArr.push(fnItem.functionId);
          }
        });
        if (createArr.length > 0) {
          params.queryparam = {
            viewId: treeTarget.value.viewId,
            functionIds: createArr,
          };
          request(
            params,
            t("SUCCESS_MAPPING", [t("VIEW"), t("FN")]),
            t("ERROR_MAPPING", [t("VIEW"), t("FN")])
          );
        }
      } else {
        toast.warning(t("WARNING_MAPPING_SELECT"));
      }
    },
    deleteFuncFormView: () => {
      let params = apiList["deleteFuncFromView"];
      const deleteSource = TreeCompTgt.value.GetSelectedSource();
      if (deleteSource.length > 0) {
        const reduceArr = [];

        const arrForEach = (arr) => {
          if (arr.functionId) {
            reduceArr.push(arr);
          }
          if (arr.children?.length > 0) {
            arr.children.forEach((childItem) => {
              arrForEach(childItem);
            });
          }
        };
        deleteSource.forEach((item) => {
          arrForEach(item);
        });

        if (reduceArr.length > 0) {
          const checkSameParent = new Set(
            reduceArr.map((item) => item.p_tree_id)
          );

          if (checkSameParent.size === 1) {
            const parentViewId = reduceArr[0].stats.getParent(
              reduceArr[0].id
            ).viewId;
            const functionIds = reduceArr.map((item) => item.functionId);
            params.queryparam = {
              viewId: parentViewId,
              functionIds: functionIds,
            };
            request(
              params,
              t("SUCCESS_MAPPING_REMOVE", [t("VIEW"), t("FN")]),
              t("ERROR_MAPPING_REMOVE", [t("VIEW"), t("FN")])
            );
          } else {
            toast.warning(t('WARNING_SAME_PARENT'));
          }
        }
      } else {
        toast.warning(t("WARNING_MAPPING_REMOVE"));
      }
    },
    linkViewAndFile: () => {
      let params = apiList["linkViewAndFile"];

      let treeTarget = TreeCompTgt.value.GetSelectedTarget();
      if (vueFileTarget.value.length === 1) {
        if (treeTarget.value?.viewId) {
          params.queryparam = {
            viewId: treeTarget.value.viewId,
            vueFileUrl: vueFileTarget.value[0].vueFileUrl,
          };
          request(
            params,
            t("SUCCESS_MAPPING", [t("VIEW"), t("FILE")]),
            t("ERROR_MAPPING", [t("VIEW"), t("FILE")])
          );
          treeTarget = null;
        } else {
          toast.warning(t('WARNING_NOT_LINK_FUNCTION'));
        }
      } else {
        toast.warning(t("WARNING_MAPPING_ONLY_ONE"));
      }
    },
    unlinkViewAndFile: () => {
      const treeTarget = TreeCompTgt.value.GetSelectedTarget();
      let params = apiList["unlinkViewAndFile"];

      if (treeTarget.value?.viewId) {
        params.queryparam = {
          vueFileUrl: treeTarget.value.url,
          viewId: treeTarget.value.viewId,
        };
        request(
          params,
          t("SUCCESS_MAPPING_REMOVE", [t("VIEW"), t("FILE")]),
          t("ERROR_MAPPING_REMOVE", [t("VIEW"), t("FILE")])
        );
      } else {
        toast.warning(t("WARNING_MAPPING_SELECT_FILE"));
      }
    },
  };
  triggerAction[method]();
};
const FuncEvent = async (method) => {
  const request = async (params, modal, successText, errorText) => {
    const { data, code, result, because, message } = await sendRequest(params);
    if (code == 200) {
      await getFuncListEvent();
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
      let params = apiList["createFunc"];
      params.queryparam = { ...funcData };
      request(
        params,
        createFuncModal,
        t("SUCCESS_ADD", [t("FN")]),
        t("ERROR_ADD", [t("FN")])
      );
    },
    update: () => {
      let params = apiList["updateFunc"];
      params.queryparam = {
        functionId: functionTarget.value[0].functionId,
        ...funcData,
      };
      request(
        params,
        updateFuncModal,
        t("SUCCESS_UPDATE", [t("FN")]),
        t("ERROR_UPDATE", [t("FN")])
      );
    },
    delete: () => {
      let params = apiList["deleteFunc"];
      let deleteArr = [];
      let obj = {
        functionIds: [],
      };
      functionTarget.value.forEach((item) => {
        obj.functionIds.push(item.functionId);
      });

      params.queryparam = obj;
      request(
        params,
        deleteFuncModal,
        t("SUCCESS_REMOVE", [t("FN")]),
        t("ERROR_REMOVE", [t("FN")])
      );
    },
  };
  if (method === "create" || method === "update") {
    await form?.value?.validate();
    if (valid.value) triggerAction[method]();
  } else {
    triggerAction[method]();
  }
};
const vueFileEvent = async (method) => {
  const request = async (params, modal, successText, errorText) => {
    const { data, code, result, because, message } = await sendRequest(params);
    if (code == 200) {
      await getVueFileListEvent();
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
      let params = apiList["createVueFile"];
      params.queryparam = { ...vueFileData };
      request(
        params,
        createVueFileModal,
        t("SUCCESS_ADD", [t("FILE")]),
        t("ERROR_ADD", [t("FILE")])
      );
    },
    update: () => {
      let params = apiList["updateVueFile"];
      params.queryparam = {
        vueFileId: vueFileTarget.value[0].vueFileId,

        ...vueFileData,
      };
      request(
        params,
        updateVueFileModal,
        t("SUCCESS_UPDATE", [t("FILE")]),
        t("ERROR_UPDATE", [t("FILE")])
      );
    },
    delete: () => {
      let params = apiList["deleteVueFile"];
      let deleteArr = [];
      let obj = {
        vueFileIds: [],
      };
      vueFileTarget.value.forEach((item) => {
        obj.vueFileIds.push(item.vueFileId);
      });
      params.queryparam = obj;
      request(
        params,
        deleteVueFileModal,
        t("SUCCESS_REMOVE", [t("FILE")]),
        t("ERROR_REMOVE", [t("FILE")])
      );
    },
  };
  if (method === "create" || method === "update") {
    await form?.value?.validate();
    if (valid.value) triggerAction[method]();
  } else {
    triggerAction[method]();
  }
};

const treeSearchBar = reactive({
  buttons: [
    {
      methodName: "BTN_SEARCH",

      title: computed(() => t("BTN_SEARCH")),
      event: () => {
        getMappingData();
      },
    },
    {
      methodName: "BTN_CREATE",

      title: computed(() => t("BTN_CREATE")),
      event: () => {
        Object.assign(viewData, viewTemp);
        createViewModal.value = true;
      },
    },
    {
      methodName: "BTN_DELETE",

      title: computed(() => t("BTN_DELETE")),

      event: () => {
        deleteViewModal.value = true;
      },
    },
  ],
});

const funcSearchBar = reactive({
  inputs: [
    {
      type: "field",
      label: t("KEYWORD"),
      target: computed({
        get() {
          return funcSearch.value.searchKeyword;
        },
        set(e) {
          funcSearch.value.searchKeyword = e;
        },
      }),
      event: () => {
        getFuncListEvent();
      },
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",

      title: computed(() => t("BTN_SEARCH")),

      event: () => {
        getFuncListEvent();
      },
    },
    {
      methodName: "BTN_CREATE",

      title: computed(() => t("BTN_CREATE")),

      event: () => {
        Object.assign(funcData, funcTemp);
        createFuncModal.value = true;
      },
    },
    {
      methodName: "BTN_UPDATE",

      title: computed(() => t("BTN_UPDATE")),

      disabled: computed(
        () =>
          !(
            functionTarget.value[0]?.functionId &&
            functionTarget.value.length == 1
          )
      ),

      event: () => {
        funcData.functionName = functionTarget.value[0].functionName;
        funcData.methodName = functionTarget.value[0].methodName;
        funcData.note = functionTarget.value[0].note;
        updateFuncModal.value = true;
      },
    },
    {
      methodName: "BTN_DELETE",

      title: computed(() => t("BTN_DELETE")),

      disabled: computed(() => !functionTarget.value.length >= 1),

      event: () => {
        deleteFuncModal.value = true;
      },
    },
  ],
});
const vueFileSearchBar = reactive({
  inputs: [
    {
      type: "field",
      label: computed(() => t("KEYWORD")),
      target: computed({
        get() {
          return vueFileSearch.value.searchKeyword;
        },
        set(e) {
          vueFileSearch.value.searchKeyword = e;
        },
      }),
      event: () => {
        getVueFileListEvent();
      },
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",

      title: computed(() => t("BTN_SEARCH")),

      event: () => {
        getVueFileListEvent();
      },
    },
    {
      methodName: "BTN_CREATE",

      title: computed(() => t("BTN_CREATE")),

      event: () => {
        Object.assign(vueFileData, vueFileTemp);
        createVueFileModal.value = true;
      },
    },
    {
      methodName: "BTN_UPDATE",

      title: computed(() => t("BTN_UPDATE")),

      disabled: computed(
        () =>
          !(
            vueFileTarget.value[0]?.vueFileId && vueFileTarget.value.length == 1
          )
      ),

      event: () => {
        vueFileData.vueName = vueFileTarget.value[0].vueName;
        vueFileData.vueFileUrl = vueFileTarget.value[0].vueFileUrl;
        vueFileData.note = vueFileTarget.value[0].note;
        updateVueFileModal.value = true;
      },
    },
    {
      methodName: "BTN_DELETE",

      title: computed(() => t("BTN_DELETE")),

      disabled: computed(() => !vueFileTarget.value.length >= 1),

      event: () => {
        deleteVueFileModal.value = true;
      },
    },
  ],
});

treeSearchBar.buttons = treeSearchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});

funcSearchBar.buttons = funcSearchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});

vueFileSearchBar.buttons = vueFileSearchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});
provide("treeSearchBar", treeSearchBar);
provide("funcSearchBar", funcSearchBar);

provide("vueFileSearchBar", vueFileSearchBar);
onMounted(async () => {
  await getMappingData();
  getVueFileListEvent();
  getFuncListEvent();
});
</script>

<template>
  <DefaultContainer>
    <v-col cols="12" lg="6">
      <SearchBarContainer :breadcrumb="true" :name="'treeSearchBar'">
      </SearchBarContainer>
      <Skeleton
        :loadingData="treeviewData.length == 0"
        :height="'calc(100vh - 140px)'"
        :type="'list-item-three-line,list-item-three-line,list-item-three-line'"
      >
        <TheTreeComp
          ref="TreeCompTgt"
          elementid="MainTree"
          :items="treeviewData"
          :canEdit="canEdit"
          :expandAll="expandAll"
          :key="treeKey"
          @update:onGoingEdit="(returnVal) => (treeEditTarget = returnVal)"
        >
        </TheTreeComp>
      </Skeleton>
    </v-col>
    <v-col
      cols="12"
      lg="1"
      :class="!mdAndDown ? 'flex-column' : ''"
      class="d-flex justify-center align-center"
    >
      <v-btn
        v-if="
          route.meta.methods?.some(
            (item2) => 'BTN_CREATE_MAPPING' === item2.methodName
          )
        "
        variant="text"
        icon="mdi-arrow-left-bold"
        color="grey"
        @click.prevent="mappingEvent('createFuncToView')"
      ></v-btn>
      <v-btn
        variant="text"
        v-if="
          route.meta.methods?.some(
            (item2) => 'BTN_DELETE_MAPPING' === item2.methodName
          )
        "
        icon="mdi-arrow-right-bold"
        color="grey"
        @click.prevent="mappingEvent('deleteFuncFormView')"
      ></v-btn>
      <v-btn
        color="grey"
        v-if="
          route.meta.methods?.some(
            (item2) => 'BTN_CREATE_LINK' === item2.methodName
          )
        "
        variant="text"
        @click.prevent="mappingEvent('linkViewAndFile')"
        icon="mdi-link"
      >
      </v-btn>
      <v-btn
        color="grey"
        v-if="
          route.meta.methods?.some(
            (item2) => 'BTN_DELETE_LINK' === item2.methodName
          )
        "
        variant="text"
        icon="mdi-link-off"
        @click.prevent="mappingEvent('unlinkViewAndFile')"
      >
      </v-btn>
    </v-col>
    <v-col cols="12" lg="5">
      <v-row>
        <v-col cols="12">
          <SearchBarContainer :name="'funcSearchBar'"> </SearchBarContainer>
          <Skeleton
            :loadingData="funcTableData.length == 0"
            :height="tableHeightCalc"
            :type="'heading,table-row,table-row'"
          >
            <Tabulator
              :pagination="true"
              :columns="funcColumn"
              :tableName="'table'"
              :tableData="funcTableData"
              :height="tableHeightCalc"
              @update:rowTarget="(returnVal) => (functionTarget = returnVal)"
            />
          </Skeleton>
        </v-col>
        <v-col cols="12">
          <SearchBarContainer :name="'vueFileSearchBar'"> </SearchBarContainer>
          <Skeleton
            :loadingData="vueFilTableData.length == 0"
            :height="tableHeightCalc"
            :type="'heading,table-row,table-row'"
          >
            <Tabulator
              :pagination="true"
              :columns="fileListColumn"
              :tableName="'table'"
              :tableData="vueFilTableData"
              :height="tableHeightCalc"
              @update:rowTarget="(returnVal) => (vueFileTarget = returnVal)"
            />
          </Skeleton>
        </v-col>
      </v-row>
    </v-col>
  </DefaultContainer>

  <ModalContainer
    :text="t('VIEW_ADD')"
    v-model="createViewModal"
    @btnEvent="viewEvent('create')"
  >
    <v-form v-model="valid" ref="form" class="" @submit.prevent="">
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('NAME')"
        v-model="viewData.viewName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('VIEW_UPDATE')"
    v-model="updateViewModal"
    @btnEvent="viewEvent('update')"
  >
    <v-form v-model="valid" ref="form" class="" @submit.prevent="">
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('NAME')"
        v-model="viewData.viewName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('VIEW_REMOVE')"
    v-model="deleteViewModal"
    @btnEvent="viewEvent('delete')"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
  <ModalContainer
    :text="t('FN_ADD')"
    @btnEvent="FuncEvent('create')"
    v-model="createFuncModal"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('NAME')"
        v-model="funcData.functionName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('FN_METHOD')"
        v-model="funcData.methodName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('NOTE')"
        v-model="funcData.note"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('FN_UPDATE')"
    @btnEvent="FuncEvent('update')"
    v-model="updateFuncModal"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        variant="solo"
        density="compact"
        class="mb-1"
        :label="t('NAME')"
        v-model="funcData.functionName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        variant="solo"
        density="compact"
        class="mb-1"
        :label="t('FN_METHOD')"
        v-model="funcData.methodName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-text-field
        density="compact"
        variant="solo"
        class="mb-1"
        :label="t('NOTE')"
        v-model="funcData.note"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('FN_REMOVE')"
    v-model="deleteFuncModal"
    @btnEvent="FuncEvent('delete')"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
  <ModalContainer
    :text="t('VUE_FILE_ADD')"
    v-model="createVueFileModal"
    @btnEvent="vueFileEvent('create')"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        variant="solo"
        density="compact"
        class="mb-1"
        :label="t('VUE_FILE_NAME')"
        v-model="vueFileData.vueName"
        clearable
      />

      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('VUE_FILE_URL')"
        v-model="vueFileData.vueFileUrl"
        clearable
      />

      <v-text-field
        class="mb-1"
        variant="solo"
        density="compact"
        :label="t('NOTE')"
        v-model="vueFileData.note"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    @btnEvent="vueFileEvent('update')"
    :text="t('VUE_FILE_UPDATE')"
    v-model="updateVueFileModal"
  >
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('VUE_FILE_NAME')"
        v-model="vueFileData.vueName"
        clearable
      />

      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('VUE_FILE_URL')"
        v-model="vueFileData.vueFileUrl"
        clearable
      />

      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('NOTE')"
        v-model="vueFileData.note"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    @btnEvent="vueFileEvent('delete')"
    :text="t('VUE_FILE_REMOVE')"
    v-model="deleteVueFileModal"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
</template>
