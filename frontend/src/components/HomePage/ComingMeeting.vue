<template>
  <v-sheet class="pl-4 mt-6" color="transparent">
    <!-- <div> -->
    <span class="point-font xxxxl-font main-col-1">다가올 모임</span>
    <v-btn class="ml-1" icon x-small @click="show = !show">
      <v-icon>$vuetify.icons.info_outline</v-icon>
    </v-btn>
    <!-- <button>
      <img
        height="15"
        class="ml-2 justify-center align-center"
        :src="require('@/assets/images/icons/info.png')"
        @click="show = !show"
      />
    </button> -->
    <v-tooltip location="right" v-model="show">
      <span class="main-col-1 xxxs-font"
        >지금으로부터 한 달 내의<br />
        나의 모임을 보여줍니다.</span
      >
    </v-tooltip>
    <!-- <swiper
      v-if="meetings.length > 0"
      class="swiper mt-2"
      :options="swiperOption"
    > -->
    <!-- <swiper-slide> -->
    <!-- <div> -->
    <v-sheet
      v-for="(meeting, idx) in meetings"
      :key="idx"
      class="px-3 py-1"
      :class="{
        'upcoming-ellipse': idx % 4 === 0,
        'upcoming-polygon': idx % 4 === 1,
        'upcoming-rectangle': idx % 4 === 2,
        'upcoming-star': idx % 4 === 3,
      }"
    >
      <div class="d-flex flex-row justify-space-between align-center">
        <span
          class="mt-1 extralight-font md-font white-font d-flex flex-row justify-space-between"
        >
          {{ meeting.meetingTime | remainDay }}
        </span>
        <div class="d-flex flex-row justify-end">
          <div
            class="mr-5"
            :class="{
              'mt-5 ellipse rounded-circle': idx % 4 === 0,
              'mt-5 triangle-first': idx % 4 === 1,
              'mt-5 rectangle-first': idx % 4 === 2,
              'mt-4 star-first': idx % 4 === 3,
            }"
          ></div>
          <div
            class="mr-2 mt-2"
            :class="{
              'ellipse rounded-circle': idx % 4 === 0,
              'triangle-second': idx % 4 === 1,
              'rectangle-second': idx % 4 === 2,
              'mr-3 star-second': idx % 4 === 3,
            }"
          ></div>
        </div>
      </div>

      <v-sheet width="91%" class="white-col-1 pa-4" color="transparent">
        <div></div>
        <span class="mt-3 d-flex flex-row bold-font md-font white-font">{{
          meeting.meetingPlace
        }}</span>
        <div class="d-flex flex-row justify-end">
          <span
            class="mt-1 mr-5 xxxxs-font extralight-font white-font d-flex flex-row justify-space-between"
            >{{ meeting.meetingPlace }}</span
          >
        </div>
        <div class="d-flex flex-row justify-end">
          <span
            class="mt-1 mr-5 xxxxs-font extralight-font white-font d-flex flex-row justify-space-between"
            >{{ meeting.meetingTime | formatDate }}</span
          >
        </div>
        <div class="d-flex flex-row justify-end">
          <span
            class="mr-5 xxxxs-font extralight-font white-font d-flex flex-row justify-space-between"
            >{{ meeting.meetingTime | formatTime }}</span
          >
        </div>
      </v-sheet>
    </v-sheet>
    <!-- </div> -->
    <!-- </swiper-slide> -->
    <!-- </swiper> -->
    <div class="none pa-3 mt-2 d-flex justify-center align-center">
      <v-sheet
        width="100%"
        height="100%"
        class="empty white-col-1 pa-4 d-flex justify-end align-center"
        color="transparent"
        max-width="330"
      >
        <!-- <div> -->
        <v-sheet
          class="d-flex flex-row justify-end align-center"
          color="transparent"
          height="100%"
        >
          <!-- <div class="d-flex justify-center align-center"> -->
          <!-- <img
                :src="require('@/assets/images/component/cards.png')"
                class="mt-1 extralight-font md-font white-font"
                height="88"
              /> -->
          <!-- </div> -->
          <div class="d-flex flex-column align-start">
            <!-- <div class="ml-2 mt-3"> -->
            <span class="semibold-font xl-font white-font">
              다가올 모임이 없습니다.
            </span>
            <!-- </div> -->
            <div class="d-flex flex-column regular-font xxs-font white-font">
              <!-- <pre class="ml-2 mt-2 regular-font xxs-font white-font">
              스터디, 식사 약속 등
              다양한 모임을 잡아보세요!</pre
              > -->
              <span>지금 바로</span>
              <span>스터디, 식사 약속 등</span>
              <span>다양한 모임을 잡아보세요!</span>
            </div>
            <!-- <v-chip class="ma-2" small color="white" text-color="main-col-1">
              <span class="ma-3 regular-font xxxs-font"> 모임 방 만들기 </span>
            </v-chip> -->
            <v-btn class="mt-1" small rounded @click="goRegister()">
              <span class="px-5 regular-font xxxs-font main-col-1">
                모임 방 만들기
              </span>
            </v-btn>
          </div>
        </v-sheet>
        <!-- </div> -->
      </v-sheet>
    </div>
    <!-- </div> -->
  </v-sheet>
</template>

