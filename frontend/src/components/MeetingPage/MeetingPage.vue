<template>
  <div>
    <meeting-title
      v-if="meeting.meetingName !== null"
      :meetingName="meeting.meetingName"
      :meetingDate="meeting.meetingDate"
      :meetingTime="meeting.meetingTime"
      :meetingPlace="meeting.meetingPlace"
      :meetingAddress="meeting.meetingAddress"
      :lateAmount="meeting.lateAmount"
    ></meeting-title>
    <meeting-datetime :meetingTime="meeting.meetingTime"></meeting-datetime>
    <meeting-place
      :meetingPlace="meeting.meetingPlace"
      :meetingAddress="meeting.meetingAddress"
    ></meeting-place>
    <meeting-start-point
      :startPlace="startPlace"
      :startAddress="startAddress"
      :startLat="startLat"
      :startLng="startLng"
    ></meeting-start-point>
    <meeting-member :memberList="meeting.meetingMembers"></meeting-member>
    <meeting-game></meeting-game>
    <meeting-late-fee :lateAmount="meeting.lateAmount"></meeting-late-fee>
    <meeting-cost
      :meetingId="meeting.meetingId"
      :calculateDetails="meeting.calculateDetails"
      :spentMoney="spentMoney"
      :lateTotal="lateTotal"
      :total="total"
      :remain="meeting.remain"
    ></meeting-cost>
  </div>
</template>

<script>
import MeetingDatetime from "./element/MeetingDatetime.vue";
import MeetingLateFee from "./element/MeetingLateFee.vue";
import MeetingMember from "./element/MeetingMember.vue";
import MeetingPlace from "./element/MeetingPlace.vue";
import MeetingStartPoint from "./element/MeetingStartPoint.vue";
import MeetingTitle from "./element/MeetingTitle.vue";
import MeetingCost from "./element/MeetingCost.vue";
import MeetingGame from "./element/MeetingGame.vue";
import { getMeeting } from "@/api/modules/meeting.js";
import { mapActions, mapState } from "vuex";

export default {
  name: "MeetingPage",
  data() {
    return {
      loading: true,
      meeting: {
        hostId: null,
        meetingId: null,
        meetingName: null,
        meetingPlace: null,
        meetingTime: null,
        meetingAddress: null,
        meetingLat: null,
        meetingLng: null,
        lateAmount: null,
        roomCode: null,
        remain: null,
        meetingMembers: [],
        calculateDetails: [],
      },
      startPlace: null,
      startAddress: null,
      startLat: null,
      startLng: null,
      spentMoney: null,
      lateTotal: null,
      total: null,
    };
  },
  computed: {
    ...mapState("meetingStore", [
      "place_name",
      "place_addr",
      "meeting_lat",
      "meeting_lng",
    ]),
    ...mapState("placeStoremet", ["placeName", "placeAddr"]),
  },
  watch: {
    place_name() {
      this.meeting.meetingPlace = this.place_name;
    },
    place_addr() {
      this.meeting.meetingAddress = this.place_addr;
    },
  },
  components: {
    MeetingTitle,
    MeetingDatetime,
    MeetingPlace,
    MeetingStartPoint,
    MeetingMember,
    MeetingLateFee,
    MeetingCost,
    MeetingGame,
  },
  created() {
    getMeeting(this.$route.params.id).then((res) => {
      this.meeting = res;
      this.setting(this.meeting.meetingMembers);

      this.SET_MEETING_INFO(res);
    });
  },
  methods: {
    ...mapActions("meetingStore", ["SET_MEETING_INFO"]),

    setting(meetingMembers) {
      const member = meetingMembers.filter(
        (member) => member.memberId == "1"
      )[0];
      this.startPlace = member.startPlace;
      this.startAddress = member.startAddress;
      this.startLat = member.startLat;
      this.startLng = member.startLng;
      this.spentMoney = member.spentMoney === null ? 0 : member.spentMoney;
      this.lateTotal =
        member.state == "LATE"
          ? this.meeting.lateAmount === null
            ? 0
            : this.meeting.lateAmount
          : 0;
      this.total = this.meeting.calculateDetails.reduce(
        (accumulator, current) => accumulator + current.price,
        0
      );
    },
  },
};
</script>

<style></style>
