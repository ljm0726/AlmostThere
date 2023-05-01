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
  const Authorization = "Bearer " + localStorage.getItem("Authorization");
  console.log(" at ", id, name, date_time, place, address, lat, lng);
  await api
    .post(
      `/meeting`,
      {
        hostId: id,
        meetingName: name,
        meetingTime: date_time,
        meetingPlace: place,
        meetingAddress: address,
        meetingLat: lat,
        meetingLng: lng,
      },
      {
        headers: {
          Authorization: Authorization,
        },
      }
    )
    .then(success)
    .catch(fail);
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

async function getcntMeetingsWithin3hours() {
  var result = null;
  await api.get("/meeting/3hours").then((res) => {
    result = res.data.data;
  });
  return await Promise.resolve(result);
}

async function getBestMember() {
  var result = null;
  await api.get("/member/best-member").then((res) => {
    result = res.data.data;
    console.log(result);
  });
  return await Promise.resolve(result);
}

export {
  meetingRegister,
  getTodayMeetings,
  getUpcomingMeetings,
  getcntMeetingsWithin3hours,
  getBestMember,
};
