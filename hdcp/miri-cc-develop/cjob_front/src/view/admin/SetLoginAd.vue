<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  InputContainer,
  ButtonContainer,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";

import { v4 as uuidv4 } from "uuid";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const height = 64 + 64 + 16 + 64;
// 48=header, 64 최상단 search & button  / 16 컨테이너 bottom 패딩 / 4 어디서 나왔는지 모름. no scroll
const tableHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const addListModal = uuidv4();
const updateListModal = uuidv4();
const removeListModal = uuidv4();
const removeImagesModal = uuidv4();
const rowTarget = ref([]);
const tableData = ref([]);
const ImageData = ref([]);
const files = ref([]);
const fileKey = ref(0);
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
      title: t("STRING_AD_NAME"),
      field: "AD_NM",
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
    url: "/v1/ad/listloginad",
    method: "get",
    queryparam: {
      searchKeyword: "",
      status: 0,
    },
  },
  addList: {
    url: "/v1/ad/addloginad",
    method: "post",
    queryparam: {
      adNm: "",
    },
  },
  updateList: {
    url: "/v1/ad/updateloginad",
    method: "post",
    queryparam: {
      adId: "",
      adNm: "",
    },
  },
  removeList: {
    url: "/v1/ad/removeloginad",
    method: "post",
    queryparam: {
      adId: "",
    },
  },
  getImages: {
    url: "/v1/ad/listloginadimages",
    method: "get",
    queryparam: {
      adId: "",
    },
  },
  uploadImages: {
    url: "/v1/ad/uploadloginad",
    method: "post",
    headers: {
      "Content-Type": "multipart/form-data",
    },
    queryparam: {
      formData: "",
    },
  },
  removeImages: {
    url: "/v1/ad/removelogindetailad",
    method: "post",
    queryparam: {
      adDetailId: 0,
    },
  },
};
const listTemp = {
  adId: 0,
  adNm: "",
  status: 0,
};
const listData = reactive({});
Object.assign(listData, listTemp);
const form = ref();
const valid = ref(false);
const defaultStatus = computed(() => useUiStore.defaultStatus);

watch([rowTarget, defaultStatus], (newVal, oldVal) => {
  if (!!rowTarget.value[0]?.AD_ID == true) {
    fileKey.value++;

    getImagesEvent();
  }
  if (newVal[1] != oldVal[1]) {
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
  fileKey.value++;

  rowTarget.value = [];
  ImageData.value = [];
};

const listEvent = async (method) => {
  const request = async (params) => {
    const { status, code, message, resultset } = await sendRequest(params);
    if (code == 200) {
      await getListEvent();
      useUiStore.closeModal();
      toast.success(message);
    } else {
      toast.error(t("ERROR_MESSAGE"));
    }
    fileKey.value++;
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
        adId: item.AD_ID,
      });
    });
    params.queryparam = [...removeArr];
    await request(params);
  }
};

const getImagesEvent = async () => {
  let params = apiList["getImages"];
  params.queryparam = {
    adId: rowTarget.value[0].AD_ID,
  };
  const { status, code, message, resultset } = await sendRequest(params);
  if (code == 200) {
    ImageData.value = resultset;
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
};

const uploadFileEvent = async (e) => {
  let params = apiList["uploadImages"];

  const formData = new FormData();
  const multipleFile = (file) => {
    if (ImageData.value.length < 5) {
      ImageData.value.push({
        ORIGINAL_FILE_NM: file.name,
        BASE64_IMG: URL.createObjectURL(file),
      });
      formData.append("adFiles", file);
    }
  };

  if (ImageData.value.length < 5) {
    [].forEach.call(e.target.files, multipleFile);
    formData.append("adId", rowTarget.value[0].AD_ID);
    params.queryparam = formData;
    const { status, code, message, resultset } = await sendRequest(params);
    if (code == 200) {
      getImagesEvent();
    } else {
      toast.error(t("ERROR_MESSAGE"));
    }
  } else {
    toast.warning(t("WARNING_IMAGE_CHECK_LENGTH"));
  }
  //   if (code == 200) {
  //     getImagesEvent();
  //   }
};
const removeImgaesEvent = async (item) => {
  let params = apiList["removeImages"];
  const { status, code, message, resultset } = await sendRequest(params);
  if (code == 200) {
    getImagesEvent();
    useUiStore.closeModal();
    toast.success(message);
  } else {
    toast.error(t("ERROR_MESSAGE"));
  }
  fileKey.value++;
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
      listData.adId = rowTarget.value[0].AD_ID;
      listData.adNm = rowTarget.value[0].AD_NM;
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
    icon: "mdi-magnify",

    event: () => {},
  },
  {
    title: computed(() => t("비활성화")),
    icon: "mdi-magnify",

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
      <v-file-input
        :label="t('STRING_FILE')"
        color="primary"
        clearable
        hide-details
        type="file"
        variant="outlined"
        multiple
        accept="image/png, image/jpeg, image/bmp"
        counter
        density="compact"
        v-model="files"
        :disabled="!(rowTarget[0]?.AD_ID && rowTarget.length == 1)"
        @change="uploadFileEvent($event)"
      ></v-file-input>

      <div>
        <v-card v-for="(item, arr, i) in ImageData" :key="i">
          <v-toolbar density="compact" color="white">
            <v-toolbar-title>{{ item.ORIGINAL_FILE_NM }}</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn
              icon="mdi-close"
              @click="
                [
                  useUiStore.openModalEvent(removeImagesModal),
                  (apiList['removeImages'].queryparam.adDetailId =
                    item.AD_DETAIL_ID),
                ]
              "
            >
            </v-btn>
          </v-toolbar>
          <v-img max-height="125" cover :src="item.BASE64_IMG" />
        </v-card>
      </div>
    </v-col>
  </DefaultContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_REMOVE_IMG')"
    :id="removeImagesModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-card-text>{{ t("STRING_CHECK_REMOVE") }}</v-card-text>
    <v-card-actions class="d-flex justify-space-around">
      <v-btn @click="removeImgaesEvent()">{{ t("STRING_REMOVE") }}</v-btn>
      <v-btn @click="useUiStore.closeModal">{{ t("STRING_CANCEL") }}</v-btn>
    </v-card-actions>
  </ModalContainer>

  <ModalContainer type="dialog" :text="t('STRING_AD_ADD')" :id="addListModal">
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="listData.adNm"
        :rules="[rules.required]"
        clearable
      />

      <v-btn class="mt-3" @click="listEvent('add')">{{
        t("STRING_SAVE")
      }}</v-btn>
    </v-form>
  </ModalContainer>

  <ModalContainer
    type="dialog"
    :text="t('STRING_AD_UPDATE')"
    :id="updateListModal"
  >
    <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
    <v-form v-model="valid" ref="form" class="text-center">
      <v-text-field
        variant="outlined"
        density="compact"
        :label="t('STRING_NAME')"
        v-model="listData.adNm"
        :rules="[rules.required]"
        clearable
      />

      <v-btn class="mt-3" @click="listEvent('update')">{{
        t("STRING_SAVE")
      }}</v-btn>
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
<style scoped></style>
