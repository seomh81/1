<script setup>
import { ref, reactive, computed, onMounted, watch, provide } from "vue";
import {
  Tabulator,
  ModalContainer,
  SearchBarContainer,
  DefaultContainer,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import {  useRoute } from "vue-router";
import { TheTreeComp } from "@daiahub/thetreecomp";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
import { useDisplay } from "vuetify";
const { mdAndDown } = useDisplay();
const toast = useToast();
const height = 64 + 32 + 16 + 44;
const treeHeight = `calc(100vh - ${height}px)`;
const route = useRoute();

const tableHeight = 64 + 64 + 12;
const tableHeightCalc = `calc(100vh - ${tableHeight}px)`;

const { t } = useI18n();
const apiList = {
  getRoleUserwMap: {
    url: "/v2/privilege/policy/role/users",
    method: "get",
  },
  createRole: {
    url: "/v2/privilege/role/create",
    method: "post",
    queryparam: {},
  },
  updateRole: {
    url: "/v2/privilege/role/update",
    method: "post",
    queryparam: {},
  },
  deleteRole: {
    url: "/v2/privilege/role/delete",
    method: "post",
    queryparam: {},
  },
  copyRole: {
    url: "/v2/privilege/role/copy",
    method: "post",
    queryparam: {},
  },
  moveRole: {
    url: "/v2/privilege/role/move",
    method: "post",
    queryparam: {},
  },

  createUserToRole: {
    url: "/v2/privilege/policy/role/users/create",
    method: "post",
    queryparam: {},
  },
  deleteUserFromRole: {
    url: "/v2/privilege/policy/role/users/delete",
    method: "post",
    queryparam: {},
  },
  getUser: {
    url: "/v2/user/list",
    method: "get",
    queryparam: {},
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
      title: t("ID"),
      field: "userId",
      sorter: "string",
      responsive: 0,
    },
    {
      title: t("NAME"),
      field: "userName",
      sorter: "string",
    },
    {
      title: t("HP"),
      field: "phonenumber",
      sorter: "string",
    },
    {
      title: t("ACCOUNT_type"),
      field: "ACCOUNT_type",
      sorter: "string",
    },
    {
      title: t("ASSET_CODE"),
      field: "assetCode",
      sorter: "string",
    },
    {
      title: t("STATUS"),
      field: "status",
      sorter: "string",
    },
  ];
});

const createRoleModal = ref(false);
const updateRoleModal = ref(false);
const deleteRoleModal = ref(false);

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
const roleTemp = {
  roleName: "",
  type: "role",
};
const roleData = reactive({});
Object.assign(roleData, roleTemp);

const rowTarget = ref([]);

const form = ref();
const valid = ref(false);

watch([treeEditTarget, updateRoleModal], (newVal, oldVal) => {
  if (newVal[0].roleId && newVal[0] !== oldVal[0]) {
    roleData.roleId = treeEditTarget.value.roleId;
    roleData.roleName = treeEditTarget.value.roleName;
    updateRoleModal.value = true;

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
        name: data.roleName ? data.roleName : data.userId,
        tree_id: data.treeId,
        p_tree_id: data.parentId,
        type: data.type,
        id: ref(false),
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
    apiList["getRoleUserwMap"]
  );
  if (code == 200) {
    if (data.length > 0) {
      treeData.value = createDataTree(data);
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("ROLE")]));
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("ROLE")]));
  }
};
const getUserListEvent = async () => {
  let params = apiList["getUser"];
  params.queryparam = {
    accountStatus: "ALL",
    searchKeyword: search.value.searchKeyword,
  };

  const { data, code, result, because, message } = await sendRequest(params);
  if (code == 200) {
    if (data.length == 0) {
      toast.warning(t("NO_DATA_SEARCH", [t("USER")]));
    }
    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("USER")]));
  }
};

