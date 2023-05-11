import axios from "axios";
// import router from '@/router';

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
      // const errorAPI = error.config;
      // console.log("#[interceptor]# error 확인: ", error);

      // i) access_token 재발급 시도

      // 권한 오류인 경우
      if (error.response.data.status == 401) {

        // 1. access token 발급 시도
        // if (errorAPI.retry == undefined) {
        // errorAPI.retry = true;
        await axios
          .post(`/token/tokenReissue`, {
            baseURL: `${process.env.VUE_APP_API_BASE_URL}`,
            withCredentials: true,
            headers: {
              "Content-Type": "application/json; charset=utf-8",
            },
          })
          .then((response) => {
            // console.log("#[access_token 재발급]# 성공 response: ", response);
            localStorage.setItem("Authorization", response.data);
          })
          .catch((error) => {
            error;
            // console.log("#[access_token 재발급]# 실패 error: ", error);
            localStorage.clear();
            // router.push(`/login`);
            window.location.href = "/login";
          });
        // }
        // else if (errorAPI.retry) {
        //   localStorage.clear();
        //   // window.location.href = "/login";
        //   router.push(`/login`)
        // }

        // return await instance(errorAPI);
      }

      // ii) refresh_token 만료 > 재로그인 권유
      // else {
      // console.log("#[refresh_token]# 만료 > 재로그인 권유");
      // localStorage.clear();
      // window.location.href = "/login";
      // }

      return Promise.reject(error);
    }
  );

  return instance;
}

export { apiInstance };
