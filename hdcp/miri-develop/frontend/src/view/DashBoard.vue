<script setup>
import DefaultContainer from "@/component/DefaultContainer.vue";
import { ref, onMounted, computed, watch, reactive } from "vue";
import ServiceRequestModal from "@/component/ServiceRequestModal.vue";
import draggable from "vuedraggable";
import { connectionStore } from "@/store/ConnectionStore";
import { PieChart } from "@opd/g2plot-vue";
import { userStore } from "@/store/UserStore";
import { useRouter } from "vue-router";
import { useToast } from "vue-toastification";
import { useI18n } from "vue-i18n";

import * as widgetModule from "@/component/Widget/WidgetTemplate";
import { useDisplay } from "vuetify";
const { mdAndDown } = useDisplay();
const toast = useToast();
const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const computedUserInfo = computed(() => useUserStore?.userInfo);
const { t } = useI18n();
const router = useRouter();
const defaultAvatar = require("@/assets/img/defaultAvatar.png");
const openServiceModalRequest = ref(false);
const thisMonth = new Date().getFullYear().toString() + "." + (new Date().getMonth() + 1).toString().padStart(2, "0");
const bfThreeMonth = new Date(new Date().setMonth(new Date().getMonth() - 2)).getFullYear().toString() + "." + 
                      (new Date(new Date().setMonth(new Date().getMonth() - 2)).getMonth() + 1).toString().padStart(2, "0");
const bfSixMonth = new Date(new Date().setMonth(new Date().getMonth() - 5)).getFullYear().toString() + "." + 
                      (new Date(new Date().setMonth(new Date().getMonth() - 5)).getMonth() + 1).toString().padStart(2, "0");
const emptyWidget = computed(() => [
  {
    title: t("WEATHER"),
    component: "WIDGET_weather",
    width: 25,
    expand: true,
  },
  {
    title: t("INSPECTION"),
    component: "WIDGET_insp",
    width: 25,
    button: true,
  },
  {
    title: t("SELF_INSPECTION"),
    component: "WIDGET_selfInsp",
    width: 25,
    button: true,
  },
  {
    title: t("MIRI_AI_CHECK"),
    component: "WIDGET_warningParts",
    width: 25,
    button: true,
  },
  {
    title: t("MAP_INQUERY"),
    component: "WIDGET_inquery",
    width: 25,
    button: true,
  },
  {
    title: t("FAIL_HANDLE_STATUS"),
    component: "WIDGET_failHandleStatus",
    width: 25,
    expand: true,
    button: true,
  },
  {
    title: t("NOW_FAULT_RESULT"),
    component: "WIDGET_faultSuccess",
    width: 25,
  },
  {
    title: t("MAP_PLUS_SERVICE"),
    component: "WIDGET_plusService",
    width: 25,
  },
]);
const widgetButtonEventList = (buttonTrigger) => {
  const trigger = {
    WIDGET_insp: () => {
      router.push("/report/base");
    },
    WIDGET_selfInsp: () => {
      router.push("/report/base");
    },
    WIDGET_failHandleStatus: () => {
      router.push("/service/calendars");
    },
    WIDGET_inquery: () => {
      openServiceModalRequest.value = true;
    },
    WIDGET_warningParts: () => {
      router.push("/parts/lifeInquiry");
    },
  };
  trigger[buttonTrigger]();
};
const pieOptions = {
  height: 80,
  color: ["#00C44F", "grey"],
  innerRadius: 0.64,
  // appendPadding:0,
  angleField: "value",
  colorField: "type",
  radius: 0.8,
  padding: 0,
  // axis:false,
  xAxis: false,
  yAxis: false,
  label: false,
  legend: false,
  interactions: [{ type: "element-active" }],
  tooltip: {
    formatter: (datum) => {
      return { name: datum.type, value: `${datum.value}%` };
    },
    domStyles: {
      'g2-tooltip-value': {
        marginLeft : '0px'
      }
    }
  },
  statistic: {
    title: false,
    content: {
      style: {
        fontSize: "10px",
      },
      customHtml: (container, view, datum, data) => {
        return `${data[0]?.value}%`;
      },
    },
  },
};
const useUserStore = userStore();
const elCount = ref({ run: 0, notRun: 0 });
const esCount = ref({ run: 0, notRun: 0 });

const companionDays = ref(0);
const runAvgReriod = reactive({
  month: "0",
  quarter: "0",
  halfyear: "0",
});
const useConnectionStore = connectionStore();
const restWidget = ref([]);
const userWidget = ref([]);

