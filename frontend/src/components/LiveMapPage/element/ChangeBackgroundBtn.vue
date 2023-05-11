<template>
  <v-sheet color="transparent">
    <v-sheet
      max-width="500"
      color="transparent"
      width="100%"
      class="d-flex flex-column justify-end align-end"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        bottom: 82%;
        padding: 0 15px 15px 0;
        z-index: 2;
      "
    >
      <!-- <v-btn class="ml-1 mt-1" color="var(--main-col-1)" dark rounded>
        배경
      </v-btn> -->
      <v-switch v-model="mapType" color="var(--main-col-1)"></v-switch>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "ChangeBackground",
  props: {},
  data() {
    return {};
  },
  methods: {},
};
</script>

<style></style>
