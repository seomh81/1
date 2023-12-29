<template>
  <v-container fluid>
    <v-row>
      <slot></slot>
    </v-row>
  </v-container>
</template>
<style scoped>
@media (min-width: 1280px) {
  .v-container {
    max-width: 80%;
  }
  .v-container--fluid {
    max-width: 100%;
  }
}
@media (min-width: 1920px) {
  .v-container {
    max-width: 80%;
  }
  .v-container--fluid {
    max-width: 100%;
  }
}
</style>
