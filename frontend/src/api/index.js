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

  // const api = instance;

  instance.interceptors.request.use(
    function (config) {
      // Do something before request is sent
      const Authorization = localStorage.getItem("Authorization");
      if (Authorization) {
        console.log(
          "#[interceptor_request]# Authorization 추가: ",
          Authorization
        );
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
      const errorAPI = error.config;

      // i) access_token 재발급
      if (!isRefreshFlag) {
        if (
          error.response.data.status === 401 &&
          errorAPI.retry === undefined
        ) {
          // console.log("#[interceptor_response]# access_token 재발급");
          errorAPI.retry = true;
          isRefreshFlag = true;

          await instance
            .post(`/token/tokenReissue`)
            .then((response) => {
              // console.log("#[access_token 재발급]# 성공 response: ", response);
              localStorage.setItem("Authorization", response.data);
            })
            .catch((error) => {
              console.log("#[access_token 재발급]# 실패 error: ", error);
            });

          return await instance(errorAPI);
        }
        // ii) 재로그인 권유
        else {
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
