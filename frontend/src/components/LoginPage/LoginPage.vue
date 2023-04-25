<template>
  <v-sheet
    class="narrow-font d-flex flex-column justify-center align-center hugelarge-font main-col-1 align-items: center"
    height="100%"
    color="transparent"
  >
    <span class="logo-font">Almost</span>
    <span class="logo-font">There!</span>
    <img
      src="@/assets/images/page/login.png"
      height="450"
      style="text-aling: center"
      alt=""
    />
    <div id="login-modal">
      <div>
        <img src="@/assets/images/component/login_modal.png" alt="" />
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
      </div>
    </div>
    <div>
      <v-col>
        <v-btn
          elevation="5"
          color="var(--main-col-1)"
          dark
          rounded
          block
          @click="kakaoLogin"
          >카카오로 시작하기</v-btn
        >
      </v-col>
    </div>
  </v-sheet>
</template>

<script>
export default {
  name: "user_login",
  data() {},
  methods: {
    kakaoLogin() {
      window.location.replace(
        "http://localhost:8080/oauth2/authorization/kakao"
      );
    },
  },
};
</script>

<style scoped>
.my-sheet {
  margin-right: auto;
}
.logo-font {
  text-align: center;
}
#login-modal {
  position: absolute;
  /* background-image: url("@/assets/images/component/Rectangle21.png"); */
  top: 55%;
  /* width: 200; */
  text-align: center;
}
#text {
  text-align: center;
  position: absolute;
  top: 40%;
  /* left: 50%; */
  transform: translate(0, -50%);
  width: 400px;
  font-family: var(--regular-font);
}

#text span {
  line-height: 15px;
}

.v-btn {
  text-align: center;
  width: 213px;
}
.v-sheet {
  position: relative;
}
</style>
