<script setup>
import { ref, computed, onMounted, watch, reactive } from "vue";

import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
import { userStore } from "@/store/UserStore";
import { useI18n } from "vue-i18n";
import { useToast } from "vue-toastification";
const props = defineProps(["item", "request"]);
const useConnectionStore = connectionStore();
const useUserStore = userStore();
const { t } = useI18n();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const warningElList = ref([]);
const getElData = async () => {
  try {
    const { status, code, message, data, because } =
      await useConnectionStore.request({
        url: "/v2/cc/current/elevators",
        method: "post",
        queryparam: {
          userPortfolioMappingId:
            computedPortfolio.value.userPortfolioMappingId,
          type: "all",
          // status: "운행중",
        },
      });
    if (code == 200) {
      if (data.length > 0) {
        let projnos = [];
        data.forEach((item) => {
          projnos.push(item.prjNo + item.hoNo);
        });
        return projnos;
      }
    }
  } catch {
    // toast.error("에러가 발생했습니다.");
  }
};
const getPartsData = async (projnos) => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/unit-service/material/predict/projnos",
      method: "post",
      queryparam: {
        // visible: true,
        projnos,
      },
    });
  if (code == 200) {
    // once = false;
    if (data.projnos && data.projnos.length > 0) {
      // let convertDeTailData = convertDataFunction(data);
      data.projnos.forEach((item) => {
        item.convertText = `${item.buldNm} - ${item.projno.slice(-2)}호기`;
      });
      return data.projnos;
      // return convertDeTailData;
    } else {
      return [];
      // toast.warning("부품 데이터가 없습니다.");
    }
    // treeData.value = createDataTree(data);
  } else {
    // toast.error(message);
  }
};
onMounted(async () => {
  if (computedPortfolio.value?.userPortfolioMappingId && props.request) {
    warningElList.value = await getPartsData(await getElData());
  }
});
watch(computedPortfolio, async (newVal, oldVal) => {
  if (
    !!newVal &&
    computedPortfolio.value?.userPortfolioMappingId &&
    props.request
  ) {
    warningElList.value = await getPartsData(await getElData());
  }
});
const currentIdx = ref();
</script>

<template>
  <div>
    <v-carousel
      height="100%"
      :show-arrows="false"
      hide-delimiters
      cycle
      v-model="currentIdx"
      v-if="warningElList?.length > 0"
    >
      <template v-slot:prev="{ props }">
        <v-btn
          color="info"
          size="x-small"
          icon="mdi-arrow-left"
          @click="props.onClick"
        >
        </v-btn>
      </template>
      <template v-slot:next="{ props }">
        <v-btn
          color="info"
          size="x-small"
          icon="mdi-arrow-right"
          @click="props.onClick"
        >
        </v-btn>
      </template>
      <v-carousel-item
        cover
        v-for="(item, i) in warningElList"
        :key="i"
        class="d-flex align-center"
      >
        <v-sheet class="mx-auto" width="100%">
          <p class="smallBoldText pl-6 mb-3">
            {{ item.convertText }}

            <span class="pl-1 xsmallOpacityText" :style="{ fontWeight: '400' }">
              ({{ `${currentIdx + 1} / ${warningElList.length}` }})
            </span>
          </p>

          <p
            v-for="leaf in item.materials"
            :key="leaf"
            class="d-flex align-center pl-6 pb-1"
          >
            <v-icon color="warning" class="mr-1"
              >mdi-alert-octagon-outline</v-icon
            >
            <span>
              {{
                leaf.mgroup == "MAIN ROPE"
                  ? t("ROPE")
                  : leaf.mgroup === "MAIN INV"
                  ? t("INV")
                  : leaf.mgroup === "MAIN SHEAVE"
                  ? t("SHEVE")
                  : leaf.mgroup === "SAFETY EDGE"
                  ? t("SAFETY_EDGE")
                  : leaf.mgroup === "DOOR INV"
                  ? t("DOOR_INV")
                  : ""
              }}{{ t("WIDGET_INSPECTION_MESSAGE") }}
            </span>
          </p>
        </v-sheet>
      </v-carousel-item>
    </v-carousel>
    <v-carousel
      height="100%"
      :show-arrows="false"
      hide-delimiters
      v-if="warningElList?.length == 0"
    >
      <v-carousel-item cover class="d-flex align-center">
        <v-sheet class="mx-auto" width="100%">
          <p class="smallBoldText pl-6 mb-3">
            {{ t("BULD_NM") }} - {{ t("HO_NO") }}

            <span class="pl-1 xsmallOpacityText" :style="{ fontWeight: '400' }">
              ( 0 / 0 )
            </span>
          </p>

          <p class="d-flex align-center pl-6 pb-1">
            <!-- <v-icon color="warning" class="mr-1"
              >mdi-alert-octagon-outline</v-icon
            > -->
            <span> {{ t("PARTS_SUCCESS") }} </span>
          </p>
        </v-sheet>
      </v-carousel-item>
    </v-carousel>
  </div>
</template>
