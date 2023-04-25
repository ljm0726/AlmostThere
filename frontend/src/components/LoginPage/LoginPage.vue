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
  /* transform: translateY(55%); */
  text-align: center;
  align-self: center;
}
#text {
  position: absolute;
  text-align: center;
  top: 30%;
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
