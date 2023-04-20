<template>
  <div class="liveMap">
    <live-map></live-map>
    <!-- <img src="@/assets/images/animals/cat_gray.png" width="60%" /> -->
    <!-- <img src="@/assets/images/animals/cat_gray.png" width="60%" /> -->
  </div>
</template>

<script>
import LiveMap from "./element/LiveMap.vue";

export default {
  name: "LiveMapPage",
  data() {
    return {};
  },
  components: {
    LiveMap,
  },
};
</script>

<style>
.liveMap {
  width: 100%;
  height: 100%;
}
</style>
