<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  Tabulator,
  ModalContainer,
  Jodit,
  DefaultContainer,
  InputContainer,
  ButtonContainer,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";

import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = uuidv4();
const updateListModal = uuidv4();
const removeListModal = uuidv4();
const rowTarget = ref([]);
const tableData = ref([]);
const height = 64 + 64 + 36;
// 48=header, 64 최상단 search & button  / 16 컨테이너 bottom 패딩
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
const avatarTemp = require("@/assets/img/avatar.png");

const columns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      maxWidth: "70",
      height: "45",
      headerSort: false,
      hozAlign: "center",
      vertAlign: "center",
      headerHozAlign: "center",
    },
    {
      title: "아바타",
      field: "avatarUrl",
      headerSort: false,

      headerHozAlign: "center",
      maxWidth: 100,
      vertAlign: "middle",
      hozAlign: "center",

      formatter: function (cell, formatterParams) {
        // console.log(cell);
        const avatar = cell.getValue();
        console.log(!!avatar);
        // return '<img style='border - radius: 50 %; width: 30px'; height: 30px; src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAZCAYAAADE6YVjAAAAJUlEQVR42u3NQQEAAAQEsJNcdFLw2gqsMukcK4lEIpFIJBLJS7KG6yVo40DbTgAAAABJRU5ErkJggg==">';
        return avatar
          ? `<img style='border-radius: 50%; width: 30px; height: 30px;' 
              src=${avatar}>`
          : `<img style='border-radius: 50%; width: 30px; height: 30px;' 
              src=${avatarTemp}>`; // <img style='border-radius: 50%; width: 30px'; height: 30px; ' src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAZCAYAAADE6YVjAAAAJUlEQVR42u3NQQEAAAQEsJNcdFLw2gqsMukcK4lEIpFIJBLJS7KG6yVo40DbTgAAAABJRU5ErkJggg==">
      },
    },
    {
      title: "아이디",
      field: "userId",
      sorter: "string",
      vertAlign: "middle",
    },

    {
      title: "이름",
      field: "userNm",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: "전화번호",
      field: "phoneno",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: "주소",
      field: "assetCode",
      sorter: "string",
      vertAlign: "middle",
    },
    {
      title: "권한",
      field: "accountType",
      sorter: "string",
      vertAlign: "middle",
    },
  ];
});
const apiList = {
  getList: {
    url: "/v1/auth/listuser",
    method: "get",
    queryparam: {},
  },
  addList: {
    url: "/v1/auth/adduser",
    method: "post",
    queryparam: {},
  },
  updateList: {
    url: "/v1/auth/updateuser",
    method: "post",
    queryparam: {},
  },
  removeList: {
    url: "/v1/auth/removeuser",
    method: "post",
    queryparam: {},
  },
};
const listTemp = {
  avatarUrl: "",
  userId: "",
  userNm: "",
  phoneno: "",
  status: "",
  note: "",
  accountType: "",
  assetCode: [],
};
const listData = reactive({});
Object.assign(listData, listTemp);

const form = ref();
const valid = ref(false);

watch([rowTarget, form], () => {});
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
    tableData.value = resultset;
  }

  rowTarget.value = [];
};

const listEvent = async (method) => {
  const response = async (params) => {
    const { status, code, message, resultset } = await sendRequest(params);
    if (code == 200) {
      await getListEvent();
      useUiStore.closeModal();
    }
    console.log(message);
  };
  if (method != "remove") {
    await form.value.validate();
  }
  // if (valid.value) {
  //   if (method == "add") {
  //     let params = apiList["addList"];
  //     params.queryparam = { ...listData };
  //     await response(params);
  //   }
  //   if (method == "update") {
  //     let params = apiList["updateList"];
  //     params.queryparam = { ...listData };
  //     await response(params);
  //   }
  // }

  if (method == "remove") {
    let params = apiList["removeList"];

    // params.queryparam = ;
    // let mapObj = {
    //   faqIds: [],
    // };
    let arr = [];
    rowTarget.value.forEach((item) => {
      // mapObj.faqIds.push(item.FAQ_ID);
      arr.push({ userId: item.userId });
    });
    console.log(arr);
    params.queryparam = arr;
    await response(params);
  }
};

const triggerEvent = (CRUD, type) => {
  const triggers = {
    get: () => {
      getListEvent();
    },
    add: () => {
      useUiStore.openModalEvent(addListModal);
      Object.assign(listData, listTemp);
    },
    update: () => {
      useUiStore.openModalEvent(updateListModal);
      listData.popupAdId = rowTarget.value[0].POPUP_AD_ID;
      listData.popupNm = rowTarget.value[0].POPUP_NM;
      listData.contents = rowTarget.value[0].CONTENTS;
    },
    remove: () => {
      useUiStore.openModalEvent(removeListModal);
    },
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
    event: () => {},
  },
  {
    title: computed(() => t("STRING_UPDATE")),
    icon: "mdi-magnify",

    event: () => {},
  },
  {
    title: computed(() => t("STRING_REMOVE")),
    icon: "mdi-magnify",
    disabled: computed(() => !rowTarget.value.length >= 1),

    event: () => {},
  },
]);

onMounted(() => {});
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
