<template>
  <div>
    <member-profile></member-profile>
    <member-summary></member-summary>
    <!-- nav 자리 -->
    <v-sheet height="56px"></v-sheet>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import MemberProfile from "./element/MemberProfile.vue";
import MemberSummary from "./element/MemberSummary.vue";

export default {
  name: "MyPage",
  components: { MemberProfile, MemberSummary },
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