const openAddWidget = ref(false);
const seletedWidget = ref([]);

const loadingWidget = () => {
  restWidget.value = emptyWidget.value
    .filter((x) => {
      return !userWidget.value.some((item) => {
        return x.component === item.component;
      });
    })
    .sort((a, b) => a.sort - b.sort);
  if (userWidget.value.some((item) => item.id === "addButton")) {
    userWidget.value.splice(
      userWidget.value.findIndex((item) => item.id == "addButton"),
      1
    );
  }
  if (
    restWidget.value.length > 0 &&
    !userWidget.value.some((item) => item.id === "addButton")
  ) {
    userWidget.value.push({
      id: "addButton",
      component: "",
      width: 25,
    });
  }
};

const expandWidget = (item) => {
  item.width < 26
    ? (item.width = item.width * 2)
    : item.width < 51
    ? (item.width = item.width / 2)
    : "";
  // item.width = item.width * 2;
};
const openAddWidgetModal = () => {
  loadingWidget();
  openAddWidget.value = true;
};
const addWidgetEvent = async () => {
  let next = false;

  seletedWidget.value.forEach((index) => {
    userWidget.value.push(restWidget.value[index]);
  });
  next = true;
  if (next == true) {
    updateUserWidgets();
    loadingWidget();
    seletedWidget.value = [];
  }
  next = false;
  openAddWidget.value = false;
};

const removeWidgetEvent = (index) => {
  userWidget.value = userWidget.value.filter((item, idx) => {
    return idx !== index;
  });
  // userWidget.value.splice(index, 1);

  loadingWidget();
};

const getRunCount = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/run-count",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    elCount.value = data.el;

    esCount.value = data.es;
  } else {
    toast.error(t("ERROR_SEARCH", [t("FAIL_HANDLE_STATUS")]));
  }
};
const getRunAvgPeriod = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/hccc/current/run-avg/period",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value.userPortfolioMappingId,
      },
    });
  if (code == 200) {
    runAvgReriod.month = data.month;
    runAvgReriod.quarter = data.quarter;
    runAvgReriod.halfyear = data.halfyear;
  } else {
    toast.error(t("ERROR_SEARCH", [t("RUN_STATUS")]));
  }
};
const getCompanionDays = async () => {
  const { data, because, result, code, message } =
    await useConnectionStore.request({
      url: "/v2/contract/current/companion-days",
      method: "post",
      queryparam: {},
    });
  if (code == 200) {
    companionDays.value = data;
  } else {
    toast.error(t("ERROR_SEARCH", [t("INFO_WITH_DAY")]));
  }
};

watch(computedPortfolio, (newVal, oldVal) => {
  if (!!newVal && computedPortfolio.value && oldVal != newVal) {
    getRunCount();
    getRunAvgPeriod();
  }
});
watch(computedUserInfo, (newVal, oldVal) => {
  if (
    !!newVal &&
    computedUserInfo.value &&
    !!computedUserInfo.value?.widgets &&
    computedUserInfo.value?.widgets &&
    JSON.parse(computedUserInfo.value?.widgets).length > 0 &&
    oldVal !== newVal
  ) {
    userWidget.value = JSON.parse(computedUserInfo.value.widgets);
  } else {
    userWidget.value = emptyWidget.value;
  }
  loadingWidget();
});
watch(openAddWidget, (newVal, oldVal) => {
  if (newVal == false) {
    seletedWidget.value = [];
  }
});

onMounted(() => {
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getRunCount();
    getRunAvgPeriod();
  }
  getCompanionDays();
  if (
    computedUserInfo.value &&
    computedUserInfo.value?.widgets &&
    !!computedUserInfo.value?.widgets &&
    JSON.parse(computedUserInfo.value?.widgets).length > 0
  ) {
    userWidget.value = JSON.parse(computedUserInfo.value.widgets);
  } else {
    userWidget.value = emptyWidget.value;
  }
  loadingWidget();
});
const updateUserWidgets = async () => {
  // try {
  const { data, because, result, code, message } =
    await useConnectionStore.request({
      url: "/v2/cc/dashboard/widget/update",
      method: "post",
      queryparam: {
        userId: computedUserInfo.value.userId,
        widgets: JSON.stringify(
          userWidget.value.filter((item) => item.id !== "addButton")
        ),
      },
    });
  JSON.stringify(userWidget.value.filter((item) => item.id !== "addButton"));

  if (code == 200) {
    await useUserStore.getUserData();
    // countInfo.value = data;
    if (
      computedUserInfo.value.widgets.length > 0 ||
      !!computedUserInfo.value.widgets == true
    ) {
      userWidget.value = JSON.parse(computedUserInfo.value.widgets);
    } else {
      userWidget.value = emptyWidget.value;
    }
    loadingWidget();
  } else {
    toast.error(t("ERROR_WIDGET"));
  }
  // } catch {
  //   toast.error("서버에서 에러가 발생했습니다.");
  // }
};
</script>

