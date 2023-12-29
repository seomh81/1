<script setup>
import { ref, onMounted,computed,watch } from "vue";

import DatePicker from "tui-date-picker";
const datepickerBox = ref();
const datepickerInput = ref();
const datepicker = ref();
const date = ref();
const props = defineProps(["item"]);
const emit = defineEmits(["update:inputChange"]);

const fieldValue = computed(() =>props.item.target);


watch(date, () => {
  emit("update:inputChange", date.value);

})
onMounted(() => {
  datepicker.value = new DatePicker(datepickerBox.value, {
    date: new Date(),
    input: {
      element: datepickerInput.value.$el,
      format: "yyyy-MM-dd",
    },
    language: "ko",
  });
  datepicker.value.on("change", () => {
    let focusDate = datepicker.value.getDate();

    date.value =
      focusDate.getFullYear() +
      "-" +
      ("0" + (1 + focusDate.getMonth())).slice(-2) +
      "-" +
      ("0" + focusDate.getDate()).slice(-2);

    // selectYears.value = datepicker.value.getDate().getFullYear();
    // selectMonth.value = datepicker.value.getDate().getMonth() + 1;
    // calendar.value.setDate(new Date(selectYears, selectMonth));
    // console.log(`Selected date: ${datepicker.value.getDate().getMonth() + 1}`);
  });
});
</script>

<template>
  <v-text-field
    ref="datepickerInput"
    :label="item.title"
    v-model="fieldValue"
    class="customeHeightInput field"
    variant="outlined"
    hide-details
    append-inner-icon="mdi-calendar-month"
    clearable
  ></v-text-field>
  <div ref="datepickerBox" class="datepickerBox" style="margin-top: -1px"></div>
</template>

<style lang="scss" >
.datepickerBox {
  .tui-datepicker {
    z-index: 1;
  }

  .tui-calendar-body{
    padding: 0 1em;
  }
}
</style>
