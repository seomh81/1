<script setup>
import { ref, onMounted, watch, computed, provide, reactive } from "vue";
import { connectionStore } from "@/store/ConnectionStore";

import Calendar from "@toast-ui/calendar";
import "@toast-ui/calendar/dist/toastui-calendar.min.css"; // Calendar 스타일
import { useTheme } from "vuetify";
import DatePicker from "tui-date-picker";
import { useRouter, useRoute } from "vue-router";
import { useToast } from "vue-toastification";
import { userStore } from "@/store/UserStore";
import { dataStore } from "@/store/DataStore";
import { Tabulator, DefaultContainer, Skeleton } from "@/component/Template";
import { useI18n } from "vue-i18n";
import ServiceDetailPopupModal from "@/component/ServiceDetailPopupModal.vue";
const height = 64 + 64 + 16 + 24 + 36;
const calcHeight = `calc(100vh - ${height}px)`;
const useConnectionStore = connectionStore();
const weekView = ref(false);
const useUserStore = userStore();
const useDataStore = dataStore();
const targetFatalList = ref([]);
const toast = useToast();
const { t } = useI18n();
const totalData = ref([]);
const nmData = ref([]);
const calendar = ref();
const datepicker = ref();
const selectMonth = ref(new Date().getMonth() + 1);
const selectYears = ref(new Date().getFullYear());
const targetInspList = ref([]);
const dialog = ref(false);
const filteringValue = reactive({
  type: "ALL",  
  name: ["ALL"],
});
const theme = useTheme();
const targetCardData = ref([]);
const selectedDateValue = ref();


let columns = computed(() => {
  return [
    {
      title: t("DATE"),
      field: "start",
      sorter: "string",
    },
    {
      title: t("BULD_NM"),
      field: "prjNm",
      minWidth: 200,
      sorter: "string",
    },
    {
      title: t("PRJ_NO"),
      field: "prj_no",
      sorter: "string",
    },
    {
      title: t("TYPE"),
      field: "dtypeText",
      sorter: "string",
    },
    {
      title: t("CONTENTS"),
      field: "status",
      sorter: "string",
    },
  ];
});

const computedPortfolio = computed(() => useUserStore?.portfolio?.selected);
const getCalender = async () => {
  calendar.value.clear();
  targetFatalList.value = [];
  targetInspList.value = [];
  nmData.value = [];
  const startYear = calendar.value.getDateRangeStart().getFullYear();
  const startMonth = String(
    calendar.value.getDateRangeStart().getMonth() + 1
  ).padStart(2, "0");
  const startDay = String(
    calendar.value.getDateRangeStart().getDate()
  ).padStart(2, "0");
  const endYear = calendar.value.getDateRangeEnd().getFullYear();
  const endMonth = String(
    calendar.value.getDateRangeEnd().getMonth() + 1
  ).padStart(2, "0");
  const endDay = String(calendar.value.getDateRangeEnd().getDate()).padStart(
    2,
    "0"
  );

  const { status, code, message, data, because } =
    await useConnectionStore.request({
      url: "/v2/cc/current/calender",
      method: "post",
      queryparam: {
        userPortfolioMappingId: computedPortfolio.value?.userPortfolioMappingId,
        startDate: `${startYear}${startMonth}${startDay}`,
        endDate: `${endYear}${endMonth}${endDay}`,
      },
    });

  if (code == 200) {
    if (data.length > 0) {

    data.forEach((item) => {
      if (item.targetDt) {
        item.id = item.hoNo;
        item.title = item.prjNm ? `${item.prjNm}-${item.hoNo}` : "null";
        item.calendarId = item.dtype;
        item.start = item.targetDt.replace(
          /^(\d{4})(\d{2})(\d{2})$/,
          "$1-$2-$3"
        );
        item.end = item.targetDt.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3");
        item.raw = item;
        item.category = "allday";
        nmData.value.push(item.prjNm ? item.prjNm : "null");
      }
    });
    nmData.value = nmData.value.filter((item, idx) => {
      return nmData.value.findIndex((item2) => item === item2) == idx;
    });
    nmData.value.unshift("ALL");
    totalData.value = data;
    calendar.value.createEvents(filteringEvents(totalData.value));
    } else {
      toast.warning(t("NO_DATA_SEARCH", [t("MENU_SERVICE_CALENDAR")]));
    }
  } else {
    toast.warning(t("ERROR_SEARCH", [t("MENU_SERVICE_CALENDAR")]));
  }
};

