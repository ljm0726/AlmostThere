<template>
  <div>
    <v-sheet
      max-width="500"
      color="transparent"
      width="100%"
      class="d-flex flex-row justify-end align-center"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        bottom: 0;
        padding: 0 10px 65px 0;
      "
    >
      <v-btn
        @click="checkLocation()"
        dark
        color="var(--main-col-1)"
        style="background-color: white"
        outlined
        small
      >
        도착완료
      </v-btn>
    </v-sheet>
    <v-dialog v-model="success" scrollable max-width="300px" rounded="xl">
      <v-card rounded="xl">
        <v-card-title class="d-flex flex-column">
          <div class="align-self-end">
            <close-button @closeDialog="closeDialog"></close-button>
          </div>
          <img src="@/assets/images/dialog/logout.png" width="60%" />
          <span class="point-font xxxxxxl-font main-col-1">로그아웃</span>
          <span class="extralight-font sm-font">로그아웃하시겠습니까?</span>
        </v-card-title>
        <v-card-text>
          <v-row>
            <v-col class="pr-1">
              <v-btn elevation="0" color="var(--main-col-1)" dark rounded block
                >로그아웃</v-btn
              >
            </v-col>
            <v-col class="pl-1">
              <v-btn
                elevation="0"
                color="var(--main-col-1)"
                outlined
                dark
                rounded
                @click="closeDialog"
                block
                >취소</v-btn
              >
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { arriveDestination } from "@/api/modules/livemap";
export default {
  name: "ArriveBtn",
  props: {
    id: String,
  },
  methods: {
    checkLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          const x = position.coords.latitude;
          const y = position.coords.longitude;
          arriveDestination(this.id, x, y);
        });
      }
    },
  },
};
</script>

<style></style>
