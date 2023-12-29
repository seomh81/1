<script setup>
import { ref, reactive, onMounted } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
import axios from "axios";
import dayjs from "dayjs";
import "dayjs/locale/ko";
import { useI18n } from "vue-i18n";
dayjs.locale("ko");
const { t } = useI18n();
const props = defineProps(["item", "request"]);

const fiveDaysWeather = ref([]);

const params = {
  q: "Seoul",
  units: "metric",
  lang: "kr",
  appid: process.env.VUE_APP_OPEN_WEATHER_KEY,
};

const getWeather = async () => {
  const weather = await axios.get(
    "https://api.openweathermap.org/data/2.5/forecast",
    {
      params,
    }
  );
  const data = weather.data.list;
  const result = [];

  // 오늘부터 5일치의 데이터를 가져옴
  for (let i = 0; i < 5; i++) {
    const targetDate = new Date();
    targetDate.setDate(targetDate.getDate() + i); // 오늘부터 5일 후의 날짜를 구함
    const dateString = targetDate.toISOString().split("T")[0]; // 날짜 포맷을 변경하여 문자열로 변환

    let temp_max = -Infinity; // 최고온도 초기값 설정
    let temp_min = Infinity; // 최저온도 초기값 설정
    let weather_icon = null; // 날씨 상태 초기값 설정
    let current_temp = null; // 현재 온도 초기값 설정
    let feels_like = null;
    let current_time = null;
    // 해당 날짜의 모든 데이터를 비교하여 최고온도와 최저온도를 구함
    for (let j = 0; j < data.length; j++) {
      const date = data[j].dt_txt.split(" ")[0];
      if (date === dateString) {
        const temp = data[j].main.temp;
        if (temp > temp_max) {
          temp_max = temp;
        }
        if (temp < temp_min) {
          temp_min = temp;
        }
        // 첫번째 weather 요소를 이용하여 날씨 상태를 구함
        weather_icon = data[j].weather[0].main;
        const dt = new Date(data[j].dt_txt);
        const now = new Date();
        // 현재 시간과 가장 가까운 시간의 온도를 구함
        if (dt <= now && now - dt < 3 * 60 * 60 * 1000) {
          current_time = dayjs(data[j].dt_txt).format("A hh:mm");
          current_temp = data[j].main.temp;
          feels_like = data[j].main.feels_like;
        }
      }
    }
    result.push({
      date: dateString,
      current_time,
      day: dayjs(dateString).format("dd"),
      temp_max: Math.round(temp_max),
      temp_min: Math.round(temp_min),
      weather_icon:
        weather_icon == "Clear"
          ? "sunny"
          : weather_icon == "Clouds"
          ? "cloudy"
          : weather_icon == "Thunderstorm"
          ? "thunderstorm"
          : weather_icon == "Drizzle"
          ? "weather_hail"
          : weather_icon == "Rain"
          ? "water_drop"
          : weather_icon == "Mist"
          ? "mist"
          : weather_icon == "Fog"
          ? "foggy"
          : weather_icon == "Tornado"
          ? "tornado"
          : weather_icon == "Snow"
          ? "snowing"
          : "",
      current_temp: Math.round(current_temp),
      feels_like: Math.round(feels_like),
    });
  }

  fiveDaysWeather.value = result;
};
const weatherFormatting = (timestamp) => {
  let a = new Date(timestamp);
  // console.log(new Date().getTime());
  // console.log(new Date(1607110465663))
  // console.log("뉴 데이트", a);
  // console.log("겟타임", a.getTime());
  // console.log("타임스탬프", timestamp);
  // today.setHours(today.getHours() + 9);
  // return a.toISOString().split("T")[0];
};

