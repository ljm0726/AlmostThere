<template>
  <v-app>
    <!-- Header -->
    <router-view name="header" />
    <!-- Main -->
    <v-main>
      <router-view />
    </v-main>
    <!-- Footer -->
    <router-view name="footer" />
    <!-- Navigation -->
    <router-view name="navigation" />
  </v-app>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { getMostRecentMeeting } from "@/api/modules/meeting.js";
import { mapActions, mapState } from "vuex";

export default {
  name: "App",

  data() {
    return {
      timeOut: null,
      isSocketConnected: false,
    };
  },

  beforeCreate() {
    getMostRecentMeeting().then((res) => {
      console.log("getMostRecentMeeting response", res);
      if (res != null) {
        this.setMeeting(res);
      }
    });
  },

  computed: {
    ...mapState("meetingStore", ["recent_meeting"]),
  },

  watch: {
    recent_meeting(newVal) {
      if (newVal != null) {
        clearTimeout(this.timeOut);
        const now = new Date();
        const meetingTime = new Date(newVal.meetingTime);

        if (now < meetingTime) {
          this.timeOut = setTimeout(
            this.connectHandler,
            this.calculateRemainTimeForTimeOut(newVal.meetingTime)
          );
        }
      }
    },
  },

  methods: {
    ...mapActions("meetingStore", ["setMeeting"]),

    calculateRemainTimeForTimeOut(newDate) {
      const now = new Date();
      const meetingTime = new Date(newDate);

      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, "0");
      const day = String(now.getDate()).padStart(2, "0");
      const hours = String(now.getHours()).padStart(2, "0");
      const minutes = String(now.getMinutes()).padStart(2, "0");
      const seconds = String(now.getSeconds()).padStart(2, "0");

      const formattedTime = new Date(
        `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
      );
      const diffTime = meetingTime.getTime() - formattedTime.getTime();

      console.log("diffTime :", diffTime);

      return diffTime;
    },

    connectHandler() {
      const access_token = localStorage.getItem("Authorization");

      if (access_token) {
        console.log("connect");
        this.connect();
      }
    },
    connect() {
      if (!this.isSocketConnected) {
        const serverURL = `${process.env.VUE_APP_API_BASE_URL}/websocket`;
        let socket = new SockJS(serverURL);
        this.stompClient = Stomp.over(socket);

        console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
        this.stompClient.connect(
          {},
          (frame) => {
            // 소켓 연결 성공
            this.isSocketConnected = true;
            this.isConnect = true;
            console.log("소켓 연결 성공", frame);

            // GeoLocation - 1초마다 현 위치 얻기
            // this.getGeoLocation();
            this.startIntervalMemberLocation();
          },
          (error) => {
            // 소켓 연결 실패
            console.log("소켓 연결 실패", error);
          }
        );
      }
    },

    startIntervalMemberLocation() {
      setInterval(() => {
        this.getGeoLocation();
      }, 3000);
    },

    getGeoLocation() {
      console.log("#21# getGeoLocation 현 위치 얻기 동작");
      // alert("## geo", navigator.geolocation);
      if (navigator.geolocation) {
        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        navigator.geolocation.getCurrentPosition((position) => {
          // 현 로그인한 사용자의 정보(id, nickname, latlng) 객체 생성
          const member = {
            memberId: 1,
            memberNickname: "이싸피",
            memberLatLng: [position.coords.latitude, position.coords.longitude],
          };

          // 현 사용자의 위치 저장
          console.log("getGeoLocation :", member);
          this.send(member);
        });
      } else {
        console.log("# geolocation을 사용할수 없어요..");
      }
    },
    send(member) {
      console.log("# send message: ", member);

      if (this.stompClient && this.stompClient.connected) {
        const msg = member;
        this.stompClient.send("/message/locShare", JSON.stringify(msg), {});
        // console.log("#21# message 전송: ", msg);
      }
    },
  },
};
</script>

<style>
@import "@/assets/styles/text/text_font.css";
@import "@/assets/styles/text/text_size.css";
@import "@/assets/styles/text/text_color.css";
@import "@/assets/styles/text/text_height.css";
@import "@/assets/styles/variable.css";
@import "@/assets/styles/font.css";
@import "@/assets/styles/override/button.css";
@import "@/assets/styles/override/icon.css";
@import "@/assets/styles/override/dialog.css";
@import "@/assets/styles/override/badge.css";
@import "@/assets/styles/override/swiper.css";
@import "@/assets/styles/box/box_shadow.css";
@import "@/assets/styles/box/box_border.css";

html body {
  background: #fafafa;
  max-width: 500px;
  margin: 0 auto;
  height: 100%;
  min-height: 100%;
}
#app {
  max-width: 100%;
  min-height: 100%;

  font-family: var(--regular-font);
}
</style>
