import Vue from "vue";
import Vuex from "vuex";

import placeStore from "./modules/place";
import meetingStore from "./modules/meeting";
import halfwayStore from "./modules/halfway";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    placeStore: placeStore,
    meetingStore: meetingStore,
    halfwayStore: halfwayStore,
  },
});
