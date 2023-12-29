// Styles
import "@mdi/font/css/materialdesignicons.css";
import "vuetify/styles";

// Vuetify
import { createVuetify } from "vuetify";
import colors from "vuetify/lib/util/colors";
console.log(colors);
// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  theme: {
    defaultTheme: "light",
    themes: {
      light: {
        dark: false,
        colors: {
          primary: "#00C44F",
          white: "#fff",
          // "page-header-background": "#d7d7ce",
          // "page-background": "#cdcdc1",
          // "table-header": "#cdcdc1",
          // background: "#c0c0b5",
          // "header-background": "#b5b5a6",
          // "info-text": "#666660",
        },
      },
      dark: {
        dark: true,
        colors: {
          // primary: "#52E3C2",
          // "page-header-background": "#282831",
          // "page-background": "#32323E",
          // "table-header": "#2e2e2e",
          background: "#282828",
          "header-background": "#4a4a59",
          // "info-text": "#99999F",
        },
      },
    },
  },
});
