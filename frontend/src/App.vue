<template>
  <v-app>
    <!-- Header -->
    <router-view name="header" />
    <!-- Main -->
    <v-main>
      <router-view />
      <location-permission-error ref="denied" />
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
import {
  getMostRecentMeeting,
  getRecentPastMeeting,
} from "@/api/modules/meeting.js";
import { mapActions, mapState } from "vuex";
import LocationPermissionError from "@/common/component/dialog/LocationPermissionError.vue";

import store from "@/store";

export default {
  name: "App",
  components: {
    LocationPermissionError,
  },
  data() {
    return {
      timeOut: null,
      intervalGeolocation: null,
    };
  },
  beforeCreate() {
    // path가 login 또는 landing일 경우 요청하지 않는다.
    const path = this.$route.fullPath;
    if (path != "/login" && path != "/landing") {
      getMostRecentMeeting().then((res) => {
        if (res != null) this.setMeeting(res);
      });
    }
  },
  computed: {
    ...mapState("meetingStore", ["recent_meeting"]),
    ...mapState("memberStore", ["member", "member_id"]),
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

      var diffTime =
        new Date(meetingTime.getTime() - 3 * 60 * 60 * 1000) -
        formattedTime.getTime();

      // diffTime이 양수인 경우 > 최근 지난 모임 중 3시간 이내인 모임이 있는 지 확인
      if (diffTime > 0) {
        getRecentPastMeeting().then((res) => {
          if (res != null) {
            const meetingTime = new Date(res.meetingTime);
            const threeHoursAfterTime = new Date(
              meetingTime.getTime() + 3 * 60 * 60 * 1000
            );
            if (new Date().getTime() <= threeHoursAfterTime.getTime()) {
              diffTime = -1;
              return diffTime;
            }
          }
        });
      } else {
        return diffTime;
      }
    },
    connectHandler() {
      const access_token = localStorage.getItem("Authorization");
      if (access_token) {
        // console.log("connect");
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
            this.connect();
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
      // setInterval(() => {
      //   this.getGeoLocation();
      // }, 3000);
      this.intervalGeolocation = setInterval(() => {
        this.getGeoLocation();
      }, 3000);
    },
    // [@Method] GeoLocation 동작
    // async getGeoLocation() {
    //   // console.log("#[getGeoLocation]# 현 위치 얻기 동작");
    //   if (this.member == null) {
    //     await store.dispatch("memberStore/isLogin");
    //   }
    //   // alert("## geo", navigator.geolocation);
    //   if (navigator.geolocation) {
    //     // GeoLocation을 이용해서 접속 위치를 얻어옵니다
    //     navigator.geolocation.getCurrentPosition(
    //       (position) => {
    //         // 현 로그인한 사용자의 정보(id, nickname, latlng) 객체 생성
    //         const member = {
    //           memberId: this.member_id,
    //           memberNickname: this.member.memberNickname,
    //           memberLatLng: [
    //             position.coords.latitude,
    //             position.coords.longitude,
    //           ],
    //         };

    //         // 현 사용자의 위치 저장
    //         // console.log("getGeoLocation :", member);
    //         this.send(member);
    //       },
    //       (error) => {
    //         // console.log("#[GeoLocation]# GeoLocation 사용불가 error: ", error);
    //         if (error.code == 1) {
    //           this.$refs.denied.openDialog();
    //           clearInterval(this.intervalGeolocation);
    //         }
    //       }
    //     );
    //   } else {
    //     console.log(
    //       "#[GeoLocation]# 해당 브라우저에서는 GPS를 사용할 수 없습니다."
    //     );
    //     alert("#[GeoLocation]# 해당 브라우저에서는 GPS를 사용할 수 없습니다.");
    //   }
    // },
    async getGeoLocation() {
      console.log("#[GeoLocation]# 동작");
      // 로그인한 member 객체 얻어오기
      if (this.member == null) {
        await store.dispatch("memberStore/isLogin");
      }

      navigator.permissions
        .query({ name: "geolocation" })
        .then((permissionStatus) => {
          console.log("#[GeoLocation]# permisstion 확인: ", permissionStatus);
          // i) 위치 권한 허용
          if (permissionStatus.state == "granted") {
            navigator.geolocation.getCurrentPosition((position) => {
              // 현 로그인한 사용자의 정보(id, nickname, latlng) 객체 생성
              const member = {
                memberId: this.member_id,
                memberNickname: this.member.memberNickname,
                memberLatLng: [
                  position.coords.latitude,
                  position.coords.longitude,
                ],
              };
              // 현 사용자의 위치 저장
              this.send(member);
            });
          }
          // ii) 위치 권한 요청 prompt가 뜬 경우
          else if (permissionStatus.state == "prompt") {
            this.geolocation();
          }
          // iii) 위치 권한 거부
          else {
            this.$refs.denied.openDialog();
            clearInterval(this.intervalGeolocation);
          }
        })
        .catch((error) => {
          console.log("#[GeoLocation]# error 확인: ", error);
        });
    },
    send(member) {
      // console.log("# send message: ", member);

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
:root {
  --swiper-theme-color: var(--main-col-1) !important;
}
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
