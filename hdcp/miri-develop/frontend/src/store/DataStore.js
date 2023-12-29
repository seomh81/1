import { defineStore } from "pinia";
import { ref, computed, reactive } from "vue";
import { uiStore } from "./UiStore";
import { useRouter, useRoute } from "vue-router";
import { useToast } from "vue-toastification";
const toast = useToast();
export const dataStore = defineStore("data", () => {
  const targetMachineData = ref({});
  const setMachineData = (params) => {
    targetMachineData.value = params;
  };

  const targetElvatorNumber = ref();
  const setElvatorNumber = (params) => {
    targetElvatorNumber.value = params;
  };

  const targetAcptDate = ref();
  const setAcptDate = (param) => {
    targetAcptDate.value = param;
  };
  const targetServiceElInfo = ref();
  const setServiceElInfo = (param) => {
    targetServiceElInfo.value = param;
  };
  return {
    targetMachineData,
    setMachineData,
    targetElvatorNumber,
    setElvatorNumber,
    targetAcptDate,
    setAcptDate,
    targetServiceElInfo,
    setServiceElInfo,
  };
});