onMounted(() => {
  if (props.request) {
    getWeather();
  }
});
</script>
<template>
  <div class="weather-box d-flex">
    <div class="weather-side px-6 py-0">
      <!-- <v-list-item-title
          class="text-white pl-1 pb-1 font-weight-bold text-medium-emphasis"
          >{{ weatherData.city }}
          <v-icon icon="mdi-map-marker" size="small"></v-icon>
        </v-list-item-title> -->
      <div :style="{ width: '180px' }">
        <p class="pl-1 font-weight-bold text-white text-medium-emphasis">
          {{ t("CITY_SEOUL") }}
        </p>
        <v-list-item-title class="text-white py-2 text-h3 font-weight-bold"
          >{{ fiveDaysWeather[0]?.current_temp ?? 0 }}°C</v-list-item-title
        >

        <p class="pl-1 font-weight-bold text-white text-medium-emphasis pb-1">
          {{
            `${fiveDaysWeather[0]?.temp_max ?? 0}°C / ${
              fiveDaysWeather[0]?.temp_min ?? 0
            }°C
          `
          }}
        </p>
        <p class="pl-1 font-weight-bold text-white text-medium-emphasis">
          {{
            `${fiveDaysWeather[0]?.day ?? "일"}, ${
              fiveDaysWeather[0]?.current_time ?? "00:00"
            }`
          }}
        </p>
      </div>
      <v-spacer></v-spacer>
      <span
        class="material-icons"
        :style="{ fontSize: '70px', color: 'white', width: '70px' }"
      >
        {{ fiveDaysWeather[0]?.weather_icon }}
      </span>
    </div>

    <div
      class="info-side d-flex align-center"
      v-if="props.item?.width ? props.item.width > 34 : false"
    >
      <div
        v-for="(item, i) in fiveDaysWeather"
        :key="item"
        class="text-center py-0 px-6"
      >
        <div class="py-5 px-0 text-center" v-if="i !== 0">
          <span class="text-white material-icons" :style="{ width: '24px' }">
            {{ item.weather_icon }}
          </span>
          <p
            class="infoText font-weight-bold text-white pt-3 text-medium-emphasis"
          >
            {{ item.day }}
          </p>
          <p class="infoText font-weight-bold text-white text-medium-emphasis">
            {{
              `${Math.round(item.temp_max)}°C / ${Math.round(item.temp_min)}°C
          `
            }}
          </p>
        </div>
      </div>
    </div>

    <!-- <v-row class="py-0">
      <v-col class="d-flex justify-center align-center">
        <v-icon icon="mdi-calendar-range" size="small" class="mr-1"></v-icon>
        <div class="text-button text-medium-emphasis">목요일</div>
      </v-col>
    </v-row>
    <v-card-text class="py-3 d-flex justify-space-between">
      <div class="text-h3 font-weight-bold text-medium-emphasis" cols="6">
        29'C
      </div>
      <v-icon
        color="warning"
        icon="mdi-weather-sunny"
        size="50"
        class="mr-3"
      ></v-icon>
    </v-card-text>

    <v-row class="py-2">
      <v-col class="d-flex justify-center align-center">
        <div class="text-button text-medium-emphasis">역삼동</div>
      </v-col>
      <v-col class="d-flex justify-center align-center">
        <v-icon icon="mdi-weather-windy" size="small" class="mr-1"></v-icon>
        <div class="text-button text-medium-emphasis">역삼동</div>
      </v-col>
      <v-col class="d-flex justify-center align-center">
        <v-icon icon="mdi-weather-pouring" size="small" class="mr-1"></v-icon>
        <div class="text-button text-medium-emphasis">48%</div>
      </v-col>
    </v-row> -->
  </div>
</template>
<style scoped>
.weather-box {
  height: 100%;
}
.weather-side {
  display: flex;
  align-items: center;
  flex-grow: 1;
  background: #4b6cb7; /* fallback for old browsers */
  background: -webkit-linear-gradient(
    to right,
    #4b6cb7,
    #182848
  ); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(
    to right,
    #4b6cb7,
    #182848
  ); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
}

.info-side {
  flex-basis: 58%;
  background: #182848; /* fallback for old browsers */
}
.infoText {
  font-size: 0.7rem;
}
</style>
