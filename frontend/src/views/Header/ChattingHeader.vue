<template>
  <v-sheet
    class="px-3 d-flex flex-row justify-space-between align-center xxxxl-font main-col-1"
    height="55"
    max-width="500"
    style="position: fixed; margin: 0 auto; left: 0; right: 0; top: 0"
  >
    <back-button color="var(--main-col-1)"></back-button>
    <span class="point-font">{{ name }}</span>
    <detail-button></detail-button>
  </v-sheet>
</template>

<script>
import BackButton from "@/common/component/button/BackButton.vue";
import DetailButton from "@/common/component/button/DetailButton.vue";

export default {
  name: "ChattingHeader",
  components: {
    BackButton,
    DetailButton,
  },
  props: {
    name: String,
  },
};
</script>

<style></style>
