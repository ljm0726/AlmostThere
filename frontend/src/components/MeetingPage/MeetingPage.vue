<template>
  <div>
    <meeting-title
      :meetingName="meeting.meetingName"
      :meetingDate="meeting.meetingDate"
      :meetingTime="meeting.meetingTime"
      :meetingPlace="meeting.meetingPlace"
      :meetingAddress="meeting.meetingAddress"
      :lateAmount="meeting.lateAmount"
    ></meeting-title>
    <meeting-datetime
      :meetingDate="meeting.meetingDate"
      :meetingTime="meeting.meetingTime"
    ></meeting-datetime>
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
    });

    // test용
    this.startPlace = "뚝섬한강공원";
    this.startAddress = "서울특별시 광진구 자양동";
    this.startLat = 37.5296;
    this.startLng = 127.0698;
  },
  methods: {
    setting(meetingMembers) {
      const member = meetingMembers.filter(
        (member) => member.memberId == "1"
      )[0];
      this.startPlace = member.startPlace;
      this.startAddress = member.startAddress;
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
