<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";
import { userStore } from "@/store/UserStore";
import { connectionStore } from "@/store/ConnectionStore";
import { useRouter, useRoute } from "vue-router";
const router = useRouter();
const useConnectionStore = connectionStore();
const useUserStore = userStore();
const toast = useToast();
const { t } = useI18n();

const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const FailHandleStatus = ref([{
  text: '[ 00빌딩 ] - 1호기 : 연결 대기',
  updDt: "2000-01-01",
  activityStatus:' 대기'
}]);
const getfailStatus = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/fail-status",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio?.value?.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    FailHandleStatus.value = data;
  } 
};
watch(computedPortfolio, (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value.userPortfolioMappingId) {
    getfailStatus();
  }
});
onMounted(() => {
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getfailStatus();
  }
});
</script>
<template>
  <v-card
    width="360"
    class="mt-3 customCard noticeCard"
    elevation="0"
    rounded="lg"
  >
    <v-toolbar color="transparent" density="comfortable">
      <v-toolbar-title class="largeBoldText">{{
        t("FAIL_HANDLE_STATUS")
      }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-card-actions>
        <v-btn
          rounded="lg"
          color="primary"
          variant="outlined"
          @click="router.push('/service/calendars')"
          >{{ t("VIEW_DETAIL") }}</v-btn
        >
      </v-card-actions>
    </v-toolbar>
    <v-divider class="mx-4"></v-divider>

    <v-container class="py-5" fluid>
      <v-row :style="{ height: '90px' }" class="overflow-y-auto">
        <v-col
          v-for="(item, i) in FailHandleStatus"
          :key="i"
          class="py-2"
          cols="12"
        >
          <div class="d-flex justify-space-between align-center">
            <p class="noticeText">
              {{ item.text }} : {{ item.activityStatus }}
              <v-tooltip activator="parent" location="top">
                {{ item.text }} : {{ item.activityStatus }}</v-tooltip
              >
            </p>

            <p class="text-disabled">
              {{ item.updDt }}
            </p>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
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
