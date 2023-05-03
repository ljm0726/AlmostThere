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
      <!-- 룰렛 -->
      <div class="d-flex justify-center align-center">
        <div class="d-flex flex-row">
          <div class="d-flex align-center point-font main-col-1 xxxxxl-font">
            돌려돌려 돌림판
          </div>
          <!-- <div class="d-flex align-center justify-center"> -->
          <div class="d-flex align-center justify-end">
            <v-btn
              id="round-small-btn"
              outlined
              color="var(--main-col-1)"
              @click="addOption()"
              icon
              x-small
            >
              <v-icon color="var(--main-col-1)">$vuetify.icons.plus</v-icon>
            </v-btn>
          </div>
        </div>
      </div>
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
    // [@Method] 룰렛 옵션 추가
    addOption() {},
  },
};
</script>

<style></style>
