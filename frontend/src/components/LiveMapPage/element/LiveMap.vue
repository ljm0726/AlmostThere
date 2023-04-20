<template>
  <div id="map"></div>
</template>

<script>
export default {
  name: "LiveMap",
  data() {
    return {};
  },
  mounted() {
    // Kakao Map Script import
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${process.env.VUE_APP_KAKAO_API_KEY}`;
      document.head.appendChild(script);
    }
  },
  methods: {
    // [@Method] Kakao Map 생성
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(37.5013, 127.0396),
        level: 4,
        // mapTypeId: kakao.maps.MapTypeId.ROADMAP, // 지도 타입 (ROADMAP, SATELLITE, HYBRID, TERRAIN 중 선택)
      };

      // 지도 객체 등록
      this.map = new kakao.maps.Map(container, options);

      // marker 생성
      // i) 모임 장소 marker
      this.createPlaceMarker(options);
      // ii) 멤버 별 marker 생성
      const location = new kakao.maps.LatLng(37.5004, 127.0361); // ! 나중에 사용자 위치로 변경
      this.createMemberMarker(location);
    },
    // [@Method] 모임장소 marker 생성
    createPlaceMarker(options) {
      const imageSrc = require("@/assets/images/page/pointer.png");
      const imageSize = new kakao.maps.Size(50, 70);
      const imageOption = { offset: new kakao.maps.Point(25, 70) };
      const markerImage = new kakao.maps.MarkerImage(
        imageSrc,
        imageSize,
        imageOption
      );
      const marker = new kakao.maps.Marker({
        position: options.center,
        image: markerImage,
      });
      // marker 표시
      marker.setMap(this.map);
    },
    // [@Method] 멤버 별 캐릭터 marker 생성
    createMemberMarker(location) {
      const imageSrc = require("@/assets/images/animals/cat_gray.png");
      const imageSize = new kakao.maps.Size(120, 120);
      const imageOption = { offset: new kakao.maps.Point(25, 70) };
      const markerImage = new kakao.maps.MarkerImage(
        imageSrc,
        imageSize,
        imageOption
      );
      const marker = new kakao.maps.Marker({
        position: location,
        image: markerImage,
      });
      // marker 표시
      marker.setMap(this.map);
    },
  },
};
</script>

<style scoped>
#map {
  width: 100%;
  height: 100%;
}
</style>
