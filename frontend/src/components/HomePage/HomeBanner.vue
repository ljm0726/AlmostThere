<template>
  <swiper class="swiper" :options="swiperOption">
    <swiper-slide>
      <v-sheet width="100%" height="200">Slide 1</v-sheet>
    </swiper-slide>
    <swiper-slide>
      <v-sheet width="100%" height="200">Slide 2</v-sheet>
    </swiper-slide>
    <swiper-slide>
      <v-sheet width="100%" height="200">Slide 3</v-sheet>
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
        },
      },
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
