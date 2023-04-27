<template>
  <v-dialog v-model="dialog" scrollable max-width="300px" rounded="xl">
    <v-card rounded="xl">
      <v-card-title class="d-flex flex-column" style="margin-bottom: 2%">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <img
          src="@/assets/images/dialog/earth.png"
          width="60%"
          style="margin-bottom: 7%"
        />
        <span class="regular-font md-font">출발지를 입력해</span>
        <span class="regular-font md-font">중간 위치를 추천 받아보세요!</span>
      </v-card-title>

      <input
        v-for="i in 3"
        :key="i"
        class="search-box"
        :placeholder="i + '. 출발지를 입력하세요!'"
        v-on:click="goToPage('/search')"
      />

      <v-card-text>
        <v-row>
          <v-col class="">
            <v-btn elevation="0" color="var(--main-col-1)" dark rounded block
              >중간 위치 찾기</v-btn
            >
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";
export default {
  name: "LogoutDialog",
  components: { CloseButton },
  data() {
    return {
      dialog: false,
    };
  },
  methods: {
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },
  },
};
</script>

<style scoped>
.search-box {
  box-sizing: border-box;
  z-index: 2;
  display: flex;
  /* margin-left: 10%; */
  width: 85%;
  height: 33px;
  align-self: center;
  /* bottom: unset; 추가 */

  background: #ffffff;
  border: 1px solid #092a49;
  border-radius: 10px;
  padding-left: 20px;

  margin-bottom: 13px;
}

input::placeholder {
  font-style: var(--regular-font);
  font-size: var(--sm-font);
}

.v-btn {
  margin-top: 5%;
}
span {
  line-height: 18px;
}
</style>
