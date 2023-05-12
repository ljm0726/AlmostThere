<template>
  <div class="parent">
    <div class="modify">
      <v-btn
        class="mr-1"
        id="square-big-btn"
        outlined
        color="var(--main-col-1)"
        rounded
        @click="open()"
      >
        <v-icon>$vuetify.icons.edit_outline</v-icon>
      </v-btn>
      <vue-bottom-sheet
        ref="modifySheet"
        max-width="500px"
        @opened="isOpened"
        @closed="isClosed"
      >
        <v-sheet class="d-flex flex-column pb-10">
          <span class="point-font xxxxl-font main-col-1 align-self-center"
            >모임 내용 수정하기</span
          >
          <v-sheet class="mx-5 my-2">
            <span class="point-font xxxl-font main-col-1">제목</span>
            <v-text-field
              v-model="name"
              outlined
              dense
              hide-details
            ></v-text-field>
          </v-sheet>
          <v-sheet class="mx-5 my-2">
            <span class="point-font xxxl-font main-col-1">일시</span>
            <div class="d-flex flex-row">
              <v-dialog
                ref="dateDialog"
                v-model="dateDialog"
                :return-value.sync="date"
                width="290px"
                style="z-index: 1000000"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    class="mr-1"
                    v-model="date"
                    readonly
                    v-bind="attrs"
                    dense
                    outlined
                    v-on="on"
                    hide-details
                  ></v-text-field>
                </template>
                <v-date-picker
                  locale="ko"
                  v-model="date"
                  scrollable
                  color="var(--main-col-1)"
                  :day-format="(date) => new Date(date).getDate()"
                  :allowed-dates="disablePastDates"
                >
                  <v-spacer></v-spacer>
                  <v-btn
                    text
                    color="var(--main-col-1)"
                    @click="dateDialog = false"
                  >
                    닫기
                  </v-btn>
                  <v-btn
                    text
                    color="var(--main-col-1)"
                    @click="$refs.dateDialog.save(date)"
                  >
                    확인
                  </v-btn>
                </v-date-picker>
              </v-dialog>
              <v-dialog
                ref="timeDialog"
                v-model="timeDialog"
                :return-value.sync="time"
                width="290px"
                style="z-index: 1000000"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    class="ml-1"
                    v-model="time"
                    readonly
                    v-bind="attrs"
                    v-on="on"
                    dense
                    outlined
                    hide-details
                  ></v-text-field>
                </template>
                <v-time-picker
                  v-model="time"
                  full-width
                  color="var(--main-col-1)"
                >
                  <v-spacer></v-spacer>
                  <v-btn
                    text
                    color="var(--main-col-1)"
                    @click="timeDialog = false"
                  >
                    닫기
                  </v-btn>
                  <v-btn
                    text
                    color="var(--main-col-1)"
                    @click="$refs.timeDialog.save(time)"
                  >
                    확인
                  </v-btn>
                </v-time-picker>
              </v-dialog>
            </div>
          </v-sheet>
          <v-sheet class="mx-5 my-2 d-flex flex-column">
            <span class="point-font xxxl-font main-col-1">장소</span>
            <v-btn
              outlined
              block
              color="var(--main-col-1)"
              @click="movePlacePage"
            >
              <span>{{ this.place }}</span>
              <span>({{ this.address }})</span>
            </v-btn>
          </v-sheet>
          <v-sheet class="mx-5 my-2">
            <span class="point-font xxxl-font main-col-1">지각비</span>
            <v-sheet class="d-flex flex-row">
              <v-text-field
                v-model="amount"
                hide-details
                outlined
                dense
                type="number"
              ></v-text-field>
              <v-sheet
                class="ml-2 px-2 detail-border d-flex flex-row align-center justify-center"
                rounded="lg"
                >원</v-sheet
              >
            </v-sheet>
          </v-sheet>
          <v-btn
            class="mx-5 my-2"
            elevation="0"
            color="var(--main-col-1)"
            dark
            rounded
            @click="modifyMetting()"
            >수정 완료</v-btn
          >
        </v-sheet>
      </vue-bottom-sheet>
    </div>
    <div class="error">
      <v-dialog
        v-model="dialog"
        scrollable
        max-width="300px"
        rounded="xl"
        persistent
      >
        <v-card rounded="xl">
          <v-card-title class="d-flex flex-column">
            <img src="@/assets/images/dialog/logout.png" width="60%" />
            <span class="logo-font xxxxxxl-font main-col-1">Server Error</span>
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
              @click="closeDialog"
              >닫기</v-btn
            >
          </v-card-text>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>

