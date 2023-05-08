<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column main-col-1" color="transparent">
    <span class="point-font xxxxl-font">정산</span>
    <v-simple-table dense>
      <template v-slot:default>
        <tbody>
          <tr
            v-for="(calculate, index) in calculateDetails"
            :key="index"
            class="d-flex flex-row justify-space-between align-center"
            @click="showDetailModel(calculate)"
            style="cursor: pointer"
          >
            <td
              class="px-1 d-flex align-center light-font main-col-1"
              style="border-bottom: none"
            >
              {{ calculate.storeName }}
            </td>
            <td
              class="px-1 d-flex align-center light-font main-col-1"
              style="border-bottom: none"
            >
              {{
                calculate.price
                  .toString()
                  .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
              }}원
            </td>
          </tr>
        </tbody>
      </template>
    </v-simple-table>
    <meeting-cost-detail ref="detail"></meeting-cost-detail>
    <v-divider class="mb-1"></v-divider>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 pb-1 medium-font sm-font">합계</span>
      <span class="px-1 pb-1 medium-font sm-font"
        >{{ String(total).replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 pb-1 medium-font sm-font">잔액</span>
      <span class="px-1 pb-1 medium-font sm-font"
        >{{ String(remain).replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 light-font sm-font">지각비</span>
      <span class="px-1 light-font sm-font"
        >{{ String(lateTotal).replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <v-divider class="my-1"></v-divider>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 bold-font sm-font">내가 내야 하는 금액</span>
      <span class="px-1 bold-font sm-font"
        >{{ String(spentMoney).replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <v-btn class="my-3" color="var(--main-col-1)" dark rounded @click="open()">
      영수증 추가
    </v-btn>
    <vue-bottom-sheet ref="costSheet" max-width="500px">
      <v-sheet class="px-5 d-flex flex-column pb-10">
        <span class="point-font xxxxl-font main-col-1 align-self-center">
          영수증 등록
        </span>
        <v-file-input
          v-model="receipt"
          class="pt-5 mb-2"
          accept="image/png, image/jpeg, image/jpg"
          placeholder="영수증 사진을 첨부해 주세요."
          prepend-icon="mdi-camera"
        ></v-file-input>
        <v-sheet
          v-if="imageLoading"
          class="mt-5 mb-5 d-flex flex-column justify-center align-center"
        >
          <v-progress-circular
            :size="40"
            color="var(--main-col-1)"
            indeterminate
            class="mb-4"
          ></v-progress-circular>
          <span class="point-font main-col-1 lg-font">영수증 정보 읽는 중</span>
        </v-sheet>
        <div v-else>
          <div class="d-flex justify-center align-center">
            <img :src="imageUrl" width="90%" />
          </div>
          <v-row class="mt-5 d-flex align-center">
            <v-col
              cols="2"
              class="d-flex flex-row justify-space-between medium-font main-col-1"
            >
              <span>상</span>
              <span>호</span>
              <span>명</span>
            </v-col>
            <v-col>
              <v-text-field
                v-model="storeName"
                type="text"
                placeholder="상호명"
                outlined
                hide-details
                dense
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row class="mb-5 d-flex align-center">
            <v-col
              cols="2"
              class="d-flex flex-row justify-space-between medium-font main-col-1"
            >
              <span>총</span>
              <span>액</span>
            </v-col>
            <v-col>
              <v-text-field
                v-model="totalPrice"
                type="number"
                placeholder="총액"
                outlined
                hide-details
                dense
              ></v-text-field>
            </v-col>
          </v-row>
        </div>
        <v-btn
          color="var(--main-col-1)"
          rounded
          dark
          @click="addCalculateDetail()"
          >등록</v-btn
        >
      </v-sheet>
    </vue-bottom-sheet>
  </v-sheet>
</template>

<script>
import MeetingCostDetail from "./MeetingCostDetail.vue";
import { postReceiptInfo, saveCalculateDetail } from "@/api/modules/meeting.js";

export default {
  name: "MeetingCost",
  props: {
    meetingId: Number,
    calculateDetails: Array,
    lateTotal: Number,
    spentMoney: Number,
    total: Number,
    remain: Number,
  },
  data() {
    return {
      imageLoading: false,
      receipt: null,
      storeName: null,
      totalPrice: 0,
    };
  },
  methods: {
    open() {
      this.$refs.costSheet.open();
    },
    showDetailModel(calculate) {
      this.$refs.detail.changeCalculate(calculate);
      this.$refs.detail.openDialog();
    },
    addCalculateDetail() {
      saveCalculateDetail(
        this.meetingId,
        this.receipt,
        this.storeName,
        this.totalPrice
      ).then((res) => {
        if (res) this.$router.go(this.$router.currentRoute);
      });
    },
  },
  components: {
    MeetingCostDetail,
  },
  computed: {
    imageUrl() {
      if (this.receipt != null) {
        return URL.createObjectURL(this.receipt);
      } else {
        return null;
      }
    },
  },
  watch: {
    async receipt() {
      this.imageLoading = true;
      if (this.receipt != null) {
        postReceiptInfo(this.receipt).then(async (res) => {
          if (res != null) {
            this.storeName = await res.storeName;
            this.totalPrice = await res.totalPrice;
            this.imageLoading = await false;
          }
        });
      }
    },
  },
};
</script>

<style></style>
