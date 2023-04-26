<template>
  <div class="p">
    <div class="name">
      {{ this.placeName }}
    </div>
    <div class="addr">
      {{ this.placeAddr }}
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  computed: {
    ...mapState("placeStore", ["placeX", "placeY", "placeName", "placeAddr"]),
  },
};
</script>

<style scoped>
.p {
  background: #ffffff;
  box-shadow: 0px 4px 50px -10px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  width: 80%;
  height: 15%;
}
.name {
  padding: 25px;
  margin-top: 20px;
  font-size: 20px;
  font-family: var(--extrabold-font);
  padding-block: 7px;
}
.addr {
  padding: 25px;
  font-family: var(--medium-font);
  padding-block: 5px;
}
</style>
