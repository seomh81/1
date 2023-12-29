<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  DefaultContainer,
  Tabulator,
  ButtonContainer,
  InputContainer,
  ModalContainer,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { v4 as uuidv4 } from "uuid";
import { TheTreeComp } from "@daiahub/thetreecomp";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const height = 64 + 32 + 22;
const treeHeight = `calc(100vh - ${height + 46}px)`;

const tableHeight = height + 30;
const tableHeightCalc = `calc(100vh - ${tableHeight}px)`;
const { t } = useI18n();

const apiList = {
  getMenuViewwMap: {
    url: "/v1/priv/listmenuviewmap",
    method: "get",
  },
  addMenu: {
    url: "/v1/priv/addmenu",
    method: "post",
    queryparam: {
      menuNm: "",
      type: "",
      parentMenuId: undefined,
      payload: "",
    },
  },
  updateMenu: {
    url: "/v1/priv/updatemenu",
    method: "post",
    queryparam: {
      menuId: "",
      menuNm: "",
      parentMenuId: "",
      payload: "",
    },
  },
  removeMenu: {
    url: "/v1/priv/removemenu",
    method: "post",
    queryparam: {
      menuId: "",
    },
  },
  copyMenu: {
    url: "/v1/priv/copymenu",
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
    url: "/v1/priv/movemenu",
    method: "post",
    queryparam: {
      srcMenuId: "",
      tgtMenuId: "",
    },
  },

  addViewToMenu: {
    url: "/v1/priv/addviewtomenu",
    method: "post",
    queryparam: {
      menuId: "",
      viewId: "",
    },
  },
  removeViewFromMenu: {
    url: "/v1/priv/removeviewfrommenu",
    method: "post",
    queryparam: {
      menuId: "",
      viewId: "",
    },
  },
  getview: {
    url: "/v1/priv/listview",
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
      title: t("STRING_INDEX"),
      field: "VIEW_ID",
      sorter: "number",
      responsive: 0,
    },
    {
      title: t("STRING_VIEW_NAME"),
      field: "VIEW_NM",
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
      responsive: 2,
    },
  ];
});

const addMenuModal = uuidv4();
const updateMenuModal = uuidv4();
const removeMenuModal = uuidv4();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const treeData = ref([]);
const TreeCompTgt = ref();
const canEdit = ref(true);
const expandAll = ref(true);
const treeEditTarget = ref([]);
const tableData = ref([]);
const treeKey = ref(0);
const readOnly = true;

const search = ref({ searchKeyword: "" });
const menuTemp = {
  menuId: "",
  parentMenuId: "",
  menuNm: "",
  type: "menu",
};
const menuData = reactive({});
Object.assign(menuData, menuTemp);

const vueFileTemp = {
  vueFileId: "",
  vueNm: "",
  vueFileUrl: "",
  note: "",
};
const vueFileData = reactive({});
Object.assign(vueFileData, vueFileTemp);
const rowTarget = ref([]);
const rules = ref({
  required: (value) => {
    return !!value || "입력이 필요합니다.";
  },
});
const form = ref();
const valid = ref(false);

