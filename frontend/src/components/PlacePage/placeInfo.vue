<template>
  <!-- <div class="p"> -->
  <div class="p" :style="infoStyle">
    <!-- 중간 장소 info -->
    <div v-if="this.placeName != null">
      <div class="name">
        {{ this.placeName }}
      </div>
      <div class="addr">
        {{ this.placeAddr }}
      </div>
    </div>
    <!-- 출발 장소 info -->
    <div v-else>
      <div class="name">
        {{ this.startPlace }}
      </div>
      <div class="addr">
        {{ this.startAddress }}
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  computed: {
    ...mapState("placeStore", ["placeX", "placeY", "placeName", "placeAddr"]),
    infoStyle() {
      // 현재 화면의 높이 계산하여 margin-bottom 게산
      const windowHeight = window.innerHeight;
      const marginBottom = Math.round(windowHeight * 0.2);

      // 스타일 객체 반환
      return { marginBottom: `${marginBottom}px` };
    },
  },
  // 출발지 선정으로 component로 접근 시 props로 데이터 전달
  props: {
    startPlace: {
      type: String,
      required: false,
    },
    startAddress: {
      type: String,
      required: false,
    },
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
