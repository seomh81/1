<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  DefaultContainer,
  InputContainer,
  Tabulator,
  ModalContainer,
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
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = uuidv4();
const updateListModal = uuidv4();
const removeListModal = uuidv4();
const rowTarget = ref([]);
const tableData = ref([]);
const height = 64 + 64 + 16 + 20;
// 48=header, 64 최상단 search & button  / 16 컨테이너 bottom 패딩 / 4 어디서 나왔는지 모름. no scroll
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
      maxWidth: "70",
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
      title: t("STRING_FAQ_QUESTION"),
      field: "QUESTION",
      sorter: "string",
      hozAlign: "start",
      minWidth: 200,
    },

    {
      title: t("STRING_CREATION_USER"),
      field: "CREATION_USER",
      sorter: "string",
      minWidth: 50,
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
      width: "50",

      cellClick: function (e, cell) {
        const createDiv = document.createElement("div");
        createDiv.classList = `accordion${cell._cell.row.data.FAQ_ID}`;
        createDiv.innerHTML = `
    <div class="v-card v-theme--light v-card--density-default v-card--variant-elevated"
     color="gray"
     >
            <div class="v-container v-container--fluid">
                <div class="v-row" >
                  <div class="v-col v-col-12 text-start ">
                        ${cell._cell.row.data.QUESTION}
                    </div>
                    <div class="v-col v-col-12 text-start">
                        ${cell._cell.row.data.ANSWER}
                    </div>
                </div>
        </div>
    </div>`;
        createDiv.classList.add("animation-init");

        let accordion = document.querySelector(
          `.accordion${cell._cell.row.data.FAQ_ID}`
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
    url: "/v1/system/listfaq",
    method: "get",
    queryparam: {
      searchKeyword: "",
      status: 0,
    },
  },
  addList: {
    url: "/v1/system/addfaq",
    method: "post",
    queryparam: {
      faqId: "",
      question: "",
      answer: "",
    },
  },
  updateList: {
    url: "/v1/system/updatefaq",
    method: "post",
    queryparam: {
      faqId: "",
      question: "",
      answer: "",
    },
  },
  removeList: {
    url: "/v1/system/removefaq",
    method: "post",
    queryparam: {
      faqId: "",
    },
  },
};

const listTemp = {
  faqId: 0,
  question: "",
  answer: "",
  status: search.value.status,
};
const listData = reactive({});

const form = ref();
const valid = ref(false);
const defaultStatus = computed(() => useUiStore.defaultStatus);

watch([defaultStatus], (newVal, oldVal) => {
  if (newVal[0] != oldVal[0]) {
    if (tableData.value.length > 0) {
      tableData.value = [];
    }
    search.value.status = useUiStore.defaultStatus;
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
  resultset.sort((a, b) => {
    return a.FAQ_ID - b.FAQ_ID;
  });
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
      faqIds: [],
    };
    rowTarget.value.forEach((item) => {
      mapObj.faqIds.push(item.FAQ_ID);
    });
    params.queryparam = mapObj;
    await response(params);
  }
};

const triggerEvent = (CRUD) => {
  const triggers = {
    get: () => {},
    add: () => {},
    update: () => {},
    remove: () => {},
  };
  triggers[CRUD]();
};
onMounted(() => {});
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
      () => !(rowTarget.value[0]?.FAQ_ID && rowTarget.value.length == 1)
    ),

    event: () => {
      useUiStore.openModalEvent(updateListModal);
      listData.faqId = rowTarget.value[0].FAQ_ID;
      listData.question = rowTarget.value[0].QUESTION;
      listData.answer = rowTarget.value[0].ANSWER;
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

  <ModalContainer type="dialog" :text="t('STRING_FAQ_ADD')" :id="addListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-end">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_FAQ_QUESTION')"
        v-model="listData.question"
        :rules="[rules.required]"
        clearable
      />
      <Jodit @update:contents="(returnVal) => (listData.answer = returnVal)" />
      <v-btn class="mt-3" @click="listEvent('add')">{{
        t("STRING_ADD")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_FAQ_UPDATE')"
    :id="updateListModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-end">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_FAQ_QUESTION')"
        v-model="listData.question"
        :rules="[rules.required]"
        clearable
      />
      <Jodit
        :contents="listData.answer"
        @update:contents="(returnVal) => (listData.answer = returnVal)"
      />
      <v-btn class="mt-3" @click="listEvent('update')">{{
        t("STRING_UPDATE")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_FAQ_REMOVE')"
    :id="removeListModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="listEvent('remove')">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>
</template>
<style lang="scss">
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
