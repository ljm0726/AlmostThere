<template>
  <v-sheet
    class="logo-font d-flex flex-row justify-center align-center xxxxl-font main-col-1"
    height="55"
    width="100%"
    max-width="500"
    color="transparent"
    @click="goHome()"
    style="cursor: pointer"
  >
    Almost There
  </v-sheet>
</template>

<script>
export default {
  name: "BaseHeader",
  methods: {
    goHome() {
      if (this.$route.name != "home") {
        this.$router.push(`/home`);
      }
    },
  },
};
</script>

<style></style>
