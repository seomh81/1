<script setup>
import { ref, onMounted, watch } from "vue";
import SampleMenuView from "@/view/sample/SampleMenuView.vue";

import Calendar from "@toast-ui/calendar";
import "@toast-ui/calendar/dist/toastui-calendar.min.css"; // Calendar 스타일
import DatePicker from "tui-date-picker";

const options = {};
const monthList = [
  {
    title: "1월",
    month: 1,
  },
  {
    title: "2월",
    month: 2,
  },
  {
    title: "3월",
    month: 3,
  },
  {
    title: "4월",
    month: 4,
  },
  {
    title: "5월",
    month: 5,
  },
  {
    title: "6월",
    month: 6,
  },
  {
    title: "7월",
    month: 7,
  },
  {
    title: "8월",
    month: 8,
  },
  {
    title: "9월",
    month: 9,
  },
  {
    title: "10월",
    month: 10,
  },
  {
    title: "11월",
    month: 11,
  },
  {
    title: "12월",
    month: 12,
  },
];
const calendar = ref();
const datepicker = ref();
const selectMonth = ref(new Date().getMonth() + 1);
const selectYears = ref(new Date().getFullYear());
const calendarList = ref([
  {
    id: "event1",
    calendarId: "cal2",
    title: "주간 회의",
    start: "2023-01-07T09:00:00",
    end: "2023-01-07T12:00:00",
  },
  {
    id: "event2",
    calendarId: "cal1",
    title: "점심 약속",
    start: "2023-01-11T12:00:00",
    end: "2023-01-11T13:00:00",
  },
  {
    id: "event6",
    calendarId: "cal1",
    title: "점심 약속",
    start: "2023-01-15T12:00:00",
    end: "2023-01-16T13:00:00",
  },
  {
    id: "event4",
    calendarId: "cal1",
    title: "점심 약속",
    start: "2023-01-13T13:00:00",
    end: "2023-01-13T14:00:00",
  },
  {
    id: "event3",
    calendarId: "cal2",
    title: "휴가",
    start: "2023-01-18",
    end: "2023-01-21",
    isAllday: true,
    category: "allday",
  },
  {
    id: "event5",
    calendarId: "cal2",
    title: "test",
    start: "2023-01-11",
    end: "2023-01-13",
    isAllday: true,
    category: "allday",
  },
]);
onMounted(() => {
  const container = document.getElementById("calendar");

  calendar.value = new Calendar(container, {
    defaultView: "month",
    usageStatistics: false,
    useFormPopup: true,
    useDetailPopup: true,
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
  <v-container fluid>
    <SampleMenuView />
    <!-- <div class="tui-datepicker-input tui-datetime-input tui-has-focus"> -->
    <!-- <input type="text" id="datepicker-input" aria-label="Date-Time" /> -->
    <!-- <span class="tui-ico-date"></span> -->
    <!-- </div> -->

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
    <v-sheet height="80vh">
      <div
        id="calendar"
        style="height: 600px"
        @beforeCreateSchedule="onBeforeCreateSchedule"
      ></div>
    </v-sheet>
  </v-container>
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
    width: 300px;
    z-index: 2;
    margin-left: 0.5rem;
  }
  .tui-calendar-body {
    margin: 1em auto;
  }
}
</style>
