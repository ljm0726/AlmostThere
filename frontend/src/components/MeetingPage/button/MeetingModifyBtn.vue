<template>
  <div>
    <v-btn
      class="mr-1"
      id="square-big-btn"
      outlined
      color="var(--main-col-1)"
      rounded
      @click="open()"
    >
      <v-icon>$vuetify.icons.edit_outline</v-icon>
    </v-btn>
    <vue-bottom-sheet ref="modifySheet" max-width="500px">
      <v-sheet class="d-flex flex-column pb-10">
        <span class="point-font xxxxl-font main-col-1 align-self-center"
          >모임 내용 수정하기</span
        >
        <v-sheet class="mx-5 my-2">
          <span class="point-font xxxl-font main-col-1">제목</span>
          <v-text-field
            v-model="name"
            outlined
            dense
            hide-details
          ></v-text-field>
        </v-sheet>
        <v-sheet class="mx-5 my-2">
          <span class="point-font xxxl-font main-col-1">일시</span>
          <div class="d-flex flex-row">
            <v-dialog
              ref="dateDialog"
              v-model="dateDialog"
              :return-value.sync="date"
              width="290px"
              style="z-index: 1000000"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  class="mr-1"
                  v-model="date"
                  readonly
                  v-bind="attrs"
                  dense
                  outlined
                  v-on="on"
                  hide-details
                ></v-text-field>
              </template>
              <v-date-picker
                locale="ko"
                v-model="date"
                scrollable
                color="var(--main-col-1)"
                :day-format="(date) => new Date(date).getDate()"
              >
                <v-spacer></v-spacer>
                <v-btn
                  text
                  color="var(--main-col-1)"
                  @click="dateDialog = false"
                >
                  닫기
                </v-btn>
                <v-btn
                  text
                  color="var(--main-col-1)"
                  @click="$refs.dateDialog.save(date)"
                >
                  확인
                </v-btn>
              </v-date-picker>
            </v-dialog>
            <v-dialog
              ref="timeDialog"
              v-model="timeDialog"
              :return-value.sync="time"
              width="290px"
              style="z-index: 1000000"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  class="ml-1"
                  v-model="time"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                  dense
                  outlined
                  hide-details
                ></v-text-field>
              </template>
              <v-time-picker
                v-model="time"
                full-width
                color="var(--main-col-1)"
              >
                <v-spacer></v-spacer>
                <v-btn
                  text
                  color="var(--main-col-1)"
                  @click="timeDialog = false"
                >
                  닫기
                </v-btn>
                <v-btn
                  text
                  color="var(--main-col-1)"
                  @click="$refs.timeDialog.save(time)"
                >
                  확인
                </v-btn>
              </v-time-picker>
            </v-dialog>
          </div>
        </v-sheet>
        <v-sheet class="mx-5 my-2 d-flex flex-column">
          <span class="point-font xxxl-font main-col-1">장소</span>
          <v-btn outlined block color="var(--main-col-1)">
            <span>{{ meetingPlace }}</span>
            <span>({{ meetingAddress }})</span>
          </v-btn>
        </v-sheet>
        <v-sheet class="mx-5 my-2">
          <span class="point-font xxxl-font main-col-1">지각비</span>
          <v-sheet class="d-flex flex-row">
            <v-text-field
              v-model="amount"
              hide-details
              outlined
              dense
              type="number"
            ></v-text-field>
            <v-sheet
              class="ml-2 px-2 detail-border d-flex flex-row align-center justify-center"
              rounded="lg"
              >원</v-sheet
            >
          </v-sheet>
        </v-sheet>
        <v-btn
          class="mx-5 my-2"
          elevation="0"
          color="var(--main-col-1)"
          dark
          rounded
          @click="modifyMetting()"
          >수정 완료</v-btn
        >
      </v-sheet>
    </vue-bottom-sheet>
  </div>
</template>

<script>
import { mapActions } from "vuex";

export default {
  name: "MeetingModifyBtn",
  methods: {
    ...mapActions("meetingStore", ["modify"]),
    open() {
      this.$refs.modifySheet.open();
    },
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },
    modifyMetting() {
      const date_time = new Date(this.date + " " + this.time);
      console.log(this.date, " ", this.time, " ", date_time);
    },
  },
  props: {
    meetingName: String,
    meetingDate: String,
    meetingTime: String,
    meetingPlace: String,
    meetingAddress: String,
    lateAmount: Number,
  },
  data() {
    return {
      name: "",
      date: null,
      time: null,
      place: "",
      address: "",
      amount: 0,
      dateDialog: false,
      timeDialog: false,
    };
  },

  created() {
    console.log(this.meetingName, this.meetingTime);
    const [date, time2] = this.meetingTime.split("T");
    const time = time2.slice(0, -3);
    this.name = this.meetingName;
    this.date = date;
    this.time = time;
    this.place = this.meetingPlace;
    this.address = this.meetingAddress;
    this.amount = this.lateAmount == null ? 0 : this.lateAmount;
  },
};
</script>

<style></style>
