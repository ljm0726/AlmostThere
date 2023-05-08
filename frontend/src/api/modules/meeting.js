import { apiInstance } from "../index";

const api = apiInstance();

async function meetingRegister(
  id,
  name,
  date_time,
  place,
  address,
  lat,
  lng,
  success,
  fail
) {
  await api
    .post(`/meeting`, {
      hostId: id,
      meetingName: name,
      meetingTime: date_time,
      meetingPlace: place,
      meetingAddress: address,
      meetingLat: lat,
      meetingLng: lng,
      roomCode: "",
    })
    .then(success)
    .catch(fail);
}
async function modifyMeeting(
  id,
  name,
  date_time,
  place,
  address,
  lat,
  lng,
  success,
  fail
) {
  await api
    .post(`/meeting`, {
      hostId: id,
      meetingName: name,
      meetingTime: date_time,
      meetingPlace: place,
      meetingAddress: address,
      meetingLat: lat,
      meetingLng: lng,
    })
    .then(success)
    .catch(fail);
}
async function getMeeting(meetingId) {
  var result = null;
  await api
    .post("/meeting/detail", { memberId: 1, meetingId: meetingId })
    .then((res) => {
      result = res.data.data;
      console.log("result ", result);
    });
  return await Promise.resolve(result);
}

async function getTodayMeetings() {
  var result = null;
  await api.get("/meeting/today").then((res) => {
    result = res.data.data;
  });
  return await Promise.resolve(result);
}

async function getUpcomingMeetings() {
  var result = null;
  await api.get("/meeting/upcoming").then((res) => {
    result = res.data.data;
  });
  return await Promise.resolve(result);
}

function getMostRecentMeeting() {
  return new Promise((resolve, reject) => {
    api
      .get("/meeting/most-recent", {
        headers: {
          Authorization: localStorage.getItem("Authorization"),
        },
      })
      .then((response) => {
        resolve(response.data.data);
      })
      .catch((error) => {
        reject(error);
      });
  });
}

async function getBestMember() {
  var result = null;
  await api.get("/member/best-member").then((res) => {
    result = res.data.data;
  });
  return await Promise.resolve(result);
}

async function postReceiptInfo(receipt) {
  var result = null;

  var formData = new FormData();
  formData.append("receipt", receipt);

  await api
    .post(`/meeting-calculate/receipt`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then(async (res) => {
      if (res.data.statusCode == 200) {
        result = res.data.data;
      }
    })
    .catch();
  return await Promise.resolve(result);
}

async function saveCalculateDetail(meetingId, receipt, storeName, totalPrice) {
  var result = false;
  var formData = new FormData();
  console.log("미팅ID: " + meetingId);
  formData.append("meetingId", meetingId);
  formData.append("receipt", receipt);
  formData.append("storeName", storeName);
  formData.append("price", totalPrice);
  await api
    .post(`/meeting-calculate/detail`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then(async () => {
      result = true;
    });
  return await Promise.resolve(result);
}

async function deleteCalculateDetail(calculateDetailId) {
  var result = false;
  await api
    .delete(`/meeting-calculate/${calculateDetailId}`, {})
    .then(async () => {
      result = true;
    });
  return await Promise.resolve(result);
}

// [POST] room-id, member에 따른 출발장소 저장
async function saveMemberStartPlace(startPlaceInfo) {
  var result = null;

  await api
    .post(`/meeting/start-place`, JSON.stringify(startPlaceInfo))
    .then((res) => {
      console.log("#[meeting]# api - response 확인: ", res);
      result = res;
    })
    .catch((error) => {
      console.log("#[meeting]# 출발장소 저장 error: ", error);
    });

  return await Promise.resolve(result);
}

export {
  meetingRegister,
  getMeeting,
  getTodayMeetings,
  getUpcomingMeetings,
  getMostRecentMeeting,
  getBestMember,
  postReceiptInfo,
  saveCalculateDetail,
  deleteCalculateDetail,
  modifyMeeting,
  saveMemberStartPlace,
};
