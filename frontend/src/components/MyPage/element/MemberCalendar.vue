<template>
  <div>캘린더</div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "MemberCalendar",
  data() {
    return {};
  },
  computed: {
    ...mapState("memberStore", ["attendMeetings"]),
  },
  methods: {},
};
</script>

<style></style>
