import axios from "axios";
let storageTokenLoading = false;
let getToken = `Bearer ${localStorage.getItem("accessToken")}`;
const apiClient = axios.create({
  "Content-Type": `application/json`,
  baseURL: process.env.VUE_APP_API_BASE_URL?.toString(),
  headers: {
    Authorization: localStorage.getItem("accessToken")
      ? `Bearer ${localStorage.getItem("accessToken")}`
      : undefined,
  },
});
apiClient.interceptors.request.use(
  (config) => {
    config.headers.Authorization = localStorage.getItem("accessToken")
      ? `Bearer ${localStorage.getItem("accessToken")}`
      : undefined;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
apiClient.interceptors.response.use(
  (response) => {
    const { data } = response;

    storageTokenLoading = false;
    return data;
  },
  async (error) => {
    console.log(error);
    // const { data } = response;
    // return data;
    const { response } = error;
    const { data } = response;
    return data;
    // if (data.code === 400) {
    //   //400일떄
    //   // return data;
    // } else if (data.code === 401) {
    //   if (data.code !== null) {
    //     if (data.response.result == "REFRESH") {
    //       const refresh = await apiClient.post("/v2/auth/refresh", {
    //         userId: "test@aa.bb",
    //       });
    //       console.log(refresh);
    //       localStorage.setItem(
    //         "accessToken",
    //         refresh.response.data.accessToken
    //       );
    //       oriConfig.headers.Authorization = `Bearer ${refresh.response.data.accessToken}`;
    //       // return apiClient();
    //       return apiClient(oriConfig);
    //     } else if (data.response.result == "PROVE") {
    //       route.
    //     }
    //   }
    // } else if (response.data.code === 403) {
    //   //접근 권한 없음
    // }
    // getToken = `Bearer ${localStorage.getItem("accessToken")}`;
    // let oriConfig = error.config;
    // if (error.response.status === 401) {
    //   oriConfig.headers.Authorization = getToken;
    //   return apiClient(oriConfig);
    // }
    //  return { code: error.response.status };
  }
);

export default apiClient;
