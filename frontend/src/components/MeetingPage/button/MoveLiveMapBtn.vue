<template>
  <v-btn color="var(--main-col-1)" icon large @click="moveLivemap">
    <v-icon large>$vuetify.icons.location_outline</v-icon>
  </v-btn>
</template>

<script>
export default {
  name: "MoveLiveMapBtn",
  props: {
    meetingTime: String,
  },
  methods: {
    moveLivemap() {
      this.$router.push(`/live-map/${this.$route.params.id}`);
    },
  },
};
</script>

<style scoped>
span.extralight-font.sm-font {
  line-height: 1.5;
}
</style>
