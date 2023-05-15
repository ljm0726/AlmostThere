<template>
  <div class="p">
    <div class="name">
      {{ this.placeName }}
    </div>
    <img
      v-show="stateTraffic == 'bus'"
      id="bus-icon"
      class="traffic"
      :src="require('@/assets/images/icons/bus-icon.png')"
    />
    <img
      v-show="stateTraffic == 'car'"
      class="traffic"
      id="car-icon"
      :src="require('@/assets/images/icons/car-icon.png')"
    />
    <div class="addr">
      <span class="average">{{ this.averageTime }}분</span>
      <span class="text1"> &nbsp;&nbsp; 평균 이동 시간 </span>
    </div>
    <div class="parent">
      <div v-for="(time, index) in minTimes" :key="index" class="child">
        <img :src="startMarkerImage[index]" class="marker-image" /><span
          v-if="time > 2"
          class="time"
          >{{ time }}분</span
        ><span class="time2" v-else>700m 이내</span>
        <div v-show="minTimes.length % 2 == 1"></div>
      </div>
    </div>
    <div
      class="detail"
      style="display: flex; justify-content: space-between; width: 88%"
    >
      <a :href="placeUrl" class="xs-font">상세보기</a>
      <v-btn
        class="px-5"
        color="var(--main-col-1)"
        dark
        rounded
        medium
        @click="regist_meeting"
      >
        선택
      </v-btn>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  data() {
    return {
      startMarkerImage: [
        require(`@/assets/images/icons/marker1.png`),
        require(`@/assets/images/icons/marker2.png`),
        require(`@/assets/images/icons/marker3.png`),
        require(`@/assets/images/icons/marker4.png`),
        require(`@/assets/images/icons/marker5.png`),
        require(`@/assets/images/icons/marker6.png`),
        require(`@/assets/images/icons/marker7.png`),
        require(`@/assets/images/icons/marker8.png`),
        require(`@/assets/images/icons/marker9.png`),
        require(`@/assets/images/icons/marker10.png`),
      ],
      averageTime: Math.round(
        this.minTimes.reduce(function add(sum, currValue) {
          return sum + currValue;
        }, 0) / this.minTimes.length
      ),
    };
  },

  computed: {
    ...mapState("meetingStore", ["regist"]),
  },

  props: {
    minTimes: Array,
    placeName: String,
    addressName: String,
    placeUrl: String,
    placeX: String,
    placeY: String,
    stateTraffic: String,
  },

  methods: {
    ...mapActions("meetingStore", ["setRegistMeeting"]),
    ...mapActions("meetingStore", ["setRegistMeeting"]),
    ...mapActions("placeStore", ["updatePlace"]),

    regist_meeting() {
      const retrievedObject = sessionStorage.getItem("from");
      if (retrievedObject !== null) {
        const from = JSON.parse(retrievedObject);
        const placeMap = new Map();
        placeMap.set("x", this.placeX);
        placeMap.set("y", this.placeY);
        placeMap.set("name", this.placeName);
        placeMap.set("addr", this.addressName);
        this.updatePlace(placeMap);

        this.$router.replace(`/meeting/${from.id}`);
      } else {
        this.regist.lat = this.placeY;
        this.regist.lng = this.placeX;
        this.regist.place_name = this.placeName;
        this.regist.place_addr = this.addressName;

        this.setRegistMeeting(this.regist);
        this.$router.replace("/register"); // register페이지
      }
    },
  },

  // watch: {
  //   minTimes() {
  //     this.averageTime = Math.round(
  //       this.minTimes.reduce(function add(sum, currValue) {
  //         return sum + currValue;
  //       }, 0) / this.minTimes.length
  //     );
  //   },
  // },
};
</script>

<style scoped>
.traffic {
  width: 14%;
  position: absolute;
  margin-left: 80%;
  margin-top: -10%;
}
.p {
  background: #ffffff;
  box-shadow: 0px 4px 50px -10px rgba(0, 0, 0, 0.25);
  border-radius: 20px;
  width: 90%;
  margin-inline: -3.5%;
  height: auto;
}
.average {
  font-size: 20px;
  font-family: var(--extrabold-font);
}
.text1 {
  font-family: var(--light-font);
  font-size: 15px;
}
.name {
  padding: 25px;
  margin-top: 3%;
  font-size: 20px;
  font-family: var(--extrabold-font);
  padding-block: 7px;
}
.addr {
  padding: 25px;
  font-family: var(--light-font);
  font-size: 13px;
  padding-block: 5px;
  margin-top: -2%;
}
.parent {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  margin-left: 5%;
}
.child {
  display: flex;
  width: 50%;
  justify-content: space-between;
}
.time {
  font-size: 20px;
  font-family: var(--medium-font);
  margin-top: 8%;
  margin-inline: 8%;
}
.time2 {
  font-size: 17px;
  font-family: var(--medium-font);
  margin-top: 10%;
  margin-inline: 8%;
}
.marker-image {
  width: 30%;
}
.detail {
  margin-block: 3%;
  margin-left: 6%;
}
a {
  text-decoration: none;
  font-family: var(--light-font);
}
</style>
