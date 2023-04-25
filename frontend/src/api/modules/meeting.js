import { apiInstance } from "./index.js";

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
          Authorization: "Bearer " + localStorage.getItem("Authorization"),
        },
      }
    )
    .then(success)
    .catch(fail);
}

export { meetingRegister };
