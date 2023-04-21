<template>
  <div
    class="mx-4 my-1 d-flex flex-column"
    :class="item.userId == userId ? 'align-end' : 'align-start'"
  >
    <span v-if="item.userId != userId">{{ item.userId }}</span>
    <div
      class="d-flex align-end"
      :class="item.userId == userId ? 'flex-row-reverse' : 'flex-row'"
    >
      <v-sheet class="pa-2 xs-font light-font" elevation="1" rounded="lg">{{
        item.message
      }}</v-sheet>

      <span class="xxxs-font thin-font">
        {{ chattingTime.getHours() }}시 {{ chattingTime.getMinutes() }}분
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChattingMessage",
  props: {
    item: Object,
    userId: Number,
  },
  computed: {
    chattingTime() {
      return new Date(this.item.chattingTime);
    },
  },
};
</script>

<style></style>
