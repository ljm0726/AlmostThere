<template>
  <v-sheet
    class="px-3 d-flex flex-row justify-space-between align-center xxxxl-font main-col-1"
    height="55"
    max-width="500"
    width="100%"
    color="transparent"
    style="
      position: fixed;
      margin: 0 auto;
      left: 0;
      right: 0;
      top: 0;
      z-index: 2;
    "
  >
    <back-button color="var(--main-col-1)"></back-button>
    <span class="logo-font">Almost There</span>
    <detail-button :isIcon="true"></detail-button>
  </v-sheet>
</template>

<script>
import BackButton from "@/common/component/button/BackButton.vue";
import DetailButton from "@/common/component/button/DetailButton.vue";

export default {
  name: "LiveMapHeader",
  components: {
    BackButton,
    DetailButton,
  },
};
</script>

<style></style>
