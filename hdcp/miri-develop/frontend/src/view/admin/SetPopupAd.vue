<script setup>
import { ref, reactive, computed, onMounted, watch, provide } from "vue";
import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  SearchBarContainer,
  Jodit,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";

import {  useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const height = 64 + 64 + 12 + 48;
const tableHeight = `calc(100vh - ${height}px)`;
const useUserStore = userStore();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = ref(false);
const updateListModal = ref(false);
const removeListModal = ref(false);
const testModal = ref(false);
const route = useRoute();
const rowTarget = ref([]);
const tableData = ref([]);
const contentsDiv = ref(null);
const search = ref({
  searchKeyword: "",
  activation: "",
});

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
      title: t("NAME"),
      field: "popupName",
      sorter: "string",
    },
    {
      title: t("STATUS"),
      field: "activationYnString",
      sorter: "string",
    },
  ];
});
const apiList = {
  get: {
    url: "/v2/ad/popup/list",
    method: "get",
    queryparam: {
      searchKeyword: "",
      status: 0,
    },
  },
  create: {
    url: "/v2/ad/popup/create",
    method: "post",
    queryparam: {
      popupName: "",
      contents: "",
    },
  },
  update: {
    url: "/v2/ad/popup/update",
    method: "post",
    queryparam: {
      popupAdId: "",
      popupName: "",
    },
  },
  remove: {
    url: "/v2/ad/popup/remove",
    method: "post",
    queryparam: {
      popupId: "",
    },
  },
  active: {
    url: "/v2/ad/popup/active",
    method: "post",
    queryparam: {},
  },
  inactive: {
    url: "/v2/ad/popup/inactive",
    method: "post",
    queryparam: {},
  },
};
const listTemp = {
  popupName: "",
  contents: "",
};
const listData = reactive({});
Object.assign(listData, listTemp);
const popupContentsDiv = ref(null);
const form = ref();
const valid = ref(false);
const userLocale = computed(() => useUserStore?.userInfo?.locale);
const activationItem = ref([]);

watch([rowTarget, userLocale, popupContentsDiv], (newVal, oldVal) => {
  if (!!rowTarget.value[0]?.popupAdId == true) {
    if (!!contentsDiv.value == true)
      contentsDiv.value.innerHTML = rowTarget.value[0].contents;
  } else {
    if (!!contentsDiv.value == true) contentsDiv.value.innerHTML = "";
  }
  if (newVal[1] != oldVal[1]) {
    getActivation();
  }
  if (popupContentsDiv.value && newVal[2] != oldVal[2]) {
    popupContentsDiv.value.innerHTML = rowTarget.value[0].contents;
  }
});
const getActivation = async () => {
  const data = await useUiStore.getActivation();
  search.value.activation = data.defaultActivation;
  activationItem.value = data.activationItem;
};
const getListEvent = async () => {
  let params = apiList["get"];
  params.queryparam = {
    searchKeyword: search.value.searchKeyword,
    activationYn: search.value.activation.code
      ? search.value.activation.code
      : search.value.activation,
  };
  const { because, code, data, message, result } =
    await useConnectionStore.request(params);

  if (code == 200) {
    if (data.length > 0) {
      if (!!activationItem.value == true) {
        data.filter((x) => {
          activationItem.value.some((item) => {
            if (x.activationYn === item.code) {
              x.activationYnString = item.value1;
            }
          });
        });
      } else {
        data.filter((x) => {
          x.activationYnString = x.activationYn;
        });
      }
    } else {
      toast.warning(t('NO_DATA_SEARCH',[t('AD')]));
    }
    tableData.value = data;
  } else {
    toast.error(t('ERROR_SEARCH',[t('AD')]));
  }
  rowTarget.value = [];
};

