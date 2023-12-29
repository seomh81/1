<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "@/view/sample/SampleMenuView.vue";
import { GridContainer, Button, Chip } from "@/component/Template";
import axios from "@/plugin/axios";
import { connectionStore } from "@/store/ConnectionStore";
import { uiStore } from "@/store/UiStore";
const useUiStore = uiStore();
const useConnectionStore = connectionStore();
const popupAdApi = reactive({
  get: {
    url: "/v1/ad/listpopupad",
    method: "get",
    queryparam: {
      status: 0,
    },
  },
  add: {
    url: "/v1/ad/addpopupad",
    method: "post",
    queryparam: {
      popupNm: "버튼2 애드 api 테스트",
      contents: "<p>hello</p>",
    },
  },
  update: {
    url: "/v1/ad/updatepopupad",
    method: "post",
    queryparam: {
      popupAdId: 1,

      popupNm: "업데이트 버튼2 테스트",
      contents: "<p>hi</p>",
    },
  },
  remove: {
    url: "/v1/ad/removepopupad",
    method: "post",
    queryparam: [
      {
        popupAdId: 2,
      },
    ],
  },
});

const sendRequest = async (params, i) => {
  // const data = await useConnectionStore.request(params);
  console.log(params);
};

const getPopupEvent = async () => {
  await sendRequest(popupAdApi["get"]);
};

const addPopupEvent = async () => {
  await sendRequest(popupAdApi["add"]);
};

const updatePopupEvent = async () => {
  await sendRequest(popupAdApi["update"]);
};

const removePopupEvent = async () => {
  await sendRequest(popupAdApi["remove"]);
};

const handleChipClose = (e) => {
  console.log(e);
}

const handleChipClick = (e) => {
  console.log(e);
}

const buttonClick = (e) => {
  console.log("hi");
  console.log(e);
};
</script>

