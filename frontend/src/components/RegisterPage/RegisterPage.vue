<template>
  <div style="margin: 0px 16px; margin-bottom: 76px" class="main-form">
    <div
      class="point-font xxxxxxl-font main-col-1"
      style="margin: 15% 0 13% 5%"
    >
      <p>많은 사람들과</p>
      <p>모임을</p>
      <p>잡아보세요!</p>
      <img
        src="@/assets/images/page/register.png"
        height="450"
        style="text-aling: center"
        alt=""
      />
    </div>
    <!-- nav 자리 -->
    <v-card rounded="xl" elevation="5" height="100%">
      <v-card-title>
        <!-- <img src="@/assets/images/dialog/logout.png" width="60%" /> -->
        <!-- <span class="point-font xxxxl-font main-col-1 text-left">제목</span>
        <v-text-field dense></v-text-field>
        <span class="point-font xxxxl-font main-col-1 text-left">일시</span>
        <span class="point-font xxxxl-font main-col-1 text-left">장소</span>
        <v-text-field dense></v-text-field> -->
      </v-card-title>
      <v-card-text style="padding: 38px 24px 0 24px">
        <!-- <img src="@/assets/images/dialog/logout.png" width="60%" /> -->
        <span class="point-font xxxxl-font main-col-1 text-left">제목</span>
        <v-text-field
          dense
          v-model="meetingname"
          @keyup="setMeetingName"
        ></v-text-field>
        <span class="point-font xxxxl-font main-col-1 text-left">일시</span>
        <!-- <v-text-field dense></v-text-field> -->
        <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          :return-value.sync="date"
          transition="scale-transition"
          offset-y
          min-width="280px"
          :nudge-right="40"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="date"
              label="날짜"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
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
          >
            <v-spacer></v-spacer>
            <v-btn
              text
              class="bold-font xl-font main-col-1"
              @click="menu = false"
            >
              Cancel
            </v-btn>
            <v-btn
              text
              class="bold-font xl-font main-col-1"
              @click="$refs.menu.save(date)"
            >
              OK
            </v-btn>
          </v-date-picker>
        </v-menu>

        <v-menu
          ref="menu2"
          v-model="menu2"
          :close-on-content-click="false"
          :nudge-right="40"
          :return-value.sync="time"
          transition="scale-transition"
          offset-y
          max-width="280px"
          min-width="280px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="time"
              label="시간"
              prepend-icon="mdi-clock-time-four-outline"
              readonly
              v-bind="attrs"
              v-on="on"
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
        </v-menu>

        <span class="point-font xxxxl-font main-col-1 text-left">장소</span>
        <v-text-field
          dense
          v-model="meeting_place"
          @click="movePlacePage"
        ></v-text-field>

        <v-row>
          <v-col class="pr-1">
            <v-btn
              elevation="0"
              color="var(--main-col-1)"
              dark
              rounded
              block
              @click="regist_meeting"
              >등록</v-btn
            >
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
// 추가해야할 부분 -> 현재 시간 이전을 설정하면 불가능하게
import { mapActions, mapState } from "vuex";
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
    ...mapState("meetingStore", [
      "meeting_name",
      "meeting_date",
      "meeting_time",
      "place_name",
      "place_addr",
    ]),
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
    ...mapActions("meetingStore", ["register"]),
    ...mapActions("meetingStore", [
      "SET_MEETING_NAME",
      "SET_MEETING_DATE",
      "SET_MEETING_TIME",
    ]),

    setMeetingName() {
      console.log(this.meetingname);
      this.SET_MEETING_NAME(this.meetingname);
    },
    setMeetingDate() {
      this.getCurTime();

      console.log(this.date + " " + this.time);
      this.SET_MEETING_DATE(this.date);
      this.SET_MEETING_TIME(this.time);
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
        const date_time = new Date(this.date + " " + this.time); //LocalDate 타입에 맞게 변환
        const meeting_name = this.meetingname;
        const place_name = this.place_name;
        const place_addr = this.place_addr;

        console.log(meeting_name, " ", date_time);

        this.register({ meeting_name, date_time, place_name, place_addr });
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
  },

  mounted() {
    // this.date = new Date().toLocaleDateString();
    if (this.place_name !== null && this.place_addr !== null) {
      this.meeting_place = this.place_name + ", " + this.place_addr;
    }

    console.log(
      this.meeting_name,
      " ",
      this.meeting_date,
      " ",
      this.meeting_time
    );

    this.meetingname = this.meeting_name;
    this.date = this.meeting_date;
    this.time = this.meeting_time;

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
  text-align: left;
}
.pr-1 {
  padding: 12px;
}
.row {
  /* width: 100%; */
  padding-right: 3%;
}
</style>
