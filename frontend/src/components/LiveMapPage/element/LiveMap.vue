<template>
  <div>
    <!-- test용 (!추후 삭제) -->
    <v-text-field label="채팅 test" v-model="testChatContent"></v-text-field>
    <v-btn @click="sendChatTest()">채팅 test</v-btn>
    <v-btn @click="resizeMapLevel()">범위 재조정</v-btn>
    <v-btn @click="chageLatLngTest()">좌표 변경 TEST</v-btn>
    <!-- --- -->
    <div id="map"></div>
  </div>
</template>

<script>
export default {
  name: "LiveMap",
  data() {
    return {
      /* # marker 설정 */
      placeMarkerSize: [50, 70], // 모임장소 marker 크기
      memberMarkerSize: [120, 120], // member marker 크기
      placeMarkerOption: [25, 65], // 모임장소 - image marker 위치 (좌표 X)
      memberMarkerOption: [60, 85], // member - image marker 위치
      /* # over-lay 설정 */
      memberOverlay: [0.5, 3.2], // member over-lay (x, y) 위치 좌표
      distanceOverlay: [-0.3, -0.1], // distance over-lay 좌표
      chatOverlay: [0.5, 4], // chat over-lay 좌표
      /* # 현재 좌표 */
      placeLatLng: [37.5013, 127.0396], // 모임장소 좌표
      memberLocation: [
        // 각 member 별 현재 좌표
        {
          member: {
            memberId: 1,
            memberNickname: "김싸피",
            memberLatLng: [37.5004, 127.0361], // 역삼역
          },
        },
        {
          member: {
            memberId: 2,
            memberNickname: "히정",
            memberLatLng: [37.5048, 127.0413], // 역삼 신라스테이
          },
        },
        {
          member: {
            memberId: 3,
            memberNickname: "시카",
            memberLatLng: [37.4912, 127.0557], // 도곡역
          },
        },
      ],
      /* # member 채팅 */
      testChatContent: "", // test용 (!추후 삭제)
      chatting: [], // 멤버 별 실시간 chatting 내용
      /* # 생성한 marker, overlay 저장 (memberId 기준) */
      memberMarkerList: [], // marker
      memberChatOverlayList: [], // 채팅 over-lay
      memberDistanceOverlayList: [], // 거리(m) over-lay
      memberNicknameOverlayList: [], // nickname over-lay
      memberPolylineList: [], // polyline
      /* # 위치 변경된 member[index, id] */
      updateMemberInfo: [], // [index, id] 위치 업데이트 된 member의 > memberLocation index와 memberId 저장
    };
  },
  watch: {
    chatting() {
      this.updateChatOverlay(this.chatting);
    },
    memberLocation: {
      deep: true,
      handler() {
        // console.log("#21# memberLocation 변경 감지");
        // 위치 값 변경에 따른 marker, polyline, overlay 재생성
        this.refreshMapOnLocationUpdate();
      },
    },
  },
  mounted() {
    // Kakao Map Script import
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${process.env.VUE_APP_KAKAO_API_KEY}`;
      document.head.appendChild(script);
    }
  },
  methods: {
    // [@Method] Kakao Map 생성
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(this.placeLatLng[0], this.placeLatLng[1]),
        level: 4,
        // mapTypeId: kakao.maps.MapTypeId.ROADMAP, // 지도 타입 (ROADMAP, SATELLITE, HYBRID, TERRAIN 중 선택)
      };

      // 지도 객체 등록
      this.map = new kakao.maps.Map(container, options);

      // 초기 marker 생성
      // i) 모임 장소 marker
      this.createPlaceMarker(options);
      // ii) 멤버 별 marker 생성
      this.createMemberMarker();
    },
    // [@Method] Kakao Map 생성 + 배경화면 설정
    // initMap() {
    //   const domain = "https://i1.daumcdn.net";
    //   const path = "/dmaps/apis/openapi/sampleimg/";
    //   const plan = (x, y, z) => {
    //     y = -y - 1;
    //     const limit = Math.ceil(3 / Math.pow(2, z));

    //     if (0 <= y && y < limit && 0 <= x && x < limit) {
    //       return domain + path + "planh" + z + "_" + y + "_" + x + ".png";
    //     } else {
    //       return "https://i1.daumcdn.net/dmaps/apis/white.png";
    //     }
    //   };

    //   kakao.maps.Tileset.add(
    //     "PLAN",
    //     new kakao.maps.Tileset(512, 512, plan, "", false, 0, 10) // 0, 10으로 level 범위
    //   );

    //   const container = document.getElementById("map");
    //   const options = {
    //     projectionId: null,
    //     mapTypeId: kakao.maps.MapTypeId.PLAN,
    //     $scale: false,
    //     center: new kakao.maps.LatLng(this.placeLatLng[0], this.placeLatLng[1]),
    //     // center: new kakao.maps.Coords(650, -550),
    //     level: 4,
    //   };

    //   // 지도 객체 등록
    //   this.map = new kakao.maps.Map(container, options);

    //   // marker 생성
    //   // i) 모임 장소 marker
    //   this.createPlaceMarker(options);
    //   // ii) 멤버 별 marker 생성
    //   this.createMemberMarker();
    // },
    // [@Method] 모임장소 marker 생성
    createPlaceMarker(options) {
      const imageSrc = require("@/assets/images/page/pointer.png");
      const imageSize = new kakao.maps.Size(
        this.placeMarkerSize[0],
        this.placeMarkerSize[1]
      );
      const imageOption = {
        offset: new kakao.maps.Point(
          this.placeMarkerOption[0],
          this.placeMarkerOption[1]
        ),
      };
      const markerImage = new kakao.maps.MarkerImage(
        imageSrc,
        imageSize,
        imageOption
      );
      const marker = new kakao.maps.Marker({
        position: options.center,
        image: markerImage,
      });
      // marker 표시
      marker.setMap(this.map);
    },
    // [@Method] 멤버 별 캐릭터 marker 생성
    createMemberMarker() {
      var markerType = 0;
      for (var ml of this.memberLocation) {
        // i) marker option 설정
        const imageSrc = require(`@/assets/images/animals/${markerType}.png`);
        markerType += 1;

        const imageSize = new kakao.maps.Size(
          this.memberMarkerSize[0],
          this.memberMarkerSize[1]
        );
        const imageOption = {
          offset: new kakao.maps.Point(
            this.memberMarkerOption[0],
            this.memberMarkerOption[1]
          ),
        };
        const markerImage = new kakao.maps.MarkerImage(
          imageSrc,
          imageSize,
          imageOption
        );
        const location = new kakao.maps.LatLng(
          ml.member.memberLatLng[0],
          ml.member.memberLatLng[1]
        );
        // ii) marker 생성
        const marker = new kakao.maps.Marker({
          position: location,
          image: markerImage,
        });

        // marker 저장 (for. 삭제)
        const object = new Object();
        object[ml.member.memberId] = marker;
        this.memberMarkerList.push(object);
        // const index = this.memberMarkerList.findIndex(
        //   (obj) => Object.keys(obj)[0] == ml.member.memberId
        // );
        // // i) 해당 member의 marker가 이미 있는 경우 값 업데이트(삭제, 추가)
        // if (index > -1) {
        //   this.memberMarkerList[index][ml.member.memberId].setMap(null); // 기존 marker 삭제
        //   this.memberMarkerList[index][ml.member.memberId] = marker; // 새로운 marker 추가
        // }
        // // ii) 없는 경우, marker 추가
        // else {
        //   const object = new Object();
        //   object[ml.member.memberId] = marker;
        //   this.memberMarkerList.push(object);
        // }

        // marker 표시
        marker.setMap(this.map);

        // iii) 멤버 별 over-lay, polyline 생성 (닉네임, 모임장소와의 거리)
        this.createMemberOverlay(ml.member, marker);
        this.createDistance(ml.member, marker);
      }
    },
    // [@Method] 지도 범위 조정
    resizeMapLevel() {
      const bounds = new kakao.maps.LatLngBounds(); // 지도 재설정할 범위정보 LatLngBounds 객체

      // LatLngBounds 객체에 좌표 추가 (모임장소, 멤버)
      const place = new kakao.maps.LatLng(
        this.placeLatLng[0],
        this.placeLatLng[1]
      );
      bounds.extend(place);

      for (const marker of this.memberMarkerList) {
        const memberId = Object.keys(marker)[0];
        bounds.extend(marker[memberId].getPosition());
      }

      // 좌표를 기준으로 지도 범위 재설정
      this.map.setBounds(bounds);
    },
    // [@Method] member 별 닉네임 over-lay 생성
    createMemberOverlay(member, marker) {
      const content = `<div class="member-overlay point-font">${member.memberNickname}</div>`;
      const position = marker.getPosition();

      // over-lay 생성
      const customOverlay = new kakao.maps.CustomOverlay({
        position: position,
        content: content,
        xAnchor: this.memberOverlay[0], // 오버레이 표시 x, y 위치
        yAnchor: this.memberOverlay[1],
      });
      // over-lay 저장 (for. 삭제)
      const object = new Object();
      object[member.memberId] = customOverlay;
      this.memberNicknameOverlayList.push(object);
      // const index = this.memberNicknameOverlayList.findIndex(
      //   (obj) => Object.keys(obj)[0] == member.memberId
      // );
      // // i) 이미 있는 경우 값 업데이트(삭제, 추가)
      // if (index > -1) {
      //   this.memberNicknameOverlayList[index][member.memberId].setMap(null); // 기존 over-lay 삭제
      //   this.memberNicknameOverlayList[index][member.memberId] = customOverlay; // 새로운 over-lay 추가
      // }
      // // ii) 없는 경우, marker 추가
      // else {
      //   const object = new Object();
      //   object[member.memberId] = customOverlay;
      //   this.memberNicknameOverlayList.push(object);
      // }

      // 오버레이 표시
      customOverlay.setMap(this.map);
    },
    // [@Method] 모임장소와의 거리 계산 및 표시
    createDistance(member, marker) {
      // i) 선을 그릴 좌표 setting
      const distancePath = [
        // 모임장소 좌표
        new kakao.maps.LatLng(this.placeLatLng[0], this.placeLatLng[1]),
        // 현재 member 좌표
        new kakao.maps.LatLng(
          marker.getPosition().getLat(),
          marker.getPosition().getLng()
        ),
      ];
      // ii) 선 setting
      const polyline = new kakao.maps.Polyline({
        path: distancePath,
        strokeWeight: 4, // 선의 두께
        strokeColor: "var(--main-col-1)",
        strokeOpacity: 1, // 불투명도 (0에 가까울수록 투명)
        strokeStyle: "solid",
      });

      // polyline 저장 (for. 삭제)
      const object = new Object();
      object[member.memberId] = polyline;
      this.memberPolylineList.push(object);
      // const index = this.memberPolylineList.findIndex(
      //   (obj) => Object.keys(obj)[0] == member.memberId
      // );
      // // i) 이미 있는 경우 값 업데이트(삭제, 추가)
      // if (index > -1) {
      //   this.memberPolylineList[index][member.memberId].setMap(null); // 기존 over-lay 삭제
      //   this.memberPolylineList[index][member.memberId] = polyline; // 새로운 over-lay 추가
      // }
      // // ii) 없는 경우, marker 추가
      // else {
      //   const object = new Object();
      //   object[member.memberId] = polyline;
      //   this.memberPolylineList.push(object);
      // }

      // 선 표시
      polyline.setMap(this.map);

      // 두 좌표의 거리 over-lay 표시
      const distance = Math.round(polyline.getLength());
      this.createDistanceOverlay(distance, marker, member);
    },
    // [@Method] member와 모임장소 거리 - 오버레이 표시
    createDistanceOverlay(distance, marker, member) {
      const content = `<div class="distance-overlay logo-font">${distance}m</div>`;
      const position = marker.getPosition();

      // 오버레이 생성
      const customOverlay = new kakao.maps.CustomOverlay({
        position: position,
        content: content,
        xAnchor: this.distanceOverlay[0],
        yAnchor: this.distanceOverlay[1],
      });
      // over-lay 저장 (for. 삭제)
      const object = new Object();
      object[member.memberId] = customOverlay;
      this.memberDistanceOverlayList.push(object);
      // const index = this.memberDistanceOverlayList.findIndex(
      //   (obj) => Object.keys(obj)[0] == member.memberId
      // );
      // // i) 이미 있는 경우 값 업데이트(삭제, 추가)
      // if (index > -1) {
      //   this.memberNicknameOverlayList[index][member.memberId].setMap(null); // 기존 over-lay 삭제
      //   this.memberNicknameOverlayList[index][member.memberId] = customOverlay; // 새로운 over-lay 추가
      // }
      // // ii) 없는 경우, marker 추가
      // else {
      //   const object = new Object();
      //   object[member.memberId] = customOverlay;
      //   this.memberDistanceOverlayList.push(object);
      // }

      // 오버레이 표시
      customOverlay.setMap(this.map);
    },
    // [@Method] chatting 내용 over-lay 표시
    updateChatOverlay() {
      for (var chat of this.chatting) {
        // chatting 내용이 없는 경우 생성 X
        if (chat.member.content == null || chat.member.content == "") continue;

        const content = `<div class="chat-overlay point-font">${chat.member.content}</div>`;
        const memberMarkerLatLng = this.memberLocation.find(
          (loc) => loc.member.memberId == chat.member.memberId
        ).member.memberLatLng;
        const position = new kakao.maps.LatLng(
          memberMarkerLatLng[0],
          memberMarkerLatLng[1]
        );

        // 오버레이 생성
        const customOverlay = new kakao.maps.CustomOverlay({
          position: position,
          content: content,
          xAnchor: this.chatOverlay[0],
          yAnchor: this.chatOverlay[1],
        });
        // 생성한 오버레이 삭제 후 업데이트 or 저장
        const index = this.memberChatOverlayList.findIndex(
          (obj) => Object.keys(obj)[0] == chat.member.memberId
        );
        // i) 해당 member의 chatOverlay가 있는 경우 값 업데이트(삭제, 추가)
        if (index > -1) {
          this.memberChatOverlayList[index][chat.member.memberId].setMap(null); // 기존 오버레이 삭제
          this.memberChatOverlayList[index][chat.member.memberId] =
            customOverlay; // 새로운 오버레이 추가
        }
        // ii) 없는 경우, 오버레이 추가
        else {
          const object = new Object();
          object[chat.member.memberId] = customOverlay;
          this.memberChatOverlayList.push(object);
        }

        // 오버레이 표시
        customOverlay.setMap(this.map);
      }
    },
    // [@Method] member 위치 update
    updateMemberLocation(newMemberLocation) {
      let memberIndex = -1;
      // memberId를 통해 해당 member 찾기
      for (let i = 0; i < this.memberLocation.length; i++) {
        if (
          this.memberLocation[i].member.memberId ==
          newMemberLocation.member.memberId
        ) {
          memberIndex = i;
          break;
        }
      }
      // 위치 값 update
      this.memberLocation[memberIndex].member.memberLatLng =
        newMemberLocation.member.memberLatLng;

      // 변경된 member [index, id] 저장
      this.updateMemberInfo = [
        memberIndex, // memberLocation의 index값
        this.memberLocation[memberIndex].member.memberId, // memberId 값
      ];
    },
    // [@Method] member의 위치 값 변경에 따른 marker, polyline, overlay 업데이트
    refreshMapOnLocationUpdate() {
      console.log("#21# 위치 값 변경에 따른 refresh");
      // 변경해야 하는 것: marker, polyline, 거리 overlay, 닉네임 overlay
      // console.log(
      //   "#21# marker, polyline, overlay 확인: ",
      //   this.memberMarkerList,
      //   this.memberPolylineList,
      //   this.memberDistanceOverlayList,
      //   this.memberNicknameOverlayList
      // );
      console.log(
        "#21# marker, polyline, overlay 확인: ",
        this.memberPolylineList
      );
      const refreshMember = this.memberLocation[this.updateMemberInfo[0]];
      // this.updateMemberInfo[0] = 변경된 memberLocation 배열의 index 값
      // this.updateMemberInfo[1] = 변경된 memberId
      const newPosition = new kakao.maps.LatLng(
        refreshMember.member.memberLatLng[0],
        refreshMember.member.memberLatLng[1]
      );

      // i) marker
      const marker =
        this.memberMarkerList[this.updateMemberInfo[0]][
          this.updateMemberInfo[1]
        ];
      marker.setPosition(newPosition);
      marker.setMap(this.map);

      // ii) over-lay & polyline
      // - 닉네임 over-lay
      const nickIndex = this.memberNicknameOverlayList.findIndex(
        (obj) => Object.keys(obj)[0] == this.updateMemberInfo[1]
      );
      const nickOverlay =
        this.memberNicknameOverlayList[nickIndex][this.updateMemberInfo[1]];
      nickOverlay.setPosition(newPosition);
      nickOverlay.setMap(this.map);
      // - 거리 polyline(선)
      const polyIndex = this.memberPolylineList.findIndex(
        (obj) => Object.keys(obj)[0] == this.updateMemberInfo[1]
      );
      const polyline =
        this.memberPolylineList[polyIndex][this.updateMemberInfo[1]];
      const newPath = [
        new kakao.maps.LatLng(this.placeLatLng[0], this.placeLatLng[1]),
        newPosition,
      ];
      polyline.setPath(newPath);
      polyline.setMap(this.map);
      // - 거리 over-lay
      const distanceIndex = this.memberDistanceOverlayList.findIndex(
        (obj) => Object.keys(obj)[0] == this.updateMemberInfo[1]
      );
      const newContent = `<div class="distance-overlay logo-font">${Math.round(
        polyline.getLength()
      )}m</div>`;
      const distanceOverlay =
        this.memberDistanceOverlayList[distanceIndex][this.updateMemberInfo[1]];
      distanceOverlay.setPosition(newPosition);
      distanceOverlay.setContent(newContent);
      distanceOverlay.setMap(this.map);
    },
    // [@Method] TEST (!추후 삭제)
    sendChatTest() {
      this.chatting = [
        {
          member: {
            memberId: 1,
            content: "100m 남음~",
          },
        },
        {
          member: {
            memberId: 2,
            content: this.testChatContent,
          },
        },
      ];
    },
    // [@Method] TEST (!추후 삭제)
    chageLatLngTest() {
      const testMember = {
        member: {
          memberId: 2,
          memberNickname: "히정",
          memberLatLng: [37.5049, 127.0371], // 역삼 충헌교회
        },
      };
      // const testMember = {
      //   member: {
      //     memberId: 3,
      //     memberNickname: "시카",
      //     memberLatLng: [37.5049, 127.0371], // 역삼 충헌교회
      //   },
      // };

      // console.log(
      //     "#21# 저장한 marker 확인: ",
      //     this.memberMarkerList[0][1].getImage()
      //   );

      // member 위치 업데이트
      this.updateMemberLocation(testMember);
    },
  },
};
</script>

<style>
/* #map {
  width: 100%;
  height: 100%;
} */
#map {
  width: 100%;
  height: 700px;
}

/* member nickname 오버레이 */
.member-overlay {
  text-shadow: -1.2px -1.2px 0 var(--main-col-1),
    1.2px -1.2px 0 var(--main-col-1), -1.2px 1.2px 0 var(--main-col-1),
    1.2px 1.2px 0 var(--main-col-1);
  padding: 5px;
  color: white;
  text-align: center;
  font-size: 16px;
}

/* 모임장소와의 거리 오버레이 */
.distance-overlay {
  padding: 5px;
  border-radius: 5px;
  color: var(--main-col-1);
  font-weight: bold;
  text-align: center;
  font-size: 16px;
}

/* member chatting 오버레이 */
.chat-overlay {
  position: relative;
  background-color: #ffffff;
  padding: 8px;
  border-radius: 10px;
  color: var(--main-col-1);
  font-weight: bold;
  text-align: center;
  font-size: 15px;
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.35);
}

.chat-overlay::before,
.chat-overlay::after {
  position: absolute;
  display: block;
  content: "";
  border-color: transparent;
  border-style: solid;
}

.chat-overlay::before {
  border-width: 10px;
  border-top-color: #ffffff;
  bottom: -18px;
  left: 50%;
  margin-left: -10px;
}

.chat-overlay::after {
  border-width: 8px;
  border-top-color: #ffffff;
  bottom: -16px;
  left: 50%;
  margin-left: -8px;
}
</style>
