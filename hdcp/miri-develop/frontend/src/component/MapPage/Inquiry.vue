<script setup>
import { ref, reactive, onMounted, computed, watch } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
import { useI18n } from "vue-i18n";
import ServiceRequestModal from "@/component/ServiceRequestModal.vue";
import { userStore } from "@/store/UserStore";
import { useRouter, useRoute } from "vue-router";
const useUserStore = userStore();
const router = useRouter();
const useConnectionStore = connectionStore();
const { t } = useI18n();

const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);

const accountData = reactive({
  deptNm: "영업소",
  mob: "00-0000-0000",
});
const dialog = ref(false);
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
    // if (data.length > 0) {
    //   data.forEach((item) => {
    //     if (item && !!item.mob == true) {
    //       item.mob = item.mob
    //         .replace(/[^0-9]/g, "")
    //         .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3")
    //         // eslint-disable-next-line
    //         .replace(/(\-{1,2})$/g, "");
    //     }
    //   });
    //   // enginnerList.value = data;
    // }
    accountData.deptNm = data[0]?.deptNm;
    accountData.mob = data[0]?.mob;
  }
};
onMounted(() => {
  if (computedPortfolio.value?.userPortfolioMappingId) {
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
  <v-card width="360" class="mt-3 customCard noticeCard" elevation="0">
    <v-toolbar color="transparent" density="comfortable">
      <v-toolbar-title class="largeBoldText">{{
        t("MAP_INQUERY")
      }}</v-toolbar-title>
      <v-card-actions>
        <v-btn
          rounded="lg"
          color="primary"
          variant="outlined"
          @click="dialog = true"
          >{{ t("SERVICE_REQUEST") }}</v-btn
        >
      </v-card-actions>
    </v-toolbar>
    <v-divider class="mx-4"></v-divider>

    <v-container class="py-5">
      <v-row>
        <v-col cols="12" class="d-flex justify-space-between align-center">
          <p class="">현대 고객케어센터</p>
          <p class="text-disabled">1577-0603</p>
        </v-col>
        <v-col cols="12" class="d-flex justify-space-between align-center">
          <p class="">{{ accountData.deptNm }}</p>
          <p class="text-disabled">{{ accountData.mob }}</p>
        </v-col>
      </v-row>
    </v-container>
  </v-card>

  <ServiceRequestModal v-model="dialog" v-if="dialog"></ServiceRequestModal>
</template>
<style lang="scss">
.noticeText {
  font-size: 14px;
}
</style>
