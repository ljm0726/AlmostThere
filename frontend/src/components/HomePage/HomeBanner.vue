<template>
  <swiper class="swiper" :options="swiperOption">
    <swiper-slide>
      <v-sheet width="100%" height="250px" style="cursor: pointer">
        <home-banner-one></home-banner-one>
      </v-sheet>
    </swiper-slide>
    <swiper-slide>
      <v-sheet width="100%" height="250px" style="cursor: pointer">
        <home-banner-two></home-banner-two>
      </v-sheet>
    </swiper-slide>
    <swiper-slide>
      <v-sheet width="100%" height="250px" style="cursor: pointer">
        <home-banner-three></home-banner-three>
      </v-sheet>
    </swiper-slide>
    <!-- <swiper-slide v-for="(banner, index) in banners" :key="index"></swiper-slide> -->
    <!-- <v-sheet
        class="d-flex flex-row justify-space-between align-center"
        width="100%"
        height="250px"
        :style="{
          backgroundImage: 'url(' + banner.image + ')',
        }"
        style="
          background-size: 250px !important;
          background-position: 10% 50% !important;
        "
      >
        <v-sheet
          width="100%"
          color="transparent"
          class="mx-10 d-flex flex-column align-end justify-center"
          style="
            text-shadow: -1px 0 white, 0 1px white, 1px 0 white, 0 -1px white;
          "
        >
          <div class="d-flex flex-column align-end mb-1">
            <span
              class="point-font xxl-font main-col-1"
              v-for="(t, i) in banner.title"
              :key="i"
            >
              {{ t }}
            </span>
          </div>

          <span
            v-for="(content, idx) in banner.contents"
            :key="idx"
            class="light-font xxs-font main-col-1"
          >
            {{ content }}
          </span>
        </v-sheet>
      </v-sheet> -->
    <div class="swiper-pagination" slot="pagination"></div>
  </swiper>
</template>

<script>
import HomeBannerOne from "./element/HomeBannerOne.vue";
import HomeBannerTwo from "./element/HomeBannerTwo.vue";
import HomeBannerThree from "./element/HomeBannerThree.vue";

export default {
  name: "HomeBanner",
  components: {
    HomeBannerOne,
    HomeBannerTwo,
    HomeBannerThree,
  },
  data() {
    return {
      swiperOption: {
        slidesPerView: "auto",
        spaceBetween: 25,
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
        loop: true,
        autoplay: {
          delay: 3000,
          disableOnInteraction: false,
        },
      },
      // banners: [
      //   {
      //     title: ["VERSION 1.0", "RELEASE"],
      //     contents: ["Almost There", "서비스 OPEN"],
      //     image: require("@/assets/images/banner/home.png"),
      //   },
      //   {
      //     title: ["보다 편하게", "모임을 잡고 관리하세요"],
      //     contents: [
      //       "영수증을 인식해 N/1 정산 기능",
      //       "멤버 간 실시간 위치 공유",
      //       "출발지를 기반으로 한 중간 위치 추천",
      //     ],
      //     image: require("@/assets/images/banner/calendar.png"),
      //   },
      //   {
      //     title: ["개발팀 '어디야?'", "A401 Team"],
      //     contents: ["엄희정 지강훈", "신도연 안승태", "이종민 서지윤"],
      //     image: require("@/assets/images/banner/group.png"),
      //   },
      // ],
    };
  },
};
</script>

<style scoped>
.swiper-slide {
  width: 100%;
}
.swiper-container {
  width: 100% !important;
}
</style>
