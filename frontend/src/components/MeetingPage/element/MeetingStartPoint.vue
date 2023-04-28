<template>
  <v-sheet class="mx-3 my-2 d-flex flex-column" color="transparent">
    <v-sheet class="d-flex flex-row justify-space-between align-end">
      <span class="point-font xxxxl-font main-col-1">나의 출발지</span>
      <v-btn
        class="d-flex justify-center align-center"
        id="square-btn"
        outlined
        color="var(--main-col-1)"
        rounded
        small
      >
        <v-icon>$vuetify.icons.edit_outline</v-icon>
      </v-btn>
    </v-sheet>
    <v-sheet
      class="my-1 py-3 px-4 d-flex flex-column justify-center detail-border main-col-1"
      rounded="lg"
    >
      <span class="sm-font">{{ startPlace }}</span>
      <span class="thin-font xxs-font">({{ startAddress }})</span>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "MeetingStartPoint",
  props: {
    startPlace: String,
    startAddress: String,
  },
};
</script>

<style></style>
