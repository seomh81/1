<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import { TheTreeComp } from "@daiahub/thetreecomp";
import axios from "@/plugin/axios";

const sampleData = [
  {
    TREE_ID: "뷰트리1",
    VIEW_NM: "뷰트리테스트1",
    PARENT_ID: "0",
    type: "view",
    VUE_FILE_URL: "/system/테스트매핑",
  },
  {
    TREE_ID: "뷰트리2",
    VIEW_NM: "뷰트리테스트2",
    PARENT_ID: "0",
    type: "view",
  },
  {
    TREE_ID: "뷰트리3",
    VIEW_NM: "뷰트리테스트3",
    PARENT_ID: "0",
    type: "view",
    VUE_FILE_URL: "/system/트리컴포넌트",
  },

  {
    TREE_ID: "펑션1",
    PARENT_ID: "뷰트리1",
    VIEW_NM: "펑션1",
    type: "function",
  },
  {
    TREE_ID: "펑션2",
    PARENT_ID: "뷰트리1",
    VIEW_NM: "펑션2",
    type: "function",
  },
  {
    TREE_ID: "펑션3",
    PARENT_ID: "뷰트리1",
    VIEW_NM: "펑션3",
    type: "function",
  },
  {
    TREE_ID: "펑션4",
    PARENT_ID: "뷰트리2",
    VIEW_NM: "펑션4",
    type: "function",
  },
  {
    TREE_ID: "펑션5",
    PARENT_ID: "뷰트리3",
    VIEW_NM: "펑션5",
    type: "function",
  },
  {
    TREE_ID: "펑션6",
    PARENT_ID: "뷰트리3",
    VIEW_NM: "펑션6",
    type: "function",
  },
];
const treeviewData = ref([]);
const TreeCompTgt = ref();
const canEdit = ref(true);
const expandAll = ref(true);
const treeEditTarget = ref([]);
watch(treeEditTarget, () => {
  console.log(treeEditTarget.value);
});
const getMappingData = async () => {
  // const data = axios.get("/v1/priv/listviewfuncmap");

  // let { status, payload } = await getlistViewFuncMap();

  const createDataTree = (dbData) => {
    let rtnVal = {};
    let treeData = [];
    dbData.forEach((data) => {
      rtnVal[data.TREE_ID] = {
        name: data.VIEW_NM ? data.VIEW_NM : data.FUNC_NM,
        tree_id: data.TREE_ID,
        p_tree_id: data.PARENT_ID,
        ...data,
        children: [],
      };
    });
    dbData.forEach((data) => {
      if (data.PARENT_ID == 0) {
        treeData.push(rtnVal[data.TREE_ID]);
      } else {
        rtnVal[data.PARENT_ID]?.children.push(rtnVal[data.TREE_ID]);
      }
    });
    return JSON.parse(JSON.stringify(treeData));
  };

  treeviewData.value = createDataTree(sampleData);
};
const addFolder = () => {
  const newRoot = {
    name: "테스트 뷰 네임",
    view_id: "테스트 뷰아이디 ",
    id: "",
    p_tree_id: "0",
  };
  TreeCompTgt.value.MakeFolder(TreeCompTgt, newRoot);
};
const remove = () => {
  TreeCompTgt.value?.RemoveItem(TreeCompTgt);
};
const move = () => {
  TreeCompTgt.value?.MoveItem(TreeCompTgt, TreeCompTgt);
};
getMappingData();
</script>

<template>
  <v-container class="sample-test-container">
    <SampleMenuView />    
    <v-card :style="{ marginTop: '30px' }">
      <TheTreeComp
        ref="TreeCompTgt"
        elementid="MainTree"
        :items="treeviewData"
        :canEdit="canEdit"
        :expandAll="expandAll"
        @update:onGoingEdit="(returnVal) => (treeEditTarget = returnVal)"
      >
      </TheTreeComp>
      <v-btn @click="addFolder">폴더 추가</v-btn>
      <v-btn @click="remove">삭제</v-btn>
      <v-btn @click="move">이동</v-btn>
    </v-card>
  </v-container>
</template>

<style scoped></style>
