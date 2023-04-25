<template>
  <div style="margin: 0px 16px">
    <div
      class="point-font xxxxxxl-font main-col-1"
      style="margin: 20% 0 13% 5%"
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
    <v-card rounded="xl" elevation="5">
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
        <v-text-field dense v-model="meetingname"></v-text-field>
        <span class="point-font xxxxl-font main-col-1 text-left">일시</span>
        <!-- <v-text-field dense></v-text-field> -->
        <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          :return-value.sync="date"
          transition="scale-transition"
          offset-y
          max-width="280px"
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
              hide-details
            ></v-text-field>
          </template>
          <v-date-picker v-model="date" no-title scrollable>
            <v-spacer></v-spacer>
            <v-btn text color="primary" @click="menu = false"> Cancel </v-btn>
            <v-btn text color="primary" @click="$refs.menu.save(date)">
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
            @click:minute="$refs.menu2.save(time)"
            format="24hr"
          ></v-time-picker>
        </v-menu>

        <span class="point-font xxxxl-font main-col-1 text-left">장소</span>
        <v-text-field
          dense
          v-model="meetingplace"
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
      date: null,
      menu: null,
      time: null,
      menu2: null,
      meetingplace: "",
      curDate: null,
      curTime: null,
    };
  },

  computed: {
    ...mapState("placeStore", ["placeX", "placeY", "placeName", "placeAddr"]),
  },
  methods: {
    ...mapActions("meetingStore", ["register"]),
    movePlacePage() {
      this.$router.push("/place");
    },

    async regist_meeting() {
      this.getCurTime();

      console.log(this.curDate, this.date, this.curTime, this.time);
      if (this.curDate >= this.date && this.curTime >= this.time) {
        alert("시간을 다시 설정해주세요!");
      } else if (this.meetingname == null || this.meetingplace == null) {
        alert("모든 정보를 입력해주세요!");
      } else {
        console.log(
          "Input value : ",
          this.meetingname,
          this.date,
          this.time,
          this.meetingplace
        );

        // API 연결 하기
        await this.register(this.meetingname, this.date + " " + this.time);
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
    if (this.placeName !== null && this.placeAddr !== null) {
      this.meetingplace = this.placeName + ", " + this.placeAddr;
    }

    this.getCurTime();
    this.date = this.curDate;
    this.time = this.curTime;
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
  top: 3%;
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
