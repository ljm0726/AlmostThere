<template>
  <v-dialog
    v-model="dialog"
    scrollable
    max-width="300px"
    rounded="xl"
    v-if="!isSearchPage"
  >
    <v-card rounded="xl">
      <v-card-title class="d-flex flex-column" style="margin-bottom: 2%">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <img
          src="@/assets/images/dialog/earth.png"
          width="60%"
          style="margin-bottom: 7%; height: 82px; width: 102px"
          alt=""
        />
        <span class="regular-font md-font">출발지를 입력해</span>
        <span class="regular-font md-font">중간 위치를 추천 받아보세요!</span>
      </v-card-title>

      <div class="input-container">
        <div v-for="(start, index) in starts" :key="index" class="input-list">
          <input
            class="search-box2"
            :value="
              start
                ? `${index + 1}. ` + start.get('name')
                : `${index + 1}. 출발지를 입력하세요!`
            "
            @click="goToSearchPage(`${index + 1}`)"
            readonly
          />
          <div class="img-container">
            <img
              v-if="index > 1"
              src="@/assets/images/dialog/close_btn_small.png"
              class="close-btn-small"
              alt=""
              @click="cancelStart(index)"
            />
          </div>
        </div>
      </div>

      <div style="align-self: center; margin: 4% 0" @click="plusStart">
        <img
          src="@/assets/images/dialog/Plus.png"
          style="margin-bottom: 8%; float: left"
          alt=""
        />
        출발지 추가하기
      </div>

      <v-card-text style="overflow: visible">
        <v-row>
          <v-col class="search_halfway">
            <v-btn elevation="0" color="var(--main-col-1)" dark rounded block
              >중간 위치 찾기</v-btn
            >
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";
import { mapActions, mapState } from "vuex";
// import  from "../SearchPlace/SearchPlacePage2.vue";

export default {
  name: "HalfwayModal",
  components: { CloseButton },
  data() {
    return {
      dialog: false,
      starts: [null, null],
      isSearchPage: false,
      selectedPlace: {},
    };
  },
  computed: {
    ...mapState("halfwayStore", ["startPlaces"]),
  },
  mounted() {
    console.log(this.startPlaces);
    this.starts = this.startPlaces;
  },
  watch: {
    dialog() {
      if (!this.dialog) {
        localStorage.removeItem("findHalfwayModal");
      }
    },
  },
  methods: {
    ...mapActions("meetingStore", [
      "updateHalfway",
      "addPlaceList",
      "removePlaceList",
    ]),
    ...mapActions("halfwayStore", ["removePlaceList"]),
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      localStorage.removeItem("findHalfwayModal");
      this.dialog = false;
    },

    goToSearchPage(index) {
      // this.$router.push(url);
      console.log(index);
      localStorage.setItem("listIndex", index);
      // this.isSearchPage = true; // dialog를 닫고 SearchPlacePage2로 이동
      this.$router.push("/search2");
    },

    updateSelectedPlace(place) {
      this.selectedPlace = place;
      this.isSearchPage = false; // dialog를 다시 열어줌
    },

    plusStart() {
      console.log("@@@");
      this.starts.push(null);
    },

    cancelStart(index) {
      this.removePlaceList(index);
      // this.starts.splice(index, 1);
    },
  },
};
</script>

<style scoped>
.search-box2 {
  box-sizing: border-box;
  width: 85%;
  height: 33px;
  background: #ffffff;
  border: 1px solid #092a49;
  border-radius: 10px;
  padding-left: 20px;
  padding-right: 25px;

  margin-bottom: 13px;
}
.plusPlace {
  /* align-self: center; */
  margin: 2% 0;
}

/* input::placeholder {
  font-style: var(--regular-font);
  font-size: var(--sm-font);
} */
span {
  line-height: 18px;
}
.input-list {
  position: relative;
  text-align: center;
  width: 100%;
}
.input-container {
  overflow-y: auto;
  max-height: 250px;
}

.img-container {
  position: absolute;
  top: 48%;
  right: 8%;
  transform: translateY(-50%);
  width: 30px;
  height: 30px;
}
</style>
