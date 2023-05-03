<template>
  <div>
    <div v-if="loading">로딩 중</div>
    <div v-else class="align-center ml-6 mr-6">
      <member-profile />
      <member-summary />
      <member-calendar />
      <!-- nav 자리 -->
      <v-sheet height="56px"></v-sheet>
    </div>
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
  data() {
    return {
      loading: true,
    };
  },
  created() {
    this.loading = true;
    // [@Method] 회원정보 조회
    this.excuteGetMemberInfo().then((res) => {
      if (res) {
        this.loading = false;
      }
    });
  },
  methods: {
    ...mapActions("memberStore", ["excuteGetMemberInfo"]),
  },
};
</script>

<style></style>
