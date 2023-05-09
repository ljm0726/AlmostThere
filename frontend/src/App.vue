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
      // isSocketConnected: false,
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
  created() {
    // window.onload = function() {
    //   setTimeout(function() {
    //     window.scrollTo(0, 1);}, 100);
    // };
    // window.addEventListener("load", function() {
    //   setTimeout(scrollTo, 0, 0, 1);
    // }, false);
    // let vh = window.innerHeight * 0.01;
    // document.documentElement.style.setProperty("--vh", `${vh}px`);
    // this.setScreenSize();
    // window.addEventListener("resize", this.setScreenSize);
    // window.addEventListener("touched", this.setScreenSize);
  },

  computed: {
    ...mapState("meetingStore", ["recent_meeting"]),
    ...mapState("memberStore", ["member"]),
    ...mapState("websocketStore", ["connected", "stompClient"]),
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
    ...mapActions("websocketStore", ["updateStompClient", "updateConnected"]),
    ...mapActions("meetingStore", ["setMeeting"]),
    // setScreenSize() {
    //   //먼저 뷰포트 높이를 얻고 1%를 곱하여 vh 단위 값을 얻습니다.
    //   let vh = window.innerHeight * 0.01;
    //   //그런 다음 --vh 사용자 정의 속성의 값을 문서의 루트로 설정합니다.
    //   document.documentElement.style.setProperty("--vh", `${vh}px`);
    // },
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

      const diffTime =
        new Date(meetingTime.getTime() - 3 * 60 * 60 * 1000) -
        formattedTime.getTime();

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
    // Websocket 연결
    connect() {
      if (
        this.connected ||
        (this.stompClient && this.stompClient.ws.readyState == 1)
      ) {
        this.waitConnect();
      } else {
        this.updateConnected(true);
        const serverURL = `${process.env.VUE_APP_API_BASE_URL}/websocket`;
        let socket = new SockJS(serverURL);
        // this.stompClient = Stomp.over(socket);
        this.updateStompClient(Stomp.over(socket));

        console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
        this.stompClient.connect(
          {},
          (frame) => {
            // 소켓 연결 성공
            // this.isSocketConnected = true;
            // this.isConnect = true;
            console.log("소켓 연결 성공", frame);
            this.updateConnected(false);

            // GeoLocation - 1초마다 현 위치 얻기
            this.getGeoLocation();
            this.startIntervalMemberLocation();
          },
          (error) => {
            // 소켓 연결 실패
            console.log("소켓 연결 실패", error);
            this.updateConnected(false);
          }
        );
      }
    },
    // 소켓 연결 기다리기
    waitConnect() {
      setTimeout(() => {
        if (this.stompClient.ws.readyState == 1) {
          this.getGeoLocation();
          this.startIntervalMemberLocation();
        } else {
          this.waitConnect();
        }
      }, 1);
    },

    startIntervalMemberLocation() {
      setInterval(() => {
        this.getGeoLocation();
      }, 3000);
    },

    getGeoLocation() {
      console.log("#[getGeoLocation]# 현 위치 얻기 동작");
      // alert("## geo", navigator.geolocation);
      if (navigator.geolocation) {
        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        navigator.geolocation.getCurrentPosition((position) => {
          // 현 로그인한 사용자의 정보(id, nickname, latlng) 객체 생성
          const member = {
            memberId: this.member.id,
            memberNickname: this.member.memberNickname,
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
@import "@/assets/styles/override/snackbar.css";
@import "@/assets/styles/box/box_shadow.css";
@import "@/assets/styles/box/box_border.css";

html body {
  background: #fafafa;
  max-width: 500px;
  margin: 0 auto;
  height: 100%;
  min-height: 100%;
  /* height: var(--vh); */
  /* height: -webkit-fill-available; */
  /* height: fill-available; */
  /* height: 100vh;
  height: calc(var(--vh, 1vh) * 100); */
}
#app {
  /* height: 100vh;
  height: calc(var(--vh, 1vh) * 100);
  height: -webkit-fill-available; */
  max-width: 100%;
  min-height: 100%;
  /* height: 100%; */
  /* height: -webkit-fill-available; */

  font-family: var(--regular-font);
}
/* .v-application {
  display: block;
} */
/* .v-application--wrap { */
/* min-height: 100% !important; */
/* flex-direction: column !important; */
/* display: block !important; */
/* flex-wrap: wrap !important; */
/* } */
</style>
