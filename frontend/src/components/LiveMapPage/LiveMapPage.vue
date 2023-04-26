<template>
  <div class="live-map">
    <v-btn @click="ssafyLogin()">싸피 로그인</v-btn>
    <v-btn @click="heeLogin()">희정 로그인</v-btn>
    <live-map></live-map>
  </div>
</template>

<script>
import LiveMap from "./element/LiveMap.vue";

export default {
  name: "LiveMapPage",
  components: { LiveMap },
  // 임시, TEST용
  methods: {
    ssafyLogin() {
      // 현 로그인한 member의 정보
      // console.log("#21# ssafy 로그인");
      const memberStore = {
        memberId: 1,
        memberNickname: "김싸피",
      };
      localStorage.setItem("member", JSON.stringify(memberStore));
    },
    heeLogin() {
      // console.log("#21# hee 로그인");
      const memberStore = {
        memberId: 2,
        memberNickname: "희정",
      };
      localStorage.setItem("member", JSON.stringify(memberStore));
    },
  },
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
