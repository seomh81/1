<script setup>
import {  inject } from "vue";

const props = defineProps(["name"]);
const data = inject(props.name ?? "searchBar", {});
</script>
<template>
  <v-col
    v-if="data?.buttons"
    cols="12"
    sm="12"
    md="auto"
    class="justify-self-end ml-auto py-0"
  >
    <v-container fluid class="pl-0 pr-3">
      <v-row justify="end">
        <v-col
          cols="auto"
          class="py-0 px-0"
          v-for="item in data?.buttons"
          :key="item"
        >
          <v-btn
            :disabled="item?.disabled"
            @click="item.event()"
            :size="item?.size"
            rounded="lg"
            height="40"
            class="customBtn"
            :prepend-icon="
              item.icon
                ? item.icon
                :
                item.methodName == 'BTN_SEARCH'?
                'mdi-magnify':
                item.methodName =='BTN_CREATE'?
                'mdi-plus-circle-outline':
                item.methodName =='BTN_UPDATE'?
                'mdi-pencil-circle-outline':
                item.methodName == 'BTN_DELETE'
                ? 'mdi-close-circle-outline'
                : ''
            "
          >
            {{ item?.title }}
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-col>
  <v-col
    v-if="data?.secondButtons"
    cols="12"
    sm="12"
    md="12"
    class="justify-self-end ml-auto py-0"
  >
    <v-container fluid class="pl-0 pr-3">
      <v-row justify="end">
        <v-col
          cols="auto"
          class="py-0 px-0"
          v-for="item in data.secondButtons"
          :key="item"
        >
          <v-btn
            :disabled="item?.disabled"
            @click="item.event()"
            :size="item?.size"
            rounded="lg"
            height="40"
            class="customBtn"
            :prepend-icon="
              item.icon
                ? item.icon
                :
            
                item.methodName =='BTN_CREATE'?
                'mdi-plus-circle-outline':
                item.methodName =='BTN_UPDATE'?
                'mdi-pencil-circle-outline':
                item.methodName == 'BTN_DELETE'
                ? 'mdi-close-circle-outline'
                : ''
            "          >
            {{ item?.title }}
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-col>
</template>
