<script setup>
import { ref, computed, onMounted, watch, reactive } from "vue";
import { useTheme } from "vuetify";
import { connectionStore } from "@/store/ConnectionStore";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";
import dayjs from "dayjs";
import { userStore } from "@/store/UserStore";
const { t } = useI18n();
const useUserStore = userStore();
const toast = useToast();
const props = defineProps(["elNoList"]);
const theme = useTheme();
const useConnectionStore = connectionStore();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const failCount = ref({
  failComplete: 0,
  failDetect: 0,
  fieldRepair: 0,
  remoteRepair: 0,
});
const plusService = ref({
  robot: "0",
  call: "0",
  view: "대기",
});
const date = reactive({
  startMonth: "",
  endMonth: "",
});
const startMonth = ref(
  `${new Date().getFullYear()}${("0" + (new Date().getMonth() + 1)).slice(-2)}`
);
const endMonth = ref(
  `${new Date().getFullYear()}${("0" + (new Date().getMonth() + 1)).slice(-2)}`
);
const selfInspCount = ref({
  field: 0,
  remote: 0,
  success: 0,
  total: 0,
});
const getfailCount = async (startDate, endDate) => {
  const { code, data } = await useConnectionStore.request({
    url: "/v2/hccc/current/fail-count",
    method: "post",
    queryparam: {
      userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      startMonth: startDate,
      endMonth: endDate,
    },
  });
  if (code == 200) {
    failCount.value = data;
  }
};
const getSelfInspCount = async (startDate, endDate) => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/cc/portfolio/self-insp/count-info",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
        startMonth: startDate,
        endMonth: endDate,
      },
    });
  if (code == 200) {
    selfInspCount.value = data;
  }
};
const calcNowDate = () => {
  startMonth.value = dayjs().format("YYYYMM");
  endMonth.value = dayjs().format("YYYYMM");

  getSelfInspCount(startMonth.value, endMonth.value);
  getfailCount(startMonth.value, endMonth.value);
  getCalls(startMonth.value, endMonth.value);
};
const calcQuarterDate = () => {
  startMonth.value = dayjs().subtract(2, "month").format("YYYYMM");
  endMonth.value = dayjs().format("YYYYMM");

  getSelfInspCount(startMonth.value, endMonth.value);
  getfailCount(startMonth.value, endMonth.value);
  getCalls(startMonth.value, endMonth.value);
};
const calcYearDate = () => {
  startMonth.value = dayjs().subtract(11, "month").format("YYYYMM");
  endMonth.value = dayjs().format("YYYYMM");

  getSelfInspCount(startMonth.value, endMonth.value);
  getfailCount(startMonth.value, endMonth.value);
  getCalls(startMonth.value, endMonth.value);
};
const getCalls = (startDate, endDate, elNoList) => {
  const getViewCallCount = async () => {
    const { because, code, data, message, result } =
      await useConnectionStore.request({
        url: "/v2/miri/view/callcount",
        method: "post",
        queryparam: {
          startDate: startDate,
          endDate,
          userPortfolioMappingId:
            computedPortfolio.value.userPortfolioMappingId,
        },
      });
    if (code == 200) {
      plusService.value.view = data.callCount;
      // selfInspCount.value = data;
    } else {
      plusService.value.view = t("FAIL_CONNECT");
    }
  };
  const getRobotCallCount = async () => {
    const { because, code, data, message, result } =
      await useConnectionStore.request({
        url: "/v2/miri/robot/callcount",
        method: "post",
        queryparam: {
          startDate: `${startDate}01`,
          endDate: `${endDate}31`,
          userPortfolioMappingId:
            computedPortfolio.value.userPortfolioMappingId,
        },
      });
    if (code == 200) {
      plusService.value.robot = data.callCount;
      // selfInspCount.value = data;
    } else {
      plusService.value.view = t("FAIL_CONNECT");
    }
  };
  const getCallCallCount = async () => {
    const { because, code, data, message, result } =
      await useConnectionStore.request({
        url: "/v2/miri/call/callcount",
        method: "post",
        queryparam: {
          startDate,
          endDate,
          userPortfolioMappingId:
            computedPortfolio.value.userPortfolioMappingId,
        },
      });
    if (code == 200) {
      plusService.value.call = data.callCount;
      // selfInspCount.value = data;
    } else {
      plusService.value.view = t("FAIL_CONNECT");
    }
  };
  getViewCallCount();
  getCallCallCount();
  getRobotCallCount();
  // } catch {
  //   toast.error("정기 점검 데이터 조회에 실패했습니다.");
  // }
};
onMounted(() => {
  // getCompanionDays();
  // getRunAvg();

  if (computedPortfolio.value?.userPortfolioMappingId) {
    calcNowDate();
  }
});
watch([computedPortfolio], (newVal, oldVal) => {
  if (!!newVal[0] && computedPortfolio.value.userPortfolioMappingId) {
    calcNowDate();
  }
});
const toggle = ref(1);
</script>
<template>
  <v-card width="360" elevation="0" class="customCard">
    <v-toolbar color="transparent" density="comfortable">
      <v-toolbar-title class="largeBoldText">{{
        t("MIRI_AI_CONNECT")
      }}</v-toolbar-title>
      <v-card-actions>
        <v-btn
          @click="[calcNowDate(), (toggle = 1)]"
          rounded="lg"
          color="primary"
          :active="toggle == 1"
          size="small"
          variant="outlined"
          >{{ t("LAST_MONTH2") }}</v-btn
        >
        <v-btn
          @click="[calcQuarterDate(), (toggle = 2)]"
          rounded="lg"
          :active="toggle == 2"
          selected-class="test"
          color="primary"
          size="small"
          variant="outlined"
          >{{ t("NOW_QUTER") }}</v-btn
        >
        <v-btn
          :active="toggle == 3"
          @click="[calcYearDate(), (toggle = 3)]"
          rounded="lg"
          color="primary"
          size="small"
          variant="outlined"
          >{{ t("YEARS") }}</v-btn
        >
      </v-card-actions>
    </v-toolbar>
    <v-divider class="mx-4"></v-divider>

    <!-- <v-card-actions class="justify-center py-0">
      <v-btn icon variant="plain" @click="prevMonth"
        ><v-icon>mdi-chevron-left</v-icon></v-btn
      >
      <v-btn size="large" variant="plain" id="calendarTitle">
        {{ ` ${selectMonth}월 ${selectYears}` }}
      </v-btn>
      <div id="datePickerBox"></div>
      <v-btn icon variant="plain" @click="nextMonth"
        ><v-icon>mdi-chevron-right</v-icon></v-btn
      >
    </v-card-actions> -->

    <v-container class="py-5">
      <v-row>
        <v-col cols="6">
          <p class="smallBoldText pb-1">{{ t("MAP_FAULT_IDX") }}</p>

          <p class="font-weight-bold text-h4">
            {{ failCount.failComplete ?? 0 }}
          </p>
        </v-col>
        <v-col cols="6">
          <v-row>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("MAP_FAIL_DETECT") }}</p>
              <p class="boldText">{{ failCount.failDetect ?? 0 }}</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("MAP_REMOTE_REPAIR") }}</p>
              <p class="boldText">{{ failCount.remoteRepair ?? 0 }}</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("MAP_FIELD_REPAIR") }}</p>
              <p class="boldText">{{ failCount.fieldRepair ?? 0 }}</p>
            </v-col>
          </v-row>
        </v-col>
        <v-divider></v-divider>
      </v-row>
      <v-row>
        <v-col cols="6">
          <p class="smallBoldText pb-1">
            {{ t("MAP_SELF_INSPECTION_IDX") }}
          </p>

          <p class="font-weight-bold text-h4">
            {{ `${selfInspCount.success ?? 0}` }}
          </p>
        </v-col>
        <v-col cols="6">
          <v-row>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("MAP_TARGET") }}</p>
              <p class="boldText">
                {{ ` ${selfInspCount.total ?? 0}` }}
              </p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("MAP_REMOTE") }}</p>
              <p class="boldText">
                {{ `${selfInspCount.remote ?? 0}` }}
              </p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("MAP_FIELD") }}</p>
              <p class="boldText">{{ selfInspCount.field ?? 0 }}</p>
            </v-col>
          </v-row>
        </v-col>
        <v-divider></v-divider>
      </v-row>
      <v-row>
        <v-col cols="6">
          <p class="smallBoldText">{{ t("MAP_PLUS_SERVICE") }}</p>
        </v-col>
        <v-col cols="6">
          <v-row>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("ROBOT") }}</p>
              <p class="text-medium-emphasis">{{ plusService.robot }}</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("CALL") }}</p>
              <p class="text-medium-emphasis">{{ plusService.call }}</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("VIEW") }}</p>
              <p class="text-medium-emphasis">{{ plusService.view }}</p>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>
<style lang="scss">
.FacilityProgress {
  .v-progress-linear__background {
    display: none;
  }
  .v-progress-linear__determinate {
    border-radius: 20px;
  }
}
</style>
