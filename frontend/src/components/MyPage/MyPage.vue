<template>
  <div>
    <div v-if="loading">
      <my-page-loading></my-page-loading>
    </div>
    <div v-else>
      <img
        class="my-page-image"
        src="@/assets/images/page/mypage.png"
        width="30%"
      />
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
import MyPageLoading from "./MyPageLoading.vue";

export default {
  name: "MyPage",
  components: { MemberProfile, MemberSummary, MemberCalendar, MyPageLoading },
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
    // ...mapActions("memberStore", ["excuteGetMemberInfo"]),
    ...mapActions("mypageStore", ["excuteGetMemberInfo"]),
  },
};
</script>

<style scoped>
.my-page-image {
  position: absolute;
  z-index: 2;
  right: 5%;
}
</style>
