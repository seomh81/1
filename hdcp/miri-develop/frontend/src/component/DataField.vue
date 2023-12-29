<script setup>
import { inject } from "vue";

import DatePicker from "./DatePicker.vue";

const props = defineProps(["name"]);
const data = inject(props.name ?? "searchBar", {});
</script>
<template>
  <v-col class="flex-grow-1 py-0" v-if="data?.inputs">
    <v-container fluid class="px-0">
      <v-row>
        <v-col
          class="py-0"
          v-for="item in data?.inputs"
          :key="item"
          :cols="item?.cols"
        >
          <v-text-field
            @keydown.enter.prevent="item.event()"
            v-if="item?.type == 'field'"
            v-model="item.target"
            :label="item?.label"
            :variant="item.variant ? item.variant : 'solo'"
            density="compact"
            @click:clear="item.target = ''"
            hide-details
            clearable
          ></v-text-field>
          <v-select
            v-if="item?.type == 'select'"
            :label="item.title"
            elevation="1"
            :variant="item.variant ? item.variant : 'solo'"
            hide-details
            density="compact"
            v-model="item.target"
            return-object
            :items="item.items"
            :item-title="item.itemTitle"
          ></v-select>

          <DatePicker
            v-if="item.type === 'datePicker'"
            :item="item"
            @update:dateChange="(returnVal) => (item.target = returnVal)"
          ></DatePicker>
        </v-col>
      </v-row>
    </v-container>
  </v-col>
</template>
<style lang="scss"></style>
