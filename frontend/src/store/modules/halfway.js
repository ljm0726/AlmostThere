// import meetingStore from "./meeting";

const halfwayStore = {
  namespaced: true,
  state: {
    startPlaces: [null, null],
    middlePlace: [],
  },
  getters: {},
  mutations: {
    ADD_MIDDLE_PLACE(state, middlePlace) {
      state.middlePlace = middlePlace;
      console.log("저장완료: ", state.middlePlace);
    },
    UPDATE_START_PLACE(state, startPlace) {
      state.startPlaces[localStorage.getItem("listIndex") - 1] = startPlace;
    },
    ADD_LIST(state) {
      state.startPlaces.push(null);
    },
    REMOVE_START_PLACE(state, index) {
      state.startPlaces.splice(index, 1);
    },
    RESET_START_PLACE(state) {
      state.startPlaces = [null, null];
    },
  },

  actions: {
    resetStartPlace({ commit }) {
      commit("RESET_START_PLACE");
    },
    addMiddlePlace({ commit }, middlePlace) {
      commit("ADD_MIDDLE_PLACE", middlePlace);
    },
    updateHalfway({ commit }, startPlace) {
      commit("UPDATE_START_PLACE", startPlace);
      console.log(startPlace.get("name"));
    },
    addPlaceList({ commit }) {
      commit("ADD_LIST");
    },
    removePlaceList({ commit }, index) {
      commit("REMOVE_START_PLACE", index);
    },
  },
};

export default halfwayStore;
