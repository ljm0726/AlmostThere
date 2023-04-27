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
          style="margin-bottom: 7%; height: 82px; width: 102px"
          alt=""
        />
        <span class="regular-font md-font">출발지를 입력해</span>
        <span class="regular-font md-font">중간 위치를 추천 받아보세요!</span>
      </v-card-title>

      <div
        v-for="(start, index) in starts"
        :key="index"
        class="input-container"
      >
        <input
          class="search-box"
          :value="`${index + 1}. 출발지를 입력하세요!`"
          @click="goToPage('/search')"
        />
        <div class="img-container">
          <img
            v-if="index > 1"
            src="@/assets/images/dialog/close_btn_small.png"
            class="close-btn-small"
            alt=""
            @click="cancelStart(index)"
          />
        </div>
      </div>

      <div style="align-self: center; margin: 2% 0" @click="plusStart">
        <img
          src="@/assets/images/dialog/Plus.png"
          style="margin-bottom: 10%; float: left"
          alt=""
        />
        출발지 추가하기
      </div>

      <v-card-text>
        <v-row>
          <v-col class="search_halfway">
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
      starts: [null, null],
    };
  },
  methods: {
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },

    plusStart() {
      console.log("@@@");
      this.starts.push(null);
    },

    cancelStart(index) {
      this.starts.splice(index, 1);
    },
  },
};
</script>

<style scoped>
.search-box {
  box-sizing: border-box;
  width: 85%;
  height: 33px;
  background: #ffffff;
  border: 1px solid #092a49;
  border-radius: 10px;
  padding-left: 20px;
  padding-right: 25px;

  margin-bottom: 13px;
}
.plusPlace {
  /* align-self: center; */
  margin: 2% 0;
}
.search_halfway {
  margin-bottom: 5px;
}

/* input::placeholder {
  font-style: var(--regular-font);
  font-size: var(--sm-font);
} */
span {
  line-height: 18px;
}

.input-container {
  text-align: center;
  width: 100%;
}
.input-container {
  position: relative;
}

.img-container {
  position: absolute;
  top: 48%;
  right: 8%;
  transform: translateY(-50%);
  width: 30px;
  height: 30px;
}
</style>
