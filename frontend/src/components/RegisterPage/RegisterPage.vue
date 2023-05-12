<template>
  <div style="margin-bottom: 76px" class="mx-3 main-form">
    <v-sheet class="point-font xxxxxxl-font main-col-1">
      <!-- <div
      class="point-font xxxxxxl-font main-col-1"
      style="margin: 15% 0 13% 5%"
    > -->
      <div class="d-flex flex-column semi2narrow-font">
        <span>많은 사람들과</span>
        <span>모임을 잡아보세요!</span>
        <span></span>
      </div>
      <!-- <p>많은 사람들과</p>
      <p>모임을</p>
      <p>잡아보세요!</p> -->
      <!-- <img
        src="@/assets/images/page/register.png"
        height="450"
        style="text-aling: center"
        alt=""
      /> -->
    </v-sheet>
    <!-- nav 자리 -->
    <v-sheet>
      <!-- <v-card rounded="xl" elevation="0" height="100%"> -->
      <!-- <v-card-title> -->
      <!-- <img src="@/assets/images/dialog/logout.png" width="60%" /> -->
      <!-- <span class="point-font xxxxl-font main-col-1 text-left">제목</span>
          <v-text-field dense></v-text-field>
          <span class="point-font xxxxl-font main-col-1 text-left">일시</span>
          <span class="point-font xxxxl-font main-col-1 text-left">장소</span>
          <v-text-field dense></v-text-field> -->
      <!-- </v-card-title> -->
      <!-- <v-card-text style="padding: 38px 24px 0 24px"> -->
      <!-- <img src="@/assets/images/dialog/logout.png" width="60%" /> -->

      <!-- 제목 -->
      <v-sheet
        width="100%"
        class="mt-3 d-flex flex-column align-center"
        elevation="1"
      >
        <v-sheet
          width="100%"
          color="var(--red-col)"
          class="point-font white-font xl-font main-col-1 px-4 py-2"
        >
          모임 제목
        </v-sheet>
        <v-text-field
          v-model="meetingname"
          @keyup="setMeetingName"
          maxlength="9"
          hide-details
          placeholder="모임 제목을 입력해 주세요."
          dense
          solo
          flat
          full-width
          style="width: 100%"
        ></v-text-field>
      </v-sheet>

      <!-- 날짜 -->
      <v-sheet
        width="100%"
        class="mt-3 d-flex flex-column align-center"
        elevation="1"
      >
        <!-- <v-sheet
        width="100%"
        class="mt-3 d-flex flex-column align-center"
        elevation="2"
        rounded="lg"
      > -->
        <!-- <v-sheet
          width="100%"
          color="var(--yellow-col)"
          class="point-font white-font xl-font main-col-1 px-4 py-2 rounded-t-lg"
        > -->
        <v-sheet
          width="100%"
          color="var(--yellow-col)"
          class="point-font white-font xl-font main-col-1 px-4 py-2"
        >
          만나는 날짜
        </v-sheet>
        <v-dialog
          ref="menu"
          v-model="menu"
          :return-value.sync="date"
          width="290px"
        >
          <!-- <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          :return-value.sync="date"
          transition="scale-transition"
          offset-y
          min-width="280px"
          :nudge-right="40"
        > -->
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="date"
              readonly
              v-bind="attrs"
              v-on="on"
              hide-details
              dense
              solo
              flat
              full-width
              style="width: 100%"
            ></v-text-field>
          </template>
          <v-date-picker
            class="regular-font"
            locale="ko"
            v-model="date"
            no-title
            scrollable
            color="var(--main-col-1)"
            :day-format="(date) => new Date(date).getDate()"
            :allowed-dates="disablePastDates"
          >
            <v-spacer></v-spacer>
            <v-btn
              text
              class="bold-font main-col-1"
              @click="menu = false"
              rounded
            >
              닫기
            </v-btn>
            <v-btn
              text
              class="bold-font main-col-1"
              @click="$refs.menu.save(date)"
              rounded
            >
              확인
            </v-btn>
          </v-date-picker>
          <!-- </v-menu> -->
        </v-dialog>
      </v-sheet>

      <!-- 시간 -->
      <v-sheet
        width="100%"
        class="mt-3 d-flex flex-column align-center"
        elevation="1"
      >
        <v-sheet
          width="100%"
          color="var(--green-col)"
          class="point-font white-font xl-font main-col-1 px-4 py-2"
        >
          만나는 시간
        </v-sheet>
        <v-dialog
          ref="menu2"
          v-model="menu2"
          :return-value.sync="time"
          width="290px"
        >
          <!-- <v-menu
          ref="menu2"
          v-model="menu2"
          :close-on-content-click="false"
          :nudge-right="40"
          :return-value.sync="time"
          transition="scale-transition"
          offset-y
          max-width="280px"
          min-width="280px"
        > -->
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="time"
              readonly
              v-bind="attrs"
              v-on="on"
              hide-details
              solo
              flat
              full-width
              dense
              style="width: 100%"
            ></v-text-field>
          </template>
          <v-time-picker
            v-if="menu2"
            v-model="time"
            full-width
            color="var(--main-col-1)"
            @click:minute="$refs.menu2.save(time)"
            format="24hr"
          ></v-time-picker>
          <!-- </v-menu> -->
        </v-dialog>
      </v-sheet>

      <!-- 장소 -->
      <v-sheet
        width="100%"
        class="mt-3 d-flex flex-column align-center"
        elevation="1"
      >
        <v-sheet
          width="100%"
          color="var(--blue-col)"
          class="point-font white-font xl-font main-col-1 px-4 py-2"
        >
          모일 장소
        </v-sheet>
        <v-text-field
          v-model="meeting_place"
          @click="movePlacePage"
          placeholder="장소를 선택해 주세요."
          solo
          readonly
          flat
          full-width
          style="width: 100%"
          hide-details
          dense
        ></v-text-field>
      </v-sheet>

      <!-- <v-row>
        <v-col class="pr-1"> -->
      <v-btn
        elevation="3"
        color="var(--main-col-1)"
        dark
        tile
        large
        block
        class="mt-3 lg-font"
        @click="regist_meeting"
      >
        모임 등록하기
      </v-btn>
      <!-- </v-col>
      </v-row> -->
      <!-- </v-card-text> -->
      <!-- </v-card> -->
    </v-sheet>
  </div>
