import { apiInstance } from "@/api/index.js";
// api instance 가져오기
const api = apiInstance();

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

async function getBestMember() {
  var result = null;
  await api.get("/member/best-member").then((res) => {
    result = res.data.data;
    console.log(result);
  });
  return await Promise.resolve(result);
}

export { getTodayMeetings, getUpcomingMeetings, getBestMember };
