<template>
  <v-sheet class="px-3 d-flex flex-row justify-space-between align-center xxxxl-font main-col-1" height="55" color="transparent">
    <back-button color="var(--main-col-1)"></back-button>
    <span class="logo-font">Almost There</span>
    <detail-button></detail-button>
  </v-sheet>
</template>

<script>
import BackButton from '@/common/component/button/BackButton.vue'
import DetailButton from '@/common/component/button/DetailButton.vue'

export default {
  name: "LiveMapHeader",
  components: {
    BackButton,
    DetailButton
  }
}
</script>

<style>

</style>