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
    <ul v-show="isRecommend" id="category">
      <li id="SW8" @click="onClickCategory">
        <v-icon class="category_icon" id="SW8" @click="onClickCategory"
          >mdi mdi-subway-variant</v-icon
        >
      </li>
      <li id="FD6" @click="onClickCategory">
        <v-icon class="category_icon" id="FD6" @click="onClickCategory"
          >mdi mdi-silverware-fork-knife</v-icon
        >
      </li>
      <li id="CE7" @click="onClickCategory">
        <v-icon class="category_icon" id="CE7" @click="onClickCategory"
          >mdi mdi-coffee</v-icon
        >
      </li>
      <li id="CT1" @click="onClickCategory">
        <v-icon class="category_icon" id="CT1" @click="onClickCategory"
          >mdi mdi-movie-play</v-icon
        >
      </li>
    </ul>
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
import { mapState, mapActions } from "vuex";
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
      startMarker: null,
      recommendMarker: null,
      curIntroduceMarker: null,
      currCategory: "",
      markers: [],
      placeOverlay: null,
      contentNode: null,
      isRecommend: false,
      currentMarker: null,
    };
  },

  computed: {
    ...mapState("placeStore", ["placeX", "placeY", "placeName", "placeAddr"]),
    ...mapState("halfwayStore", ["startPlaces", "middlePlace"]),
  },

  watch: {
    // 중간 위치 찾기 후 출발지 마커
    // 나중에 장소 추천지 있으면 여기에 추천장소 마커 추가해야함.
    middlePlace() {
      if (this.middlePlace != null) {
        // 1. 검색한게 있으면 false 처리
        this.isSelect = false;
        this.isRecommend = true;
        // 2. 출발지 좌표 찍기
        var positions = [];
        var bounds = new window.kakao.maps.LatLngBounds();
        for (const startPlace of this.startPlaces) {
          var sp = new Map();
          sp.set("title", startPlace.get("name"));
          sp.set(
            "latlng",
            new window.kakao.maps.LatLng(
              startPlace.get("y"),
              startPlace.get("x")
            )
          );
          positions.push(sp);
          bounds.extend(
            new window.kakao.maps.LatLng(
              startPlace.get("y"),
              startPlace.get("x")
            )
          );
        }

        if (this.startMarker) this.startMarker.setMap(null);
        if (this.curIntroduceMarker) this.curIntroduceMarker.setMap(null);
        for (var i = 0; i < positions.length; i++) {
          // 마커를 생성합니다
          console.log("생성?");
          this.startMarker = new window.kakao.maps.Marker({
            map: this.map, // 마커를 표시할 지도
            position: positions[i].get("latlng"), // 마커를 표시할 위치
            title: positions[i].get("title"), // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
          });
        }
        this.map.setBounds(bounds);

        this.placeOverlay = new window.kakao.maps.CustomOverlay({ zIndex: 1 });
        this.contentNode = document.createElement("div"); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
        this.contentNode.className = "placeinfo_wrap";
        this.placeOverlay.setContent(this.contentNode);

        this.ps = new window.kakao.maps.services.Places();

        this.searchPlaces();
      }
    },
  },

  mounted() {
    if (window.kakao && window.kakao.maps) {
      // 카카오 객체가 있고, 카카오 맵 그릴 준비가 되어 있다면 맵 실행
      this.loadMap();
    } else {
      // 없다면 카카오 스크립트 추가 후 맵 실행
      this.loadScript();
    }

    if (this.placeX !== null) {
      this.isSelect = true;
      this.isRecommend = false;
    } else this.isSelect = false;
    if (this.placeX !== null) {
      // map의 marker를 다 지운다, 아래 displayMarker에서 뺴옴
      if (this.startMarker) this.startMarker.setMap(null);
      if (this.curIntroduceMarker) this.curIntroduceMarker.setMap(null);

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
    ...mapActions("placeStore", ["updatePlace"]),
    // 카테고리별 함수
    changeCategoryClass(el) {
      var category = document.getElementById("category"),
        children = category.children,
        i;
      for (i = 0; i < children.length; i++) {
        children[i].className = "";
      }
      if (el) {
        el.target.className = "on";
      }
    },
    onClickCategory(e) {
      var id = e.target.id;

      this.placeOverlay.setMap(null);

      this.currCategory = id;
      // this.changeCategoryClass(e);
      this.searchPlaces();
    },
    displayPlaceInfo(place) {
      var content =
        '<div class="placeinfo">' +
        '   <a class="title" href="' +
        place.place_url +
        '" target="_blank" title="' +
        place.place_name +
        '">' +
        place.place_name +
        "</a>" +
        `<div class="click">`;

      if (place.road_address_name) {
        content +=
          '    <span title="' +
          place.road_address_name +
          '">' +
          place.road_address_name +
          "</span>" +
          '  <span class="jibun" title="' +
          place.address_name +
          '">(지번 : ' +
          place.address_name +
          ")</span>";
      } else {
        content +=
          '    <span title="' +
          place.address_name +
          '">' +
          place.address_name +
          "</span>";
      }

      content +=
        '    <span class="tel">' +
        place.phone +
        "</span>" +
        "</div>" +
        "</div>" +
        '<div class="after"></div>';

      var self = this;
      this.contentNode.innerHTML = content;
      this.contentNode
        .querySelector(".click")
        .addEventListener("click", function () {
          self.recommendData(place);
        });
      this.placeOverlay.setContent(this.contentNode);

      this.placeOverlay.setPosition(
        new window.kakao.maps.LatLng(place.y, place.x)
      );
      this.placeOverlay.setMap(this.map);
    },

    recommendData(place) {
      console.log("뭐가찍힘?", place);
      const placeMap = new Map();
      placeMap.set("x", place.x);
      placeMap.set("y", place.y);
      placeMap.set("name", place.place_name);
      placeMap.set("addr", place.address_name);
      this.updatePlace(placeMap);
      this.$router.push("/register");
    },

    removeMarker() {
      for (var i = 0; i < this.markers.length; i++) {
        this.markers[i].setMap(null);
      }
      this.markers = [];
    },

    addMarker(position) {
      var imageSrc =
          "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png", // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new window.kakao.maps.Size(24, 35), // 마커 이미지의 크기
        markerImage = new window.kakao.maps.MarkerImage(imageSrc, imageSize),
        marker = new window.kakao.maps.Marker({
          map: this.map,
          position: position, // 마커의 위치
          image: markerImage,
        });

      marker.setMap(this.map); // 지도 위에 마커를 표출합니다
      this.markers.push(marker); // 배열에 생성된 마커를 추가합니다

      return marker;
    },

    displayRecommendMarker(place) {
      let size = 0;

      if (place.length >= 3) {
        size = 3;
      } else {
        size = place.length;
      }
      var self = this;
      for (var i = 0; i < size; i++) {
        // 마커를 생성하고 지도에 표시합니다
        var marker = this.addMarker(
          new window.kakao.maps.LatLng(place[i].y, place[i].x)
        );
        // 마커와 검색결과 항목을 클릭 했을 때
        // 장소정보를 표출하도록 클릭 이벤트를 등록합니다
        (function (marker, p) {
          window.kakao.maps.event.addListener(marker, "click", function () {
            if (marker == self.currentMarker) {
              self.currentMarker = null;
              self.placeOverlay.setMap(null);
            } else {
              self.currentMarker = marker;
              self.displayPlaceInfo(p);
            }
          });
        })(marker, place[i]);
      }
    },

    placesSearchCB(data, status) {
      if (status === window.kakao.maps.services.Status.OK) {
        // for (var i = 0; i < data.length; i++) {
        this.displayRecommendMarker(data);
        // }
      } else if (status === window.kakao.maps.services.Status.ZERO_RESULT) {
        // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요
      } else if (status === window.kakao.maps.services.Status.ERROR) {
        // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요
      }
    },

    searchPlaces() {
      if (!this.currCategory) {
        return;
      }
      // 커스텀 오버레이를 숨깁니다
      this.placeOverlay.setMap(null);
      // 지도에 표시되고 있는 마커를 제거합니다
      this.removeMarker();
      var center = new window.kakao.maps.LatLng(
        this.middlePlace.middleAvergeY,
        this.middlePlace.middleAvergeX
      );
      var radius = 5000;
      this.ps.categorySearch(this.currCategory, this.placesSearchCB, {
        location: center,
        radius: radius,
        useMapBounds: false,
      });
    },

    //----------------------------------------------------------------------

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
      console.log("마커찌그러오나?");
      if (this.curIntroduceMarker) this.curIntroduceMarker.setMap(null);
      // 마커를 생성하고 지도에 표시합니다
      this.curIntroduceMarker = new window.kakao.maps.Marker({
        map: this.map,
        position: new window.kakao.maps.LatLng(y, x),
      });
    },
  },
};
</script>

