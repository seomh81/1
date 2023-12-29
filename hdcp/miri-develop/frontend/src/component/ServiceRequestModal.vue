<script setup>
import { connectionStore } from "@/store/ConnectionStore";
import { useToast } from "vue-toastification";
import { userStore } from "@/store/UserStore";
import { uiStore } from "@/store/UiStore";
import { useI18n } from "vue-i18n";
import { ref, computed, watch, onMounted } from "vue";
import { dataStore } from "@/store/DataStore";
const props = defineProps(["modelValue"]);
const { t } = useI18n();
const emits = defineEmits(["update:modelValue"]);
const useDataStore = dataStore();
const computedOpens = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emits("update:modelValue", value);
  },
});
const useUiStore = uiStore();
const contractData = ref([]);
const projectHoNoArr = ref([]);
const projectNumberArr = ref([]);
const serviceTypeArr = ref([]);
const toast = useToast();
const failTypeArr = ref([]);
const selectedProjectNumber = ref();
const selectedHoNo = ref();
const selectedServiceCode = ref();
const selectedFaultServiceCode = ref();
const requestDetailText = ref();
const useUserStore = userStore();
const useConnectionStore = connectionStore();
const getContractDetails = async (userPortfolioMappingId) => {
  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "/v2/contract/portfolio/details",
      method: "post",
      queryparam: {
        userPortfolioMappingId:
          useUserStore.portfolio.selected.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    let newArr = [];
    data.contractList.forEach((item) => {
      newArr.push(...item.projectList);
    });
    newArr.forEach((item) => {
      projectNumberArr.value.push({
        siteNm: `${item?.siteNm} - ${item.prjNo}`,
        prjNo: item.prjNo,
        contractCode: item.contractCode,
      });
    });
    contractData.value = data;
  }
};
const requestService = async () => {
  if(selectedServiceCode.value.code == "0010")
  {
    if(!confirm(t("CONFIRM_SERVICE_REQUEST")))
    {
      return false;
    }
  }

  const { data, result, because, message, code } =
    await useConnectionStore.request({
      url: "/v2/cc/service/request",
      method: "post",
      queryparam: {
        prjNo: selectedProjectNumber.value.prjNo,
        hoNo: selectedHoNo.value,
        serviceType: selectedServiceCode.value.code,
        serviceTypeNm: selectedServiceCode.value.value1,
        serviceCd: selectedFaultServiceCode.value,
        contents: requestDetailText.value,
        userName: useUserStore.userInfo.userName,
        phonenumber : useUserStore.userInfo.phonenumber,
        userId : useUserStore.userInfo.userId,
      },
    });
  if (code == 200) {
    toast.success(t("SERVICE_REQUEST_SUCCESS"));
    emits("update:modelValue", false);
  } else {
    toast.error(t("SERVICE_REQUEST_ERROR"));
  }
};

watch(
  [
    selectedProjectNumber,
    selectedHoNo,
    selectedServiceCode,
    selectedFaultServiceCode,
  ],
  async (newVal, oldVal) => {
    if (newVal[0] && newVal[0] !== oldVal[0]) {
      findElHoNo(newVal[0].contractCode, newVal[0].prjNo);
    }
    if (newVal[1] && newVal[1] !== oldVal[1]) {
      selectedServiceCode.value = undefined;
      selectedFaultServiceCode.value = undefined;
      requestDetailText.value = undefined;

      const { items } = await useUiStore.getServcieType();
      items.splice(
        items.findIndex((item) => item.code == "ALL"),
        1
      );
      serviceTypeArr.value = items;
    }
    if (newVal[2] && newVal[2] !== oldVal[2]) {
      selectedFaultServiceCode.value = undefined;
      requestDetailText.value = undefined;

      const { items } = await useUiStore.getFailType();

      failTypeArr.value = items.filter((item) => {
        return item.code !== "010102";
      });
    }
    if (newVal[3] && newVal[3] !== oldVal[3]) {
      requestDetailText.value = undefined;
    }
  },
  { deep: true }
);

