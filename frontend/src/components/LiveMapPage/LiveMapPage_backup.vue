<template>
  <div>
    <h1>카카오맵 테스트</h1>
    <div id="map"></div>

    <div>
      <div class="location">
        <input v-model="xloc" />
      </div>
      <div class="location">
        <input v-model="yloc" />
      </div>
      <button id="button" @click="sendMessage(xloc, yloc)"></button>
    </div>
    <div>{{ lat }}</div>
    <div>{{ lng }}</div>
    <!-- HEE -->
    <!-- <div><live-map></live-map></div> -->
  </div>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import LiveMap from "./element/LiveMap.vue";

export default {
  name: "KakaoMap",

  LiveMapata() {
    return {
      map: null,
      lat: "",
      lng: "",
    };
  },

  created() {
    // App.vue가 생성되면 소켓 연결을 시도합니다.
    this.connect();
  },

  computed: {
    LiveMap,
  },

  mounted() {
    if (window.kakao && window.kakao.maps) {
      // 카카오 객체가 있고, 카카오 맵 그릴 준비가 되어 있다면 맵 실행
      this.loadMap();
    } else {
      // 없다면 카카오 스크립트 추가 후 맵 실행
      this.loadScript();
    }
  },

  methods: {
    sendMessage(lat, lng) {
      console.log("sendMessage 도착");
      this.send(lat, lng);
    },
    send(lat, lng) {
      console.log("lat & lng", lat, lng);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          xloc: lat,
          yloc: lng,
        };
        this.stompClient.send("/message/locShare/1", JSON.stringify(msg), {});
      }
    },
    connect() {
      const serverURL = "http://localhost:8080/websocket";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          console.log("소켓 연결 성공", frame);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          const meetingId = 1;
          this.stompClient.subscribe(`/topic/${meetingId}`, (res) => {
            console.log("구독으로 받은 메시지 입니다.", res.body);
          });
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          this.connected = false;
        }
      );
    },

    loadScript() {
      const script = document.createElement("script");
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?appkey=4a440970d2ed6adb820352f0223f931f&autoload=false"; // &autoload=false api를 로드한 후 맵을 그리는 함수가 실행되도록 구현
      script.onload = () => window.kakao.maps.load(this.loadMap); // 스크립트 로드가 끝나면 지도를 실행될 준비가 되어 있다면 지도가 실행되도록 구현

      document.head.appendChild(script); // html>head 안에 스크립트 소스를 추가
    },
    loadMap() {
      const container = document.getElementById("map"); // 지도를 담을 DOM 영역
      const options = {
        // 지도를 생성할 때 필요한 기본 옵션
        center: new window.kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3, // 지도의 레벨(확대, 축소 정도)
      };

      this.map = new window.kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴

      // 마커 띄우기

      if (navigator.geolocation) {
        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        navigator.geolocation.getCurrentPosition((position) => {
          var lat = position.coords.latitude, // 위도
            lng = position.coords.longitude; // 경도
          this.lat = lat;
          this.lng = lng;
          console.log("받아온 위도", lat);
          console.log("받아온 경도", lng);

          var locPosition = new window.kakao.maps.LatLng(lat, lng), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
            message = '<div style="padding:5px;">여기에 계신가요?!</div>'; // 인포윈도우에 표시될 내용입니다

          // 마커와 인포윈도우를 표시합니다
          this.displayMarker(locPosition, message);
        });
      } else {
        // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다

        var locPosition = new window.kakao.maps.LatLng(33.450701, 126.570667),
          message = "geolocation을 사용할수 없어요..";

        this.displayMarker(locPosition, message);
      }
    },

    displayMarker(locPosition, message) {
      // 마커를 생성합니다
      var marker = new window.kakao.maps.Marker({
        map: this.map,
        position: locPosition,
      });

      var iwContent = message, // 인포윈도우에 표시할 내용
        iwRemoveable = true;

      // 인포윈도우를 생성합니다
      var infowindow = new window.kakao.maps.InfoWindow({
        content: iwContent,
        removable: iwRemoveable,
      });

      // 인포윈도우를 마커위에 표시합니다
      infowindow.open(this.map, marker);

      // 지도 중심좌표를 접속위치로 변경합니다
      this.map.setCenter(locPosition);
    },
  },
};
</script>

<style scoped>
.location {
  width: 50px;
  height: 50px;
  border-color: black;
}
#map {
  width: 400px;
  height: 400px;
  margin: auto;
}

#button {
  width: 50px;
  height: 50px;
  background-color: black;
  margin: auto;
}
/* * .live-map {
  width: 100%;
  height: 100%;
} */
</style>
