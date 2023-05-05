<template>
  <div style="width: 100%; height: 100%">
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
