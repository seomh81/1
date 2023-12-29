<script setup>
import { useRouter, useRoute } from "vue-router";
const router = useRouter();
</script>
<template>
  <v-list bg-color="transparent" class="menuCard">
    <v-list-item class="py-5 px-4">
      <v-avatar rounded="lg" class="mr-3">
        <v-img
          src="https://cdn.vuetifyjs.com/images/john.jpg"
          alt="John"
        ></v-img>
      </v-avatar>

      <!-- <v-icon size="x-large">mdi-cog-outline</v-icon> -->
      ahsxkx@daialab.co.kr
    </v-list-item>
    <v-divider class="pt-5"></v-divider>
    <v-list-item class="menuBox" @click="router.push('/setting/info')">
      <v-icon class="menuBoxIcon">mdi-account-outline</v-icon>
      <span>내 정보</span>
    </v-list-item>
    <v-list-item class="menuBox">
      <v-icon class="menuBoxIcon">mdi-lock-reset</v-icon>
      <span>비밀번호 변경</span>
    </v-list-item>
    <v-list-item class="menuBox" @click="router.push('/setting/portfolio')">
      <v-icon class="menuBoxIcon">mdi-folder-information-outline</v-icon>
      <span>포트폴리오 관리</span>
    </v-list-item>
    <v-list-item class="menuBox">
      <v-icon class="menuBoxIcon">mdi-bell-outline</v-icon>
      <span>서비스 환경설정</span>
    </v-list-item>
  </v-list>
</template>
<style lang="scss">
.menuCard {
//   background-color: rgb(250, 250, 250);

  .menuBox {
    display: flex;
    align-items: center;
    margin: 0px 4px;
    opacity: 0.8;
    border-radius: 10px;
    &:hover {
      transition: 0.3s ease;
      transform: scale(1.03);
      opacity: 1;
      color: white;
      background-color: rgba(68, 35, 68, 0.7);
      cursor: pointer;
    }
    .menuBoxIcon {
      margin-right: 16px;
    }
  }
}
</style>
