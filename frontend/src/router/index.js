import Vue from "vue";
import VueRouter from "vue-router";
import Vuex from "vuex"; //

import main from "@/router/modules/main";
import meeting from "@/router/modules/meeting";
import error from "@/router/modules/error";

import store from "@/store";

import { apiInstance } from "@/api/index";
const api = apiInstance();

async function checkMeetingMember(roomCode) {
  await api.get(`/meeting/meeting-member/${roomCode}`).then((res) => {
    if (res.data.statusCode == "403") return null;
    if (res.data.statusCode == "200") return res.data.data; // meetingId return
  });
}

Vue.use(VueRouter);
Vue.use(Vuex); //

const routes = [...main, ...meeting, ...error];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
  scrollBehavior() {
    return { x: 0, y: 0 };
  },
});

router.beforeEach((to, from, next) => {
  const access_token = localStorage.getItem("Authorization");
  // console.log("Before", access_token, from, to);
  store.dispatch("memberStore/isLogin");

  if (access_token && to.name === "entrance") {
    // # 초대 링크 클릭 시 로그인 되어 있을 때

    // 파람 룸코드 받기
    const roomCode = to.params.roomCode;

    // 가입되어 있는지, 아닌지 체크하고 가입시킨 다음 meetingId 받기
    const meetingId = checkMeetingMember(roomCode);
    // const meetingId = 29;
    if (meetingId) {
      next({ name: "meeting", params: { id: meetingId } });
    } else {
      // 정원초과이면 meetingId가 null
      alert("모임의 정원을 초과하였습니다.");
      next({
        name: "home",
      });
    }
  } else if (to.name === "entrance") {
    // # 초대 링크 클릭 시 로그인 안 되어 있을 때
    const roomCode = to.params.roomCode;
    console.log("roomcode: ", roomCode);
    // 룸코드 store에 저장
    store.dispatch("meetingStore/setInvitedMeeting", roomCode);
    // login으로 보냄
    next({
      name: "login",
    });
    // login 성공하면 store에 roomcode 저장되어있는지 확인하는 로직 실행
  } else if (access_token) {
    next();
  } else if (to.name === "landing" || to.name === "login") {
    //login page를 가거나 login이 성공 됐을 때는 다음으로 넘어감
    next();
  } else {
    //그 외에 모든 경로는 login으로
    next({
      name: "login",
    });
  }
});

export default router;
