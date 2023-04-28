<template>
  <swiper class="swiper" :options="swiperOption">
    <swiper-slide v-for="(banner, index) in banners" :key="index">
      <v-sheet
        width="100%"
        height="200"
        class="d-flex flex-row align-center justify-center"
      >
        {{ banner.title }}
      </v-sheet>
    </swiper-slide>
    <div class="swiper-pagination" slot="pagination"></div>
  </swiper>
</template>

<script>
export default {
  name: "HomeBanner",
  data() {
    return {
      swiperOption: {
        slidesPerView: "auto",
        spaceBetween: 25,
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
      },
      banners: [
        { title: "Slider 1" },
        { title: "Slider 2" },
        { title: "Slider 3" },
      ],
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
