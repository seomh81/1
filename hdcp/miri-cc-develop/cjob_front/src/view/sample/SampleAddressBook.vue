<script setup>
import { ref, reactive, inject, computed, onMounted, watch } from "vue";
import SampleMenuView from "./SampleMenuView.vue";
import { GridContainer, AddressBook } from "@/component/Template";
import { connectionStore } from "@/store/ConnectionStore";
const useConnectionStore = connectionStore();

const data = reactive({
  items: [
  { type: 'subheader', title: '생산관리부' },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
      title: 'Brunch this weekend?',
      subtitle: `<span class="text-primary">Ali Connors</span> &mdash; I'll be in your neighborhood doing errands this weekend. Do you want to hang out?`,
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/5.jpg',
      title: 'Recipe to try',
      subtitle: '<span class="text-primary">Britta Holt</span> &mdash; We should eat this: Grate, Squash and tomatillo Tacos.',
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
      title: 'Summer BBQ',
      subtitle: `<span class="text-primary">to Alex, Scott, Jennifer</span> &mdash; Wish I could come, but I'm out of town this weekend.`,
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
      title: 'Oui oui',
      subtitle: '<span class="text-primary">Sandra Adams</span> &mdash; Do you have Paris recommendations? Have you ever been?',
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/4.jpg',
      title: 'Birthday gift',
      subtitle: '<span class="text-primary">Trevor Hansen</span> &mdash; Have any ideas about what we should get Heidi for her birthday?',
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/5.jpg',
      title: 'Recipe to try',
      subtitle: '<span class="text-primary">Britta Holt</span> &mdash; We should eat this: Grate, Squash and tomatillo Tacos.',
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
      title: 'Summer BBQ',
      subtitle: `<span class="text-primary">to Alex, Scott, Jennifer</span> &mdash; Wish I could come, but I'm out of town this weekend.`,
    },
    { type: 'subheader', title: '인사부' },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
      title: 'Brunch this weekend?',
      subtitle: `<span class="text-primary">Ali Connors</span> &mdash; I'll be in your neighborhood doing errands this weekend. Do you want to hang out?`,
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
      title: 'Oui oui',
      subtitle: '<span class="text-primary">Sandra Adams</span> &mdash; Do you have Paris recommendations? Have you ever been?',
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
      title: 'Summer BBQ',
      subtitle: `<span class="text-primary">to Alex, Scott, Jennifer</span> &mdash; Wish I could come, but I'm out of town this weekend.`,
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/5.jpg',
      title: 'Recipe to try',
      subtitle: '<span class="text-primary">Britta Holt</span> &mdash; We should eat this: Grate, Squash and tomatillo Tacos.',
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
      title: 'Oui oui',
      subtitle: '<span class="text-primary">Sandra Adams</span> &mdash; Do you have Paris recommendations? Have you ever been?',
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/4.jpg',
      title: 'Birthday gift',
      subtitle: '<span class="text-primary">Trevor Hansen</span> &mdash; Have any ideas about what we should get Heidi for her birthday?',
    },
    { type: 'divider', inset: true },
    {
      prependAvatar: 'https://cdn.vuetifyjs.com/images/lists/5.jpg',
      title: 'Recipe to try',
      subtitle: '<span class="text-primary">Britta Holt</span> &mdash; We should eat this: Grate, Squash and tomatillo Tacos.',
    },
  ],
});
</script>

<template>
  <v-container class="sample-test-container">    
    <SampleMenuView />
      
    <h3 class="sample-title">주소록</h3>
    <GridContainer colType="inline-center">
      <AddressBook :items="data.items" width="600px" />
    </GridContainer>

  </v-container>
</template>

<style>

</style>
<style scoped>
h2 {
  display: block;
  margin: 0;
  padding: 3px 0;
  font-size: 1rem;
  line-height: 1.2em;
}
</style>
