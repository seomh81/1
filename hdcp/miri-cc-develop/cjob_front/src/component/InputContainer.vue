<script setup>
import { ref, computed } from "vue";
import { useI18n } from "vue-i18n";
import { useDisplay } from "vuetify";
import DataField from "./DataField.vue";
import Button from "./Button.vue";
import DatePicker from "./DatePicker.vue";
const display = ref(useDisplay());
const props = defineProps(["inputItems", "buttonItems", "secondButtonItems"]);
const emit = defineEmits([
  "update:inputChange",
  "update:buttonEvent",
  "update:secondButtonEvent",
]);
</script>

<template>
  <v-container fluid class="px-0">
    <v-row align="center">
      <v-col class="flex-grow-1 py-0">
        <v-container fluid class="px-0">
          <v-row>
            <v-col
              class="py-0"
              v-for="item in inputItems"
              :key="item"
              :cols="item?.cols"
            >
              <DataField
                v-if="item.type === 'select' || 'field'"
                :item="item"
                @update:inputChange="emit('update:inputChange', $event, item)"
              ></DataField>
              <DatePicker
                :item="item"
                @update:inputChange="emit('update:inputChange', $event, item)"
                v-if="item.type === 'datePicker'"
              ></DatePicker>
            </v-col>
          </v-row>
        </v-container>
      </v-col>
      <v-col cols="12" sm="12" md="auto" class="py-0">
        <v-container fluid class="pl-0 pr-3">
          <v-row justify="end">
            <v-col
              cols="auto"
              class="py-0 px-0"
              v-for="item in buttonItems"
              :key="item"
            >
              <Button
                class="ml-2"
                :item="item"
                @update:buttonEvent="emit('update:buttonEvent', item)"
              ></Button>
            </v-col>
          </v-row>
        </v-container>
      </v-col>
      <v-col v-if="secondButtonItems" cols="12" class="py-0">
        <v-container fluid class="pl-0 pr-3">
          <v-row justify="end">
            <v-col
              cols="auto"
              class="py-0 px-0"
              v-for="item in secondButtonItems"
              :key="item"
            >
              <Button
                class="ml-2"
                :item="item"
                @update:buttonEvent="emit('update:secondButtonEvent', item)"
              ></Button>
            </v-col>
          </v-row>
        </v-container>
      </v-col>
    </v-row>
  </v-container>
  <!-- <div class="d-flex justify-end align-center">
    <v-text-field
      class="customeHeightInput field"
      :style="{ flex: '50%' }"
      :label="inputOptions.fieldLabel"
      variant="outlined"
      v-model="fieldData"
      hide-details
      clearable
    ></v-text-field>
    <v-select
      v-if="inputOptions.hideSelect == true ? false : true"
      class="ml-3 customeHeightInput select"
      label="언어"
      variant="outlined"
      hide-details
      return-object
      :items="inputOptions.selectItems"
      v-model="selectData"
      :item-title="inputOptions.selectTitle"
    ></v-select>
    <slot name="secondSelect"></slot>
  </div> -->

  <slot name="secondLine"></slot>
</template>

<style lang="scss"></style>
