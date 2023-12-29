// Styles
import "@mdi/font/css/materialdesignicons.css";
import "vuetify/styles";
import "material-design-icons-iconfont/dist/material-design-icons.css"; // Ensure your project is capable of handling css files
import { aliases, md } from "vuetify/iconsets/md";
// Vuetify
import { VSkeletonLoader } from "vuetify/labs/VSkeletonLoader";
import { createVuetify } from "vuetify";

import colors from "vuetify/lib/util/colors";

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  components: {
    VSkeletonLoader,
  },
  theme: {
    defaultTheme: "light",
    themes: {
      light: {
        dark: false,
        colors: {
          primary: "#00C44F",
          white: "#fff",
          info: "#0D99FF",
          // surface: '#cdcdc1',
          cancle: "#eee",
          // "page-header-background": "#d7d7ce",
          background: "#f5f5f5",
          // "table-header": "#cdcdc1",
          // background: "#c0c0b5",
          // "header-background": "#b5b5a6",
          // "info-text": "#666660",
        },
      },
      dark: {
        dark: true,
        colors: {
          primary: "#00C44F",

          cancle: "#5a5a5a",

          // primary: "#52E3C2",
          // "page-header-background": "#282831",
          // "page-background": "#32323E",
          // "table-header": "#2e2e2e",
          // background: "#282828",
          // "header-background": "#4a4a59",
          // "info-text": "#99999F",
        },
      },
    },
  },
});
