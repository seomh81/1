<script setup>
import { ref, onMounted, watch, computed } from "vue";

import Calendar from "@toast-ui/calendar";
import "@toast-ui/calendar/dist/toastui-calendar.min.css"; // Calendar 스타일
import DatePicker from "tui-date-picker";
import DefaultContainer from "@/component/DefaultContainer.vue";

import { Tabulator } from "@/component/Template";
const calendar = ref();
const datepicker = ref();
const selectMonth = ref(new Date().getMonth() + 1);
const selectYears = ref(new Date().getFullYear());
const dialog = ref(false);
const calendarList = ref([
  {
    id: "event1",
    calendarId: "cal2",
    title: "주간 회의",
    data: [{ contractNo: "123" }],
    start: "2023-02-07T09:00:00",
    end: "2023-02-07T12:00:00",
  },
  {
    id: "event2",
    calendarId: "cal1",
    title: "점심 약속",
    start: "2023-02-11T12:00:00",
    end: "2023-02-11T13:00:00",
  },
  {
    id: "event6",
    calendarId: "cal1",
    title: "점심 약속",
    start: "2023-02-15T12:00:00",
    end: "2023-02-16T13:00:00",
  },
  {
    id: "event4",
    calendarId: "cal1",
    title: "점심 약속",
    start: "2023-02-13T13:00:00",
    end: "2023-02-13T14:00:00",
  },
  {
    id: "event3",
    calendarId: "cal2",
    title: "휴가",
    start: "2023-02-18",
    end: "2023-02-21",
    isAllday: true,
    category: "allday",
  },
  {
    id: "event5",
    calendarId: "cal2",
    title: "test",
    start: "2023-02-11",
    end: "2023-02-13",
    isAllday: true,
    category: "allday",
  },
]);
const desserts = [
  {
    day: "2022.10.23",
    name: "Frozen Yogurt",
    calories: 159,
  },
  {
    day: "2022.10.23",

    name: "Ice cream sandwich",
    calories: 237,
  },
  {
    day: "2022.10.23",

    name: "Eclair",
    calories: 262,
  },
  {
    day: "2022.10.23",

    name: "Cupcake",
    calories: 305,
  },
  {
    day: "2022.10.23",

    name: "Gingerbread",
    calories: 356,
  },
  {
    day: "2022.10.23",

    name: "Jelly bean",
    calories: 375,
  },
  {
    day: "2022.10.23",

    name: "Lollipop",
    calories: 392,
  },
  {
    day: "2022.10.23",

    name: "Honeycomb",
    calories: 408,
  },
  {
    day: "2022.10.23",

    name: "Donut",
    calories: 452,
  },
  {
    day: "2022.10.23",

    name: "KitKat",
    calories: 518,
  },
];
let headColumn = computed(() => {
  return [
    {
      formatter: "rowSelection",
      titleFormatter: "rowSelection",
      width: "70",
      headerSort: false,
      headerHozAlign: "center",
      hozAlign: "center",
    },
    {
      title: "날짜",
      field: "day",
      sorter: "string",
    },
    {
      title: "텍스트",
      field: "text",
      sorter: "string",
    },
  ];
});
const datas = [
  {
    day: "2022-01-23",
    text: "테스트1번",
  },
  {
    day: "2022-01-13",
    text: "테스트2번",
  },
  {
    day: "2022-05-23",
    text: "테스트3번",
  },
  {
    day: "2022-08-02",
    text: "테스트4번",
  },
  {
    day: "2022-08-22",
    text: "테스트5번",
  },
  {
    day: "2022-11-31",
    text: "테스트6번",
  },
];
onMounted(() => {
  const container = document.getElementById("calendar");

  calendar.value = new Calendar(container, {
    defaultView: "month",
    usageStatistics: false,
    useFormPopup: false,
    useDetailPopup: false,
    isReadOnly: true,

    calendars: [
      {
        id: "cal1",
        name: "개인",
        color: "#fff",
        backgroundColor: "#03bd9e",
      },
      {
        id: "cal2",
        name: "직장",
        color: "#fff",
        backgroundColor: "#00a9ff",
      },
    ],
  });

  calendar.value.createEvents(calendarList.value);
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
    // calendar.value.setDate(new Date(selectYears, selectMonth));

    // console.log(`Selected date: ${datepicker.value.getDate().getMonth() + 1}`);
  });
  calendar.value.on("beforeCreateEvent", (scheduleData) => {
    const schedule = {
      calendarId: scheduleData.calendarId,
      id: String(Math.random() * 100000000000000000),
      title: scheduleData.title,
      data: scheduleData.data,
      isAllDay: scheduleData.isAllDay,
      start: scheduleData.start,
      end: scheduleData.end,
      category: scheduleData.isAllDay,
      location: scheduleData.location, // 장소 정보도 입력할 수 있네요!
    };

    calendarList.value.push({ ...schedule });
    calendar.value.createEvents([schedule]);
  });
  calendar.value.on("beforeUpdateEvent", ({ event, changes }) => {
    const index = calendarList.value.findIndex((items) => event.id == items.id);
    Object.assign(calendarList.value[index], changes);
    calendar.value.updateEvent(event.id, event.calendarId, changes);
  });

  calendar.value.on("beforeDeleteEvent", (eventObj) => {
    const index = calendarList.value.findIndex(
      (items) => eventObj.id == items.id
    );
    calendarList.value.splice(index, 1);

    calendar.value.deleteEvent(eventObj.id, eventObj.calendarId);
  });
  calendar.value.on("clickEvent", (eventObj) => {
    const modalData = calendarList.value.find(
      (item) => item.id === eventObj.event.id
    );
    console.log(modalData);
    dialog.value = true;
  });
});

