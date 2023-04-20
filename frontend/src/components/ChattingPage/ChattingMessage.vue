<template>
  <div
    class="mx-4 my-1 d-flex flex-column"
    :class="item.nickname == nickname ? 'align-end' : 'align-start'"
  >
    <span>{{ item.nickname }}</span>
    <v-sheet class="pa-2" elevation="1" rounded="lg">{{
      item.message
    }}</v-sheet>
    <!-- <h3>유저이름: {{ item.nickname }}</h3> -->
    <!-- <h3>내용:</h3> -->
  </div>
</template>

<script>
export default {
  name: "ChattingMessage",
  props: {
    item: Object,
    nickname: String,
  },
};
</script>

<style></style>