<style>
#category {
  position: absolute;
  top: 7.3%;
  left: 2%;
  border-radius: 20px;
  border: 1px solid #909090;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.4);
  background: #fff;
  overflow: hidden;
  z-index: 2;
  list-style: none;
  padding: 0;
  margin: 0;
}
#category li {
  float: left;
  list-style: none;
  width: 40px;
  border-right: 1px solid #acacac;
  padding: 2.9%;
  text-align: center;
  cursor: pointer;
}

#category li:hover {
  background: #ffe6e6;
  border-left: 1px solid #acacac;
  margin-left: -1px;
}

.placeinfo_wrap {
  position: absolute;
  bottom: 28px;
  left: -150px;
  width: 300px;
}
.placeinfo {
  position: relative;
  width: 100%;
  border-radius: 6px;
  border: 1px solid #ccc;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
  background: #fff;
}
.placeinfo:nth-of-type(n) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}
.placeinfo_wrap .after {
  content: "";
  position: relative;
  margin-left: -12px;
  left: 50%;
  width: 22px;
  height: 12px;
  background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png");
}
.placeinfo a,
.placeinfo a:hover,
.placeinfo a:active {
  color: #fff;
  text-decoration: none;
}
.placeinfo a,
.placeinfo span {
  display: block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.placeinfo span {
  margin: 5px 5px 0 5px;
  cursor: default;
  font-size: 13px;
}
.placeinfo .title {
  font-weight: bold;
  font-size: 14px;
  border-radius: 6px 6px 0 0;
  margin: -1px -1px 0 -1px;
  padding: 10px;
  color: #fff;
  background: #092a49;
  background: #092a49
    url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png)
    no-repeat right 14px center;
}
.placeinfo .tel {
  color: #0f7833;
}
.placeinfo .jibun {
  color: #999;
  font-size: 11px;
  margin-top: 0;
}
</style>
<style scoped>
.category_icon {
  z-index: 100;
}
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
