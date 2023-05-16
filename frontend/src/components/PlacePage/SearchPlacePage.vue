<template>
  <div class="full-search-color">
    <v-sheet
      class="px-3 main-col-1"
      max-width="500"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        top: 0;
        z-index: 2;
      "
      color="transparent"
    >
      <v-sheet
        height="55"
        color="transparent"
        class="d-flex flex-row justify-space-between align-center"
      >
        <v-btn
          id="square-btn"
          class="mr-2"
          outlined
          color="var(--main-col-1)"
          @click="goBack()"
          elevation="3"
          rounded
          style="background-color: white; border: 1.6px solid var(--main-col-1)"
        >
          <v-icon>$vuetify.icons.arrow_left</v-icon>
        </v-btn>
        <form action="" style="width: 100%">
          <input
            class="search-box"
            placeholder="장소를 검색하세요"
            v-model="searchValue"
            ref="myInput"
            autocomplete="off"
          />
          <button id="submit_btn" @click.prevent="getSearchResult"></button>
        </form>
        <!-- <form action="">
          <input
            class="search-box"
            v-model="searchValue"
            placeholder="장소를 검색하세요"
            ref="myInput"
            v-on:click="goToPage('/search')"
            autocomplete="off"
          />
        </form> -->
      </v-sheet>
    </v-sheet>
    <!-- <v-btn id="square-btn" class="back-btn" outlined @click="goBack()" rounded>
      <v-icon>$vuetify.icons.arrow_left</v-icon>
    </v-btn>
    <form action="">
      <input
        class="search-box"
        placeholder=" 장소를 검색하세요"
        
        ref="myInput"
        autocomplete="off"
      />
      <button id="submit_btn" @click.prevent="getSearchResult"></button>
    </form> -->
    <div id="list" class="lists" v-show="isListOpen"></div>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";

export default {
  name: "SearchPlacePage",
  data() {
    return {
      map: null,
      ps: null,
      current: { lat: 37.5, lng: 127.039 },
      searchValue: "",
      geocoder: null,
      dong: "",
      regCode: "",
      residentResults: [],
      isListOpen: false,
    };
  },
  computed: {
    ...mapState("meetingStore", ["regist"]),
  },
  created() {},
  mounted() {
    this.loadScript();
    this.$refs.myInput.focus();
  },

  methods: {
    ...mapActions("placeStore", ["updatePlace"]),
    ...mapActions("meetingStore", ["setRegistMeeting"]),

    goBack() {
      this.$router.go(-1);
    },
    loadScript() {
      const script = document.createElement("script");
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?appkey=4a440970d2ed6adb820352f0223f931f&autoload=false&libraries=services"; // &autoload=false api를 로드한 후 맵을 그리는 함수가 실행되도록 구현
      script.onload = () => {
        window.kakao.maps.load(() => {
          this.loadMap;
          this.ps = new window.kakao.maps.services.Places();
        });
      }; // 스크립트 로드가 끝나면 지도를 실행될 준비가 되어 있다면 지도가 실행되도록 구현

      document.head.appendChild(script); // html>head 안에 스크립트 소스를 추가
    },
    getSearchResult() {
      // 검색 결과
      this.geocoder = new window.kakao.maps.services.Geocoder();
      if (this.marker) this.marker.setMap(null);
      this.ps.keywordSearch(this.searchValue, this.placesSearchCB);
    },
    placesSearchCB(data, status) {
      if (status === window.kakao.maps.services.Status.OK) {
        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        this.isResident = false;
        this.residentResults = [];

        const listDiv = data.reduce(
          (
            cur,
            { place_url, place_name, category_name, y, x, address_name }
          ) => {
            this.residentResults.push({
              place_url,
              place_name,
              category_name,
              y,
              x,
              address_name,
            });
            return (
              cur +
              `
                <div class="resident_items" data-x=${x} data-y=${y} data-place=${place_name} data-address="${address_name}" >
                  <div class="place-name point-font">${place_name}</div>
                  <div class="address-name point-font">${address_name}</div>
                </div>
              `
            );
          },
          ""
        );
        const list = document.querySelector("#list");
        list.innerHTML = listDiv;
        this.isListOpen = true;
        // #21#
        list.childNodes.forEach((child) => {
          child.addEventListener("click", () => {
            const x = child.attributes["data-x"].value;
            const y = child.attributes["data-y"].value;
            this.address = child.attributes["data-address"].value;
            this.place = child.attributes["data-place"].value;
            const placeMap = new Map();
            placeMap.set("x", x);
            placeMap.set("y", y);
            placeMap.set("name", this.place);
            placeMap.set("addr", this.address);

            // 중간장소 선정 or 출발지 선정에 따라 다른 페이지로 이동
            // i) 중간장소
            if (this.$route.query.type == null) {
              this.updatePlace(placeMap);

              const from = sessionStorage.getItem("from");
              if (from === null) {
                this.regist.lat = y;
                this.regist.lng = x;
                this.regist.place_name = this.place;
                this.regist.place_addr = this.address;
                this.setRegistMeeting(this.regist);
              }

              this.$router.replace("/place");
            }
            // ii) 출발지
            else {
              this.$router.replace({
                path: `/start-place/${this.$route.query.id}`,
                query: {
                  startPlace: placeMap.get("name"),
                  startAddress: placeMap.get("addr"),
                  startLat: placeMap.get("y"),
                  startLng: placeMap.get("x"),
                },
              });
            }
          });
        });
      } else {
        alert("검색 결과가 없습니다.");
      }
    },
    searchAddrFromCoords(coords, callback) {
      // 좌표로 행정동 주소 정보를 요청합니다
      this.geocoder.coord2RegionCode(coords.lng, coords.lat, callback);
    },
    getAddressFromRes(result, status) {
      // 주소 정보 파싱
      if (status === window.kakao.maps.services.Status.OK) {
        for (var i = 0; i < result.length; i++) {
          // 법정동 region_type 값은 'B' 이므로
          if (result[i].region_type === "B") {
            this.currentAddress = result[i].address_name;
            const temp = this.currentAddress.split(" ");
            this.currentPrevAddress = `${temp[0]} ${temp[1]}`;
            this.dong = temp[2];
            this.regCode = result[i].code;
            break;
          }
        }
        // this.getRecentPrice(); => 가장 최근 가격
      } else {
        this.currentAddress = "실패";
      }
    },
  },
};
</script>

<style lang="scss">
.full-search-color {
  color: #092a49;
}
.lists {
  overflow: scroll;
  margin-top: 13%;
}
.resident_items {
  position: relative;
  margin-inline: 6%;
}
.resident_items .place-name {
  font-size: 21px;
  font-family: var(--extrabold-font);
  padding-top: 13px;
}
.resident_items .address-name {
  font-family: var(--reqular-font);
  border-bottom: 2px solid #000;
  font-size: 14px;
  padding-bottom: 13px;
  padding-top: 3px;
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
  color: #092a49;
  z-index: 2;
  background: #ffffff;
  left: 2.2%;
  top: 1.7%;
  bottom: unset; /* 추가 */
}
.search-box {
  // box-sizing: border-box;
  // z-index: 2;
  // display: flex;
  // position: absolute;
  // margin-left: 5%;
  // width: 80%;
  // height: 37px;
  // left: 10%;
  // right: 10%;
  // top: 1.7%;
  // bottom: unset;

  height: 36px;
  width: 100%;
  background: white;
  border: 1.6px solid #092a49;
  box-shadow: 0px 4px 10px rgba(9, 42, 73, 0.25);
  border-radius: 10px;
}
</style>
