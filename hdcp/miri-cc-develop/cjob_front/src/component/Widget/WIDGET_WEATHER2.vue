<script setup>
import { connectionStore } from "@/store/ConnectionStore";
import axios from "axios";

const useConnectionStore = connectionStore();

const sendRequest = async (params, i) => {
  const data = await axios.get(
    "http://api.openweathermap.org/data/2.5/weather?q=Seoul&appid=abf7e052c3925dbae905a657956470b7"
  );

};
sendRequest();
</script>
<template>
  <div>
    <v-list class="weather-side py-3">
      <v-list-item class="pb-0 d-flex justify-space-between align-center">
        <v-list-item-title
          class="text-white font-weight-bold text-medium-emphasis"
          >역삼동
          <v-icon icon="mdi-map-marker" size="small"></v-icon>
        </v-list-item-title>
      </v-list-item>
      <v-list-item class="py-3">
        <v-row align-item="center">
          <v-col cols="auto" class="pr-2">
            <v-list-item-title class="text-white text-h3 font-weight-bold"
              >9°C</v-list-item-title
            >
          </v-col>
          <v-col cols="auto" class="d-flex align-end flex-wrap mb-1">
            <div>
              <p
                class="text-caption font-weight-medium text-white text-medium-emphasis"
                :style="{ flex: '100%', lineHeight: '10px' }"
              >
                13 /0' 체감온도 3'
              </p>
              <p
                class="text-caption font-weight-medium text-white text-medium-emphasis"
                :style="{ flex: '100%' }"
              >
                월, 오후 4:23
              </p>
            </div>
          </v-col>
        </v-row>
        <template v-slot:append>
          <span
            class="material-icons"
            :style="{ fontSize: '56px', color: 'white' }"
          >
            sunny
          </span>
        </template>
      </v-list-item>
    </v-list>
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
.weather-side {
  width: 100%;
  height: 100%;

  background: #7f7fd5; /* fallback for old browsers */
  background: -webkit-linear-gradient(
    to right,
    #7f7fd5,
    #86a8e7,
    #91eae4
  ); /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(
    to right,
    #7f7fd5,
    #86a8e7,
    #91eae4
  ); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

  transition: transform 300ms ease, -webkit-transform 300ms ease;
}

.weather-side:hover {
  -webkit-transform: scale(1.03) perspective(1500px) rotateY(10deg);
  transform: scale(1.03) perspective(1500px) rotateY(10deg);
}
</style>
