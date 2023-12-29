<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  ButtonContainer,
  Tabulator,
  ModalContainer,
  Jodit,
  DefaultContainer,
  InputContainer,
} from "@/component/Template";

import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";

import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = uuidv4();
const updateListModal = uuidv4();
const removeListModal = uuidv4();
const rowTarget = ref([]);
const tableData = ref([]);
const height = 64 + 64 + 16 + 20;
// 48=header, 64 최상단 search & button  / 16 컨테이너 패딩 / 4 어디서 나왔는지 모름. no scroll

const tableHeight = `calc(100vh - ${height}px)`;

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
      title: t("STRING_CREATION_DT"),
      field: "CREATION_DT",
      sorter: "number",
    },
    {
      title: t("STRING_TITLE"),
      field: "TITLE",
      hozAlign: "start",
      sorter: "string",
    },

    {
      title: t("STRING_CREATION_USER"),
      field: "ASSET_CODE",
      sorter: "string",
    },
    {
      title: t("STRING_STATUS"),
      field: "statusString",
      sorter: "string",
    },
    {
      // eslint-disable-next-line
      hozAlign: "center",
      headerSort: false,
      formatter: "tickCross",
      formatterParams: {
        crossElement: "<i class='fa fa-plus'></i>",
      },
      width: 50,

      cellClick: function (e, cell) {
        const createDiv = document.createElement("div");
        createDiv.classList = `accordion${cell._cell.row.data.NOTICE_ID}`;
        createDiv.innerHTML = `
    <div class="v-card  v-theme--light v-card--density-default v-card--variant-elevated" color="gray">
            <div class="v-container v-container--fluid">
                <div class="v-row">
                    <div class="v-col v-col-12 text-start">
                        ${cell._cell.row.data.TITLE}
                    </div>
                    <div class="v-col v-col-12 text-start">
                        ${cell._cell.row.data.CONTENTS}
                    </div>
                </div>
        </div>
    </div>`;
        createDiv.classList.add("animation-init");

        let accordion = document.querySelector(
          `.accordion${cell._cell.row.data.NOTICE_ID}`
        );

        if (accordion) {
          accordion.classList.remove("animation-fade");
          accordion.classList.add("animation-leave");

          setTimeout(() => {
            accordion.remove();
            cell._cell.element.children[0].classList.remove("fa-minus");
            cell._cell.element.children[0].classList.add("fa-plus");
          }, 400);
        } else {
          cell._cell.row.getElement().appendChild(createDiv);
          cell._cell.element.children[0].classList.remove("fa-plus");
          cell._cell.element.children[0].classList.add("fa-minus");
          setTimeout(() => {
            createDiv.classList.toggle("animation-fade");
          }, 30);
        }
      },
    },
  ];
});
const apiList = {
  getList: {
    url: "/v1/system/listnotice",
    method: "get",
    queryparam: {
      searchKeyword: "",
      status: 0,
    },
  },
  addList: {
    url: "/v1/system/addnotice",
    method: "post",
    queryparam: {
      noticeId: "",
      title: "",
      contents: "",
      ASSET_CODE: "",
    },
  },
  updateList: {
    url: "/v1/system/updatenotice",
    method: "post",
    queryparam: {
      noticeId: "",
      title: "",
      contents: "",
      ASSET_CODE: "",
    },
  },
  removeList: {
    url: "/v1/system/removenotice",
    method: "post",
    queryparam: {
      noticeId: "",
    },
  },
};
const listTemp = {
  noticeId: 0,
  title: "",
  contents: "",
  status: 0,
};
const listData = reactive({});
Object.assign(listData, listTemp);

const form = ref();
const valid = ref(false);
const defaultStatus = computed(() => useUiStore.defaultStatus);

watch([defaultStatus], (newVal, oldVal) => {
  if (newVal[0] != oldVal[0]) {
    search.value.status = useUiStore.defaultStatus;
    if (!!tableData.value.length > 0) {
      tableData.value = [];
    }
  }
});
const sendRequest = async (params, i) => {
  return await useConnectionStore.request(params);
};

