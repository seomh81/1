<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import { GridContainer, Button, DataField } from "@/component/Template";
import { uiStore } from "@/store/UiStore";
const useUiStore = uiStore();

const data = reactive({
  fieldVal: "lorem ipsum",
  switchVal: true,
  sliderVal: 30,
  rangeSliderVal: [20, 80],
  rangeSliderVal2: [30, 50]
});

const rules = ref({
  required: (value) => {
    return !!value || "입력이 필요합니다.";
  },
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




    
    
    
    <h3 class="sample-title">Simple - Complex</h3>
        
    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: '100%',
          sliderVal: data.sliderVal,
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: '100%',
          sliderVal: data.rangeSliderVal,
          max: '100',
          min: '0',
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal = sliderVal)"
      />
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'file',
          label: '계약서 사본',
          width: '100%',
        }"
      />
      <DataField
        :option="{
          type: 'checkbox',
          label: '약관에 동의합니다',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>

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
      <DataField
        :option="{
          type: 'switch',
          label: '공개',
          switchValueData: data.switchVal,
        }"
        @update:switchVal="(switchVal) => (data.switchVal = switchVal)"
      />
      <DataField
        :option="{
          type: 'checkbox',
          label: '약관에 동의합니다',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>

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
      <DataField
        :option="{
          type: 'checkbox',
          label: '약관에 동의합니다',
        }"
      />
      <DataField
        :option="{
          type: 'switch',
          label: '공개',
        }"
      />
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
      <GridContainer colType="inline-right">
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
            errorMessage: '최소 길이 10',
          }"
        />
        <Button @update:click="buttonClick">승인</Button>
      </GridContainer>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
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
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
      <GridContainer colType="inline-left">
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
      </GridContainer>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'range-slider',
            label: '범위',
            width: '100%',
            sliderVal: data.rangeSliderVal,
            max: '100',
            min: '0',
          }"
          @update:sliderVal="(sliderVal) => (data.rangeSliderVal = sliderVal)"
        />
      </GridContainer>
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
      <GridContainer colType="inline-right">
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
            errorMessage: '최소 길이 10',
          }"
        />
        <Button @update:click="buttonClick">승인</Button>
      </GridContainer>
      <GridContainer colType="inline-left">
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
      </GridContainer>
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'switch',
            label: '공개',
          }"
        />
        <DataField
          :option="{
            type: 'slider',
            label: '갯수',
            width: '100%',
            sliderVal: data.sliderVal,
            step: 10,
          }"
          @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
        />
      </GridContainer>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
        <DataField
          :option="{
            type: 'radio',
            fieldItems: [
              { label: 'SK', value: '1' },
              { label: 'KT', value: '2' },
              { label: 'LGU+', value: '3' },
            ],
          }"
        />
      </GridContainer>
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
      <GridContainer colType="inline-right">
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
            errorMessage: '최소 길이 10',
          }"
        />
        <DataField
          :option="{
            type: 'switch',
            label: '공개',
            switchValueData: data.switchVal,
          }"
          @update:switchVal="(switchVal) => (data.switchVal = switchVal)"
        />
        <Button @update:click="buttonClick">승인</Button>
      </GridContainer>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'radio',
            fieldItems: [
              { label: 'SK', value: '1' },
              { label: 'KT', value: '2' },
              { label: 'LGU+', value: '3' },
            ],
          }"
        />
        <Button @update:click="buttonClick">승인</Button>
      </GridContainer>
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
      <GridContainer colType="inline-right">
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
            errorMessage: '최소 길이 10',
          }"
        />
        <Button @update:click="buttonClick">승인</Button>
      </GridContainer>
      <GridContainer colType="inline-right">
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">제출</Button>
        <Button @update:click="buttonClick">취소</Button>
        <Button @update:click="buttonClick">저장</Button>
        <Button @update:click="buttonClick">열기</Button>
      </GridContainer>
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
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
      </GridContainer>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'file',
            label: '계약서 사본',
            width: '100%',
          }"
        />
        <Button @update:click="buttonClick">승인</Button>
      </GridContainer>
    </GridContainer>


    <GridContainer colType="grid" colSpacing rowSpacing>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'range-slider',
            label: '범위',
            width: '100%',
            sliderVal: data.rangeSliderVal,
            max: '100',
            min: '0',
          }"
          @update:sliderVal="(sliderVal) => (data.rangeSliderVal = sliderVal)"
        />
      </GridContainer>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
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
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
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
      </GridContainer>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
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
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
      <GridContainer colType="inline">
        <Button @update:click="buttonClick">승인</Button>
        <DataField
          :option="{
            type: 'switch',
            label: '공개',
            errorMessage: '반드시',
          }"
        />
        <DataField
          :option="{
            type: 'switch',
            label: '공개',
          }"
        />
        <DataField
          :option="{
            type: 'switch',
            label: '공개',
            switchValueData: data.switchVal,
          }"
          @update:switchVal="(switchVal) => (data.switchVal = switchVal)"
        />
      </GridContainer>
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
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 300,
          fieldItems: [
            'California',
            'Colorado',
            'Florida',
            'Georgia',
            'Texas',
            'Wyoming',
          ],
          errorMessage: '최소 길이 10',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">제출</Button>
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
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 300,
          fieldItems: [
            'California',
            'Colorado',
            'Florida',
            'Georgia',
            'Texas',
            'Wyoming',
          ],
          errorMessage: '최소 길이 10',
        }"
      />
      <DataField
        :option="{
          type: 'file',
          label: '계약서 사본',
          width: '100%',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: '100%',
          sliderVal: data.sliderVal,
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
      <DataField
        :option="{
          type: 'file',
          label: '계약서 사본',
          width: '100%',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'file',
          label: '계약서 사본',
          width: '100%',
        }"
      />
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: 600,
          sliderVal: data.rangeSliderVal,
          max: '100',
          min: '0',
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 200,
          fieldItems: [
            'California',
            'Colorado',
            'Florida',
            'Georgia',
            'Texas',
            'Wyoming',
          ],
          errorMessage: '최소 길이 10',
        }"
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
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 300,
          fieldItems: [
            'California',
            'Colorado',
            'Florida',
            'Georgia',
            'Texas',
            'Wyoming',
          ],
          errorMessage: '최소 길이 10',
        }"
      />
      <DataField
        :option="{
          type: 'select',
          label: '번호',
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
      <DataField
        :option="{
          type: 'switch',
          label: '공개',
          errorMessage: '반드시',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'input',
          label: '연락처',
          width: '100%',
          fieldVal: data.fieldVal,
          errorMessage: '최소 길이 10',
        }"
        @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
      />
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: 400,
          sliderVal: data.rangeSliderVal,
          max: '100',
          min: '0',
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal = sliderVal)"
      />
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'input',
          label: '연락처',
          width: '100%',
          fieldVal: data.fieldVal,
          errorMessage: '최소 길이 10',
        }"
        @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
      />
      <DataField
        :option="{
          type: 'input',
          label: '결과',
          width: 150,
          fieldVal: data.fieldVal,
        }"
        @update:fieldVal="(fieldVal) => (data.fieldVal = fieldVal)"
      />
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>
  

    


    <h3 class="sample-title">Simple - 가변 너비 케이스</h3>
    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'textarea',
          label: '상세내용',
          errorMessage: '내용을 입력해야 합니다',
        }"
      />
      <DataField
        :option="{
          type: 'textarea',
          label: '상세내용',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="grid" colSpacing rowSpacing>
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'textarea',
            label: '상세내용',
            errorMessage: '내용을 입력해야 합니다',
          }"
          :rules="[rules.required]"
        />
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">추가</Button>
      </GridContainer>
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
    </GridContainer>

    <GridContainer colType="grid-stretch-first">
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'checkbox',
            label: '약관에 동의합니다',
          }"
        />
        <DataField
          :option="{
            type: 'textarea',
            label: '상세내용',
            errorMessage: '내용을 입력해야 합니다',
          }"
          :rules="[rules.required]"
        />
      </GridContainer>
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="grid-stretch-first" colSpacing>
      <DataField
        :option="{
          type: 'textarea',
          label: '상세내용',
          errorMessage: '내용을 입력해야 합니다',
        }"
      />
      <GridContainer colType="inline-right">
        <Button @update:click="buttonClick">승인</Button>
        <Button @update:click="buttonClick">저장</Button>
        <Button @update:click="buttonClick">취소</Button>
      </GridContainer>
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'textarea',
          label: '상세내용',
          errorMessage: '내용을 입력해야 합니다',
        }"
      />
      <DataField
        :option="{
          type: 'textarea',
          label: '상세내용',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>


    
    
    
    <h3 class="sample-title">Simple - Stretch</h3>
    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'textarea',
          label: '상세내용',
          width: '100%',
          errorMessage: '내용을 입력해야 합니다',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'file',
          label: '계약서 사본',
          width: '100%',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: '100%',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
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
          errorMessage: '최소 길이 10',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
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
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: '100%',
          errorMessage: '반드시 입력해야 합니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: '100%',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: '100%',
          sliderVal: data.sliderVal,
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: '100%',
          sliderVal: data.sliderVal,
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: '100%',
          sliderVal: data.rangeSliderVal,
          max: '100',
          min: '0',
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: '100%',
          sliderVal: data.rangeSliderVal2,
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal2 = sliderVal)"
      />
    </GridContainer>

    
    
    
    <h3 class="sample-title">Simple - Right</h3>
    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'textarea',
          label: '상세내용',
          width: 300,
          errorMessage: '내용을 입력해야 합니다',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'file',
          label: '계약서 사본',
          width: 300,
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: 300,
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 300,
          fieldItems: [
            'California',
            'Colorado',
            'Florida',
            'Georgia',
            'Texas',
            'Wyoming',
          ],
          errorMessage: '최소 길이 10',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 300,
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
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: 300,
          errorMessage: '반드시 입력해야 합니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: 300,
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: 300,
          sliderVal: data.sliderVal,
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: 300,
          sliderVal: data.sliderVal,
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: 300,
          sliderVal: data.rangeSliderVal,
          max: '100',
          min: '0',
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: 300,
          sliderVal: data.rangeSliderVal2,
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal2 = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'radio',
          fieldItems: [
            { label: 'SK', value: '1' },
            { label: 'KT', value: '2' },
            { label: 'LGU+', value: '3' },
          ],
          errorMessage: '반드시 선택해야 합니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'radio',
          fieldItems: [
            { label: 'SK', value: '1' },
            { label: 'KT', value: '2' },
            { label: 'LGU+', value: '3' },
          ],
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'switch',
          label: '공개',
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'switch',
          label: '공개',
          switchValueData: data.switchVal,
        }"
        @update:switchVal="(switchVal) => (data.switchVal = switchVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'checkbox',
          label: '약관에 동의합니다',
          errorMessage: '동의해야 진행할 수 있습니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-right">
      <DataField
        :option="{
          type: 'checkbox',
          label: '약관에 동의합니다',
        }"
      />
    </GridContainer>

    
    
    
    <h3 class="sample-title">Simple - Center</h3>
    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'textarea',
          label: '상세내용',
          width: 300,
          errorMessage: '내용을 입력해야 합니다',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'file',
          label: '계약서 사본',
          width: 300,
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: 300,
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 300,
          fieldItems: [
            'California',
            'Colorado',
            'Florida',
            'Georgia',
            'Texas',
            'Wyoming',
          ],
          errorMessage: '최소 길이 10',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 300,
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
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: 300,
          errorMessage: '반드시 입력해야 합니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: 300,
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: 300,
          sliderVal: data.sliderVal,
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: 300,
          sliderVal: data.sliderVal,
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: 300,
          sliderVal: data.rangeSliderVal,
          max: '100',
          min: '0',
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: 300,
          sliderVal: data.rangeSliderVal2,
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal2 = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'radio',
          fieldItems: [
            { label: 'SK', value: '1' },
            { label: 'KT', value: '2' },
            { label: 'LGU+', value: '3' },
          ],
          errorMessage: '반드시 선택해야 합니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'radio',
          fieldItems: [
            { label: 'SK', value: '1' },
            { label: 'KT', value: '2' },
            { label: 'LGU+', value: '3' },
          ],
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'switch',
          label: '공개',
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'switch',
          label: '공개',
          switchValueData: data.switchVal,
        }"
        @update:switchVal="(switchVal) => (data.switchVal = switchVal)"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'checkbox',
          label: '약관에 동의합니다',
          errorMessage: '동의해야 진행할 수 있습니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline-center">
      <DataField
        :option="{
          type: 'checkbox',
          label: '약관에 동의합니다',
        }"
      />
    </GridContainer>

    
    
    
    <h3 class="sample-title">Simple - Left</h3>
    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'textarea',
          label: '상세내용',
          width: 300,
          errorMessage: '내용을 입력해야 합니다',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'file',
          label: '계약서 사본',
          width: 300,
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: 300,
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
      <Button @update:click="buttonClick">제출</Button>
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 300,
          fieldItems: [
            'California',
            'Colorado',
            'Florida',
            'Georgia',
            'Texas',
            'Wyoming',
          ],
          errorMessage: '최소 길이 10',
        }"
      />
      <Button @update:click="buttonClick">승인</Button>
      <Button @update:click="buttonClick">추가</Button>
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'select',
          label: '번호',
          width: 300,
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
      <Button @update:click="buttonClick">승인</Button>
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: 300,
          errorMessage: '반드시 입력해야 합니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'input',
          label: '번호',
          width: 300,
        }"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: 300,
          sliderVal: data.sliderVal,
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: 300,
          sliderVal: data.sliderVal,
          thumbLabel: 'always',
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'slider',
          label: '갯수',
          width: 300,
          sliderVal: data.sliderVal,
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.sliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: 300,
          sliderVal: data.rangeSliderVal,
          max: '100',
          min: '0',
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'range-slider',
          label: '범위',
          width: 300,
          sliderVal: data.rangeSliderVal2,
          step: 10,
        }"
        @update:sliderVal="(sliderVal) => (data.rangeSliderVal2 = sliderVal)"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'radio',
          fieldItems: [
            { label: 'SK', value: '1' },
            { label: 'KT', value: '2' },
            { label: 'LGU+', value: '3' },
          ],
          errorMessage: '반드시 선택해야 합니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'radio',
          fieldItems: [
            { label: 'SK', value: '1' },
            { label: 'KT', value: '2' },
            { label: 'LGU+', value: '3' },
          ],
        }"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'switch',
          label: '공개',
          errorMessage: '선택해야 진행할 수 있습니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'switch',
          label: '공개',
          switchValueData: data.switchVal,
        }"
        @update:switchVal="(switchVal) => (data.switchVal = switchVal)"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'checkbox',
          label: '약관에 동의합니다',
          errorMessage: '동의해야 진행할 수 있습니다',
        }"
      />
    </GridContainer>

    <GridContainer colType="inline">
      <DataField
        :option="{
          type: 'checkbox',
          label: '약관에 동의합니다',
        }"
      />
    </GridContainer>
  </v-container>
</template>

<style scoped></style>
