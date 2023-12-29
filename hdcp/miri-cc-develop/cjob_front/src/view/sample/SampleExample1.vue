<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import { GridGroup, GridContainer, Button, DataField, TextBox } from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
const useConnectionStore = connectionStore();

const data = reactive({
  sliderVal: 50
});
</script>

<template>
  <v-container class="sample-test-container">    
    <SampleMenuView />
    
    <br />
    <br />

    <GridGroup width="500px" borderBetween>
      <GridContainer colType="grid-stretch-first">
        <TextBox
          type="normal"
          prepend-icon="mdi-web"
        >해외 로그인시도 차단</TextBox>
        <DataField
          :option="{
            type: 'switch',
          }"
        />
      </GridContainer>
      <GridContainer colType="grid-stretch-first">
        <TextBox type="normal">로그인알람제외목록</TextBox>
        <Button>목록</Button>
      </GridContainer>
      <GridContainer colType="grid-stretch-first">
        <TextBox type="normal" append-icon="mdi-checkbox-marked-circle">해외 차단 횟수</TextBox>
        <DataField
          :option="{
            type: 'slider',
            label: '갯수',
            width: 300,
            sliderVal: data.sliderVal,
          }"
          @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
        />
      </GridContainer>
      <GridContainer colType="grid-stretch-first">
        <TextBox type="normal">프라이버시 보호</TextBox>
        <DataField
          :option="{
            type: 'switch',
          }"
        />
      </GridContainer>
      <GridContainer colType="inline-center">
        <DataField
          :option="{
            type: 'select',
            label: '번호',
            width: '100%',
            fieldItems: [
              'California',
              'Colorado',
              'Florida',
              'Georgia',
              'Texas',
              'Wyoming',
            ],
          }"
        />
        <Button>승인</Button>
        <DataField
          :option="{
            type: 'input',
            label: '번호',
            width: '100%',
          }"
        />
        <Button>승인</Button>
      </GridContainer>
    </GridGroup>

    <br />
    <br />

    <GridGroup width="500px">
      <GridContainer colType="grid-stretch-first">
        <TextBox type="normal">해외 로그인시도 차단</TextBox>
        <DataField
          :option="{
            type: 'switch',
          }"
        />
      </GridContainer>
    </GridGroup>

    <br />
    <br />

    <GridGroup width="500px" borderBetween>
      <GridContainer colType="grid-stretch-first">
        <TextBox type="normal">해외 로그인시도 차단</TextBox>
        <DataField
          :option="{
            type: 'switch',
          }"
        />
      </GridContainer>
      <GridContainer colType="grid-stretch-first">
        <TextBox type="normal">해외 차단 횟수</TextBox>
        <DataField
          :option="{
            type: 'slider',
            label: '갯수',
            width: 300,
            sliderVal: data.sliderVal,
          }"
          @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
        />
      </GridContainer>
    </GridGroup>

    <br />
    <br />

  </v-container>
</template>

<style>

</style>
<style scoped>
</style>