const filteringEvents = (data) => {
  targetFatalList.value = [];
  targetInspList.value = [];
  const selection = document.querySelectorAll(
    ".toastui-calendar-grid-selection"
  );
  if (selection.length > 0) {
    selection.forEach((item) => {
      item.style.backgroundColor = "transparent";
      item.style.border = "none";
    });
  }
  if (filteringValue.type == "ALL" && filteringValue.name[0] === "ALL") {
    return data;
  } else if (
    filteringValue.type !== "ALL" ||
    filteringValue.name[0] !== "ALL"
  ) {
    if (filteringValue.type !== "ALL") {
      data = data.filter((item) => item.dtype === filteringValue.type);
    }
    if (filteringValue.name[0] !== "ALL") {
      data = data.filter((item) => {
        return filteringValue.name.some((item2) => item.prjNm === item2);
      });
    }
    return data;
  }
};

const nextMonth = () => {
  if (selectMonth.value === 12) {
    calendar.value.move(1);
  } else {
    calendar.value.next();
  }
  selectYears.value = calendar.value.getDate().getFullYear();
  selectMonth.value = calendar.value.getDate().d.getMonth() + 1;
  if (weekView.value) {
    getCalender();
  }

};
const prevMonth = () => {
  if (selectMonth.value === 12) {
    calendar.value.move(-1);
  } else {
    calendar.value.prev();
  }
  selectYears.value = calendar.value.getDate().getFullYear();
  selectMonth.value = calendar.value.getDate().d.getMonth() + 1;
  if (weekView.value) {
    getCalender();
  }
};

const returnToday = () => {
  selectMonth.value = new Date().getMonth() + 1;
  selectYears.value = new Date().getFullYear();
  calendar.value.setDate(new Date());
};