const getListEvent = async () => {
  let params = apiList["getList"];
  params.queryparam = {
    searchKeyword: search.value.searchKeyword,
    status: search.value.status.CODE
      ? search.value.status.CODE
      : search.value.status,
  };

  const { status, code, message, resultset } = await sendRequest(params);
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
  const response = async (params) => {
    const { status, code, message, resultset } = await sendRequest(params);
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
      await response(params);
    }
    if (method == "update") {
      let params = apiList["updateList"];
      params.queryparam = { ...listData };
      await response(params);
    }
  }
  if (method == "remove") {
    let params = apiList["removeList"];

    // params.queryparam = ;

    let mapObj = {
      noticeIds: [],
    };
    rowTarget.value.forEach((item) => {
      mapObj.noticeIds.push(item.NOTICE_ID);
    });
    params.queryparam = mapObj;
    await response(params);
  }
};
const btnClickEvent = async (method) => {
  if (method == "add") {
    useUiStore.openModalEvent(addListModal);
    Object.assign(listData, listTemp);
  }
  if (method == "update") {
    useUiStore.openModalEvent(updateListModal);
    listData.noticeId = rowTarget.value[0].NOTICE_ID;
    listData.title = rowTarget.value[0].TITLE;
    listData.contents = rowTarget.value[0].CONTENTS;
  }
  if (method == "remove") {
    useUiStore.openModalEvent(removeListModal);
  }
};

onMounted(() => {
  setTimeout(() => {
    search.value.status = useUiStore.defaultStatus;
  }, 200);
});
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
    disabled: !(rowTarget.value[0]?.FAQ_ID && rowTarget.value.length == 1),

    event: () => {
      useUiStore.openModalEvent(updateListModal);
      listData.noticeId = rowTarget.value[0].NOTICE_ID;
      listData.title = rowTarget.value[0].TITLE;
      listData.contents = rowTarget.value[0].CONTENTS;
    },
  },
  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",
    disabled: !rowTarget.value.length >= 1,

    event: () => {
      useUiStore.openModalEvent(removeListModal);
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
    <v-col cols="12">
      <v-card elevation="2" rounded="lg">
        <Tabulator
        :columns="columns"
        :tableData="tableData"
        :height="tableHeight"
        @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </v-card>
    </v-col>
  </DefaultContainer>

  <!-- <GridContainer colType="grid-stretch-last">
      <GridContainer colType="block">
        <v-card height="80vh">
          <v-card-titlet('STRING_TITLE'): {{ rowTarget[0]?.TITLE }}</v-card-title>
          <div ref="contentsDiv"></div>
        </v-card>
      </GridContainer>
    </GridContainer> -->

  <ModalContainer type="dialog" text="공지사항 추가" :id="addListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_FAQ_QUESTION')"
        v-model="listData.title"
        :rules="[rules.required]"
        clearable
      />
      <Jodit
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
      <v-btn class="mt-3" @update:click="listEvent('add')">{{
        t("STRING_ADD")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer type="dialog" text="공지사항 수정" :id="updateListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_FAQ_QUESTION')"
        v-model="listData.title"
        :rules="[rules.required]"
        clearable
      />

      <Jodit
        :contents="listData.contents"
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
      <v-btn class="mt-3" @click="listEvent('update')">{{
        t("STRING_UPDATE")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer type="dialog" text="공지사항 삭제" :id="removeListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="listEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
</template>
<style>
.animation-init {
  opacity: 0;
  transform: translateY(-5px);
}

.animation-fade {
  opacity: 1;
  transform: translateY(0);
  transition: ease-in-out 0.4s;
}
.animation-leave {
  opacity: 0;
  transform: translateY(-5px);
  transition: ease-in-out 0.4s;
}
</style>
