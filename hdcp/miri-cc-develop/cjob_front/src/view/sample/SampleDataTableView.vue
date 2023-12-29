<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import { connectionStore } from "@/store/ConnectionStore";
import { Tabulator } from "@/component/Template";

const useConnectionStore = connectionStore();

// const heightCalc = 48 + 82 + 32;
const rowTarget = ref([]);
const columns = [
  {
    formatter: "rowSelection",
    titleFormatter: "rowSelection",
    width: "70",
    headerSort: false,
    hozAlign: "center",
    headerHozAlign: "center",
  },
  {
    title: "OPTION1",
    field: "OPTION1",
    sorter: "string",
  },
  {
    title: "OPTION2",
    field: "OPTION2",
    sorter: "string",
  },
  {
    title: "OPTION3",
    field: "OPTION3",
    sorter: "string",
  },
  {
    title: "OPTION4",
    field: "OPTION4",
    sorter: "string",
  },
];
const data = [
  {
    OPTION1: "lkjelkjf",
    OPTION2: "테스트2",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "1212",
    OPTION2: "432423",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "23",
    OPTION2: "12",
    OPTION3: "brebda",
    OPTION4: "423",
  },
  {
    OPTION1: "23",
    OPTION2: "432",
    OPTION3: "4",
    OPTION4: "5",
  },
  {
    OPTION1: "lkjelkjf",
    OPTION2: "5v25v",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "1212",
    OPTION2: "432423",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "23",
    OPTION2: "5634563",
    OPTION3: "25v25v25",
    OPTION4: "423",
  },
  {
    OPTION1: "23",
    OPTION2: "432",
    OPTION3: "adsf4",
    OPTION4: "5",
  },
  {
    OPTION1: "lkjelkjf",
    OPTION2: "테스트2",
    OPTION3: "y5yj",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "1212",
    OPTION2: "432423",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "23",
    OPTION2: "12",
    OPTION3: "ddf",
    OPTION4: "43567ytre",
  },
  {
    OPTION1: "23",
    OPTION2: "432",
    OPTION3: "4",
    OPTION4: "5",
  },
  {
    OPTION1: "1212",
    OPTION2: "432423",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "23",
    OPTION2: "12",
    OPTION3: "123",
    OPTION4: "423",
  },
  {
    OPTION1: "23",
    OPTION2: "432",
    OPTION3: "4",
    OPTION4: "5",
  },
  {
    OPTION1: "lkjelkjf",
    OPTION2: "테스트2",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "1212",
    OPTION2: "432423",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "23",
    OPTION2: "12",
    OPTION3: "123",
    OPTION4: "423",
  },
  {
    OPTION1: "23",
    OPTION2: "432",
    OPTION3: "4",
    OPTION4: "5",
  },
  {
    OPTION1: "1212",
    OPTION2: "432423",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "23",
    OPTION2: "12",
    OPTION3: "123",
    OPTION4: "423",
  },
  {
    OPTION1: "23",
    OPTION2: "432",
    OPTION3: "4",
    OPTION4: "5",
  },
  {
    OPTION1: "lkjelkjf",
    OPTION2: "테스트2",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "1212",
    OPTION2: "432423",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "23",
    OPTION2: "12",
    OPTION3: "123",
    OPTION4: "423",
  },
  {
    OPTION1: "23",
    OPTION2: "432",
    OPTION3: "4",
    OPTION4: "5",
  },
  {
    OPTION1: "1212",
    OPTION2: "432423",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "23",
    OPTION2: "12",
    OPTION3: "123",
    OPTION4: "423",
  },
  {
    OPTION1: "23",
    OPTION2: "432",
    OPTION3: "4",
    OPTION4: "5",
  },
  {
    OPTION1: "lkjelkjf",
    OPTION2: "테스트2",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "1212",
    OPTION2: "432423",
    OPTION3: "테스트6",
    OPTION4: "테이뷸레이터",
  },
  {
    OPTION1: "23",
    OPTION2: "12",
    OPTION3: "123",
    OPTION4: "423",
  },
  {
    OPTION1: "23",
    OPTION2: "432",
    OPTION3: "4",
    OPTION4: "5",
  },
];
</script>

<template>
  <v-container class="sample-test-container">
    <SampleMenuView />
    <Tabulator
      :columns="columns"
      :tableName="'table'"
      :tableData="data"
      :height="700"
      @update:rowTarget="(returnVal) => (rowTarget = returnVal)"
    />
  </v-container>
</template>

<style scoped></style>
