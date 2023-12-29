<script setup>
import { ref, reactive, computed, onMounted, watch, provide } from "vue";
import {
  DefaultContainer,
  Tabulator,
  SearchBarContainer,
  ModalContainer,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { useRoute } from "vue-router";
import { TheTreeComp } from "@daiahub/thetreecomp";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
import { useDisplay } from "vuetify";
const { mdAndDown } = useDisplay();
const toast = useToast();

const route = useRoute();

const tableHeight = 64 + 64 + 12;
const tableHeightCalc = `calc(100vh - ${tableHeight}px)`;
const { t } = useI18n();

const apiList = {
  getMenuViewMap: {
    url: "/v2/privilege/policy/menu/views",
    method: "get",
  },
  createMenu: {
    url: "/v2/privilege/menu/create",
    method: "post",
    queryparam: {
      menuName: "",
      type: "",
      parentMenuId: undefined,
      payload: "",
    },
  },
  updateMenu: {
    url: "/v2/privilege/menu/update",
    method: "post",
    queryparam: {
      menuId: "",
      menuName: "",
      parentMenuId: "",
      payload: "",
    },
  },
  removeMenu: {
    url: "/v2/privilege/menu/delete",
    method: "post",
    queryparam: {
      menuId: "",
    },
  },
  copyMenu: {
    url: "/v2/privilege/menu/copy",
    method: "post",
    queryparam: {
      srcMenuId: [
        {
          menuId: "",
        },
      ],
      tgtMenuId: {
        menuId: "",
      },
    },
  },
  moveMenu: {
    url: "/v2/privilege/menu/move",
    method: "post",
    queryparam: {
      srcMenuId: "",
      tgtMenuId: "",
    },
  },

  createViewToMenu: {
    url: "/v2/privilege/policy/menu/views/create",
    method: "post",
    queryparam: {
      menuId: "",
      viewId: "",
    },
  },
  deleteViewFromMenu: {
    url: "/v2/privilege/policy/menu/views/delete",
    method: "post",
    queryparam: {
      menuId: "",
      viewId: "",
    },
  },
  getview: {
    url: "/v2/privilege/view/list",
    method: "get",
    queryparam: {
      searchKeyword: "",
    },
  },
};
const columns = computed(() => {
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
      field: "viewId",
      sorter: "number",
      responsive: 0,
    },
    {
      title: t("VIEW_NAME"),
      field: "viewName",
      sorter: "string",
    },
    {
      title: t("VUE_FILE_URL"),
      field: "vueFileUrl",
      sorter: "string",
    },
    {
      title: t("NOTE"),
      field: "NOTE",
      sorter: "string",
      responsive: 2,
    },
  ];
});

const createMenuModal = ref(false);
const updateMenuModal = ref(false);
const removeMenuModal = ref(false);
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const treeData = ref([]);
const TreeCompTgt = ref();
const canEdit = ref(true);
const expandAll = ref(true);
const treeEditTarget = ref([]);
const tableData = ref([]);
const treeKey = ref(0);


const search = ref({ searchKeyword: "" });
const menuTemp = {
  menuName: "",
  type: "menu",
};
const menuData = reactive({});
Object.assign(menuData, menuTemp);

const vueFileTemp = {
  vueFileId: "",
  vueName: "",
  vueFileUrl: "",
  note: "",
};
const vueFileData = reactive({});
Object.assign(vueFileData, vueFileTemp);
const rowTarget = ref([]);

const form = ref();
const valid = ref(false);

watch([treeEditTarget, updateMenuModal], (newVal, oldVal) => {
  if (newVal[0].menuId && newVal[0] !== oldVal[0]) {
    menuData.menuName = treeEditTarget.value.menuName;

    updateMenuModal.value = true;

  }
  if (oldVal[1] == true && newVal[1] == false) {
    treeEditTarget.value = [];
  }
});

const sendRequest = async (params, i) => {
  return await useConnectionStore.request(params);
};

const getMappingData = async () => {

  const createDataTree = (dbData) => {
    let rtnVal = {};
    let treeData = [];
    dbData.forEach((data) => {
      rtnVal[data.treeId] = {
        name: data.menuName ? data.menuName : data.viewName,
        tree_id: data.treeId,
        p_tree_id: data.parentId,
        type: data.type,
        id: ref(false),
        url: data.vueFileUrl,
        children: [],
        ...data,
      };
    });
    dbData.forEach((data) => {
      if (!!data.parentId == false) {
        treeData.push(rtnVal[data.treeId]);
      } else {
        rtnVal[data.parentId]?.children.push(rtnVal[data.treeId]);
      }
    });
    return JSON.parse(JSON.stringify(treeData));
  };
  const { data, code, result, because } = await sendRequest(
    apiList["getMenuViewMap"]
  );
  if (code == 200) {
    if (data.length > 0) {
      treeData.value = createDataTree(data);
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU")]));
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("MENU")]));
  }
};
const getViewListEvent = async () => {
  let params = apiList["getview"];
  params.queryparam = {
    searchKeyword: search.value.searchKeyword,
  };

  const { data, code, result, because } = await sendRequest(params);
  if (code == 200) {
    if (data.length == 0) {
      toast.warning(t("NO_DATA_SEARCH", [t("VIEW")]));
    }
    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("VIEW")]));
  }
};

