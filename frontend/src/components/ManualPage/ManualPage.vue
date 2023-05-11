<template>
  <v-sheet width="100%">
    <v-sheet
      max-width="500"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        top: 0;
        z-index: 2;
      "
    >
      <nav-header></nav-header>
      <v-tabs
        v-model="tab"
        background-color="transparent"
        color="var(--main-col-1)"
        height="40"
        grow
      >
        <v-tab v-for="item in items" :key="item">
          <span class="sm-font medium-font">{{ item }}</span>
        </v-tab>
      </v-tabs>
    </v-sheet>
    <!-- <div> -->
    <v-tabs-items v-model="tab">
      <v-tab-item>
        <v-sheet style="padding: 115px 30px 70px 30px">
          <div class="mb-5 d-flex flex-column main-col-1">
            <span class="logo-font xxxxxxl-font">Almost There?</span>
            <v-divider
              class="mb-3"
              width="250"
              style="border-color: var(--main-col-1)"
            ></v-divider>
            <div class="mb-2 xxs-font d-flex flex-column thin-font">
              Almost There은
            </div>
          </div>
          <div class="mb-5 d-flex flex-column main-col-1">
            <span class="logo-font xxxxxxl-font">Open Source</span>
            <v-divider
              class="mb-3"
              width="250"
              style="border-color: var(--main-col-1)"
            ></v-divider>
            <div class="mb-2 xxs-font d-flex flex-column thin-font">
              해당 프로젝트는 오픈소스 프로젝트로 진행
            </div>
          </div>
          <div class="d-flex flex-column main-col-1">
            <span class="logo-font xxxxxxl-font">Developer</span>
            <v-divider
              class="mb-3"
              width="250"
              style="border-color: var(--main-col-1)"
            ></v-divider>
            <div class="mb-2 xxs-font d-flex flex-column thin-font">
              <div>
                <span class="mr-1 bold-font">진행</span>
                <span>삼성청년SW아카데미, SSAFY 8기 자율 프로젝트</span>
              </div>
              <div>
                <span class="mr-1 bold-font">팀명</span>
                <span>A401 어디야?</span>
              </div>
              <div>
                <span class="mr-1 bold-font">팀장</span>
                <span>엄희정</span>
              </div>
              <div>
                <span class="mr-1 bold-font">문의</span>
                <span>hihejyop@gmail.com</span>
              </div>
            </div>
            <div class="sm-font">
              <div class="my-1">
                <v-chip class="mr-2" color="var(--main-col-1)" dark small>
                  실시간 위치 공유
                </v-chip>
                <span class="light-font xs-font">엄희정 지강훈</span>
              </div>
              <div class="my-1">
                <v-chip class="mr-2" color="var(--main-col-1)" dark small>
                  중간 위치 찾기
                </v-chip>
                <span class="light-font xs-font">안승태 이종민</span>
              </div>
              <div class="my-1">
                <v-chip class="mr-2" color="var(--main-col-1)" dark small>
                  정산 및 실시간 채팅
                </v-chip>
                <span class="light-font xs-font">신도연 서지윤</span>
              </div>
            </div>
          </div>
        </v-sheet>
      </v-tab-item>
      <v-tab-item>
        <v-sheet style="padding: 115px 30px 70px 30px">
          <div
            class="mb-2 d-flex flex-column point-font main-col-1 xxxxl-font semi2narrow-font"
          >
            <span>모임을 만들고 관리하고</span>
            <span>다양한 기능을 이용해보세요!</span>
            <v-divider
              class="mt-2"
              width="300"
              style="border-color: var(--main-col-1)"
            ></v-divider>
          </div>
          <div class="d-flex flex-column flex-no-strech">
            <div class="d-flex flex-column my-3">
              <v-chip color="var(--red-col)" dark>
                <span class="mr-2 black-font">STEP 1</span>
                <span>모임 등록하기</span>
              </v-chip>
              <div>dd</div>
            </div>
            <div class="d-flex flex-column my-3">
              <v-chip color="var(--yellow-col)" dark>
                <span class="mr-2 black-font">STEP 2</span>
                <span>친구 초대하기</span>
              </v-chip>
            </div>
            <div class="d-flex flex-column my-3">
              <v-chip color="var(--green-col)" dark>
                <span class="mr-2 black-font">STEP 3</span>
                <span>나의 출발지 정하기</span>
              </v-chip>
            </div>
            <div class="d-flex flex-column my-3">
              <v-chip color="var(--blue-col)" dark>
                <span class="mr-2 black-font">STEP 4</span>
                <span>정산하기</span>
              </v-chip>
              <div class="mt-2 d-flex flex-column">
                <span>1. 지각비 설정</span>
                <span>2. 영수증 등록</span>
                <span>3. 정산 내역 확인</span>
                <span>TIP. 돌려돌려 돌림판</span>
              </div>
            </div>
          </div>
        </v-sheet>
      </v-tab-item>
      <v-tab-item>
        <v-sheet style="padding: 115px 30px 70px 30px" height="1500">3</v-sheet>
      </v-tab-item>
      <v-tab-item>
        <v-sheet style="padding: 115px 30px 70px 30px" height="1500">4</v-sheet>
      </v-tab-item>
    </v-tabs-items>
    <!-- </div> -->

    <v-bottom-navigation
      fixed
      style="margin: 0 auto; left: 0; right: 0; box-shadow: none"
      max-width="500"
      height="50"
    >
      <v-btn
        id="manual-btn"
        class="sm-font point-font"
        color="var(--main-col-1)"
        style="color: white !important"
        block
        @click="goRegister()"
      >
        모임 등록하러가기
      </v-btn>
    </v-bottom-navigation>
  </v-sheet>
</template>

<script>
import NavHeader from "@/views/Header/NavHeader.vue";
export default {
  components: { NavHeader },
  name: "ManualPage",
  data() {
    return {
      tab: 0,
      items: ["소개", "모임", "중간장소", "실시간위치"],
    };
  },
  methods: {
    goRegister() {
      this.$router.push("/register");
    },
  },
};
</script>

<style>
.v-tabs:not(.v-tabs--vertical):not(.v-tabs--right)
  > .v-slide-group--is-overflowing.v-tabs-bar--is-mobile:not(
    .v-slide-group--has-affixes
  )
  .v-slide-group__prev {
  display: none !important;
}
</style>
