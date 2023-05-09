import Vue from "vue";
import VueRouter from "vue-router";

import main from "@/router/modules/main";
import meeting from "@/router/modules/meeting";
import error from "@/router/modules/error";

import store from "@/store";

import jwt_decode from "jwt-decode";

Vue.use(VueRouter);

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
  if (to.name === "landing" || to.name === "login") {
    //login page를 가거나 login이 성공 됐을 때는 다음으로 넘어감
    next();
  } else if (access_token) {
    store.dispatch(
      "memberStore/SET_MEMBER_ID",
      jwt_decode(access_token.substring(7)).id
    );
    //router 이동할 때 로그인된 member 정보 가져오기
    next();
  } else {
    //그 외에 모든 경로는 login으로
    next({
      name: "login",
    });
  }
});

export default router;
