<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column main-col-1" color="transparent">
    <span class="point-font xxxxl-font">정산</span>
    <v-simple-table dense>
      <template v-slot:default>
        <tbody>
          <tr
            v-for="(calculate, index) in calculates"
            :key="index"
            class="d-flex flex-row justify-space-between align-center"
            @click="showDetailModel(index)"
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
    <v-divider class="mb-1"></v-divider>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 pb-1 medium-font sm-font">합계</span>
      <span class="px-1 pb-1 medium-font sm-font"
        >{{ total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 light-font sm-font">지각비</span>
      <span class="px-1 light-font sm-font"
        >{{
          lateTotal.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
        }}원</span
      >
    </div>
    <v-divider class="my-1"></v-divider>
    <div class="d-flex flex-row justify-space-between">
      <span class="px-1 bold-font sm-font">내가 내야 하는 금액</span>
      <span class="px-1 bold-font sm-font"
        >{{ myTotal.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") }}원</span
      >
    </div>
    <v-btn class="my-3" color="var(--main-col-1)" dark rounded @click="open()">
      영수증 및 카드내역 추가
    </v-btn>
    <vue-bottom-sheet ref="costSheet" max-width="500px">
      <v-sheet class="px-5 d-flex flex-column pb-10">
        <span class="point-font xxxxl-font main-col-1 align-self-center">
          영수증 및 카드 내역 등록
        </span>
        <v-file-input
          v-model="receipt"
          class="py-5"
          accept="image/png, image/jpeg, image/bmp"
          placeholder="영수증 사진을 첨부해 주세요."
          prepend-icon="mdi-camera"
          dense
        ></v-file-input>
        {{ imageUrl }}
        {{ receipt }}
        <img :src="imageUrl" />
        <v-row class="d-flex align-center">
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
              type="number"
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
              type="number"
              placeholder="총액"
              outlined
              hide-details
              dense
            ></v-text-field>
          </v-col>
        </v-row>
        <v-btn color="var(--main-col-1)" rounded dark>등록</v-btn>
      </v-sheet>
    </vue-bottom-sheet>
    <meeting-cost-detail></meeting-cost-detail>
  </v-sheet>
</template>

<script>
import MeetingCostDetail from "./MeetingCostDetail.vue";

export default {
  name: "MeetingCost",
  methods: {
    open() {
      this.$refs.costSheet.open();
    },
    showDetailModel(index) {
      index;
    },
  },
  components: {
    MeetingCostDetail,
  },
  computed: {
    imageUrl() {
      if (this.receipt != null) {
        return URL.createObjectURL(this.receipt[0]);
      } else {
        return null;
      }
    },
  },
  data() {
    return {
      calculates: [
        {
          storeName: "스타벅스",
          price: 1590000,
        },
        {
          storeName: "빽다방",
          price: 237000,
        },
        {
          storeName: "투썸플레이스",
          price: 26200,
        },
      ],
      total: 10000,
      lateTotal: 100000,
      myTotal: 10000,
      receipt: null,
    };
  },
};
</script>

<style></style>
