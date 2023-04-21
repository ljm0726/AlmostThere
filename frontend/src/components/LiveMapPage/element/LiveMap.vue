<template>
  <div id="map"></div>
</template>

<script>
export default {
  name: "LiveMap",
  data() {
    return {
      placeMarkerSize: [50, 70], // 모임장소 marker 크기
      memberMarkerSize: [120, 120], // member marker 크기
      markerOption: [25, 70], // image marker 위치 (좌표 X)
      meetingLatLng: [37.5013, 127.0396], // 모임장소 좌표
      memberLocation: [
        // 각 member 별 현재 좌표
        {
          member: {
            memberId: 1,
            memberNickname: "김싸피",
            memberLatLng: [37.5004, 127.0361], // 역삼역
          },
        },
        {
          member: {
            memberId: 2,
            memberNickname: "히정",
            memberLatLng: [37.5048, 127.0413], // 역삼 신라스테이
          },
        },
      ],
    };
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
      this.createMemberMarker();
    },
    // [@Method] 모임장소 marker 생성
    createPlaceMarker(options) {
      const imageSrc = require("@/assets/images/page/pointer.png");
      const imageSize = new kakao.maps.Size(
        this.placeMarkerSize[0],
        this.placeMarkerSize[1]
      );
      const imageOption = {
        offset: new kakao.maps.Point(
          this.markerOption[0],
          this.markerOption[1]
        ),
      };
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
    createMemberMarker() {
      for (var ml of this.memberLocation) {
        // console.log("#21# member 확인: ", ml);
        // console.log(
        //   "#21# memberLatLng 확인: ",
        //   ml.member.memberLatLng
        // );
        // i) marker option 설정
        const imageSrc = require(`@/assets/images/animals/${
          ml.member.memberId % 10
        }.png`);
        const imageSize = new kakao.maps.Size(
          this.memberMarkerSize[0],
          this.memberMarkerSize[1]
        );
        const imageOption = {
          offset: new kakao.maps.Point(
            this.markerOption[0],
            this.markerOption[1]
          ),
        };
        const markerImage = new kakao.maps.MarkerImage(
          imageSrc,
          imageSize,
          imageOption
        );
        const location = new kakao.maps.LatLng(
          ml.member.memberLatLng[0],
          ml.member.memberLatLng[1]
        );
        // ii) marker 생성
        const marker = new kakao.maps.Marker({
          position: location,
          image: markerImage,
        });
        // marker 표시
        marker.setMap(this.map);

        // iii) 오버레이 표시
        this.createOverlay(ml.member.memberNickname, marker);
      }
    },
    // [@Method] marker 별 오버레이 생성
    createOverlay(nickname, marker) {
      const content = `<div class="label">${nickname}</div>`;
      const position = marker.getPosition();

      // 오버레이 생성
      const customOverlay = new kakao.maps.CustomOverlay({
        map: this.map,
        position: position,
        content: content,
        yAnchor: 2.8, // 오버레이 표시 x, y 위치
        xAnchor: -0.3,
      });

      // 오버레이 표시
      customOverlay.setMap(this.map);
    },
  },
};
</script>

<style>
#map {
  width: 100%;
  height: 100%;
}

.label {
  /* background-color: white; */
  text-shadow: -1px -1px 0 var(--main-col-1), 1px -1px 0 var(--main-col-1),
    -1px 1px 0 var(--main-col-1), 1px 1px 0 var(--main-col-1);
  padding: 5px;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  text-align: center;
  font-size: 16px;
}
</style>
