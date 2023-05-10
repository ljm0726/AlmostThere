<template>
  <v-sheet
    @click="goManual()"
    width="100%"
    height="100%"
    class="d-flex flex-row align-center justify-center"
  >
    <img height="100%" src="@/assets/images/banner/home.png" />
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