<script>
import { mapMutations, mapState, mapActions } from "vuex";
export default {
  name: "MeetingModifyBtn",
  computed: {
    ...mapState("meetingStore", [
      "place_name",
      "place_addr",
      "meeting_lat",
      "meeting_lng",
      "recent_meeting",
    ]),
    ...mapState("placeStore", ["placeName", "placeAddr", "placeX", "placeY"]),
  },
  watch: {
    place_name: function (newValue, oldValue) {
      console.log("WW", newValue, oldValue);
      this.place = newValue;
    },
  },
  methods: {
    ...mapMutations("placeStore", ["UPDATE_PLACE"]),
    ...mapActions("meetingStore", ["modify"]),
    ...mapActions("placeStore", ["resetPlace"]),
    ...mapActions("halfwayStore", ["resetStartPlace"]),

    open() {
      if (this.$refs.modifySheet) {
        this.$refs.modifySheet.open();
      }
    },
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      console.log(this.dialog);
      this.dialog = false;
      console.log(this.dialog);
    },
    isOpened() {
      console.log("M", this.name, this.placeName);
      console.log("Sheet is open.");
    },
    isClosed() {
      sessionStorage.removeItem("from");

      const [date, time2] = this.meetingTime.split("T");
      const time = time2.slice(0, -3);
      this.name = this.meetingName;
      this.date = date;
      this.time = time;

      this.place = this.meetingPlace;
      this.address = this.meetingAddress;
      this.lat = this.meetingLat;
      this.lng = this.meetingLng;
      this.amount = this.lateAmount == null ? 0 : parseInt(this.lateAmount);
    },
    // openDialog() {
    //   this.dialog = true;
    // },
    // closeDialog() {
    //   this.dialog = false;
    //   console.log(this.dialog);
    // },

    movePlacePage() {
      console.log(this.meeting_lat, this.meeting_lng);
      //지도에 현재 위치 찍기 위해 저장
      const placeMap = new Map();
      placeMap.set("x", this.meeting_lat);
      placeMap.set("y", this.meeting_lng);
      placeMap.set("name", this.place);
      placeMap.set("addr", this.address);
      this.UPDATE_PLACE(placeMap);

      sessionStorage.setItem(
        "from",
        JSON.stringify({
          id: this.$route.params.id,
          name: this.name,
          date: this.date,
          time: this.time,
          late: this.amount,
        })
      );

      this.$router.push("/place");
    },

    handlePlaceDataSubmitted(placeData) {
      // Place.vue에서 보내온 데이터를 받아서 처리함
      console.log("pD ", this.placeData);
      this.place = placeData.name;
      this.address = placeData.address;
      this.showPlaceForm = false; // 다이얼로그를 닫음
      // ...
    },

    modifyMetting() {
      this.getCurTime();

      if (this.curDate >= this.date && this.curTime >= this.time) {
        this.errorMsg = "시간을 다시 설정해주세요!";
        this.dialog = true;
      } else if (
        this.name == null ||
        this.name == "" ||
        this.name.replace(/\s/g, "") == "" ||
        this.place == null ||
        this.address == null
      ) {
        this.errorMsg = "모든 정보를 입력해주세요!";
        this.dialog = true;
      } else if (this.name.length > 10) {
        this.errorMsg = "모임 이름을 최대 10자 입니다!";
        this.dialog = true;
      } else if (
        isNaN(parseInt(this.amount)) ||
        this.amount < 0 ||
        this.amount > 10000
      ) {
        this.errorMsg = "지각비는 최대 10,000원 입니다!";
        console.log(this.dialog);
        this.dialog = true;
        console.log(this.dialog);
      } else {
        const savedRecentMeeting = new Date(this.recent_meeting.meetingTime);
        const modifiedRecentMeeting = new Date(this.meetingTime);

        this.modify({
          meeting_name: this.name,
          date: this.date,
          time: this.time,
          place_name: this.place,
          place_addr: this.address,
          amount: this.amount,
        })
          .then(() => {
            console.log("정상실행");
            this.$nextTick(() => {
              this.resetPlace();
              this.resetStartPlace();
              // this.$refs.modifySheet.close();
              sessionStorage.removeItem("from");

              if (savedRecentMeeting > modifiedRecentMeeting) {
                const year = modifiedRecentMeeting.getFullYear();
                const month = String(
                  modifiedRecentMeeting.getMonth() + 1
                ).padStart(2, "0");
                const day = String(modifiedRecentMeeting.getDate()).padStart(
                  2,
                  "0"
                );
                const hours = String(modifiedRecentMeeting.getHours()).padStart(
                  2,
                  "0"
                );
                const minutes = String(
                  modifiedRecentMeeting.getMinutes()
                ).padStart(2, "0");
                const seconds = String(
                  modifiedRecentMeeting.getSeconds()
                ).padStart(2, "0");

                const formattedTime = new Date(
                  `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
                );

                const id = this.$route.params.id;

                const recentMeeting = {
                  meetingId: id,
                  meetingTime: formattedTime,
                };
                this.savedRecentMeeting(recentMeeting);
              }
              window.location.reload(true); //reload를 해야 하나??
            });
          })
          .catch((error) => {
            console.error(error);
            alert("수정 중 에러 발생");
          });
      }
    },

    getCurTime() {
      const currentDate = new Date();
      const options = {
        timeZone: "Asia/Seoul",
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      };
      const dateArray = currentDate
        .toLocaleDateString("en-GB", options)
        .split("/");
      this.curDate = `${dateArray[2]}-${dateArray[1]}-${dateArray[0]}`;
      this.curTime = currentDate.toLocaleTimeString("en-GB", {
        timeZone: "Asia/Seoul",
        hour12: false,
        hour: "2-digit",
        minute: "2-digit",
      });
    },

    disablePastDates(val) {
      const date = new Date();
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const formattedDate = `${year}-${month}-${day}`;
      return val >= formattedDate;
    },

    handleBeforeUnload() {
      sessionStorage.removeItem("from");
    },
    handlePopstate() {
      sessionStorage.removeItem("from");
      history.back();
    },
  },
  props: {
    meetingName: String,
    meetingDate: String,
    meetingTime: String,
    meetingPlace: String,
    meetingAddress: String,
    meetingLat: Number,
    meetingLng: Number,
    lateAmount: Number,
  },
  data() {
    return {
      name: null,
      date: null,
      time: null,
      place: "",
      address: "",
      lat: 0,
      lng: 0,
      amount: 0,
      dateDialog: false,
      timeDialog: false,
      showPlaceForm: false,
      errorMsg: "",
      dialog: false,
    };
  },

  created() {
    console.log("C", this.place_name, this.placeName, this.meetingPlace);
    const [date, time2] = this.meetingTime.split("T");
    const time = time2.slice(0, -3);
    this.name = this.meetingName;
    this.date = date;
    this.time = time;
    // this.place = this.meetingPlace;
    // this.address = this.meetingAddress;
    this.amount = this.lateAmount == null ? 0 : parseInt(this.lateAmount);

    window.addEventListener("beforeunload", this.handleBeforeUnload);
    window.addEventListener("popstate", this.handlePopstate);
  },

  mounted() {
    const retrievedObject = sessionStorage.getItem("from");
    console.log("M", this.place_name, this.placeName);
    if (retrievedObject !== null) {
      const from = JSON.parse(retrievedObject);

      this.name = from.name;
      this.date = from.date;
      this.time = from.time;
      this.amount = from.late;

      if (this.placeName == null || this.placeAddr == null) {
        this.place = this.place_name;
        this.address = this.place_addr;
        this.lat = this.placeX;
        this.lng = this.placeY;
      } else {
        this.place = this.placeName;
        this.address = this.placeAddr;
        this.lat = this.placeX;
        this.lng = this.placeY;
      }

      this.open();
    } else {
      this.place = this.meetingPlace;
      this.address = this.meetingAddress;
      this.lat = this.meetingLat;
      this.lng = this.meetingLng;
    }
  },

  beforeDestroy() {
    window.removeEventListener("beforeunload", this.handleBeforeUnload);
    window.removeEventListener("popstate", this.handlePopstate);
  },
};
</script>

<style>
.parent {
  position: relative;
}

.modify {
  position: relative;
  z-index: 2;
}

.error {
  position: relative;
  z-index: 1;
}
</style>
