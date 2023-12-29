<script setup>
import { Jodit } from "jodit";
import { watch, ref, onMounted, computed } from "vue";
let editor;
const props = defineProps(["contents"]);
const emit = defineEmits(["update:contents"]);

const checkChange = (e) => {
  console.log("겟 네이티브 벨류", editor.getNativeEditorValue()); // <p><img src="blob:http://localhost:2000/03377cf0-6260-4351-82ad-8a8901ea104f%22%3E</p>

  emit("update:contents", editor.value);
};
onMounted(() => {
  editor = Jodit.make("#editor", {
    activeButtonsInReadOnly: ["source", "fullsize", "print", "about", "dots"],
    toolbarButtonSize: "middle",
    theme: "default",
    debugLanguage: false,
    uploader: {
      url: "http://daialab.co.kr:2002/v1/upload",
      // imagesExtensions: ["jpg", "png", "jpeg", "gif"],

      buildData: function (data) {
        console.log(data);
        console.log(data.getAll("files[0]")[0]);
        return data;
        // let test = new FormData();
        // test.append("files", data.getAll("files[0]")[0]);
        // return test;

        // return new Promise(function (resolve, reject) {
        //   var reader = new FileReader();
        //   reader.readAsDataURL(data.getAll("files[0]")[0]);
        //   reader.onload = function () {
        //     return resolve({
        //       image: reader.result,
        //     });
        //   };
        //   reader.onerror = function (error) {
        //     reject(error);
        //   };
        // });
      },
      isSuccess: function (resp) {
        console.log("isSuccess", resp);
        this.jodit.selection.insertImage(resp.fileKey[0]);
      },
      getMessage: function (resp) {
        console.log(resp);
      },
      defaultHandlerSuccess: function () {},
      defaultHandlerError: function (resp) {
        console.log(resp);
      },
      error: function (e) {
        console.log(e);
      },
    },

    defaultMode: "1",
    saveModeInStorage: true,
    minHeight: 550,
    maxHeight: 550,
    buttons: [
      "source",
      "|",
      "bold",
      "strikethrough",
      "underline",
      "italic",
      "|",
      "ul",
      "ol",
      "|",
      "outdent",
      "indent",
      "|",
      "font",
      "fontsize",
      "brush",
      // "paragraph",
      // "|",
      // "image",
      "video",
      // "table",
      // "link",
      // "|",
      // "align",
      // "undo",
      // "redo",
      // "|",
      // "hr",
      // "eraser",
      // "copyformat",
      // "|",
      // "print",
      // "about",
    ],
    buttonsXS: [
      "bold",
      "image",
      "|",
      "brush",
      "paragraph",
      "|",
      "align",
      "|",
      "undo",
      "redo",
      "|",
      "eraser",
      "dots",
    ],
  });
  editor.value = props.contents;
});
</script>
<template>
  <textarea id="editor" @change="checkChange"></textarea>
</template>
<style lang="scss">
.jodit-workplace {
  overflow: scroll;
}
</style>
