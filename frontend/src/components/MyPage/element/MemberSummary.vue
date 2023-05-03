<template>
  <div>
    <div class="image-container">
      <!-- image -->
      <img height="180" width="180" src="@/assets/images/dialog/my-page.png" />
    </div>

    <v-sheet
      class="mt-5 pa-4 d-flex flex-column detail-shadow"
      height="125"
      outlined
      rounded="xl"
    >
      <!-- text -->
      <div class="light-font xs-font">
        <!-- 이번달 모임개수 -->
        <div class="d-flex align-center mb-1 mt-4">
          <span
            ><img height="15" width="15" src="@/assets/images/dialog/check.png"
          /></span>
          <span class="ml-2"
            >이번달에
            <b class="main-col-1 bold-font"
              >{{ this.thisMonthAttendMeetingCnt }}번</b
            >의 모임을 잡으셨네요.</span
          >
        </div>
        <!-- 누적 지각횟수 -->
        <div class="d-flex align-center mb-1">
          <span
            ><img height="15" width="15" src="@/assets/images/dialog/check.png"
          /></span>
          <span class="ml-2"
            >누적
            <b class="main-col-1 bold-font"
              >{{ this.attendMeetings.length }}번</b
            >의 약속 중
            <b class="main-col-1 bold-font">{{ this.totalLateCnt }}번</b>
            지각하셨습니다.</span
          >
        </div>
        <!-- 지난달 총 소비금액 -->
        <div class="d-flex align-center">
          <span
            ><img height="15" width="15" src="@/assets/images/dialog/check.png"
          /></span>
          <span class="ml-2"
            >지난달 모임에
            <b class="main-col-1 bold-font"
              >{{ this.lastMonthTotalSpentMoney.toLocaleString("ko-KR") }}원</b
            >을 소비했습니다.</span
          >
        </div>
      </div>
    </v-sheet>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "MemberSummary",
  data() {
    return {};
  },
  computed: {
    ...mapState("memberStore", [
      "attendMeetings",
      "thisMonthAttendMeetingCnt",
      "totalLateCnt",
      "lastMonthTotalSpentMoney",
    ]),
  },
  methods: {},
};
</script>

<style scoped>
.image-container {
  position: absolute;
  margin-left: 45%;
  margin-top: -33%;
}
</style>
