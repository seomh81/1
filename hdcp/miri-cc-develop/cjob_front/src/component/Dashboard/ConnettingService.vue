<script setup>
import { ref } from "vue";
</script>
<template>
  <v-card width="360" elevation="0" rounded="lg">
    <v-toolbar color="transparent" density="comfortable">
      <v-toolbar-title class="largeBoldText">MIRI 연결 서비스</v-toolbar-title>
      <v-card-actions>
        <v-btn rounded="lg" color="primary" size="small" variant="outlined"
          >당월</v-btn
        >
        <v-btn rounded="lg" color="primary" size="small" variant="outlined"
          >당분기</v-btn
        >
        <v-btn rounded="lg" color="primary" size="small" variant="outlined"
          >연간</v-btn
        >
      </v-card-actions>
    </v-toolbar>
    <!-- <v-card-actions class="justify-center py-0">
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
    </v-card-actions> -->

    <v-container class="py-5">
      <v-row>
        <v-col cols="6">
          <p class="smallBoldText pb-1">고장 처리(건)</p>

          <p class="font-weight-bold text-h4">119</p>
        </v-col>
        <v-col cols="6">
          <v-row>
            <v-col class="d-flex justify-space-between">
              <p class="">고장감지</p>
              <p class="boldText">5</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">원격수리</p>
              <p class="boldText">124</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">출동수리</p>
              <p class="boldText">86</p>
            </v-col>
          </v-row>
        </v-col>
        <v-divider></v-divider>
      </v-row>
      <v-row>
        <v-col cols="6">
          <p class="smallBoldText pb-1">정기점검 처리(건)</p>

          <p class="font-weight-bold text-h4">129/206</p>
        </v-col>
        <v-col cols="6">
          <v-row>
            <v-col class="d-flex justify-space-between">
              <p class="">점검</p>
              <p class="boldText">129/206</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">원격</p>
              <p class="boldText">79</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">현장</p>
              <p class="boldText">50</p>
            </v-col>
          </v-row>
        </v-col>
        <v-divider></v-divider>
      </v-row>
      <v-row>
        <v-col cols="6">
          <p class="smallBoldText">플러스서비스</p>
        </v-col>
        <v-col cols="6">
          <v-row>
            <v-col class="d-flex justify-space-between">
              <p class="">로봇</p>
              <p class="text-medium-emphasis">준비중</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">MIRI-Call</p>
              <p class="text-medium-emphasis">비가동</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">MIRI-View</p>
              <p class="text-medium-emphasis">점검중</p>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>
<style lang="scss">
.FacilityProgress {
  .v-progress-linear__background {
    display: none;
  }
  .v-progress-linear__determinate {
    border-radius: 20px;
  }
}
</style>
