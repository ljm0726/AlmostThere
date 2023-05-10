import BaseHeader from "@/views/Header/BaseHeader.vue";
import HomePage from "@/components/HomePage/HomePage.vue";
import RegisterPage from "@/components/RegisterPage/RegisterPage.vue";
import MyPage from "@/components/MyPage/MyPage.vue";
import TheNavigation from "@/views/TheNavigation.vue";
import TheLanding from "@/views/TheLanding.vue";
import LoginPage from "@/components/LoginPage/LoginPage.vue";
import PlacePage from "@/components/PlacePage/PlacePage.vue";
import SearchPlacePage from "@/components/PlacePage/SearchPlacePage.vue";
import SearchPlacePage2 from "@/components/PlacePage/SearchPlace/SearchPlacePage2";
import meetingStore from "@/store/modules/meeting";

const isLogin = async (to, from, next) => {
  // console.log(to, " ", to.query);
  const access_token = localStorage.getItem("Authorization");
  if (to.name === "landing") {
    next();
  }

  if (to.query.login || access_token) {
    console.log("login 성공 ");
    if (Object.keys(to.query).length !== 0) {
      localStorage.setItem(
        "Authorization",
        "Bearer " + to.query.login.substring(7)
      );
    }

    // if meeting store에 roomcode가 저장되어 있으면
    if (meetingStore.getters.GET_INVITED_MEETING) {
      const savedRoomCode = meetingStore.getters.GET_INVITED_MEETING;
      console.log("초대 링크 접속 후 저장된 RoomCode", savedRoomCode);
      // store 값을 null로 초기화하고
      meetingStore.mutations.SET_INVITED_MEETING = null;
      console.log(
        "스토어 룸코드 초기화 후",
        meetingStore.getters.GET_INVITED_MEETING
      );
      // 해당 룸코드로 entrance/:roomcode 로 보내서 모임에 가입되어 있는지 아닌지
      // 확인하는 로직 실행 후 meeting detail페이지로 보냄
      next({ name: "entrance", params: { roomCode: savedRoomCode } });
    } else {
      // 그게 아니면 아래의 next home으로 보냄
      next({
        name: "home",
      });
    }
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
    path: "/search2",
    name: "search2",
    components: {
      default: SearchPlacePage2,
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
