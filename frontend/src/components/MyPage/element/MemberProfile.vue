<template>
  <v-sheet class="my-2 d-flex flex-row align-center">
    <!-- 회원 프로필 -->
    <v-card style="border-radius: 18px" elevation="0">
      <v-img
        class="align-end"
        height="90"
        width="90"
        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
        :src="this.member.memberProfileImg"
      >
      </v-img>
    </v-card>
    <div class="ml-5 xxxxl-font thin-font main-col-1">
      안녕하세요!
      <div>
        <span class="bold-font">{{ this.member.memberNickname }}</span
        >님
      </div>
      <!-- 로그아웃 -->
      <div>
        <v-btn
          style="font-size: 12px"
          color="var(--main-col-1)"
          dark
          rounded
          small
          @click="logout()"
        >
          로그아웃
        </v-btn>
        <logout-dialog ref="logout"></logout-dialog>
      </div>
    </div>
  </v-sheet>
</template>

<script>
import { mapState } from "vuex";
import LogoutDialog from "./LogoutDialog.vue";

export default {
  name: "MemberProfile",
  data() {
    return {};
  },
  computed: {
    ...mapState("memberStore", ["member"]),
  },
  components: { LogoutDialog },
  methods: {
    logout() {
      this.$refs.logout.openDialog();
    },
  },
};
</script>

<style></style>
