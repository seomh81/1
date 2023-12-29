<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import { Table, Thead, Tbody, Tr, Th, Td } from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
const useConnectionStore = connectionStore();

</script>

<template>
  <v-container class="sample-test-container">    
    <SampleMenuView />
      
    <h3 class="sample-title">스크롤 + 헤더고정형</h3>
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


    <h3 class="sample-title">복합형</h3>
    <Table class="v-simple-table" :colWidth="[100,false,100,false]" >
      <Tbody>
        <Tr>
          <Th>TH11</Th>
          <Td>TD12</Td>
          <Th>TH11</Th>
          <Td>TD12</Td>
        </Tr>
        <Tr>
          <Th>TH11</Th>
          <Td>TD12</Td>
          <Th>TH11</Th>
          <Td>TD12</Td>
        </Tr>
        <Tr>
          <Th>TH11</Th>
          <Td colSpan="3">TD12</Td>
        </Tr>
        <Tr>
          <Th>TH11</Th>
          <Td colSpan="3">TD12</Td>
        </Tr>
        <Tr>
          <Td colSpan="4">TD52</Td>
        </Tr>
      </Tbody>
    </Table>


    <h3 class="sample-title">hover, 세로형</h3>
    <Table class="v-simple-table" :colWidth="[100,false]" rowHoverEffect>
      <Tbody>
        <Tr>
          <Th>TH11</Th>
          <Td>TD12</Td>
        </Tr>
        <Tr>
          <Th alignRight>TH21</Th>
          <Td alignLeft>TD22</Td>
        </Tr>
        <Tr>
          <Th alignLeft>TH31</Th>
          <Td alignLeft>TD32</Td>
        </Tr>
        <Tr>
          <Th>TH41</Th>
          <Td alignLeft>TD42</Td>
        </Tr>
        <Tr>
          <Th>TH51</Th>
          <Td alignLeft>TD52</Td>
        </Tr>
      </Tbody>
    </Table>


    <h3 class="sample-title">hover</h3>
    <Table class="v-simple-table" :colWidth="[200,false,false]" rowHoverEffect>
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
          <Td>TD53</Td>
        </Tr>
      </Tbody>
    </Table>


    <h3 class="sample-title">기본 colWidth</h3>
    <Table class="v-simple-table" :colWidth="[false,100,false]" >
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
          <Td>TD53</Td>
        </Tr>
      </Tbody>
    </Table>


    <h3 class="sample-title">기본</h3>
    <Table class="v-simple-table">
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
          <Td>TD53</Td>
        </Tr>
      </Tbody>
    </Table>

  </v-container>
</template>

<style scoped>
h2 {
  display: block;
  margin: 0;
  padding: 3px 0;
  font-size: 1rem;
  line-height: 1.2em;
}
</style>
