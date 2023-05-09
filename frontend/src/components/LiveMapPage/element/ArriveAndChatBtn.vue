<template>
  <v-sheet color="transparent">
    <v-sheet
      max-width="500"
      color="transparent"
      width="100%"
      class="d-flex flex-column justify-end align-end"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        bottom: 0;
        padding: 0 15px 15px 0;
        z-index: 2;
      "
    >
      <v-btn
        class="ml-1 mt-1"
        @click="goChatting()"
        color="var(--main-col-1)"
        dark
        rounded
      >
        채팅
      </v-btn>
      <v-btn
        class="ml-1 mt-1"
        @click="checkLocation()"
        color="var(--main-col-1)"
        dark
        rounded
      >
        도착 완료
      </v-btn>
      <v-btn
        class="mt-1"
        @click="resizeMapLevel()"
        color="var(--main-col-1)"
        dark
        rounded
      >
        범위 재조정
      </v-btn>
    </v-sheet>
    <v-dialog v-model="success" max-width="300px" rounded="xl">
      <v-card rounded="xl">
        <v-card-title class="d-flex flex-column">
          <div class="align-self-end">
            <close-button @closeDialog="closeSuccess"></close-button>
          </div>
          <img src="@/assets/images/dialog/o.png" width="40%" />
          <span class="point-font xxxxxxl-font main-col-1">도착 완료</span>
          <span class="extralight-font sm-font">목적지에 도착했습니다.</span>
        </v-card-title>
        <v-card-text>
          <v-btn
            elevation="0"
            color="var(--main-col-1)"
            dark
            rounded
            @click="closeSuccess"
            block
          >
            닫기
          </v-btn>
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog v-model="failure" max-width="300px" rounded="xl">
      <v-card rounded="xl">
        <v-card-title class="d-flex flex-column">
          <div class="align-self-end">
            <close-button @closeDialog="closeFailure"></close-button>
          </div>
          <img src="@/assets/images/dialog/x.png" width="40%" />
          <span class="point-font xxxxxxl-font main-col-1">도착 불가</span>
          <span class="mt-1 extralight-font xxs-font seminarrow-font">
            위치가 목적지 100m 이내인 경우만
          </span>
          <span class="extralight-font xxs-font seminarrow-font">
            도착 완료가 가능합니다.
          </span>
        </v-card-title>
        <v-card-text>
          <v-btn
            elevation="0"
            color="var(--main-col-1)"
            dark
            rounded
            @click="closeFailure"
            block
          >
            닫기
          </v-btn>
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog v-model="timeover" max-width="300px" rounded="xl">
      <v-card rounded="xl">
        <v-card-title class="d-flex flex-column">
          <div class="align-self-end">
            <close-button @closeDialog="closeTimeover"></close-button>
          </div>
          <img src="@/assets/images/dialog/x.png" width="40%" />
          <span class="point-font xxxxxxl-font main-col-1">시간 초과</span>
          <span class="mt-1 extralight-font xxs-font seminarrow-font">
            모임 시간을 초과해
          </span>
          <span class="extralight-font xxs-font seminarrow-font">
            도착 완료가 불가능합니다.
          </span>
        </v-card-title>
        <v-card-text>
          <v-btn
            elevation="0"
            color="var(--main-col-1)"
            dark
            rounded
            @click="closeTimeover"
            block
          >
            닫기
          </v-btn>
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog v-model="error" max-width="300px" rounded="xl">
      <v-card rounded="xl">
        <v-card-title class="d-flex flex-column">
          <div class="align-self-end">
            <close-button @closeDialog="closeError"></close-button>
          </div>
          <img src="@/assets/images/dialog/x.png" width="40%" />
          <span class="logo-font xxxxxxl-font main-col-1">Server Error</span>
          <span class="mt-1 extralight-font xxs-font seminarrow-font">
            인터넷 연결이 되지 않았거나
          </span>
          <span class="extralight-font xxs-font seminarrow-font">
            도착 완료가 불가능한 상태입니다.
          </span>
        </v-card-title>
        <v-card-text>
          <v-btn
            elevation="0"
            color="var(--main-col-1)"
            dark
            rounded
            @click="closeError"
            block
          >
            닫기
          </v-btn>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-sheet>
</template>

<script>
import { arriveDestination } from "@/api/modules/livemap";
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "ArriveBtn",
  components: {
    CloseButton,
  },
  props: {
    id: String,
  },
  data() {
    return {
      success: false,
      failure: false,
      timeover: false,
      error: false,
    };
  },
  methods: {
    closeSuccess() {
      this.success = false;
    },
    closeFailure() {
      this.failure = false;
    },
    closeTimeover() {
      this.timeover = false;
    },
    closeError() {
      this.error = false;
    },
    // [@Method] 지도 범위 조정
    resizeMapLevel() {
      this.$emit("resizeMapLevel");
    },
    checkLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          const x = position.coords.latitude;
          const y = position.coords.longitude;
          arriveDestination(this.$route.params.id, x, y).then((res) => {
            if (res == 1) {
              this.success = true;
            } else if (res == 2) {
              this.failure = true;
            } else if (res == 3) {
              this.timeover = true;
            } else if (res == 4) {
              this.error = true;
            }
          });
        });
      }
    },
    goChatting() {
      this.$router.push(`/chat/${this.$route.params.id}`);
    },
  },
};
</script>

<style></style>
