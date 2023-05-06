<template>
  <div style="width: 100%; height: 100%">
    <v-btn
      id="square-btn"
      class="back-btn"
      outlined
      @click="$router.go(-1)"
      rounded
    >
      <v-icon>$vuetify.icons.arrow_left</v-icon>
    </v-btn>
    <input
      class="search-box"
      placeholder=" 모임장소를 검색하세요"
      v-on:click="goSearchPage('/search')"
    />
    <div id="map"></div>
  </div>
</template>

<script>
export default {
  name: "StartPlace",
  data() {
    return {
      startPlace: null,
      startAddress: null,
      startLatLng: [],
    };
  },
  mounted() {
    // Kakao Map Script import
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      //   /* global kakao */
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?appkey=4a440970d2ed6adb820352f0223f931f&autoload=false&libraries=services"; // &autoload=false api를 로드한 후 맵을 그리는 함수가 실행되도록 구현
      script.onload = () => window.kakao.maps.load(this.initMap); // 스크립트 로드가 끝나면 지도를 실행될 준비가 되어 있다면 지도가 실행되도록 구현
      document.head.appendChild(script);
    }
  },
  created() {
    // 출발지 장소, 주소 저장
    this.startPlace = this.$route.query.startPlace;
    this.startAddress = this.$route.query.startAddress;
    // 출발지 주소를 토대로 lan, lng 저장
    this.startLatLng.push(this.$route.query.startLat);
    this.startLatLng.push(this.$route.query.startLng);
  },
  methods: {
    // [@Method] Kakao Map 생성
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new window.kakao.maps.LatLng(37.5296, 127.0698),
        level: 4,
      };
      // 지도 객체 등록
      this.map = new window.kakao.maps.Map(container, options);

      // 기존 출발지 marker 표시
      if (this.startLatLng.length != 0) {
        this.createMarker();
      }
    },
    // [@Method] 출발지 marker 생성
    createMarker() {
      const marker = new window.kakao.maps.Marker({
        position: new window.kakao.maps.LatLng(
          this.startLatLng[0],
          this.startLatLng[1]
        ),
      });
      // marker 표시
      marker.setMap(this.map);
    },
    // [@Method] 검색 페이지로 이동
    goSearchPage(url) {
      this.$router.push({
        path: url,
        query: { type: "start-place" },
      });
    },
  },
};
</script>

<style>
#map {
  width: 100%;
  height: 100%;
}
</style>
