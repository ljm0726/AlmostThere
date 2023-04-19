<template>
  <v-sheet
    class="px-3 d-flex flex-row justify-space-between align-center xxxxl-font main-col-1"
    height="55"
    color="transparent"
  >
    <back-button color="var(--main-col-1)"></back-button>
    <span class="logo-font">Almost There</span>
    <home-button color="var(--main-col-1)"></home-button>
  </v-sheet>
</template>

<script>
import BackButton from "@/common/component/button/BackButton.vue";
import HomeButton from "@/common/component/button/HomeButton.vue";

export default {
  name: "NavHeader",
  components: {
    BackButton,
    HomeButton,
  },
};
</script>

<style></style>
