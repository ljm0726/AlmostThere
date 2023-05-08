<template>
  <div>
    <v-btn color="var(--main-col-1)" icon large @click="moveLivemap">
      <v-icon large>$vuetify.icons.location_outline</v-icon>
    </v-btn>
    <!-- 실시간 위치 공유 페이지 접근불가 -->
    <v-card rounded="xl" v-if="dialog">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <img src="@/assets/images/dialog/dont_enter.png" width="60%" />
        <span class="point-font xxxxxxl-font main-col-1"> 위치 공유 불가</span>
        <span class="extralight-font sm-font"
          >멤버들과 실시간 위치 공유는
          <p></p>
          모임 시간 전후 3시간 동안만 지원됩니다.</span
        >
      </v-card-title>
      <v-card-text>
        <v-row>
          <v-col class="pl-1">
            <v-btn
              elevation="0"
              color="var(--main-col-1)"
              outlined
              dark
              rounded
              @click="closeDialog"
              block
              >확인</v-btn
            >
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "MoveLiveMapBtn",
  props: {
    meetingTime: String,
  },
  data() {
    return {
      enterTimeCheckFlag: false, // 모임 시간 3시간 전/후 check flag
      dialog: false,
    };
  },
  methods: {
    moveLivemap() {
      this.checkMeetingTime();
      console.log("#21# 3시간 전/후 check: ", this.enterTimeCheckFlag);

      if (this.enterTimeCheckFlag) {
        this.$router.push(`/live-map/${this.$route.params.id}`);
      } else {
        this.dialog = true;
      }
    },
    // [@Method] 모임시간 3시간 전/후 check
    checkMeetingTime() {
      const meetingTime = new Date(this.meetingTime);
      const threeHoursAgoTime = new Date(
        meetingTime.getTime() - 3 * 60 * 60 * 1000
      );
      const threeHoursAfterTime = new Date(
        meetingTime.getTime() + 3 * 60 * 60 * 1000
      );
      const currentTime = new Date();
      // console.log("#21# 3시간 전: ", threeHoursAgoTime);
      // console.log("#21# 3시간 후: ", threeHoursAfterTime);

      // i) 현재 시각이 모임 시간의 3시간 전/후 이내
      if (
        threeHoursAgoTime.getTime() <= currentTime.getTime() &&
        currentTime.getTime() <= threeHoursAfterTime.getTime()
      ) {
        this.enterTimeCheckFlag = true;
      }
      // ii) 그 외 시간
      else {
        this.enterTimeCheckFlag = false;
      }
    },
    closeDialog() {
      this.dialog = false;
    },
  },
};
</script>

<style></style>
