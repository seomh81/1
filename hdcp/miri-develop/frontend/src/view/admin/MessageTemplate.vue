<script setup>
import {
  ref,
  reactive,
  computed,
  onMounted,
  watch,
  provide,
} from "vue";
import {
  DefaultContainer,
  SearchBarContainer,
  Tabulator,
  Skeleton,
} from "@/component/Template";

import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const toast = useToast();
const { t } = useI18n();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();

const rowTarget = ref([]);
const tableData = ref([]);
const height = 64 + 64 + 12;
const tableHeight = `calc(100vh - ${height}px)`;

const selectedData = ref({});
watch(
  rowTarget,
  (newVal, oldVal) => {
    if (rowTarget.value.length == 0) {
      selectedData.value = {};
    } else {
      selectedData.value = {
        msgTemplateNote: newVal[0].msgTemplateNote,
        msgTemplateId: newVal[0].msgTemplateId,
        msgTemplateContents: newVal[0].msgTemplateContents,
        msgTemplateName: newVal[0].msgTemplateName,
      };
    }
  },
  { deep: true }
);
const columns = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      maxWidth: "70",
      headerSort: false,
      hozAlign: "center",
      vertAlign: "middle",
      headerHozAlign: "center",
    },
    {
      title: t("NOTIFICATION_NOTE"),
      field: "msgTemplateNote",
      sorter: "string",
    },
    {
      title: t("REGIST_DATE"),
      field: "registDt",
      sorter: "string",
    },
  ];
});

const getMessages = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/cc/alarm/msg-template/search",
      method: "post",
      queryparam: {
        searchKeyword: searchBar.inputs.find((item) => item.name == "keyword")
          .target,
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      selectedData.value = {};
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_SYSTEM_MESSAGE_TEMPLATE")]));
    }
    tableData.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("MENU_SYSTEM_MESSAGE_TEMPLATE")]));
  }
};
const updateMessage = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/cc/alarm/msg-template/update",
      method: "post",
      queryparam: {
        msgTemplateId: selectedData.value.msgTemplateId,
        msgTemplateNote: selectedData.value.msgTemplateNote,
        msgTemplateContents: selectedData.value.msgTemplateContents,
      },
    });
  if (code == 200) {
    getMessages();
    toast.success(t("SUCCESS_UPDATE"));
  } else {
    toast.error(t("ERROR_UPDATE"));
  }
};

const searchBar = reactive({
  inputs: [
    {
      name: "keyword",
      type: "field",
      label: computed(() => t("KEYWORD")),
      target: "",
      event: () => {
        getMessages();
      },
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",

      title: computed(() => t("BTN_SEARCH")),

      event: () => {
        getMessages();
      },
    },
  ],
});
onMounted(async () => {
  const { defaultItem, items } = await useUiStore.getAlarmType();
  searchBar.inputs.find((item) => {
    if (item.name == "serviceType") {
      item.items = items;
      item.target = defaultItem;
    }
  });
  getMessages();
});
provide("searchBar", searchBar);
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" class="pb-0">
      <SearchBarContainer :breadcrumb="true"> </SearchBarContainer>
    </v-col>
    <v-col cols="4" class="pt-0">
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
    <v-col cols="8" class="pt-0 align-self-center">
      <Skeleton
        :height="tableHeight"
        :padding="true"
        :loadingData="!selectedData.msgTemplateId"
      >
        <v-text-field
          variant="solo"
          density="compact"
          :label="t('NOTIFICATION_NAME')"
          disabled
          v-model="selectedData.msgTemplateName"
          :rules="[useUiStore.rules.required]"
          clearable
        />
        <v-text-field
          variant="solo"
          density="compact"
          disabled
          :label="t('NOTIFICATION_NOTE')"
          v-model="selectedData.msgTemplateNote"
          :rules="[useUiStore.rules.required]"
          clearable
        />
        <v-textarea
          rounded="lg"
          rows="21"
          v-model="selectedData.msgTemplateContents"
        ></v-textarea>

        <div class="text-end">
          <v-btn
            class="text-white customBtn mt-4 ml-auto"
            color="primary"
            rounded="lg"
            size="large"
            @click="updateMessage"
            >{{ t("OK") }}</v-btn
          >
        </div>
      </Skeleton>
    </v-col>
  </DefaultContainer>
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
