import axios from "axios";

function apiInstance() {
  const instance = axios.create({
    baseURL: `${process.env.VUE_APP_API_BASE_URL}`,
    withCredentials: true,
    headers: {
      "Content-Type": "application/json; charset=utf-8",
    },
  });

  instance.interceptors.request.use(
    function (config) {
      const Authorization = localStorage.getItem("Authorization");
      if (Authorization) {
        config.headers.Authorization = Authorization;
      }
      return config;
    },
    function (error) {
      return Promise.reject(error);
    }
  );

  instance.interceptors.response.use(
    function (response) {
      // console.log(response);
      return response;
    },
    async function (error) {
      console.log(error);
      var result = null;
      // 권한 오류인 경우, access_token 재발급 시도
      if (error.response.data.status == 401) {
        // access token 발급 시도
        await axios
          .create({
            baseURL: `${process.env.VUE_APP_API_BASE_URL}`,
            withCredentials: true,
            headers: {
              "Content-Type": "application/json; charset=utf-8",
              Authorization: localStorage.getItem("Authorization"),
            },
          })
          .post(`/token/tokenReissue`)
          .then(async (response) => {
            // access token 재발급 성공
            localStorage.setItem("Authorization", response.data);

            console.log("401 error ", error.config);
            if (error.config.url === "/meeting-calculate/receipt") {
              console.log("영수증 error");
              error.config.headers["Content-Type"] = "multipart/form-data";
            }
            result = await instance(error.config);
          })
          .catch(async (error) => {
            console.log(error);
            const data = error.response.data;
            // access token 재발급 불가 또는 존재하지 않는 회원인 경우
            if (
              data.status == 500 ||
              (data.status == 404 && data.message == "member not found.")
            ) {
              localStorage.clear();
              // window.location.href = "/login";
            } else {
              result = await Promise.reject(error);
            }
          });
      } else {
        // 그 외의 경우는 오류 그대로 전달
        result = await Promise.reject(error);
      }
      return result;
    }
  );

  return instance;
}

export { apiInstance };
