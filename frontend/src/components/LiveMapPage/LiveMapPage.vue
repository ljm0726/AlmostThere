<template>
  <div class="live-map">
    <!-- kakao map -->
    <div class="live-map" v-if="enterTimeCheckFlag">
      <arrive-and-chat-btn
        @resizeMapLevel="resizeMapLevel"
      ></arrive-and-chat-btn>
      <live-map ref="livemap"></live-map>
    </div>
    <!-- 진입 불가 -->
    <div v-else>
      <v-dialog
        v-model="dialog"
        scrollable
        max-width="300px"
        rounded="xl"
        persistent
      >
        <v-card rounded="xl">
          <v-card-title class="d-flex flex-column">
            <div class="align-self-end">
              <close-button @closeDialog="closeDialog"></close-button>
            </div>
            <img src="@/assets/images/dialog/dont_enter.png" width="60%" />
            <span class="point-font xxxxxxl-font main-col-1">
              위치 공유 불가</span
            >
            <span class="extralight-font sm-font mt-2"
              >멤버들과 실시간 위치 공유는
            </span>
            <span class="extralight-font sm-font mb-2"
              >모임 시간 전후 3시간 동안만 지원됩니다.</span
            >
          </v-card-title>
          <v-card-text>
            <v-row>
              <v-col class="pl-1">
                <v-btn
                  elevation="0"
                  color="white"
                  outlined
                  dark
                  rounded
                  @click="closeDialog"
                  block
                  style="background-color: var(--main-col-1)"
                  >뒤로가기</v-btn
                >
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>

<script>
import LiveMap from "./element/LiveMap.vue";
import ArriveAndChatBtn from "./element/ArriveAndChatBtn.vue";

export default {
  name: "LiveMapPage",
  components: { LiveMap, ArriveAndChatBtn },
  data() {
    return {
      meetingTime: null,
      enterTimeCheckFlag: false, // 모임 시간 3시간 전/후 check flag
      dialog: true,
    };
  },
  created() {
    this.meetingTime = this.$route.query.time;
    this.checkMeetingTime();
  },
  methods: {
    resizeMapLevel() {
      this.$refs.livemap.resizeMapLevel();
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
        this.dialog = false;
      }
      // ii) 그 외 시간
      else {
        this.enterTimeCheckFlag = false;
        this.dialog = true;
      }
    },
    // [@Method] 뒤로 돌아가기
    closeDialog() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
.live-map {
  width: 100%;
  height: 100%;
}
.live-map > div:not(.v-sheet) {
  height: 100%;
}

span.extralight-font.sm-font {
  line-height: 1.5;
}
</style>
