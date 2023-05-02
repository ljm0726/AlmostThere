<template>
  <v-sheet class="mt-6 align-center" style="width: 100%">
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
  </v-sheet>
</template>

<script>
import MeetingListOnDay from "./MeetingListOnDay.vue";

export default {
  name: "MemberCalendar",
  data() {
    return {
      date: new Date().toISOString().substring(0, 10),
    };
  },
  components: { MeetingListOnDay },
  methods: {},
};
</script>

<style scoped>
.v-picker {
  flex-direction: row !important;
  width: 100%;
  font-size: large;
}
</style>
