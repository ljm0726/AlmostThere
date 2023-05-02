<template>
  <v-sheet class="mt-6 align-center" style="width: 100%">
    <!-- <v-date-picker
      class="regular-font v-picker"
      locale="ko"
      v-model="date"
      scrollable
      no-title
      color="var(--main-col-1)"
      :day-format="(date) => new Date(date).getDate()"
      :highlighted="highlightedDates"
    >
    </v-date-picker> -->
    <v-date-picker
      class="regular-font v-picker"
      locale="ko"
      v-model="date"
      scrollable
      no-title
      color="var(--main-col-1)"
      :day-format="(date) => new Date(date).getDate()"
    >
    </v-date-picker>
    <meeting-list-on-day :date="date"></meeting-list-on-day>
    <v-btn @click="check()">확인</v-btn>
  </v-sheet>
</template>

<script>
import { mapState } from "vuex";
import MeetingListOnDay from "./MeetingListOnDay.vue";

export default {
  name: "MemberCalendar",
  data() {
    return {
      date: new Date().toISOString().substring(0, 10), // calendar로 선택한 date
    };
  },
  components: { MeetingListOnDay },
  computed: {
    ...mapState("memberStore", ["attendMeetings"]),
    // highlightedDates() {
    //   return this.attendMeetings.map((attendMeeting) =>
    //     new Date(attendMeeting.meetingDto.meetingTime).toISOString.substring(
    //       0,
    //       10
    //     )
    //   );
    // },
  },
  created() {
    console.log("#21# [created] attendMeetings 확인: ", this.attendMeetings);
  },
  methods: {
    check() {},
  },
};
</script>

<style scoped>
.v-picker {
  flex-direction: row !important;
  width: 100%;
  font-size: large;
}
</style>
