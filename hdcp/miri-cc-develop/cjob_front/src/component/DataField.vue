<script setup>
import { ref, computed } from "vue";
import { useI18n } from "vue-i18n";
import { useDisplay } from "vuetify";
const props = defineProps(["item"]);
console.log(props);
const emit = defineEmits(["update:inputChange"]);
const fieldValue = computed({
  get() {
    return props.item.target;
  },
  set(value) {
    emit("update:inputChange", value);
  },
});
</script>
<template>
  <v-text-field
    v-if="item?.type == 'field'"
    v-model="fieldValue"
    class="customeHeightInput field"
    :label="item?.label"
    variant="outlined"
    hide-details
    clearable
  ></v-text-field>
  <v-select
    v-if="item?.type == 'select'"
    class="customeHeightInput select"
    :label="item.title"
    :variant="item.variant ? item.variant : 'outlined'"
    hide-details
    v-model="fieldValue"
    return-object
    :items="item.items"
    :item-title="item.itemTitle"
  ></v-select>
</template>
