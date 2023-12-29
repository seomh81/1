<script setup>
const props = defineProps(["loadingData", "height", "data", "type", "padding"]);
</script>
<template>
  <v-card
    class="customCard overflow-y-auto"
    :height="height"
    v-if="!!props.hideCard == false"
  >
    <transition appear name="fade">
      <v-skeleton-loader
        :class="loadingData ? 'pa-4' : ''"
        transition="fade"
        :height="height"
        :type="
          props.type ??
          'heading,table-row,table-row,table-row,table-row, table-tfoot,actions'
        "
        :loading="loadingData"
      >
        <v-responsive :class="props.padding ? 'pa-5' : ''">
          <slot></slot>
        </v-responsive>
      </v-skeleton-loader>
    </transition>
  </v-card>
</template>
<style lang="scss">
.v-skeleton-loader {
  cursor: default;
}
.v-skeleton-loader__bone::after {
  content: none;
  // transition: none;
}
</style>