<template>
  <v-container class="sample-test-container">
    <SampleMenuView />

    <GridContainer colType="inline-right" rowSpacing>
      <v-btn
        :color="useUiStore.testBorder ? 'indigo' : 'primary'"
        @click.prevent="useUiStore.toggleTestBorder()"
      >테두리</v-btn>
    </GridContainer>

    <h3 class="sample-title">Chip</h3>
    <GridContainer colType="inline" rowSpacing>
      <Chip
        @click="handleChipClick"
        variant="outlined"
      >
        User
      </Chip>
      <Chip
        closable
        @click:close="handleChipClose"
        variant="outlined"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        variant="outlined"
        prepend-icon="mdi-account-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        variant="outlined"
        append-icon="mdi-star"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        variant="outlined"
        append-icon="mdi-checkbox-marked-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        variant="outlined"
        append-icon="mdi-cancel"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        variant="outlined"
        append-icon="mdi-minus-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        variant="outlined"
        prepend-icon="mdi-arrow-left"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        variant="outlined"
        append-icon="mdi-arrow-right"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        variant="outlined"
        append-icon="mdi-cloud-upload"
      >
        User
      </Chip>
    </GridContainer>
    <GridContainer colType="inline" rowSpacing>
      <Chip
        @click="handleChipClick"
      >
        User
      </Chip>
      <Chip
        closable
        @click:close="handleChipClose"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        prepend-icon="mdi-account-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        append-icon="mdi-star"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        append-icon="mdi-checkbox-marked-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        append-icon="mdi-cancel"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        append-icon="mdi-minus-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        prepend-icon="mdi-arrow-left"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        append-icon="mdi-arrow-right"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        append-icon="mdi-cloud-upload"
      >
        User
      </Chip>
    </GridContainer>
    <GridContainer colType="inline" rowSpacing>
      <Chip
        @click="handleChipClick"
        color="red"
      >
        User
      </Chip>
      <Chip
        closable
        color="red"
        @click:close="handleChipClose"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="red"
        prepend-icon="mdi-account-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="red"
        append-icon="mdi-star"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="red"
        append-icon="mdi-checkbox-marked-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="red"
        append-icon="mdi-cancel"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="red"
        append-icon="mdi-minus-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="red"
        prepend-icon="mdi-arrow-left"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="red"
        append-icon="mdi-arrow-right"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="red"
        append-icon="mdi-cloud-upload"
      >
        User
      </Chip>
    </GridContainer>
    <GridContainer colType="inline" rowSpacing>
      <Chip
        @click="handleChipClick"
        color="orange-darken-2"
      >
        User
      </Chip>
      <Chip
        closable
        color="orange-darken-2"
        @click:close="handleChipClose"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange-darken-2"
        prepend-icon="mdi-account-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange-darken-2"
        append-icon="mdi-star"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange-darken-2"
        append-icon="mdi-checkbox-marked-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange-darken-2"
        append-icon="mdi-cancel"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange-darken-2"
        append-icon="mdi-minus-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange-darken-2"
        prepend-icon="mdi-arrow-left"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange-darken-2"
        append-icon="mdi-arrow-right"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange-darken-2"
        append-icon="mdi-cloud-upload"
      >
        User
      </Chip>
    </GridContainer>
    <GridContainer colType="inline" rowSpacing>
      <Chip
        @click="handleChipClick"
        color="orange"
      >
        User
      </Chip>
      <Chip
        closable
        color="orange"
        @click:close="handleChipClose"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange"
        prepend-icon="mdi-account-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange"
        append-icon="mdi-star"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange"
        append-icon="mdi-checkbox-marked-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange"
        append-icon="mdi-cancel"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange"
        append-icon="mdi-minus-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange"
        prepend-icon="mdi-arrow-left"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange"
        append-icon="mdi-arrow-right"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="orange"
        append-icon="mdi-cloud-upload"
      >
        User
      </Chip>
    </GridContainer>
    <GridContainer colType="inline" rowSpacing>
      <Chip
        @click="handleChipClick"
        color="yellow-darken-2"
      >
        User
      </Chip>
      <Chip
        closable
        color="yellow-darken-2"
        @click:close="handleChipClose"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="yellow-darken-2"
        prepend-icon="mdi-account-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="yellow-darken-2"
        append-icon="mdi-star"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="yellow-darken-2"
        append-icon="mdi-checkbox-marked-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="yellow-darken-2"
        append-icon="mdi-cancel"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="yellow-darken-2"
        append-icon="mdi-minus-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="yellow-darken-2"
        prepend-icon="mdi-arrow-left"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="yellow-darken-2"
        append-icon="mdi-arrow-right"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="yellow-darken-2"
        append-icon="mdi-cloud-upload"
      >
        User
      </Chip>
    </GridContainer>
    <GridContainer colType="inline" rowSpacing>
      <Chip
        @click="handleChipClick"
        color="green"
      >
        User
      </Chip>
      <Chip
        closable
        color="green"
        @click:close="handleChipClose"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="green"
        prepend-icon="mdi-account-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="green"
        append-icon="mdi-star"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="green"
        append-icon="mdi-checkbox-marked-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="green"
        append-icon="mdi-cancel"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="green"
        append-icon="mdi-minus-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="green"
        prepend-icon="mdi-arrow-left"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="green"
        append-icon="mdi-arrow-right"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="green"
        append-icon="mdi-cloud-upload"
      >
        User
      </Chip>
    </GridContainer>
    <GridContainer colType="inline" rowSpacing>
      <Chip
        @click="handleChipClick"
        color="blue"
      >
        User
      </Chip>
      <Chip
        closable
        color="blue"
        @click:close="handleChipClose"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="blue"
        prepend-icon="mdi-account-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="blue"
        append-icon="mdi-star"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="blue"
        append-icon="mdi-checkbox-marked-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="blue"
        append-icon="mdi-cancel"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="blue"
        append-icon="mdi-minus-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="blue"
        prepend-icon="mdi-arrow-left"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="blue"
        append-icon="mdi-arrow-right"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="blue"
        append-icon="mdi-cloud-upload"
      >
        User
      </Chip>
    </GridContainer>
    <GridContainer colType="inline" rowSpacing>
      <Chip
        @click="handleChipClick"
        color="purple"
      >
        User
      </Chip>
      <Chip
        closable
        color="purple"
        @click:close="handleChipClose"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="purple"
        prepend-icon="mdi-account-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="purple"
        append-icon="mdi-star"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="purple"
        append-icon="mdi-checkbox-marked-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="purple"
        append-icon="mdi-cancel"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="purple"
        append-icon="mdi-minus-circle"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="purple"
        prepend-icon="mdi-arrow-left"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="purple"
        append-icon="mdi-arrow-right"
      >
        User
      </Chip>
      <Chip
        @click="handleChipClick"
        color="purple"
        append-icon="mdi-cloud-upload"
      >
        User
      </Chip>
    </GridContainer>
    
    


    
    <h3 class="sample-title">사이즈</h3>
    <GridContainer colType="block" rowSpacing>
      <Button
        @update:click="getPopupEvent"
        :option="{
          prepend: 'mdi-playlist-plus',
        }"
        widthFull
      >승인</Button>
    </GridContainer>
    <GridContainer colType="block" rowSpacing>
      <Button
        @update:click="getPopupEvent"
        :option="{
          prepend: 'mdi-playlist-plus',
          width: '400px',
        }"
      >승인</Button>
    </GridContainer>
    <GridContainer colType="inline" rowSpacing>
      <Button
        @update:click="getPopupEvent"
        :option="{
          prepend: 'mdi-playlist-plus',
          width: '250px',
        }"
      >승인</Button>
      <Button
        @update:click="getPopupEvent"
        :option="{
          prepend: 'mdi-playlist-plus',
          width: '400px',
        }"
      >승인</Button>
    </GridContainer>

    <h3 class="sample-title">아이콘</h3>
    <GridContainer colType="inline">
      <Button
        @update:click="getPopupEvent"
        :option="{
          prepend: 'mdi-playlist-plus'
        }"
      >승인</Button>
      <Button
        @update:click="addPopupEvent"
        :option="{
          append: 'mdi-filter-outline',
        }"
      >추가</Button>
      <Button
        @update:click="updatePopupEvent"
        :option="{
          prepend: 'mdi-playlist-edit'
        }"
      >수정</Button>
      <Button
        @update:click="removePopupEvent"
        :option="{
          prepend: 'mdi-playlist-remove'
        }"
      >삭제</Button>
    </GridContainer>

    <h3 class="sample-title">우측정렬</h3>
    <GridContainer colType="inline-right">
      <Button @update:click="getPopupEvent">승인</Button>
      <Button @update:click="addPopupEvent">추가</Button>
      <Button @update:click="removePopupEvent">삭제</Button>
    </GridContainer>

    <h3 class="sample-title">중앙정렬</h3>
    <GridContainer colType="inline-center">
      <Button @update:click="getPopupEvent">승인</Button>
      <Button @update:click="addPopupEvent">추가</Button>
      <Button @update:click="updatePopupEvent">수정</Button>
    </GridContainer>

    <h3 class="sample-title">좌측정렬</h3>
    <GridContainer colType="inline">
      <Button @update:click="getPopupEvent">승인</Button>
      <Button @update:click="addPopupEvent">추가</Button>
      <Button @update:click="updatePopupEvent">수정</Button>
    </GridContainer>

    
    
  </v-container>
</template>

<style scoped></style>
