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

export { meetingRegister };
