<script setup>
import { ref, reactive, onMounted, computed, watch } from "vue";
import { TabulatorFull as Tabulator } from "tabulator-tables";
import "tabulator-tables/dist/css/tabulator_semanticui.min.css";
import { useI18n } from "vue-i18n";
import { useTheme } from "vuetify";

const theme = useTheme();
const tabulator = ref(); //variable to hold your table
const table = ref(null); //reference to your table element
// 41=헤더 / 48=tabs, 28=tabs 패딩 / 56 = cardaction / 36=패딩
const props = defineProps(["tableData", "tableName", "columns", "height"]);
const emit = defineEmits(["update:rowTarget"]);
const keys = ref(0);
const i18n = useI18n();
const textColor = computed(() => {
  return theme.global.name.value == "light" ? "black" : "white";
});
const hoverColor = computed(() => {
  return theme.global.name.value == "light" ? "#eee" : "#777";
});
watch(
  () => [props.tableData, i18n.locale.value],
  (newVal, oldVal) => {
    tabulator.value.replaceData(props.tableData);

    if (newVal[1] !== oldVal[1]) {
      setTimeout(() => {
        tabulator.value.setColumns([...props.columns]);
      }, 100);
    }
  }
);

onMounted(() => {
  tabulator.value = new Tabulator(table.value, {
    index: "idx",
    data: props.tableData, //link data to table
    selectable: true,
    selectableRangeMode: "click",
    selectablePersistence: false,
    columnHeaderVertAlign: "middle",
    printRowRange: "selected", //
    height: props.height,
    debugInvalidComponentFuncs: false,
    pagination: true,
    layout: "fitColumns",
    resizableColumnFit: true,
    // paginationSize: 5,
    // paginationAddRow: "table",
    locale: true,
    langs: {
      "ko-kr": {
        pagination: {
          first: "<<", //text for the first page button
          first_title: "", //tooltip text for the first page button
          last: ">>",
          last_title: "",
          prev: "<",
          prev_title: "",
          next: ">",
          next_title: "",
          all: "All",
        },
      },
    },

    columns: props.columns,
  });
  tabulator.value.on("rowSelected", function (row) {
    const rows = tabulator.value.getSelectedRows();
    const rowArr = [];

    rows.forEach((x) => {
      rowArr.push(x._row.data);
    });
    emit("update:rowTarget", rowArr);
  });
  tabulator.value.on("rowDeselected", function (row) {
    emit("update:rowTarget", []);
    // getMasterDataEvent(row._row.data);
  });

  tabulator.value.on("localized", function (locale, lang) {
    // console.log(columns2);
    // lang.columns = columns2;
  });

  // window.addEventListener('resize', function(){
  //   table.value.redraw();
  // });
});
</script>
<template>
  <div ref="table" :key="keys" :style="{ height: props.height }"></div>
</template>
<style lang="scss">
.tabulator * {
  color: v-bind(textColor);
}
.tabulator-row.tabulator-selectable:hover {
  background-color: v-bind(hoverColor) !important;
}

/* 최상위 테이뷸레이터 위아래 마진값 삭제 각 컬럼에 넣기 번거로워 css로 텍스트 가운데 정렬 */
.tabulator {
  margin: 0;
  /* text-align: center; */
  font-size: 0.8rem;
  border: none;
  /* border: 1px solid #bbb; */
  /* border-radius: 8px; */
  background-color: transparent;
  /* border-radius: 0; */
  .tabulator-header {
    border: none;
    .tabulator-col {
      
      .tabulator-col-group-cols {
        border: none !important;
      }
      .tabulator-col-sorter {
        display: none !important;
      }
    }
  }
  .tabulator-row {
    border: none;
    background: transparent;
  }
  .tabulator-footer {
    border: none;
  }
}
.tabulator-col-resize-handle {
  // border-right: 1px solid #eee;
}

.tabulator-row .tabulator-cell:last-of-type {
  // border-right: none;
}
/* .tabulator .tabulator-header .tabulator-header-contents .tabulator-headers { */
/* height: auto !important; */
/* display: flex;
  align-items: center;
  justify-content: center; */
/* } */
.tabulator .tabulator-header,
.tabulator .tabulator-header .tabulator-col,
.tabulator .tabulator-footer {
  font-weight: 400;
  background-color: transparent;
}
/* .tabulator .tabulator-header .tabulator-col { */
/* height: auto !important; */
/* } */

/* .tabulator */
/* .tabulator-header */
/* .tabulator-col */
/* .tabulator-col-content */
/* .tabulator-col-title { */
/* display: flex;
  align-items: center;
  justify-content: center; */
/* text-align: center; */
/* } */

/* 페이지네이션 css */

/* 페이지네이션 가운데 정렬을 위한 css */
.tabulator-paginator {
  display: flex;
  align-items: center;
  justify-content: center;
}
/* 페이지네이션 보더 제거 및 간격조정 */

.tabulator .tabulator-footer .tabulator-page {
  border: none;
}
/* 페이지네이션 숫자부분 조정 */
.tabulator
  .tabulator-footer
  .tabulator-page:not([data-page="first"], [data-page="last"], [data-page="prev"], [data-page="next"]) {
  padding: 2px 10px;
}
/* 페이지네이션 버튼 따로 조정 */
.tabulator-page[data-page="first"],
.tabulator-page[data-page="last"],
.tabulator-page[data-page="next"],
.tabulator-page[data-page="prev"] {
  padding: 2px 5px !important;
  font-family: "Nanum Gothic";
}
.tabulator .tabulator-footer .tabulatorp-page[data-page="first"] {
  display: none !important;
}

/* 페이지네이션 클릭 css */
.tabulator .tabulator-footer .tabulator-page.active {
  transition: 0.4s;
  /* color: inherit; */
  background-color: rgba(34, 36, 38, 0.1);
}

/* 페이지네이션 호버 애니메이션 추가 및 백그라운드 컬러 변경 */
.tabulator .tabulator-footer .tabulator-page:not(.disabled):hover {
  /* transition: 0.4s; */
  cursor: pointer;
  background: rgba(0, 0, 0, 0.2);
  /* color: inherit; */
}

/* // .tabulator .tabulator-header .tabulator-col .tabulator-col-content { */
/* padding: 0.78571em; */
/* // } */
</style>
