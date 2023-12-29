<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";
import { userStore } from "@/store/UserStore";
import { connectionStore } from "@/store/ConnectionStore";
const useConnectionStore = connectionStore();
const props = defineProps(["item", "request"]);
const useUserStore = userStore();
const toast = useToast();
const { t } = useI18n();

const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const failHandleStatus = ref([]);
const getfailStatus = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/fail-status",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    failHandleStatus.value = data;
  } else {
    toast.error(message);
  }
};
watch(computedPortfolio, (newVal, oldVal) => {
  if (
    !!newVal &&
    computedPortfolio.value.userPortfolioMappingId &&
    props.request
  ) {
    getfailStatus();
  }
});
onMounted(() => {
  if (computedPortfolio.value?.userPortfolioMappingId && props.request) {
    getfailStatus();
  }
});
</script>
<template>
  <v-container fluid>
    <div :style="{ height: '100%' }" class="overflow-y-auto">
      <div v-if="failHandleStatus.length > 0">
        <div
          v-for="(item, i) in failHandleStatus"
          :key="i"
          class="py-2 d-flex justify-space-between align-center"
          cols="12"
        >
          <p class="noticeText">
            {{ item.text }} : {{ item?.activityStatus }}
            <v-tooltip
              v-if="props.item?.width < 30"
              activator="parent"
              location="top"
            >
              {{ item.text }} : {{ item?.activityStatus }}</v-tooltip
            >
          </p>

          <p class="text-disabled">
            {{ item?.updDt }}
          </p>
        </div>
      </div>

      <div
        :style="{ height: '100%' }"
        class="d-flex justify-center align-center"
        v-if="failHandleStatus.length == 0"
      >
        <p>고장 신고 내역이 없습니다.</p>
      </div>
    </div>
  </v-container>
</template>
<style lang="scss">
.noticeText {
  flex-basis: 70%;
  white-space: nowrap;
  overflow: hidden;

  text-overflow: ellipsis;
  font-size: 14px;
}
</style>