<script>
import { getUpcomingMeetings } from "@/api/modules/meeting.js";
import moment from "moment";
export default {
  name: "ComingMeeting",
  data() {
    return {
      meetings: [],
      swiperOption: {
        slidesPerView: "auto",
      },
      show: false,
    };
  },
  created() {
    getUpcomingMeetings().then((res) => {
      this.meetings = res;
    });
  },
  methods: {
    goRegister() {
      this.$router.push("/register");
    },
  },
  filters: {
    formatDate(value) {
      const date = new Date(value);
      const year = date.getFullYear();
      const month = date.getMonth();
      const day = date.getDate();
      const result = `${year}년 ${
        month + 1 < 10 ? `0${month + 1}` : month + 1
      }월 ${day < 10 ? `0${day}` : day}일`;
      return result;
    },
    formatTime(value) {
      const date = new Date(value);
      const hour = date.getHours();
      const min = date.getMinutes();
      const result = `${
        hour >= 12
          ? `오후 ${hour == 12 ? `${hour}` : hour - 12}`
          : `오전 ${hour}`
      }시 ${min < 10 ? `0${min}` : min}분`;
      return result;
    },
    remainDay(value) {
      const now = moment();
      const time = moment(value, "YYYY-MM-DD hh:mm:ss");
      const hour = moment.duration(time.diff(now)).days();
      return `D-${hour}`;
    },
  },
};
</script>

<style scoped>
.empty {
  background-image: url("@/assets/images/component/cards.png");
  background-size: 120px;
  background-position-x: left;
  background-position-y: center;
}
/* .swiper-slide {
  width: 33%;
} */
.upcoming-ellipse {
  width: 160px;
  height: 150px;

  background: linear-gradient(
    180deg,
    rgba(243, 122, 127, 0.9) 0%,
    #f24e55 100%
  );
  box-shadow: 0px 5px 20px -10px #f37a7f;
  border-radius: 15px;
}
.upcoming-polygon {
  width: 160px;
  height: 150px;

  background: linear-gradient(180deg, rgba(245, 186, 85, 0.9) 0%, #f4a41a 100%);
  box-shadow: 0px 5px 20px -10px #f4a41a;
  border-radius: 15px;
}
.upcoming-rectangle {
  width: 160px;
  height: 150px;

  background: linear-gradient(180deg, rgba(43, 192, 161, 0.9) 0%, #00a381 100%);
  box-shadow: 0px 5px 20px -10px #2bc0a1;
  border-radius: 15px;
}
.upcoming-star {
  width: 160px;
  height: 150px;

  background: linear-gradient(180deg, rgba(28, 128, 223, 0.9) 0%, #005fba 100%);
  box-shadow: 0px 5px 20px -10px #005fba;
  border-radius: 15px;
}
.none {
  height: 175px;
  /* width: 353px; */
  /* height: 150px; */
  background: linear-gradient(180deg, rgba(9, 42, 73, 0.8) 0%, #092a49 100%);
  box-shadow: 0px 5px 20px -10px #000000;
  border-radius: 15px;
}
.ellipse {
  position: absolute;
  width: 35px;
  height: 35px;
  background: rgba(255, 255, 255, 0.3);
}
.rectangle-first {
  position: absolute;
  width: 31px;
  height: 31px;
  background: rgba(255, 255, 255, 0.3);
}
.rectangle-second {
  position: absolute;
  width: 31px;
  height: 31px;
  background: rgba(217, 255, 247, 0.3);
}
.triangle-first {
  position: absolute;
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 0 17.5px 35px 17.5px;
  border-color: transparent transparent rgba(255, 255, 255, 0.3) transparent;
  transform: rotate(-14.43deg);
}
.triangle-second {
  position: absolute;
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 0 17.5px 35px 17.5px;
  border-color: transparent transparent rgba(250, 235, 211, 0.3) transparent;
  transform: rotate(9.24deg);
}
.star-first {
  position: absolute;
  width: 35px;
  height: 35px;
  clip-path: polygon(
    50% 5%,
    61% 40%,
    98% 40%,
    68% 62%,
    79% 96%,
    50% 75%,
    21% 96%,
    32% 62%,
    2% 40%,
    39% 40%
  );
  background: rgba(255, 255, 255, 0.3);
  transform: rotate(-8.97deg);
}
.star-second {
  position: absolute;
  width: 35px;
  height: 35px;
  clip-path: polygon(
    50% 5%,
    61% 40%,
    98% 40%,
    68% 62%,
    79% 96%,
    50% 75%,
    21% 96%,
    32% 62%,
    2% 40%,
    39% 40%
  );
  background: rgba(168, 213, 255, 0.3);
  transform: rotate(3.72deg);
}
.line {
  width: 30%;
  height: 0.5px;
  background: #fff;
}

.v-tooltip__content {
  position: absolute;
  width: 160px;
  height: 70px;
  left: 86px !important;
  top: 590px !important;
  padding: 12px;
  background: #ffffff;
  -webkit-border-radius: 10px;
  -moz-border-radius: 10px;
  border-radius: 10px;
  border: #092a49 solid 1px;
  display: flex;
  justify-content: center;
  align-content: center;
}

.v-tooltip__content:after {
  content: "";
  position: absolute;
  border-style: solid;
  border-width: 0 8px 9px;
  border-color: #ffffff transparent;
  display: block;
  width: 0;
  z-index: 1;
  top: -9px;
  left: 35px;
}

.v-tooltip__content:before {
  content: "";
  position: absolute;
  border-style: solid;
  border-width: 0 8px 9px;
  border-color: #092a49 transparent;
  display: block;
  width: 0;
  z-index: 0;
  top: -10px;
  left: 35px;
}
</style>
