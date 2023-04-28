<template>
  <v-btn color="var(--main-col-1)" icon large @click="moveChatting()">
    <v-icon large>$vuetify.icons.chatting_outline</v-icon>
  </v-btn>
</template>

<script>
export default {
  name: "MoveChattingBtn",
  methods: {
    moveChatting() {
      this.$router.push(`/chat/${this.$route.params.id}`);
    },
  },
};
</script>

<style></style>
