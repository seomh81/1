<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  DefaultContainer,
  InputContainer,
  Tabulator,
  ModalContainer,
  ButtonContainer,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const i18n = useI18n();
const { t } = useI18n();
const useUserStore = userStore();
const useUiStore = uiStore();
const addHeadModal = uuidv4();
const updateHeadModal = uuidv4();
const deleteHeadModal = uuidv4();
const addDetailModal = uuidv4();
const updateDetailModal = uuidv4();
const deleteDetailModal = uuidv4();

const height = 64 + 64 + 16 + 46 + 24;
// const masterHeight = 48 + 64 + 16;
// 48=header, 64 최상단 search & button  / 16 컨테이너 bottom 패딩 / 4 어디서 나왔는지 모름. no scroll
// const detailHeight = masterHeight + 46 ;
const tableHeight = `calc(100vh - ${height}px)`;
const searchList = ref({
  searchKeyword: "",
  locale: useUiStore.defaultLocale ? useUiStore.defaultLocale : "ko_kr",
});
const masterDataApi = {
  getHead: {
    url: "/v2/masterdata/head/list",
    method: "get",
    queryparam: {},
  },
  addHead: {
    url: "/v2/masterdata/head/create",
    method: "post",
    queryparam: {},
  },
  updateHead: {
    url: "/v2/masterdata/head/update",
    method: "post",
    queryparam: {},
  },
  deleteHead: {
    url: "/v2/masterdata/head/delete",
    method: "post",
    queryparam: {},
  },
  getDetail: {
    url: "/v2/masterdata/detail/list",
    method: "get",
    queryparam: {},
  },
  addDetail: {
    url: "/v2/masterdata/detail/create",
    method: "post",
    queryparam: {},
  },
  updateDetail: {
    url: "/v2/masterdata/detail/update",
    method: "post",
    queryparam: {},
  },
  deleteDetail: {
    url: "/v2/masterdata/detail/delete",
    method: "post",
    queryparam: {},
  },
};
let headColumn = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      width: "70",
      headerSort: false,
      headerHozAlign: "center",
      hozAlign: "center",
    },
    {
      title: t("STRING_MASTER_KEY"),
      field: "masterdataKey",
      sorter: "string",
    },
    {
      title: t("STRING_MASTER_NAME"),
      field: "masterdataNm",
      sorter: "string",
    },
  ];
});

const detailColumns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      hozAlign: "center",
      width: "70",
      headerHozAlign: "center",
      headerSort: false,
    },
    {
      title: t("STRING_MASTER_CODE"),
      field: "code",
      sorter: "string",
      minWidth: 120,
    },
    {
      title: t("STRING_MASTER_VALUE1"),
      field: "value1",
      sorter: "string",
      minWidth: 120,
    },
    {
      title: t("STRING_MASTER_VALUE2"),
      field: "value2",
      sorter: "string",
      minWidth: 120,
    },
    {
      title: t("STRING_MASTER_VALUE3"),
      field: "value3",
      sorter: "string",
      minWidth: 120,
    },
    {
      title: t("STRING_MASTER_VALUE4"),
      field: "value4",
      sorter: "string",
      minWidth: 120,
    },
    {
      title: t("STRING_MASTER_VALUE5"),
      field: "value5",
      sorter: "string",
      minWidth: 120,
      hozAlign: "center",
      vertAlign: "middle",
    },
    {
      title: t("STRING_SORT"),
      field: "sortSeq",
      sorter: "number",
      minWidth: 120,
    },
  ];
});
const useConnectionStore = connectionStore();
const defaultLocale = computed(() => useUiStore.defaultLocale);
const rowTargetHeadData = ref([]);
const rowTargetDetailData = ref([]);

const headTableData = ref([]);
const detailTableData = ref([]);
const form = ref();
const valid = ref(false);

let headDataTemp = {
  masterdataKey: "",
  masterdataNm: "",
  locale: "",
  note: "",
};
const headData = reactive({});

let detailDataTemp = {
  // masterdataId: 1,
  code: "",
  value1: "",
  value2: "",
  value3: "",
  value4: "",
  value5: "",
  sortSeq: 0,
};
const detailData = reactive({});

const rules = ref({
  required: (value) => {
    return !!value || "입력이 필요합니다.";
  },
});

const sendRequest = async (params, i) => {
  return await useConnectionStore.request(params);
};

