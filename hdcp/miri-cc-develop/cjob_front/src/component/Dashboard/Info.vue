<script setup>
import { ref } from "vue";

const percentage = ref(28);
const maxPercentage = ref(30);
</script>
<template>
  <v-card width="360" class="text-center" elevation="0" rounded="lg">
    <v-container class="py-6">
      <v-row>
        <v-col cols="12">
          <v-avatar
            color="grey-darken-3"
            image="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
          ></v-avatar>
        </v-col>
        <v-col class="largeBoldText" cols="12">
          <p>반갑습니다. 홍길동 고객님</p>
          <p :style="{ color: 'green' }">현대엘리베이터와 1,004일째</p>
          <p>동행하고 계십니다.</p>
        </v-col>
        <v-col>
          <v-progress-linear
            max="30"
            v-model="percentage"
            height="25"
            color="#00C44F"
            rounded="xl"
          >
            <div class="d-flex align-center justify-space-between w-100 px-3">
              <p class="progressText">{{ `안전운행 (${percentage}EA)` }}</p>
              <p class="progressText">
                {{ `(${maxPercentage - percentage}EA) 장애` }}
              </p>
            </div>
          </v-progress-linear>
        </v-col>
        <v-col cols="12">
          <div class="d-flex align-center justify-space-around ">
            <p class="text-disabled">
              고장 갯수
              <v-icon class="pl-2">mdi-help-circle-outline</v-icon>
            </p>
            <p class="text-h5 font-weight-bold">
              <span> 2EA </span> / <span class="text-disabled">총 30EA</span>
            </p>
          </div>
          <div></div>
        </v-col>
        <v-col cols="12">
          <div class="d-flex align-center justify-space-around">
            <p class="text-disabled">
              일 평균 운행 거리
              <v-icon class="pl-2">mdi-help-circle-outline</v-icon>
            </p>
            <p class="text-h5 font-weight-bold">1,024km</p>
          </div>
          <div class="text-center"></div>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>
