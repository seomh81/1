<script setup>
import { ref, reactive, onMounted, computed, watch } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
import dayjs from "dayjs";
const useUserStore = userStore();
const useConnectionStore = connectionStore();
const props = defineProps(["item", "request"]);
const { t } = useI18n();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);

const plusService = reactive({
  robot: "0",
  call: "0",
  view: "대기",
});
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
      plusService.view = data.callCount;
      // selfInspCount.value = data;
    } else {
      plusService.view = "연결실패";
    }
  };
  const getRobotCallCount = async () => {
    const { because, code, data, message, result } =
      await useConnectionStore.request({
        url: "/v2/miri/robot/callcount",
        method: "post",
        queryparam: {
          startDate: dayjs().subtract(1, "month").format("YYYYMMDD"),
          endDate: dayjs().format("YYYYMMDD"),
          userPortfolioMappingId:
            computedPortfolio.value.userPortfolioMappingId,
        },
      });
    if (code == 200) {
      plusService.robot = data.callCount;
      // selfInspCount.value = data;
    } else {
      plusService.robot = "연결실패";
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
      plusService.call = data.callCount;
      // selfInspCount.value = data;
    } else {
      plusService.call = "연결실패";
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

  if (computedPortfolio.value?.userPortfolioMappingId && props.request) {
    getCalls();
  }
});
watch([computedPortfolio], (newVal, oldVal) => {
  if (
    !!newVal[0] &&
    computedPortfolio.value.userPortfolioMappingId &&
    props.request
  ) {
    getCalls();
  }
});
</script>
<template>
  <v-container :style="{ cursor: 'default' }">
    <div
      :style="{ height: '100%' }"
      class="d-flex justify-space-around align-center"
    >
      <div>
        <p class="smallBoldText">{{ t("MAP_PLUS_SERVICE") }}</p>
      </div>
      <div>
        <div class="py-2 d-flex justify-space-between">
          <p class="mr-10">{{ t("ROBOT") }}</p>
          <p class="text-medium-emphasis">{{ plusService.robot }}</p>
        </div>
        <div class="py-2 d-flex justify-space-between">
          <p class="mr-10">{{ t("CALL") }}</p>
          <p class="text-medium-emphasis">{{ plusService.call }}</p>
        </div>
        <div class="py-2 d-flex justify-space-between">
          <p class="mr-10">{{ t("VIEW") }}</p>
          <p class="text-medium-emphasis">{{ plusService.view }}</p>
        </div>
      </div>
    </div>
  </v-container>
</template>
<style lang="scss">
.noticeText {
  font-size: 14px;
}
</style>
