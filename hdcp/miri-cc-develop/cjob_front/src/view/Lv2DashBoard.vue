<script setup>
import DefaultContainer from "@/component/DefaultContainer.vue";
import { ref, onMounted, defineAsyncComponent, computed } from "vue";
import draggable from "vuedraggable";
import { connectionStore } from "@/store/ConnectionStore";
import { _ } from "lodash";
import { v4 as uuidv4 } from "uuid";
import { uiStore } from "@/store/UiStore";
import * as widgetModule from "@/component/Widget/WidgetTemplate";
const useUiStore = uiStore();
const addWidget = uuidv4();
const useConnectionStore = connectionStore();
const widget = ref([]);

// let myWidget = ref([
// {
//   name: "weather",
//   size: {
//     col: 2,
//     row: 2,
//   },
//   position: {
//     x: 0,
//     y: 0,
//     status: "fixed",
//   },
//   body: {
//     type: "text",
//   },
// },
// {
//   name: "watch",
//   size: {
//     col: 1,
//     row: 1,
//   },
//   position: {
//     x: 0,
//     y: 2,
//     status: "fixed",
//   },
//   body: {
//     type: "text",
//   },
// },
// {
//   name: "notice",
//   size: {
//     col: 2,
//     row: 1,
//   },
//   position: {
//     x: 2,
//     y: 0,
//     status: "fixed",
//   },
//   body: {
//     type: "text",
//   },
// },
// {
//   name: "elevator-status",
//   size: {
//     col: 1,
//     row: 1,
//   },
//   position: {
//     x: 1,
//     y: 2,
//     status: "fixed",
//   },
//   body: {
//     type: "text",
//   },
// },
// ]);
const emptyWidget = ref([
  {
    title: "테스트1번",
    id: 1,
    width: 33.333,
    componenet: "WIDGET_CHART1",

    sort: 0,
  },
  {
    title: "테스트2번",
    id: 2,
    width: 33.333,
    componenet: "WIDGET_TEST",
    sort: 1,
  },
  {
    title: "테스트3번",
    id: 3,
    width: 33.333,
    componenet: "WIDGET_KKK",
    sort: 2,
  },
  {
    title: "테스트4번",
    id: 4,
    width: 33.333,

    sort: 3,
  },
  {
    title: "테스트5번",
    id: 5,
    componenet: "WIDGET_WEATHER2",
    width: 33.333,

    sort: 4,
  },
  {
    title: "테스트6번",
    id: 6,
    width: 33.333,
    expend: true,
    componenet: "WIDGET_WEATHER",

    sort: 5,
  },
  {
    title: "테스트7번",
    id: 7,
    width: 33.333,

    componenet: "WIDGET_CHART2",

    sort: 6,
  },
  {
    title: "테스트8번",
    id: 8,
    width: 66.666,

    sort: 7,
  },
]);

const restWidget = ref([]);
const userWidget = ref([
  {
    title: "테스트6번",
    id: 6,
    width: 33.333,
    expend: true,
    componenet: "WIDGET_WEATHER",

    sort: 5,
  },
  {
    title: "테스트1번",
    id: 1,
    width: 33.333,
    componenet: "WIDGET_CHART1",

    sort: 0,
  },
  {
    title: "테스트2번",
    id: 2,
    width: 33.333,
    componenet: "WIDGET_TEST",
    sort: 1,
  },
  {
    title: "테스트5번",
    id: 5,
    componenet: "WIDGET_WEATHER2",
    width: 33.333,

    sort: 4,
  },
]);
const loadingWidget = () => {
  restWidget.value = emptyWidget.value
    .filter((x) => {
      return !userWidget.value.some((item) => {
        return x.id == item.id;
      });
    })
    .sort((a, b) => a.sort - b.sort);
  console.log(restWidget.value);
};

const saveWidget = ref([]);

const key = ref(0);
const target = ref();
const moveTarget = ref();
const onMoveEvent = (evt) => {};
const start = (evt) => {
  // myWidget.value[evt.oldIndex].position.status = "dragging";
  // evt.clone.style.border = "1px solid blue";
  // for (let i = 0; i < myWidget.value.length; i++) {
  //   if (i !== evt.oldIndex) {
  //     if (
  //       myWidget.value[evt.oldIndex].size.col === myWidget.value[i].size.col &&
  //       myWidget.value[evt.oldIndex].size.row === myWidget.value[i].size.row
  //     ) {
  //       myWidget.value[i].position.status = "target-possible";
  //     }
  //   }
  // }
};
const end = (evt) => {
  // myWidget.value.forEach((x, i) => {
  //   x.sort = i;
  // });
};
const expendWidget = (item) => {
  item.width < 34
    ? (item.width = item.width * 2)
    : item.width < 67
    ? (item.width = item.width / 2)
    : "";
  // item.width = item.width * 2;
};
const openAddWidgetEvent = () => {
  loadingWidget();
  openAddWidget.value = true;
};
const addWidgetEvent = () => {
  console.log("hi");
  seletedWidget.value.forEach((index) => {
    userWidget.value.push(restWidget.value[index]);
  });
  seletedWidget.value = [];
  openAddWidget.value = false;
};
onMounted(() => {
  loadingWidget();
});
const removeWidgetEvent = (index) => {
  userWidget.value.splice(index, 1);
};
const openAddWidget = ref(false);
const seletedWidget = ref([]);
</script>

