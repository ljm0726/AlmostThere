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
            },
          })
          .post(`/token/tokenReissue`)
          .then(async (response) => {
            // access token 재발급 성공
            localStorage.setItem("Authorization", response.data);
            result = await instance(error.config);
          })
          .catch((error) => {
            error;
            localStorage.clear();
            window.location.href = "/login";
            return true;
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