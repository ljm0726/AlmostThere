import BaseHeader from "@/views/Header/BaseHeader.vue";
import HomePage from "@/components/HomePage/HomePage.vue";
import RegisterPage from "@/components/RegisterPage/RegisterPage.vue";
import MyPage from "@/components/MyPage/MyPage.vue";
import TheNavigation from "@/views/TheNavigation.vue";
import TheLanding from "@/views/TheLanding.vue";
import LoginPage from "@/components/LoginPage/LoginPage.vue";
import PlacePage from "@/components/PlacePage/PlacePage.vue";
import SearchPlacePage from "@/components/PlacePage/SearchPlacePage.vue";

const isLogin = async (to, from, next) => {
  console.log(to, " ", to.query);
  const access_token = localStorage.getItem("Authorization");
  if (to.name === "landing") {
    next();
  }

  if (to.query.login || access_token) {
    console.log("login 성공 ");
    if (Object.keys(to.query).length !== 0) {
      localStorage.setItem("Authorization", to.query.login.substring(7));
    }
    next({
      name: "home",
    });
  } else if (to.name !== "landing") {
    // console.log("로그인 하러 옴");
    next({ name: "landing" });
  }
};

const home = [
  {
    path: "/",
    name: "landing",
    beforeEnter: isLogin,
    components: {
      default: TheLanding,
    },
  },
  {
    path: "/login",
    name: "login",
    components: {
      default: LoginPage,
    },
  },
  {
    path: "/home",
    name: "home",
    components: {
      header: BaseHeader,
      default: HomePage,
      navigation: TheNavigation,
    },
  },
  {
    path: "/register",
    name: "register",
    components: {
      header: BaseHeader,
      default: RegisterPage,
      navigation: TheNavigation,
    },
  },
  {
    path: "/place",
    name: "place",
    components: {
      default: PlacePage,
    },
  },
  {
    path: "/search",
    name: "search",
    components: {
      default: SearchPlacePage,
      navigation: TheNavigation,
    },
  },
  {
    path: "/place",
    name: "place",
    components: {
      default: PlacePage,
    },
  },
  {
    path: "/search",
    name: "search",
    components: {
      default: SearchPlacePage,
    },
  },
  {
    path: "/mypage",
    name: "mypage",
    components: {
      header: BaseHeader,
      default: MyPage,
      navigation: TheNavigation,
    },
  },
];

export default home;