const findElHoNo = (contractCode, prjNo) => {
  projectHoNoArr.value = [];
  selectedHoNo.value = undefined;
  selectedServiceCode.value = undefined;
  selectedFaultServiceCode.value = undefined;
  requestDetailText.value = undefined;
  let carListArr = [];
  contractData.value.contractList.forEach((item) => {
    if (item.contractCode === contractCode) {
      const project = item.projectList.find(
        (project) => project.prjNo === prjNo
      );
      carListArr = project.carList;
    }
  });

  if (carListArr.length > 0) {
    carListArr.forEach((item) => {
      projectHoNoArr.value.push({
        installationPlace: `${item.installationPlace} - ${item.hoNo}`,
        hoNo: item.hoNo,
      });
    });
  } else {
    toast.warning(t("WARNING_HO_NO_LIST"));
  }
  projectHoNoArr.value.sort(
    (a, b) => Number(a.hoNo.slice(-2)) - Number(b.hoNo.slice(-2))
  );

  if(useDataStore?.targetServiceElInfo)
  {
    selectedHoNo.value = projectHoNoArr.value.find((item) => item.hoNo == useDataStore.targetServiceElInfo.hoNo).hoNo;
    useDataStore.setServiceElInfo({});
  }
};
onMounted(async () => {
  await getContractDetails();
  if(useDataStore?.targetServiceElInfo)
    {
      selectedProjectNumber.value = projectNumberArr.value.find((item) => item.prjNo == useDataStore.targetServiceElInfo.prjNo);
    }
});
</script>
<template>
  <v-dialog v-model="computedOpens" no-click-animation persistent width="500px">
    <v-card class="customCard py-3">
      <v-toolbar compact="density" color="transparent">
        <v-toolbar-title>{{ t("SERVICE_REQUEST") }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn
          rounded="xl"
          icon
          dark
          @click="emits('update:modelValue', false)"
        >
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-toolbar>
      <v-container>
        <v-row>
          <v-col cols="3" class="justify-center d-flex align-center">
            {{ t("BULD_NM") }}
          </v-col>
          <v-col cols="9"
            ><v-select
              :label="selectedProjectNumber ? t('BULD_NM') : t('CHECK_BULD_NM')"
              density="compact"
              variant="solo"
              return-object
              hide-details
              v-model="selectedProjectNumber"
              :items="projectNumberArr"
              item-title="siteNm"
            ></v-select
          ></v-col>
          <v-col cols="3" class="justify-center d-flex align-center">{{
            t("HO_NO")
          }}</v-col>
          <v-col cols="9"
            ><v-select
              density="compact"
              :label="selectedHoNo ? t('HO_NO') : t('CHECK_HO_NO')"
              variant="solo"
              hide-details
              v-model="selectedHoNo"
              :items="projectHoNoArr"
              item-title="installationPlace"
              item-value="hoNo"
              :disabled="projectHoNoArr.length === 0 ? true : false"
            ></v-select
          ></v-col>
          <v-col cols="3" class="justify-center d-flex align-center">{{
            t("SERVICE_TYPE")
          }}</v-col>
          <v-col cols="9"
            ><v-select
              :disabled="!selectedHoNo"
              density="compact"
              :label="
                selectedServiceCode
                  ? t('SERVICE_TYPE')
                  : t('CHECK_SERVICE_TYPE')
              "
              variant="solo"
              v-model="selectedServiceCode"
              :items="serviceTypeArr"
              item-title="value1"
              item-value="code"
              hide-details
              return-object
            ></v-select>
          </v-col>
          <v-col
            cols="3"
            v-if="selectedServiceCode?.code == '0010'"
            class="justify-center d-flex align-center"
            >{{ t("FAIL_TYPE") }}</v-col
          >
          <v-col cols="9" v-if="selectedServiceCode?.code == '0010'"
            ><v-select
              density="compact"
              :label="
                selectedFaultServiceCode ? t('FAIL_TYPE') : t('CHECK_FAIL_TYPE')
              "
              variant="solo"
              hide-details
              item-title="value1"
              item-value="code"
              :items="failTypeArr"
              v-model="selectedFaultServiceCode"
            ></v-select>
          </v-col>
          <v-col cols="3" class="justify-center d-flex align-center">{{
            t("DETAIL")
          }}</v-col>
          <v-col cols="9">
            <v-textarea
              hide-details
              :disabled="!selectedServiceCode?.code"
              v-model.trim="requestDetailText"
              density="compact"
              variant="solo"
              rows="3"
            ></v-textarea>
          </v-col>
        </v-row>
      </v-container>

      <div class="my-4 px-8 text-end">
        <v-btn
          class="text-white customBtn"
          color="primary"
          rounded="lg"
          size="large"
          :disabled="
            (selectedProjectNumber &&
              selectedHoNo &&
              selectedServiceCode?.code == '0010' &&
              selectedFaultServiceCode &&
              requestDetailText) ||
            (selectedProjectNumber &&
              selectedHoNo &&
              selectedServiceCode &&
              selectedServiceCode?.code !== '0010' &&
              requestDetailText)
              ? false
              : true
          "
          @click="requestService"
          >{{ t("SERVICE_REQUEST") }}</v-btn
        >
        <v-btn
          class="customBtn"
          rounded="lg"
          size="large"
          color="#eee"
          @click="emits('update:modelValue', false)"
        >
          {{ t("CANCEL") }}
        </v-btn>
      </div>
    </v-card>
  </v-dialog>
</template>