const menuEvent = async (method) => {
  let source = TreeCompTgt.value?.GetSelectedSource();
  let target = TreeCompTgt.value?.GetSelectedTarget();
  const request = async (params, modal, successText, errorText) => {
    const { data, code, result, because, message } = await sendRequest(params);

    if (code == 200) {
      await getMappingData();
      treeKey.value++;

      if (modal) {
        modal.value = false;
      }
      source = undefined;
      target = undefined;

      toast.success(successText);
    } else {
      toast.error(errorText);
    }
  };
  const triggerAction = {
    create: () => {
      if (!!target.value?.viewId == false) {
        let params = apiList["createMenu"];
        params.queryparam = {
          parentMenuId: target.value?.menuId ? target.value.menuId : undefined,
          ...menuData,
        };
        request(
          params,
          createMenuModal,
          t("SUCCESS_ADD", [t("MENU")]),
          t("ERROR_ADD", [t("MENU")])
        );
      } else {
        toast.warning(t("WARNING_CHECK_ADD_MAPPING"));
      }
    },
    update: () => {
      let params = apiList["updateMenu"];

      params.queryparam = {
        menuId: treeEditTarget.value.menuId,

        ...menuData,
      };
      request(
        params,
        updateMenuModal,
        t("SUCCESS_UPDATE", [t("MENU")]),
        t("ERROR_UPDATE", [t("MENU")])
      );
    },
    remove: () => {
      let params = apiList["removeMenu"];
      if (source.length === 1) {
        if (source[0].menuId) {
          // 멀티선택 안하게 하기 위해서 0번으로 처리함.
          const removeArr = [];
          source.forEach((x) => {
            removeArr.push({
              menuId: x.menuId,
            });
          });
          params.queryparam = { ...removeArr[0] };
          request(
            params,
            removeMenuModal,
            t("SUCCESS_REMOVE", [t("MENU")]),
            t("ERROR_REMOVE", [t("MENU")])
          );
        } else {
          toast.warning(t("WARNING_CHECK_REMOVE_MAPPING"));
        }
      } else {
        toast.warning(t("WARNING_MAPPING_ONLY_ONE"));
      }
    },
    move: () => {
      let params = apiList["moveMenu"];
      if (source[0]?.menuId && target.value?.menuId) {
        const obj = {
          srcMenuIds: [],
          tgtMenuId: "",
        };

        obj.tgtMenuId = target.value.menuId;
        source.forEach((item) => {
          obj.srcMenuIds.push(item.menuId);
        });

        params.queryparam = obj;
        request(
          params,
          undefined,
          t("SUCCESS_MOVE", [t("MENU")]),
          t("ERROR_MOVE", [t("MENU")])
        );
      } else {
        toast.warning(t("WARING_MAPPING_MOVE"));
      }
    },
    copy: () => {
      const params = apiList["copyMenu"];
      if (source[0]?.menuId && target.value?.menuId) {
        const obj = {
          srcMenuIds: [],
          tgtMenuId: "",
        };
        obj.tgtMenuId = target.value.menuId;
        source.forEach((item) => {
          obj.srcMenuIds.push(item.menuId);
        });

        params.queryparam = obj;
        request(
          params,
          undefined,
          t("SUCCESS_COPY", [t("MENU")]),
          t("ERROR_COPY", [t("MENU")])
        );
      } else {
        toast.warning(t("WARNING_MAPPING_COPY"));
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
    const { status, code, message, data, because } = await sendRequest(params);
    if (code == 200) {
      await getMappingData();
      let treeTarget = TreeCompTgt.value.GetSelectedTarget();

      treeKey.value++;
      toast.success(successText);
    } else {
      toast.error(errorText);
    }
  };
  const triggerAction = {
    createViewToMenu: () => {
      let params = apiList["createViewToMenu"];
      let treeTarget = TreeCompTgt.value.GetSelectedTarget();
      let checkDupli = true;

      let itemArr = [];

      if (treeTarget.value?.menuId && rowTarget.value.length > 0) {
        const checkValue = (target, item) => {
          let check = false;

          target.forEach((treeChildItem) => {
            if (treeChildItem.viewId == item.viewId) {
              check = true;
            }
          });
          return check;
        };

        let rows = rowTarget.value;
        rows.forEach((item) => {
       
          itemArr.push(item.viewId);
        });
        if (itemArr.length > 0) {
          params.queryparam = {
            menuId: treeTarget.value.menuId,
            viewIds: itemArr,
          };
          request(
            params,
            t("SUCCESS_MAPPING", [t("MENU"), t("VIEW")]),
            t("ERROR_MAPPING", [t("MENU"), t("VIEW")])
          );
        }
      } else {
        toast.warning(t("WARNING_MAPPING_SELECT"));
      }
    },
    deleteViewFromMenu: () => {
      let params = apiList["deleteViewFromMenu"];
      const deleteSource = TreeCompTgt.value.GetSelectedSource();
      if (deleteSource.length > 0) {
        const reduceArr = [];

        const arrForEach = (arr) => {
          if (arr.viewId) {
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
            const parentMenuId = reduceArr[0].stats.getParent(
              reduceArr[0].id
            ).menuId;
            const viewIds = reduceArr.map((item) => item.viewId);
            params.queryparam = {
              menuId: parentMenuId,
              viewIds: viewIds,
            };
            request(
              params,
              t("SUCCESS_MAPPING_REMOVE", [t("MENU"), t("VIEW")]),
              t("ERROR_MAPPING_REMOVE", [t("MENU"), t("VIEW")])
            );
          } else {
            toast.warning(t("WARNING_SAME_PARENT"));
          }
        }
      } else {
        toast.warning(t("WARNING_MAPPING_REMOVE"));
      }
    },
  };
  triggerAction[method]();
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
        createMenuModal.value = true;
        Object.assign(menuData, menuTemp);
      },
    },

    {
      methodName: "BTN_DELETE",

      title: computed(() => t("BTN_DELETE")),

      event: () => {
        removeMenuModal.value = true;
      },
    },
    {
      methodName: "BTN_COPY",
      icon: "mdi-content-copy",
      title: computed(() => t("BTN_COPY")),

      event: () => {
        menuEvent("copy");
      },
    },
    {
      methodName: "BTN_MOVE",
      icon: "mdi-file-move-outline",
      title: computed(() => t("BTN_MOVE")),

      event: () => {
        menuEvent("move");
      },
    },
  ],
});
const viewSearchBar = reactive({
  inputs: [
    {
      type: "field",
      label: computed(() => t("KEYWORD")),
      target: computed({
        get() {
          return search.value.searchKeyword;
        },
        set(e) {
          search.value.searchKeyword = e;
        },
      }),
      event: () => {
        getViewListEvent();
      },
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",
      title: computed(() => t("BTN_SEARCH")),

      event: () => {
        getViewListEvent();
      },
    },
  ],
});