watch([treeEditTarget, form], (newVal, oldVal) => {
  if (newVal[0] !== oldVal[0]) {
    menuData.menuId = treeEditTarget.value.MENU_ID;
    menuData.menuNm = treeEditTarget.value.MENU_NM;

    useUiStore.openModalEvent(updateMenuModal);

    // viewData.viewId = treeEditTarget.value.VIEW_ID;
    // viewData.viewNm = treeEditTarget.value.VIEW_NM;
    // useUiStore.openModalEvent(updateViewModal);
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
        name: data.MENU_NM ? data.MENU_NM : data.VIEW_NM,
        tree_id: data.TREE_ID,
        p_tree_id: data.PARENT_ID,
        type: data.TYPE,
        id: uuidv4(),
        children: [],
        ...data,
      };
    });
    dbData.forEach((data) => {
      if (!!data.PARENT_ID == false) {
        treeData.push(rtnVal[data.TREE_ID]);
      } else {
        rtnVal[data.PARENT_ID]?.children.push(rtnVal[data.TREE_ID]);
      }
    });
    return JSON.parse(JSON.stringify(treeData));
  };
  const { status, code, message, resultset } = await sendRequest(
    apiList["getMenuViewwMap"]
  );
  if (code == 200) {
    treeData.value = createDataTree(resultset);
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
};
const getViewListEvent = async () => {
  let params = apiList["getview"];
  params.queryparam = {
    searchKeyword: search.value.searchKeyword,
  };

  const { status, code, message, resultset } = await sendRequest(params);
  if (code == 200) {
    tableData.value = resultset;
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
};

const menuEvent = async (method) => {
  let source = TreeCompTgt.value.GetSelectedSource();
  let target = TreeCompTgt.value.GetSelectedTarget();
  const response = async (params) => {
    const { status, code, message, resultset } = await sendRequest(params);

    if (code == 200) {
      await getMappingData();
      treeKey.value++;
      useUiStore.closeModal();
      source = undefined;
      target = undefined;
      toast.success(message);
    } else {
      toast.error(t("ERROR_MESSAGE"));
    }
  };

  if (method == "add") {
    if (!!target.value?.VIEW_ID == false) {
      let params = apiList["addMenu"];
      menuData.parentMenuId = target.value?.MENU_ID ? target.value.MENU_ID : "";
      params.queryparam = { ...menuData };
      await response(params);
    } else {
      toast.warning(t("WARNING_CHECK_ADD_MAPPING"));
    }
  }
  if (method == "update") {
    let params = apiList["updateMenu"];
    params.queryparam = { ...menuData };
    await response(params);
  }
  if (method == "remove") {
    let params = apiList["removeMenu"];
    if (source.length === 1) {
      if (source[0].MENU_ID) {
        // 멀티선택 안하게 하기 위해서 0번으로 처리함.
        const removeArr = [];
        source.forEach((x) => {
          removeArr.push({
            menuId: x.MENU_ID,
          });
        });
        params.queryparam = { ...removeArr[0] };
        await response(params);
      } else {
        toast.warning(t("WARNING_CHECK_REMOVE_MAPPING"));
      }
    } else {
      toast.warning(t("WARNING_MAPPING_ONLY_ONE"));
    }
  }
  if (method == "move") {
    let params = apiList["moveMenu"];
    if (source[0]?.MENU_ID && target.value?.MENU_ID) {
      const obj = {
        srcMenuIds: [],
        tgtMenuId: "",
      };

      obj.tgtMenuId = target.value.MENU_ID;
      source.forEach((item) => {
        obj.srcMenuIds.push(item.MENU_ID);
      });

      params.queryparam = obj;
      // console.log(params.queryparam);
      await response(params);
    } else {
      toast.warning(t("WARING_MAPPING_MOVE"));
    }
  }
  if (method == "copy") {
    const params = apiList["copyMenu"];
    if (source[0]?.MENU_ID) {
      params.queryparam = {
        tgtMenuId: target.value?.MENU_ID ? target.value.MENU_ID : undefined,
        srcMenuIds: [source[0].MENU_ID],
      };
      await response(params);
    } else {
      toast.warning(t("WARNING_MAPPING_COPY"));
    }
  }
};

const mappingEvent = async (method) => {
  const response = async (params, key) => {
    console.log(params);
    const { status, code, message, resultset } = await sendRequest(params);
    if (code == 200) {
      await getMappingData();
      treeKey.value++;
      toast.success(message);
    } else {
      toast.error(t("ERROR_MESSAGE"));
    }
  };

  if (method == "addViewToMenu") {
    let params = apiList["addViewToMenu"];
    let treeTarget = TreeCompTgt.value.GetSelectedTarget();
    let checkDupli = true;

    let itemArr = [];

    if (treeTarget.value?.MENU_ID && rowTarget.value.length > 0) {
      const checkValue = (target, item) => {
        let check = false;

        target.forEach((treeChildItem) => {
          if (treeChildItem.VIEW_ID == item.VIEW_ID) {
            check = true;
          }
        });
        return check;
      };

      let rows = rowTarget.value;
      rows.forEach((item) => {
        checkDupli = checkValue(treeTarget.value.children, item);
        if (checkDupli == true) {
          toast.warning(t("WARNING_MAPPING_DUPLI"));
        } else {
          itemArr.push(item.VIEW_ID);
        }
      });
      if (itemArr.length > 0) {
        params.queryparam = {
          menuId: treeTarget.value.MENU_ID,
          viewIds: itemArr,
        };
        await response(params);
      }
    } else {
      toast.warning(t("WARNING_MAPPING_SELECT"));
    }
  }
  // ids로 처리하는 로직
  if (method == "removeViewFromMenu") {
    let removeSource = TreeCompTgt.value.GetSelectedSource();
    console.log(removeSource);
    if (removeSource.length > 0) {
      let params = apiList["removeViewFromMenu"];

      let mapObj = {
        menuId: "",
        viewIds: [],
      };
      const arrForEach = (arr) => {
        arr.forEach((item) => {
          mapObj.menuId = item.stats.getParent(item.id).MENU_ID;
          mapObj.viewIds.push(item.VIEW_ID);
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

  // 배열로 처리하는 로직
  // if (method == "removeViewFromMenu") {
  //   let removeSource = TreeCompTgt.value.GetSelectedSource();
  //   console.log(removeSource);

  //   let params = apiList["removeViewFromMenu"]

  //   const arrForEach = (arr) => {
  //     const removeArr = [];

  //     console.log(arr);
  //     arr.forEach((item) => {
  //       removeArr.push({
  //         menuId: item.stats.getParent(item.id).MENU_ID,
  //         viewId: item.VIEW_ID,
  //       });
  //     });
  //     return removeArr;
  //   };

  //   if (removeSource.length == 1 && removeSource[0].PARENT_ID == "0") {
  //     params.queryparam = arrForEach(removeSource[0].children);
  //   }
  //   if (removeSource.length > 0 && removeSource[0].PARENT_ID != 0) {
  //     params.queryparam = arrForEach(removeSource);
  //   }
  //   await response(params, treeKey.value);
  //   removeSource = {};
  // }
};

onMounted(() => {
  getMappingData();
});

const treeButtons = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      getMappingData();
    },
  },
  {
    title: computed(() => t("STRING_ADD")),
    icon: "mdi-magnify",
    event: () => {
      useUiStore.openModalEvent(addMenuModal);
      Object.assign(menuData, menuTemp);
    },
  },

  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",

    event: () => {
      useUiStore.openModalEvent(removeMenuModal);
    },
  },
  {
    title: computed(() => t("STRING_COPY")),
    icon: "mdi-magnify",
    event: () => {
      menuEvent("copy");
    },
  },
  {
    title: computed(() => t("STRING_MOVE")),
    icon: "mdi-magnify",

    event: () => {
      menuEvent("move");
    },
  },
]);
const inputItems = ref([
  {
    type: "field",
    label: computed(() => t("STRING_KEYWORD")),
    target: computed({
      get() {
        return search.value.searchKeyword;
      },
      set(e) {
        search.value.searchKeyword = e;
      },
    }),
  },
]);
const buttonItems = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      getViewListEvent();
    },
  },
]);
</script>

