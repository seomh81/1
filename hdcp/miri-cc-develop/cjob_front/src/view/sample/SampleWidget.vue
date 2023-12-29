<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import { Table, Thead, Tbody, Tr, Th, Td } from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
const useConnectionStore = connectionStore();

const page = 2;
</script>

<template>
  <v-container class="sample-test-container">
    <SampleMenuView />
    <h2>위젯</h2>
  </v-container>
</template>

<style>
/* .v-pagination {}
.v-pagination .v-pagination__list {}
.v-pagination .v-pagination__item {} */
.v-pagination .v-pagination__item .v-btn--icon.v-btn--density-default {
  border: 1px solid #333;
}
.v-pagination .v-btn--icon.v-btn--density-default {
  min-width: 26px;
  width: auto;
  height: 24px;
}
.v-pagination .v-pagination__item .v-btn__content {
  font-family: 'Consolas';
  letter-spacing: -1px;
  padding-right: 2px;
}
</style>
<style scoped>
h2 {
  display: block;
  margin: 0;
  padding: 3px 0;
  font-size: 1rem;
  line-height: 1.2em;
}
</style>
