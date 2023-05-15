<template>
  <v-btn color="var(--main-col-1)" @click="moveLivemap" text rounded icon>
    <div class="d-flex flex-column align-center">
      <v-icon large style="font-size: 30px; height: 30px; width: 30px">
        $vuetify.icons.location_outline
      </v-icon>
      <span class="mt-1 xxxxs-font">위치공유</span>
    </div>
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
