<script setup>

import DataField from "./DataField.vue";
import Button from "./Button.vue";
const props = defineProps(["name"]);
</script>

<template>
  <v-container fluid class="pt-0 mb-1 px-0">
    <v-row align="center">
      <DataField :name="props.name"></DataField>
      <Button :name="props.name"></Button>
    </v-row>
  </v-container>
</template>

<style lang="scss"></style>
