<template>
  <v-sheet
    height="55"
    width="100%"
    max-width="500"
    class="px-3 d-flex flex-row justify-space-between align-center"
  >
    <v-btn large icon style="visibility: hidden">
      <v-icon color="var(--main-col-2)">mdi-help-circle-outline</v-icon>
    </v-btn>
    <div
      class="logo-font xxxxl-font main-col-1"
      color="transparent"
      @click="goHome()"
      style="cursor: pointer"
    >
      Almost There
    </div>
    <v-btn @click="goManual()" large icon>
      <v-icon color="var(--main-col-2)">mdi-help-circle-outline</v-icon>
    </v-btn>
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
    goManual() {
      this.$router.push(`/manual`);
    },
  },
};
</script>

<style></style>
