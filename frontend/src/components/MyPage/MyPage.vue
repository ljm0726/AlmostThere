<template>
  <div>
    <member-profile></member-profile>
    <!-- nav 자리 -->
    <v-sheet height="56px"></v-sheet>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import MemberProfile from "./element/MemberProfile.vue";

export default {
  name: "MyPage",
  components: { MemberProfile },
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
