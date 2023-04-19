<template>
  <v-sheet class="d-flex flex-column">
    <home-banner></home-banner>
    <v-btn @click="openBottomSheet">테스트 중</v-btn>
    <bottom-sheet ref="bottomsheet"></bottom-sheet>
    <today-meeting></today-meeting>
    <coming-meeting></coming-meeting>
    <close-friends></close-friends>
    <!-- nav 자리 -->
    <v-sheet height="56px"></v-sheet>
  </v-sheet>
</template>

<script>
import BottomSheet from "@/common/component/BottomSheet.vue";
import CloseFriends from "./CloseFriends.vue";
import ComingMeeting from "./ComingMeeting.vue";
import HomeBanner from "./HomeBanner.vue";
import TodayMeeting from "./TodayMeeting.vue";

export default {
  name: "HomePage",
  components: {
    ComingMeeting,
    TodayMeeting,
    HomeBanner,
    CloseFriends,
    BottomSheet,
  },
  methods: {
    openBottomSheet() {
      this.$refs.bottomsheet.open();
    },
  },
};
</script>

<style></style>
