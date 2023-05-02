<template>
  <v-sheet
    >모임 리스트 {{ date }}
    <div v-if="meetings.length == 0"><no-meeting /></div>
    <div v-else>모임있음 {{ this.meetings }}</div>
  </v-sheet>
</template>

<script>
import { mapState } from "vuex";
import NoMeeting from "./NoMeeting.vue";

export default {
  name: "MeetingListDay",
  props: {
    date: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      meetings: [],
    };
  },
  components: { NoMeeting },
  computed: {
    ...mapState("memberStore", ["attendMeetings"]),
  },
  watch: {
    date() {
      this.getOnDayMeetings();
    },
  },
  created() {},
  methods: {
    getOnDayMeetings() {
      console.log("#21# 해당 날짜 모임조회 동작");

      const onDayMeeting = [];
      this.meetings = this.attendMeetings.map((meeting) => {
        const meetingDate = meeting.meetingDto.meetingTime.substring(0, 10);
        // console.log("#21# 모임날짜 vs 선택한 날짜: ", meetingDate, this.date);

        // 현재 선택한 날짜의 모임 조회
        if (meetingDate == this.date) {
          onDayMeeting.push(meeting.meetingDto);
        }
      });
      this.meetings = onDayMeeting;
    },
  },
};
</script>

<style></style>
