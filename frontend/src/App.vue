<template>
  <v-app>
    <!-- Header -->
    <router-view name="header" />
    <!-- Main -->
    <v-main>
      <router-view />
    </v-main>
    <!-- Footer -->
    <router-view name="footer" />
    <!-- Navigation -->
    <router-view name="navigation" />
  </v-app>
</template>

<script>
export default {
  name: "App",
};
</script>

<style>
@import "@/assets/styles/text/text_font.css";
@import "@/assets/styles/text/text_size.css";
@import "@/assets/styles/text/text_color.css";
@import "@/assets/styles/text/text_height.css";
@import "@/assets/styles/variable.css";
@import "@/assets/styles/font.css";
@import "@/assets/styles/override/button.css";
@import "@/assets/styles/override/icon.css";
@import "@/assets/styles/override/dialog.css";
@import "@/assets/styles/override/badge.css";
@import "@/assets/styles/override/swiper.css";
@import "@/assets/styles/box/box_shadow.css";
@import "@/assets/styles/box/box_border.css";

html body {
  background: #fafafa;
  max-width: 500px;
  margin: 0 auto;
  height: 100%;
  min-height: 100%;
}
#app {
  max-width: 100%;
  min-height: 100%;

  font-family: var(--regular-font);
}
</style>
