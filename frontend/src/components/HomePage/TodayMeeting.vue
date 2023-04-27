<template>
  <v-sheet class="px-4 mt-6" color="transparent">
    <div>
      <span class="point-font xxxxl-font main-col-1">오늘의 모임</span>
      <button>
        <img
          height="15"
          class="ml-2 justify-center align-center"
          :src="require('@/assets/images/icons/info.png')"
          @click="show = !show"
        />
      </button>
      <v-tooltip location="right" v-model="show">
        <span class="main-col-1 xxxs-font"
          >지금으로부터 24시간 내의<br />
          나의 모임을 보여줍니다.</span
        >
      </v-tooltip>
      <swiper
        v-if="meetings.length > 0"
        class="swiper mt-2"
        :options="swiperOption"
      >
        <swiper-slide v-for="(meeting, idx) in meetings" :key="idx">
          <div>
            <meeting-card :meeting="meeting"></meeting-card>
          </div>
        </swiper-slide>
      </swiper>
      <v-card
        v-else
        width="353px"
        height="176px"
        class="mt-2 d-flex justify-center align-center"
        elevation="0"
        style="
          border: 1px solid #092a49;
          border-radius: 15px;
          box-shadow: 0px 5px 20px -10px #092a49;
        "
      >
        <v-sheet width="91%" class="white-col-1 pa-4" color="transparent">
          <div>
            <div class="d-flex flex-row justify-space-between">
              <div>
                <div class="ml-2 mt-3">
                  <span class="semibold-font xl-font main-col-1"
                    >오늘 모임이 없습니다.</span
                  >
                </div>
                <div>
                  <pre class="ml-2 mt-2 regular-font xxs-font main-col-1">
남는 시간이 있다면
친구들과 번개 약속을
잡아보는 건 어떠신가요?</pre
                  >
                </div>
                <v-chip class="ma-2" small color="#092A49" text-color="white"
                  ><span class="ma-3 regular-font xxxs-font"
                    >모임 방 만들기</span
                  ></v-chip
                >
              </div>
              <div class="d-flex justify-center align-center">
                <img
                  :src="require('@/assets/images/component/calendar.png')"
                  class="mt-1 extralight-font md-font white-font"
                  width="118"
                  height="142"
                />
              </div>
            </div>
          </div>
        </v-sheet>
      </v-card>
    </div>
  </v-sheet>
</template>

<script>
import { getTodayMeetings } from "@/api/modules/meeting.js";
import MeetingCard from "./element/MeetingCard.vue";

export default {
  name: "TodayMeeting",
  components: {
    MeetingCard,
  },
  data() {
    return {
      swiperOption: {
        slidesPerView: "auto",
        spaceBetween: 3,
      },
      meetings: [],
      show: false,
    };
  },
  created() {
    getTodayMeetings().then((res) => {
      this.meetings = res;
    });
  },
};
</script>
<style scoped>
.swiper-slide {
  width: 70%;
}
.v-tooltip__content {
  position: absolute;
  width: 160px;
  height: 70px;
  left: 84px !important;
  top: 350px !important;
  padding: 12px;
  background: #ffffff;
  -webkit-border-radius: 10px;
  -moz-border-radius: 10px;
  border-radius: 15px;
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
