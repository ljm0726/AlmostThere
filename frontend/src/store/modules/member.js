import { getLoginMember } from "@/api/modules/member";
// import router from "@/router";

const memberStore = {
  namespaced: true,
  state: {
    member: null, // member 객체
  },
  getters: {},
  mutations: {
    SET_MEMBER(state, member) {
      state.member = member;
      console.log(state.member.id);
    },
  },
  actions: {
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
