<template>
  <v-dialog v-model="dialog" scrollable max-width="300px" rounded="xl">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        class="mr-1"
        id="square-big-btn"
        outlined
        color="var(--main-col-1)"
        rounded
        v-bind="attrs"
        v-on="on"
      >
        <v-icon>$vuetify.icons.out_outline</v-icon>
      </v-btn>
    </template>
    <v-card rounded="xl">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <img src="@/assets/images/dialog/dont_enter.png" width="60%" />
        <span class="point-font xxxxxxl-font main-col-1"> 위치 공유 불가</span>
        <span class="extralight-font sm-font"
          >멤버들과 실시간 위치 공유는
          <p></p>
          모임 시간 전후 3시간 동안만 지원됩니다.</span
        >
      </v-card-title>
      <v-card-text>
        <v-row>
          <v-col class="pr-1">
            <v-btn elevation="0" color="var(--main-col-1)" dark rounded block
              >나가기</v-btn
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
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "UnableEnterLiveMapBtn",
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

<style></style>
