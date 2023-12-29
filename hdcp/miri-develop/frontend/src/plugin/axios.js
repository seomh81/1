import { ls } from "@/plugin/secure";
import axios from "axios";
import { useToast } from "vue-toastification";
import router from "@/router";
import i18n from "../plugin/i18n";

const toast = useToast();
let refreshToken;
const getRefreshToken = () =>
  apiClient
    .post("/v2/auth/refresh", {
      userId: JSON.parse(ls.get("userId")),
    })
    .then((token) => {
      return token.data.accessToken;
    });

const apiClient = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL?.toString(),

  headers: {
    "Content-Type": "application/json",
    Authorization: ls.get("accessToken")
      ? `Bearer ${ls.get("accessToken")}`
      : null,
  },
});

apiClient.interceptors.request.use(
  (config) => {
    config.headers.Authorization = ls.get("accessToken")
      ? `Bearer ${ls.get("accessToken")}`
      : null;

    return config;
  },
  (error) => {
    console.log(error);
    return Promise.reject(error);
  }
);
apiClient.interceptors.response.use(
  (response) => {
    const { data, headers } = response;
    if (headers["content-type"] !== "application/octet-stream") {
      const responseObejct = {
        data: data?.response?.data,
        code: data?.code,
        result: data?.response?.result,
        message: data?.message,
        because: data?.response.because,
      };
      return responseObejct;
    } else {
      return data;
    }
  },
  async (error) => {
    console.log(error);
    if (error.response.status == 401) {
      console.log(error.response.data.status);
      if (error.response.data.response.result == "refresh") {
        if (!refreshToken) {
          refreshToken = getRefreshToken().then((token) => {
            refreshToken = null;
            return token; // (1)
          });
        }
        return refreshToken.then((token) => {
          ls.set("accessToken", token);
          error.config.headers.Authorization = `Bearer ${ls.get(
            "accessToken"
          )}`;
          error.config.data = JSON.parse(error.config.data);

          return apiClient(error.config);
        });
      } else if (error.response.data.response.result == "prove") {
        toast.warning(i18n.global.t("AUTH_LOGIN_MESSAGE_PROVE"));
        ls.clear();
        router.push("/");
        return;
      } else if (error.response.data.response.result == "failure") {
        toast.warning(i18n.global.t("AUTH_LOGIN_FAILURE"));
        ls.clear();
        router.push("/");
        return;
      } 
    }
    const responseObejct = {
      data: error?.response?.data,
      code: error?.response.status,
      result: error?.response?.data?.result,
      message: error?.response?.data?.message,
      because: error?.response?.data?.because,
    };
    return responseObejct;
  }
);

export default apiClient;
