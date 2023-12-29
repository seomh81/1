<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  InputContainer,
  ButtonContainer,
  Jodit,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";

import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const height = 64 + 64 + 16 + 64;
const tableHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = uuidv4();
const updateListModal = uuidv4();
const removeListModal = uuidv4();
const testModal = uuidv4();
const rowTarget = ref([]);
const tableData = ref([]);
const contentsDiv = ref();
const search = ref({
  searchKeyword: "",
  status: useUiStore.defaultStatus ? useUiStore.defaultStatus : 0,
});
const rules = ref({
  required: (value) => {
    return !!value || "입력이 필요합니다.";
  },
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
      title: t("STRING_NAME"),
      field: "POPUP_NM",
      sorter: "string",
    },
    {
      title: t("STRING_STATUS"),
      field: "statusString",
      sorter: "string",
    },
  ];
});
const apiList = {
  getList: {
    url: "/v1/ad/listpopupad",
    method: "get",
    queryparam: {
      searchKeyword: "",
      status: 0,
    },
  },
  addList: {
    url: "/v1/ad/addpopupad",
    method: "post",
    queryparam: {
      popupNm: "",
      contents: "",
    },
  },
  updateList: {
    url: "/v1/ad/updatepopupad",
    method: "post",
    queryparam: {
      popupAdId: "",
      popupNm: "",
    },
  },
  removeList: {
    url: "/v1/ad/removepopupad",
    method: "post",
    queryparam: {
      popupId: "",
    },
  },
};
const listTemp = {
  popupAdId: 0,
  popupNm: "",
  contents: "",
  status: 0,
};
const listData = reactive({});
Object.assign(listData, listTemp);
const popupContentsDiv = ref(null);
const form = ref();
const valid = ref(false);
const defaultStatus = computed(() => useUiStore.defaultStatus);
watch([rowTarget, defaultStatus, popupContentsDiv], (newVal, oldVal) => {
  if (!!rowTarget.value[0]?.POPUP_AD_ID == true) {
    contentsDiv.value.innerHTML = rowTarget.value[0].CONTENTS;
  } else {
    contentsDiv.value.innerHTML = "";
  }
  if (newVal[1] != oldVal[1]) {
    search.value.status = useUiStore.defaultStatus;
    if (!!tableData.value.length > 0) {
      tableData.value = [];
    }
  }
  if (popupContentsDiv.value && newVal[2] != oldVal[2]) {
    popupContentsDiv.value.innerHTML = rowTarget.value[0].CONTENTS;
  }
});

const getListEvent = async () => {
  let params = apiList["getList"];
  params.queryparam = {
    searchKeyword: search.value.searchKeyword,
    status: search.value.status.CODE
      ? search.value.status.CODE
      : search.value.status,
  };
  const { status, code, message, resultset } = await useConnectionStore.request(
    params
  );
  if (code == 200) {
    resultset.forEach((x) => {
      x.statusString = search.value.status.VALUE_1;
    });
    tableData.value = resultset;
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
  rowTarget.value = [];
};

const listEvent = async (method) => {
  const request = async (params) => {
    const { status, code, message, resultset } =
      await useConnectionStore.request(params);
    if (code == 200) {
      await getListEvent();
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
      let params = apiList["addList"];
      params.queryparam = { ...listData };
      await request(params);
    }
    if (method == "update") {
      let params = apiList["updateList"];
      params.queryparam = { ...listData };
      await request(params);
    }
  }

  if (method == "remove") {
    let params = apiList["removeList"];

    // params.queryparam = ;
    let removeArr = [];
    rowTarget.value.forEach((item) => {
      removeArr.push({
        popupAdId: item.POPUP_AD_ID,
      });
    });
    params.queryparam = [...removeArr];
    await request(params);
  }
};


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
  {
    cols: 3,
    type: "select",
    title: computed(() => t("STRING_STATUS")),

    items: useUiStore.statusItem ? useUiStore.statusItem : [0, 9],
    itemTitle: "value1",
    target: computed({
      get() {
        return search.value.status;
      },
      set(e) {
        search.value.status = e;
      },
    }),
  },
]);
const buttonItems = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      getListEvent();
    },
  },
  {
    title: computed(() => t("STRING_ADD")),
    icon: "mdi-magnify",
    event: () => {
      useUiStore.openModalEvent(addListModal);
      Object.assign(listData, listTemp);
    },
  },
  {
    title: computed(() => t("STRING_UPDATE")),
    icon: "mdi-magnify",
    disabled: computed(
      () => !(rowTarget.value[0]?.AD_ID && rowTarget.value.length == 1)
    ),

    event: () => {
      useUiStore.openModalEvent(updateListModal);
      listData.popupAdId = rowTarget.value[0].POPUP_AD_ID;
      listData.popupNm = rowTarget.value[0].POPUP_NM;
      listData.contents = rowTarget.value[0].CONTENTS;
    },
  },
  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",
    disabled: computed(() => !rowTarget.value.length >= 1),

    event: () => {
      useUiStore.openModalEvent(removeListModal);
    },
  },
]);
const secondButtonItems = ref([
  {
    title: computed(() => t("활성화")),
    event: () => {},
  },
  {
    title: computed(() => t("비활성화")),
    event: () => {},
  },
  {
    title: computed(() => t("테스트")),
    event: () => {},
  },
]);
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-1">
      <InputContainer
        :inputItems="inputItems"
        @update:inputChange="(value, item) => (item.target = value)"
        :buttonItems="buttonItems"
        :secondButtonItems="secondButtonItems"
      >
      </InputContainer>
    </v-col>
    <v-col cols="4">
      <v-card elevation="2" rounded="lg">

        <Tabulator
        :columns="columns"
        :tableData="tableData"
        :height="tableHeight"
        @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </v-card>
    </v-col>
    <v-col cols="8">
      <v-card elevation="2" :style="{ height: tableHeight }">
        <v-card-title>{{ rowTarget[0]?.POPUP_NM }}</v-card-title>
        <div class="content-box" ref="contentsDiv"></div>
      </v-card>
    </v-col>
  </DefaultContainer>

  <ModalContainer type="dialog" :text="t('STRING_AD_ADD')" :id="addListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_AD_NAME')"
        v-model="listData.popupNm"
        :rules="[rules.required]"
        clearable
      />

      <Jodit
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
      <v-btn class="mt-3" @click="listEvent('add')">{{
        t("STRING_ADD")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_AD_UPDATE')"
    :id="updateListModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_AD_NAME')"
        v-model="listData.popupNm"
        :rules="[rules.required]"
        clearable
      />
      <Jodit
        :contents="listData.contents"
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
      <v-btn @click="listEvent('update')">{{ t("STRING_SAVE") }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_AD_REMOVE')"
    :id="removeListModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="listEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
  <ModalContainer type="dialog" text="팝업 테스트" :id="testModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <div ref="popupContentsDiv"></div>
  </ModalContainer>
</template>

<style scoped>
.content-box {
  margin: 0;
  padding: 10px 15px;
  line-height: 1.2em;
}
</style>
