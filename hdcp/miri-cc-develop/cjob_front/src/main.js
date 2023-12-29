import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import vuetify from "./plugin/vuetify";
import { loadFonts } from "./plugin/webfontloader";
import i18n from "./plugin/i18n";
import axios from "./plugin/axios";
import { createPinia } from "pinia";
import Toast from "vue-toastification";
import "vue-toastification/dist/index.css";
import VueGridLayout from "vue-grid-layout";
import "@/assets/styles/main.scss";
const options = {
  position: "top-right",
  timeout: 2508,
  closeOnClick: true,
  pauseOnFocusLoss: true,
  pauseOnHover: true,
  draggable: true,
  draggablePercent: 1.29,
  showCloseButtonOnHover: false,
  closeButton: "button",
  icon: true,
  rtl: false,
};

loadFonts();
const pinia = createPinia();

// app.config.globalProperties.axios = axios;
// app.use(i18n).use(router).use(store).use(vuetify).mount("#app");

createApp(App)
  .use(i18n)
  .use(router)
  .use(VueGridLayout)
  .use(vuetify)
  .use(pinia)
  .use(Toast, options)
  .provide("$axios", axios)

  .mount("#app");