watch([selectMonth, selectYears], () => {
  calendar.value.setDate(new Date(selectYears.value, selectMonth.value - 1));
  // console.log(calendar.value.getDate().d);
});

const nextMonth = () => {
  if (selectMonth.value === 12) {
    calendar.value.move(1);
  } else {
    calendar.value.next();
  }
  selectYears.value = calendar.value.getDate().getFullYear();
  selectMonth.value = calendar.value.getDate().d.getMonth() + 1;

  //   calendar.value.move(12);
  //   calendar.value.setDate("2023-1");
};
const prevMonth = () => {
  //   calendar.value.setDate("2023-12");
  if (selectMonth.value === 12) {
    calendar.value.move(-1);
  } else {
    calendar.value.prev();
  }
  selectYears.value = calendar.value.getDate().getFullYear();
  selectMonth.value = calendar.value.getDate().d.getMonth() + 1;
  // calendar.value.move(1);
};
const onBeforeCreateSchedule = (e) => {
  console.log(e);
};
const returnToday = () => {
  selectMonth.value = new Date().getMonth() + 1;
  selectYears.value = new Date().getFullYear();
  calendar.value.setDate(new Date());
};
</script>
<template>
  <DefaultContainer>
    <v-col cols="8">
      <v-card class="pa-3">
        <v-card-actions class="px-0">
          <v-btn icon variant="plain" @click="prevMonth"
            ><v-icon>mdi-chevron-left</v-icon></v-btn
          >
          <v-btn size="large" variant="plain" id="calendarTitle">
            {{ ` ${selectMonth}월 ${selectYears}` }}
          </v-btn>
          <div id="datePickerBox"></div>
          <v-btn icon variant="plain" @click="nextMonth"
            ><v-icon>mdi-chevron-right</v-icon></v-btn
          >
          <v-spacer></v-spacer>
          <v-btn
            size="large"
            variant="plain"
            id="calendarTitle"
            @click="returnToday"
          >
            Today
          </v-btn>
        </v-card-actions>

        <!-- <v-col cols="auto" class="d-flex align-center justify-end"> -->
        <!-- <v-select
          variant="outlined"
          :items="monthList"
          v-model="selectMonth"
          item-title="title"
          item-value="month"
        ></v-select> -->
        <!-- </v-col> -->
        <v-sheet height="78.2vh">
          <div
            id="calendar"
            style="height: 100%"
            @beforeCreateSchedule="onBeforeCreateSchedule"
          ></div>
        </v-sheet>
      </v-card>
    </v-col>
    <v-col cols="4">
      <v-card>
        <v-card-title>진행완료</v-card-title>
        <div class="pa-3">
          <Tabulator
            :columns="headColumn"
            :tableName="'table'"
            :tableData="datas"
            :height="'35.2vh'"
          />
        </div>
      </v-card>
      <v-card class="mt-3">
        <v-card-title>진행중</v-card-title>
        <div class="pa-3">
          <Tabulator
            :columns="headColumn"
            :tableName="'table'"
            :tableData="datas"
            :height="'35.2vh'"
          />
        </div>
      </v-card>
    </v-col>
  </DefaultContainer>
  <v-dialog v-model="dialog" width="30vw">
    <v-card rounded="lg" elevation="4" class="py-3">
      <v-card-subtitle class="py-4">서비스 요청</v-card-subtitle>
      <v-container>
        <v-row>
          <v-col cols="3" class="justify-center d-flex align-center">
            계약번호
          </v-col>
          <v-col cols="9"
            ><v-select
              class="customeHeightInput select fieldRadius lg"
              label="2023-14호"
              variant="outlined"
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
              class="customeHeightInput select fieldRadius lg"
              label="X231113"
              variant="outlined"
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
              class="customeHeightInput select fieldRadius lg"
              label="고장신고"
              variant="outlined"
              hide-details
              return-object
              :items="['기사 요청', '고장 신고']"
            ></v-select
          ></v-col>
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
  </v-dialog>
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
#datePickerBox {
  > .tui-datepicker {
    // width: 300px;
    z-index: 2;
    // margin-left: 0.5rem;
  }
  .tui-calendar-body {
    // margin: 1em auto;
  }
}
</style>
