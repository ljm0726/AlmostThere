// import meetingStore from "./meeting";

const halfwayStore = {
  namespaced: true,
  state: {
    startPlaces: [null, null],
  },
  getters: {},
  mutations: {
    UPDATE_START_PLACE(state, startPlace) {
      state.startPlaces[localStorage.getItem("listIndex") - 1] = startPlace;

      console.log(state.startPlaces);
    },
    ADD_LIST(state) {
      state.startPlaces.push(null);
    },
    REMOVE_START_PLACE(state, index) {
      state.startPlaces.splice(index, 1);
    },
  },

  actions: {
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
