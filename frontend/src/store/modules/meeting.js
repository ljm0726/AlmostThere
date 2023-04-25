import { meetingRegister } from "@/api/modules/meeting";

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
    async register(meeting_name, date_time) {
      //id, lat, lng는 store에서 가져오기
      console.log(this.placeX, " ", this.placeY);
      await meetingRegister(
        // this.,
        1,
        meeting_name,
        date_time,
        this.place,
        this.address,
        this.placeX,
        this.placeY,
        ({ data }) => {
          console.log(data);
          this.$router.push("/home");
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
};

export default meetingStore;