<template>
  <DefaultContainer>
    <v-col cols="12" class="d-flex align-center justify-space-between">
      <span class="dash-title text-medium-emphasis">대시보드</span>

      <v-card-actions>
        <v-btn>기본 레이아웃으로 재설정</v-btn>
        <v-btn @click="openAddWidgetEvent">위젯 추가</v-btn>
      </v-card-actions>
    </v-col>
    <v-col cols="12">
      <transition-group>
        <draggable
          handle="#handle"
          v-model="userWidget"
          item-key="id"
          key="dragggable"
          animation="400"
          class="widget-container"
          :move="onMoveEvent"
          @end="end"
        >
          <template #item="{ element, index }">
            <v-card
              elevation="7"
              rounded="lg"
              class="widget-item"
              :style="{ flex: `0 0 calc(${element.width}% - 20px)` }"
            >
              <v-toolbar density="compact">
                <v-btn id="handle" icon variant="plain"
                  ><v-icon>mdi-drag</v-icon></v-btn
                >
                <v-toolbar-title class="">{{ element.title }}</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-menu location="end">
                  <template v-slot:activator="{ props }">
                    <v-btn
                      variant="plain"
                      icon="mdi-dots-vertical"
                      v-bind="props"
                    ></v-btn>
                  </template>
                  <v-card class="pa-2">
                    <v-list>
                      <v-list-subheader>ACTIONS</v-list-subheader>

                      <v-list-item
                        value="remove"
                        active-color="primary"
                        rounded="lg"
                        @click="removeWidgetEvent(index)"
                      >
                        <template v-slot:prepend>
                          <v-icon
                            icon="mdi-minus-circle-outline"
                            color="red"
                          ></v-icon>
                        </template>

                        <v-list-item-title>삭제</v-list-item-title>
                      </v-list-item>
                      <v-list-item
                        rounded="lg"
                        value="reset"
                        active-color="primary"
                      >
                        <template v-slot:prepend>
                          <v-icon icon="mdi-refresh"></v-icon>
                        </template>

                        <v-list-item-title>초기화</v-list-item-title>
                      </v-list-item>
                      <v-list-item
                        rounded="lg"
                        value="expand"
                        active-color="primary"
                        @click="expendWidget(element)"
                        :disabled="element.expend == true ? false : true"
                      >
                        <template v-slot:prepend>
                          <v-icon icon="mdi-arrow-expand"></v-icon>
                        </template>

                        <v-list-item-title>확대</v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-card>
                </v-menu>
              </v-toolbar>

              <component
                :style="{ height: '180px' }"
                v-if="element?.componenet"
                :item="element"
                :is="widgetModule[element.componenet]"
              >
              </component>
              <div
                :style="{ height: '180px' }"
                v-if="!!element.componenet == false"
              >
                undifined Componenet
              </div>
            </v-card>
          </template>
        </draggable>
      </transition-group>
    </v-col>
  </DefaultContainer>
  <v-dialog v-model="openAddWidget" max-width="70vw" rounded="large">
    <v-toolbar class="add-widget-header">
      <v-toolbar-title>위젯 추가</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon>
        <v-icon @click="openAddWidget = false">mdi-close</v-icon>
      </v-btn>
    </v-toolbar>
    <v-card rounded="large">
      <p class="py-3 pl-11">
        대시보드에 추가할 위젯을 선택하고 추가를 눌러주세요. 해당 위젯은 하단에
        추가됩니다
      </p>

      <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
      <v-item-group
        multiple
        v-model="seletedWidget"
        class="widget-container px-8"
      >
        <v-card
          rounded="lg"
          elevation="6"
          class="widget-item widget-modal-item"
          v-for="(item, i) in restWidget"
          :key="i"
          :style="{ flex: `0 0 calc(33.333% - 20px)` }"
        >
          <v-item v-slot="{ isSelected, toggle }">
            <div
              class="widget-selected"
              :style="{ background: isSelected ? '#4b6cb7' : null }"
              @click.prevent="toggle"
            ></div>
            <v-sheet>
              <v-toolbar density="compact">
                <v-toolbar-title class="">{{ item.title }}</v-toolbar-title>
              </v-toolbar>
              <component
                :style="{ height: '180px' }"
                v-if="item.componenet"
                :is="widgetModule[item.componenet]"
              >
              </component>
              <div
                :style="{ height: '180px' }"
                v-if="!!item.componenet == false"
              >
                undifined Componenet
              </div>
            </v-sheet>
          </v-item>
        </v-card>
      </v-item-group>

      <div class="d-flex justify-end py-3">
        <v-card-actions>
          <v-btn variant="plain" size="large">취소</v-btn>
          <v-btn
            color="primary"
            variant="plain"
            :disabled="seletedWidget.length == 0 ? true : false"
            size="large"
            @click="addWidgetEvent"
            >추가</v-btn
          >
        </v-card-actions>
      </div>
    </v-card>
  </v-dialog>
</template>

<style lang="scss">
.dash-title {
  font-size: 2em !important;
}
.widget-container {
  width: 100%;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
}
.widget-item {
  margin: 10px;
  // height: 12.7rem;
  &:hover {
    cursor: pointer;
  }
}
.widget-modal-item {
  position: relative;
}
.widget-selected {
  width: 100%;
  height: 100%;
  position: absolute;
  opacity: 0.5;
  z-index: 2;
  transition: 0.3s ease;
  display: hidden !important;
}
.widget-seleted-foucs {
  display: block !important;
  background-color: #4b6cb7;
}
.add-widget-header {
}
.widget-content-box {
  border: 1px dashed blue;
  height: 180px;
}
.widget-content {
  height: 100%;
}
</style>
