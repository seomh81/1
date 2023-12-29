<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  Tabulator,
  ModalContainer,
  InputContainer,
  DefaultContainer,
  ButtonContainer,
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
  getRoleUserwMap: {
    url: "/v1/priv/listroleusermap",
    method: "get",
  },
  addRole: {
    url: "/v1/priv/addrole",
    method: "post",
    queryparam: {
      roleNm: "",
      type: "",
      parentRoleId: undefined,
      payload: "",
    },
  },
  updateRole: {
    url: "/v1/priv/updaterole",
    method: "post",
    queryparam: {
      roleId: "",
      roleNm: "",
      parentRoleId: "",
      payload: "",
    },
  },
  removeRole: {
    url: "/v1/priv/removerole",
    method: "post",
    queryparam: {
      roleId: "",
    },
  },
  copyRole: {
    url: "/v1/priv/copyrole",
    method: "post",
    queryparam: {
      srcRoleId: [
        {
          roleId: "",
        },
      ],
      tgtRoleId: {
        roleId: "",
      },
    },
  },
  moveRole: {
    url: "/v1/priv/moverole",
    method: "post",
    queryparam: {
      srcRoleId: "",
      tgtRoleId: "",
    },
  },

  addUserToRole: {
    url: "/v1/priv/addusertorole",
    method: "post",
    queryparam: {
      roleId: "",
      userId: "",
    },
  },
  removeUserFromRole: {
    url: "/v1/priv/removeuserfromrole",
    method: "post",
    queryparam: {
      roleId: "",
      userId: "",
    },
  },
  getUser: {
    url: "/v1/priv/listuser",
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
      title: t("STRING_ID"),
      field: "USER_ID",
      sorter: "number",
      responsive: 0,
    },
    {
      title: t("STRING_NAME"),
      field: "USER_NM",
      sorter: "string",
    },
    {
      title: t("STRING_PHONE_NUMBER"),
      field: "PHONENO",
      sorter: "string",
    },
    {
      title: t("STRING_ACCOUNT_TYPE"),
      field: "ACCOUNT_TYPE",
      sorter: "string",
    },
    {
      title: t("STRING_ASSET_CODE"),
      field: "assetCode",
      sorter: "string",
    },
    {
      title: t("STRING_STATUS"),
      field: "status",
      sorter: "string",
    },
  ];
});

const addRoleModal = uuidv4();
const updateRoleModal = uuidv4();
const removeRoleModal = uuidv4();

const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const treeData = ref([]);
const TreeCompTgt = ref();
const canEdit = ref(true);
const expandAll = ref(true);
const treeEditTarget = ref([]);
const tableData = ref([]);
const treeKey = ref(0);

