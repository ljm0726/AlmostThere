<template>
  <v-sheet class="d-flex align-center">
    <v-timeline align-top dense style="margin-left: -20px">
      <v-timeline-item
        v-for="(meeting, index) in meetings"
        :key="meeting.id"
        :color="colorSet[index % colorSet.length].color"
        small
      >
        <div>
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
    </v-timeline>
    <!-- 무한스크롤 감지 -->
    <!-- <div
      v-infinite-scroll="loadMore"
      infinite-scroll-disabled="loading"
      infinite-scroll-distance="10"
    ></div> -->
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingListCard",
  props: {
    meetings: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      colorSet: [
        { color: "var(--red-col)", bgColor: "red lighten-1" },
        { color: "var(--yellow-col)", bgColor: "yellow lighten-1" },
        { color: "var(--green-col)", bgColor: "green lighten-1" },
        { color: "var(--blue-col)", bgColor: "blue lighten-1" },
      ],
    };
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
  },
};
</script>

<style></style>
