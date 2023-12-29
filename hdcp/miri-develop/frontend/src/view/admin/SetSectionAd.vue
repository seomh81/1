<script setup>
import {
  ref,
  reactive,
  computed,
  onMounted,
  watch,
  provide,
} from "vue";
import axios from "axios";

import {
  Tabulator,
  ModalContainer,
  DefaultContainer,
  SearchBarContainer,
  Skeleton,
} from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { userStore } from "@/store/UserStore";
import { uiStore } from "@/store/UiStore";
import { useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const route = useRoute();
const { t } = useI18n();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const height = 64 + 64 + 12 + 48;
const useUserStore = userStore();
const tableHeight = `calc(100vh - ${height}px)`;
const createListModal = ref(false);
const updateListModal = ref(false);
const removeListModal = ref(false);
const deleteImagesModal = ref(false);
const search = ref({
  searchKeyword: "",
  activation: "",
});
const rowTarget = ref([]);
const tableData = ref([]);
const ImageData = ref([]);
const activationItem = ref([]);
const files = ref([]);
const baseUrl = ref(process.env.VUE_APP_API_BASE_URL);

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
      title: t("NAME"),
      field: "adName",
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
  getList: {
    url: "/v2/ad/section/list",
    method: "get",
    queryparam: {},
  },
  createList: {
    url: "/v2/ad/section/create",
    method: "post",
    queryparam: {},
  },
  updateList: {
    url: "/v2/ad/section/update",
    method: "post",
    queryparam: {},
  },
  removeList: {
    url: "/v2/ad/section/remove",
    method: "post",
    queryparam: {},
  },
  active: {
    url: "/v2/ad/section/active",
    method: "post",
    queryparam: {},
  },
  inactive: {
    url: "/v2/ad/section/inactive",
    method: "post",
    queryparam: {},
  },
  getImages: {
    url: "/v2/ad/section/details",
    method: "get",
    queryparam: {},
  },
  uploadImages: {
    url: "/v2/ad/section/details/create",
    method: "post",
    headers: {
      "Content-Type": "multipart/form-data",
    },
    queryparam: {},
  },
  deleteImage: {
    url: "/v2/ad/section/detail/delete",
    method: "post",
    queryparam: {},
  },
};

const listTemp = {
  adName: "",
};
const listData = reactive({});
const form = ref();
const valid = ref(false);
const userLocale = computed(() => useUserStore?.userInfo?.locale);
watch([rowTarget, userLocale], (newVal, oldVal) => {
  if (!!rowTarget.value[0]?.adId == true) {
    files.value = [];

    getImagesEvent();
  }
  if (newVal[1] != oldVal[1]) {
    getActivation();
  }
});

const getActivation = async () => {
  const data = await useUiStore.getActivation();
  search.value.activation = data.defaultActivation;
  activationItem.value = data.activationItem;
};

