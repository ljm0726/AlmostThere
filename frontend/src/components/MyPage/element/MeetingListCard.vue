<template>
  <v-sheet class="d-flex align-center mr-4">
    <v-timeline align-top dense style="margin-left: -25px">
      <v-timeline-item
        v-for="(meeting, index) in onDayMeetings"
        :key="meeting.id"
        :color="colorSet[index % colorSet.length].color"
        small
      >
        <div style="margin-left: -12%; width: 115%; max-width:  width: 115%;">
          <v-card>
            <div :class="`${colorSet[index % colorSet.length].bgColor}`">
              <v-app-bar flat color="rgba(0, 0, 0, 0)">
                <v-toolbar-title class="light-font white--text ml-3">{{
                  meeting.meetingName
                }}</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn color="white" icon>
                  <v-icon>mdi-dots-vertical</v-icon>
                </v-btn>
              </v-app-bar>
            </div>
            <v-card-text class="regular-font">
              <!-- 모임 장소-->
              <div class="d-flex align-center">
                <div>
                  <img
                    height="20"
                    width="15"
                    src="@/assets/images/page/pointer.png"
                  />
                </div>
                <div class="ml-3">{{ meeting.meetingPlace }}</div>
              </div>
              <!-- 모임 시간-->
              <div class="d-flex align-center">
                <div>
                  <img
                    height="20"
                    width="15"
                    src="@/assets/images/icons/clock.png"
                  />
                </div>
                <div class="ml-3">
                  {{ formattedTime(meeting.meetingTime) }}
                </div>
              </div>
            </v-card-text>
          </v-card>
        </div>
      </v-timeline-item>
      <!-- 무한스크롤 감지 -->
      <infinite-loading
        ref="infiniteLoading"
        @infinite="infiniteHandler"
      ></infinite-loading>
    </v-timeline>
  </v-sheet>
</template>

<script>
import InfiniteLoading from "vue-infinite-loading";

export default {
  name: "MeetingListCard",
  props: {
    // 선택한 날짜의 all 모임 list
    meetings: {
      type: Array,
      required: true,
    },
  },
  components: {
    InfiniteLoading,
  },
  data() {
    return {
      onDayMeetings: [], // 무한스크롤을 통해 보여줄 선택한 날짜의 모임 list (2개씩)
      infiniteIndex: 0,
      colorSet: [
        { color: "var(--red-col)", bgColor: "red lighten-1" },
        { color: "var(--yellow-col)", bgColor: "yellow lighten-1" },
        { color: "var(--green-col)", bgColor: "green lighten-1" },
        { color: "var(--blue-col)", bgColor: "blue lighten-1" },
      ],
    };
  },
  watch: {
    meetings() {
      this.initOnDayMeetings();
    },
  },
  created() {
    this.initOnDayMeetings();
  },
  methods: {
    // [@Method] DateTime을 원하는 format으로 변경
    formattedTime(dateTime) {
      const date = new Date(dateTime);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const hour = String(date.getHours() % 12 || 12).padStart(2, "0");
      const minute = String(date.getMinutes()).padStart(2, "0");
      const meridiem = date.getHours() >= 12 ? "오후" : "오전";

      return `${year}년 ${month}월 ${day}일 ${meridiem} ${hour}시 ${minute}분`;
    },
    // [@Method] 모임 List 초기 2개 넣기
    initOnDayMeetings() {
      const onDayMeeting = [];
      if (this.meetings.length > 1) {
        onDayMeeting.push(this.meetings[0]);
        onDayMeeting.push(this.meetings[1]);
        this.infiniteIndex = 1;
      } else if (this.meetings.length == 1) {
        onDayMeeting.push(this.meetings[0]);
        this.infiniteIndex = 0;
      }
      this.onDayMeetings = onDayMeeting;
    },
    // [@Method] 무한스크롤
    infiniteHandler($state) {
      // console.log(
      //   "#21# 무한 스크롤 감지 - onDayMeetings: ",
      //   this.onDayMeetings
      // );

      // meetings 마지막 index라면 다 가져왔다고 판단
      if (this.infiniteIndex == this.meetings.length - 1) {
        $state.complete();
      } else {
        // onDayMeetings 배열에 2개씩 추가
        for (var i = 0; i < 2; i++) {
          if (this.meetings[this.infiniteIndex + 1]) {
            this.infiniteIndex += 1;
            this.onDayMeetings.push(this.meetings[this.infiniteIndex]);
          }
        }
        $state.loaded();
      }
    },
  },
};
</script>

<style></style>
