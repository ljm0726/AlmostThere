<template>
  <div class="live-map">
    <arrive-and-chat-btn @resizeMapLevel="resizeMapLevel"></arrive-and-chat-btn>
    <live-map ref="livemap"></live-map>
  </div>
</template>

<script>
import LiveMap from "./element/LiveMap.vue";
import ArriveAndChatBtn from "./element/ArriveAndChatBtn.vue";

export default {
  name: "LiveMapPage",
  components: { LiveMap, ArriveAndChatBtn },
  methods: {
    resizeMapLevel() {
      this.$refs.livemap.resizeMapLevel();
    },
  },
};
</script>

<style scoped>
.live-map {
  width: 100%;
  height: 100%;
}
.live-map > div:not(.v-sheet) {
  height: 100%;
}
</style>
