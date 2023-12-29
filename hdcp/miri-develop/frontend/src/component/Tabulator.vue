<script setup>
import { ref, reactive, onMounted, computed, watch } from "vue";
import { TabulatorFull as Tabulator } from "tabulator-tables";
import "tabulator-tables/dist/css/tabulator_semanticui.min.css";
import { useRouter, useRoute } from "vue-router";

import { useI18n } from "vue-i18n";
import { useTheme } from "vuetify";
import { userStore } from "@/store/UserStore";

import { uiStore } from "@/store/UiStore.js";
import { dataStore } from "@/store/DataStore.js";
const theme = useTheme();
const useUserStore = userStore();
const { t } = useI18n();
const useDataStore = dataStore();
const tabulator = ref(); //variable to hold your table
const table = ref(null); //reference to your table element
const props = defineProps([
  "tableData",
  "tableName",
  "columns",
  "height",
  "pagination",
  "keepData",
  "rowTarget",
  "selectable",
  "export",
  "selectedFirstRow",
  "selectableRangeMode",
  "exportFileName",
]);
const router = useRouter();
const useUiStore = uiStore();
const emit = defineEmits(["update:rowTarget"]);
const keys = ref(0);
const drawer = computed(() => useUiStore.drawer);
const textColor = computed(() => {
  return theme.global.name.value == "light" ? "black" : "#ddd";
});
const hoverColor = computed(() => {
  return theme.global.name.value == "light" ? "#eee" : "#777";
});

const oddColor = computed(() => {
  return theme.global.name.value == "light" ? "#f6f6f6" : "#353535";
});
watch(
  () => [props.tableData, props.columns],
  (newVal, oldVal) => {
    tabulator.value.replaceData(props.tableData);
    if (props.selectedFirstRow) {
      tabulator.value.selectRow(tabulator.value.getRows()[0]._row);
    }
    if (newVal[1] !== oldVal[1]) {
      setTimeout(() => {
        tabulator.value.setColumns(props.columns);
        tabulator.value.redraw();
      }, 300);
    }
  },
  { deep: true }
);

const computedExport = computed(() => props?.export);

watch(computedExport, () => {
  if (props.export) {
    exportTable();
  }
});
watch(drawer, () => {
  tabulator.value.redraw(true);
});

