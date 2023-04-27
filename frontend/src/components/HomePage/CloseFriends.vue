<template>
  <v-sheet class="px-4 mt-6" color="transparent">
    <span class="point-font xxxxl-font main-col-1">자주 만나는 친구</span>
    <swiper
      v-if="members.length > 0"
      class="swiper mt-1"
      :options="swiperOption"
      style="height: 270px"
    >
      <swiper-slide
        v-for="(member, idx) in members"
        :key="idx"
        class="mt-1"
        style="height: 30%"
      >
        <v-card
          width="100%"
          height="100%"
          class="card-gradient mx-auto my-2 d-flex flex-row"
          elevation="0"
          rounded="lg"
        >
          <v-sheet
            width="100"
            :style="{
              // backgroundImage: `url(${member.friend.memberProfileImg})`,
              backgroundImage: `url(https://cdn.vuetifyjs.com/images/john.jpg)`,
              backgroundSize: 'cover',
              borderTopRightRadius: '0px',
              borderBottomLeftRadius: 'inherit',
              backgroundPosition: 'center',
            }"
          ></v-sheet>
          <div class="d-flex flex-row justify-space-between">
            <div class="px-6 mx-auto d-flex justify-center align-center">
              <span class="point-font xxxl-font">{{ idx + 1 }}</span>
            </div>
            <div class="mt-1">
              <div class="xxxxs-font" style="color: #7e7e7e">
                <span>총 누적 만남 횟수&nbsp;</span>
                <span class="medium-font">{{ member.cnt }}회</span>
              </div>
              <div>
                <span class="xl-font">{{ member.friend.memberNickname }}</span>
              </div>
            </div>
          </div>
        </v-card>
      </swiper-slide>
    </swiper>
    <div v-else class="d-flex justify-center align-center">
      <v-sheet width="100%" class="white-col-1 mb-4" color="transparent">
        <img
          :src="require('@/assets/images/component/friend_no_data.png')"
          class="d-flex justify-center align-center"
          width="100%"
        />
      </v-sheet>
    </div>
  </v-sheet>
</template>

<script>
import { getBestMember } from "@/api/modules/meeting.js";
export default {
  name: "CloseFriends",
  data() {
    return {
      members: [],
      swiperOption: {
        slidesPerView: 1,
        slidesPerColumn: 3,
        spaceBetween: 3,
      },
    };
  },
  created() {
    getBestMember().then((res) => {
      this.members = res;
      console.log(this.members.length);
    });
  },
};
</script>

<style></style>
