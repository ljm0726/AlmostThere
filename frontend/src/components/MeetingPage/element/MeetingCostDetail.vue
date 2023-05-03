<template>
  <v-dialog v-model="dialog" scrollable max-width="300px">
    <v-card rounded="xl" class="pb-3">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <span class="point-font xxxxxxl-font main-col-1">정산 상세내역</span>
        <v-sheet
          class="pr-1 px-4 d-flex flex-row justify-space-between align-center"
          width="100%"
        >
          <div>
            <v-row class="mt-5 d-flex align-center">
              <span class="md-font medium-font main-col-1">상호명</span>
              <span class="md-font ml-2 light-font main-col-1">{{
                calculate.storeName
              }}</span>
            </v-row>
            <v-row class="mt-5 d-flex align-center">
              <span class="md-font medium-font main-col-1"
                >총&nbsp;&nbsp;액</span
              >
              <span class="md-font ml-4 light-font main-col-1">{{
                calculate.price
              }}</span>
            </v-row>
            <div class="mt-3 d-flex justify-center align-center">
              <img
                :src="`https://k8a401.p.ssafy.io/almostthere/${calculate.fileName}`"
                width="100%"
              />
            </div>
          </div>
        </v-sheet>
        <div class="mt-3 d-flex justify-center align-center">
          <v-btn color="var(--main-col-1)" rounded dark @click="closeDialog()"
            >닫기</v-btn
          >
          <v-btn
            class="ml-2"
            id="square-big-btn"
            color="var(--main-col-1)"
            rounded
          >
            <v-icon color="white">$vuetify.icons.delete_outline</v-icon>
          </v-btn>
        </div>
      </v-card-title>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "MeetingCostDetail",
  components: { CloseButton },
  data() {
    return {
      dialog: false,
      link: "http://localhost:3000/invite",
      calculate: {
        calculateDetailId: null,
        fileName: "",
        meetingId: null,
        type: null,
        storeName: "",
        price: "",
      },
    };
  },
  props: {
    meetingName: String,
  },
  methods: {
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },
    changeCalculate(calculate) {
      this.calculate = calculate;
    },
  },
};
</script>

<style></style>
