<template>
  <v-dialog v-model="dialog" scrollable max-width="300px" rounded="xl">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        class="mr-1"
        id="square-big-btn"
        outlined
        color="var(--main-col-1)"
        rounded
        v-bind="attrs"
        v-on="on"
      >
        <v-icon>$vuetify.icons.user_invite_outline</v-icon>
      </v-btn>
    </template>
    <v-card rounded="xl" class="pb-3">
      <v-card-title class="d-flex flex-column">
        <div class="align-self-end">
          <close-button @closeDialog="closeDialog"></close-button>
        </div>
        <span class="point-font xl-font main-col-1">{{ meetingName }}</span>
        <span class="point-font xxxxxxl-font main-col-1">초대 링크</span>
        <img src="@/assets/images/dialog/invite_link.png" width="60%" />
        <v-sheet
          class="pr-1 px-4 detail-border d-flex flex-row justify-space-between align-center"
          rounded="lg"
          width="100%"
        >
          <span class="xs-font light-font">{{
            link.length > 22 ? link.substr(0, 22) + "···" : link
          }}</span>
          <div>
            <v-btn icon><v-icon>$vuetify.icons.copy_outline</v-icon></v-btn>
            <v-btn icon><v-icon>$vuetify.icons.share_outline</v-icon></v-btn>
          </div>
        </v-sheet>
        <span class="extralight-font xs-font main-col-1">
          초대 링크를 공유해 멤버를 추가해 보세요.
        </span>
      </v-card-title>
    </v-card>
  </v-dialog>
</template>

<script>
import CloseButton from "@/common/component/button/CloseButton.vue";

export default {
  name: "MeetingInviteBtn",
  components: { CloseButton },
  data() {
    return {
      dialog: false,
      link: "http://localhost:3000/invite",
    };
  },
  props: {
    meetingName: String,
  },
  methods: {
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },
  },
};
</script>

<style></style>
