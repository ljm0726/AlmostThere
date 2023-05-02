<template>
  <div class="ml-6 mr-6">
    <member-profile />
    <member-summary />
    <member-calendar />
    <!-- nav 자리 -->
    <v-sheet height="56px"></v-sheet>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import MemberProfile from "./element/MemberProfile.vue";
import MemberSummary from "./element/MemberSummary.vue";
import MemberCalendar from "./element/MemberCalendar.vue";

export default {
  name: "MyPage",
  components: { MemberProfile, MemberSummary, MemberCalendar },
  created() {
    // [@Method] 회원정보 조회
    this.excuteGetMemberInfo();
  },
  methods: {
    ...mapActions("memberStore", ["excuteGetMemberInfo"]),
  },
};
</script>

<style></style>
