<template>
  <v-sheet
    max-width="500"
    color="transparent"
    width="100%"
    class="d-flex flex-row justify-end align-center"
    style="
      position: fixed;
      margin: 0 auto;
      left: 0;
      right: 0;
      bottom: 0;
      padding: 0 10px 65px 0;
    "
  >
    <v-btn
      v-scroll="onScroll"
      v-show="fab"
      dark
      color="var(--main-col-1)"
      style="background-color: white"
      @click="toBottom"
      icon
      outlined
      small
    >
      <v-icon>mdi-arrow-down</v-icon>
    </v-btn>
  </v-sheet>
</template>

<script>
export default {
  name: "ScrollBottomButton",
  data() {
    return {
      fab: false,
    };
  },
  methods: {
    onScroll() {
      if (typeof window === "undefined") return;
      const scrollY = window.scrollY;
      const visible = document.documentElement.clientHeight;
      const pageHeight = document.documentElement.scrollHeight;
      const bottomOfPage = visible + scrollY + 20 >= pageHeight;
      this.fab = !(bottomOfPage || pageHeight < visible);
    },
    // [@Method] 맨 위로 가기
    toBottom() {
      window.scrollTo(0, document.querySelector("body").scrollHeight);
    },
  },
};
</script>

<style></style>