const toggleFilterValueName = (data) => {
  if (data == "ALL") {
    filteringValue.name = ["ALL"];
  } else if (filteringValue.name.some((item) => data === item)) {
    filteringValue.name.splice(
      filteringValue.name.findIndex((item2) => data === item2),
      1
    );
  } else {
    if (filteringValue.name.find((item2) => "ALL" === item2)) {
      filteringValue.name.splice(
        filteringValue.name.findIndex((item2) => "ALL" === item2),
        1
      );
    }
    filteringValue.name.push(data);
  }
};
const selectDate = (yyyymmdd) => {
  const fatalList = [];
  const inspList = [];
  targetFatalList.value = filteringEvents(totalData.value).forEach((item) => {
    if (item.dtype == "FAULT_HIS") {
      if (item.targetDt === yyyymmdd) {
        fatalList.push(item);
      }
    } else {
      if (item.dtype && item.targetDt === yyyymmdd) {
        item.dtypeText =
          item.dtype === "SELF_INSP_HIS"
            ? "정기 점검"
            : item.dtype === "INSP_HIS"
            ? "정기 검사"
            : "-";
        item.prj_no = item.intgPrjNo + item.hoNo;
        inspList.push(item);
      }
    }
  });
  targetFatalList.value = fatalList;
  targetInspList.value = inspList;

  const selection = document.querySelectorAll(
    ".toastui-calendar-grid-selection"
  );
  if (selection.length > 0) {
    selection.forEach((item) => {
      item.style.backgroundColor = "transparent";
      item.style.border = "none";
    });
  }
};
const refreshData = async function() {
    await getCalender();
    selectDate(selectedDateValue.value);
}
const computedText = computed(() =>
  theme.global.name.value == "light" ? "black" : "#ddd"
);
const computedBackGround = computed(() =>
  theme.global.name.value == "light" ? "white" : "#212121"
);
onMounted(async () => {
  const container = document.getElementById("calendar");

  calendar.value = new Calendar(container, {
    defaultView: "month",
    usageStatistics: false,

    useFormPopup: false,
    useDetailPopup: false,
    isReadOnly: true,
    theme: {
      week: {
        today: {
          color: "white !important",
        },
      },
    },
    calendars: [
      {
        id: "FAULT_HIS",
        name: computed(() => t("MAP_INQUERY_FAULT")),
        color: "#fff",
        backgroundColor: "orange",
      },
      {
        id: "SELF_INSP_HIS",
        name: computed(() => t("SELF_INSPECTION")),
        color: "#fff",
        backgroundColor: "#03bd9e",
      },
      {
        id: "INSP_HIS",
        name: computed(() => t("INSPECTION")),
        color: "#fff",
        backgroundColor: "#00a9ff",
      },
    ],
  });

  if (computedPortfolio.value?.userPortfolioMappingId) {
    if(useDataStore?.targetAcptDate)
    {
      const year  = new Date().getFullYear().toString();
      const month = (new Date().getMonth() + 1).toString().padStart(2, "0");

      const diffMonth = ((useDataStore.targetAcptDate.substr(0, 4) - year) * 12) + (useDataStore.targetAcptDate.substr(4, 2) - month);

      if((year + month) != useDataStore.targetAcptDate.substr(0, 6))
      {
        if (selectMonth.value === 12) {
          calendar.value.move(diffMonth);
        } else {
          for(let i = 0; i < Math.abs(diffMonth); i++)
          {
            calendar.value.prev();
          }
        }

        selectYears.value = calendar.value.getDate().getFullYear();
        selectMonth.value = calendar.value.getDate().d.getMonth() + 1;
      }
      else
      {
        await getCalender();

        selectDate(useDataStore.targetAcptDate);

        useDataStore.setAcptDate({});
      }
    }
    else
    {
      getCalender();
    }
  }

  datepicker.value = new DatePicker("#datePickerBox", {
    date: new Date(),
    input: {
      element: "#calendarTitle",
      format: "yyyy-MM-dd",
    },
    language: "ko",
    type: "month",
  });

  datepicker.value.on("change", () => {
    selectYears.value = datepicker.value.getDate().getFullYear();
    selectMonth.value = datepicker.value.getDate().getMonth() + 1;
  });

  calendar.value.on("selectDateTime", function (event) {
    const year = event.start.getFullYear().toString();
    const month = (event.start.getMonth() + 1).toString().padStart(2, "0");
    const date = event.start.getDate().toString().padStart(2, "0");
    const yyyymmdd = year + month + date;

    const fatalList = [];
    const inspList = [];
    targetFatalList.value = filteringEvents(totalData.value).forEach((item) => {
      if (item.dtype == "FAULT_HIS") {
        if (item.targetDt === yyyymmdd) {
          fatalList.push(item);
        }
      } else {
        if (item.dtype && item.targetDt === yyyymmdd) {
          item.dtypeText =
            item.dtype === "SELF_INSP_HIS"
              ? t("SELF_INSPECTION")
              : item.dtype === "INSP_HIS"
              ? t("INSPECTION")
              : "-";
          inspList.push(item);
        }
      }
    });
    targetFatalList.value = fatalList;
    targetInspList.value = inspList;
    const selection = document.querySelectorAll(
      ".toastui-calendar-grid-selection"
    );
    if (selection.length > 0) {
      selection.forEach((item) => {
        item.style.backgroundColor = "transparent";
        item.style.border = "none";
      });
    }
  });

  calendar.value.on("clickEvent", ({ event, nativeEvent }) => {
    const year = event.start.getFullYear().toString();
    const month = (event.start.getMonth() + 1).toString().padStart(2, "0");
    const date = event.start.getDate().toString().padStart(2, "0");

    selectedDateValue.value = year + month + date;

    selectDate(selectedDateValue.value);
  });
});

