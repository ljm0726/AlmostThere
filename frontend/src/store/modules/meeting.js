import { meetingRegister, saveMemberStartPlace } from "@/api/modules/meeting";
import placeStore from "./place";
import router from "@/router"; // 라우터 import

const meetingStore = {
  namespaced: true,
  state: {
    meeting_name: null,
    meeting_date: null,
    meeting_time: null,
    place_name: null,
    place_addr: null,
  },
  getters: {},
  mutations: {
    SET_MEETING_NAME(state, meeting_name) {
      state.meeting_name = meeting_name;
    },
    SET_MEETING_DATE(state, meeting_date) {
      state.meeting_date = meeting_date;
    },
    SET_MEETING_TIME(state, meeting_time) {
      state.meeting_time = meeting_time;
    },
    SET_PLACE_NAME(state, place_name) {
      console.log(place_name);
      state.place_name = place_name;
      console.log(state.place_name);
    },
    SET_PLACE_ADDR(state, place_addr) {
      console.log(place_addr);
      state.place_addr = place_addr;
      console.log(state.place_addr);
    },
  },
  actions: {
    async register(
      { commit },
      { meeting_name, date_time, place_name, place_addr }
    ) {
      console.log(meeting_name, date_time);
      await meetingRegister(
        // this.,
        1,
        meeting_name,
        date_time,
        place_name,
        place_addr,
        placeStore.state.placeX,
        placeStore.state.placeY,
        ({ data }) => {
          console.log(data);
          commit("SET_MEETING_NAME", null);
          commit("SET_MEETING_DATE", null);
          commit("SET_MEETING_TIME", null);
          commit("SET_PLACE_NAME", null);
          commit("SET_PLACE_ADDR", null);
          router.push({ name: "home" });
        },
        (error) => {
          console.log(error);
        }
      );
    },

    SET_MEETING_NAME({ commit }, meeting_name) {
      commit("SET_MEETING_NAME", meeting_name);
    },
    SET_MEETING_DATE({ commit }, meeting_date) {
      commit("SET_MEETING_DATE", meeting_date);
    },
    SET_MEETING_TIME({ commit }, meeting_time) {
      commit("SET_MEETING_TIME", meeting_time);
    },
    // [@Method] 출발지 저장
    async excuteSaveStartPlace(meetingRoomId) {
      console.log("#21# roomId, member에 따른 출발지 저장: ", meetingRoomId);
      var result = false;

      await saveMemberStartPlace(
        meetingRoomId,
        async ({ data }) => {
          if (data.statusCode == 200) {
            result = true;
          }
        },
        (error) => {
          console.log(error);
        }
      );
      return await Promise.resolve(result);
    },
  },
};

export default meetingStore;