treeSearchBar.buttons = treeSearchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});

viewSearchBar.buttons = viewSearchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});

provide("treeSearchBar", treeSearchBar);
provide("viewSearchBar", viewSearchBar);
onMounted(async () => {
  await getMappingData();
  await getViewListEvent();
});
</script>

<template>
  <DefaultContainer>
    <v-col cols="12" lg="6">
      <SearchBarContainer :breadcrumb="true" :name="'treeSearchBar'">
      </SearchBarContainer>
      <Skeleton
        :loadingData="treeData.length == 0"
        :height="'calc(100vh - 140px)'"
        :type="'list-item-three-line,list-item-three-line,list-item-three-line'"
      >
        <TheTreeComp
          ref="TreeCompTgt"
          elementid="MainTree"
          :items="treeData"
          :canEdit="canEdit"
          :expandAll="expandAll"
          :key="treeKey"
          :readOnly="false"
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
        variant="text"
        icon="mdi-arrow-left-bold"
        color="grey"
        v-if="
          route.meta.methods?.some(
            (item2) => 'BTN_CREATE_MAPPING' === item2.methodName
          )
        "
        @click.prevent="mappingEvent('createViewToMenu')"
      ></v-btn>
      <v-btn
        variant="text"
        icon="mdi-arrow-right-bold"
        color="grey"
        v-if="
          route.meta.methods?.some(
            (item2) => 'BTN_DELETE_MAPPING' === item2.methodName
          )
        "
        @click.prevent="mappingEvent('deleteViewFromMenu')"
      ></v-btn>
    </v-col>
    <v-col cols="12" lg="5">
      <SearchBarContainer :name="'viewSearchBar'"> </SearchBarContainer>
      <Skeleton :loadingData="tableData.length == 0" :height="tableHeightCalc">
        <Tabulator
          :pagination="true"
          :columns="columns"
          :tableName="'table'"
          :tableData="tableData"
          :height="tableHeightCalc"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </Skeleton>
    </v-col>
  </DefaultContainer>
  <ModalContainer
    @btnEvent="menuEvent('create')"
    :text="t('MENU_ADD')"
    v-model="createMenuModal"
  >
    <v-form v-model="valid" ref="form" class="" @submit.prevent="" >
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('NAME')"
        v-model="menuData.menuName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    @btnEvent="menuEvent('update')"
    :text="t('MENU_UPDATE')"
    v-model="updateMenuModal"
  >
    <v-form v-model="valid" ref="form" class="" @submit.prevent="">
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('NAME')"
        v-model="menuData.menuName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    @btnEvent="menuEvent('remove')"
    :text="t('MENU_REMOVE')"
    v-model="removeMenuModal"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
</template>
