<script setup>
import SampleMenuView from "@/view/sample/SampleMenuView.vue";
import { ref, watch } from "vue";

const portfolioList = ref([
  {
    name: "디폴트 포트폴리오",
    id: 1,
  },
  {
    name: "강남구",
    id: 2,
  },
  {
    name: "송파구",
    id: 3,
  },
  {
    name: "강북구",
    id: 4,
  },
]);
const contractList = ref([]);
const selectedPortfolio = ref([]);
const createContractList = () => {
  contractList.value = [];
  let a = Math.floor(Math.random() * 7);
  if (a == 0) a++;
  for (let i = 0; i < a; i++) {
    contractList.value.push({
      id: i++,
      contractNumber: `${`SM-${Math.floor(Math.random() * 5000)} -
        ${Math.floor(Math.random() * 1000)}`}`,
      clientName: `홍길동${Math.floor(Math.random() * 99)}`,
      integrationProjectNumber: Math.floor(Math.random() * 99999999),
      startContract: `2011-${Math.floor(Math.random() * 12)}-${Math.floor(
        Math.random() * 31
      )}`,
      endContract: `2022-${Math.floor(Math.random() * 12)}-${Math.floor(
        Math.random() * 31
      )}`,
      projectNumber: Math.floor(Math.random() * 31),
      product: "HRTS",
    });
  }
};
watch(selectedPortfolio, () => {
  if (selectedPortfolio.value == undefined) {
    contractList.value = [];
  } else {
    selctedContract.value = [];
    createContractList();
  }
});
const selctedContract = ref([]);
const removePortfolio = (index) => {
  portfolioList.value.splice(index, 1);
  selectedPortfolio.value = [];
};
const removeContract = () => {
  contractList.value = contractList.value.filter((item, index) => {
    return !selctedContract.value.some((item2) => {
      return index === item2;
    });
  });
  selctedContract.value = [];
};
</script>
<template>
  <v-container class="sample-test-container">
    <SampleMenuView />
    <v-card>
      <v-container>
        <v-row>
          <v-col cols="4">
            <v-toolbar density="compact" color="white">
              <v-toolbar-title>포트폴리오</v-toolbar-title>
              <v-spacer></v-spacer>
              <v-btn><v-icon>mdi-plus</v-icon></v-btn>
            </v-toolbar>
            <v-item-group v-model="selectedPortfolio">
              <v-list>
                <v-item
                  v-slot="{ toggle }"
                  v-for="(item, index) in portfolioList"
                  :key="item"
                >
                  <v-list-item
                    :value="item"
                    rounded="xl"
                    color="red"
                    class="text-center my-1"
                    @click="toggle"
                  >
                    <p>{{ item.name }}</p>
                    <template v-slot:append>
                      <v-menu location="end">
                        <template v-slot:activator="{ props }">
                          <v-btn
                            variant="plain"
                            icon="mdi-dots-vertical"
                            v-bind="props"
                          ></v-btn>
                        </template>
                        <v-card class="pa-2">
                          <v-list>
                            <v-list-subheader>ACTIONS</v-list-subheader>

                            <v-list-item
                              rounded="lg"
                              value="reset"
                              active-color="primary"
                            >
                              <template v-slot:prepend>
                                <v-icon icon="mdi-refresh"></v-icon>
                              </template>

                              <v-list-item-title>기본 설정</v-list-item-title>
                            </v-list-item>
                            <v-list-item
                              rounded="lg"
                              value="expand"
                              active-color="primary"
                            >
                              <template v-slot:prepend>
                                <v-icon icon="mdi-arrow-expand"></v-icon>
                              </template>

                              <v-list-item-title>이름 변경</v-list-item-title>
                            </v-list-item>
                            <v-list-item
                              value="remove"
                              active-color="primary"
                              rounded="lg"
                              @click="removePortfolio(index)"
                            >
                              <template v-slot:prepend>
                                <v-icon
                                  icon="mdi-minus-circle-outline"
                                  color="red"
                                ></v-icon>
                              </template>

                              <v-list-item-title>삭제</v-list-item-title>
                            </v-list-item>
                          </v-list>
                        </v-card>
                      </v-menu>
                    </template>
                  </v-list-item>
                </v-item>
              </v-list>
            </v-item-group>
          </v-col>
          <v-col cols="8" height="70vh">
            <v-toolbar density="compact" color="white">
              <v-toolbar-title>상세 계약 정보</v-toolbar-title>
              <v-spacer></v-spacer>
              <v-btn><v-icon>mdi-plus</v-icon></v-btn>
              <v-btn @click="removeContract"><v-icon>mdi-minus</v-icon></v-btn>
            </v-toolbar>
            <v-sheet
              v-if="contractList.length > 0"
              height="60vh"
              :style="{ overflow: 'auto' }"
            >
              <v-item-group v-model="selctedContract" multiple>
                <v-list select-strategy="multiple">
                  <v-item
                    v-slot="{ toggle }"
                    v-for="(item, i) in contractList"
                    :key="i"
                  >
                    <v-list-item
                      rounded="lg"
                      :value="item"
                      class="py-4"
                      @click="toggle"
                    >
                      <template v-slot:prepend="{ isActive }">
                        <v-list-item-action start>
                          <v-checkbox-btn
                            :model-value="isActive"
                          ></v-checkbox-btn>
                        </v-list-item-action>
                      </template>

                      <v-card rounded="lg">
                        <v-toolbar density="compact">
                          <v-toolbar-title
                            >충북 성주 아이커파크</v-toolbar-title
                          >
                        </v-toolbar>
                        <v-container>
                          <v-row>
                            <v-col cols="6"
                              >계약번호 {{ item.contractNumber }}</v-col
                            >
                            <v-col cols="6">고객명 {{ item.clientName }}</v-col>
                            <v-col cols="6"
                              >통합 프로젝트 번호
                              {{ item.integrationProjectNumber }}</v-col
                            >
                            <v-col cols="6"
                              >계약기간 {{ item.startContract }} ~
                              {{ item.endContract }}</v-col
                            >
                            <v-col cols="6"
                              >프로젝트 번호 {{ item.projectNumber }}</v-col
                            >
                            <v-col cols="6">상풍명 {{ item.product }}</v-col>
                          </v-row>
                        </v-container>
                      </v-card>
                    </v-list-item>
                  </v-item>
                </v-list>
              </v-item-group>
            </v-sheet>
            <v-sheet
              height="60vh"
              class="d-flex justify-center align-center"
              v-if="contractList.length == 0"
            >
              <p>포트폴리오를 선택해주세요</p>
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-container>
</template>
