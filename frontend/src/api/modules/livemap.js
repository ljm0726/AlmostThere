import { apiInstance } from "../index";

const api = apiInstance();

async function arriveDestination(meetingId, x, y) {
  await api.post(`/map/arrive/${meetingId}`, {}, { params: { x: x, y: y } })
    .then((res) => {
      console.log(res);
      if (res.data.statusCode == 200) {
        console.log("성공");
      } else {
        // 실패 현재 위치가 도착 지점이 아니다

      }
    })
    .catch((err) => {
      console.log(err)
    });
}

export { arriveDestination };