import { apiInstance } from "../index";

// api instance 가져오기
const api = apiInstance();

async function getChatting(meetingId) {
  var result = null;
  await api.get(`/api/chat/${meetingId}`)
    .then((res) => {
      result = res;
    })

  return await Promise.resolve(result)
}

export { getChatting }