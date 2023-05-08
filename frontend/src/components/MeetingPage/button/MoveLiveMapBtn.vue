<template>
  <v-btn color="var(--main-col-1)" icon large @click="moveLivemap">
    <v-icon large>$vuetify.icons.location_outline</v-icon>
  </v-btn>
</template>

<script>
export default {
  name: "MoveLiveMapBtn",
  props: {
    meetingTime: String,
  },
  data() {
    return {
      threeHoursAgoFlag: false, // 모임 시간 3시간 전 check value
    };
  },
  methods: {
    moveLivemap() {
      this.checkMeetingTime();

      if (this.threeHoursAgoFlag) {
        this.$router.push(`/live-map/${this.$route.params.id}`);
      } else {
        // 위치공유 페이지로 이동 불가
      }
    },
    // [@Method] 모임시간 3시간 전 check
    checkMeetingTime() {
      const meetingTime = new Date(this.meetingTime);
      const threeHoursAgoTime = new Date(
        meetingTime.getTime - 3 * 60 * 60 * 1000
      );
      const currentTime = new Date();

      if (currentTime.getTime() >= threeHoursAgoTime.getTime()) {
        this.threeHoursAgoFlag = true;
      }
    },
  },
};
</script>

<style></style>
