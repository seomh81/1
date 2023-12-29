<script setup>
import { UserSettingMenu } from "@/component/Template.js";
import { userStore } from "@/store/UserStore";
import { ref, onMounted, watch, computed } from "vue";
import { useToast } from "vue-toastification";
import { ls } from "@/plugin/secure.js";
import { ParallelSpiner } from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
import { useI18n } from "vue-i18n";
const useConnectionStore = connectionStore();
const useUserStore = userStore();
const toast = useToast();
const { t } = useI18n();
const originData = ref([]);
const fictionalData = ref([]);
const uiData = ref({
  locale: "",
  landingpageType: "",
  theme: "",
});
const selectCheckBox = ref([]);
const computedUserInfo = computed(() => useUserStore?.userInfo);

const findData = (item) => {
  originData.value.forEach((origin) => {
    origin.useYn = "n";
  });

  selectCheckBox.value.forEach((item) => {
    fictionalData.value.alarmRuleList.forEach((item2) => {
      item2.alarmDivList[0].access.forEach((item3) => {
        if (item == item3.divNm) {
          originData.value.find(
            (item4) => item4.updateId == item + item3.sendMethodNo
          ).useYn = "y";
        }
      });
    });
  });
};

watch(
  [computedUserInfo],
  () => {
    uiData.value.locale = useUserStore?.userInfo?.locale;
    uiData.value.landingpageType = useUserStore?.userInfo?.landingpageType;
    uiData.value.theme = useUserStore?.userInfo?.theme;
  },
  { deep: true }
);

const changeTheme = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/setup/current/change-theme",
      method: "post",
      queryparam: {
        theme: uiData.value.theme,
      },
    });
  if (code === 200 && result == "success") {
    return code;
  } else {
    toast.error(t("ERROR_THEME_CHANGE"));
  }
};
const changeLandingPage = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/setup/current/change-landing-page",
      method: "post",
      queryparam: {
        landingpageType: uiData.value.landingpageType,
      },
    });
  if (code === 200 && result == "success") {
    ls.set("page", uiData.value.landingpageType);
    return code;
  } else {
    toast.error(t("ERROR_LANDING_CHANGE"));
  }
};
const changeLocale = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/setup/current/change-locale",
      method: "post",
      queryparam: {
        locale: uiData.value.locale,
      },
    });
  if (code === 200 && result == "success") {
    return code;
  } else {
    toast.error(t("ERROR_LOCALE_CHANGE"));
  }
};

const getAlram = async () => {
  let arr = [];
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/alarm-setup/current/web",
      method: "get",
      queryparam: {},
    });
  if (code == 200) {
    fictionalData.value = data[0];

    // 초기 선택값 설정
    fictionalData.value.alarmRuleList.forEach((cat) => {
      cat.alarmDivList.forEach((division, index) => {
        // checkbox 초기값 설정
        division.checkBox = division.useYn === "y" ? division.divNo : "-1";

        // 제일 첫번째 값으로 LOV 초기값을 설정한다.
        if (index == 0) {
          let temp = division.alarmDetailList;
          let _sel = temp.map((item) => (item.methodYn === "y" ? item : ""));
          _sel = _sel.filter(function (item) {
            return item !== null && item !== undefined && item !== "";
          });
          cat.selection = _sel;
        }
      });
    });
  } else {
    toast.error(t("ERROR_ALRAM_GET"));
  }
};

const StripParams = function (_params) {
  let rtnVal = [];
  _params.forEach((element) => {
    rtnVal.push({ alarmSetupId: element.alarmSetupId, useYn: element.useYn });
  });
  return rtnVal;
};

const updateAlram = async () => {
  let _params = [];
  fictionalData.value.alarmRuleList.forEach((ruleList) => {
    ruleList.alarmDivList.forEach((divList) => {
      _params = [..._params, ...divList.alarmDetailList];
    });
  });

  _params = StripParams(_params);

  let reqParams = { codes: _params };
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/alarm-setup/current/update",
      method: "post",
      queryparam: reqParams,
    });
  if (code === 200) {
    return code;
  } else {
    toast.error(t("ERROR_ALRAM_CHANGE"));
  }
};
const parallelSpiner = ref(false);
const updateEvent = async () => {
  parallelSpiner.value = true;
  try {
    const codes = await Promise.all([
      updateAlram(),
      changeTheme(),
      changeLandingPage(),
      changeLocale(),
    ]);

    if (
      codes.every((code) => {
        return code == 200;
      })
    ) {
      toast.success(t("SUCCESS_SERVICE_SAVE"));
    }
    await useUserStore.getUserData();
    await getAlram();
    parallelSpiner.value = false;
  } catch {
    parallelSpiner.value = false;
  }
};
onMounted(() => {
  if (computedUserInfo.value) {
    uiData.value.locale = computedUserInfo.value.locale;
    uiData.value.landingpageType = computedUserInfo.value.landingpageType;
    uiData.value.theme = computedUserInfo.value.theme;
  }
  getAlram();
});

const ToggleSelection = function (rowItem) {
  let selection = rowItem.selection;
  let _temp = null;
  rowItem.alarmDivList.forEach((div) => {
    div.alarmDetailList.forEach((ele) => {
      ele.useYn = "n";
    });
    // -1 이면 체크박스가 미선택이므로 row들의 useYn = n로 설정한다.
    if (div.checkBox === "-1") {
      //
    } else {
      // 이 이벤트는 토클로 생각하고 처리한다.
      selection.forEach((sel) => {
        _temp = div.alarmDetailList.find(
          (element) => element.sendMethodNo === sel.sendMethodNo
        );
        _temp.useYn = "y";
      });
    }
  });

  console.log("S:", rowItem.alarmDivList);
};

