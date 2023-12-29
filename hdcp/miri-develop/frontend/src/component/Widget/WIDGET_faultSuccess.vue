<script setup>
import { ref, reactive, onMounted, watch, computed } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
import dayjs from "dayjs";
const useConnectionStore = connectionStore();
const props = defineProps(["item", "request"]);
const { t } = useI18n();
const failCount = ref({});
const useUserStore = userStore();

const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);

const getfailCount = async () => {
  const { code, data } = await useConnectionStore.request({
    url: "/v2/hccc/current/fail-count",
    method: "post",
    queryparam: {
      userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      startMonth: dayjs().subtract(1, "month").format("YYYYMMDD"),
      endMonth: dayjs().format("YYYYMMDD"),
    },
  });
  if (code == 200) {
    failCount.value = data;
  }
};
onMounted(async () => {
  if (computedPortfolio.value?.userPortfolioMappingId && props.request) {
    getfailCount();
  }
});
watch(computedPortfolio, async (newVal, oldVal) => {
  if (
    !!newVal &&
    computedPortfolio.value?.userPortfolioMappingId &&
    props.request
  ) {
    getfailCount();
  }
});
</script>
<template>
  <v-container :style="{ cursor: 'default' }">
    <div
      :style="{ height: '100%' }"
      class="d-flex align-center justify-space-around"
    >
      <div>
        <p class="font-weight-bold text-h4">
          {{ t("TOTAL") }} {{ failCount.failComplete ?? 0 }}{{ t("NUMBER2") }}
        </p>
      </div>
      <div>
        <div cols="12" class="py-2 d-flex justify-space-between">
          <p class="mr-10">{{ t("MAP_FAIL_DETECT") }}</p>
          <p class="boldText">{{ failCount.failDetect ?? 0 }}</p>
        </div>
        <div cols="12" class="py-2 d-flex justify-space-between">
          <p class="mr-10">{{ t("MAP_REMOTE_REPAIR") }}</p>
          <p class="boldText">{{ failCount.remoteRepair ?? 0 }}</p>
        </div>
        <div cols="12" class="py-2 d-flex justify-space-between">
          <p class="mr-10">{{ t("MAP_FIELD_REPAIR") }}</p>
          <p class="boldText">{{ failCount.fieldRepair ?? 0 }}</p>
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