const getListEvent = async () => {
  let params = apiList["getList"];
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
      toast.warning(t("NO_DATA_SEARCH", [t("AD")]));
    }

    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("AD")]));
  }
  files.value = [];
  rowTarget.value = [];
  ImageData.value = [];
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
      await getListEvent();
    } else {
      toast.error(errorText);
    }
    files.value = [];
  };

  const triggerAction = {
    create: () => {
      let params = apiList["createList"];
      params.queryparam = { ...listData };
      request(
        params,
        createListModal,
        t("SUCCESS_ADD", [t("AD")]),
        t("ERROR_ADD", [t("AD")])
      );
    },
    update: () => {
      let params = apiList["updateList"];
      params.queryparam = {
        adId: rowTarget.value[0].adId,
        ...listData,
      };
      request(
        params,
        updateListModal,
        t("SUCCESS_UPDATE", [t("AD")]),
        t("ERROR_UPDATE", [t("AD")])
      );
    },
    remove: () => {
      let params = apiList["removeList"];
      let removeObj = {
        adIds: [],
      };
      rowTarget.value.forEach((item) => {
        removeObj.adIds.push(item.adId);
      });
      params.queryparam = removeObj;
      request(
        params,
        removeListModal,
        t("SUCCESS_REMOVE", [t("AD")]),
        t("ERROR_REMOVE", [t("AD")])
      );
    },
    active: () => {
      let params = apiList["active"];
      let obj = {
        adIds: [],
      };
      if (
        tableData.value.every((item) => item.activationYn == "n") &&
        rowTarget.value.length === 1
      ) {
        if (ImageData.value.length > 0) {
          rowTarget.value.forEach((item) => {
            obj.adIds.push(item.adId);
          });
          params.queryparam = obj;
          request(params, undefined, t("SUCCESS_ACTIVE"), t("ERROR_ACTIVE"));
        } else {
          toast.warning(t("WARNING_ACTIVE_NO_IMG"));
        }
      } else {
        toast.warning(t("WARNING_ACTIVE_ONLY_ONE"));
      }
    },
    inactive: () => {
      let params = apiList["inactive"];
      let obj = {
        adIds: [],
      };
      rowTarget.value.forEach((item) => {
        obj.adIds.push(item.adId);
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

const getImagesEvent = async () => {
  let params = apiList["getImages"];
  params.queryparam = {
    adId: rowTarget.value[0].adId,
  };
  const { because, code, data, message, result } =
    await useConnectionStore.request(params);
  if (code == 200) {
    if (data.length === 0) {
      toast.warning(t("NO_DATA_SEARCH", [t("IMAGE")]));
    }
    ImageData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("IMAGE")]));
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
      formData.append("files", file);
    }
  };

  if (ImageData.value.length < 5) {
    [].forEach.call(e.target.files, multipleFile);
    const requestObj = {
      adId: rowTarget.value[0].adId,
      userLocale: "ko_kr",
    };
    formData.append(
      "request",
      new Blob([JSON.stringify(requestObj)], { type: "application/json" })
    );
    params.queryparam = formData;

    const { because, code, data, message, result } =
      await useConnectionStore.request(params);
    if (code == 200) {
      getImagesEvent();
      toast.success(t("SUCCESS_IMAGE_UPLOAD"));
    } else {
      toast.error(t("ERROR_IMAGE_UPLOAD"));
    }
  } else {
    toast.warning(t("WARNING_IMAGE_CHECK_LENGTH"));
  }
};
const deleteImgaeEvent = async (item) => {
  let params = apiList["deleteImage"];

  const { status, code, message, data, because } =
    await useConnectionStore.request(params);
  if (code == 200) {
    getImagesEvent();
    deleteImagesModal.value = false;
    toast.success(t("SUCCESS_IMAGE_REMOVE"));
    files.value = [];
  } else {
    toast.error(t("ERROR_IMAGE_REMOVE"));
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
        createListModal.value = true;
      },
    },
    {
      methodName: "BTN_UPDATE",

      title: computed(() => t("BTN_UPDATE")),

      disabled: computed(
        () => !(rowTarget.value[0]?.adId && rowTarget.value.length == 1)
      ),

      event: () => {
        listData.adName = rowTarget.value[0].adName;
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
        :breadcrumb="true"
        @update:inputChange="(value, item) => (item.target = value)"
      >
      </SearchBarContainer>
    </v-col>
    <v-col cols="4" class="pt-0">
      <Skeleton :loadingData="tableData.length == 0" :height="tableHeight">
        <Tabulator
          :pagination="true"
          :columns="columns"
          :tableData="tableData"
          :height="tableHeight"
          @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
        />
      </Skeleton>
    </v-col>
    <v-col cols="8" class="pt-0">
      <Skeleton
        :loadingData="!rowTarget[0]?.adId"
        :height="tableHeight"
        type="heading,image,image,image"
      >
        <v-card rounded="lg" class="customCard" :height="tableHeight">
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-file-input
                  :label="t('FILE')"
                  color="primary"
                  clearable
                  hide-details
                  type="file"
                  variant="solo"
                  multiple
                  accept="image/png, image/jpeg, image/bmp"
                  counter
                  density="compact"
                  v-model="files"
                  :disabled="!(rowTarget[0]?.adId && rowTarget.length == 1)"
                  @change="uploadFileEvent($event)"
                >
                  <template v-slot:selection="{ fileNames }">
                    <template
                      v-for="(fileName, index) in fileNames"
                      :key="fileName"
                    >
                      <v-chip
                        v-if="index < 3"
                        color="deep-purple-accent-4"
                        label
                        size="small"
                        class="me-2"
                      >
                        {{ fileName }}
                      </v-chip>

                      <span
                        v-else-if="index === 3"
                        class="text-overline text-grey-darken-3 mx-2"
                      >
                        +{{ files.length - 3 }} File(s)
                      </span>
                    </template>
                  </template>
                </v-file-input></v-col
              >
              <v-col cols="6" v-for="(item, i) in ImageData" :key="i">
                <v-card rounded="lg" class="customCard">
                  <v-toolbar density="compact" color="white">
                    <v-toolbar-title>{{
                      item.originalFileName
                    }}</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn
                      icon="mdi-close"
                      @click="
                        [
                          (deleteImagesModal = true),
                          (apiList['deleteImage'].queryparam.adDetailId =
                            item.adDetailId),
                        ]
                      "
                    >
                    </v-btn>
                  </v-toolbar>
                  <v-img
                    max-height="125"
                    cover
                    :src="`${baseUrl}/v2/storage/images/viewer/${item.physicalFileName}`"
                  />
                </v-card>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
      </Skeleton>
    </v-col>
  </DefaultContainer>

  <ModalContainer
    :text="t('AD_ADD')"
    v-model="createListModal"
    @btnEvent="callAPIs('create')"
  >
    <v-form v-model="valid" ref="form" @submit.prevent="" class="text-center">
      <v-text-field
        variant="solo"
        class="pb-1"
        density="compact"
        :label="t('NAME')"
        v-model="listData.adName"
        :rules="[useUiStore.rules.required]"
        clearable
      />
    </v-form>
  </ModalContainer>

  <ModalContainer
    :text="t('AD_UPDATE')"
    v-model="updateListModal"
    @btnEvent="callAPIs('update')"
  >
    <v-form v-model="valid" ref="form"  @submit.prevent="" class="text-center">
      <v-text-field
        variant="solo"
        density="compact"
        class="pb-1"
        :label="t('NAME')"
        v-model="listData.adName"
        :rules="[useUiStore.rules.required]"
        clearable
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
  <ModalContainer
    :text="t('REMOVE_IMG')"
    v-model="deleteImagesModal"
    @btnEvent="deleteImgaeEvent()"
  >
    <v-card-text>
      {{ t("CHECK_REMOVE") }}
    </v-card-text>
  </ModalContainer>
</template>
<style scoped>

</style>