const ToggleChecked = function (evt, rowItem, item) {
  if (evt) {
    item.useYn = "y";
    let selection = rowItem.selection;
    let _temp = null;
    selection.forEach((sel) => {
      _temp = item.alarmDetailList.find(
        (element) => element.sendMethodNo === sel.sendMethodNo
      );
      _temp.useYn = "y";
    });
  } else {
    item.useYn = "n";
    item.checkBox = "-1";
    item.alarmDetailList.forEach((item) => {
      item.useYn = "n";
    });
  }
  console.log("C:", rowItem.alarmDivList);
};
</script>
<template>
  <v-container class="settingMenu" fluid>
    <v-row justify="center">
      <v-col sm="4" lg="3" class="pr-0">
        <v-card class="settingMenuCard menuCard rounded-s-lg">
          <UserSettingMenu></UserSettingMenu>
        </v-card>
      </v-col>
      <v-col sm="8" lg="7" class="pl-0">
        <v-card
          class="settingContentCard contentCard rounded-e-lg"
          border="none"
        >
          <v-toolbar class="contentToolbar" color="transparent">
            <v-toolbar-title>{{ t("SET_UI") }}</v-toolbar-title>
            <v-btn
              @click="updateEvent"
              width="5vw"
              rounded="lg"
              variant="elevated"
              color="info"
              >{{ t("SAVE") }}</v-btn
            >
          </v-toolbar>
          <v-container class="contentInner pb-4">
            <v-row align="center" justify="space-around">
              <v-col cols="12" lg="auto" class="pa-0 text-center">
                <v-toolbar color="transparent" density="compact">
                  <v-toolbar-title>{{ t("LOCALE") }}</v-toolbar-title>
                </v-toolbar>
                <v-btn-toggle
                  mandatory
                  v-model="uiData.locale"
                  color="info"
                  group
                >
                  <v-btn value="ko_kr" rounded="lg">
                    {{ t("LOCALE_KO") }}
                    <span class="pl-1" :style="{ opacity: '0.5' }"> KO </span>
                  </v-btn>
<!-- 임시 삭제_20230710
                  <v-btn value="en_gn" rounded="lg">
                    {{ t("LOCALE_EN") }}

                    <span class="pl-1" :style="{ opacity: '0.5' }"> EN </span>
                  </v-btn>
-->
                </v-btn-toggle></v-col
              >
              <v-col cols="12" lg="auto" class="pa-0 text-center">
                <v-sheet>
                  <v-toolbar color="transparent" density="compact">
                    <v-toolbar-title>{{ t("DEFAULT_PAGE") }}</v-toolbar-title>
                  </v-toolbar>
                  <v-btn-toggle
                    mandatory
                    v-model="uiData.landingpageType"
                    color="info"
                    group
                  >
                    <v-btn
                      append-icon="mdi-map-search"
                      value="map_page"
                      rounded="lg"
                    >
                      {{ t("MENU_MAP_PAGE") }}
                    </v-btn>

                    <v-btn
                      value="dashboard"
                      append-icon="mdi-view-dashboard"
                      rounded="lg"
                    >
                      {{ t("MENU_DASHBOARD") }}
                    </v-btn>
                  </v-btn-toggle>
                </v-sheet>
              </v-col>
              <v-col cols="12" lg="auto" class="pa-0 text-center">
                <v-sheet>
                  <v-toolbar color="transparent" density="compact">
                    <v-toolbar-title>{{ t("THEME") }}</v-toolbar-title>
                  </v-toolbar>
                  <v-btn-toggle
                    mandatory
                    v-model="uiData.theme"
                    color="info"
                    group
                  >
                    <v-btn
                      value="light"
                      rounded="lg"
                      append-icon="mdi-weather-sunny"
                    >
                      {{ t("THEME_LIGHT") }}
                    </v-btn>

                    <v-btn
                      value="dark"
                      rounded="lg"
                      append-icon="mdi-weather-night"
                    >
                      {{ t("THEME_DARK") }}
                    </v-btn>
                  </v-btn-toggle>
                </v-sheet>
              </v-col>
            </v-row>
          </v-container>

          <v-toolbar class="contentToolbar" color="transparent">
            <v-toolbar-title>{{ t("SET_NOTIFICATION") }}</v-toolbar-title>
          </v-toolbar>
          <v-container fluid class="contentInner">
            <v-row
              align="center"
              v-for="rowItem in fictionalData.alarmRuleList"
              :key="rowItem"
            >
              <v-col cols="12" class="pa-0">
                <v-select
                  variant="solo"
                  density="compact"
                  multiple
                  :label="rowItem.catNm"
                  :items="rowItem?.alarmDivList[0]?.alarmDetailList"
                  v-model="rowItem.selection"
                  item-title="sendMethodNm"
                  hide-details
                  return-object
                  @update:modelValue="ToggleSelection(rowItem)"
                >
                </v-select>
              </v-col>

              <v-col
                class="pa-0"
                cols="3"
                v-for="item in rowItem.alarmDivList"
                :key="item"
              >
                <v-checkbox
                  color="info"
                  v-model="item.checkBox"
                  :value="item.divNo"
                  :label="item.divNm"
                  @update:modelValue="
                    ToggleChecked(item.checkBox, rowItem, item)
                  "
                  hide-details
                ></v-checkbox>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

  <teleport to="#extra-modal" :disabled="false">
    <ParallelSpiner :parallelSpiner="parallelSpiner"></ParallelSpiner>
  </teleport>
</template>
<style lang="scss"></style>
