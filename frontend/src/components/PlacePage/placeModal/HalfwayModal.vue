<template>
  <div class="parent">
    <div class="input">
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
            <span class="regular-font md-font"
              >중간 위치를 추천 받아보세요!</span
            >
          </v-card-title>

          <div class="input-container" ref="inputContainer">
            <div
              v-for="(start, index) in starts"
              :key="index"
              class="input-list"
            >
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
                <v-btn
                  elevation="0"
                  color="var(--main-col-1)"
                  dark
                  rounded
                  block
                  @click="findHalfway"
                  >중간 위치 찾기</v-btn
                >
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-dialog>
    </div>

    <div class="error">
      <v-dialog
        v-model="dialogError"
        scrollable
        max-width="300px"
        rounded="xl"
        persistent
      >
        <v-card rounded="xl">
          <v-card-title class="d-flex flex-column">
            <img src="@/assets/images/dialog/logout.png" width="60%" />
            <span class="logo-font xxxxxxl-font main-col-1">Error</span>
            <span
              class="extralight-font xs-font d-flex flex-column align-center seminarrow-font"
            >
              <div>{{ errorMsg }}</div>
            </span>
          </v-card-title>
          <v-card-text>
            <v-btn
              elevation="0"
              color="var(--main-col-1)"
              dark
              rounded
              block
              @click="closeErrorDialog"
              >닫기</v-btn
            >
          </v-card-text>
        </v-card>
      </v-dialog>
    </div>
  </div>
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
      dialogError: false,
      errorMsg: "",
      starts: [null, null], //모달에서 보여줄 정보
      isSearchPage: false,
      selectedPlace: {},
      size: 0,
    };
  },
  computed: {
    ...mapState("halfwayStore", ["startPlaces", "meeting_start_places"]),
    ...mapState("meetingStore", ["meeting_members"]),
  },
  mounted() {
    // console.log(this.startPlaces, this.meeting_members);
    this.starts = this.startPlaces;
    if (this.starts.length < 2) {
      this.starts.push(null);
    }
  },
  watch: {
    dialog() {
      if (!this.dialog) {
        sessionStorage.removeItem("findHalfwayModal");
      }
    },
  },
  methods: {
    ...mapActions("meetingStore", [
      "updateHalfway",
      "addPlaceList",
      "removePlaceList",
    ]),
    ...mapActions("halfwayStore", [
      "removePlaceList",
      "addMiddlePlace",
      "setStartPlace",
    ]),
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      sessionStorage.removeItem("findHalfwayModal");
      this.dialog = false;
    },

    openErrorDialog() {
      this.dialogError = true;
    },
    closeErrorDialog() {
      this.dialogError = false;
    },

    goToSearchPage(index) {
      // this.$router.push(url);
      // console.log(index);
      localStorage.setItem("listIndex", index);
      // this.isSearchPage = true; // dialog를 닫고 SearchPlacePage2로 이동
      this.$router.push("/search2");
    },

    updateSelectedPlace(place) {
      this.selectedPlace = place;
      this.isSearchPage = false; // dialog를 다시 열어줌
    },

    plusStart() {
      // console.log("@@@", this.starts.length);
      if (this.starts.length > 9) {
        this.errorMsg = "최대 10명까지 가능합니다";
        this.dialogError = true;
      } else {
        this.starts.push(null);
        setTimeout(() => {
          this.scrollToBottom();
        }, 100);
      }
    },

    cancelStart(index) {
      this.removePlaceList(index);
      // this.starts.splice(index, 1);
    },

    scrollToBottom() {
      const inputContainer = this.$refs.inputContainer;
      inputContainer.scrollTo({
        top: inputContainer.scrollHeight - inputContainer.clientHeight,
        behavior: "smooth", // 부드러운 애니메이션 적용
      });
    },

    combine(arr, k) {
      const result = [];

      function dfs(start, comb) {
        if (comb.length === k) {
          result.push(comb.slice());
          return;
        }

        for (let i = start; i < arr.length; i++) {
          comb.push(arr[i]);
          dfs(i + 1, comb);
          comb.pop();
        }
      }

      dfs(0, []);
      return result;
    },

    findHalfway() {
      for (let i = 0; i < this.starts.length; i++) {
        if (this.starts[i] == null) {
          this.dialogError = true;
          this.errorMsg = "출발지를 입력하세요!";
          return;
        }
      }
      this.size = this.startPlaces.length;

      const combinations = [];
      for (let i = 1; i <= this.size; i++) {
        // console.log("출발정보", this.startPlaces);
        const result = this.combine(this.startPlaces, i);
        combinations.push(...result);
      }

      // console.log("combi: ", combinations);

      const middlePlace = [];
      combinations.forEach((combination) => {
        let xSum = 0;
        let ySum = 0;
        combination.forEach((place) => {
          xSum += parseFloat(place.get("x"));
          ySum += parseFloat(place.get("y"));
        });
        const middleX = xSum / (combination.length * 1.0);
        const middleY = ySum / (combination.length * 1.0);
        console.log("Sum: ", xSum, ySum);
        middlePlace.push({ middleX, middleY });
      });
      // console.log("middle: ", middlePlace);
      let middleAvergeX = 0;
      let middleAvergeY = 0;
      middlePlace.forEach((place) => {
        middleAvergeX += place.middleX;
        middleAvergeY += place.middleY;
      });
      middleAvergeX /= middlePlace.length;
      middleAvergeY /= middlePlace.length;

      // console.log("중간좌표: ", middleAvergeX, " ", middleAvergeY);
      this.addMiddlePlace({ middleAvergeX, middleAvergeY });
      this.dialog = false;
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
  max-height: 200px;
}

.img-container {
  position: absolute;
  top: 48%;
  right: 8%;
  transform: translateY(-50%);
  width: 30px;
  height: 30px;
}

.parent {
  position: relative;
}

.input {
  position: relative;
  z-index: 2;
}

.error {
  position: relative;
  z-index: 1;
}
</style>
