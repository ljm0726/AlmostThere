<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column main-col-1" color="transparent">
    <span class="point-font xxxxl-font">일시</span>
    <v-sheet class="d-flex flex-row justify-space-between" color="transparent">
      <v-sheet
        width="49%"
        class="my-2 py-2 d-flex justify-center detail-border main-col-1 sm-font"
        rounded="lg"
      >
        2023년 04월 04일
      </v-sheet>
      <v-sheet
        width="49%"
        class="my-2 py-2 d-flex justify-center detail-border main-col-1 sm-font"
        rounded="lg"
      >
        오후 15시 30분
      </v-sheet>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingDatetime",
  props: {
    meetingTime: String,
    meetingDate: String,
  },
};
</script>

<style></style>
