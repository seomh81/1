import axios from "@/plugin/axios";
import { computed } from "vue";
import { defineStore } from "pinia";
import { uiStore } from "./UiStore";
import { useRouter } from "vue-router";
import { useToast } from "vue-toastification";
import { userStore } from "./UserStore";
export const connectionStore = defineStore("connection", () => {
  const useUiStore = uiStore();
  const useUserStore = userStore();
  // const userLocale = computed(() => useUserStore?.userInfo?.locale);
  
  const request = async (params) => {
    if(params.spinerFg != false)
    {
      params.spinerFg = true;
    }
    let axiosTemp = {
      method: params.method,
      url: params.url,
      params: params.queryparam ?? {},
      headers: params.headers,
      timeout: params.timeout ? params.timeout : 30000,
      responseType: params.responseType,
      // userLocale
    };
    console.log(useUserStore?.userInfo);
    if (useUserStore?.userInfo?.locale && !!params.queryparam == true) {
      axiosTemp.params.userLocale = useUserStore?.userInfo?.locale ?? "ko_kr";
    } else {
      axiosTemp.params.userLocale = "ko_kr";
    }

    if (params.method == "post") {
      axiosTemp["data"] = params.queryparam;
      delete axiosTemp["params"];
    }

    try {
      if(params.spinerFg)
      {
        useUiStore.spinerControll(true);
      }
      
      const result = await axios(axiosTemp);
      useUiStore.spinerControll(false);
      return result;
    } catch {
      useUiStore.spinerControll(false);
      return;
    }
  };

  return {
    request,
  };
});
