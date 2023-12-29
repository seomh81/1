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
import dayjs from "dayjs";
const toast = useToast();
const route = useRoute();
const { t } = useI18n();
const useUserStore = userStore();
const useConnectionStore = connectionStore();
const useUiStore = uiStore();
const rowTarget = ref([]);
const tableData = ref([]);
const accountStatusItem = ref([]);
const roleTypeItem = ref([]);
const delYnItem = ref([]);
const search = ref({
  startDate: "",
  endDate:"",
  accountStatus: "",
  roleType: "",
  delYn: "",
});
const exportCheckData = ref(false);
const height = 64 + 64 + 12;
const tableHeight = `calc(100vh - ${height}px)`;

const columns = computed(() => {
  return [
    {
      title: t("ID"),
      field: "userId",
      headerHozAlign: "center",
      hozAlign: "left",
    },
    {
      title: t("NAME"),
      field: "userName",
      headerHozAlign: "center",
      hozAlign: "left",
    },
    {
      title: t("ROLE"),
      field: "roleType",
      headerHozAlign: "center",
      hozAlign: "center",
    },
    {
      title: t("HP"),
      field: "phonenumber",
      headerHozAlign: "center",
      hozAlign: "center",
    },
    {
      title: t("STATUS"),
      field: "accountStatus",
      headerHozAlign: "center",
      hozAlign: "center",
    },
    {
      title: t("ACCOUNT_STATUS"),
      field: "delYn",
      headerHozAlign: "center",
      hozAlign: "center",
    },
    {
      title: t("ACCOUNT_CREATION_DATE"),
      field: "creationDt",
      headerHozAlign: "center",
      hozAlign: "center",
    },
    {
      title: t("MOBILE"),
      headerHozAlign: "center",
      hozAlign: "start",
      columns: [
        {
          title: t("FINAL_LOGIN_TIME"),
          field: "appLoginTime",
          headerHozAlign: "center",
          hozAlign: "center",
        },
        {
          title: t("LOGIN_COUNT"),
          field: "appLoginCnt",
          headerHozAlign: "center",
          hozAlign: "center",
        },
      ]
    },
    {
      title: t("PERSONAL_COMPUTER"),
      headerHozAlign: "center",
      hozAlign: "start",
      columns: [
        {
          title: t("FINAL_LOGIN_TIME"),
          field: "pcLoginTime",
          headerHozAlign: "center",
          hozAlign: "center",
        },
        {
          title: t("LOGIN_COUNT"),
          field: "pcLoginCnt",
          headerHozAlign: "center",
          hozAlign: "center",
        },
      ]
    },
  ];
});
const getLoginData = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/miri/portal/loginData",
      method: "post",
      queryparam: {
        startDate: search.value.startDate.replace(/-/g, ""),
        endDate: search.value.endDate.replace(/-/g, ""),
        roleType: search.value.roleType.code,
        accountStatus : search.value.accountStatus.code,
        delYn : search.value.delYn.code,
      },
    });
  if(code == 200){
    tableData.value = data;

    if (data.length == 0) {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_SYSTEM_VIEWLOGINDATA")]));
    }
  }
  else{
    toast.error(t("ERROR_SEARCH", [t("MENU_SYSTEM_VIEWLOGINDATA")]));
  }
};
const exportCheckDataFnc = () => {
  exportCheckData.value = true;
  setTimeout(() => {
    exportCheckData.value = false;
  }, 200);
};
const searchBar = reactive({
  inputs: [
  {
      name: "startDate",
      cols: 2,
      title: computed(() => t("START_DAY")),
      type: "datePicker",
      target: computed({
        get() {
          return search.value.startDate;
        },
        set(e) {
          search.value.startDate = e;
        },
      }),
    },
    {
      name: "endDate",
      cols: 2,
      title: computed(() => t("END_DAY")),
      type: "datePicker",
      target:computed({
        get() {
          return search.value.endDate;
        },
        set(e) {
          search.value.endDate = e;
        },
      }),
    },
    {
      cols: 2,
      type: "select",
      title: computed(() => t("ROLE")),

      items: computed(() => roleTypeItem.value),
      itemTitle: "value1",
      target: computed({
        get() {
          return search.value.roleType;
        },
        set(e) {
          search.value.roleType = e;
        },
      }),
    },
    {
      cols: 2,
      type: "select",
      title: computed(() => t("STATUS")),

      items: computed(() => accountStatusItem.value),
      itemTitle: "value1",
      target: computed({
        get() {
          return search.value.accountStatus;
        },
        set(e) {
          search.value.accountStatus = e;
        },
      }),
    },
    {
      cols: 2,
      type: "select",
      title: computed(() => t("ACCOUNT_STATUS")),

      items: computed(() => delYnItem.value),
      itemTitle: "value1",
      target: computed({
        get() {
          return search.value.delYn;
        },
        set(e) {
          search.value.delYn = e;
        },
      }),
    },
  ],
  buttons: [
    {
      methodName: "BTN_SEARCH",
      title: t("BTN_SEARCH"),
      event: () => {
        getLoginData();
      },
    },
    {
      methodName: "BTN_EXPORT",
      title: t("BTN_EXPORT"),
      icon: "mdi-export",
      event: () => {
        exportCheckDataFnc();
      },
    }
  ],
});
provide("searchBar", searchBar);

const getAccountStatus = async () => {
  const data = await useUiStore.getAccountStatus();
  search.value.accountStatus = data.defaultAccountStatus;
  accountStatusItem.value = data.accountStatusItem;
};
const getRoleType = async () => {
  const data = await useUiStore.getRoleType();
  search.value.roleType = data.defaultItem;
  roleTypeItem.value = data.items;
};
const getDelYn = async () => {
  const data = await useUiStore.getDelYn();
  search.value.delYn = data.defaultDelYn;
  delYnItem.value = data.delYnItem;
};

onMounted(async () => {
  search.value.startDate = dayjs().subtract(3, "month").format("YYYY-MM-DD");
  search.value.endDate   =  dayjs().format("YYYY-MM-DD");
  await getRoleType();
  await getAccountStatus();
  await getDelYn();
  await getLoginData();
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
          :export="exportCheckData"
          :exportFileName="'로그인현황_' + dayjs().format('YYYYMMDD')"
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
