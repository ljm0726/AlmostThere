import { meetingRegister, modifyMeeting } from "@/api/modules/meeting";
import placeStore from "./place";
import memberStore from "./member";
import router from "@/router"; // 라우터 import

const meetingStore = {
  namespaced: true,
  state: {
    meeitng_id: null,
    meeting_host_id: null,
    meeting_name: null,
    meeting_date: null,
    meeting_time: null,
    place_name: null,
    place_addr: null,
    meeting_lat: null,
    meeting_lng: null,
    late_amount: 0, //지각비
    recent_meeting: null,
  },
  getters: {
    GET_RECENT_MEETING: function (state) {
      return state.recent_meeting;
    },
  },
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
      state.meeitng_id = meeting.meetingId;
      state.meeting_host_id = meeting.hostId;
      state.meeting_name = meeting.meetingName;
      state.place_name = meeting.meetingPlace;
      state.place_addr = meeting.meetingAddress;
      state.meeting_lat = meeting.meetingLat;
      state.meeting_lng = meeting.meetingLng;
      state.late_amount = meeting.lateAmount;
    },
    SET_RECENT_MEETING(state, recent_meeting) {
      console.log(recent_meeting);
      state.recent_meeting = recent_meeting;
      console.log("# meeting set 확인: ", recent_meeting);
    },
  },
  actions: {
    async register(
      { commit },
      { meeting_name, date_time, place_name, place_addr }
    ) {
      console.log(meeting_name, memberStore.state.member.id);
      await meetingRegister(
        // this.,
        memberStore.state.member.id,
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
      { commit, state },
      { meeting_name, date, time, place_name, place_addr, amount }
    ) {
      //meeting x,y 좌표를 받음.
      const date_time = new Date(date + " " + time);
      const X =
        placeStore.state.placeX == 0
          ? state.meeting_lat
          : placeStore.state.placeX;
      const Y =
        placeStore.state.placeY == 0
          ? state.meeting_lng
          : placeStore.state.placeY;
      console.log("XY", X, Y);
      // console.log(date_time, place_name, place_addr, amount);
      await modifyMeeting(
        // this.,
        // 장소 버튼을 누를 때 place store에있는 update actions 실행 해주기
        // 처음 가져올 때는 meeting에만 data 저장.
        state.meeitng_id,
        state.meeting_host_id,
        meeting_name,
        date_time,
        place_name,
        place_addr,
        X,
        Y,
        amount,
        ({ data }) => {
          console.log(data);
          commit("SET_MEETING_NAME", meeting_name);
          commit("SET_MEETING_DATE", date);
          commit("SET_MEETING_TIME", time);
          commit("SET_PLACE_NAME", place_name);
          commit("SET_PLACE_ADDR", place_addr);
          // router.push({ name: "home" });
        },
        (error) => {
          console.log(error);
          throw error;
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
    //
    setMeeting({ commit }, meeting) {
      commit("SET_RECENT_MEETING", meeting);
    },
  },
};

export default meetingStore;
