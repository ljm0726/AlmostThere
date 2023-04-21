<template>
  <v-sheet
    class="d-flex flex-column justify-end"
    style="padding: 55px 0px 72px 0px"
    min-height="100%"
  >
    <chatting-header></chatting-header>
    <v-sheet class="d-flex flex-column" min-height="100">
      <div v-for="(item, idx) in chatList" :key="idx">
        <div
          class="d-flex flex-row justify-center sm-font main-col-1"
          v-if="
            idx == 0 ||
            item.chattingTime.split('T')[0] !=
              chatList[idx - 1].chattingTime.split('T')[0]
          "
        >
          {{ new Date(item.chattingTime).getFullYear() }}년
          {{ new Date(item.chattingTime).getMonth() }}월
          {{ new Date(item.chattingTime).getDate() }}일
        </div>
        <!-- <chatting-message :item="item" :userId="userId"> </chatting-message> -->
        <div
          class="mx-4 mt-1 d-flex flex-column"
          :class="item.memberId == memberId ? 'align-end' : 'align-start'"
        >
          <span
            v-if="
              item.memberId != memberId &&
              (idx == 0 ||
                item.memberId != chatList[idx - 1].memberId ||
                item.chattingTime.split('T')[1].substr(0, 5) !=
                  chatList[idx - 1].chattingTime.split('T')[1].substr(0, 5))
            "
            >{{ item.memberId }}</span
          >
          <div
            class="d-flex align-end"
            :class="item.memberId == memberId ? 'flex-row-reverse' : 'flex-row'"
          >
            <v-sheet
              class="pa-1 px-2 xs-font light-font"
              max-width="370"
              :dark="item.memberId == memberId"
              :outlined="item.memberId != memberId"
              :color="item.memberId == memberId ? 'var(--main-col-1)' : ''"
              elevation="0"
              rounded="lg"
              style="word-break: break-all"
              >{{ item.message }}</v-sheet
            >
            <span
              class="mx-1 xxxxs-font thin-font"
              v-if="
                idx == chatList.length - 1 ||
                item.chattingTime.split('T')[1].substr(0, 5) !=
                  chatList[idx + 1].chattingTime.split('T')[1].substr(0, 5)
              "
            >
              {{ new Date(item.chattingTime).getHours() }}시
              {{ new Date(item.chattingTime).getMinutes() }}분
            </span>
          </div>
        </div>
      </div>
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
// import ChattingMessage from "./ChattingMessage.vue";

export default {
  components: { ChattingHeader },
  name: "ChattingPage",
  data() {
    return {
      memberId: 2,
      message: "",
      chatList: [],
    };
  },
  async created() {
    // App.vue가 생성되면 소켓 연결을 시도합니다.
    this.connect();
    // 저장된 채팅 정보를 가져옵니다.
    await getChatting(this.$route.params.id).then((res) => {
      this.chatList = res.data.data.chattingList;
      console.log("chatList >>", this.chatList);
      // 스크롤 맨 아래로 이동
      setTimeout(this.goBottom, 50);
    });
  },
  methods: {
    // 맨 아래로 스크롤 이동
    goBottom() {
      window.scrollTo(0, document.querySelector("body").scrollHeight);
    },
    // 메세지 보내고, 입력 내용 초기화
    sendMessage() {
      if (this.memberId !== "" && this.message !== "") {
        this.send();
        this.message = "";
      }
    },
    // 메세지 전송
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send(
          `/message/receive/${this.$route.params.id}`,
          this.message,
          {}
        );
      }
    },
    connect() {
      const serverURL = `${process.env.VUE_APP_API_BASE_URL}/websocket`;
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
              // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
              this.chatList.push(JSON.parse(res.body).data);
              // 스크롤 맨 아래로 이동
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
