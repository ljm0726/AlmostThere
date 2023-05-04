<template>
  <v-sheet
    class="narrow-font d-flex flex-column justify-center align-center hugelarge-font main-col-1"
    height="100%"
    color="transparent"
  >
    <span class="logo-font">Almost</span>
    <span class="logo-font">There!</span>
    <img
      src="@/assets/images/page/login.png"
      height="450"
      style="text-align: center"
      alt=""
    />
    <div id="login-modal" class="d-flex flex-column align-center">
      <div>
        <img
          src="@/assets/images/component/login_modal.png"
          alt=""
          style="width: 100%"
        />
      </div>

      <div id="text" class="xs-font main-col-1">
        <span>친구들과 약속을 잡고</span>
        <br />
        <span>약속 당일 서로의 위치를 공유해 보세요!</span>
        <br />
        <br />
        <span>만남 장소 추천부터 모임 정산 기능까지</span>
        <br />
        <span>더 편하게 모임을 즐겨보세요!</span>

        <v-btn
          elevation="5"
          color="var(--main-col-1)"
          dark
          rounded
          block
          @click="kakaoLogin"
          >카카오로 시작하기</v-btn
        >
      </div>
    </div>
  </v-sheet>
</template>

<script>
export default {
  name: "user_login",
  methods: {
    kakaoLogin() {
      // console.log(`${process.env.VUE_APP_KAKAO_LOGIN_URL}`);
      window.location.replace(`${process.env.VUE_APP_KAKAO_LOGIN_URL}`);
    },
  },
};
</script>

<style scoped>
.logo-font {
  text-align: center;
}
#login-modal {
  position: absolute;
  /* background-image: url("@/assets/images/component/Rectangle21.png"); */
  top: 55%;
  /* transform: translateY(55%); */
  text-align: center;
  align-self: center;
  /* min-height: 280px; */
}
#text {
  position: absolute;
  text-align: center;
  top: 25%;
  width: auto;
}

#text > span {
  font-family: var(--regular-font);
  line-height: 18px;
}

.v-btn {
  margin-top: 10%;
  align-self: center;
  width: 213px;
}
.v-sheet {
  position: relative;
}
.logo-font {
  animation: jump 1s infinite;
}

@keyframes jump {
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
  100% {
    transform: translateY(0);
  }
}
</style>
