import { apiInstance } from "../index";

const api = apiInstance();

async function arriveDestination(meetingId, x, y) {
  var result = 0;
  await api.post(`/map/arrive/${meetingId}`, {}, { params: { x: x, y: y } })
    .then((res) => {
      // console.log(res);
      if (res.data.statusCode == 200) {
        // console.log("성공");
        result = 1;
      } else {
        // 실패 현재 위치가 도착 지점이 아니다
        result = 2;
      }
    })
    .catch((err) => {
      // console.log(err.response.data.message);
      const message = err.response.data.message;
      if (message == "meeting timeover.") {
        // 시간 지나서 지각 처리
        result = 3;
      } else {
        // 오류
        result = 4;
      }
    });
  return await Promise.resolve(result)
}

export { arriveDestination };