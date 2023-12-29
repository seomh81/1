<script setup>
import {
  ref,
  reactive,
  computed,
  onMounted,
  provide,
} from "vue";
import {
  SearchBarContainer,
  DefaultContainer,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { TheTreeComp } from "@daiahub/thetreecomp";
import { useI18n } from "vue-i18n";
import {  useRoute } from "vue-router";
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
  getRoleMenuMap: {
    url: "/v2/privilege/policy/role/menus",
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

  createMenuToRole: {
    url: "/v2/privilege/policy/role/menus/create",
    method: "post",
    queryparam: {},
  },
  deleteMenuFromRole: {
    url: "/v2/privilege/policy/role/menus/delete",
    method: "post",
    queryparam: {},
  },
  getMenuViewMap: {
    url: "/v2/privilege/policy/menu/views",
    method: "get",
    queryparam: {},
  },
};

const useConnectionStore = connectionStore();
const treeRoleData = ref([]);
const treeMenuData = ref([]);
const TreeCompSrc = ref();
const TreeCompTgt = ref();
const canEdit = ref(true);
const expandAll = ref(true);
const treeEditTarget = ref([]);
const treeKey = ref(0);

const roleTemp = {
  roleName: "",
  type: "role",
};
const roleData = reactive({});
Object.assign(roleData, roleTemp);


const sendRequest = async (params, i) => {
  return await useConnectionStore.request(params);
};

const getRoleMappingData = async () => {

  const createDataTree = (dbData) => {
    let rtnVal = {};
    let treeData = [];
    dbData.forEach((data) => {
      rtnVal[data.treeId] = {
        name: data.roleName ? data.roleName : data.menuName,
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
    apiList["getRoleMenuMap"]
  );
  if (code == 200) {
    if (data.length > 0) {
      treeRoleData.value = createDataTree(data);
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("ROLE")]));
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("ROLE")]));

  }
};
const getMenuMappingData = async () => {

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
      treeMenuData.value = createDataTree(data);
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU")]));
    }
  } else {
    toast.error(t("ERROR_SEARCH", [t("MENU")]));

  }
};


const mappingEvent = async (method) => {
  const request = async (params, successText,errorText) => {

      const { status, code, message, data, because } = await sendRequest(
        params
      );
      if (code == 200) {
        await getRoleMappingData();
        treeKey.value++;
        toast.success(successText);
      } else {
        toast.error(errorText);
      }
  
  };
  const triggerAction = {
    createMenuToRole: () => {
      let params = apiList["createMenuToRole"];
      const menuSource = TreeCompSrc.value?.GetSelectedSource();
      let roleTarget = TreeCompTgt.value?.GetSelectedTarget();
      let checkDupli = true;

      let itemArr = [];
      let obj = {
        roleId: "",
        menuIds: [],
      };
      if (roleTarget.value?.roleId && menuSource.length > 0) {
        obj.roleId = roleTarget.value.roleId;
        menuSource.forEach((item) => {
          obj.menuIds.push(item.menuId);
        });
        params.queryparam = obj;
        request(params,
        t('SUCCESS_MAPPING', [t('ROLE'),t('MENU')]),
          t('ERROR_MAPPING',[t('ROLE'),t('MENU')])
        );
      } else {
        
        toast.warning(t("WARNING_MAPPING_SELECT"));
      }
    },
    deleteMenuFromRole: () => {
      let params = apiList["deleteMenuFromRole"];
      const deleteSource = TreeCompTgt.value.GetSelectedSource();
      if (deleteSource.length > 0) {
        const reduceArr = [];

        const arrForEach = (arr) => {
          if (arr.menuId && !!arr.parentMenuId == false) {
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
            const parentId = reduceArr[0].stats.getParent(
              reduceArr[0].id
            ).roleId;
            const menuIds = reduceArr.map((item) => item.menuId);
            params.queryparam = {
              roleId: parentId,
              menuIds,
            };
            request(params,
            t('SUCCESS_MAPPING_REMOVE', [t('ROLE'),t('MENU')]),
          t('ERROR_MAPPING_REMOVE',[t('ROLE'),t('MENU')])
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

onMounted(() => {
  getRoleMappingData();
  getMenuMappingData();
});

const treeSearchBar = reactive({
  buttons: [
    {
      methodName: "BTN_SEARCH",
      title: computed(() => t("BTN_SEARCH")),
  
      event: () => {
        getRoleMappingData();
      },
    },
  ],
});

const menuSearchBar = reactive({
  buttons: [
    {
      methodName: "BTN_SEARCH",
      title: computed(() => t("BTN_SEARCH")),
  
      event: () => {
        getMenuMappingData();
      },
    },
  ],
});

treeSearchBar.buttons = treeSearchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});
menuSearchBar.buttons = menuSearchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});
provide("menuSearchBar", menuSearchBar);
provide("treeSearchBar", treeSearchBar);
</script>

<template>
  <DefaultContainer>
    <v-col cols="12" lg="6">
      <SearchBarContainer :breadcrumb="true" :name="'treeSearchBar'">
      </SearchBarContainer>

      <Skeleton
        :padding="true"
        :loadingData="treeRoleData.length == 0"
        :height="'calc(100vh - 140px)'"
        :type="'list-item-three-line,list-item-three-line,list-item-three-line,list-item-three-line,list-item-three-line,list-item-three-line,list-item-three-line,list-item-three-line,list-item-three-line'"
      >
        <TheTreeComp
          :hideChildMenuCheck="true"
          ref="TreeCompTgt"
          elementid="MainTree"
          :items="treeRoleData"
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
    >      <v-btn
        variant="text"
        v-if="
          route.meta.methods?.some(
            (item2) => 'BTN_CREATE_MAPPING' === item2.methodName
          )
        "
        icon="mdi-arrow-left-bold"
        color="grey"
        @click.prevent="mappingEvent('createMenuToRole')"
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
        @click.prevent="mappingEvent('deleteMenuFromRole')"
      ></v-btn>
    </v-col>
    <v-col cols="12" lg="5">
      <SearchBarContainer :name="'menuSearchBar'"></SearchBarContainer>
      <Skeleton
        :loadingData="treeMenuData.length == 0"
        :height="'calc(100vh - 140px)'"
        :type="'list-item-three-line,list-item-three-line,list-item-three-line'"
      >
        <TheTreeComp
          :useOnlyRootCheck="true"
          ref="TreeCompSrc"
          elementid="SubTree"
          :items="treeMenuData"
          :canEdit="canEdit"
          :expandAll="expandAll"
          :key="treeKey"
          @update:onGoingEdit="(returnVal) => (treeEditTarget = returnVal)"
        >
        </TheTreeComp>
      </Skeleton>
    </v-col>
  </DefaultContainer>
</template>
