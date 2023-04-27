// import meetingStore from "./meeting";

const placeStore = {
  namespaced: true,
  state: {
    placeX: null,
    placeY: null,
    placeName: null,
    placeAddr: null,
  },
  getters: {},
  mutations: {
    UPDATE_PLACE(state, placeMap) {
      state.placeX = placeMap.get("x");
      state.placeY = placeMap.get("y");
      state.placeName = placeMap.get("name");
      state.placeAddr = placeMap.get("addr");
    },
  },
  actions: {
    updatePlace({ commit }, placeMap) {
      commit("UPDATE_PLACE", placeMap);
      console.log(placeMap.get("name"));

      // meetingStore.commit("SET_PLACE_NAME", placeMap.get("name"));
      commit("meetingStore/SET_PLACE_NAME", placeMap.get("name"), {
        root: true,
      });
      commit("meetingStore/SET_PLACE_ADDR", placeMap.get("addr"), {
        root: true,
      });
    },
  },
};

export default placeStore;
