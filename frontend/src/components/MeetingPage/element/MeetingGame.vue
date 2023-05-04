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
    <vue-bottom-sheet ref="gameSheet" max-width="500px" style="width: 100%">
      <!-- 룰렛 이름 & 버튼 -->
      <div>
        <div class="d-flex justify-content-center">
          <div
            class="d-flex align-items-center point-font main-col-1 xxxxxxl-font"
            style="margin-left: 30%"
          >
            돌려돌려 돌림판
          </div>
          <div
            class="d-flex align-center justify-end mr-8"
            style="flex-grow: 1"
          >
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
        <!-- 룰렛 -->
        <div class="d-flex align-center justify-center"><roulette-game /></div>
      </div>
    </vue-bottom-sheet>
  </v-sheet>
</template>

<script>
import RouletteGame from "./RouletteGame.vue";

export default {
  components: { RouletteGame },
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
