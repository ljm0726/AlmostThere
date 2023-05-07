import { getMemberInfo, getLoginMember } from "@/api/modules/mypage";
// import router from "@/router";

const memberStore = {
  namespaced: true,
  state: {
    member: null, // member 객체
    attendMeetings: [], // 해당 member가 참여한 모든 meeting
    thisMonthAttendMeetingCnt: null, // 이번달 모임개수
    totalLateCnt: null, // 총 지각횟수
    lastMonthTotalSpentMoney: null, // 지난달 총 소비금액
  },
  getters: {},
  mutations: {
    SET_MEMBER(state, member) {
      state.member = member;
      console.log(state.member.id);
    },
    SET_ATTEND_MEETINGS(state, attendMeetings) {
      state.attendMeetings = attendMeetings;
    },
    SET_THIS_MONTH_ATTEND_MEETING_CNT(state, thisMonthAttendMeetingCnt) {
      state.thisMonthAttendMeetingCnt = thisMonthAttendMeetingCnt;
    },
    SET_TOTAL_LATE_CNT(state, totalLateCnt) {
      state.totalLateCnt = totalLateCnt;
    },
    SET_LAST_MONTH_TOTAL_SPENT_MONEY(state, lastMonthTotalSpentMoney) {
      state.lastMonthTotalSpentMoney = lastMonthTotalSpentMoney;
    },
  },
  actions: {
    // [@Method] my-page 회원정보 조회
    async excuteGetMemberInfo({ commit }) {
      // console.log("#21# 회원정보 조회 동작");
      var result = false;
      await getMemberInfo(
        async ({ data }) => {
          if (data.statusCode == 200) {
            // console.log("#21# data 확인: ", data);

            commit("SET_MEMBER", data.data.member);
            commit("SET_ATTEND_MEETINGS", data.data.attendMeetings);
            commit(
              "SET_THIS_MONTH_ATTEND_MEETING_CNT",
              data.data.thisMonthAttendMeetingCnt
            );
            commit("SET_TOTAL_LATE_CNT", data.data.totalLateCnt);
            commit(
              "SET_LAST_MONTH_TOTAL_SPENT_MONEY",
              data.data.lastMonthTotalSpentMoney
            );
            result = true;
          }
        },
        (error) => {
          console.log(error);
        }
      );
      return await Promise.resolve(result);
    },
    // [@Method] 로그인 회원 확인
    async isLogin({ commit }) {
      // console.log("2. getUserInfo() decodeToken :: ");
      await getLoginMember(
        ({ data }) => {
          if (data.message === "SUCCESS") {
            // console.log()
            console.log("3. getUserInfo data >> ", data);
            commit("SET_MEMBER", data.data);
          } else {
            console.log("유저 정보 없음");
          }
        },
        async (error) => {
          console.log(
            "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
            error
          );
          // router.push("/");
          // commit("SET_IS_VALID_TOKEN", false);
          // await dispatch("tokenRegeneration");
        }
      );
    },
  },
};

export default memberStore;
