import Vue from "vue";
import VueRouter from "vue-router";

import main from "@/router/modules/main";
import meeting from "@/router/modules/meeting";
import error from "@/router/modules/error";

Vue.use(VueRouter);

const routes = [...main, ...meeting, ...error];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach(async (to, from, next) => {
  const access_token = localStorage.getItem("Authorization");
  console.log("Before", access_token);
  if (access_token) {
    next();
  } else if (to.name === "login") {
    next();
  } else {
    next({
      name: "login",
    });
  }
});

export default router;
