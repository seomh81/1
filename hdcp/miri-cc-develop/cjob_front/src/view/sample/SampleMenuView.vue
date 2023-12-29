<script setup>
import { useRouter } from "vue-router";
import { uiStore } from "@/store/UiStore";
import {
  LayoutContainer,
  GridContainer,
  Button,
  DataField,
  Tabulator,
  ModalContainer,
  Spiner,
} from "@/component/Template";
import { ref } from "vue";
const useUiStore = uiStore();
const router = useRouter();
</script>

<template>
  <v-container>
    <ul class="sample-menu">
      <li>
        <Button @click.prevent="router.push('/dev/sample-grid')">그리드</Button>
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-input')"
          >입력폼</Button
        >
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-button')">버튼</Button>
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-table')"
          >테이블</Button
        >
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-data-table')"
          >데이터테이블</Button
        >
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-modal')">모달</Button>
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-paging')"
          >페이징</Button
        >
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-text')">텍스트</Button>
      </li>
    </ul>
    <ul class="sample-menu">
      <li>
        <Button @click.prevent="router.push('/dev/sample-widget')">위젯</Button>
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-tree')">트리</Button>
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-map')">지도</Button>
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-calendar')"
          >캘린더</Button
        >
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-portfolio')"
          >포트폴리오</Button
        >
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-addressbook')"
          >주소록</Button
        >
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-chart')">차트</Button>
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-dashboard')"
          >lv3 대쉬보드</Button
        >
      </li>
    </ul>
    <ul class="sample-menu">
      <li>
        <Button
          @click.prevent="useUiStore.toggleFont2()"
          :option="{
            append: 'mdi-checkbox-marked-circle',
          }"
          >폰트2</Button
        >
      </li>
      <li>
        <Button @click.prevent="router.push('/dev/sample-example1')"
          >복합1</Button
        >
      </li>
    </ul>
  </v-container>
</template>

<style scoped>
div {
  /* display: flex; */
  margin: 0;
  margin-bottom: 10px;
  padding: 0;
  padding-bottom: 0px;
  border-bottom: 1px solid #bbb;
}
ul {
  display: block;
  list-style-type: none;
  margin: 0;
  margin-bottom: 4px;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0;
  margin-right: 8px;
  margin-bottom: 4px;
  padding: 0;
  position: relative;
}
</style>
<style>
ul.sample-menu .v-btn.button {
  padding: 0 6px;
  min-width: 0;
}
</style>
