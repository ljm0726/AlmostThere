<template>
  <v-btn
    class="ma-4"
    outlined
    color="var(--main-col-1)"
    @click="goDetail()"
    rounded
  >
    <!-- <v-icon color="var(--main-col-1)">$vuetify.icons.detail_outline</v-icon> -->
    모임 페이지로 이동
  </v-btn>
</template>

<script>
export default {
  name: "DetailButton",
  methods: {
    goDetail() {
      this.$router.push(`/meeting/${this.$route.params.id}`);
    },
  },
};
</script>

<style></style>
