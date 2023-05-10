import {
  meetingRegister,
  modifyMeeting,
  outMeeting,
} from "@/api/modules/meeting";
import placeStore from "./place";
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

    regist: {
      name: null,
      date: null,
      time: null,
      lat: null,
      lng: null,
      place_name: null,
      place_addr: null,
    },
    invited_meeting: null,
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
      state.recent_meeting = recent_meeting;
    },
    SET_INVITED_MEETING(state, invited_meeting) {
      state.invited_meeting = invited_meeting;
    },
    SET_REGIST_INFO(state, regist) {
      state.regist.name = regist.name;
      state.regist.date = regist.date;
      state.regist.time = regist.time;
      state.regist.lat = regist.lat;
      state.regist.lng = regist.lng;
      state.regist.place_name = regist.place_name;
      state.regist.place_addr = regist.place_addr;
    },

    RESET_REGIST(state) {
      state.regist = {
        name: null,
        date: null,
        time: null,
        lat: null,
        lng: null,
        place_name: null,
        place_addr: null,
      };
    },
  },

  actions: {
    async register(
      { commit },
      {
        member_id,
        meeting_name,
        date_time,
        place_name,
        place_addr,
        meeting_lat,
        meeting_lng,
      }
    ) {
      console.log(
        meeting_name,
        date_time,
        place_name,
        place_addr,
        meeting_lat,
        meeting_lng
      );
      await meetingRegister(
        // this.,
        member_id,
        meeting_name,
        date_time,
        place_name,
        place_addr,
        meeting_lat,
        meeting_lng,
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

    async outMeeting({ state }, member_id) {
      // console.log(date_time, place_name, place_addr, amount);
      console.log(member_id);
      await outMeeting(
        // this.,
        // 장소 버튼을 누를 때 place store에있는 update actions 실행 해주기
        // 처음 가져올 때는 meeting에만 data 저장.
        member_id,
        state.meeitng_id,
        ({ data }) => {
          console.log(data);
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
    setMeeting({ commit }, meeting) {
      commit("SET_RECENT_MEETING", meeting);
    },

    setRegistMeeting({ commit }, regist) {
      commit("SET_REGIST_INFO", regist);
    },

    resetRegist({ commit }) {
      commit("RESET_REGIST");
    },
    setInvitedMeeting({ commit }, roomCode) {
      commit("SET_INVITED_MEETING", roomCode);
    },
  },
};

export default meetingStore;
