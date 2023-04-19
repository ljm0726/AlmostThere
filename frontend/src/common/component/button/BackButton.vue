<template>
  <v-btn id="square-btn" outlined :color="color" @click="goBack()" rounded>
    <v-icon v-if="color == 'white'">$vuetify.icons.arrow_left_white</v-icon>
    <v-icon v-else>$vuetify.icons.arrow_left</v-icon>
  </v-btn>
</template>

<script>
export default {
  name: "BackButton",
  props: {
    color: String
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style>

</style>