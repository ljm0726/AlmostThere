<template>
  <div class="map-area">
    <v-btn id="square-btn" class="back-btn" outlined @click="goBack()" rounded>
      <v-icon>$vuetify.icons.arrow_left</v-icon>
    </v-btn>
    <input
      class="search-box"
      placeholder=" 모임장소를 검색하세요"
      v-on:click="goToPage('/search')"
    />

    <v-btn class="find-place-btn" @click="findHalfway()"
      ><i class="fa-light fa-location-dot"></i>중간 위치 찾기</v-btn
    >
    <halfway-modal ref="halfway"></halfway-modal>

    <div id="map" class="maps"></div>
    <div v-if="isSelect" @click="moveRegisterPage">
      <place-info class="place-info"></place-info>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import placeInfo from "./placeInfo.vue";
import HalfwayModal from "./placeModal/HalfwayModal.vue";

export default {
  components: { placeInfo, HalfwayModal },
  name: "PlacePage",
  data() {
    return {
      map: null,
      ps: null,
      current: { lat: 37.5, lng: 127.039 },
      geocoder: null,
      isSelect: false,
    };
  },

  computed: {
    ...mapState("placeStore", ["placeX", "placeY", "placeName", "placeAddr"]),
  },

  mounted() {
    console.log(
      "제대로 왓나?",
      this.placeX,
      this.placeY,
      this.placeName,
      this.placeAddr
    );
    if (window.kakao && window.kakao.maps) {
      // 카카오 객체가 있고, 카카오 맵 그릴 준비가 되어 있다면 맵 실행
      this.loadMap();
    } else {
      // 없다면 카카오 스크립트 추가 후 맵 실행
      this.loadScript();
    }

    if (this.placeX !== null) this.isSelect = true;
    else this.isSelect = false;
    if (this.placeX !== null) {
      var bounds = new window.kakao.maps.LatLngBounds();
      bounds.extend(new window.kakao.maps.LatLng(this.placeY, this.placeX));
      this.current.lng = this.placeX;
      this.current.lat = this.placeY;
      this.displayMarker(this.placeY, this.placeX);
      // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
      this.map.setBounds(bounds);
      this.map.relayout();
      this.map.setCenter(
        new window.kakao.maps.LatLng(this.current.lat, this.current.lng)
      );
    }

    if (sessionStorage.getItem("findHalfwayModal") !== null) {
      this.$refs.halfway.openDialog();
    }
  },

  methods: {
    findHalfway() {
      sessionStorage.setItem("findHalfwayModal", true);
      this.$refs.halfway.openDialog();
    },
    moveRegisterPage() {
      this.$router.push("/register");
    },

    goToPage(url) {
      this.$router.push(url);
    },
    goBack() {
      this.$router.go(-1);
    },
    loadScript() {
      const script = document.createElement("script");
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?appkey=4a440970d2ed6adb820352f0223f931f&autoload=false&libraries=services"; // &autoload=false api를 로드한 후 맵을 그리는 함수가 실행되도록 구현
      script.onload = () => window.kakao.maps.load(this.loadMap); // 스크립트 로드가 끝나면 지도를 실행될 준비가 되어 있다면 지도가 실행되도록 구현

      document.head.appendChild(script); // html>head 안에 스크립트 소스를 추가
    },
    loadMap() {
      const container = document.getElementById("map"); // 지도를 담을 DOM 영역
      const options = {
        // 지도를 생성할 때 필요한 기본 옵션
        center: new window.kakao.maps.LatLng(
          this.current.lat,
          this.current.lng
        ), // 지도의 중심좌표
        level: 4, // 지도의 레벨(확대, 축소 정도)
      };

      this.map = new window.kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴
      this.ps = new window.kakao.maps.services.Places();
      this.geocoder = new window.kakao.maps.services.Geocoder();
    },
    displayMarker(y, x) {
      // 마커를 생성하고 지도에 표시합니다
      if (this.curIntroduceMarker) this.curIntroduceMarker.setMap(null);
      this.curIntroduceMarker = new window.kakao.maps.Marker({
        map: this.map,
        position: new window.kakao.maps.LatLng(y, x),
      });
    },
  },
};
</script>

<style scoped>
.place-info {
  z-index: 2;
  position: absolute;
  bottom: 5%;
  left: 8.5%;
}
input {
  padding-left: 10px; /* 여백 크기 조절 */
  font-family: var(--medium-font);
}
.find-place-btn {
  box-sizing: border-box;
  position: absolute;
  z-index: 2;
  right: 5%;
  top: 7.5%;
  font-family: var(--extrabold-font);
  background: #ffffff;
  border: 1px solid #092a49;
  box-shadow: 0px 4px 10px rgba(9, 42, 73, 0.25);
  border-radius: 18px;
}
.back-btn {
  display: flex;
  position: absolute;
  z-index: 2;
  background: #ffffff;
  left: 2.2%;
  top: 1.7%;
  bottom: unset; /* 추가 */
}
.map-area {
  width: 100%;
  height: 100%;
  display: flex; /* 추가 */
  flex-direction: column; /* 추가 */
  justify-content: center; /* 추가 */
  align-items: center; /* 추가 */
}
.maps {
  width: 100%;
  height: 100%;
}
.search-box {
  box-sizing: border-box;
  z-index: 2;
  display: flex;
  position: absolute;
  margin-left: 5%;
  width: 80%;
  height: 37px;
  left: 10%; /* 수정 */
  right: 10%; /* 추가 */
  top: 1.7%;
  bottom: unset; /* 추가 */

  background: #ffffff;
  border: 1px solid #092a49;
  box-shadow: 0px 4px 10px rgba(9, 42, 73, 0.25);
  border-radius: 10px;
}
</style>
