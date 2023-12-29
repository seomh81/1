<script setup>
import { ref, reactive } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
import axios from "axios";

const props = defineProps(["item"]);
const useConnectionStore = connectionStore();
const weatherData = reactive({
  city: "",
  temp: 0,
  tempMax: 0,
  tempMin: 0,
  feelsLike: 0,
  day: "",
  time: "",
});
const week = ["일", "월", "화", "수", "목", "금", "토"];
const icon = [
  "nights_stay",
  "nights_stay",
  "air",
  "sunny_snowing",
  "sunny",
  "snowing",
];
const getNowWeather = async (params, i) => {
  const { data } = await axios.get(
    "http://api.openweathermap.org/data/2.5/weather?q=Seoul&appid=abf7e052c3925dbae905a657956470b7"
  );
  console.log(data);
  weatherData.city = data.name;
  weatherData.temp = Math.round(data.main.temp - 273.15);
  weatherData.tempMax = Math.round(data.main.temp_max - 273.15);
  weatherData.tempMin = Math.round(data.main.temp_min - 273.15);
  weatherData.feelsLike = Math.round(data.main.feels_like - 273.15);
  weatherData.day = week[new Date().getDay()];
  weatherData.time = new Date().toLocaleTimeString().slice(0, 7);
};
const getWeather = async () => {
  const { data } = await axios.get(
    "https://api.openweathermap.org/data/2.5/forecast?lat=37.413294&lon=127.269311&appid=ad1b196bd54a92f2a29e831ed77b3561"
  );
  console.log(data);
  data.list.forEach((data) => {
    const a = weatherFormatting(data.dt);
  });
};
const weatherFormatting = (timestamp) => {
  let a = new Date(timestamp);
  console.log(new Date().getTime());
  // console.log(new Date(1607110465663))
  // console.log("뉴 데이트", a);
  // console.log("겟타임", a.getTime());
  // console.log("타임스탬프", timestamp);
  // today.setHours(today.getHours() + 9);
  // return a.toISOString().split("T")[0];
};
getWeather();
getNowWeather();
</script>
<template>
  <div class="weather-box d-flex">
    <v-list class="weather-side pl-4">
      <v-list-item>
        <template v-slot:append>
          <span
            class="material-icons"
            :style="{ fontSize: '70px', color: 'white' }"
          >
            sunny
          </span>
        </template>
        <v-list-item-title
          class="text-white pl-1 pb-1 font-weight-bold text-medium-emphasis"
          >{{ weatherData.city }}
          <v-icon icon="mdi-map-marker" size="small"></v-icon>
        </v-list-item-title>
        <v-list-item-title class="text-white pb-4 text-h3 font-weight-bold pb-1"
          >{{ weatherData.temp }}°C</v-list-item-title
        >

        <p class="pl-1 font-weight-bold text-white text-medium-emphasis pb-1">
          {{
            `${weatherData.tempMax} / ${weatherData.tempMin},  체감온도
           ${weatherData.feelsLike}
          `
          }}
        </p>
        <p class="pl-1 font-weight-bold text-white text-medium-emphasis">
          {{ `${weatherData.day}, ${weatherData.time}` }}
        </p>
      </v-list-item>
    </v-list>

    <div
      class="info-side d-flex align-center"
      v-if="props.item?.width ? props.item.width > 34 : false"
    >
      <v-container>
        <v-row>
          <v-col v-for="n in 5" :key="n" class="text-center py-0 px-1">
            <div class="py-5 px-0">
              <span class="text-white material-icons"> {{ icon[n] }} </span>

              <p
                class="infoText font-weight-bold text-white pt-3 text-medium-emphasis"
              >
                {{ week[n + 1] }}
              </p>
              <p
                class="infoText font-weight-bold text-white text-medium-emphasis"
              >
                {{
                  `${Math.round(Math.random() * 30)}°C / ${Math.round(
                    Math.random() * 30
                  )}°C
          `
                }}
              </p>
            </div>
          </v-col>
        </v-row>
      </v-container>
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
  flex-basis: 55%;
  background: #182848; /* fallback for old browsers */
}
.infoText {
  font-size: 0.7rem;
}
</style>
