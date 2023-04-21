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
    },
  },
};

export default placeStore;