const treeEvent = async (method) => {
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
      let params = apiList["createRole"];
      if (!!target.value?.userId == false) {
        params.queryparam = {
          parentRoleId: target.value?.roleId ? target.value.roleId : undefined,
          ...roleData,
        };
        request(
          params,
          createRoleModal,
          t("SUCCESS_ADD", [t("ROLE")]),
          t("ERROR_ADD", [t("ROLE")])
        );
      } else {
        toast.warning(t("WARNING_CHECK_ADD_MAPPING"));
      }
    },
    update: () => {
      let params = apiList["updateRole"];
      params.queryparam = {
        roleId: treeEditTarget.value.roleId,

        ...roleData,
      };
      request(
        params,
        updateRoleModal,
        t("SUCCESS_UPDATE", [t("ROLE")]),
        t("ERROR_UPDATE", [t("ROLE")])
      );
    },
    delete: () => {
      let params = apiList["deleteRole"];
      if (source.length === 1) {
        if (source[0].roleId) {
          // 멀티선택 안하게 하기 위해서 0번으로 처리함.
          const deleteArr = [];

          source.forEach((x) => {
            deleteArr.push({
              roleId: x.roleId,
            });
          });
          params.queryparam = { ...deleteArr[0] };
          request(
            params,
            deleteRoleModal,
            t("SUCCESS_REMOVE", [t("ROLE")]),
            t("ERROR_REMOVE", [t("ROLE")])
          );
        } else {
          toast.warning(t("WARNING_CHECK_REMOVE_MAPPING"));
        }
      } else {
        toast.warning(t("WARNING_MAPPING_ONLY_ONE"));
      }
    },
    move: () => {
      let params = apiList["moveRole"];
      if (source[0]?.roleId && target.value?.roleId) {
        const obj = {
          srcRoleIds: [],
          tgtRoleId: "",
        };

        obj.tgtRoleId = target.value.roleId;
        source.forEach((item) => {
          obj.srcRoleIds.push(item.roleId);
        });

        params.queryparam = obj;
        request(
          params,
          undefined,
          t("SUCCESS_MOVE", [t("ROLE")]),
          t("ERROR_MOVE", [t("ROLE")])
        );
      } else {
        toast.warning(t("WARNING_MAPPING_MOVE"));
      }
    },
    copy: () => {
      const params = apiList["copyRole"];
      if (source[0]?.roleId && target.value?.roleId) {
        const obj = {
          srcRoleIds: [],
          tgtRoleId: "",
        };

        obj.tgtRoleId = target.value.roleId;
        source.forEach((item) => {
          obj.srcRoleIds.push(item.roleId);
        });

        params.queryparam = obj;
        request(
          params,
          undefined,
          t("SUCCESS_COPY", [t("ROLE")]),
          t("ERROR_COPY", [t("ROLE")])
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
      treeKey.value++;
      toast.success(successText);
    } else {
      toast.error(errorText);
    }
  };
  const triggerAction = {
    createUserToRole: () => {
      let params = apiList["createUserToRole"];
      let treeTarget = TreeCompTgt.value.GetSelectedTarget();
      let checkDupli = true;

      let itemArr = [];
      if (treeTarget.value?.roleId && rowTarget.value.length > 0) {
        const checkValue = (target, item) => {
          let check = false;

          target.forEach((treeChildItem) => {
            if (treeChildItem.userId == item.userId) {
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
            itemArr.push(item.userId);
          }
        });
        if (itemArr.length > 0) {
          params.queryparam = {
            roleId: treeTarget.value.roleId,
            userIds: itemArr,
          };
          request(
            params,
            t("SUCCESS_MAPPING", [t("ROLE"), t("USER")]),
            t("ERROR_MAPPING", [t("ROLE"), t("USER")])
          );
        }
      } else {
        toast.warning(t("WARNING_MAPPING_SELECT"));
      }
    },
    deleteUserFromRole: () => {
      let deleteSource = TreeCompTgt.value.GetSelectedSource();
      let params = apiList["deleteUserFromRole"];
      if (deleteSource.length > 0) {
        const reduceArr = [];

        const arrForEach = (arr) => {
          if (arr.userId) {
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
            const parentRoleId = reduceArr[0].stats.getParent(
              reduceArr[0].id
            ).roleId;
            const userIds = reduceArr.map((item) => item.userId);
            params.queryparam = {
              roleId: parentRoleId,
              userIds: userIds,
            };
            request(
              params,
              t("SUCCESS_MAPPING_REMOVE", [t("ROLE"), t("USER")]),
              t("ERROR_MAPPING_REMOVE", [t("ROLE"), t("USER")])
            );
          } else {
            toast.warning(t('WARNING_SAME_PARENT'));
          }
        }
      } else {
        toast.warning(t("WARNING_MAPPING_REMOVE"));
      }
    },
  };
  triggerAction[method]();
};

onMounted(async () => {
  await getMappingData();
  getUserListEvent();
});

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
        Object.assign(roleData, roleTemp);
        createRoleModal.value = true;
      },
    },

    {
      methodName: "BTN_DELETE",

      title: computed(() => t("BTN_DELETE")),

      event: () => {
        deleteRoleModal.value = true;
      },
    },
    {
      methodName: "BTN_COPY",
      icon: "mdi-content-copy",

      title: computed(() => t("BTN_COPY")),

      event: () => {
        treeEvent("copy");
      },
    },
    {
      methodName: "BTN_MOVE",
      title: computed(() => t("BTN_MOVE")),
      icon: "mdi-file-move-outline",

      event: () => {
        treeEvent("move");
      },
    },
  ],
});
const userSearchBar = reactive({
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
        getUserListEvent();
      },
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",
      title: computed(() => t("BTN_SEARCH")),

      event: () => {
        getUserListEvent();
      },
    },
  ],
});
treeSearchBar.buttons = treeSearchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});
userSearchBar.buttons = userSearchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});
provide("treeSearchBar", treeSearchBar);
provide("userSearchBar", userSearchBar);
</script>

<template>
  <DefaultContainer>
    <v-col cols="12" lg="6">
      <SearchBarContainer :name="'treeSearchBar'" :breadcrumb="true">
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
          :height="treeHeight"
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
        @click.prevent="mappingEvent('createUserToRole')"
      ></v-btn>
      <v-btn
        v-if="
          route.meta.methods?.some(
            (item2) => 'BTN_DELETE_MAPPING' === item2.methodName
          )
        "
        variant="text"
        icon="mdi-arrow-right-bold"
        color="grey"
        @click.prevent="mappingEvent('deleteUserFromRole')"
      ></v-btn>
    </v-col>
    <v-col cols="12" lg="5">
      <SearchBarContainer :name="'userSearchBar'"></SearchBarContainer>
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
    :text="t('ROLE_ADD')"
    v-model="createRoleModal"
    @btnEvent="treeEvent('create')"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="" @submit.prevent="">
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('NAME')"
        v-model="roleData.roleName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    @btnEvent="treeEvent('update')"
    :text="t('ROLE_UPDATE')"
    v-model="updateRoleModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center" @submit.prevent="">
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('NAME')"
        v-model="roleData.roleName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
    </v-form>
  </ModalContainer>
  <ModalContainer
    :text="t('ROLE_REMOVE')"
    @btnEvent="treeEvent('delete')"
    v-model="deleteRoleModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
</template>
