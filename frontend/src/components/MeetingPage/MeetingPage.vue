<template>
  <div>
    <meeting-title
      :meetingName="meetingName"
      :meetingDate="meetingDate"
      :meetingTime="meetingTime"
      :meetingPlace="meetingPlace"
      :meetingAddress="meetingAddress"
      :lateAmount="lateAmount"
    ></meeting-title>
    <meeting-datetime
      :meetingDate="meetingDate"
      :meetingTime="meetingTime"
    ></meeting-datetime>
    <meeting-place
      :meetingPlace="meetingPlace"
      :meetingAddress="meetingAddress"
    ></meeting-place>
    <meeting-start-point
      :startPlace="startPlace"
      :startAddress="startAddress"
    ></meeting-start-point>
    <meeting-member :memberList="memberList"></meeting-member>
    <meeting-game></meeting-game>
    <meeting-late-fee :lateAmount="lateAmount"></meeting-late-fee>
    <meeting-cost :meetingId="meetingId"></meeting-cost>
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

export default {
  name: "MeetingPage",
  data() {
    return {
      loading: true,
      meetingId: 1,
      meetingName: "파이썬 알고리즘 스터디",
      meetingDate: "2023-04-04",
      meetingTime: "15:30",
      meetingPlace: "멀티캠퍼스 역삼",
      meetingAddress: "서울 강남구 테헤란로 212",
      startPlace: "배곧신도시SK뷰아파트",
      startAddress: "경기도 시흥시 배곧3로 27-7",
      memberList: [
        {
          memberProfileImg:
            "https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2023/04/16/56055563-5446-4847-b730-1e90f86509aa.jpg",
          memberNickname: "안유진",
          state: "GOING",
        },
        {
          memberProfileImg:
            "https://mblogthumb-phinf.pstatic.net/MjAyMTEyMzBfMTA4/MDAxNjQwODQ3NDI3MDM2.P4HfJr3z7A7UcGKXEYm4qI2sumDb6ZnvSSfoaux0V3Yg.i2Gk92Ap_426xzUsTNPBFGzRIFmTkIn9JxpSwpL_9U8g.JPEG.afeelcompany/%EC%95%84%EC%9D%B4%EB%B8%8C_%EA%B3%B5%EC%8B%9D_%ED%8A%B8%EC%9C%84%ED%84%B0_%EB%A6%AC%EC%A6%88_(3).jpg?type=w800",
          memberNickname: "김지원",
          state: "LATE",
        },
        {
          memberProfileImg:
            "https://news.koreadaily.com/data/photo/2023/03/11/202303111814773347_640c485bf380c.jpg",
          memberNickname: "장원영",
          state: "LATE",
        },
        {
          memberProfileImg:
            "https://image5jvqbd.fmkorea.com/files/attach/new2/20220307/4366334379/2059023680/4407516091/d0ee20c460ff99e881c4fb2c4328b34c.jpg",
          memberNickname: "이현서",
          state: "ARRIVE",
        },
        {
          memberProfileImg:
            "https://static-storychat.pstatic.net/2022/7/13/19/3516269_16hmae0ad1gea40.jpg?type=rsc5",
          memberNickname: "김가을",
          state: "GOING",
        },
        {
          memberProfileImg:
            "https://storage.blip.kr/collection/93cb2f579f2efc0a4110d8c71fee9451.jpg",
          memberNickname: "레이",
          state: "ARRIVE",
        },
      ],
      lateAmount: 1000,
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
};
</script>

<style></style>
