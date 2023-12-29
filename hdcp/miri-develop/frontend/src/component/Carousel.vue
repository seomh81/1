<script setup>
import { ref, onMounted } from "vue";
import { connectionStore } from "@/store/ConnectionStore";
const useConnectionStore = connectionStore();
const images = ref([]);
const baseUrl = ref(process.env.VUE_APP_API_BASE_URL);

const getLoginAdImages = async () => {
  const { because, code, data, message, result } =
    await useConnectionStore.request({
      url: "/v2/ad/signin/images",
      method: "get",
    });
  if (code == 200) {
    images.value = data;
  }
};
onMounted(() => {
  getLoginAdImages();
});
</script>

<template>
  <v-carousel
    v-if="images.length > 0"
    height="96vh"
    cycle
    show-arrows="hover"
    hide-delimiters
    progress="primary"
  >
    <v-carousel-item v-for="(item, i) in images" :key="i">
      <v-img
        height="96vh"
        cover
        :src="`${baseUrl}/v2/storage/images/viewer/${item}`"
      >
      </v-img>
    </v-carousel-item>
  </v-carousel>
</template>
