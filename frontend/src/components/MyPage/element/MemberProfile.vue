<template>
  <v-sheet class="mx-5 d-flex flex-row align-center">
    <!-- 회원 프로필 -->
    <v-card rounded="xl" elevation="0">
      <v-img
        class="align-end"
        height="80"
        width="80"
        :src="member.memberProfileImg"
      >
      </v-img>
    </v-card>
    <div class="ml-4 xl-font thin-font semi2narrow-font">
      <span>안녕하세요!</span>
      <div>
        <span class="bold-font main-col-1">
          {{ member.memberNickname }}
        </span>
        <span>님</span>
      </div>
      <!-- 로그아웃 -->
      <v-btn
        class="px-4 mt-1"
        color="var(--main-col-1)"
        dark
        rounded
        x-small
        @click="logout()"
      >
        로그아웃
      </v-btn>
      <logout-dialog ref="logout"></logout-dialog>
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