const callAPIs = async (method) => {
  const request = async (params, modal,successText,errorText) => {
    const { status, code, message, data, because } =
      await useConnectionStore.request(params);
    if (code == 200) {
      if (modal) {
        modal.value = false;
      }
      toast.success(successText);
      await getListEvent();
    } else {
      toast.error(errorText);
    }
  };

  const triggerAction = {
    create: () => {
      let params = apiList["create"];
      params.queryparam = { ...listData };
      request(params, addListModal,
        t('SUCCESS_ADD', [t('AD')]),
        t('ERROR_ADD', [t('AD')]),
      );
    },
    update: () => {
      let params = apiList["update"];
      params.queryparam = {
        popupAdId: rowTarget.value[0].popupAdId,
        ...listData,
      };
      request(params, updateListModal,
      t('SUCCESS_UPDATE', [t('AD')]),
        t('ERROR_UPDATE', [t('AD')]),

      );
    },
    remove: () => {
      let params = apiList["remove"];

      let removeObj = {
        popupAdIds: [],
      };
      rowTarget.value.forEach((item) => {
        removeObj.popupAdIds.push(item.popupAdId);
      });
      params.queryparam = removeObj;
      request(params, removeListModal,
      t('SUCCESS_REMOVE', [t('AD')]),
        t('ERROR_REMOVE', [t('AD')]),
);
    },
    active: () => {
      let params = apiList["active"];
      let obj = {
        popupAdIds: [],
      };
      if (
        tableData.value.every((item) => item.activationYn == "n") &&
        rowTarget.value.length === 1
      ) {
        rowTarget.value.forEach((item) => {
          obj.popupAdIds.push(item.popupAdId);
        });
        params.queryparam = obj;
        request(params, undefined,
        t('SUCCESS_ACTIVE'),
        t('ERROR_ACTIVE'),

        );
      } else {
        toast.warning(t('WARNING_ACTIVE_ONLY_ONE'));
      }
    },
    inactive: () => {
      let params = apiList["inactive"];
      let obj = {
        popupAdIds: [],
      };
      rowTarget.value.forEach((item) => {
        obj.popupAdIds.push(item.popupAdId);
      });
      params.queryparam = obj;
      request(params, undefined, t("SUCCESS_INACTIVE"), t("ERROR_INACTIVE"));
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
        getListEvent();
      },
    },
    {
      cols: 3,
      type: "select",
      title: computed(() => t("STATUS")),
      items: computed(() =>
        activationItem.value ? activationItem.value : ["ALL", "y", "n"]
      ),
      itemTitle: "value1",
      target: computed({
        get() {
          return search.value.activation;
        },
        set(e) {
          search.value.activation = e;
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

      event: () => {
        addListModal.value = true;
        Object.assign(listData, listTemp);
      },
    },
    {
      methodName: "BTN_UPDATE",

      title: computed(() => t("BTN_UPDATE")),

      disabled: computed(
        () => !(rowTarget.value[0]?.popupAdId && rowTarget.value.length == 1)
      ),
      event: () => {
        updateListModal.value = true;
        listData.popupName = rowTarget.value[0].popupName;
        listData.contents = rowTarget.value[0].contents;
      },
    },
    {
      methodName: "BTN_DELETE",

      title: computed(() => t("BTN_DELETE")),

      disabled: computed(() => !rowTarget.value.length >= 1),
      event: () => {
        removeListModal.value = true;
      },
    },
  ],
  secondButtons: [
    {
      methodName: "BTN_ACTIVE",

      title: computed(() => t("BTN_ACTIVE")),
      icon: "mdi-lightbulb-on-outline",
      disabled: computed(() => !rowTarget.value.length >= 1),
      event: () => {
        callAPIs("active");
      },
    },
    {
      methodName: "BTN_INACTIVE",

      title: computed(() => t("BTN_INACTIVE")),
      icon: "mdi-lightbulb-off-outline",
      disabled: computed(() =>
        rowTarget.value.length == 1 &&
        rowTarget.value &&
        rowTarget.value[0].activationYn == "y"
          ? false
          : true
      ),
      event: () => {
        callAPIs("inactive");
      },
    },
    {
      methodName: "BTN_TEST",

      title: computed(() => t("BTN_TEST")),

      disabled: computed(() => !rowTarget.value.length >= 1),
      event: () => {
        testModal.value = true;
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
onMounted(async () => {
  await getActivation();
  await getListEvent();
});
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="4" class="pt-0">
      <Skeleton :loadingData="tableData.length == 0" :height="tableHeight">
        <Tabulator
          :columns="columns"
          :pagination="true"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </Skeleton>
    </v-col>
    <v-col cols="8" class="pt-0">
      <Skeleton
        :loadingData="!rowTarget[0]?.popupName"
        :height="tableHeight"
        type="heading,image,text,paragraph,text,sentences,text"
      >
        <v-toolbar color="transparent" density="compact">
          <v-toolbar-title>{{ rowTarget[0]?.popupName }}</v-toolbar-title>
        </v-toolbar>
        <div class="pa-4" ref="contentsDiv"></div>
      </Skeleton>
    </v-col>
  </DefaultContainer>

  <ModalContainer
    :text="t('AD_ADD')"
    v-model="addListModal"
    @btnEvent="callAPIs('create')"
  >
    <v-form v-model="valid" ref="form"  @submit.prevent="">
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('AD_NAME')"
        v-model="listData.popupName"
        :rules="[useUiStore.rules.required]"
        clearable
      />

      <Jodit
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    :text="t('AD_UPDATE')"
    v-model="updateListModal"
    @btnEvent="callAPIs('update')"
  >
    <v-form v-model="valid" ref="form" @submit.prevent="" >
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('AD_NAME')"
        v-model="listData.popupName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <Jodit
        :contents="listData.contents"
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    :text="t('AD_REMOVE')"
    :width="'auto'"
    v-model="removeListModal"
    @btnEvent="callAPIs('remove')"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
  <ModalContainer type="dialog" :text="t('AD_TEST')" v-model="testModal">
    <div ref="popupContentsDiv"></div>
  </ModalContainer>
</template>

<style scoped></style>
