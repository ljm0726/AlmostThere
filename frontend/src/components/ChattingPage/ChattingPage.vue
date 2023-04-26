<template>
  <v-sheet height="100%">
    <!-- header -->
    <chatting-header :name="meetingName"></chatting-header>
    <internet-error ref="error"></internet-error>
    <chatting-loading v-if="loading"></chatting-loading>
    <v-sheet
      v-else
      class="d-flex flex-column justify-end"
      style="padding: 55px 0px 72px 0px"
      min-height="100%"
    >
      <!-- scroll 맨 아래로 내리는 버튼 -->
      <scroll-bottom-button></scroll-bottom-button>
      <!-- 채팅창 -->
      <v-sheet class="d-flex flex-column">
        <!-- 무한스크롤 -->
        <infinite-loading
          direction="top"
          @infinite="infiniteHandler"
        ></infinite-loading>

        <!-- 메세지 반복 -->
        <div v-for="(item, idx) in chatList" :key="idx">
          <!-- 날짜 : 년월일 (날짜가 같은 경우처음에만 노출) -->
          <div
            class="d-flex flex-row justify-center sm-font main-col-1 mt-3"
            v-if="
              idx == 0 ||
              item.chattingTime.split('T')[0] !=
                chatList[idx - 1].chattingTime.split('T')[0]
            "
          >
            {{ new Date(item.chattingTime).getFullYear() }}년
            {{ new Date(item.chattingTime).getMonth() + 1 }}월
            {{ new Date(item.chattingTime).getDate() }}일
          </div>
          <!-- 메세지 목록 -->
          <div
            class="mx-3 mt-1 d-flex flex-row"
            :class="item.memberId == memberId ? 'justify-end' : 'justify-start'"
          >
            <!-- 사용자 프로필 (시간이 같은 경우 처음에만 노출) -->
            <div v-if="item.memberId != memberId">
              <!-- 사진 -->
              <v-avatar
                class="mr-2"
                size="34"
                rounded="lg"
                v-if="
                  idx == 0 ||
                  item.memberId != chatList[idx - 1].memberId ||
                  item.chattingTime.split('T')[1].substr(0, 5) !=
                    chatList[idx - 1].chattingTime.split('T')[1].substr(0, 5)
                "
              >
                <img :src="members[item.memberId].profile" alt="John" />
              </v-avatar>
              <!-- 사진 없이 사진 크기와 동일한빈 공간 -->
              <v-sheet v-else class="mr-2" style="padding-left: 34px">
              </v-sheet>
            </div>

            <!-- 사용자 닉네임, 메세지 창, 등록시간 -->
            <v-sheet class="d-flex flex-column" width="100%">
              <!-- 사용자 닉네임 (시간이 같은 경우 처음에만 노출) -->
              <span
                class="xxxs-font"
                v-if="
                  item.memberId != memberId &&
                  (idx == 0 ||
                    item.memberId != chatList[idx - 1].memberId ||
                    item.chattingTime.split('T')[1].substr(0, 5) !=
                      chatList[idx - 1].chattingTime.split('T')[1].substr(0, 5))
                "
              >
                <span>{{ members[item.memberId].nickname }}</span>
              </span>

              <!-- 메세지 내용 및 시간 -->
              <v-sheet
                class="d-flex align-end"
                :class="
                  item.memberId == memberId ? 'flex-row-reverse' : 'flex-row'
                "
                width="100%"
              >
                <!-- 메세지 창 -->
                <v-sheet
                  class="pa-1 px-2 xs-font light-font d-flex flex-row"
                  max-width="70%"
                  :dark="item.memberId == memberId"
                  :outlined="item.memberId != memberId"
                  :color="item.memberId == memberId ? 'var(--main-col-1)' : ''"
                  elevation="0"
                  rounded="lg"
                  style="word-break: break-all"
                >
                  {{ item.message }}
                </v-sheet>
                <!-- 작성 날짜 -->
                <span
                  class="mx-1 xxxxs-font thin-font d-flex flex-row"
                  v-if="
                    idx == chatList.length - 1 ||
                    item.chattingTime.split('T')[1].substr(0, 5) !=
                      chatList[idx + 1].chattingTime.split('T')[1].substr(0, 5)
                  "
                >
                  <span
                    v-if="
                      new Date(item.chattingTime).getHours() == 0 ||
                      new Date(item.chattingTime).getHours() == 24
                    "
                  >
                    오전 12시
                    {{ new Date(item.chattingTime).getMinutes() }}분
                  </span>
                  <span
                    v-else-if="new Date(item.chattingTime).getHours() >= 12"
                  >
                    오후
                    {{ new Date(item.chattingTime).getHours() - 12 }}시
                    {{ new Date(item.chattingTime).getMinutes() }}분
                  </span>
                  <span v-else>
                    오전
                    {{ new Date(item.chattingTime).getHours() }}시
                    {{ new Date(item.chattingTime).getMinutes() }}분
                  </span>
                </span>
              </v-sheet>
            </v-sheet>
          </div>
        </div>
      </v-sheet>

      <!-- 메세지 입력창 -->
      <v-sheet
        class="px-3 pb-4"
        max-width="500"
        color="transparent"
        style="position: fixed; margin: 0 auto; left: 0; right: 0; bottom: 0"
      >
        <v-text-field
          style="background-color: white"
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
          maxlength="255"
        ></v-text-field>
      </v-sheet>
    </v-sheet>
  </v-sheet>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { getChatting, getChattingLog } from "@/api/modules/chatting.js";
