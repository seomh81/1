<script setup>
import { UserSettingMenu } from "@/component/Template.js";
import { ref, watch, onMounted } from "vue";

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
const contractListBox = ref(null);
const changeArrow = ref(false);
const createContractList = () => {
  contractList.value = [];
  changeArrow.value = false;
  let a = Math.floor(Math.random() * 15);
  if (a == 0) a++;
  for (let i = 0; i < a; i++) {
    contractList.value.push({
      id: i++,
      contractNumber: `${`SM-${Math.floor(Math.random() * 5000)}-${Math.floor(
        Math.random() * 1000
      )}`}`,
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

onMounted(() => {
  console.log(contractListBox);
  contractListBox.value.$el.addEventListener("scroll", (e) => {
    if (e.target.scrollHeight - e.target.clientHeight == e.target.scrollTop) {
      changeArrow.value = true;
    } else {
      changeArrow.value = false;
    }
  });
});
</script>
<template>
  <v-container class="settingMenu" fluid>
    <v-row justify="center">
      <v-col cols="2" class="pr-0">
        <v-card class="settingMenuCard menuCard rounded-s-xl" border="none">
          <UserSettingMenu></UserSettingMenu>
        </v-card>
      </v-col>
      <v-col cols="7" class="pl-0">
        <v-card
          class="settingContentCard rounded-e-xl"
          border="none"
          elevation="0"
        >
          <v-toolbar-title>포트폴리오 관리</v-toolbar-title>
          <v-container height="100%" class="px-10 pt-5">
            <v-row>
              <v-col cols="4">
                <v-toolbar density="compact" color="transparent">
                  <v-toolbar-title>포트폴리오</v-toolbar-title>
                  <v-spacer></v-spacer>
                  <v-btn><v-icon>mdi-plus</v-icon></v-btn>
                </v-toolbar>
                <v-item-group v-model="selectedPortfolio">
                  <v-list bg-color="transparent">
                    <v-item
                      v-slot="{ toggle }"
                      v-for="(item, index) in portfolioList"
                      :key="item"
                    >
                      <v-list-item
                        :value="item"
                        rounded="xl"
                        color="primary"
                        class="text-center my-1"
                        @click="toggle"
                      >
                        <v-list-item-title>{{ item.name }}</v-list-item-title>
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

                                  <v-list-item-title
                                    >기본 설정</v-list-item-title
                                  >
                                </v-list-item>
                                <v-list-item
                                  rounded="lg"
                                  value="expand"
                                  active-color="primary"
                                >
                                  <template v-slot:prepend>
                                    <v-icon icon="mdi-arrow-expand"></v-icon>
                                  </template>

                                  <v-list-item-title
                                    >이름 변경</v-list-item-title
                                  >
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
              <v-col cols="8">
                <v-toolbar density="compact" color="transparent">
                  <v-toolbar-title>상세 계약 정보</v-toolbar-title>
                  <v-spacer></v-spacer>
                  <v-btn><v-icon>mdi-plus</v-icon></v-btn>
                  <v-btn @click="removeContract"
                    ><v-icon>mdi-minus</v-icon></v-btn
                  >
                </v-toolbar>
                <v-sheet
                  v-show="contractList.length > 0"
                  height="75.3vh"
                  color="transparent"
                  class="contractListBox"
                  ref="contractListBox"
                >
                  <v-item-group v-model="selctedContract" multiple>
                    <v-list select-strategy="multiple" bg-color="transparent">
                      <v-item
                        v-slot="{ toggle }"
                        v-for="(item, i) in contractList"
                        :key="i"
                      >
                        <v-list-item
                          active-color="primary"
                          rounded="lg"
                          :value="item"
                          class="py-4 pr-10"
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
                              <v-toolbar-title class="text-button"
                                >충북 성주 아이커파크</v-toolbar-title
                              >
                            </v-toolbar>
                            <v-container>
                              <v-row>
                                <v-col cols="6">
                                  계약번호 {{ item.contractNumber }}
                                </v-col>
                                <v-col cols="6">
                                  고객명 {{ item.clientName }}
                                </v-col>
                                <v-col cols="6">
                                  통합 프로젝트 번호
                                  {{ item.integrationProjectNumber }}
                                </v-col>
                                <v-col cols="6"
                                  >계약기간 {{ item.startContract }} ~
                                  {{ item.endContract }}</v-col
                                >
                                <v-col cols="6"
                                  >프로젝트 번호 {{ item.projectNumber }}</v-col
                                >
                                <v-col cols="6"
                                  >상풍명 {{ item.product }}</v-col
                                >
                              </v-row>
                            </v-container>
                          </v-card>
                        </v-list-item>
                      </v-item>
                    </v-list>
                  </v-item-group>
                  <div
                    v-if="contractList.length > 3"
                    class="contractScroll"
                    :class="{
                      scrollDownAnime: changeArrow == false,
                      scrollUpAnime: changeArrow == true,
                    }"
                  >
                    <i
                      v-if="changeArrow == false"
                      class="fa-solid fa-caret-down"
                      :style="{ fontSize: '40px', opacity: '0.7' }"
                    ></i>
                    <i
                      v-if="changeArrow == true"
                      class="fa-solid fa-caret-up"
                      :style="{ fontSize: '40px', opacity: '0.7' }"
                    ></i>
                  </div>
                </v-sheet>
                <v-sheet
                  color="transparent"
                  height="60vh"
                  class="d-flex justify-center align-center"
                  v-if="contractList.length == 0"
                >
                  <p class="text-button">포트폴리오를 선택해주세요</p>
                </v-sheet>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
<style lang="scss">
.settingMenu {
  background-color: rgb(230, 230, 230);
  .settingMenuCard {
    z-index: 2;
    height: calc(100vh - 94px);
    box-shadow: rgba(0, 0, 0, 0.15) 3px 0px 10px -3px;
    padding: 0 20px;
    /* box-shadow: rgba(0, 0, 0, 0.15) 2.4px  2.4px 3.2px; */
  }
  .settingContentCard {
    height: calc(100vh - 94px);

    padding: 20px 30px;
    .contractListBox {
      position: relative;
      overflow: auto;

      padding: 0 10px;
      .contractScroll {
        position: fixed;
        bottom: 2.5%;
        left: 66.5%;
      }
      .scrollDownAnime {
        animation: down 1.5s infinite;
      }
      .scrollUpAnime {
        animation: up 1.5s infinite;
      }
      &::-webkit-scrollbar {
        width: 10px;
      }
      &::-webkit-scrollbar * {
        background: transparent;
      }
      &::-webkit-scrollbar-thumb {
        background: rgba(65, 65, 65, 0.5) !important;
        border-radius: 10px;
      }
    }
  }
}

@keyframes down {
  0% {
    transform: translateY(0);
  }
  20% {
    transform: translateY(7px);
  }
  40% {
    transform: translateY(0);
  }
}
@keyframes up {
  0% {
    transform: translateY(0px);
  }
  20% {
    transform: translateY(-7px);
  }
  40% {
    transform: translateY(0px);
  }
}
</style>