</template>

<script>
// 추가해야할 부분 -> 현재 시간 이전을 설정하면 불가능하게
import { mapActions, mapState } from "vuex";
import { getMostRecentMeeting } from "@/api/modules/meeting.js";

// const meetingStore = "meetingStore";

export default {
  name: "RegisterPage",
  data() {
    return {
      meetingname: null,
      date: new Date().toISOString().substring(0, 10),
      menu: null,
      time: null,
      menu2: null,
      meeting_place: "",
      curDate: null,
      curTime: null,
    };
  },

  computed: {
    ...mapState("meetingStore", ["regist"]),
    ...mapState("memberStore", ["member_id"]),
  },
  watch: {
    date: {
      handler: function () {
        this.setMeetingDate();
      },
      deep: true,
    },
    time: {
      handler: function () {
        this.setMeetingDate();
      },
      deep: true,
    },
  },
  methods: {
    ...mapActions("meetingStore", [
      "register",
      "setMeeting",
      "setRegistMeeting",
      "resetRegist",
    ]),
    ...mapActions("placeStore", ["resetPlace"]),
    ...mapActions("halfwayStore", ["resetStartPlace"]),

    setMeetingName() {
      this.regist.name = this.meetingname;
      console.log(this.regist);
      this.setRegistMeeting(this.regist);
    },
    setMeetingDate() {
      this.getCurTime();

      console.log(this.date + " " + this.time);
      this.regist.date = this.date;
      this.regist.time = this.time;
      this.setRegistMeeting(this.regist);
    },

    movePlacePage() {
      this.$router.push("/place");
    },

    regist_meeting() {
      this.getCurTime();

      if (this.curDate >= this.date && this.curTime >= this.time) {
        alert("시간을 다시 설정해주세요!");
      } else if (this.meetingname == null || this.meeting_place == null) {
        alert("모든 정보를 입력해주세요!");
      } else {
        console.log(this.date, " ", this.time);
        const date_time = this.date + " " + this.time; //LocalDate 타입에 맞게 변환
        const meeting_name = this.meetingname;
        const place_name = this.regist.place_name;
        const place_addr = this.regist.place_addr;
        const meeting_lat = this.regist.lat;
        const meeting_lng = this.regist.lng;

        const member_id = this.member_id;

        console.log(
          meeting_name,
          date_time,
          place_name,
          place_addr,
          meeting_lat,
          meeting_lng
        );

        this.register({
          member_id,
          meeting_name,
          date_time,
          place_name,
          place_addr,
          meeting_lat,
          meeting_lng,
        }).then(() => {
          getMostRecentMeeting().then((res) => {
            const newRecentMeeting = res;
            const savedRecentMeeting = this.recent_meeting;

            if (
              savedRecentMeeting == null ||
              savedRecentMeeting.meetingTime > newRecentMeeting.meetingTime
            ) {
              this.setMeeting(newRecentMeeting);
            }
          });
        });
        this.resetPlace();
        this.resetStartPlace();
        this.resetRegist();
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
  },

  mounted() {
    // this.date = new Date().toLocaleDateString();

    console.log("@#@#", this.regist);
    if (this.regist.place_name !== null && this.regist.place_addr !== null) {
      this.meeting_place =
        this.regist.place_name + "(" + this.regist.place_addr + ")";
    }
    // console.log(this.member_id);

    this.meetingname = this.regist.name;
    this.date = this.regist.date;
    this.time = this.regist.time;

    // console.log(this.name, " ", this.date, " ", this.time);
    if (this.date === null || this.time === null) {
      this.getCurTime();
      this.date = this.curDate;
      this.time = this.curTime;
    }
  },
};
</script>

<style scoped>
p {
  margin: -3% 0%;
  font-size: 25px;
}

img {
  position: absolute;
  width: 184px;
  height: 200px;
  z-index: 100;
  right: 16px;
  top: 1%;
}

.v-input {
  /* width: 95%; */
  /* text-align: left; */
}
.pr-1 {
  padding: 12px;
}
.row {
  /* width: 100%; */
  padding-right: 3%;
}
</style>
