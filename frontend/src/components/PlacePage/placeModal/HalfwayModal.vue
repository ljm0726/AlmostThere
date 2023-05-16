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
            <span class="regular-font sm-font">출발지를 입력해</span>
            <span class="regular-font sm-font">
              중간 위치를 추천 받아보세요!
            </span>
          </v-card-title>
          <v-card-text class="pa-0 px-5" style="max-height: 200px">
            <div
              class="d-flex flex-column justify-center align-center"
              ref="inputContainer"
            >
              <v-btn
                class="mb-2 px-3 py-1 d-flex flex-row justify-space-between align-center"
                v-for="(start, index) in starts"
                block
                :key="index"
                outlined
                small
                color="var(--main-col-1)"
                style="border-radius: 10px"
              >
                <div
                  class="xs-font medium-font"
                  @click="goToSearchPage(`${index + 1}`)"
                >
                  {{
                    start
                      ? `${index + 1}. ` + start.get("name")
                      : `${index + 1}. 출발지를 입력하세요!`
                  }}
                </div>
                <!-- <input
                class="search-box2"
                :value="
                  
                "
                @click="goToSearchPage(`${index + 1}`)"
                readonly
              /> -->
                <!-- <div> -->
                <!-- <img
                  v-if="index > 1"
                  src="@/assets/images/dialog/close_btn_small.png"
                  class="close-btn-small"
                  alt=""
                  @click="cancelStart(index)"
                /> -->
                <v-btn
                  v-if="index > 1"
                  @click="cancelStart(index)"
                  icon
                  x-small
                  outlined
                  style="border: 1.6px solid var(--main-col-1)"
                >
                  <v-icon
                    x-small
                    style="font-size: 8px; height: 8px; width: 8px"
                  >
                    $vuetify.icons.close
                  </v-icon>
                </v-btn>
                <!-- </div> -->
              </v-btn>
            </div>
          </v-card-text>

          <!-- <div style="align-self: center; margin: 4% 0" @click="plusStart"> -->
          <!-- <img
              src="@/assets/images/dialog/Plus.png"
              style="margin-bottom: 8%; float: left"
              alt=""
            /> -->
          <!-- <v-icon>$vuetify.icons.flag_plus</v-icon>
            출발지 추가하기 -->
          <!-- </div> -->
          <div class="ma-0 pa-0 mx-4 mt-2 mb-4 d-flex flex-column">
            <v-btn
              class="mb-2"
              color="var(--main-col-1)"
              text
              @click="plusStart"
              rounded
              block
            >
              <v-icon>$vuetify.icons.flag_plus</v-icon>
              <span>출발지 추가하기</span>
            </v-btn>
            <!-- </v-card-actions>
          <v-card-actions> -->
            <v-btn
              elevation="0"
              color="var(--main-col-1)"
              dark
              rounded
              block
              @click="findHalfway"
              >중간 위치 찾기</v-btn
            >
          </div>
          <!-- <v-card-text style="overflow: visible"> -->
          <!-- <v-row>
            <v-col class="search_halfway"> -->
          <!-- </v-col>
          </v-row> -->
          <!-- </v-card-text> -->
        </v-card>
      </v-dialog>
    </div>
    <no-image-default
      ref="error"
      :title="errorTitle"
      :message="errorMsg"
    ></no-image-default>
    <!-- <div class="error">
      <v-dialog
        v-model="dialogError"
        scrollable
        max-width="300px"
        rounded="xl"
        persistent
      >
        <v-card rounded="xl">
          <v-card-title class="d-flex flex-column">
            <img src="@/assets/images/dialog/internet_error.png" width="60%" />
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
    </div> -->
  </div>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";
import { mapActions, mapState } from "vuex";
import NoImageDefault from "@/common/component/dialog/NoImageDefault.vue";
// import  from "../SearchPlace/SearchPlacePage2.vue";

export default {
  name: "HalfwayModal",
  components: { CloseButton, NoImageDefault },
  data() {
    return {
      dialog: false,
      // dialogError: false,
      errorMsg: "",
      errorTitle: "",
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

    // openErrorDialog() {
    //   this.dialogError = true;
    // },
    // closeErrorDialog() {
    //   this.dialogError = false;
    // },

    goToSearchPage(index) {
      localStorage.setItem("listIndex", index);
      this.$router.push("/search2");
    },

    updateSelectedPlace(place) {
      this.selectedPlace = place;
      this.isSearchPage = false; // dialog를 다시 열어줌
    },

    plusStart() {
      if (this.starts.length > 9) {
        this.errorTitle = "<span>최대 10개까지만</span><span>가능합니다</span>";
        // this.errorMsg = "최대 10개까지 가능합니다";
        this.$refs.error.openDialog();
        // this.dialogError = true;
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
          // this.dialogError = true;
          this.errorTitle = "출발지를 입력하세요!";
          this.$refs.error.openDialog();
          return;
        }
      }
      this.size = this.startPlaces.length;

      const combinations = [];
      for (let i = 1; i <= this.size; i++) {
        const result = this.combine(this.startPlaces, i);
        combinations.push(...result);
      }

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
        middlePlace.push({ middleX, middleY });
      });
      let middleAvergeX = 0;
      let middleAvergeY = 0;
      middlePlace.forEach((place) => {
        middleAvergeX += place.middleX;
        middleAvergeY += place.middleY;
      });
      middleAvergeX /= middlePlace.length;
      middleAvergeY /= middlePlace.length;

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
