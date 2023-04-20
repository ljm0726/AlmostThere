import MeetingPage from "@/components/MeetingPage/MeetingPage.vue"
import MeetingHeader from "@/views/Header/MeetingHeader.vue"
import LiveMapPage from "@/components/LiveMapPage/LiveMapPage.vue"
import LiveMapHeader from "@/views/Header/LiveMapHeader.vue"
import ChattingPage from "@/components/ChattingPage/ChattingPage.vue"

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
    path: "/chat/:id",
    name: "chat",
    components: {
      default: ChattingPage,
    },
  },
]

export default meeting;
