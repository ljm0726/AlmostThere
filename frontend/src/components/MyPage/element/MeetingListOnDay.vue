<template>
  <v-sheet
    >모임 리스트 {{ date }}
    <div v-if="meetings.length == 0"><no-meeting /></div>
    <div v-else>모임있음</div>
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
  methods: {},
};
</script>

<style></style>
