<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import { GridContainer, Button, DataField } from "@/component/Template";
import { uiStore } from "@/store/UiStore";
const useUiStore = uiStore();

const data = reactive({
  fieldVal: 1234,
});
watch(
  () => data.fieldVal,
  () => console.log(data)
);
const buttonClick = (e) => {
  console.log(e);
};
</script>

<template>
  <v-container class="sample-test-container">
    <SampleMenuView />    
    
    <GridContainer colType="inline-right" rowSpacing>
      <v-btn
        :color="useUiStore.testBorder ? 'indigo' : 'primary'"
        @click.prevent="useUiStore.toggleTestBorder()"
      >테두리</v-btn>
    </GridContainer>

    
    <div>

      <h3 class="sample-title">colSpacing - grid-stretch-last</h3>
      <GridContainer colType="grid-stretch-last" colSpacing>
        <div style="height:100px;width:200px;background:yellowgreen;">A-BLOCK</div>
        <div style="height:100px;width:100%;background:orange;">B-BLOCK</div>
      </GridContainer>

      <h3 class="sample-title">colSpacing - grid-stretch-center</h3>
      <GridContainer colType="grid-stretch-center" colSpacing>
        <div style="height:100px;width:150px;background:yellowgreen;">A-BLOCK</div>
        <div style="height:100px;width:100%;background:orange;">B-BLOCK</div>
        <div style="height:100px;width:150px;background:red;">C-BLOCK</div>
      </GridContainer>

      <h3 class="sample-title">colSpacing - grid-stretch-first</h3>
      <GridContainer colType="grid-stretch-first" colSpacing>
        <div style="height:100px;width:100%;background:yellowgreen;">A-BLOCK</div>
        <div style="height:100px;width:200px;background:orange;">B-BLOCK</div>
      </GridContainer>

      <h3 class="sample-title">grid-stretch-center</h3>
      <GridContainer colType="grid-stretch-center" colSpacing>
        <Button @update:click="buttonClick">승인</Button>
        <GridContainer colType="inline-center">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">승인</Button>
        </GridContainer>
        <Button @update:click="buttonClick">삭제</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-center" colSpacing>
        <Button @update:click="buttonClick">승인</Button>
        <GridContainer colType="inline">
          <DataField
            :option="{
              type: 'input',
              label: '결과',
              width: '100%',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
          <Button @update:click="buttonClick">승인</Button>
        </GridContainer>
        <Button @update:click="buttonClick">삭제</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-center" colSpacing>
        <Button @update:click="buttonClick">승인</Button>
        <GridContainer colType="inline-right">
          <DataField
            :option="{
              type: 'input',
              label: '결과',
              width: '100%',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">추가</Button>
        </GridContainer>
        <Button @update:click="buttonClick">삭제</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-center" colSpacing>
        <Button @update:click="buttonClick">승인</Button>
        <GridContainer colType="inline-right">
          <DataField
            :option="{
              type: 'input',
              label: '결과',
              width: '100%',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">추가</Button>
        </GridContainer>
        <GridContainer colType="inline-right">
          <Button @update:click="buttonClick">삭제</Button>
          <Button @update:click="buttonClick">승인</Button>
        </GridContainer>
      </GridContainer>

      <GridContainer colType="grid-stretch-center" colSpacing>
        <Button @update:click="buttonClick">승인</Button>
        <DataField
          :option="{
            type: 'input',
            label: '결과',
            width: '100%',
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
        <Button @update:click="buttonClick">삭제</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-center" colSpacing>
        <Button @update:click="buttonClick">승인</Button>
        <DataField
          :option="{
            type: 'select',
            label: '번호',
            width: '100%',
            fieldItems: [
              'California',
              'Colorado',
              'Florida',
              'Georgia',
              'Texas',
              'Wyoming',
            ],
          }"
        />
        <Button @update:click="buttonClick">삭제</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-center">
        <Button @update:click="buttonClick">승인</Button>
        <DataField
          :option="{
            type: 'input',
            label: '결과',
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
        <Button @update:click="buttonClick">삭제</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-center">
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
        <Button @update:click="buttonClick">삭제</Button>
      </GridContainer>

      <h3 class="sample-title">grid-stretch-last</h3>
      <GridContainer colType="grid-stretch-last" colSpacing>
        <DataField
          :option="{
            type: 'textarea',
            label: '상세내용',
            width: 300,
            errorMessage: '내용을 입력해야 합니다',
          }"
        />
        <GridContainer colType="inline-left">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
      </GridContainer>

      <GridContainer colType="grid-stretch-last" colSpacing>
        <DataField
          :option="{
            type: 'textarea',
            label: '상세내용',
            width: 300,
            errorMessage: '내용을 입력해야 합니다',
          }"
        />
        <GridContainer colType="inline-center">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
      </GridContainer>

      <GridContainer colType="grid-stretch-last" colSpacing>
        <DataField
          :option="{
            type: 'textarea',
            label: '상세내용',
            width: 300,
            errorMessage: '내용을 입력해야 합니다',
          }"
        />
        <GridContainer colType="inline-right">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
      </GridContainer>

      <GridContainer colType="grid-stretch-last" colSpacing>
        <GridContainer colType="inline-right">
          <DataField
            :option="{
              type: 'textarea',
              label: '상세내용',
              width: 300,
              errorMessage: '내용을 입력해야 합니다',
            }"
          />
        </GridContainer>
        <GridContainer colType="inline-right">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
      </GridContainer>

      <GridContainer colType="grid-stretch-last" colSpacing>
        <GridContainer colType="inline-left">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
        <DataField
          :option="{
            type: 'textarea',
            label: '상세내용',
            width: '100%',
            errorMessage: '내용을 입력해야 합니다',
          }"
        />
      </GridContainer>

      <GridContainer colType="grid-stretch-last" colSpacing>
        <GridContainer colType="inline-left">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
        <DataField
          :option="{
            type: 'textarea',
            label: '상세내용',
            width: '100%',
            errorMessage: '내용을 입력해야 합니다',
          }"
        />
      </GridContainer>

      <GridContainer colType="grid-stretch-last">
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
        <Button @update:click="buttonClick">삭제</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-last">
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-last">
        <Button @update:click="buttonClick">승인</Button>
        <DataField
          :option="{
            type: 'input',
            label: '번호',
            width: '100%',
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
      </GridContainer>
      
      <GridContainer colType="grid-stretch-last">
        <Button @update:click="buttonClick">승인</Button>
        <DataField
          :option="{
            type: 'select',
            label: '번호',
            width: '100%',
            fieldItems: [
              'California',
              'Colorado',
              'Florida',
              'Georgia',
              'Texas',
              'Wyoming',
            ],
          }"
        />
      </GridContainer>

      <h3 class="sample-title">grid-stretch-first</h3>
      <GridContainer colType="grid-stretch-first">
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-first">
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
        <Button @update:click="buttonClick">저장</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-first">
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
        <DataField
          :option="{
            type: 'input',
            label: '번호',
            width: '100%',
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
        <Button @update:click="buttonClick">저장</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-first">
        <DataField
          :option="{
            type: 'input',
            label: '번호',
            width: '100%',
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
        <Button @update:click="buttonClick">저장</Button>
      </GridContainer>

      <GridContainer colType="grid-stretch-first">
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
        <Button @update:click="buttonClick">저장</Button>
        <DataField
          :option="{
            type: 'input',
            label: '번호',
            width: '100%',
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
      </GridContainer>

      <GridContainer colType="grid-stretch-first" colSpacing>
        <GridContainer colType="inline-right">
          <DataField
            :option="{
              type: 'input',
              label: '번호',
              width: '100%',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
          <DataField
            :option="{
              type: 'input',
              label: '번호',
              width: '100%',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
        </GridContainer>
        <GridContainer colType="inline-right">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
      </GridContainer>

      <GridContainer colType="grid-stretch-first" colSpacing>
        <GridContainer colType="inline-right">
          <DataField
            :option="{
              type: 'input',
              label: '번호',
              width: '100%',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
          <DataField
            :option="{
              type: 'input',
              label: '번호',
              width: 200,
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
        </GridContainer>
        <GridContainer colType="inline-right">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
      </GridContainer>

      <GridContainer colType="grid-stretch-first" colSpacing>
        <GridContainer colType="inline-right">
          <DataField
            :option="{
              type: 'input',
              label: '번호',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
          <DataField
            :option="{
              type: 'input',
              label: '번호',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
        </GridContainer>
        <GridContainer colType="inline-right">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
      </GridContainer>

      <GridContainer colType="grid-stretch-first" colSpacing>
        <GridContainer colType="inline-right">
          <DataField
            :option="{
              type: 'input',
              label: '번호',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
          <DataField
            :option="{
              type: 'input',
              label: '번호',
              width: '100%',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
        </GridContainer>
        <GridContainer colType="inline-right">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
      </GridContainer>

      <GridContainer colType="grid-stretch-first" colSpacing>
        <GridContainer colType="inline-right">
          <DataField
            :option="{
              type: 'input',
              label: '번호',
              width: '100%',
              fieldVal: data.fieldVal,
            }"
            @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
          />
        </GridContainer>
        <GridContainer colType="inline-right">
          <Button @update:click="buttonClick">승인</Button>
          <Button @update:click="buttonClick">저장</Button>
          <Button @update:click="buttonClick">저장</Button>
        </GridContainer>
      </GridContainer>

      <h3 class="sample-title">grid</h3>
      <GridContainer colType="grid" rowSpacing>
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
        <Button @update:click="buttonClick">취소</Button>
        <Button @update:click="buttonClick">삭제</Button>
      </GridContainer>

      <GridContainer colType="grid" rowSpacing>
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
        <Button @update:click="buttonClick">취소</Button>
      </GridContainer>

      <GridContainer colType="grid" rowSpacing>
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
      </GridContainer>

      <h3 class="sample-title">inline</h3>
      <GridContainer colType="inline">
        <DataField
          :option="{
            type: 'input',
            label: '번호',
            width: 300,
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">취소</Button>
        <Button @update:click="buttonClick">제출</Button>
      </GridContainer>

      <h3 class="sample-title">inline-center</h3>
      <GridContainer colType="inline-center">
        <DataField
          :option="{
            type: 'input',
            label: '번호',
            width: 300,
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">취소</Button>
      </GridContainer>

      <h3 class="sample-title">inline-right</h3>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'input',
            label: '번호',
            width: 300,
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">취소</Button>
      </GridContainer>

      <GridContainer colType="inline-right">
        <Button @update:click="buttonClick">승인</Button>
        <DataField
          :option="{
            type: 'input',
            label: '번호',
            width: '100%',
            fieldVal: data.fieldVal,
          }"
          @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
        />
        <Button @update:click="buttonClick">취소</Button>
        <Button @update:click="buttonClick">삭제</Button>
        <Button @update:click="buttonClick">등록</Button>
      </GridContainer>

    </div>
  </v-container>
</template>

<style scoped>
</style>
