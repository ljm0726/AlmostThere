import axios from "axios";

function apiInstance() {
  const instance = axios.create({
    baseURL: `${process.env.VUE_APP_API_BASE_URL}`,
    headers: {
      "Content-Type": "application/json; charset=utf-8",
      Authorization: localStorage.getItem("Authorization"),
    },
  });

  return instance;
}

export { apiInstance };
