<script setup>
import { ref, reactive, computed, onMounted, watch, provide } from "vue";
import {
  DefaultContainer,
  SearchBarContainer,
  Tabulator,
  ModalContainer,
  Jodit,
  Skeleton,
} from "@/component/Template";

import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { useRoute } from "vue-router";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const route = useRoute();
const { t } = useI18n();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const useUserStore = userStore();
const addListModal = ref(false);
const updateListModal = ref(false);
const removeListModal = ref(false);
const rowTarget = ref([]);
const tableData = ref([]);
const height = 64 + 64 + 12;
const tableHeight = `calc(100vh - ${height}px)`;
const search = ref({
  searchKeyword: "",
  // delYn: useUiStore.defaultDelYn ? useUiStore.defaultDelYn : "n",
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
      title: t("CREATION_DT"),
      field: "creationDt",
      sorter: "string",
    },
    {
      title: t("FAQ_QUESTION"),
      field: "question",
      sorter: "string",
      hozAlign: "start",
      minWidth: 200,
    },
    {
      title: t("CREATION_USER"),
      field: "currentUser",
      sorter: "string",
      minWidth: 50,
    },
    {
      hozAlign: "center",
      headerSort: false,
      formatter: "tickCross",
      formatterParams: {
        crossElement: "<i class='fa fa-plus'></i>",
      },
      width: 100,
      cellClick: function (e, cell) {
        const createDiv = document.createElement("div");
        createDiv.classList = `accordion${cell._cell.row.data.faqId}`;
        createDiv.innerHTML = `
    <div
     color="gray"
     >
            <div class="v-container v-container--fluid">
                <div class="v-row" >
                  <div class="v-col v-col-12 text-start ">
                        ${cell._cell.row.data.question}
                    </div>
                    <div class="v-col v-col-12 text-start">
                        ${cell._cell.row.data.answer}
                    </div>
                </div>
        </div>
    </div>`;
        createDiv.classList.add("animation-init");

        let accordion = document.querySelector(
          `.accordion${cell._cell.row.data.faqId}`
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
    url: "/v2/board/faq/list",
    method: "get",
    queryparam: {},
  },
  addList: {
    url: "/v2/board/faq/create",
    method: "post",
    queryparam: {},
  },
  updateList: {
    url: "/v2/board/faq/update",
    method: "post",
    queryparam: {},
  },
  removeList: {
    url: "/v2/board/faq/remove",
    method: "post",
    queryparam: {},
  },
};

const listTemp = {
  question: "",
  answer: "",
  // delYn: search.value.delYn,
};
const listData = reactive({});

const form = ref();
const valid = ref(false);

// const getDelYnEvent = async () => {
//   const data = await useUiStore.getDelYn();
//   search.value.delYn = data.defaultDelYn;
//   delYnItem.value = data.delYnItem;
// };
const getListEvent = async () => {
  let params = apiList["getList"];
  params.queryparam = {
    searchKeyword: search.value.searchKeyword,
    // delYn: search.value.delYn.code
    //   ? search.value.delYn.code
    //   : search.value.delYn,
  };
  const { because, code, data, message, result } =
    await useConnectionStore.request(params);
  if (code == 200) {
    if (data.length == 0) {
      toast.warning(t("NO_DATA_SEARCH", ["FAQ"]));
    }
    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", ["FAQ"]));
  }
  rowTarget.value = [];
};

const callAPIs = async (method) => {
  const request = async (params, modal, successText, errorText) => {
    const { because, code, data, message, result } =
      await useConnectionStore.request(params);
    if (code == 200) {
      if (modal) {
        modal.value = false;
      }
      toast.success(successText);
      getListEvent();
    } else {
      toast.error(errorText);
    }
  };
  const triggerAction = {
    create: () => {
      let params = apiList["addList"];
      params.queryparam = { ...listData };
      request(
        params,
        addListModal,
        t("SUCCESS_ADD", [t("FAQ")]),
        t("ERROR_ADD", [t("FAQ")])
      );
    },
    update: () => {
      let params = apiList["updateList"];
      params.queryparam = {
        faqId: rowTarget.value[0].faqId,
        ...listData,
      };
      request(
        params,
        updateListModal,
        t("SUCCESS_UPDATE", [t("FAQ")]),
        t("ERROR_UPDATE", [t("FAQ")])
      );
    },
    remove: () => {
      let params = apiList["removeList"];

      let mapObj = {
        faqIds: [],
      };
      rowTarget.value.forEach((item) => {
        mapObj.faqIds.push(item.faqId);
      });
      params.queryparam = mapObj;
      request(
        params,
        removeListModal,
        t("SUCCESS_REMOVE", [t("FAQ")]),
        t("ERROR_REMOVE", [t("FAQ")])
      );
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
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",

      title: t("BTN_SEARCH"),

      event: () => {
        getListEvent();
      },
    },
    {
      methodName: "BTN_CREATE",

      title: t("BTN_CREATE"),

      event: () => {
        Object.assign(listData, listTemp);
        addListModal.value = true;
      },
    },
    {
      methodName: "BTN_UPDATE",

      title: t("BTN_UPDATE"),

      disabled: computed(
        () => !(rowTarget.value[0]?.faqId && rowTarget.value.length == 1)
      ),

      event: () => {
        listData.question = rowTarget.value[0].question;
        listData.answer = rowTarget.value[0].answer;
        updateListModal.value = true;
      },
    },
    {
      methodName: "BTN_DELETE",

      title: t("BTN_DELETE"),

      disabled: computed(() => !rowTarget.value.length >= 1),

      event: () => {
        removeListModal.value = true;
      },
    },
  ],
});

searchBar.buttons = searchBar.buttons.filter((button) => {
  return route.meta.methods?.some(
    (item2) => button.methodName === item2.methodName
  );
});

provide("searchBar", searchBar);

onMounted(() => {
  getListEvent();
});
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="12" class="pt-0">
      <Skeleton :loadingData="tableData == 0" :height="tableHeight">
        <Tabulator
          :pagination="true"
          :columns="columns"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </Skeleton>
    </v-col>
  </DefaultContainer>

  <ModalContainer
    :width="'600px'"
    :text="t('FAQ_ADD')"
    v-model="addListModal"
    @btnEvent="callAPIs('create')"
  >
    <v-form v-model="valid" ref="form" @submit.prevent="">
      <v-text-field
        class="mb-1"
        density="compact"
        variant="solo"
        :label="t('FAQ_QUESTION')"
        v-model="listData.question"
        :rules="[useUiStore.rules.required]"
        clearable
      />

      <Jodit
        :contetns="listData.answer"
        @update:contents="(returnVal) => (listData.answer = returnVal)"
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    :width="'600px'"
    :text="t('FAQ_UPDATE')"
    v-model="updateListModal"
    @btnEvent="callAPIs('update')"
  >
    <v-form v-model="valid" ref="form" @submit.prevent="">
      <v-text-field
        class="mb-1"
        variant="solo"
        :label="t('FAQ_QUESTION')"
        density="compact"
        v-model="listData.question"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <Jodit
        :contents="listData.answer"
        @update:contents="(returnVal) => (listData.answer = returnVal)"
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    :text="t('FAQ_REMOVE')"
    v-model="removeListModal"
    @btnEvent="callAPIs('remove')"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
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
