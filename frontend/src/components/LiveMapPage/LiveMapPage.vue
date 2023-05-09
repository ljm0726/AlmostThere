<template>
  <div class="live-map">
    <live-map></live-map>
  </div>
</template>

<script>
import LiveMap from "./element/LiveMap.vue";

export default {
  name: "LiveMapPage",
  components: { LiveMap },
};
</script>

<style scoped>
.live-map {
  width: 100%;
  height: 100%;
}
.live-map > div {
  height: 100%;
}
</style>
