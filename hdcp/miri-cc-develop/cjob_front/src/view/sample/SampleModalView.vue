<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "@/view/sample/SampleMenuView.vue";
import { TextBox, DataField, Button, GridContainer, ModalContainer, Table, Thead, Tbody, Tr, Th, Td } from "@/component/Template";
import { uiStore } from "@/store/UiStore";
import { v4 as uuidv4 } from "uuid";

const useUiStore = uiStore();
const modal1 = uuidv4();
const modal2 = uuidv4();
const modal3 = uuidv4();
const modal4 = uuidv4();
const modal5 = uuidv4();
const modal6 = uuidv4();
</script>
<template>
  <v-container class="sample-test-container">
    <SampleMenuView />
    
    <h3 class="sample-title">dialog</h3>
    <GridContainer colType="inline" rowSpacing>
      <Button @click="useUiStore.openModalEvent(modal1)">예아니오</Button>
    </GridContainer>

    <h3 class="sample-title">table</h3>
    <GridContainer colType="inline" rowSpacing>
      <Button @click="useUiStore.openModalEvent(modal2)">테이블</Button>
    </GridContainer>

    <h3 class="sample-title">dataField</h3>
    <GridContainer colType="inline" rowSpacing>
      <Button @click="useUiStore.openModalEvent(modal3)">입력폼1</Button>
      <Button @click="useUiStore.openModalEvent(modal4)">입력폼2</Button>
      <Button @click="useUiStore.openModalEvent(modal5)">입력폼3</Button>
      <Button @click="useUiStore.openModalEvent(modal6)">입력폼4</Button>
    </GridContainer>

    <ModalContainer type="dialog" text="모달6" :id="modal6">
      <GridContainer colType="inline">
        <DataField
          :option="{
            type: 'input',
            label: '현재언어',
            width: '100%',
          }"
        />
      </GridContainer>
      <GridContainer colType="inline">
        <DataField
          :option="{
            type: 'select',
            label: '변경언어',
            width: '100%',
            fieldItems: [
              '한국어',
              'English',
              'Русский',
              '中文',
              'Français',
              'Español',
            ],
          }"
        />
      </GridContainer>
      <GridContainer colType="inline" rowSpacing>
        <DataField
          :option="{
            type: 'checkbox',
            label: '로그인시 기본 언어로 설정',
          }"
        />
      </GridContainer>
      <GridContainer colType="inline-right">
        <Button @click="useUiStore.closeModal">등록</Button>
        <Button @click="useUiStore.closeModal">닫기</Button>
      </GridContainer>
    </ModalContainer>

    <ModalContainer type="dialog" text="모달5" :id="modal5">
      <GridContainer colType="inline-right">
        <DataField
          :option="{
            type: 'input',
            label: '프로젝트번호',
            width: '100%',
          }"
        />
        <Button @update:click="buttonClick">조회</Button>
      </GridContainer>
      <Table class="v-simple-table" :colWidth="[100,false,200]">
        <Thead>
          <Tr>
            <Th>TH1</Th>
            <Th>TH2</Th>
            <Th>TH3</Th>
          </Tr>
        </Thead>
        <Tbody>
          <Tr>
            <Td>TD11</Td>
            <Td>TD12</Td>
            <Td>TD13</Td>
          </Tr>
          <Tr>
            <Td alignRight>TD21</Td>
            <Td>TD22</Td>
            <Td>TD23</Td>
          </Tr>
          <Tr>
            <Td alignLeft>TD31</Td>
            <Td alignRight>TD32</Td>
            <Td>TD33</Td>
          </Tr>
          <Tr>
            <Td>TD41</Td>
            <Td alignRight>TD42</Td>
            <Td>TD43</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>TD52</Td>
            <Td>ADFBF</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>TD52</Td>
            <Td>QWERQWER</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>DFDSAF</Td>
            <Td>1234123412</Td>
          </Tr>
        </Tbody>
      </Table>
      <GridContainer colType="inline-right">
        <Button @click="useUiStore.closeModal">등록</Button>
        <Button @click="useUiStore.closeModal">닫기</Button>
      </GridContainer>
    </ModalContainer>

    <ModalContainer type="dialog" text="모달1" :id="modal1">
      <!-- <v-card-title class="px-6"> masterKey이름 </v-card-title> -->
      <GridContainer colType="inline-center" rowSpacing>
        <TextBox type="medium bold">정말 삭제하시겠습니까?</TextBox>
      </GridContainer>
      <GridContainer colType="inline-center">
        <Button @click="useUiStore.closeModal">등록</Button>
        <Button @click="useUiStore.closeModal">취소</Button>
      </GridContainer>
    </ModalContainer>

    <ModalContainer type="dialog" text="모달3" :id="modal3">
      <GridContainer colType="inline-center" rowSpacing>
        <DataField
          :option="{
            type: 'radio',
            fieldItems: [
              {label: 'SK', value: '1'},
              {label: 'KT', value: '2'},
              {label: 'LGU+', value: '3'}
            ],
          }"
        />
      </GridContainer>
      <GridContainer colType="inline-center">
        <Button @click="useUiStore.closeModal">등록</Button>
        <Button @click="useUiStore.closeModal">닫기</Button>
      </GridContainer>
    </ModalContainer>

    <ModalContainer type="dialog" text="모달4" :id="modal4">
      <GridContainer colType="inline-center">
        <DataField
          :option="{
            type: 'input',
            label: '연락처',
            width: '100%',
          }"
        />
        <Button @update:click="buttonClick">승인</Button>
      </GridContainer>
      <GridContainer colType="inline-center">
        <DataField
          :option="{
            type: 'input',
            label: '연락처',
            width: '100%',
          }"
        />
      </GridContainer>
      <GridContainer colType="inline-center">
        <DataField
          :option="{
            type: 'input',
            label: '연락처',
            width: '100%',
          }"
        />
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
      <GridContainer colType="inline-center" rowSpacing>
        <DataField
          :option="{
            type: 'radio',
            fieldItems: [
              {label: 'SK', value: '1'},
              {label: 'KT', value: '2'},
              {label: 'LGU+', value: '3'}
            ],
          }"
        />
      </GridContainer>
      <GridContainer colType="inline-center">
        <Button @click="useUiStore.closeModal">등록</Button>
        <Button @click="useUiStore.closeModal">닫기</Button>
      </GridContainer>
    </ModalContainer>

    <ModalContainer type="dialog" text="모달2" :id="modal2">
        <Table class="v-simple-table" fixedHeaderWithTableHeight="380px" :colWidth="[100,false,200]">
        <Thead>
          <Tr>
            <Th>TH1</Th>
            <Th>TH2</Th>
            <Th>TH3</Th>
          </Tr>
        </Thead>
        <Tbody>
          <Tr>
            <Td>TD11</Td>
            <Td>TD12</Td>
            <Td>TD13</Td>
          </Tr>
          <Tr>
            <Td alignRight>TD21</Td>
            <Td>TD22</Td>
            <Td>TD23</Td>
          </Tr>
          <Tr>
            <Td alignLeft>TD31</Td>
            <Td alignRight>TD32</Td>
            <Td>TD33</Td>
          </Tr>
          <Tr>
            <Td>TD41</Td>
            <Td alignRight>TD42</Td>
            <Td>TD43</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>TD52</Td>
            <Td>ADFBF</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>TD52</Td>
            <Td>QWERQWER</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>DFDSAF</Td>
            <Td>1234123412</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>ASDFASDF</Td>
            <Td>IKLYUKUTYK</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>234G5GF</Td>
            <Td>FBSDFBDFS654TR</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>FDSGSDFG52UW456YJRHT</Td>
            <Td>CMHGJR</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>4GH43H435H</Td>
            <Td>SDBSDFBF</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>4G4B5J567J</Td>
            <Td>FFF</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>TD52</Td>
            <Td>587456R</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>43T34ERGFDB</Td>
            <Td>FBGDFGSDF</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>123EDEC</Td>
            <Td>9LKIUJH</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>KUJSDHFLIKUA</Td>
            <Td>SDFGSFD</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>54TG45YG</Td>
            <Td>SDFGDFG</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>8999</Td>
            <Td>2EWFC</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>VKJEBNVKD</Td>
            <Td>4RF34R</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>5Y5Y5Y</Td>
            <Td>FBFBF</Td>
          </Tr>
          <Tr>
            <Td>TD41</Td>
            <Td alignRight>TD42</Td>
            <Td>TD43</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>TD52</Td>
            <Td>ADFBF</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>TD52</Td>
            <Td>QWERQWER</Td>
          </Tr>
          <Tr>
            <Td>TD41</Td>
            <Td alignRight>TD42</Td>
            <Td>TD43</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>TD52</Td>
            <Td>ADFBF</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>TD52</Td>
            <Td>QWERQWER</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>DFDSAF</Td>
            <Td>1234123412</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>ASDFASDF</Td>
            <Td>IKLYUKUTYK</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>234G5GF</Td>
            <Td>FBSDFBDFS654TR</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>DFDSAF</Td>
            <Td>1234123412</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td alignRight>ASDFASDF</Td>
            <Td>IKLYUKUTYK</Td>
          </Tr>
          <Tr>
            <Td>TD51</Td>
            <Td>234G5GF</Td>
            <Td>FBSDFBDFS654TR</Td>
          </Tr>
        </Tbody>
      </Table>
      <GridContainer colType="inline-center">
        <Button @click="useUiStore.closeModal">등록</Button>
        <Button @click="useUiStore.closeModal">취소</Button>
      </GridContainer>
    </ModalContainer>

  </v-container>
</template>

<style scope>
</style>