const exportTable = async () => {
  if(props.exportFileName)
  {
    tabulator.value.download("xlsx", `${props.exportFileName}.xlsx`, {
        sheetName: props.tableName,
      });
  }
  else{
    if (useUserStore.userInfo.roleType.toUpperCase() === "SYSTEM") {
      const customColumn = {
        title: t("EMPLOYEE_NUMBER"),
        field: "empId",
        hozAlign: "center",
      };
      await tabulator.value.addColumn(customColumn, false);

      tabulator.value.download("xlsx", `${"내보내기"}.xlsx`, {
        sheetName: props.tableName,
      });

      await tabulator.value.deleteColumn("empId");
    } else {
      tabulator.value.download("xlsx", `${"내보내기"}.xlsx`, {
        sheetName: props.tableName,
      });
    }
  }
};
onMounted(() => {
  tabulator.value = new Tabulator(table.value, {
    layout: "fitColumns",
    data: props.tableData, //link data to table
    selectable: props.selectable ?? true,
    selectableRangeMode: props.selectableRangeMode ?? "click",
    selectablePersistence: false,
    selectableRollingSelection: props.selectableRollingSelection ?? true,
    columnHeaderVertAlign: "middle",
    downloadConfig: {
      columnWidths: [200, 100, 300],
    },
    rowFormatter: function (row) {
      if (row._row.data.registYn == "N") {
        row.getElement().style.background = "#FFC107";
      }
      row.getElement().style.margin = "8px 0px";
      // 각 row의 HTML 요소를 반환합니다
      return row.getElement();
    },
    autoResize: true,
    printRowRange: "selected", //
    maxHeight: props.height,
    debugInvalidComponentFuncs: false,
    pagination: props.pagination,
    paginationSizeSelector: props.pagination ? [50, 100, 150, true] : false,

    locale: true,
    headerVisible: true,

    langs: {
      "ko-kr": {
        pagination: {
          first: "<<",
          first_title: "",
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
    paginationCounter:function(pageSize, currentRow, currentPage, totalRows, totalPages){
        return "total : " + totalRows;
    }
  });
  tabulator.value.on("tableBuilt", function () {
    if (props.selectedFirstRow) {
      tabulator.value.selectRow(tabulator.value.getRows()[0]._row);
    }
  });
  tabulator.value.on("rowSelected", function (row) {
    if (props.keepData == true) {
      emit("update:rowTarget", row._row.data);
    } else {
      const rows = tabulator.value.getSelectedRows();
      const rowArr = [];
      rows.forEach((x) => {
        rowArr.push(x._row.data);
      });
      emit("update:rowTarget", rowArr);
    }
  });
  tabulator.value.on("rowDblClick", function (e, row) {
    if (props.tableName == "MachineSearch") {
      useDataStore.setMachineData(row._row.data);
      router.push("/machine/detail");
    }
  });

  tabulator.value.on("rowDeselected", function (row) {
    if (props.keepData == true) {
      emit("update:rowTarget", row._row.data);
    } else {
      const rows = tabulator.value.getSelectedRows();
      const rowArr = [];
      rows.forEach((x) => {
        rowArr.push(x._row.data);
      });
      emit("update:rowTarget", rowArr);
    }
    /* if (!!props.keepData == false) {
      emit("update:rowTarget", []);
    } */
  });

  window.addEventListener("resize", function () {
    tabulator.value.redraw(true);
  });
});
</script>
<template>
  <v-card class="customCard">
    <slot></slot>
    <div ref="table" :key="keys" :style="{ height: props.height }"></div>
  </v-card>
</template>
<style lang="scss">
.customCard {
  border-radius: 24px !important;
  box-shadow: -1px 0px 3px -2px var(--v-shadow-key-umbra-opacity, rgba(0, 0, 0, 0.2)),
    0px 1px 2px 0px var(--v-shadow-key-penumbra-opacity, rgba(0, 0, 0, 0.2)),
    0px 1px 5px 0px var(--v-shadow-key-penumbra-opacity, rgba(0, 0, 0, 0.2)) !important;
}
.tabulator * {
  color: v-bind(textColor);
}
.tabulator-row.tabulator-selectable:hover {
  background-color: v-bind(hoverColor) !important;
}
.tabulator-tooltip {
  color: white;
  background-color: rgb(100, 100, 100);
}
.tabulator {
  margin: 0;
  /* text-align: center; */
  font-size: 0.8rem;
  border-radius: 24px;
  border: none;
  background: transparent;
  /* border-radius: 8px; */
  background-color: none;
  // background-color: #eeeeee;
  /* border-radius: 0; */
  .tabulator-cell {
    padding-left: 14px;
  }
  .tabulator-header {
    // border: none;

    border-radius: 24px 24px 0 0;

    border-bottom: 1px solid rgba(0, 0, 0, 0.08);
    background-color: transparent;

    .tabulator-col {
      padding: 4px 0;
      background: transparent;
      .tabulator-col-title {
        padding-left: 3px;
      }
      .tabulator-col-group-cols {
        border: none !important;
        .tabulator-col {
          border: none !important;
        }
        .tabulator-col-resize-handle {
          border: none !important;
        }
      }
    }
  }
  .tabulator-tableholder {
    // overflow-x: auto;
    .tabulator-row {
      // border: none;
      box-shadow: rgb(0 0 0 / 15%) 0px 4px 10px -3px;
      background-color: transparent;
      padding: 6px 0px;
      border-radius: 12px;
      .v-card {
        background: initial !important;
      }
      &.tabulator-selected {
        *:not(.v-col, p, div) {
          color: white !important;
        }
        background-color: #0d99ff !important;
      }
      &.tabulator-row-odd {
        background-color: v-bind(oddColor);
      }
    }
  }
  .tabulator-footer {
    border: none;
    background-color: transparent;

    border-radius: 0 0 24px 24px;
  }
}
.tabulator-col-resize-handle {
  border-right: 1px solid #eee;
}

.tabulator .tabulator-header,
.tabulator .tabulator-header .tabulator-col,
.tabulator .tabulator-footer {
  font-weight: 400;
}

/* 페이지네이션 css */

/* 페이지네이션 가운데 정렬을 위한 css */
.tabulator-footer {
  position: relative;
  .tabulator-paginator {
    display: flex;
    align-items: center;
    justify-content: center;
    label {
      display: none;
    }
    .tabulator-page-size {
      position: absolute;
      right: 6px;
      border-radius: 6px;
    }
  }
}

/* 페이지네이션 보더 제거 및 간격조정 */

.tabulator .tabulator-footer .tabulator-page {
  border: none;
}

/* 페이지네이션 버튼 따로 조정 */
.tabulator-page[data-page="first"],
.tabulator-page[data-page="last"],
.tabulator-page[data-page="next"],
.tabulator-page[data-page="prev"] {
  // padding: 2px 5px;
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
//row count 부분 css
.tabulator-page-counter {
  font-size: 18px;
}
</style>