watch([rowTargetHeadData, defaultLocale], (newVal, oldVal) => {
  if (
    !!rowTargetHeadData.value[0]?.masterdataId == true &&
    newVal[0] !== oldVal[0]
  ) {
    getDetailEvent();
  }

  if (newVal[1] != oldVal[1]) {
    if (headTableData.value.length > 0) {
      headTableData.value = [];
      detailTableData.value = [];
    }
    searchList.value.locale = useUiStore.defaultLocale;
  }
});
const openUpdateHeadModal = () => {
  Object.assign(headData, headDataTemp);

  headData.masterdataId = rowTargetHeadData?.value[0].masterdataId;
  headData.masterdataKey = rowTargetHeadData?.value[0].masterdataKey;
  headData.masterdataNm = rowTargetHeadData?.value[0].masterdataNm;
  headData.note = rowTargetHeadData?.value[0].note;
  headData.locale = useUiStore.localeItem?.find((x) => {
    return x.code == rowTargetHeadData?.value[0].locale;
  });

  useUiStore.openModalEvent(updateHeadModal);
};

const getHeadEvent = async () => {
  console.log("hi");
  let params = masterDataApi["getHead"];
  params.queryparam = {
    searchKeyword: searchList.value?.searchKeyword
      ? searchList.value.searchKeyword
      : undefined,
    userLocale: searchList.value.locale?.code
      ? searchList.value.locale?.code
      : "ko_kr",
  };
  const { because, code, data, message, result } = await sendRequest(params);
  if (code == 200) {
    headTableData.value = data;
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }

  rowTargetHeadData.value = [];
  detailTableData.value = [];
};
const headEvent = async (method) => {
  const request = async (params) => {
    const { because, code, data, message, result } = await sendRequest(params);
    if (code == 200) {
      useUiStore.closeModal();
      await getHeadEvent();
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
      let params = masterDataApi["addHead"];
      params.queryparam = {
        ...headData,
      };
      await request(params);
    }

    if (method == "update") {
      let params = masterDataApi["updateHead"];
      params.queryparam = { ...headData };
      params.queryparam.locale = headData.locale.code;
      await request(params);
    }
  }

  if (method == "remove") {
    let params = masterDataApi["deleteHead"];

    // params.queryparam = ;
    let removeArr = [];
    rowTargetHeadData.value.forEach((item) => {
      removeArr.push({
        masterdataId: item.masterdataId,
      });
    });
    params.queryparam = [...removeArr];
    await request(params);
  }
};
const getDetailEvent = async () => {
  let params = masterDataApi["getDetail"];
  params.queryparam = {
    masterdataId: rowTargetHeadData.value[0].masterdataId,
  };
  const { because, code, data, message, result } = await sendRequest(params);

  if (code == 200) {
    detailTableData.value = data;
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
  rowTargetDetailData.value = [];
};

const openUpdateDetailModal = () => {
  useUiStore.openModalEvent(updateDetailModal);

  detailData.code = rowTargetDetailData?.value[0].code;
  detailData.value1 = rowTargetDetailData?.value[0].value1;
  detailData.value2 = rowTargetDetailData?.value[0].value2;
  detailData.value3 = rowTargetDetailData?.value[0].value3;
  detailData.value4 = rowTargetDetailData?.value[0].value4;
  detailData.value5 = rowTargetDetailData?.value[0].value5;
  detailData.sortSeq = rowTargetDetailData?.value[0].sortSeq;
};

const detailEvent = async (method) => {
  const request = async (params) => {
    const { status, code, message, resultset } = await sendRequest(params);
    if (code == 200) {
      await getDetailEvent();
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
      let params = masterDataApi["addDetail"];
      console.log(detailData);
      params.queryparam = {
        masterdataId: rowTargetHeadData.value[0].masterdataId,
        ...detailData,
      };
      await request(params);
    }

    if (method == "update") {
      let params = masterDataApi["updateDetail"];

      params.queryparam = {
        masterdataDetailId: rowTargetDetailData?.value[0].masterdataDetailId,
        ...detailData,
      };

      // params.queryparam = {
      //   masterdataDetailId: detailData.masterdataDetailId,
      //   code: detailData.code,
      //   value1: detailData.value1,
      //   value2: detailData.value2,
      //   value3: detailData.value3,
      //   value4: detailData.value4,
      //   value5: detailData.value5,
      //   sortSeq: detailData.sortSeq,
      // };

      await request(params);
    }
  }
  if (method == "remove") {
    let params = masterDataApi["deleteDetail"];

    // params.queryparam = ;
    let removeArr = [];
    rowTargetDetailData.value.forEach((item) => {
      removeArr.push({
        // masterdataId: item.masterdataId,
        masterdataDetailId: item.masterdataDetailId,
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
        return searchList.value.searchKeyword;
      },
      set(e) {
        searchList.value.searchKeyword = e;
      },
    }),
  },
  {
    cols: 3,
    title: computed(() => t("STRING_LOCALE")),
    type: "select",
    items: useUiStore.localeItem ? useUiStore.localeItem : ["ko_kr", "en_gn"],
    itemTitle: "value1",
    target: computed({
      get() {
        return searchList.value.locale;
      },
      set(e) {
        searchList.value.locale = e;
      },
    }),
  },
]);
const buttonItems = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      getHeadEvent();
    },
  },
  {
    title: computed(() => t("STRING_ADD")),
    icon: "mdi-magnify",
    event: () => {
      headDataTemp.locale = searchList.value.locale.code;
      useUiStore.openModalEvent(addHeadModal);
      Object.assign(headData, headDataTemp);
    },
  },
  {
    title: computed(() => t("STRING_UPDATE")),
    icon: "mdi-magnify",
    disabled: computed(
      () =>
        !(
          rowTargetHeadData.value[0]?.masterdataId &&
          rowTargetHeadData.value.length == 1
        )
    ),

    event: () => {
      openUpdateHeadModal();
    },
  },
  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",
    disabled: computed(() => !rowTargetHeadData.value.length >= 1),

    event: () => {
      useUiStore.openModalEvent(deleteHeadModal);
    },
  },
]);
const secondButtonItems = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    disabled: computed(() => !rowTargetHeadData.value[0]?.masterdataId),

    event: () => {},
  },
  {
    title: computed(() => t("STRING_ADD")),
    icon: "mdi-magnify",
    disabled: computed(() => !rowTargetHeadData.value[0]?.masterdataId),

    event: () => {
      useUiStore.openModalEvent(addDetailModal);
      Object.assign(detailData, detailDataTemp);
    },
  },
  {
    title: computed(() => t("STRING_UPDATE")),
    icon: "mdi-magnify",
    disabled: computed(
      () =>
        !(
          rowTargetDetailData.value[0]?.masterdataDetailId &&
          rowTargetDetailData.value.length == 1
        )
    ),
    event: () => {
      openUpdateDetailModal();
    },
  },
  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",
    disabled: computed(() => !rowTargetDetailData.value.length >= 1),

    event: () => {
      useUiStore.openModalEvent(deleteDetailModal);
    },
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
          :columns="headColumn"
          :tableName="'table'"
          :tableData="headTableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTargetHeadData = returnVal)"
        />
      </v-card>
    </v-col>
    <v-col cols="8">
      <v-card elevation="2" rounded="lg">
        <Tabulator
          :columns="detailColumns"
          :tableName="'table'"
          :tableData="detailTableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTargetDetailData = returnVal)"
        />
      </v-card>
    </v-col>
  </DefaultContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_MASTER_LIST_ADD')"
    :id="addHeadModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_KEY')"
        v-model="headData.masterdataKey"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_NAME')"
        v-model="headData.masterdataNm"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NOTE')"
        v-model="headData.note"
        :rules="[rules.required]"
        clearable
      />
      <v-select
        density="compact"
        return-object
        hide-details
        variant="outlined"
        :items="
          useUiStore.localeItem ? useUiStore.localeItem : ['ko_kr', 'en_gn']
        "
        item-title="value1"
        v-model="headData.locale"
        :label="t('STRING_LOCALE')"
      >
      </v-select>
      <v-btn class="mt-3" @click="headEvent('add')">
        {{ t("STRING_SAVE") }}
      </v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_MASTER_LIST_UPDATE')"
    :id="updateHeadModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_KEY')"
        v-model="headData.masterdataKey"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_NAME')"
        v-model="headData.masterdataNm"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NOTE')"
        v-model="headData.note"
        :rules="[rules.required]"
        clearable
      />
      <v-btn class="mt-3" @click="headEvent('update')">
        {{ t("STRING_SAVE") }}
      </v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_MASTER_LIST_REMOVE')"
    :id="deleteHeadModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="headEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_MASTER_DETAIL_ADD')"
    :id="addDetailModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_CODE')"
        v-model="detailData.code"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE1')"
        v-model="detailData.value1"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE2')"
        v-model="detailData.value2"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE3')"
        v-model="detailData.value3"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE4')"
        v-model="detailData.value4"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE5')"
        v-model="detailData.value5"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_SORT')"
        v-model="detailData.sortSeq"
        clearable
      />
      <v-btn class="mt-3" @click="detailEvent('add')">
        {{ t("STRING_SAVE") }}
      </v-btn>
    </v-form>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_MASTER_DETAIL_UPDATE')"
    :id="updateDetailModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form ref="form" v-model="valid" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_CODE')"
        v-model="detailData.code"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE1')"
        v-model="detailData.value1"
        :rules="[rules.required]"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE2')"
        v-model="detailData.value2"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE3')"
        v-model="detailData.value3"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE4')"
        v-model="detailData.value4"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_MASTER_VALUE5')"
        v-model="detailData.value5"
        clearable
      />
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_SORT')"
        v-model="detailData.sortSeq"
        clearable
      />
      <v-btn class="mt-3" @click="detailEvent('update')">
        {{ t("STRING_SAVE") }}
      </v-btn>
    </v-form>
  </ModalContainer>
  <ModalContainer
    type="dialog"
    :text="t('STRING_MASTER_DETAIL_REMOVE')"
    :id="deleteDetailModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="detailEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
</template>
