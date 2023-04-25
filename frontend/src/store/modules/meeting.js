import { meetingRegister } from "@/api/modules/meeting";
import placeStore from "./place";
import router from "@/router"; // 라우터 import

const meetingStore = {
  namespaced: true,
  state: {
    // placeX: null,
    // placeY: null,
    // placeName: null,
    // placeAddr: null,
  },
  getters: {},
  mutations: {
    // UPDATE_PLACE(state, placeMap) {
    //   state.placeX = placeMap.get("x");
    //   state.placeY = placeMap.get("y");
    //   state.placeName = placeMap.get("name");
    //   state.placeAddr = placeMap.get("addr");
    // },
  },
  actions: {
    async register({ commit }, { meeting_name, date_time }) {
      //id, lat, lng는 store에서 가져오기
      console.log(meeting_name);
      commit;
      await meetingRegister(
        // this.,
        1,
        meeting_name,
        date_time,
        placeStore.state.placeName,
        placeStore.state.placeAddr,
        placeStore.state.placeX,
        placeStore.state.placeY,
        ({ data }) => {
          console.log(data);
          router.push({ name: "home" });
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
};

export default meetingStore;
