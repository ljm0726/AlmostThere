<template>
  <v-sheet
    class="d-flex flex-column justify-end"
    style="padding: 55px 0px 72px 0px"
    min-height="100%"
  >
    <chatting-header></chatting-header>

    유저이름:
    <v-text-field v-model="nickname" type="text"></v-text-field>
    <v-sheet class="d-flex flex-column" min-height="100">
      <chatting-message
        v-for="(item, idx) in chatList"
        :key="idx"
        :item="item"
        :nickname="nickname"
      ></chatting-message>
    </v-sheet>

    <!-- <div> -->
    <v-sheet
      class="px-4 py-4"
      max-width="500"
      style="position: fixed; margin: 0 auto; left: 0; right: 0; bottom: 0"
    >
      <v-text-field
        v-model="message"
        @click:append-outer="sendMessage"
        @click:clear="message == ''"
        @keyup.enter="sendMessage"
        placeholder="메시지를 입력해주세요"
        outlined
        append-outer-icon="$vuetify.icons.send_outline"
        dense
        hide-details
        clearable
      ></v-text-field>
    </v-sheet>
    <!-- </div> -->
  </v-sheet>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { getChatting } from "@/api/modules/chatting.js";
import ChattingHeader from "@/views/Header/ChattingHeader.vue";
import ChattingMessage from "./ChattingMessage.vue";

export default {
  components: { ChattingHeader, ChattingMessage },
  name: "ChattingPage",
  data() {
    return {
      nickname: "",
      message: "",
      chatList: [],
    };
  },
  async created() {
    // App.vue가 생성되면 소켓 연결을 시도합니다.
    this.connect();
    // 저장된 채팅 정보를 가져옵니다.
    await getChatting(this.$route.params.id).then((res) => {
      this.chatList = res.data.data.chatList;
      setTimeout(this.goBottom, 50);
    });
  },
  methods: {
    goBottom() {
      // window.scrollTo(0, document.body.scrollHeight);
      // console.log("scrollTop", document.scrollTop);
      // console.log("scrollTop", document.scrollHeight);
      // const main = document.querySelector(".v-main");
      // main.scrollTop = main.scrollHeight;
      console.log("height", document.querySelector("body").scrollHeight);
      window.scrollTo(0, document.querySelector("body").scrollHeight);
      // scrollTo(0, main.offsetTop + main.offsetHeight);
      // console.log("scrollTop", main.offsetTop + main.offsetHeight);
    },
    sendMessage() {
      if (this.nickname !== "" && this.message !== "") {
        this.send();
        this.message = "";
      }
    },
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          nickname: this.nickname,
          message: this.message,
        };
        this.stompClient.send(
          `/message/receive/${this.$route.params.id}`,
          JSON.stringify(msg),
          {}
        );
      }
    },
    connect() {
      const serverURL = "http://localhost:9999/websocket";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          console.log("소켓 연결 성공", frame);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          this.stompClient.subscribe(
            `/send/${this.$route.params.id}`,
            (res) => {
              console.log("구독으로 받은 메시지 입니다.", res.body);
              console.log("넣을 리스트", this.chatList);

              // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
              this.chatList.push(JSON.parse(res.body));
              window.setTimeout(this.goBottom, 50);
            }
          );
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          this.connected = false;
        }
      );
    },
  },
};
</script>
