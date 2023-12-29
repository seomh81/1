<script setup>
import { ref } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import Info from "@/component/Dashboard/Info";

import FacilityStatus from "@/component/Dashboard/FacilityStatus.vue";
import Notice from "@/component/Dashboard/Notice.vue";
import ConnettingService from "@/component/Dashboard/ConnettingService.vue";
import AiDiagnosis from "@/component/Dashboard/AiDiagnosis.vue";
import Inquiry from "@/component/Dashboard/Inquiry.vue";
const data2 = [];
</script>
<template>
  <v-container>
    <SampleMenuView />

    <v-row>
      <v-col cols="auto" class="px-1">
        <Info />
        <FacilityStatus />
        <Notice />
      </v-col>
      <v-col cols="auto" class="px-1">
        <ConnettingService />
        <AiDiagnosis />
        <Inquiry />
      </v-col>
    </v-row>
  </v-container>
</template>

<style lang="scss"></style>
