<template>
  <div id="container" class="container">
    <h1>채팅</h1>
    <input type="hidden" id="sessionId" value="" />

    <div id="chating" class="chating"></div>

    <div id="yourName">
      <table class="inputTable">
        <tr>
          <th>사용자명</th>
          <th><input type="text" name="userName" id="userName" /></th>
          <th><button @click="chatName()" id="startBtn">이름 등록</button></th>
        </tr>
      </table>
    </div>
    <div id="yourMsg">
      <table class="inputTable">
        <tr>
          <th>메시지</th>
          <th>
            <input id="chatting" placeholder="보내실 메시지를 입력하세요." />
          </th>
          <th><button @click="send()" id="sendBtn">보내기</button></th>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChattingPage",
  data() {
    return {
      ws: null,
    };
  },
  methods: {
    wsOpen() {
      this.ws = new WebSocket("ws://localhost:9999/chat");
      this.wsEvt();
    },
    wsEvt() {
      this.ws.onopen = function (data) {
        // 소켓이 열리면 동작
        data;
      };

      this.ws.ommessage = function (data) {
        // 메시지를 받으면 동작
        var msg = data.data;
        if (msg != null && msg.trim() != "") {
          var d = JSON.parse(msg);
          if (d.type == "getId") {
            var si = d.sessionId != null ? d.sessionId : "";
            if (si != "") {
              document.getElementById("sessionId").value = si;
            }
          } else if (d.type == "message") {
            if (d.sessionId == document.getElementById("sessionId").value) {
              document
                .getElementById("chating")
                .insertAdjacentHTML("<p class='me'>나 :" + d.msg + "</p>");
            } else {
              document
                .getElementById("chating")
                .insertAdjacentHTML(
                  "<p class='others'>" + d.userName + " :" + d.msg + "</p>"
                );
            }
          } else {
            console.warn("unknown type!");
          }
        }
      };

      document.addEventListener("keypress", function (e) {
        if (e.keyCode == 13) {
          //enter press
          this.send();
        }
      });
    },
    chatName() {
      var userName = document.getElementById("userName").value;
      if (userName == null || userName.trim() == "") {
        alert("사용자 이름을 입력해주세요.");
        document.getElementById("userName").focus();
      } else {
        this.wsOpen();
        // document.getElementById("chatting").style.display = "none";
        document.getElementById("yourName").style.display = "none";
        document.getElementById("yourMsg").style.display = "block";
      }
    },
    send() {
      var option = {
        type: "message",
        sessionId: document.getElementById("sessionId").value,
        userName: document.getElementById("userName").value,
        msg: document.getElementById("chatting").value,
      };
      this.ws.send(JSON.stringify(option));
      document.getElementById("chatting").value = "";
    },
  },
  created() {},
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}
.container {
  width: 500px;
  margin: 0 auto;
  padding: 25px;
}
.container h1 {
  text-align: left;
  padding: 5px 5px 5px 15px;
  color: #ffbb00;
  border-left: 3px solid #ffbb00;
  margin-bottom: 20px;
}
.chating {
  background-color: #000;
  width: 500px;
  height: 500px;
  overflow: auto;
}
.chating .me {
  color: #f6f6f6;
  text-align: right;
}
.chating .others {
  color: #ffe400;
  text-align: left;
}
input {
  width: 330px;
  height: 25px;
}
#yourMsg {
  display: none;
}
</style>
