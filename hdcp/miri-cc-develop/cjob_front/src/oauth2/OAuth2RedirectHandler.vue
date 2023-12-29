<template>
  <div></div>
</template>
<script setup>
import { useRouter } from "vue-router";
import { userStore } from "@/store/UserStore";
const useUserStore = userStore();

const router = useRouter();
// 즉시 실행 함수로 oauth redirect 실행해서 라우트 변경
(() => {
  const getUrlParameter = (name) => {
    name = name.replace(/[\\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
    var results = regex.exec(window.location.search);

    return results === null
      ? ""
      : decodeURIComponent(results[1].replace(/\+/g, " "));
  };
  const token = getUrlParameter("token");
  const error = getUrlParameter("error");

  if (token) {
    localStorage.setItem("accessToken", token);
    localStorage.setItem("refreshToken", null);
    // useUserStore.LoadCurrentlyLoggedInUser();
    router.push("/dev/sample");
  } else {
    console.log(error);
    router.push("/");
  }
})();
</script>
