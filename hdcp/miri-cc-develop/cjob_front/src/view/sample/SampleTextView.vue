<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import { TextBox, GridContainer, Table, Thead, Tbody, Tr, Th, Td } from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
const useConnectionStore = connectionStore();

const page = 2;
</script>

<template>
  <v-container class="sample-test-container">    
    <SampleMenuView />

    <h3 class="sample-title">ultra big</h3>
    <GridContainer colType="inline">
      <TextBox type="ultra-big">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>
    
    <h3 class="sample-title">big</h3>
    <GridContainer colType="inline">
      <TextBox type="big" prepend-icon="mdi-account-circle">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>

    <h3 class="sample-title">medium</h3>
    <GridContainer colType="inline">
      <TextBox type="medium">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>
    
    <h3 class="sample-title">normal</h3>
    <GridContainer colType="inline">
      <TextBox type="normal">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>
    
    <h3 class="sample-title">small</h3>
    <GridContainer colType="inline">
      <TextBox type="small">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>
    
    <h3 class="sample-title">ultra-big + warning</h3>
    <GridContainer colType="inline">
      <TextBox type="ultra-big warning">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>
    
    <h3 class="sample-title">big + warning</h3>
    <GridContainer colType="inline">
      <TextBox type="big warning" prepend-icon="mdi-account-circle">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>
    
    <h3 class="sample-title">medium + warning</h3>
    <GridContainer colType="inline">
      <TextBox type="medium warning">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>
    
    <h3 class="sample-title">normal + warning</h3>
    <GridContainer colType="inline">
      <TextBox type="normal warning">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>
    
    <h3 class="sample-title">small + warning</h3>
    <GridContainer colType="inline">
      <TextBox type="small warning">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>


    <h3 class="sample-title">ultra-big + bold</h3>
    <GridContainer colType="inline">
      <TextBox type="ultra-big bold">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>

    <h3 class="sample-title">big + bold</h3>
    <GridContainer colType="inline">
      <TextBox type="big bold" prepend-icon="mdi-account-circle">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>

    <h3 class="sample-title">medium + bold</h3>
    <GridContainer colType="inline">
      <TextBox type="medium bold">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>

    <h3 class="sample-title">normal + bold</h3>
    <GridContainer colType="inline">
      <TextBox type="normal bold">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>

    <h3 class="sample-title">small + bold</h3>
    <GridContainer colType="inline">
      <TextBox type="small bold">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
    </GridContainer>
    

    <h3 class="sample-title">ultra-big</h3>
    <GridContainer colType="block">
      <TextBox type="ultra-big" prepend-icon="mdi-account-circle">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
      <TextBox type="ultra-big">거북아, 나무에서 내려와야 같이 간을 찾으러 갈거 아니냐</TextBox>
      <TextBox type="ultra-big">용왕님이 애타게 기다린다고 하지 않았냐?</TextBox>
    </GridContainer>

    <h3 class="sample-title">big</h3>
    <GridContainer colType="block">
      <TextBox type="big" prepend-icon="mdi-account-circle">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
      <TextBox type="big">거북아, 나무에서 내려와야 같이 간을 찾으러 갈거 아니냐</TextBox>
      <TextBox type="big">용왕님이 애타게 기다린다고 하지 않았냐?</TextBox>
    </GridContainer>

    <h3 class="sample-title">medium</h3>
    <GridContainer colType="block">
      <TextBox type="medium" prepend-icon="mdi-account-circle">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
      <TextBox type="medium">거북아, 나무에서 내려와야 같이 간을 찾으러 갈거 아니냐</TextBox>
      <TextBox type="medium">용왕님이 애타게 기다린다고 하지 않았냐?</TextBox>
    </GridContainer>

    <h3 class="sample-title">normal</h3>
    <GridContainer colType="block">
      <TextBox type="normal" prepend-icon="mdi-account-circle">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
      <TextBox type="normal">거북아, 나무에서 내려와야 같이 간을 찾으러 갈거 아니냐</TextBox>
      <TextBox type="normal">용왕님이 애타게 기다린다고 하지 않았냐?</TextBox>
    </GridContainer>

    <h3 class="sample-title">small</h3>
    <GridContainer colType="block">
      <TextBox type="small" prepend-icon="mdi-account-circle">거북이가 나무에 올라가니 토끼가 <strong>어이없어</strong> 바라만 보더라</TextBox>
      <TextBox type="small">거북아, 나무에서 내려와야 같이 간을 찾으러 갈거 아니냐</TextBox>
      <TextBox type="small">용왕님이 애타게 기다린다고 하지 않았냐?</TextBox>
    </GridContainer>
    
    <br />
    <br />
    <br />
    <br />
    <br />

  </v-container>
</template>

<style>
/* .v-pagination {}
.v-pagination .v-pagination__list {}
.v-pagination .v-pagination__item {} */
.v-pagination .v-pagination__item .v-btn--icon.v-btn--density-default {
  border: 1px solid #999;
  line-height: 1.1em;
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
