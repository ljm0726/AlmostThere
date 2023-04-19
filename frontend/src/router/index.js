import Vue from 'vue'
import VueRouter from 'vue-router'

import main from "@/router/modules/main";
import meeting from "@/router/modules/meeting";
import error from "@/router/modules/error"

Vue.use(VueRouter)

const routes = [
  ...main,
  ...meeting,
  ...error
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