const search = ref({searchKeyword:''});
const roleTemp = {
  roleId: "",
  parentRoleId: "",
  roleNm: "",
  type: "role",
};
const roleData = reactive({});
Object.assign(roleData, roleTemp);

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
    roleData.roleId = treeEditTarget.value.ROLE_ID;
    roleData.roleNm = treeEditTarget.value.ROLE_NM;

    useUiStore.openModalEvent(updateRoleModal);

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
    console.log(dbData);
    dbData.forEach((data) => {
      rtnVal[data.TREE_ID] = {
        name: data.ROLE_NM ? data.ROLE_NM : data.USER_ID,
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
    apiList["getRoleUserwMap"]
  );
  if (code == 200) {
    treeData.value = createDataTree(resultset);
    console.log(treeData.value);
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
};
const getUserListEvent = async () => {
  let params = apiList["getUser"];
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

const treeEvent = async (method) => {
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
    let params = apiList["addRole"];
    if (!!target.value?.USER_ID == false) {
      roleData.parentRoleId = target.value?.ROLE_ID ? target.value.ROLE_ID : "";
      params.queryparam = { ...roleData };
      await response(params);
    } else {
      toast.warning(t("WARNING_CHECK_ADD_MAPPING"));
    }
  }
  if (method == "update") {
    let params = apiList["updateRole"];
    params.queryparam = { ...roleData };
    await response(params);
  }
  if (method == "remove") {
    let params = apiList["removeRole"];
    if (source.length === 1) {
      if (source[0].ROLE_ID) {
        // 멀티선택 안하게 하기 위해서 0번으로 처리함.
        const removeArr = [];
        source.forEach((x) => {
          removeArr.push({
            roleId: x.ROLE_ID,
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
    let params = apiList["moveRole"];
    if (source[0]?.ROLE_ID && target.value?.ROLE_ID) {
      const obj = {
        srcRoleIds: [],
        tgtRoleId: "",
      };

      obj.tgtRoleId = target.value.ROLE_ID;
      source.forEach((item) => {
        obj.srcRoleIds.push(item.ROLE_ID);
      });

      params.queryparam = obj;
      // console.log(params.queryparam);
      await response(params);
    } else {
      toast.warning(t("WARNING_MAPPING_MOVE"));
    }
  }
  if (method == "copy") {
    const params = apiList["copyRole"];
    console.log(target);
    console.log(source);
    if (source[0]?.ROLE_ID) {
      params.queryparam = {
        tgtRoleId: target.value?.ROLE_ID ? target.value.ROLE_ID : undefined,
        srcRoleIds: [source[0].ROLE_ID],
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

  if (method == "addUserToRole") {
    let params = apiList["addUserToRole"];
    let treeTarget = TreeCompTgt.value.GetSelectedTarget();
    let checkDupli = true;

    let itemArr = [];
    if (treeTarget.value?.ROLE_ID && rowTarget.value.length > 0) {
      const checkValue = (target, item) => {
        let check = false;

        target.forEach((treeChildItem) => {
          if (treeChildItem.USER_ID == item.USER_ID) {
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
          itemArr.push(item.USER_ID);
        }
      });
      if (itemArr.length > 0) {
        params.queryparam = {
          roleId: treeTarget.value.ROLE_ID,
          userIds: itemArr,
        };
        await response(params);
      }
    } else {
      toast.warning(t("WARNING_MAPPING_SELECT"));
    }
  }

  if (method == "removeUserFromRole") {
    let removeSource = TreeCompTgt.value.GetSelectedSource();
    console.log(removeSource);
    if (removeSource.length > 0) {
      let params = apiList["removeUserFromRole"];

      const arrForEach = (arr) => {
        let mapObj = {
          roleId: "",
          userIds: [],
        };
        arr.forEach((item) => {
          mapObj.roleId = item.stats.getParent(item.id).ROLE_ID;
          mapObj.userIds.push(item.USER_ID);
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
      removeSource = {};
    } else {
      toast.warning(t("WARNING_MAPPING_REMOVE"));
    }
  }
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
      useUiStore.openModalEvent(addRoleModal);
      Object.assign(roleData, roleTemp);
    },
  },

  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",

    event: () => {
      useUiStore.openModalEvent(removeRoleModal);
    },
  },
  {
    title: computed(() => t("STRING_COPY")),
    icon: "mdi-magnify",
    event: () => {
      treeEvent("copy");
    },
  },
  {
    title: computed(() => t("STRING_MOVE")),
    icon: "mdi-magnify",

    event: () => {
      treeEvent("move");
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
      getUserListEvent();
    },
  },
]);
</script>

<template>
  <DefaultContainer>
    <v-col cols="6">
      <InputContainer :buttonItems="treeButtons"> </InputContainer>

      <v-card class="pa-2">

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
        @click.prevent="mappingEvent('addUserToRole')"
      ></v-btn>
      <v-btn
        variant="text"
        icon="mdi-arrow-right-bold"
        color="grey"
        @click.prevent="mappingEvent('removeUserFromRole')"
      ></v-btn>
    </v-col>
    <v-col cols="5">
      <InputContainer
        :inputItems="inputItems"
        @update:inputChange="(value, item) => (item.target = value)"
        :buttonItems="buttonItems"
      ></InputContainer>
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

  <ModalContainer type="dialog" text="메뉴 추가" :id="addRoleModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="roleData.roleNm"
        :rules="[rules.required]"
        clearable
      />
      <v-btn class="mt-3" :disabled="!valid" @click="treeEvent('add')">
        {{ t("STRING_ADD") }}</v-btn
      >
    </v-form>
  </ModalContainer>

  <ModalContainer type="dialog" text="메뉴 업데이트" :id="updateRoleModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="roleData.roleNm"
        :rules="[rules.required]"
        clearable
      />
      <v-btn class="mt-3" :disabled="!valid" @click="treeEvent('update')">
        {{ t("STRING_SAVE") }}</v-btn
      >
    </v-form>
  </ModalContainer>
  <ModalContainer type="dialog" text="메뉴 삭제" :id="removeRoleModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="treeEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
</template>
