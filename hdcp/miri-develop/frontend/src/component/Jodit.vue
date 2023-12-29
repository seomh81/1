<script setup>
import { JoditEditor } from "jodit-vue";
import { userStore } from "@/store/UserStore";
import { ls } from "@/plugin/secure";
import { useTheme } from "vuetify";

import { ref,  computed } from "vue";
const useUserStore = userStore();
const editor = ref();
const props = defineProps(["id", "contents"]);
const emits = defineEmits(["update:contents"]);
const theme = useTheme();
const editorKey = ref(0);
const computedText = computed({
  get() {
    return props.contents ?? "";
  },
  set(value) {
    return emits("update:contents", value);
  },
});
const buttons = [
  "bold",
  "italic",
  "underline",
  "brush",
  "strikethrough",
  "eraser",
  "ol",
  "font",
  "fontsize",
  "paragraph",
  "image",
  "lineHeight",
  "superscript",
  "subscript",
  "file",
  "hr",
  "table",
  "symbols",
  "indent",
  "outdent",

  "undo",
  "redo",
];

const config = {
  debugLanguage: false,

  uploader: {
    multiple: false,
    headers: {
      Authorization: `Bearer ${ls.get("accessToken")}`,
    },
    url: `${process.env.VUE_APP_API_BASE_URL?.toString()}/v2/storage/images/upload`,
    buildData: function (data) {
      let formData = new FormData();
      const requestObj = {
        userLocale: useUserStore?.userInfo?.locale ?? "ko_kr",
      };
      formData.append(
        "request",
        new Blob([JSON.stringify(requestObj)], { type: "application/json" })
      );
      data.forEach((value, key) => {
        if (key.includes("files")) {
          formData.append("files", value);
        }
      });

      return formData;
      },
    isSuccess: function (resp) {
      resp.response.data.forEach((imgPath) => {
        this.jodit.selection.insertImage(
          `${process.env.VUE_APP_API_BASE_URL?.toString() + imgPath}`
        );
      });
    },
  },
  theme: theme.global.name.value == "light" ? "default" : "dark",
  direction: "",
  showCharsCounter: false,
  showWordsCounter: false,
  showXPathInStatusbar: false,
  defaultMode: "1",
  saveModeInStorage: true,
  minHeight: 450,
  enter: "BR",

  maxHeight: 450,
};

</script>
<template>
  <v-card class="customCard">
    <jodit-editor
      v-model="computedText"
      ref="editor"
      :buttons="buttons"
      :config="config"
      :key="id ?? editorKey"
    />
  </v-card>
</template>
<style lang="scss">
.jodit-workplace {
  overflow: scroll;
}
.jodit-popup__content {
  width: 200px;
}
.jodit-ui-button_link {
  display: none;
}
.jodit-placeholder {
  display: none !important;
}
.jodit-ui-group {
  padding: 0 6px;
}
.jodit-toolbar-button:hover {
  border-radius: 8px;
}
.jodit-container {
  border-radius: 8px !important;
  &:not(.jodit_inline) {
    border: 1px solid #fafafa;
  }
  box-shadow: -1px 0px 3px -2px var(--v-shadow-key-umbra-opacity, rgba(0, 0, 0, 0.1)),
    0px 1px 2px 0px var(--v-shadow-key-penumbra-opacity, rgba(0, 0, 0, 0.1)),
    0px 1px 5px 0px var(--v-shadow-key-penumbra-opacity, rgba(0, 0, 0, 0.1));

  .jodit-toolbar__box {
    border-radius: 8px 0px 0px;
    &:not(:empty):not(:empty) {
      background-color: transparent;
    }
  }
  .jodit-status-bar {
    border-radius: 0px 0px 8px;
    background-color: transparent;
  }
  
}.jodit-add-new-line {
  display: none;
}

</style>
