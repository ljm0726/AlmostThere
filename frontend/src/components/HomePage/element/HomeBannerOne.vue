<template>
  <v-sheet
    @click="goManual()"
    width="100%"
    height="100%"
    class="d-flex flex-column main-col-1 align-center justify-center banner-font-1 hugexlarge-font narrow-font"
  >
    <span>Welcome</span>
    <span>To</span>
    <span>Almost There</span>
    <v-btn
      class="px-7 mt-3"
      elevation="0"
      color="var(--main-col-1)"
      rounded
      dark
      @click="goManual()"
    >
      <span class="light-font mr-2">사용설명서 보러가기</span>
      <v-icon small>$vuetify.icons.arrow_right_white</v-icon>
    </v-btn>
  </v-sheet>
</template>

<script>
export default {
  name: "HomeBannerOne",
  methods: {
    goManual() {
      this.$router.push(`/manual`);
    },
  },
};
</script>

<style></style>
