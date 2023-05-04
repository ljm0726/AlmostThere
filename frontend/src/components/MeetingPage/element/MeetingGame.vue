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
        <!-- 룰렛 options -->
        <div class="d-flex align-center justify-center flex-wrap mt-3">
          <div
            class="d-flex align-center justify-center"
            v-for="(option, index) in options"
            :key="index"
            style="margin: 5px"
          >
            <v-text-field
              v-model="options[index]"
              outlined
              dense
              hide-details
              style="max-width: 150px"
              ><template v-slot:append>
                <v-icon small @click="removeOption(index)">mdi-close</v-icon>
              </template>
            </v-text-field>
          </div>
          <!-- 룰렛 options -->
          <!-- <div
          class="d-flex align-center justify-center"
          v-if="this.options.length > 1>" -->
          <div class="d-flex align-center justify-center">
            <roulette-game :options="options" />
          </div>
        </div>
      </div>
    </vue-bottom-sheet>
  </v-sheet>
</template>

<script>
import RouletteGame from "./RouletteGame.vue";

export default {
  components: { RouletteGame },
  name: "MeetingGame",
  data() {
    return {
      options: [],
    };
  },
  watch: {
    options() {
      console.log("#21# options 확인: ", this.options);
    },
  },
  methods: {
    open() {
      this.$refs.gameSheet.open();
    },
    // [@Method] 룰렛 옵션 추가
    addOption() {
      // 옵션 최대 개수 10개로 제한
      if (this.options.length < 10) this.options.push("");
    },
    // [@Method] options 삭제
    removeOption(index) {
      this.options.splice(index, 1);
    },
  },
};
</script>

<style></style>