watch ([selectMonth, selectYears, weekView],  async (newVal, oldVal) => {
  if (oldVal[0] !== newVal[0] || oldVal[1] !== newVal[1]) {
    if (weekView.value == false) {
      calendar.value.setDate(
        new Date(selectYears.value, selectMonth.value - 1)
      );

      if(useDataStore.targetAcptDate)
      {
        await getCalender();

        selectDate(useDataStore.targetAcptDate);

        useDataStore.setAcptDate({});
      }
      else
      {
        getCalender();
      }
    }
  }
  if (oldVal[2] !== newVal[2]) {
    getCalender();
  }
});
watch(computedPortfolio, () => {
  if (computedPortfolio.value?.userPortfolioMappingId) {
    getCalender();
  }
});
watch(
  filteringValue,
  () => {
    calendar.value.clear();

    calendar.value.createEvents(filteringEvents(totalData.value));
  },
  { deep: true }
);
</script>
<template>
  <DefaultContainer>
    <v-col cols="12" lg="7" class="pt-0">
      <div
        :style="{ height: '28px' }"
        class="mb-2 ml-4 d-flex justify-end flex-end align-center"
      >
        <div class="d-flex align-center">
          <div
            class="mr-2"
            :style="{
              borderRadius: '50%',
              width: '8px',
              height: '8px',
              background: 'orange',
            }"
          ></div>
          {{ t("MAP_INQUERY_FAULT") }} ({{ t("TOOLTIP_1YEAR") }})
        </div>
        <div class="ml-8 d-flex align-center">
          <div
            class="mr-2"
            :style="{
              borderRadius: '50%',
              width: '8px',
              height: '8px',
              background: '#00a9ff',
            }"
          ></div>
          {{ t("INSPECTION") }} ({{ t("TOOLTIP_5YEAR") }})
        </div>
        <div class="ml-8 d-flex align-center">
          <div
            class="mr-2"
            :style="{
              borderRadius: '50%',
              width: '8px',
              height: '8px',
              background: '#03bd9e',
            }"
          ></div>
          {{ t("SELF_INSPECTION") }} ({{ t("TOOLTIP_1YEAR") }})
        </div>
      </div>
      <v-card
        class="pb-3 customCard"
        :style="{
          transition: 'none',
        }"
      >
        <div class="d-flex align-center py-3 px-3">
          <v-btn icon variant="plain" @click="prevMonth"
            ><v-icon>mdi-chevron-left</v-icon></v-btn
          >
          <v-btn size="large" variant="plain" id="calendarTitle">
            {{ ` ${selectMonth}${t("MONTH")} ${selectYears}` }}
          </v-btn>
          <div id="datePickerBox"></div>
          <v-btn icon variant="plain" @click="nextMonth"
            ><v-icon>mdi-chevron-right</v-icon></v-btn
          >
          <v-btn
            size="large"
            variant="plain"
            id="calendarTitle"
            @click="returnToday"
          >
            {{ t("TODAY") }}
          </v-btn>

          <v-spacer></v-spacer>
          <!-- <v-btn
            @click="
              weekView == false
                ? [calendar.changeView('week'), (weekView = true)]
                : [calendar.changeView('month'), (weekView = false)]
            "
            >{{ weekView == false ? "월간" : "주간" }}</v-btn
          > -->
          <v-menu location="end" :close-on-content-click="false">
            <template v-slot:activator="{ props }">
              <v-btn variant="plain" icon="mdi-filter" v-bind="props"></v-btn>
            </template>
            <v-card class="px-2 py-4">
              <v-list>
                <v-list-item
                  @click="filteringValue.type = 'ALL'"
                  :active="filteringValue.type == 'ALL' ? true : false"
                >
                  <template v-slot:prepend="{ isActive }">
                    <v-list-item-action start>
                      <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                    </v-list-item-action>
                  </template>
                  <v-list-item-title>{{ t("ALL") }}</v-list-item-title>
                </v-list-item>
                <v-list-item
                  @click="filteringValue.type = 'FAULT_HIS'"
                  :active="filteringValue.type == 'FAULT_HIS' ? true : false"
                >
                  <template v-slot:prepend="{ isActive }">
                    <v-list-item-action start>
                      <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                    </v-list-item-action>
                  </template>
                  <v-list-item-title>
                    {{ t("MAP_INQUERY_FAULT") }}
                  </v-list-item-title>
                </v-list-item>
                <v-list-item
                  @click="filteringValue.type = 'INSP_HIS'"
                  :active="filteringValue.type == 'INSP_HIS' ? true : false"
                >
                  <template v-slot:prepend="{ isActive }">
                    <v-list-item-action start>
                      <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                    </v-list-item-action>
                  </template>
                  <v-list-item-title>{{ t("INSPECTION") }}</v-list-item-title>
                </v-list-item>
                <v-list-item
                  @click="filteringValue.type = 'SELF_INSP_HIS'"
                  :active="
                    filteringValue.type == 'SELF_INSP_HIS' ? true : false
                  "
                >
                  <template v-slot:prepend="{ isActive }">
                    <v-list-item-action start>
                      <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                    </v-list-item-action>
                  </template>
                  <v-list-item-title>{{
                    t("SELF_INSPECTION")
                  }}</v-list-item-title>
                </v-list-item>
              </v-list>
              <v-list>
                <v-list-item
                  v-for="item in nmData"
                  :key="item"
                  @click="toggleFilterValueName(item)"
                  :active="
                    item === filteringValue.name.find((item2) => item2 === item)
                  "
                >
                  <template v-slot:prepend="{ isActive }">
                    <v-list-item-action start>
                      <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                    </v-list-item-action>
                  </template>
                  <v-list-item-title>{{ item }}</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-card>
          </v-menu>
        </div>

        <v-sheet :height="calcHeight">
          <div id="calendar" style="height: 100%"></div>
        </v-sheet>
      </v-card>
    </v-col>
    <v-col cols="12" lg="5" class="pt-0">
      <v-toolbar-title class="mb-2 ml-4">{{
        t("SELF_INSPECTION_RESULT")
      }}</v-toolbar-title>
      <Skeleton
        :loadingData="targetInspList.length == 0"
        :height="'calc(100vh - 519px)'"
        :type="'heading,table-row,table-row,table-row'"
      >
        <Tabulator
          :selectable="false"
          :pagination="true"
          :columns="columns"
          :tableData="targetInspList"
          :height="'calc(100vh - 519px)'"
        >
        </Tabulator>
      </Skeleton>

      <v-toolbar class="mt-2" density="compact" color="transparent">
        <v-toolbar-title>{{ t("FAULT_HISTORY") }}</v-toolbar-title>
        <v-btn
          icon="mdi-refresh"
          @click="[
            refreshData(),
          ]"
        ></v-btn>
      </v-toolbar>
      <Skeleton
        :padding="true"
        :loadingData="targetFatalList.length == 0"
        :height="'calc(100vh - 519px)'"
        :type="'image'"
      >
        <v-card
          rounded="lg"
          class="customCard py-4 mx-4 mt-1 mb-4"
          v-for="item in targetFatalList"
          :key="item"
        >
          <div style="display:flex;justify-content: space-between;">
          <v-card-subtitle class="largeBoldText">
            {{ item.prjNm }}
          </v-card-subtitle>
          <v-btn
            variant="outlined"
            rounded="lg"
            :style="{margin:'0 10px 0 0'}"
            color="primary"
            @click="[
              targetCardData=item,
              dialog = true,
            ]"
            >{{ t("VIEW_DETAIL") }}
          </v-btn>
          </div>
          <v-card-subtitle>
            {{ t("PRJ_NO") }} : {{ item.intgPrjNo + item.hoNo }}
          </v-card-subtitle>
          <v-card-subtitle>{{ t("DATE") }} : {{ item.start }}</v-card-subtitle>
          <v-timeline
            class="px-4 pb-4"
            side="end"
            direction="horizontal"
            line-inset="12"
          >
            <v-timeline-item
              :dot-color="item.status == '접수' ? 'primary' : ''"
              size="small"
            >
              {{ t("REGISTER") }}
            </v-timeline-item>

            <v-timeline-item
              :dot-color="item.status == '배치' ? 'primary' : ''"
              size="small"
            >
              {{ t("DEPLOYMENT") }}
            </v-timeline-item>

            <v-timeline-item
              :dot-color="item.status == '도착' ? 'primary' : ''"
              size="small"
            >
              {{ t("PROCESSING") }}
            </v-timeline-item>

            <v-timeline-item
              :dot-color="item.status == '완료' ? 'primary' : ''"
              size="small"
            >
              {{ t("SUCCESS") }}
            </v-timeline-item>
          </v-timeline>
        </v-card>
      </Skeleton>
    </v-col>
  </DefaultContainer>
  <!-- <v-dialog v-model="dialog" width="30vw">
    <v-card rounded="lg" elevation="4" class="py-3">
      <v-card-subtitle class="py-4">서비스 요청</v-card-subtitle>
      <v-container>
        <v-row>
          <v-col cols="3" class="justify-center d-flex align-center">
            계약번호
          </v-col>
          <v-col cols="9"
            ><v-select
              density="compact"
              label="2023-14호"
              variant="solo"
              hide-details
              return-object
              :items="['2023-1호', '2023-2호']"
            ></v-select
          ></v-col>
          <v-col cols="3" class="justify-center d-flex align-center"
            >호기번호</v-col
          >
          <v-col cols="9"
            ><v-select
              density="compact"
              label="X231113"
              variant="solo"
              hide-details
              return-object
              :items="['C22322343', 'ZX222311123']"
            ></v-select
          ></v-col>
          <v-col cols="3" class="justify-center d-flex align-center"
            >서비스 종류</v-col
          >
          <v-col cols="9"
            ><v-select
              density="compact"
              label="서비스 종류"
              variant="solo"
              hide-details
              v-model="selectSerivce"
              return-object
              :items="[
                '고장 신고',
                '청구서 재발행',
                '청구서 받는 방법 변경',
                '세금 계산서 수신처 변경',
                '납부 방법 변경',
                '고객정보 변경',
              ]"
            ></v-select>
          </v-col>
          <v-col
            v-if="selectSerivce === '고장 신고'"
            cols="3"
            class="justify-center d-flex align-center"
            >고장 내역</v-col
          >
          <v-col cols="9" v-if="selectSerivce === '고장 신고'"
            ><v-select
              density="compact"
              label="고장 내역"
              variant="solo"
              hide-details
              :model-value="정지"
              return-object
              :items="[
                '운행 중 정지',
                '정지',
                '승강기 소음 발생',
                '승강기 진동 발생',
                '기타',
              ]"
            ></v-select>
          </v-col>
          <v-col cols="3" class="justify-center d-flex align-center"
            >상세 내역</v-col
          >
          <v-col cols="9"
            ><v-sheet class="d-flex justify-center align-center">
              Lorem Ipsum has been the industry's standard dummy text ever when
              an unknown printer took a galley of type and scrambled it to make
              a type specimen book. It has survived not only five centuries,
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>

      <v-card-actions class="justify-end">
        <v-btn color="primary">서비스 요청</v-btn>
        <v-btn @click="dialog = false">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog> -->
  <ServiceDetailPopupModal v-model="dialog" v-if="dialog" :data="targetCardData"></ServiceDetailPopupModal>
</template>
<style lang="scss">
#calendarTitle {
  border-width: 1px;
  border-color: white;
  transition: 0.3s;

  &:hover {
    border: 1px solid black;
  }
}
.toastui-calendar-layout {
  background: v-bind(computedBackGround) !important;
  .toastui-calendar-weekday-grid-date:not(.toastui-calendar-weekday-grid-date-decorator) {
    color: v-bind(computedText) !important;
  }
  .toastui-calendar-template-monthDayName {
    color: v-bind(computedText) !important;
  }
}
.toastui-calendar-see-more {
  background: v-bind(computedBackGround) !important;
}
.toastui-calendar-more-title-date {
  color: v-bind(computedText) !important;
}
.toastui-calendar-more-title-day {
  color: v-bind(computedText) !important;
}
#datePickerBox {
  > .tui-datepicker {
    
    z-index: 2;
  }

  .tui-calendar-body {
    // margin: 1em auto;
  }
}

.toastui-calendar-template-time {
  strong {
    display: none;
  }
}
.toastui-calendar-see-more-container {
  position: absolute;
  width: 400px !important;
  top: 50% !important;
  left: 50% !important;
  transform: translate(-50%, -50%);
}
</style>
