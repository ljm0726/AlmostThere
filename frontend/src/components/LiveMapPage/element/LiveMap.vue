<template>
  <div>
    <div id="map"></div>
  </div>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { mapState } from "vuex";

export default {
  name: "LiveMap",
  data() {
    return {
      /* # memberId, meetingId */
      memberId: null,
      meetingId: this.$route.params.id,
      /* # marker 설정 */
      markerType: 0, // marker 이미지 순서
      placeMarkerSize: [50, 70], // 모임장소 marker 크기
      memberMarkerSize: [120, 120], // member marker 크기
      placeMarkerOption: [25, 65], // 모임장소 - image marker 위치 (좌표 X)
      memberMarkerOption: [60, 85], // member - image marker 위치
      /* # over-lay 설정 */
      memberOverlay: [0.5, 3.2], // member nickname over-lay (x, y) 위치 좌표
      distanceOverlay: [-0.4, -0.1], // distance over-lay 좌표
      chatOverlay: [0.5, 4], // chat over-lay 좌표
      warningOverlay: [6.0, 3.5], // GPS 수신불가 over-lay 좌표
      /* # 좌표 */
      placeLatLng: [], // 모임장소 좌표
      memberLocation: [], // 사용자들의 좌표 (memberId, memberNickname, LatLng)
      /* # member 채팅 */
      chatting: {}, // 멤버 별 실시간 chatting 내용
      /* # 생성한 marker, overlay 저장 (memberId 기준) */
      memberMarkerList: [], // marker
      memberChatOverlayList: [], // 채팅 over-lay
      memberDistanceOverlayList: [], // 거리(m) over-lay
      memberNicknameOverlayList: [], // nickname over-lay
      memberPolylineList: [], // polyline
      circleList: [], // 모임장소 반경 circle
      geoWarningOverlayList: [], // GeoLocation 경고 over-lay
      /* # 위치 변경된 member[index, id] */
      updateMemberInfo: [], // [index, id] 위치 업데이트 된 member의 > memberLocation index와 memberId 저장
      /* # 가장 먼 곳에 있는 memeber distance */
      maxMemberDistance: 2500,
    };
  },
  computed: {
    ...mapState("memberStore", ["member"]),
    ...mapState("meetingStore", ["meeting_lat", "meeting_lng"]),
  },
  watch: {
    chatting: {
      handler(val, oldVal) {
        alert("여기닷", val, oldVal);
        this.updateChatOverlay(this.chatting);
      },
      deep: true,
    },
  },
  mounted() {
    // i) memberId 저장
    this.memberId = this.member.id;
    // ii) 모임장소 좌표 저장
    this.placeLatLng.push(this.meeting_lat);
    this.placeLatLng.push(this.meeting_lng);
    // iii) Kakao Map Script import
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
    // initMap() {
    //   const container = document.getElementById("map");
    //   const options = {
    //     center: new kakao.maps.LatLng(this.placeLatLng[0], this.placeLatLng[1]),
    //     level: 4,
    //   };

    //   // 지도 객체 등록
    //   this.map = new kakao.maps.Map(container, options);

    //   // 초기 marker 생성
    //   // i) 모임 장소 marker
    //   this.createPlaceMarker(options);
    //   // ii) 멤버 별 marker 생성
    //   if (this.memberLocation.length != 0) this.createMemberMarker();

    //   // WebSocket 연결
    //   this.connect();
    // },
    // [@Method] Kakao Map 생성 + 배경화면 설정
    initMap() {
      const plan = (x, y, z) => {
        y = -y - 1;
        const limit = Math.ceil(3 / Math.pow(2, z));
        if (0 <= y && y < limit && 0 <= x && x < limit) {
          return "https://i1.daumcdn.net/dmaps/apis/white.png";
        } else {
          return "https://i1.daumcdn.net/dmaps/apis/white.png";
        }
      };
      kakao.maps.Tileset.add(
        "PLAN",
        new kakao.maps.Tileset(512, 512, plan, "", false, 0, 19) // 0, 19으로 level 범위
      );

      const container = document.getElementById("map");
      const options = {
        projectionId: null,
        mapTypeId: kakao.maps.MapTypeId.PLAN,
        $scale: false,
        center: new kakao.maps.LatLng(this.placeLatLng[0], this.placeLatLng[1]),
        level: 4,
      };
      // 지도 객체 등록
      this.map = new kakao.maps.Map(container, options);

      // 모임장소로 부터 원 표시
      this.createCircle();

      // 모임 장소 marker 생성
      this.createPlaceMarker(options);

      // WebSocket 연결
      this.connect();
    },
    // [@Method] 모임장소로 부터 원 표시
    createCircle() {
      this.setInitValue(); // 원 범위 초기화

      // 가장 먼 곳에 있는 member를 기준으로 (unit)m 단위 circle 생성
      for (let i = 0; i < this.memberDistanceOverlayList.length; i++) {
        const memberId = Object.keys(this.memberDistanceOverlayList[i])[0];
        const overlay = this.memberDistanceOverlayList[i][memberId];

        // distance 추출
        const contentEl = overlay.getContent();
        const distanceStr = contentEl.replace(/[^\d]/g, "");
        const distance = parseInt(distanceStr);

        this.maxMemberDistance = Math.max(this.maxMemberDistance, distance);
      }
      const unit = 500; // m 단위
      const maxRadius = Math.ceil(this.maxMemberDistance / unit) * unit; // 반지름

      // 기존에 있던 circle 삭제
      if (this.circleList.length != 0) {
        for (const circle of this.circleList) {
          circle.setMap(null);
        }
        this.circleList = [];
      }

      // circle 생성
      for (let radius = unit; radius <= maxRadius; radius += unit) {
        const circle = new kakao.maps.Circle({
          center: new kakao.maps.LatLng(
            this.placeLatLng[0],
            this.placeLatLng[1]
          ),
          radius: radius, // m 단위의 원 반지름
          strokeWeight: 0.5, // 선의 두께
          strokeColor: "var(--main-col-2)",
          strokeOpacity: 1, // 선 불투명도
          strokeStyle: "solid", // 선 스타일
        });

        // circle 표시
        circle.setMap(this.map);
        // circle 저장 (for. 삭제)
        this.circleList.push(circle);
      }
    },
    // [@Method] 초기값 설정
    setInitValue() {
      this.maxMemberDistance = 2500; // circle를 생성하기 위한 maxMemberDistance 기본값
    },
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
    // [@Method] WebSocket 연결
    connect() {
      const serverURL = `${process.env.VUE_APP_API_BASE_URL}/websocket`;
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);

      // console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          this.isConnect = true;
          console.log("#[실시간 지도]# 소켓 연결 성공", frame);

          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          this.stompClient.subscribe(`/topic/${this.memberId}`, (res) => {
            // console.log("구독으로 받은 메시지 입니다.", res.body);

            // socket을 통해 받은 message(사용자 좌표) 저장
            this.saveMembersLocation(JSON.parse(res.body));
          });

          // GeoLocation - 1초마다 현 위치 얻기
          this.startIntervalMemberLocation();
          this.subscribeChatting();
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
        }
      );
    },
    subscribeChatting() {
      this.stompClient.subscribe(
        `/send/${this.$route.params.id}`,
        async (res) => {
          const data = await JSON.parse(res.body);
          // console.log(
          //   "chaa>>>> " + data.data.memberId + " " + data.data.message
          // );
          if (data.statusCode == 200) {
            // console.log("dddddddddd", String(data.data.memberId));
            this.chatting[String(data.data.memberId)] = data.data.message;
            this.updateChatOverlay();
          }
        },
        { id: `chatting-subscribe-${this.$route.params.id}` }
      );
    },
    // [@Method] 1초마다 해당 모임에 member 객체(좌표) 얻기
    startIntervalMemberLocation() {
      setInterval(() => {
        this.send();
      }, 1000);
    },
    // [@Method] client에서 server로 message 보내기(send) - 해당 모임의 member 객체 얻기
    send() {
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send(
          `/message/locShare/meetingId/${this.meetingId}/memberId/${this.memberId}`,
          {}
        );
      }
    },
    // [@Method] socket을 통해 받은 message(사용자 좌표) 저장
    saveMembersLocation(receivedMemberLocations) {
      // console.log(
      //   "#21# socket을 통해 받은 사용자 정보 state에 저장 ",
      //   membersLocation
      // );
      if (receivedMemberLocations.length == 0) return;

      for (const member of receivedMemberLocations) {
        if (member == null) continue;

        // memberId를 통해 해당 member 찾기
        let memberIndex = -1;
        if (this.memberLocation != 0) {
          for (let i = 0; i < this.memberLocation.length; i++) {
            if (this.memberLocation[i].memberId == member.memberId) {
              memberIndex = i;
              break;
            }
          }
        }

        if (memberIndex == -1) {
          // console.log(
          //   "#21# 현 memberLocaion에 저장되어 있지 않은 새로운 member 확인",
          //   member
          // );
          this.memberLocation.push(member);

          // 멤버 별 marker 생성
          this.createMemberMarker(member);
        } else {
          // console.log(
          //   "#21# 이미 저장되어 있는 member 위치 값 update: ",
          //   this.memberLocation[memberIndex]
          // );
          // 위치 값 update
          this.memberLocation[memberIndex].memberLatLng = member.memberLatLng;

          // 변경된 member [index, id] 저장
          this.updateMemberInfo = [
            memberIndex, // memberLocation의 index값
            this.memberLocation[memberIndex].memberId, // memberId 값
          ];

          // marker, polyline, overlay 재조정
          this.refreshMapOnLocationUpdate();
        }
      }
    },
    // [@Method] 멤버 별 캐릭터 marker 생성
    createMemberMarker(member) {
      // console.log("#21# member 캐릭터 marker 생성 ", member);

      // i) marker option 설정
      const imageSrc = require(`@/assets/images/animals/${this.markerType}.png`);
      if (this.markerType == 10) this.markerType = 0;
      else this.markerType += 1;

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
        member.memberLatLng[0],
        member.memberLatLng[1]
      );
      // ii) marker 생성
      const marker = new kakao.maps.Marker({
        position: location,
        image: markerImage,
      });
      if (member.memberId == this.memberId) marker.setZIndex(9999);

      // marker 저장 (for. 삭제)
      const object = new Object();
      object[member.memberId] = marker;
      this.memberMarkerList.push(object);

      // marker 표시
      marker.setMap(this.map);

      // iii) 멤버 별 over-lay, polyline 생성 (닉네임, 모임장소와의 거리)
      this.createMemberOverlay(member, marker);
      this.createDistance(member, marker);

      // iv) map rebound
      this.resizeMapLevel();
    },
    // [@Method] member 별 닉네임 over-lay 생성
    createMemberOverlay(member, marker) {
      // console.log("#21# member 닉네임 over-lay 생성 ", member);

      const content = `<div class="member-overlay point-font">${member.memberNickName}</div>`;
      const position = marker.getPosition();

      // over-lay 생성
      var customOverlay = null;
      if (member.memberId == this.meetingId) {
        customOverlay = new kakao.maps.CustomOverlay({
          position: position,
          content: content,
          xAnchor: this.memberOverlay[0], // 오버레이 표시 x, y 위치
          yAnchor: this.memberOverlay[1],
          style: {
            zIndex: 1000,
          },
        });
      } else {
        customOverlay = new kakao.maps.CustomOverlay({
          position: position,
          content: content,
          xAnchor: this.memberOverlay[0],
          yAnchor: this.memberOverlay[1],
        });
      }

      // over-lay 저장 (for. 삭제)
      const object = new Object();
      object[member.memberId] = customOverlay;
      this.memberNicknameOverlayList.push(object);

      // 오버레이 표시
      customOverlay.setMap(this.map);
    },
    // [@Method] 모임장소와의 거리 계산 및 표시
    createDistance(member, marker) {
      // console.log("#21# member 선(polyline) 생성 ", member);

      // i) 선을 그릴 좌표 setting [모임장소 좌표, 현재 member 좌표]
      const distancePath = [
        new kakao.maps.LatLng(this.placeLatLng[0], this.placeLatLng[1]),
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
        zIndex: 9999,
      });

      // polyline 저장 (for. 삭제)
      const object = new Object();
      object[member.memberId] = polyline;
      this.memberPolylineList.push(object);

      // 선 표시
      polyline.setMap(this.map);

      // 두 좌표의 거리 over-lay 표시
      const distance = Math.round(polyline.getLength());
      this.createDistanceOverlay(distance, marker, member);
    },
    // [@Method] member와 모임장소 거리 - 오버레이 표시
    createDistanceOverlay(distance, marker, member) {
      // console.log("#21# member 거리 over-lay 생성 ", member);

      const content = `<div class="distance-overlay logo-font">${distance.toLocaleString(
        "ko-KR"
      )}m</div>`;
      const position = marker.getPosition();

      // over-lay 생성
      var customOverlay = null;
      if (member.memberId == this.meetingId) {
        customOverlay = new kakao.maps.CustomOverlay({
          position: position,
          content: content,
          xAnchor: this.distanceOverlay[0],
          yAnchor: this.distanceOverlay[1],
          style: {
            zIndex: 1000,
          },
        });
      } else {
        customOverlay = new kakao.maps.CustomOverlay({
          position: position,
          content: content,
          xAnchor: this.distanceOverlay[0],
          yAnchor: this.distanceOverlay[1],
        });
      }

      // over-lay 저장 (for. 삭제)
      const object = new Object();
      object[member.memberId] = customOverlay;
      this.memberDistanceOverlayList.push(object);

      // over-lay 표시
      customOverlay.setMap(this.map);
    },
    // [@Method] member의 위치 값 변경에 따른 marker, polyline, overlay 업데이트
    refreshMapOnLocationUpdate() {
      // console.log("#21# member 위치 update에 따른 marker, over-lay 등 update");
      const refreshMember = this.memberLocation[this.updateMemberInfo[0]];
      // this.updateMemberInfo[0] = 변경된 memberLocation 배열의 index 값
      // this.updateMemberInfo[1] = 변경된 memberId
      // console.log("#21# refresh member 확인: ", refreshMember);

      const newPosition = new kakao.maps.LatLng(
        refreshMember.memberLatLng[0],
        refreshMember.memberLatLng[1]
      );

      // i) marker
      const markerIndex = this.memberMarkerList.findIndex(
        (obj) => Object.keys(obj)[0] == this.updateMemberInfo[1]
      );
      // 기존 marker
      const marker =
        this.memberMarkerList[markerIndex][this.updateMemberInfo[1]];
      // 위치를 변경한 new marker
      const newMarker =
        this.memberMarkerList[markerIndex][this.updateMemberInfo[1]];
      newMarker.setPosition(newPosition);
      marker.setMap(null);
      newMarker.setMap(this.map);

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
      const distance = Math.round(polyline.getLength());
      const newContent = `<div class="distance-overlay logo-font">${distance.toLocaleString(
        "ko-KR"
      )}m</div>`;
      const distanceOverlay =
        this.memberDistanceOverlayList[distanceIndex][this.updateMemberInfo[1]];
      distanceOverlay.setPosition(newPosition);
      distanceOverlay.setContent(newContent);
      distanceOverlay.setMap(this.map);
      // - 채팅 over-lay
      if (this.memberChatOverlayList.length != 0) {
        const chatIndex = this.memberChatOverlayList.findIndex(
          (obj) => Object.keys(obj)[0] == this.updateMemberInfo[1]
        );
        const chatOverlay =
          this.memberChatOverlayList[chatIndex][this.updateMemberInfo[1]];
        chatOverlay.setPosition(newPosition);
        chatOverlay.setMap(this.map);
      }

      // iii) circle
      this.createCircle();
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
    // [@Method] GeoLocation 위치 부정확 over-lay 표시
    showLocationUnavailableOverlay(memberId) {
      // console.log(
      //   "#21# GeoLocation 부정확 over-lay 생성 - memberId: ",
      //   memberId
      // );

      // 현 로그인 사용자의 marker
      const markerIndex = this.memberMarkerList.findIndex(
        (obj) => Object.keys(obj)[0] == memberId
      );
      const marker = this.memberMarkerList[markerIndex][memberId];

      // over-lay 생성
      // const imageUrl = require("@/assets/images/dialog/warning.png");
      // const imageUrl = require("@/assets/images/dialog/x.png");
      // const content = `<div><img src="${imageUrl}" /></div>`;
      const content =
        '<div><i class="fas fa-exclamation-triangle" style="color:red; font-size:20px;">!</i></div>';
      const position = marker.getPosition();
      // const overlaySize = new kakao.maps.Size(100, 100);

      const customOverlay = new kakao.maps.CustomOverlay({
        content: content,
        position: position,
        xAnchor: this.warningOverlay[0], // 오버레이 표시 x, y 위치
        yAnchor: this.warningOverlay[1],
        // size: overlaySize,
      });
      // over-lay 저장 (for. 삭제)
      const object = new Object();
      object[memberId] = customOverlay;
      this.geoWarningOverlayList.push(object);

      // 오버레이 표시
      customOverlay.setMap(this.map);
    },
    // [@Method] GeoLocation 위치 부정확 경고 over-lay 존재 확인 (true - 이미 존재함)
    checkGeoWarningOverlay(memberId) {
      let index = -1;
      for (let i = 0; i < this.geoWarningOverlayList.length; i++) {
        if (Object.keys(this.geoWarningOverlayList[i]) == memberId) {
          index = i;
          break;
        }
      }

      return index;
    },
    // [@Method] chatting 내용 over-lay 표시
    // 여기
    updateChatOverlay() {
      //     {
      //       member: {
      //         memberId: 1,
      //         content: "100m 남음~",
      //       },
      //     },
      for (var key of Object.keys(this.chatting)) {
        console.log("key " + key);
        // chatting 내용이 없는 경우 생성 X
        if (this.chatting[key] == null || this.chatting[key] == "") continue;

        const content = `<div class="chat-overlay point-font">${this.chatting[key]}</div>`;
        const memberMarkerLatLng = this.memberLocation.find(
          (loc) => loc.memberId == key
        ).memberLatLng;
        const position = new kakao.maps.LatLng(
          memberMarkerLatLng[0],
          memberMarkerLatLng[1]
        );

        // over-lay 생성
        const customOverlay = new kakao.maps.CustomOverlay({
          position: position,
          content: content,
          xAnchor: this.chatOverlay[0],
          yAnchor: this.chatOverlay[1],
        });

        // 생성한 오버레이 삭제 후 업데이트 or 저장
        const index = this.memberChatOverlayList.findIndex(
          (obj) => Object.keys(obj)[0] == key
        );
        // i) 해당 member의 chatOverlay가 있는 경우 값 업데이트(삭제, 추가)
        if (index > -1) {
          this.memberChatOverlayList[index][key].setMap(null); // 기존 오버레이 삭제
          this.memberChatOverlayList[index][key] = customOverlay; // 새로운 오버레이 추가
        }
        // ii) 없는 경우, 오버레이 추가
        else {
          const object = new Object();
          object[key] = customOverlay;
          this.memberChatOverlayList.push(object);
        }

        // over-lay 표시
        customOverlay.setMap(this.map);
      }
    },
    destroyed() {
      this.stompClient.unsubscribe(
        `chatting-subscribe-${this.$route.params.id}`
      );
    },
  },
};
</script>

<style>
#map {
  width: 100%;
  height: 100%;
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
