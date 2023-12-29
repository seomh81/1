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
const height = 64 + 64 + 16 + 22;
// 48=header, 64 최상단 search & button  / 16 컨테이너 bottom 패딩 / 4 어디서 나왔는지 모름. no scroll
const tableHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = uuidv4();
const updateListModal = uuidv4();
const removeListModal = uuidv4();
const rowTarget = ref([]);
const tableData = ref([]);
const contentsDiv = ref(null);
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
      title: "장비구분",
      field: "TITLE",
      sorter: "string",
    },
    {
      title: "건물명",
      field: "statusString",
      sorter: "string",
    },
    {
      title: "호기번호",
      field: "statusString",
      sorter: "string",
    },
    {
      title: "보고서 종류",
      field: "statusString",
      sorter: "string",
    },
    {
      title: "점검일자",
      field: "statusString",
      sorter: "string",
    },
  ];
});
const secondColumns = computed(() => {
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
      title: "점검항목",
      field: "TITLE",
      sorter: "string",
    },
    {
      title: "점검내용",
      field: "statusString",
      sorter: "string",
    },
    {
      title: "점검일자",
      field: "statusString",
      sorter: "string",
    },
    {
      title: "점검결과",
      headerHozAlign: "center",
      field: "statusString",
      sorter: "string",
      columns: [
        {
          title: "직전",
        },
        {
          title: "당월",
        },
      ],
    },
  ];
});
const apiList = {
  getList: {
    url: "/v1/ad/listvideoad",
    method: "get",
    queryparam: {
      searchKeyword: "",
      status: 0,
    },
  },
  addList: {
    url: "/v1/ad/addvideoad",
    method: "post",
    queryparam: {
      title: "",
      contents: "",
    },
  },
  updateList: {
    url: "/v1/ad/updatevideoad",
    method: "post",
    queryparam: {
      videoAdId: "",
      title: "",
      contents: "",
    },
  },
  removeList: {
    url: "/v1/ad/removevideoad",
    method: "post",
    queryparam: {
      videoAdId: "",
    },
  },
};
const listTemp = {
  videoAdId: 0,
  title: "",
  contents: "",
  status: 0,
};
const listData = reactive({});
Object.assign(listData, listTemp);
const defaultStatus = computed(() => useUiStore.defaultStatus);

const search = ref({
  searchKeyword: "",
  status: useUiStore.defaultStatus ? useUiStore.defaultStatus : 0,
});
const form = ref();
const valid = ref(false);

const triggerEvent = (CRUD, type) => {
  const triggers = {
    get: () => {},
  };
  triggers[CRUD]();
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
    cols: 2,
    title: "시작일",
    type: "datePicker",
    target: undefined,
  },
  {
    cols: 2,
    title: "종료일",
    type: "datePicker",
    target: undefined,
  },
]);
const buttonItems = ref([
  {
    title: computed(() => t("STRING_SEARCH")),
    icon: "mdi-magnify",
    event: () => {
      console.log("hi");
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
      >
      </InputContainer>
    </v-col>
    <v-col cols="5">
      <v-card elevation="1" rounded="lg">
        <Tabulator
          :columns="columns"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </v-card>
    </v-col>
    <v-col cols="7">
      <v-sheet color="transparent" :style="{ height: tableHeight }">
        pdf
      </v-sheet>
    </v-col>
  </DefaultContainer>

  <ModalContainer type="dialog" :text="t('STRING_AD_ADD')" :id="addListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_AD_NAME')"
        v-model="listData.title"
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
        v-model="listData.title"
        :rules="[rules.required]"
        clearable
      />

      <Jodit
        :contents="listData.contents"
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
      <v-btn class="mt-3" @click="listEvent('update')">{{
        t("STRING_SAVE")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer type="dialog" text="비디오 광고 삭제" :id="removeListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="listEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
</template>

<style scoped></style>
