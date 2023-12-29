<script setup>
import { ref, computed, onMounted, watch, reactive } from "vue";

import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";

import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const useConnectionStore = connectionStore();
const props = defineProps(["item", "request"]);

const useUserStore = userStore();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const inspectionData = ref([]);
const acceptSize = ref(0);
const failSize = ref(0);
const nullSize = ref(0);
const getSelfInspectData = async () => {
  acceptSize.value = 0;
  failSize.value = 0;
  nullSize.value = 0;
  try {
    const { status, code, message, data, because } =
      await useConnectionStore.request({
        url: "/v2/cc/manage/car/insp/head",
        method: "post",
        queryparam: {
          userPortfolioMappingId:
            computedPortfolio.value.userPortfolioMappingId,
          // status: "운행중",
        },
      });
    if (code == 200) {
      data.forEach((item) => {
        if (item.checkRst == "합격") {
          acceptSize.value++;
        } else if (item.checkRst == "불합격") {
          failSize.value++;
        } else {
          nullSize.value++;
        }
      });
      // let projnos = [];
      // data.forEach((item) => {
      // projnos.push(item.prjNo + item.hoNo);
      // });
      // return projnos;
    }
  } catch {
    // toast.error("에러가 발생했습니다.");
  }
};

onMounted(async () => {
  if (computedPortfolio.value?.userPortfolioMappingId && props.request) {
    await getSelfInspectData();
  }
});
watch(computedPortfolio, async (newVal, oldVal) => {
  if (
    !!newVal &&
    computedPortfolio.value?.userPortfolioMappingId &&
    props.request
  ) {
    await getSelfInspectData();
  }
});
</script>

<template>
  <v-container :style="{ cursor: 'default' }">
    <div
      :style="{ height: '100%' }"
      class="d-flex align-center justify-space-around text-center"
    >
      <div>
        <div>
          <v-icon size="large" color="primary">mdi-check-circle-outline</v-icon>
        </div>
        <p class="my-3">{{ t("PASS") }}</p>
        <p class="boldText" :style="{ fontSize: '24px' }">
          {{ acceptSize }}{{ t("EL_NUMBER") }}
        </p>
      </div>
      <div>
        <div>
          <v-icon size="large" color="error">mdi-alert-circle-outline</v-icon>
        </div>
        <p class="my-3">{{ t("FAIL") }}</p>
        <p class="boldText" :style="{ fontSize: '24px' }">
          {{ failSize }}{{ t("EL_NUMBER") }}
        </p>
      </div>
      <div>
        <div>
          <v-icon size="large" :style="{ opacity: '0.5' }"
            >mdi-minus-circle-outline</v-icon
          >
        </div>
        <p class="my-3">{{ t("NOT_CHECK") }}</p>
        <p class="boldText" :style="{ fontSize: '24px' }">
          {{ nullSize }}{{ t("EL_NUMBER") }}
        </p>
      </div>
    </div>
  </v-container>
</template>