<template>
  <DefaultContainer>
    <v-col cols="6">
      <InputContainer :buttonItems="treeButtons"> </InputContainer>
      <v-card class="pa-2" elevation="2" rounded="lg">

        <TheTreeComp
          ref="TreeCompTgt"
          elementid="MainTree"
          :items="treeData"
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
        @click.prevent="mappingEvent('addViewToMenu')"
      ></v-btn>
      <v-btn
        variant="text"
        icon="mdi-arrow-right-bold"
        color="grey"
        @click.prevent="mappingEvent('removeViewFromMenu')"
      ></v-btn>
    </v-col>
    <v-col>
      <InputContainer
        :inputItems="inputItems"
        @update:inputChange="(value, item) => (item.target = value)"
        :buttonItems="buttonItems"
      >
      </InputContainer>
      <v-card elevation="2" rounded="lg">

        <Tabulator
        :columns="columns"
        :tableName="'table'"
        :tableData="tableData"
        :height="tableHeightCalc"
        @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </v-card>
    </v-col>
  </DefaultContainer>
  <ModalContainer type="dialog" :text="t('STRING_MENU_ADD')" :id="addMenuModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="menuData.menuNm"
        :rules="[rules.required]"
        clearable
      />

      <v-btn :disabled="!valid" class="mt-3" @update:click="menuEvent('add')">{{
        t("STRING_ADD")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_MENU_UPDATE')"
    :id="updateMenuModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="menuData.menuNm"
        :rules="[rules.required]"
        clearable
      />

      <v-btn
        :disabled="!valid"
        class="mt-3"
        @update:click="menuEvent('update')"
        >{{ t("STRING_ADD") }}</v-btn
      >
    </v-form>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_MENU_REMOVE')"
    :id="removeMenuModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="menuEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
</template>
