<template>
  <v-sheet>
    <v-sheet
      class="pa-3"
      style="
        background-image: linear-gradient(to bottom, #092a49 75%, white 25%);
      "
    >
      <v-sheet
        class="pa-6 d-flex flex-column justify-space-between detail-shadow detail-border"
        height="190"
        outlined
        rounded="xl"
      >
        <div class="d-flex flex-row justify-end">
          <v-skeleton-loader
            type="image"
            width="30"
            height="30"
          ></v-skeleton-loader>
        </div>
        <div class="d-flex flex-row justify-space-between align-end">
          <div>
            <img height="35" width="35" src="@/assets/images/logo/logo.png" />
            <v-skeleton-loader
              type="image"
              width="200"
              height="30"
            ></v-skeleton-loader>
          </div>
          <div class="d-flex flex-row">
            <v-skeleton-loader
              class="mr-2"
              type="image"
              width="30"
              height="30"
            ></v-skeleton-loader>
            <v-skeleton-loader
              type="image"
              width="30"
              height="30"
            ></v-skeleton-loader>
          </div>
        </div>
      </v-sheet>
    </v-sheet>

    <!-- <div class="d-flex flex-column"> -->
    <div v-for="n in 4" :key="n" class="mx-3 my-2">
      <v-skeleton-loader
        type="image"
        width="100"
        height="30"
      ></v-skeleton-loader>
      <v-skeleton-loader
        class="my-1"
        type="image"
        width="100%"
        height="80"
      ></v-skeleton-loader>
    </div>
    <!-- </div> -->
  </v-sheet>
</template>

<script>
export default {};
</script>

<style></style>
