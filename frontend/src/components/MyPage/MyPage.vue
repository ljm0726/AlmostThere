<template>
  <div>
    <v-btn color="var(--main-col-1)" dark rounded @click="logout()">
      로그아웃
    </v-btn>
    <logout-dialog ref="logout"></logout-dialog>
    <!-- nav 자리 -->
    <v-sheet height="56px"></v-sheet>
  </div>
</template>

<script>
import LogoutDialog from './LogoutDialog.vue'

export default {
  name: "MyPage",
  components: { LogoutDialog },
  methods: {
    logout() {
      this.$refs.logout.openDialog();
    }
  }
}
</script>

<style>

</style>