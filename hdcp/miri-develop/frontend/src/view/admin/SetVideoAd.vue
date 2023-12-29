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
  Jodit,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { userStore } from "@/store/UserStore";
import { useRoute } from "vue-router";
import { uiStore } from "@/store/UiStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const route = useRoute();
const height = 64 + 64 + 12 + 48;
const tableHeight = `calc(100vh - ${height}px)`;
const useUserStore = userStore();
const popupContentsDiv = ref(null);
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = ref(false);
const updateListModal = ref(false);
const removeListModal = ref(false);
const rowTarget = ref([]);
const testModal = ref(false);
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
      title: t("AD_NAME"),
      field: "title",
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
    url: "/v2/ad/video/list",
    method: "get",
    queryparam: {},
  },
  create: {
    url: "/v2/ad/video/create",
    method: "post",
    queryparam: {},
  },
  update: {
    url: "/v2/ad/video/update",
    method: "post",
    queryparam: {},
  },
  remove: {
    url: "/v2/ad/video/remove",
    method: "post",
    queryparam: {},
  },
  active: {
    url: "/v2/ad/video/active",
    method: "post",
    queryparam: {},
  },
  inactive: {
    url: "/v2/ad/video/inactive",
    method: "post",
    queryparam: {},
  },
};
const listTemp = {
  title: "",
  contents: "",
};
const listData = reactive({});
Object.assign(listData, listTemp);
const userLocale = computed(() => useUserStore?.userInfo?.locale);
const activationItem = ref([]);

const form = ref();
const valid = ref(false);

watch([rowTarget, userLocale, popupContentsDiv], (newVal, oldVal) => {
  if (!!rowTarget.value[0]?.videoAdId == true) {
    if (!!contentsDiv.value == true) {
      contentsDiv.value.innerHTML = rowTarget.value[0].contents;
    }
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
      );    },
    update: () => {
      let params = apiList["update"];
      params.queryparam = {
        videoAdId: rowTarget.value[0].videoAdId,
        ...listData,
      };
      request(params, updateListModal,
      t('SUCCESS_UPDATE', [t('AD')]),
        t('ERROR_UPDATE', [t('AD')]),

      );    },
    remove: () => {
      let params = apiList["remove"];

      let removeObj = {
        videoAdIds: [],
      };
      rowTarget.value.forEach((item) => {
        removeObj.videoAdIds.push(item.videoAdId);
      });
      params.queryparam = removeObj;
      request(params, removeListModal,
      t('SUCCESS_REMOVE', [t('AD')]),
        t('ERROR_REMOVE', [t('AD')]),
);    },
    active: () => {
      let params = apiList["active"];
      let obj = {
        videoAdIds: [],
      };
      if (
        tableData.value.every((item) => item.activationYn == "n") &&
        rowTarget.value.length === 1
      ) {
        rowTarget.value.forEach((item) => {
          obj.videoAdIds.push(item.videoAdId);
        });
        params.queryparam = obj;
        request(params, undefined,
        t('SUCCESS_ACTIVE'),
        t('ERROR_ACTIVE'),

        );      } else {
          toast.warning(t('WARNING_ACTIVE_ONLY_ONE'));
      }
    },
    inactive: () => {
      let params = apiList["inactive"];
      let obj = {
        videoAdIds: [],
      };
      rowTarget.value.forEach((item) => {
        obj.videoAdIds.push(item.videoAdId);
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
        Object.assign(listData, listTemp);
        addListModal.value = true;
      },
    },
    {
      methodName: "BTN_UPDATE",

      title: computed(() => t("BTN_UPDATE")),

      disabled: computed(
        () => !(rowTarget.value[0]?.videoAdId && rowTarget.value.length == 1)
      ),

      event: () => {
        listData.title = rowTarget.value[0].title;
        listData.contents = rowTarget.value[0].contents;
        updateListModal.value = true;
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
      <SearchBarContainer
        @update:inputChange="(value, item) => (item.target = value)"
      >
      </SearchBarContainer>
    </v-col>
    <v-col cols="4" class="pt-0">
      <Skeleton :loadingData="tableData.length === 0" :height="tableHeight">
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
        :loadingData="!rowTarget[0]?.title"
        :height="tableHeight"
        type="heading,image,text,paragraph,text,sentences,text"
      >
        <v-toolbar color="transparent" density="compact">
          <v-toolbar-title>{{ rowTarget[0]?.title }}</v-toolbar-title>
        </v-toolbar>
        <div class="pa-4" ref="contentsDiv"></div>
      </Skeleton>
    </v-col>
  </DefaultContainer>

  <ModalContainer
    :text="t('AD_ADD')"
    :width="'600px'"
    v-model="addListModal"
    @btnEvent="callAPIs('create')"
  >
    <v-form v-model="valid" ref="form" @submit.prevent="">
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('AD_NAME')"
        v-model="listData.title"
        :rules="[useUiStore.rules.required]"
        clearable
      />

      <Jodit
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    :width="'600px'"
    :text="t('AD_UPDATE')"
    v-model="updateListModal"
    @btnEvent="callAPIs('update')"
  >
    <v-form v-model="valid" ref="form" @submit.prevent="">
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('AD_NAME')"
        v-model="listData.title"
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
    v-model="removeListModal"
    @btnEvent="callAPIs('remove')"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
  </ModalContainer>
  <ModalContainer type="dialog" text="팝업 테스트" v-model="testModal" :width="'600px'">
    <div ref="popupContentsDiv"></div>
  </ModalContainer>
</template>

<style scoped></style>
