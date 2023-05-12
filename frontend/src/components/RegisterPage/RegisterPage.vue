<template>
  <div style="margin-bottom: 76px" class="mx-5 main-form">
    <v-sheet class="point-font xxxxxxl-font main-col-1">
      <div class="d-flex flex-column semi2narrow-font">
        <span>많은 사람들과</span>
        <span>모임을 잡아보세요!</span>
        <span></span>
      </div>
    </v-sheet>
    <v-sheet elevation="0" class="mt-3">
      <!-- 제목 -->
      <v-sheet width="100%" class="d-flex flex-row" elevation="1" rounded>
        <v-sheet
          width="80"
          color="var(--red-col)"
          class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font xl-font main-col-1"
        >
          <span>제목</span>
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
      <v-sheet width="100%" class="mt-3 d-flex flex-row" elevation="1" rounded>
        <v-sheet
          width="80"
          color="var(--yellow-col)"
          class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font xl-font main-col-1"
        >
          <span>날짜</span>
        </v-sheet>
        <v-dialog
          ref="menu"
          v-model="menu"
          :return-value.sync="date"
          width="290px"
        >
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
        </v-dialog>
      </v-sheet>

      <!-- 시간 -->
      <v-sheet width="100%" class="mt-3 d-flex flex-row" elevation="1" rounded>
        <v-sheet
          width="80"
          color="var(--green-col)"
          class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font xl-font main-col-1"
        >
          시간
        </v-sheet>
        <v-dialog
          ref="menu2"
          v-model="menu2"
          :return-value.sync="time"
          width="290px"
        >
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
        </v-dialog>
      </v-sheet>

      <!-- 장소 -->
      <v-sheet width="100%" class="mt-3 d-flex flex-row" elevation="1" rounded>
        <v-sheet
          width="80"
          color="var(--blue-col)"
          class="rounded-l pt-1 d-flex flex-row align-center justify-center point-font white-font xl-font main-col-1"
        >
          장소
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
      <v-btn
        elevation="3"
        color="var(--main-col-1)"
        dark
        block
        class="mt-3 md-font"
        @click="regist_meeting"
      >
        모임 등록하기
      </v-btn>
    </v-sheet>
    <time-error ref="time"></time-error>
    <input-error ref="input"></input-error>
  </div>
</template>

<script>
// 추가해야할 부분 -> 현재 시간 이전을 설정하면 불가능하게
import { mapActions, mapState } from "vuex";
import { getMostRecentMeeting } from "@/api/modules/meeting.js";
import InputError from "./dialog/InputError.vue";
import TimeError from "./dialog/TimeError.vue";

// const meetingStore = "meetingStore";

export default {
  components: { InputError, TimeError },
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
        // alert("시간을 다시 설정해주세요!");
        this.$refs.time.openDialog();
      } else if (this.meetingname == null || this.meeting_place == null) {
        // alert("모든 정보를 입력해주세요!");
        this.$refs.input.openDialog();
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
