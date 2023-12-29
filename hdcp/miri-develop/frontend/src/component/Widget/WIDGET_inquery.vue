<script setup>
import { ref, reactive, onMounted, watch, computed } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
const useConnectionStore = connectionStore();
const useUserStore = userStore();
const props = defineProps(["item", "request"]);
const { t } = useI18n();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);

const accountData = reactive({
  deptNm: "영업소",
  mob: "00-0000-0000",
});

const getAccount = async () => {
  const { code, result, data, because, message } =
    await useConnectionStore.request({
      url: "/v2/contract/current/contact/list",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    if (data.length > 0) {
      data.forEach((item) => {
        if (item && !!item.mob == true) {
          item.mob = item.mob
            .replace(/[^0-9]/g, "")
            .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
            // eslint-disable-next-line
            .replace(/(\-{1,2})$/g, "");
        }
      });
      accountData.deptNm = data[0].deptNm;
      accountData.mob = data[0].mob;
      // enginnerList.value = data;
    }
  }
};

onMounted(() => {
  if (
    props.request == true &&
    computedPortfolio.value?.userPortfolioMappingId
  ) {
    getAccount();
  }
});
watch([computedPortfolio], (newVal, oldVal) => {
  if (!!newVal[0] && computedPortfolio.value.userPortfolioMappingId) {
    getAccount();
  }
});
</script>
<template>
  <v-container :style="{ cursor: 'default' }">
    <div class="d-flex align-center" :style="{ height: '100%' }">
      <div class="flex-grow-1">
        <div class="py-4 d-flex justify-space-between align-center">
          <p class="">현대 고객케어센터</p>
          <p class="text-disabled">1577-0603</p>
        </div>
        <div class="py-4 d-flex justify-space-between align-center">
          <p class="">{{ accountData.deptNm }}</p>
          <p class="text-disabled">{{ accountData.mob }}</p>
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
