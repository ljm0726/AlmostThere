<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column" color="transparent">
    <v-btn
      elevation="0"
      outlined
      color="var(--main-col-1)"
      rounded
      @click="open()"
    >
      <img class="mr-1" height="25" src="@/assets/images/icons/roulette.png" />
      <span class="medium-font">돌려돌려 돌림판</span>
    </v-btn>
    <vue-bottom-sheet ref="gameSheet" max-width="500px">
      <h1>Lorem Ipsum</h1>
      <h2>What is Lorem Ipsum?</h2>
      <p><strong>Lorem Ipsum</strong> is simply dummy text</p>
    </vue-bottom-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingGame",
  methods: {
    open() {
      this.$refs.gameSheet.open();
    },
  },
};
</script>

<style></style>
