import axios from "axios";

function apiInstance() {
  // const Authorization = localStorage.getItem("Authorization");
  const instance = axios.create({
    baseURL: `${process.env.VUE_APP_API_BASE_URL}`,
    withCredentials: true,
    headers: {
      "Content-Type": "application/json; charset=utf-8",
    },
  });

  const api = instance;

  instance.interceptors.request.use(
    function (config) {
      // Do something before request is sent
      const Authorization = localStorage.getItem("Authorization");
      if (Authorization) {
        config.headers.Authorization = Authorization;
      }
      return config;
    },
    function (error) {
      // Do something with request error
      return Promise.reject(error);
    }
  );

  let isRefreshFlag = false;

  instance.interceptors.response.use(
    function (response) {
      console.log(response);
      return response;
    },
    async function (error) {
      // Any status codes that falls outside the range of 2xx cause this function to trigger
      // Do something with response error

      const errorAPI = error.config;
      if (!isRefreshFlag) {
        console.log("에러일 경우", error.response.data);
        if (
          error.response.data.status === 401 &&
          errorAPI.retry === undefined
        ) {
          errorAPI.retry = true;
          isRefreshFlag = true;
          console.log("토큰이 이상한 오류일 경우");

          await api
            .post(`${process.env.VUE_APP_API_BASE_URL}` + "/token/tokenReissue")
            .then((response) => {
              console.log(response);
              localStorage.setItem("Authorization", response.data);

              console.log(errorAPI);
            })
            .catch((error) => {
              console("무슨에러?", error);
            });

          return await axios(errorAPI);
        } else {
          console.log("#interceptor# 로그인 페이지로 이동");
          // this.$router.push(name: "/login");
        }
      }
      return Promise.reject(error);
    }
  );

  return instance;
}

export { apiInstance };
