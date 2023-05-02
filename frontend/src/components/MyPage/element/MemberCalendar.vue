<template>
  <v-sheet class="mt-6 d-flex flex-row align-center" style="width: 100%">
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
  </v-sheet>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "MemberCalendar",
  data() {
    return {
      date: new Date().toISOString().substring(0, 10),
    };
  },
  computed: {
    ...mapState("memberStore", ["attendMeetings"]),
  },
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
