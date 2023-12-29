<script setup>
import { UserSettingMenu } from "@/component/Template.js";
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
          class="settingContentCard contentCard rounded-e-xl"
          border="none"
          elevation="0"
        >
          <v-toolbar-title>내 정보</v-toolbar-title>
          <v-list class="pt-5 px-10" bg-color="transparent">
            <v-list-item class="listItemPadding">
              <v-list-item-subtitle>이름</v-list-item-subtitle>
              <v-list-item-title>홍길동</v-list-item-title>
            </v-list-item>
            <v-list-item class="listItemPadding">
              <v-list-item-subtitle>아이디</v-list-item-subtitle>
              <v-list-item-title>test@gmail.com</v-list-item-title>
            </v-list-item>
            <v-list-item class="listItemPadding">
              <v-list-item-subtitle>전화번호</v-list-item-subtitle>
              <v-list-item-title>010-2234-6547</v-list-item-title>
            </v-list-item>
            <v-list-item class="listItemPadding">
              <v-list-item-subtitle>주소</v-list-item-subtitle>
              <v-list-item-action class="pt-3">
                <v-text-field
                  readonly
                  variant="outlined"
                  label="기본 주소"
                ></v-text-field>
                <v-btn class="ml-1" variant="outlined">검색</v-btn>
              </v-list-item-action>
              <v-list-item-action class="pt-1">
                <v-text-field
                  readonly
                  variant="outlined"
                  label="상세 주소"
                ></v-text-field>
              </v-list-item-action>
            </v-list-item>
            <v-list-item class="mt-5">
              <template v-slot:append>
                <v-btn variant="outlined" color="primary">저장</v-btn>
              </template>
            </v-list-item>
          </v-list>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
<style scoped lang="scss">
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
    padding: 20px 30px;
    height: calc(100vh - 94px);
    .listItemPadding {
      padding: 8px 0;
    }
  }
}
</style>
