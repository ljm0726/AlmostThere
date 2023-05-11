<template>
  <v-sheet
    @click="goRegister()"
    width="100%"
    height="100%"
    class="d-flex flex-column main-col-1 align-center justify-center banner-font-2 hugexlarge-font narrow-font"
  >
    <span>Version 1.0</span>
    <span>Release</span>
    <v-btn
      class="px-7 mt-3"
      elevation="0"
      color="var(--main-col-1)"
      rounded
      dark
    >
      <span class="light-font mr-2">모임 등록하러 가기</span>
      <v-icon small>$vuetify.icons.arrow_right_white</v-icon>
    </v-btn>
  </v-sheet>
</template>

<script>
export default {
  name: "HomeBannerTwo",
  methods: {
    goRegister() {
      this.$router.push(`/register`);
    },
  },
};
</script>

<style></style>
