<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { useTheme } from "vuetify";

import { useI18n } from "vue-i18n";
import dayjs from "dayjs";
import DatePicker from "tui-date-picker";
const datepickerBox = ref();
const datepickerInput = ref();
const datepicker = ref();
const date = ref();
const props = defineProps(["item"]);
const emit = defineEmits(["update:dateChange"]);
const i18n = useI18n();
const theme = useTheme();
const textColor = computed(() => {
  return theme.global.name.value == "light" ? "black" : "white";
});
const backgroundColor = computed(() => {
  return theme.global.name.value == "light" ? "white" : "#212121";
});
const computedLocale = computed(() =>
  i18n.locale.value == "ko_kr" ? "ko" : "en"
);
watch(date, () => {
  emit("update:dateChange", date.value);
});
onMounted(() => {
  datepicker.value = new DatePicker(datepickerBox.value, {
    date: new Date(dayjs(props.item.target)),
    type: props.item.viewType ? props.item.viewType : "date",
    input: {
      element: datepickerInput.value.$el,
      format: "yyyy-MM-dd",
    },
    language: computedLocale.value,
  });
  datepicker.value.on("change", () => {
    let focusDate = datepicker.value.getDate();
    if (props.item.viewType == "month") {
      date.value =
        focusDate.getFullYear() +
        "-" +
        ("0" + (1 + focusDate.getMonth())).slice(-2);
    } else {
      date.value =
        focusDate.getFullYear() +
        "-" +
        ("0" + (1 + focusDate.getMonth())).slice(-2) +
        "-" +
        ("0" + focusDate.getDate()).slice(-2);
    }
  });
});
</script>

<template>
  <v-text-field
    ref="datepickerInput"
    :label="item.title"
    :model-value="item.target"
    density="compact"
    variant="solo"
    hide-details
    append-inner-icon="mdi-calendar-month"
  ></v-text-field>
  <div ref="datepickerBox" class="datepickerBox" style="margin-top: -1px"></div>
</template>

<style lang="scss">
.datepickerBox {
  .tui-calendar-header {
    border: none;
  }
  *:not(.tui-is-selected) {
    color: v-bind(textColor) !important;
    background: v-bind(backgroundColor) !important;
  }

  .tui-datepicker {
    z-index: 1;
  }

  .tui-calendar-body {
    padding: 0 1em;
    padding-top: 1em;
  }
}
</style>
