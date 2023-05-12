<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column main-col-1" color="transparent">
    <div class="d-flex flex-row justify-space-between">
      <span class="point-font xxxxl-font">정산</span>
      <v-btn
        class="ml-2 justify-space-between"
        depressed
        small
        color="var(--main-col-1)"
        dark
        rounded
        @click="sendkakao"
      >
        <v-icon class="mr-2" color="white" small
          >mdi-share-variant-outline</v-icon
        >
        <span class="xxxs-font">카카오톡 공유하기</span>
      </v-btn>
    </div>
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
    <vue-bottom-sheet
      ref="costSheet"
      max-width="500px"
      max-height="90% !important"
      height="fit-content !important"
    >
      <v-sheet class="px-5 d-flex flex-column pb-10">
        <span class="point-font xxxxl-font main-col-1 align-self-center">
          영수증 등록
        </span>
        <v-file-input
          v-model="receipt"
          class="pt-5"
          accept="image/png, image/jpeg, image/jpg"
          placeholder="영수증 사진을 첨부해 주세요."
          prepend-icon="mdi-camera"
        ></v-file-input>
        <v-sheet
          v-if="imageLoading"
          class="mt-5 mb-5 d-flex flex-column justify-center align-center"
          height="300px"
        >
          <v-progress-circular
            :size="40"
            color="var(--main-col-1)"
            indeterminate
            class="mb-4"
          ></v-progress-circular>
          <span class="point-font main-col-1 lg-font">영수증 정보 읽는 중</span>
        </v-sheet>
        <div class="mt-5 mb-5" v-else style="height: 300px">
          <div class="d-flex justify-center align-center">
            <img :src="imageUrl" width="90%" />
          </div>
          <div v-if="ocrSuccess">
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
            <div class="d-flex flex-column">
              <v-btn
                v-if="this.ocrSuccess"
                class="mb-5"
                color="var(--main-col-1)"
                rounded
                dark
                @click="addCalculateDetail()"
                >등록</v-btn
              >
            </div>
          </div>
          <div
            v-else
            class="mt-5 mb-5 d-flex flex-column justify-center align-center"
          >
            <span class="point-font main-col-1 lg-font"
              >사진 정보 읽기에 실패하였습니다.</span
            >
            <span class="point-font main-col-1 lg-font"
              >다시 시도해주세요!</span
            >
          </div>
        </div>
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
    meetingName: String,
    calculateDetails: Array,
    lateTotal: Number,
    spentMoney: Number,
    total: Number,
    remain: Number,
  },
  data() {
    return {
      imageLoading: false,
      ocrSuccess: true,
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
    sendkakao() {
      // const filterMeetingDate = this.formatDate(this.meetingTime);
      // const filterMeetingTime = this.formatTime(this.meetingTime);
      // const roomCode = this.roomCode;

      window.Kakao.Share.sendDefault({
        objectType: "feed",
        content: {
          title: "모임 이름: " + this.meetingName,
          description:
            "모임의 정산 내역입니다.\n" + "정산 내역을 확인하여 정산해주세요! ",
          imageUrl: "https://k8a401.p.ssafy.io/img/home.5daad672.png",
          link: {
            // mobileWebUrl: "http://localhost:8080",
            // webUrl: "http://localhost:8080",
          },
        },
        itemContent: {
          items: [
            {
              item: "합계:",
              itemOp: String(this.total),
            },
            {
              item: "잔액:",
              itemOp: String(this.remain),
            },
            {
              item: "지각비:",
              itemOp: String(this.lateTotal),
            },
            {
              item: "내가 내야하는 금액:",
              itemOp: String(this.spentMoney),
            },
          ],
        },
        buttons: [
          {
            title: "정산내역 확인하러 가기",
            link: {
              // 룸코드 props? store로 받아와서 url에 추가하기
              mobileWebUrl: `https://k8a401.p.ssafy.io/meeting/${this.meetingId}`,
              webUrl: `https://k8a401.p.ssafy.io/meeting/${this.meetingId}`,
              // mobileWebUrl: `http://localhost:3000/entrance/${roomCode}`,
              // webUrl: `http://localhost:3000/entrance/${roomCode}`,
            },
          },
        ],
      });
    },
    formatDate(value) {
      const date = new Date(value);
      const year = date.getFullYear();
      const month = date.getMonth();
      const day = date.getDate();

      const result = `${year}년 ${
        month + 1 < 10 ? `0${month + 1}` : month + 1
      }월 ${day < 10 ? `0${day}` : day}일`;
      return result;
    },
    formatTime(value) {
      const date = new Date(value);
      const hour = date.getHours();
      const min = date.getMinutes();
      const result = `${
        hour >= 12
          ? `오후 ${hour == 12 ? `${hour}` : hour - 12}`
          : `오전 ${hour}`
      }시 ${min < 10 ? `0${min}` : min}분`;
      return result;
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
      this.ocrSuccess = await true;
      if (this.receipt != null) {
        postReceiptInfo(this.receipt).then(async (res) => {
          if (res != null) {
            this.storeName = await res.storeName;
            this.totalPrice = await res.totalPrice;
            this.imageLoading = await false;
            this.ocrSuccess = await true;
          } else {
            this.imageLoading = await false;
            this.ocrSuccess = await false;
          }
        });
      }
    },
  },
};
</script>

<style></style>