<template>
  <DefaultContainer>
    <v-col sm="12" lg="4">
      <v-card class="customCard" height="183px">
        <v-toolbar density="comfortable" color="transparent"
          ><v-toolbar-title class="largeBoldText">{{
            t("CONTRACT_STATUS")
          }}</v-toolbar-title></v-toolbar
        >
        <v-container fluid class="dashTopCardHeight">
          <v-row :style="{ height: '100%' }" align="center" justify="center">
            <v-col cols="auto" class="text-end">
              <v-avatar rounded="lg" class="mr-3">
                <v-img width="50" class="mx-auto" :src="defaultAvatar"></v-img>
              </v-avatar>
            </v-col>
            <v-col class="largeBoldText dashInfoText" cols="auto">
              <p class="largeBoldText">
                {{ t("INFO_MESSAGE1") }}
                {{ useUserStore.userInfo?.userName }}
                {{ t("INFO_MESSAGE2") }}
              </p>
              <p class="largeBoldText">
                <span class="largeBoldText" :style="{ color: 'green' }">
                  {{ t("INFO_MESSAGE3") }}
                  {{ companionDays }}
                  {{ t("INFO_MESSAGE4") }}
                </span>
                {{ t("INFO_MESSAGE5") }}
              </p>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-col>
    <v-col sm="12" lg="4">
      <v-card class="customCard" height="183px">
        <v-toolbar color="transparent" density="comfortable">
          <v-toolbar-title class="largeBoldText">{{
            t("FAIL_HANDLE_STATUS")
          }}</v-toolbar-title>
          <v-btn
            rounded="lg"
            color="primary"
            variant="outlined"
            @click="router.push('/machine/search')"
            >{{ t("VIEW_DETAIL") }}</v-btn
          >
        </v-toolbar>
        <v-container fluid class="dashTopCardHeight">
          <v-row :style="{ height: '100%' }" align="center">
            <v-col cols="6">
              <p class="smallBoldText mb-4">{{ t("EL") }}</p>

              <div class="d-flex align-center">
                <p class="text-disabled">
                  {{ t("MAP_FAULT_EL") }}
                </p>
                <p class="ml-5">
                  <span class="text-h5 font-weight-bold">
                    {{ elCount.notRun }}{{ t("EL_NUMBER") }}
                  </span>
                  <span class="text-h5 font-weight-bold"> / </span>
                  <span class="font-weight-bold text-h5 text-disabled"
                    >{{ t("TOTAL") }} {{ elCount?.notRun + elCount?.run
                    }}{{ t("EL_NUMBER") }}</span
                  >
                </p>
              </div>
            </v-col>
            <v-col cols="6">
              <p class="smallBoldText mb-4">{{ t("SL") }}</p>
              <!-- <p class="xlBoldText">
                {{ esCount?.run + esCount?.notRun }}{{ t("EL_NUMBER") }}
              </p> -->
              <div class="d-flex align-center">
                <p class="text-disabled">
                  {{ t("MAP_FAULT_EL") }}
                </p>
                <p class="ml-5">
                  <span class="text-h5 font-weight-bold">
                    {{ esCount.notRun }}{{ t("EL_NUMBER") }}
                  </span>
                  <span class="text-h5 font-weight-bold"> / </span>
                  <span class="font-weight-bold text-h5 text-disabled"
                    >{{ t("TOTAL") }} {{ esCount?.notRun + esCount?.run
                    }}{{ t("EL_NUMBER") }}</span
                  >
                </p>
              </div>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-col>
    <v-col sm="12" lg="4">
      <v-card class="customCard">
        <v-toolbar color="transparent" density="comfortable">
          <v-toolbar-title class="largeBoldText">{{
            t("EL_RUN_STATUS")
          }}</v-toolbar-title>
          <!-- <v-card-subtitle>
            <span class="dot" :style="{ background: 'blue' }"></span
            >정상</v-card-subtitle
          >
          <v-card-subtitle>
            <span class="dot" :style="{ background: 'grey' }"></span
            >미운행</v-card-subtitle
          > -->
          <v-btn
            rounded="lg"
            color="primary"
            variant="outlined"
            @click="router.push('/machine/search')"
            >{{ t("VIEW_DETAIL") }}</v-btn
          >
        </v-toolbar>
        <v-container fluid class="dashTopCardHeight">
          <v-row :style="{ height: '100%' }" align="center">
            <v-col cols="4">
              <p class="smallBoldText">{{ thisMonth }}</p>
            </v-col>
            <v-col cols="4"
              ><p class="smallBoldText">{{ bfThreeMonth + " ~ " + thisMonth }}</p>
            </v-col>
            <v-col cols="4"
              ><p class="smallBoldText">{{ bfSixMonth + " ~ " + thisMonth }}</p>
            </v-col>
            <v-col
              class="py-0"
              cols="4"
              v-for="(item, keys) in runAvgReriod"
              :key="keys"
            >
              <pie-chart
                v-bind="{
                  ...pieOptions,
                }"
                :data="[
                  { type: '운행', value: Number(item) },
                  { type: '미운행', value: 100 - Number(item) },
                ]"
              ></pie-chart
            ></v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-col>
    <v-col cols="12" class="text-end py-0">
      <v-btn
        :disabled="userWidget.value >= 1"
        @click="updateUserWidgets"
        variant="outlined"
        color="primary"
        rounded="lg"
        :style="{ marginRight: '20px' }"
        >{{ t("SAVE") }}</v-btn
      >
    </v-col>
    <v-col cols="12" class="py-0">
      <transition-group>
        <draggable
          v-if="userWidget"
          handle="#handle"
          v-model="userWidget"
          item-key="id"
          key="dragggable"
          animation="400"
          class="widget-container"
        >
          <template #item="{ element, index }">
            <v-card
              class="widget-item customCard"
              :style="{
                flex:
                  mdAndDown == false
                    ? `0 0 calc(${element.width}% - 10px)`
                    : element.width >= 50
                    ? '0 0 calc(100% - 10px)'
                    : '0 0 calc(50% - 10px)',
              }"
            >
              <div v-if="element.component">
                <v-toolbar density="compact" color="transparent">
                  <v-btn id="handle" icon varaint="plain"
                    ><v-icon>mdi-drag</v-icon></v-btn
                  >
                  <v-toolbar-title class="">{{
                    emptyWidget.find((item) => item?.component == element?.component)?.title
                  }}</v-toolbar-title>
                  <v-btn
                    rounded="lg"
                    color="primary"
                    variant="outlined"
                    @click="widgetButtonEventList(element.component)"
                    v-if="element.button"
                    >{{
                      element.component == "WIDGET_inquery"
                        ? t("SERVICE_REQUEST")
                        : t("VIEW_DETAIL")
                    }}</v-btn
                  >
                  <v-menu location="end">
                    <template v-slot:activator="{ props }">
                      <v-btn
                        varaint="plain"
                        icon="mdi-dots-vertical"
                        v-bind="props"
                      ></v-btn>
                    </template>
                    <v-card class="pa-2">
                      <v-list>
                        <!-- <v-list-subheader>ACTIONS</v-list-subheader> -->

                        <v-list-item
                          value="remove"
                          active-color="primary"
                          rounded="xl"
                          @click="removeWidgetEvent(index)"
                        >
                          <template v-slot:prepend>
                            <v-icon
                              icon="mdi-minus-circle-outline"
                              color="red"
                            ></v-icon>
                          </template>

                          <v-list-item-title>{{
                            t("REMOVE")
                          }}</v-list-item-title>
                        </v-list-item>
                        <!-- <v-list-item
                          rounded="xl"
                          value="reset"
                          active-color="primary"
                        >
                          <template v-slot:prepend>
                            <v-icon icon="mdi-refresh"></v-icon>
                          </template>

                          <v-list-item-title>{{
                            t("RESET")
                          }}</v-list-item-title>
                        </v-list-item> -->
                        <v-list-item
                          rounded="xl"
                          value="expand"
                          active-color="primary"
                          @click="expandWidget(element)"
                          :disabled="element.expand == true ? false : true"
                        >
                          <template v-slot:prepend>
                            <v-icon icon="mdi-arrow-expand"></v-icon>
                          </template>

                          <v-list-item-title>{{
                            element.width > 25 ? t("REDUCE") : t("EXPAND")
                          }}</v-list-item-title>
                        </v-list-item>
                      </v-list>
                    </v-card>
                  </v-menu>
                </v-toolbar>
                <v-divider></v-divider>
                <component
                  :request="true"
                  :style="{ height: '155px' }"
                  :item="element"
                  :is="widgetModule[element.component]"
                >
                </component>
              </div>

              <div
                v-if="!element.component"
                :style="{ height: '203px' }"
                class="d-flex justify-center align-center"
              >
                <v-btn
                  icon="mdi-plus"
                  color="info"
                  variant="text"
                  size="x-large"
                  @click="openAddWidgetModal"
                >
                </v-btn>
              </div>
            </v-card>
          </template>
        </draggable>
      </transition-group>
    </v-col>
  </DefaultContainer>
  <v-dialog
    v-model="openAddWidget"
    persistent
    no-click-animation
    max-width="90vw"
  >
    <v-card class="customCard">
      <v-toolbar class="add-widget-header">
        <v-toolbar-title>{{ t("ADD_WIDGET") }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon>
          <v-icon @click="openAddWidget = false">mdi-close</v-icon>
        </v-btn>
      </v-toolbar>
      <v-sheet>
        <p class="py-3 pl-11">
          {{ t("ADD_WIDGET_MESSAGE") }}
        </p>

        <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
        <v-item-group
          multiple
          v-model="seletedWidget"
          class="widget-container px-8"
        >
          <v-card
            class="customCard widget-item widget-modal-item"
            v-for="(item, i) in restWidget"
            :key="i"
            :style="{ flex: `0 0 calc(25% - 20px)` }"
          >
            <v-item v-slot="{ isSelected, toggle }">
              <div
                class="widget-selected"
                :style="{ background: isSelected ? '#4b6cb7' : null }"
                @click.prevent="toggle"
              ></div>
              <v-sheet>
                <v-toolbar density="compact" color="transparent">
                  <v-toolbar-title class="">{{ item.title }}</v-toolbar-title>
                </v-toolbar>
                <v-divider></v-divider>
                <component
                  :request="false"
                  :style="{ height: '155px' }"
                  v-if="item.component"
                  :is="widgetModule[item.component]"
                >
                </component>
                <div
                  :style="{ height: '155px' }"
                  v-if="!!item.component == false"
                >
                  undifined component
                </div>
              </v-sheet>
            </v-item>
          </v-card>
        </v-item-group>

        <div class="my-4 px-8 text-end">
          <v-btn
            class="text-white customBtn mx-0"
            color="primary"
            rounded="lg"
            :disabled="seletedWidget.length == 0 ? true : false"
            size="large"
            @click="addWidgetEvent"
            >{{ t("OK") }}
          </v-btn>
          <v-btn
            class="customBtn"
            rounded="lg"
            size="large"
            color="#eee"
            @click="openAddWidget = false"
          >
            {{ t("CANCLE") }}</v-btn
          >
        </div>
      </v-sheet>
    </v-card>
  </v-dialog>
  <ServiceRequestModal
    v-model="openServiceModalRequest"
    v-if="openServiceModalRequest"
  ></ServiceRequestModal>
</template>

<style lang="scss">
.dashInfoText {
  font-size: 36px;
}
.dash-title {
  font-size: 2em !important;
}
.widget-container {
  width: 100%;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
}
.widget-item {
  margin: 10px 5px;
}
.widget-item:nth-child(4n) {
  margin: 10px 0px 10px 10px;
  // height: 12.7rem;
}
.widget-item:nth-child(1),
.widget-item:nth-child(5) {
  margin: 10px 10px 10px 0px;
}
@media screen and (max-width: 1280px) {
  .widget-item:nth-child(odd) {
    margin: 10px 10px 10px 0px;
    // height: 12.7rem;
  }
  .widget-item:nth-child(even) {
    margin: 10px 0px 10px 10px;
    // height: 12.7rem;
  }
}
.widget-modal-item {
  position: relative;
  &:hover {
    cursor: pointer;
  }
}
.widget-selected {
  width: 100%;
  height: 100%;
  position: absolute;
  opacity: 0.5;
  z-index: 2;
  transition: 0.3s ease;
  display: hidden !important;
}
.widget-seleted-foucs {
  display: block !important;
  background-color: #4b6cb7;
}
.add-widget-header {
}
.widget-content-box {
  border: 1px dashed blue;
  height: 155px;
}
.widget-content {
  height: 100%;
}
.dot {
  margin-right: 4px;
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
}
.dashTopCardHeight {
  height: calc(100% - 56px);
}
</style>
