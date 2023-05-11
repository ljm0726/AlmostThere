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

  // let isRefreshFlag = false;
  instance.interceptors.response.use(
    function (response) {
      console.log(response);
      return response;
    },
    async function (error) {
      const errorAPI = error.config;
      // console.log("#[interceptor]# error 확인: ", error);

      // i) access_token 재발급
      if (error.response.data.status == 401 && errorAPI.retry == undefined) {
        errorAPI.retry = true;

        await instance
          .post(`/token/tokenReissue`)
          .then((response) => {
            // console.log("#[access_token 재발급]# 성공 response: ", response);
            localStorage.setItem("Authorization", response.data);
          })
          .catch((error) => {
            console.log("#[access_token 재발급]# 실패 error: ", error);
            localStorage.clear();
            window.location.href = "/login";
          });

        return await instance(errorAPI);
      }
      // ii) refresh_token 만료 > 재로그인 권유
      else {
        // console.log("#[refresh_token]# 만료 > 재로그인 권유");
        localStorage.clear();
        window.location.href = "/login";
      }

      return Promise.reject(error);
    }
  );

  return instance;
}

export { apiInstance };
