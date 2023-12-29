<script setup>
const props = defineProps(["img"]);
</script>

<template>
  <v-carousel
    height="96vh"
    cycle
    show-arrows="hover"
    hide-delimiters
    progress="primary"
  >
    <v-carousel-item v-for="(item, i) in props.img" :key="i">
      <v-img height="96vh" cover :src="item"> </v-img>
    </v-carousel-item>
  </v-carousel>
</template>
