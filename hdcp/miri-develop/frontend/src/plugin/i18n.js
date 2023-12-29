import { createI18n } from "vue-i18n";

import en_gn from "../locale/en_gn.json";
import ko_kr from "../locale/ko_kr.json";

export default createI18n({
  legacy: false,
  locale: process.env.VUE_APP_I18N_LOCALE?.toString(),
  globalInjection: true,
  fallbackLocale: "en_gn",

  messages: {
    en_gn,
    ko_kr,
  },
});
