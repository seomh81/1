import axios from "@/plugin/axios";
import { defineStore } from "pinia";
import { ref, computed, reactive } from "vue";
import { uiStore } from "./UiStore";
import { useRouter, useRoute } from "vue-router";
import { useToast } from "vue-toastification";
import { userStore } from "./UserStore";
const toast = useToast();
export const connectionStore = defineStore("connection", () => {
  const useUiStore = uiStore();
  const router = useRouter();
  const useUserStore = userStore();

  const request = async (params) => {
    params.queryparam.userLocale = useUserStore.userInfo.locale;
    let axiosTemp = {
      method: params.method,
      url: params.url,
      params: params.queryparam,
      headers: params.headers,
    };
    if (params.method == "post") {
      axiosTemp["data"] = params.queryparam;
      delete axiosTemp["params"];
    }
    const responseParser = async (data) => {
      const reponseObejct = {
        data: data?.response?.data,
        code: data?.code,
        result: data?.response?.result,
        message: data?.message,
        because: data?.because,
      };
      if (data.code === 200 || data.code === 400) {
        return reponseObejct;
      } else if (data.code === 401) {
        if (data.code !== null) {
          if (data.response.result == "REFRESH") {
            const refresh = await axios.post("/v2/auth/refresh", {
              userId: "test@aa.bb",
            });
            if (refresh.response.result === "success") {
              localStorage.setItem(
                "accessToken",
                refresh.response.data.accessToken
              );
              const result = await responseParser(await axios(axiosTemp));
              useUiStore.spinerControll(false);

              return result;
            } else if (refresh.response.result === "PROVE") {
              localStorage.removeItem("accessToken");
              router.push("/");

              toast.error("로그인 시간이 만료되었습니다. 다시 로그인 해주세요");
            }
            useUiStore.spinerControll(false);
          }
        }
      } else if (data.code === 403) {
        //접근 권한 없음
      }
    };
    // const data = await axios(axiosTemp);
    // console.log(data);
    try {
      useUiStore.spinerControll(true);

      const result = responseParser(await axios(axiosTemp));
      useUiStore.spinerControll(false);
      return result;
    } catch {
      useUiStore.spinerControll(false);
    }
    // return result;
    // const result = responseParser(await axios(axiosTemp));
    // useUiStore.spinerControll(false);
  };

  const testClickBtn = (params) => {
    const triggerAction = {
      search: () => {
        console.log("서치 클릭, 서치데이타도 같이 가져옴", params.searchData);
      },
      add: () => console.log("애드"),
      update: () => console.log("업데이트"),
      remove: () => console.log("리무브"),
    };
    triggerAction[params.method]();
  };

  return {
    request,
    testClickBtn,
  };
});
