<script setup>
import { userStore } from "@/store/UserStore";
import { ref, watch, onMounted, computed } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const useUserStore = userStore();
const useConnectionStore = connectionStore();
const companionDays = ref(0);
const toast = useToast();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const distanceAverage = ref(0);
const countInfo = ref({
  allCount: 0,
  failCount: 0,
});
const defaultAvatar = require("@/assets/img/defaultAvatar.png");

const getCompanionDays = async () => {
  const { data, because, result, code, message } = await useConnectionStore.request({
    url: "/v2/contract/current/companion-days",
    method: "post",
    queryparam: {},
  });
  if (code == 200) {
    companionDays.value = data;
  }
};
const getDistanceAverage = async () => {
  const { data, because, result, code, message } = await useConnectionStore.request({
    url: "/v2/hrts/car/distance/average",
    method: "post",
    queryparam: {
      userPortfolioMappingId: computedPortfolio?.value?.userPortfolioMappingId,
    },
  });
  if (code == 200) {
    distanceAverage.value = {
      elAvgRunDistance: data,
      startDate: result.split("/")[1],
      endDate: result.split("/")[2],
    };
  }
};
const getCountInfo = async () => {
  const { data, because, result, code, message } = await useConnectionStore.request({
    url: "/v2/hccc/current/count-info",
    method: "post",
    queryparam: {
      userPortfolioMappingId: computedPortfolio?.value?.userPortfolioMappingId,
    },
  });
  if (code == 200) {
    countInfo.value = data;
  }
};
watch(computedPortfolio, (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    getDistanceAverage();
    getCountInfo();
  }
});
onMounted(() => {
  // getCompanionDays();
  // getRunAvg();
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getDistanceAverage();
    getCountInfo();
  }
  getCompanionDays();
});
</script>
<template>
  <v-card width="360" class="customCard text-center" elevation="0">
    <v-container class="py-5">
      <v-row>
        <v-col cols="12">
          <v-img width="50" color="white" class="mx-auto" :src="defaultAvatar"></v-img>
        </v-col>
        <v-col class="largeBoldText" cols="12">
          <p>
            {{ t("INFO_MESSAGE1") }} {{ useUserStore.userInfo?.userName }}
            {{ t("INFO_MESSAGE2") }}
          </p>
          <p :style="{ color: 'green' }">
            {{ t("INFO_MESSAGE3") }} {{ companionDays }}{{ t("INFO_MESSAGE4") }}
          </p>
          <p>{{ t("INFO_MESSAGE5") }}</p>
        </v-col>
        <v-col>
          <v-progress-linear
            :max="
              countInfo.allCount + countInfo.failCount > 0
                ? countInfo.allCount
                : 100
            "
            :bg-color="countInfo.failCount > 0 ? 'red' : ''"
            :bg-opacity="countInfo.failCount > 0 ? 1 : ''"
            :model-value="countInfo.allCount - countInfo.failCount"
            height="25"
            color="#00C44F"
            rounded="xl"
          >
            <div class="d-flex align-center justify-space-between w-100 px-3">
              <strong class="progressText">
                {{
                  `${t("NORMAL_OPERATION")} (${
                    countInfo.allCount - countInfo.failCount
                  }${t("EL_NUMBER")})`
                }}
              </strong>
              <strong class="progressText">
                {{ ` ${t("ERROR")} (${countInfo.failCount}${t("EL_NUMBER")})` }}
              </strong>
            </div>
          </v-progress-linear>
        </v-col>
        <v-col cols="12">
          <div class="d-flex align-center justify-space-evenly">
            <p class="text-disabled ml-2 d-flex align-center">
              {{ t("MAP_FAULT_EL") }}
              <!-- <v-icon class="pl-2">mdi-help-circle-outline</v-icon> -->
            </p>
            <p>
              <span class="text-h5 font-weight-bold">
                {{ countInfo.failCount }}{{ t("EL_NUMBER") }}
              </span>
              <span class="text-h5 font-weight-bold"> / </span>
              <span class="font-weight-bold text-h5 text-disabled"
                >{{ t("TOTAL") }} {{ countInfo.allCount }}{{ t("EL_NUMBER") }}</span
              >
            </p>
          </div>
          <div></div>
        </v-col>
        <v-col cols="12">
          <div class="d-flex align-center justify-space-evenly">
            <p class="text-disabled" :style="{ cursor: 'pointer' }">
              {{ t("RUN_TOTAL_MONTH") }}
              <v-icon class="pl-2">mdi-help-circle-outline </v-icon>
              <v-tooltip activator="parent" location="top">
                통계성 DATA는 MIRI/HRTS connected 대상만 나타냅니다.
              </v-tooltip>
              <br>{{ distanceAverage?.startDate ? distanceAverage?.startDate + ' ~ ' + distanceAverage?.endDate :  t("SELECTING") + '..'  }}
            </p>
            <p class="text-h5 font-weight-bold">{{ distanceAverage.elAvgRunDistance ?? '' }}km</p>
          </div>
          <div class="text-center"></div>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>