import ChattingHeader from "@/views/Header/ChattingHeader.vue";
import InfiniteLoading from "vue-infinite-loading";
import ScrollBottomButton from "@/common/component/button/ScrollBottomButton.vue";
import InternetError from "@/common/component/dialog/InternetError.vue";
import ChattingLoading from "./ChattingLoading.vue";

export default {
  components: {
    ChattingHeader,
    InfiniteLoading,
    ScrollBottomButton,
    InternetError,
    ChattingLoading,
  },
  name: "ChattingPage",
  data() {
    return {
      memberId: 2, // 현재 로그인 돼 있는 사용자 아이디
      message: "", // 작성한 메세지 내용
      chatList: [], // 채팅 리스트
      meetingName: null, // 미팅 제목
      members: {}, // 미팅에 참여하는 멤버
      last: -1, // 무한 스크롤 마지막에 불러온 Index
      page: 1, // 무한 스크롤 페이지
      loading: true, // 페이지 로딩 여부
    };
  },
  async created() {
    this.loading = true;
    // 저장된 채팅 정보를 가져옵니다.
    await getChatting(this.$route.params.id).then(async (res) => {
      if (res && res.data.statusCode == 200) {
        const info = await res.data.data;
        // 방 이름
        this.meetingName = await info.meetingName;
        // 멤버 정보
        this.members = await info.chattingMemberMap;
        // 채팅 기록
        this.chatList =
          await info.chattingListDto.chattingDetailDtoList.reverse();
        // loading 상태 변경
        this.loading = await false;
        // 마지막 기록
        this.last = await info.chattingListDto.lastNumber;
        // 스크롤 맨 아래로 이동
        // this.goBottom();
        await setTimeout(this.goBottom, 100);
        // 요청 값을 받아오면 소켓 연결을 시도합니다.
        await this.connect();
      } else {
        this.$refs.error.openDialog();
      }
    });
  },
  methods: {
    // 무한 스크롤 함수
    infiniteHandler($state) {
      // 마지막 Index가 0이나 음수면 값을 다 가져왔다고 판단
      if (this.last <= 0) $state.complete();
      // 그 외에 가져와야 할 값이 더 있는 경우
      else {
        // ChattingLog를 가져오는 API 요청
        getChattingLog(this.$route.params.id, this.last).then(async (res) => {
          // chat 정보 저장
          const chat = await res.data.data;
          // 무한 스크롤 페이지
          this.page += 1;
          // 채팅 기록 리스트에 넣기
          this.chatList.unshift(...chat.chattingDetailDtoList.reverse());
          // 마지막 Index 업데이트
          this.last = await chat.lastNumber;
          // Rerendering
          $state.loaded();
        });
      }
    },
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
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send(
          `/message/receive/${this.$route.params.id}`,
          this.message,
          {}
        );
      }
    },
    // Websocket 연결
    connect() {
      const serverURL = `${process.env.VUE_APP_API_BASE_URL}/websocket`;
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      // console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
        {},
        () => {
          // (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          // console.log("소켓 연결 성공", frame);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          this.stompClient.subscribe(
            `/send/${this.$route.params.id}`,
            (res) => {
              // console.log("구독으로 받은 메시지 입니다.", res.body);
              const data = JSON.parse(res.body);
              if (data.statusCode == 200) {
                // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
                this.chatList.push(data.data);
                // 스크롤 맨 아래로 이동
                window.setTimeout(this.goBottom, 50);
              }
            }
          );
          this.stompClient.subscribe(
            `/send/${this.$route.params.id}/mine`,
            (res) => {
              console.log("/me 구독으로 받은 메시지 입니다.", res.body);
              const data = JSON.parse(res.body);
              if (data.statusCode == 200) {
                // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
                this.chatList.push(data.data);
                // 스크롤 맨 아래로 이동
                window.setTimeout(this.goBottom, 50);
              }
            }
          );
        },
        () => {
          // (error) => {
          // 소켓 연결 실패
          // console.log("소켓 연결 실패");
          this.connected = false;
        }
      );
    },
  },
};
</script>
