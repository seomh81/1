<script setup>
import { ref, reactive, computed, onMounted, watch, provide } from "vue";
import {
  Tabulator,
  ModalContainer,
  Jodit,
  DefaultContainer,
  SearchBarContainer,
  Skeleton,
} from "@/component/Template";
import { useRoute } from "vue-router";

import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const route = useRoute();
const { t } = useI18n();
const useUserStore = userStore();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = ref(false);
const updateListModal = ref(false);
const removeListModal = ref(false);
const rowTarget = ref([]);
const tableData = ref([]);
const height = 64 + 64 + 12;
const tableHeight = `calc(100vh - ${height}px)`;

const search = ref({
  searchKeyword: "",
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
      title: t("CREATION_DT"),
      field: "creationDt",
      sorter: "string",
    },
    {
      title: t("TITLE"),
      field: "title",
      hozAlign: "start",
      tooltip: true,
      sorter: "string",
    },

    {
      title: t("CREATION_USER"),
      field: "lastupdateUser",
      sorter: "string",
    },

    {
      hozAlign: "center",
      headerSort: false,
      formatter: "tickCross",
      formatterParams: {
        crossElement: "<i class='fa fa-plus test1' ></i>",
      },
      width: 100,

      cellClick: function (e, cell) {
        const createDiv = document.createElement("div");
        createDiv.classList = `accordion${cell._cell.row.data.noticeId}`;
        createDiv.innerHTML = `
    <div class="">
            <div class="v-container v-container--fluid">
                <div class="v-row">
                    <div class="v-col v-col-12 text-start">
                        ${cell._cell.row.data.title}
                    </div>
                    <div class="v-col v-col-12 text-start">
                        ${cell._cell.row.data.contents}
                    </div>
                </div>
        </div>
    </div>`;
        createDiv.classList.add("animation-init");

        let accordion = document.querySelector(
          `.accordion${cell._cell.row.data.noticeId}`
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
    url: "/v2/board/notice/list",
    method: "get",
    queryparam: {},
  },
  addList: {
    url: "/v2/board/notice/create",
    method: "post",
    queryparam: {},
  },
  updateList: {
    url: "/v2/board/notice/update",
    method: "post",
    queryparam: {},
  },
  removeList: {
    url: "/v2/board/notice/remove",
    method: "post",
    queryparam: {},
  },
  contractSummary: {
    url: "/v2/contract/portfolio/details/summary",
    method: "post",
    queryparam: {},
  },
};
const listTemp = {
  title: "",
  contents: "",
};
const listData = reactive({});
const contractObj = ref({
  selected: "",
  items: [],
});
Object.assign(listData, listTemp);

const form = ref();
const valid = ref(false);

const callAPIs = async (method) => {
  const request = async (params, modal, successText, errorText) => {
    const { because, code, data, message, result } =
      await useConnectionStore.request(params);
    if (code == 200) {
      await triggerAction.get();
      if (modal) {
        modal.value = false;
      }
      toast.success(successText);
    } else {
      toast.error(errorText);
    }
  };
  const triggerAction = {
    get: async () => {
      let params = apiList["getList"];
      params.queryparam = {
        searchKeyword: search.value.searchKeyword,
      };
      const { because, code, data, message, result } =
        await useConnectionStore.request(params);
      if (code == 200) {
        if (data.length == 0) {
          toast.warning(t("NO_DATA_SEARCH", [t("MENU_SYSTEM_NOTICE")]));
        }
        rowTarget.value = [];
        tableData.value = data;
      } else {
        toast.error(t("ERROR_SEARCH", ["MENU_SYSTEM_NOTICE"]));
      }
    },

    create: () => {
      let params = apiList["addList"];
      params.queryparam = {
        intgProjectTrlineCdCode:
          useUserStore.userInfo.roleType.toUpperCase() === "SYSTEM"
            ? ""
            : contractObj.value.selected.intgPrjTrlineCdCode,

        ...listData,
      };
      request(
        params,
        addListModal,
        t("SUCCESS_ADD", [t("MENU_SYSTEM_NOTICE")]),
        t("ERROR_ADD", [t("MENU_SYSTEM_NOTICE")])
      );
    },
    update: () => {
      let params = apiList["updateList"];
      params.queryparam = {
        intgProjectTrlineCdCode: rowTarget.value[0].intgProjectTrlineCdCode,
        noticeId: rowTarget.value[0].noticeId,
        ...listData,
      };
      request(
        params,
        updateListModal,
        t("SUCCESS_UPDATE", [t("MENU_SYSTEM_NOTICE")]),
        t("ERROR_UPDATE", [t("MENU_SYSTEM_NOTICE")])
      );
    },
    remove: () => {
      let params = apiList["removeList"];
      let mapObj = {
        noticeIds: [],
      };
      rowTarget.value.forEach((item) => {
        mapObj.noticeIds.push(item.noticeId);
      });
      params.queryparam = mapObj;
      request(
        params,
        removeListModal,
        t("SUCCESS_REMOVE", [t("MENU_SYSTEM_NOTICE")]),
        t("ERROR_REMOVE", [t("MENU_SYSTEM_NOTICE")])
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

const computedSelectedId = computed(
  () => useUserStore.portfolio.selected?.userPortfolioMappingId
);
watch(computedSelectedId, () => {
  if (
    !!useUserStore.userInfo?.roleType &&
    useUserStore.userInfo?.roleType.toUpperCase() !== "SYSTEM"
  ) {
    getSummary();
  }
});
const getSummary = async () => {
  const params = apiList["contractSummary"];
  params.queryparam = {
    userPortfolioMappingId: computedSelectedId.value,
  };
  const { code, data, result, because, message } =
    await useConnectionStore.request(params);
  if (code == 200) {
    if (result == "success") {
      contractObj.value.items = data;
      contractObj.value.selected = data[0];
    } else {
      toast.error(because, result);
    }
  } else {
    toast.error(message);
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
        callAPIs("get");
      },
    },

  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",
      title: t("BTN_SEARCH"),

      event: () => {
        callAPIs("get");
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
        () =>
          !(
            rowTarget.value[0]?.noticeId &&
            rowTarget.value.length == 1 &&
            rowTarget.value[0].lastupdateUser == useUserStore.userInfo.userId
          )
      ),

      event: () => {
        listData.title = rowTarget.value[0].title;
        listData.contents = rowTarget.value[0].contents;
        updateListModal.value = true;
      },
    },
    {
      methodName: "BTN_DELETE",

      title: t("BTN_DELETE"),

      disabled: computed(() =>
        rowTarget.value.length >= 1 &&
        rowTarget.value[0].lastupdateUser == useUserStore.userInfo.userId
          ? false
          : true
      ),

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
  callAPIs("get");

  if (
    !!useUserStore.userInfo.roleType &&
    useUserStore.userInfo.roleType.toUpperCase() !== "SYSTEM"
  ) {
    getSummary();
  }
});
</script>

<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="12" class="pt-0">
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
  </DefaultContainer>

  <ModalContainer
    :text="t('NOTICE_ADD')"
    @btnEvent="callAPIs('create')"
    v-model="addListModal"
    :width="'600px'"
  >
    <v-form v-model="valid" ref="form" class="" @submit.prevent="">
      <v-text-field
        class="mb-1"
        variant="solo"
        :label="t('TITLE')"
        v-model="listData.title"
        :rules="[useUiStore.rules.required]"
        density="compact"
        clearable
      />
      <v-select
        density="compact"
        :label="t('MY_CONTRACT_NUMBER')"
        variant="solo"
        v-if="useUserStore.userInfo.roleType.toUpperCase() !== 'SYSTEM'"
        v-model="contractObj.selected"
        :items="contractObj.items"
        item-title="siteNm"
        return-object
      />
      <Jodit
        :contetns="listData.contents"
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    :text="t('NOTICE_UPDATE')"
    @btnEvent="callAPIs('update')"
    :width="'600px'"
    v-model="updateListModal"
  >
    <v-form v-model="valid" ref="form" class="" @submit.prevent="">
      <v-text-field
        class="pb-1"
        variant="solo"
        density="compact"
        :label="t('TITLE')"
        v-model="listData.title"
        :rules="[useUiStore.rules.required]"
        clearable
      />
      <v-select
        density="compact"
        :label="t('MY_CONTRACT_NUMBER')"
        variant="solo"
        v-if="useUserStore.userInfo.roleType.toUpperCase() !== 'SYSTEM'"
        v-model="contractObj.selected"
        :items="contractObj.items"
        item-title="siteNm"
        return-object
      />
      <Jodit
        :contents="listData.contents"
        @update:contents="(returnVal) => (listData.contents = returnVal)"
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    @btnEvent="callAPIs('remove')"
    :text="t('NOTICE_REMOVE')"
    v-model="removeListModal"
  >
    <v-card-text>{{ t("CHECK_REMOVE") }}</v-card-text>
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
