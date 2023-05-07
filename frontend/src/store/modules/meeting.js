import { meetingRegister, modifyMeeting } from "@/api/modules/meeting";
import placeStore from "./place";
import memberStore from "./member";
import router from "@/router"; // 라우터 import

const meetingStore = {
  namespaced: true,
  state: {
    meeting_name: null,
    meeting_date: null,
    meeting_time: null,
    place_name: null,
    place_addr: null,
    meeting_lat: null,
    meeting_lng: null,
    late_amount: 0, //지각비
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
    SET_MEETING_INFO(state, meeting) {
      state.meeting_name = meeting.meetingName;
      state.place_name = meeting.meetingPlace;
      state.place_addr = meeting.meetingAddress;
      state.meeting_lat = meeting.meetingLat;
      state.meeting_lng = meeting.meetingLng;
      state.late_amount = meeting.lateAmount;
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
        memberStore.memberId,
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

    async modify(
      { commit },
      { meeting_name, date_time, place_name, place_addr }
    ) {
      //meeting x,y 좌표를 받음.
      console.log(memberStore.memberId, date_time);
      await modifyMeeting(
        // this.,
        // 장소 버튼을 누를 때 place store에있는 update actions 실행 해주기
        // 처음 가져올 때는 meeting에만 data 저장.
        memberStore.memberId,
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

    SET_MEETING_INFO({ commit }, meeting) {
      console.log(meeting);
      commit("SET_MEETING_INFO", meeting);
    },
  },
};

export default meetingStore;
