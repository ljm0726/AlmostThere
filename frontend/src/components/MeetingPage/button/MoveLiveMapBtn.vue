<template>
  <div>
    <v-btn color="var(--main-col-1)" icon large @click="moveLivemap">
      <v-icon large>$vuetify.icons.location_outline</v-icon>
    </v-btn>
    <unable-enter-live-map-btn v-if="!enterTimeCheckFlag" />
  </div>
</template>

<script>
import UnableEnterLiveMapBtn from "./UnableEnterLiveMapBtn.vue";

export default {
  name: "MoveLiveMapBtn",
  props: {
    meetingTime: String,
  },
  data() {
    return {
      enterTimeCheckFlag: false, // 모임 시간 3시간 전/후 check flag
    };
  },
  components: { UnableEnterLiveMapBtn },
  methods: {
    moveLivemap() {
      this.checkMeetingTime();

      if (this.threeHoursAgoFlag) {
        this.$router.push(`/live-map/${this.$route.params.id}`);
      }
    },
    // [@Method] 모임시간 3시간 전/후 check
    checkMeetingTime() {
      const meetingTime = new Date(this.meetingTime);
      const threeHoursAgoTime = new Date(
        meetingTime.getTime - 3 * 60 * 60 * 1000
      );
      const threeHoursAfterTime = new Date(
        meetingTime.getTime + 3 * 60 * 60 * 1000
      );
      const currentTime = new Date();

      // i) 현재 시각이 모임 시간의 3시간 전
      if (currentTime.getTime() >= threeHoursAgoTime.getTime()) {
        this.enterTimeCheckFlag = true;
      }
      // ii) 현재 시각이 모임 시간의 3시간 이내
      else if (currentTime.getTime() <= threeHoursAfterTime.getTime()) {
        this.enterTimeCheckFlag = true;
      }
      // iii) 그 외 시간
      else {
        this.enterTimeCheckFlag = false;
      }
    },
  },
};
</script>

<style></style>
