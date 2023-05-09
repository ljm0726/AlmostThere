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
          class="time"
          >{{ time }}분</span
        >
        <div v-show="minTimes.length % 2 == 1"></div>
      </div>
    </div>
    <div class="detail">
      <a :href="placeUrl">상세보기</a>
    </div>
  </div>
</template>

<script>
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

  props: {
    minTimes: Array,
    placeName: String,
    addressName: String,
    placeUrl: String,
    placeX: String,
    placeY: String,
    stateTraffic: String,
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
  width: 95%;
}
.child {
  display: flex;
  width: 40vw;
  justify-content: space-between;
}
.time {
  font-size: 20px;
  font-family: var(--medium-font);
  margin-top: 8%;
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
