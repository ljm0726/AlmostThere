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
        <v-icon>$vuetify.icons.user_invite_outline</v-icon>
      </v-btn>
    </template>
    <v-card rounded="xl" class="pb-3">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <span class="point-font xl-font main-col-1">{{ meetingName }}</span>
        <span class="point-font xxxxxxl-font main-col-1">초대 링크</span>
        <img src="@/assets/images/dialog/invite_link.png" width="60%" />
        <v-sheet
          class="pr-1 px-4 detail-border d-flex flex-row justify-space-between align-center"
          rounded="lg"
          width="100%"
        >
          <span class="xs-font light-font">{{
            link.length > 22 ? link.substr(0, 22) + "···" : link
          }}</span>
          <div>
            <v-btn icon><v-icon>$vuetify.icons.copy_outline</v-icon></v-btn>
            <v-btn icon @click="sendkakao"
              ><v-icon>$vuetify.icons.share_outline</v-icon></v-btn
            >
          </div>
        </v-sheet>
        <span class="extralight-font xs-font main-col-1">
          초대 링크를 공유해 멤버를 추가해 보세요.
        </span>
      </v-card-title>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "MeetingInviteBtn",
  components: { CloseButton },
  data() {
    return {
      dialog: false,
      link: "http://localhost:3000/invite",
      image: require("@/assets/images/banner/home.png"),
    };
  },
  props: {
    meetingName: String,
    meetingDate: String,
    meetingTime: String,
    meetingPlace: String,
  },
  methods: {
    sendkakao: function () {
      console.log("모임 이름: ", this.meetingName);
      console.log("모임 날짜: ", this.meetingDate);
      console.log("모임 시간: ", this.meetingTime);
      console.log("모임 장소: ", this.meetingPlace);

      const filterMeetingDate = this.formatDate(this.meetingTime);
      const filterMeetingTime = this.formatTime(this.meetingTime);

      window.Kakao.Share.sendDefault({
        objectType: "feed",
        content: {
          title: "모임 이름: " + this.meetingName,
          description:
            "호스트가 모임에 초대합니다.\n" +
            "아래 버튼을 통해 초대를 수락하세요! ",
          imageUrl: "https://k8a401.p.ssafy.io/img/home.5daad672.png",
          link: {
            // mobileWebUrl: "http://localhost:8080",
            // webUrl: "http://localhost:8080",
          },
        },
        itemContent: {
          items: [
            {
              item: "모임 장소:",
              itemOp: this.meetingPlace,
            },
            {
              item: "모임 날짜:",
              itemOp: filterMeetingDate,
            },
            {
              item: "모임 시간:",
              itemOp: filterMeetingTime,
            },
          ],
        },
        buttons: [
          {
            title: "초대 모임 참여하기",
            link: {
              mobileWebUrl: "http://localhost:8080",
              webUrl: "http://localhost:8080",
            },
          },
        ],
      });
    },
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
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
      const result = `
      ${
        hour < 13
          ? ` 오전 ${hour - 12 < 10 ? `0${hour - 12}` : hour - 12}`
          : ` 오후 ${hour - 12 < 10 ? `0${hour - 12}` : hour - 12}`
      }시 ${min < 10 ? `0${min}` : min}분`;
      return result;
    },
  },
};
</script>

<style></style>
