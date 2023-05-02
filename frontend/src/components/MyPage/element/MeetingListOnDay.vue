<template>
  <v-sheet>모임 리스트 {{ date }}</v-sheet>
</template>

<script>
import { mapState } from "vuex";

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
  computed: {
    ...mapState("memberStore", ["attendMeetings"]),
  },
  methods: {},
};
</script>

<style></style>
