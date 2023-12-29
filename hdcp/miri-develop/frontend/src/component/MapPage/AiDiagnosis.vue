<script setup>
import { ref, computed, onMounted, watch, reactive } from "vue";
import { useTheme } from "vuetify";
import { connectionStore } from "@/store/ConnectionStore";
import { useRouter, useRoute } from "vue-router";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";
import { userStore } from "@/store/UserStore";
const useUserStore = userStore();
const toast = useToast();
const theme = useTheme();
const router = useRouter();
const { t } = useI18n();
const useConnectionStore = connectionStore();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const aiCount = ref({
  allCount: 0,
  doorInvCount: 0,
  edgeCount: 0,
  mainInvCount: 0,
  otherCount: 0,
  ropeCount: 0,
  sheaveCount: 0,
});
const getAiInfo = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/unit-service/material/count-info",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    aiCount.value = data;
  }
};
watch(computedPortfolio, (newVal, oldVal) => {

  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    getAiInfo();
  }
});
onMounted(() => {
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getAiInfo();
  }
});
</script>
<template>
  <v-card width="360" elevation="0" class="customCard mt-3">
    <v-toolbar color="transparent" density="comfortable">
      <v-toolbar-title class="largeBoldText">{{
        t("MIRI_AI_CHECK")
      }}</v-toolbar-title>
      <v-card-actions>
        <v-btn
          color="primary"
          rounded="lg"
          variant="outlined"
          @click="router.push('/parts/lifeInquiry')"
          >{{ t("VIEW_DETAIL") }}</v-btn
        >
      </v-card-actions>
    </v-toolbar>
    <v-divider class="mx-4"></v-divider>
    <v-container class="py-5">
      <v-row>
        <v-col cols="6">
          <p class="smallBoldText pb-1">{{ t("MAP_MACHINE_LIFE") }}</p>

          <p class="font-weight-bold text-h4">{{ aiCount?.allCount ?? 0 }}</p>
        </v-col>
        <v-col cols="6">
          <v-row>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("ROPE") }}</p>
              <p class="boldText">{{ aiCount?.ropeCount ?? 0 }}</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("SHEVE") }}</p>
              <p class="boldText">{{ aiCount?.sheaveCount ?? 0 }}</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("INV") }}</p>
              <p class="boldText">{{ aiCount?.mainInvCount ?? 0 }}</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("DOOR_INV") }}</p>
              <p class="boldText">{{ aiCount?.doorInvCount ?? 0 }}</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="py-2 d-flex justify-space-between">
              <p class="">{{ t("SAFETY_EDGE") }}</p>
              <p class="boldText">{{ aiCount?.edgeCount ?? 0 }}</p>
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
