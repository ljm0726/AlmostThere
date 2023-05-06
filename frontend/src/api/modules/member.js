import { apiInstance } from "../index";

const api = apiInstance();

// [GET] my-page 필요한 회원정보 모두 조회
async function getMemberInfo(success, fail) {
  await api.get(`/member/info`).then(success).catch(fail);
}

// [POST] room-id, member에 따른 출발장소 저장
async function saveMemberStartPlace(meetingId, success, fail) {
  await api
    .post(`/member/save/start-place`, { meetingId: meetingId })
    .then(success)
    .catch(fail);
}

export { getMemberInfo, saveMemberStartPlace };
