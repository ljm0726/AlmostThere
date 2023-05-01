<template>
  <div>
    <member-profile></member-profile>
    <!-- nav 자리 -->
    <v-sheet height="56px"></v-sheet>
  </div>
</template>

<script>
import MemberProfile from "./element/MemberProfile.vue";

export default {
  name: "MyPage",
  components: { MemberProfile },
  methods: {
    logout() {
      this.$refs.logout.openDialog();
    },
  },
};
</script>

<style></style>
