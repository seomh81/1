<script setup>
import { ref } from "vue";
</script>
<template>
  <v-card width="360" elevation="0" class="mt-3" rounded="lg">
    <v-toolbar color="transparent" density="comfortable">
      <v-toolbar-title class="largeBoldText">MIRI 연결 서비스</v-toolbar-title>
      <v-card-actions>
        <v-btn color="primary" rounded="lg" variant="outlined">상세보기</v-btn>
      </v-card-actions>
    </v-toolbar>
    <v-divider class="mx-4"></v-divider>
    <v-container class="py-5">
      <v-row>
        <v-col cols="6">
          <p class="smallBoldText pb-1">부품 수명(건)</p>

          <p class="font-weight-bold text-h4">237</p>
        </v-col>
        <v-col cols="6">
          <v-row>
            <v-col class="d-flex justify-space-between">
              <p class="">로프</p>
              <p class="boldText">28</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">쉬브</p>
              <p class="boldText">3</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">인버트</p>
              <p class="boldText">36</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">도어인버터</p>
              <p class="boldText">32</p>
            </v-col>
            <v-divider></v-divider>
            <v-col class="d-flex justify-space-between">
              <p class="">도어 안전장치</p>
              <p class="boldText">136</p>
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
