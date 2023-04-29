<template>
  <v-btn color="var(--main-col-1)" icon large @click="moveLivemap">
    <v-icon large>$vuetify.icons.location_outline</v-icon>
  </v-btn>
</template>

<script>
export default {
  name: "MoveLiveMapBtn",
  methods: {
    moveLivemap() {
      this.$router.push(`/live-map/${this.$route.params.id}`);
    },
  },
};
</script>

<style></style>
