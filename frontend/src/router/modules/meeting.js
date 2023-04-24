import MeetingPage from "@/components/MeetingPage/MeetingPage.vue";
import MeetingHeader from "@/views/Header/MeetingHeader.vue";
import LiveMapPage from "@/components/LiveMapPage/LiveMapPage.vue";
import LiveMapHeader from "@/views/Header/LiveMapHeader.vue";

// 임시
import LiveMap from "@/components/LiveMapPage/element/LiveMap.vue";

const meeting = [
  {
    path: "/meeting/:id",
    name: "meeting",
    components: {
      header: MeetingHeader,
      default: MeetingPage,
    },
  },
  {
    path: "/live-map/:id",
    name: "live-map",
    components: {
      header: LiveMapHeader,
      default: LiveMapPage,
    },
  },
  {
    path: "/live-map-temp/:id",
    name: "live-map-temp",
    components: {
      header: LiveMapHeader,
      default: LiveMap,
    },
  },
];

export default meeting;
