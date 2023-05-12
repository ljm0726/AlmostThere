<template>
  <v-dialog v-model="dialog" scrollable max-width="300px" rounded="xl">
    <v-card rounded="xl">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="dialog = false"></close-button>
        </div>
        <!-- <img src="@/assets/images/dialog/logout.png" width="60%" /> -->
        <div
          class="point-font xxxxxxl-font main-col-1 d-flex flex-column align-center"
          v-html="title"
        ></div>
        <span
          class="extralight-font xs-font d-flex flex-column align-center seminarrow-font"
          v-html="message"
        >
        </span>
      </v-card-title>
      <v-card-text>
        <v-btn
          elevation="0"
          color="var(--main-col-1)"
          dark
          rounded
          block
          @click="dialog = false"
          >닫기</v-btn
        >
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "NoImageDefault",
  components: { CloseButton },
  props: {
    title: String,
    message: String,
  },
  data() {
    return {
      dialog: false,
    };
  },
  methods: {
    openDialog() {
      this.dialog = true;
    },
  },
};
</script>

<style></style>
