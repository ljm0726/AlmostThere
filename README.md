![Almost There](/docs/image/header.png)

# Almost There

- 공유를 통해 모임을 쉽게 관리하고, 일상에서 재미있게 즐길 수 있는 서비스
- 기간 2023.04.10 ~ 05.19 (6주)
- 삼성청년SW아카데미 SSAFY 8기 2학기 자율 프로젝트
- TEAM 어디야? : 엄희정(팀장), 서지윤, 지강훈, 신도연, 안승태, 이종민
- [Almost There 시연 UCC](https://youtu.be/dL11J09aq0I)

## 목차

1. [기획 배경](#기획-배경)
2. [Almost There 소개](#almost-there-소개)
3. [주요 기능](#주요-기능)
4. [주요 기술](#주요-기술)
5. [팀원 역할](#팀원-역할)
6. [프로젝트 구조](#프로젝트-구조)
7. [산출물](#산출물)
8. [서비스 화면](#서비스-화면)

## 기획 배경

사람 간의 만남은 피할 수 없고, 모임에서는 다양한 문제들이 발생됩니다.

저희는 이러한 문제를 해결하고자 모임을 잡을 때, 만났을 때, 만난 후의 시점에 따른 문제점을 파악하고,
이를 해결하여 만나기 전부터 만난 후까지 편리하고 일상생활에서 재미있게 사용할 수 있는 Almost There 서비스를 기획하였습니다.

## Almost There 소개

Almost There는 4가지의 메인 기능을 통해 사용자가 일상생활에서 모임을 편리하고 재미있게 관리할 수 있도록 서비스를 제공하고 있습니다.

첫번째로, 모임을 만들고 카카오톡 메신저를 통해 친구를 모임에 초대할 수 있습니다. 모임에 대한 정보는 초대된 모든 멤버들과 공유되기 때문에 모임 정보를 손쉽게 확인할 수 있습니다.

두번째, 여러 명의 멤버가 있을 때 멤버 간의 중간위치를 토대로 4가지 카테고리(카페, 지하철역, 문화시설, 음식점)의 중간장소를 추천하여 모임장소 선정에 도움을 주고 있습니다.

세번째, 모임 당일 실시간 그룹채팅과 서로의 현 위치를 표시하는 위치공유 지도를 통해 서로의 현 위치를 확인하고 모임장소까지의 남은 거리와 지각여부를 확인할 수 있습니다.

네번째, 영수증 이미지를 통해 자동으로 멤버들과 N/1 계산하는 정산 기능을 제공하고 있습니다. 만약, 지각비를 설정하였다면 지각한 구성원 정산 내역에는 지각비가 반영됩니다. 계산된 정산 내역을 카카오톡 메신저를 통해 공유할 수도 있습니다.

## 주요 기능

### 로그인

- **Kakao** 소셜 로그인 제공
- Spring Security, JWT token, OAuth를 통한 인증, 인가 적용

### 메인 페이지

- **오늘의 모임** 오늘(24H) 이내 잡혀있는 모임 조회
- **다가올 모임** 한달 내 예정되어 있는 모임 조회
- **자주 만나는 친구** 자주 만나는 친구 상위 9명 조회
- **사용 설명서** Banner 내 별도의 서비스 이용 안내 페이지 Link 제공

### 모임 생성페이지

- **모임 생성**
  - 모임 이름, 일시, 장소를 입력하여 모임 생성 가능
  - 모임을 생성한 사용자에게 방장 역할 부여
- **중간장소 추천**
  - 모임장소 선정 시 선택한 장소에 따른 중간위치 판별
  - 판별된 중간위치를 기준으로 반경 500m 내 4가지의 카테고리 별 3개의 모임장소 추천
    - 카테고리 [지하철역, 음식점, 카페, 문화시설]
  - 선정한 모임장소와 멤버 출발장소와의 대중교통/자동차 경로 및 소요시간 표시

### 모임 상세페이지

- **모임 상세**
  - 모임에 대한 정보(이름, 일시, 장소, 나의 출발지, 모임에 참여하는 멤버, 지각비, 정산 내역) 조회 가능
  - 모임에 대한 수정, 삭제, 모임 나가기 기능 제공
  - 모임 시간 전/후 3시간을 기준으로 각 멤버의 실시간 위치공유 가능
  - 모임에 참여한 멤버들간의 실시간 그룹채팅 가능
- **모임 초대**
  - 카카오톡 메신저 & 링크를 통한 모임초대 가능
- **돌림판**
  - 사용자가 원하는 대로 구성하여 사용할 수 있는 돌림판(룰렛) 제공
  - "모임 구성원 추가" 버튼을 통해 모임에 참가한 멤버를 돌림판에 자동 세팅 가능
- **자동 정산**
  - 영수증 이미지 내 텍스트 추출을 통한 각 멤버 별 자동정산 제공(1인 당 내야하는 금액 표시)
    - 소비 금액과 모임 멤버 수에 따른 N/1 정산
    - 지각비가 설정되어 있을 경우, 정산 시 지각한 멤버는 지각비 추가
  - 도출된 정산 내역 결과는 카카오톡 메신저를 통한 공유 기능 제공

### 실시간 그룹 채팅

- 모임에 참여한 멤버 간의 실시간 그룹 채팅 제공
- 각 모임 별 채팅 참여 시 이전 채팅 내용 조회 가능 (무한 스크롤 적용)
- 새로운 메세지에 대한 알림 기능 제공
- 채팅에 참여하고 있는 멤버들의 프로필 조회 가능

### 실시간 위치공유 지도

- **위치 공유**
  - 모임에 참여한 멤버들의 실시간 위치를 지도에 marker 표시하여 현 위치공유 제공
    - 멤버 별 닉네임, 거리(m), 최근 채팅 내용 실시간 반영 및 표시
  - 모임시간을 기준으로 전/후 3시간 동안 사용자의 위치공유 가능
- **배경 선택**
  - 스위칭 버튼을 통해 tile(원 배경)과 Kakao Map(지도) 배경화면으로 스위칭 가능
    - tile: 모임 장소로부터 2,500m 이하는 500m 간격의 원(circle) 2,500m 이상은 10km 간격의 원(circle) 표시
    - map: 카카오맵 지도 배경
- **도착 검증**
  - 모임장소 반경 100m 이내 도달 시 도착완료 버튼을 통해 도착검증 마크 획득 가능
  - 모임시간이 지나도록 미도착 시 자동으로 지각 마크 부여

### 마이페이지

- **사용자 정보**
  - 사용자의 모임 활동이력에 따른 활동 요약 제공
    - 이번달 모임 횟수
    - 총 모임 중 누적 지각 횟수
    - 지난달 소비 금액
- **모임 이력**
  - 캘린더를 통한 전체 모임이력 조회 제공
    - 모임이 있는 일자 하이라이트 표시
  - 일자 선택 시 선택한 일자에 따른 모임 목록 조회 가능 (무한 스크롤 적용)

## 주요 기술

### Backend

- <img src="https://img.shields.io/badge/Spring Boot 2.7.9-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat-square&logo=&logoColor=white"/>
- <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=SpringSecurity&logoColor=white"/> <img src="https://img.shields.io/badge/OAuth-000000?style=flat-square&logo=&logoColor=white"/> <img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=&logoColor=white"/>
- <img src="https://img.shields.io/badge/WebSocket-FF6C37?style=flat-square&logo=WebSocke" />
- <img src="https://img.shields.io/badge/Intellij IDEA-0052CC?style=flat-square&logo=Intellij IDEA&logoColor=white"/>
- <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/>

### Frontend

- <img src="https://img.shields.io/badge/Vue.js 2.6.14-4FC08D?style=flat-square&logo=Vue.js&logoColor=white"/> <img src="https://img.shields.io/badge/Vuex-4FC08D?style=flat-square&logo=&logoColor=white"/>
- <img src="https://img.shields.io/badge/Vuetify 2.6.14-8041D9?style=flat-square&logo=Vuetify&logoColor=white"/>
- <img src="https://img.shields.io/badge/Visual Studio Code-1867C0?style=flat-square&logo=Visual Studio Code&logoColor=white"/>

### Database

- <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=&logoColor=white"/>

### DevOps

- <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat-square&logo=Amazon EC2&logoColor=white"/> <img src="https://img.shields.io/badge/NGINX-009639?style=flat-square&logo=NGINX&logoColor=white"/> <img src="https://img.shields.io/badge/SSL-0054FF?style=flat-square&logo=&logoColor=white"/>
- <img src="https://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>

### Tools

- <img src="https://img.shields.io/badge/GitLab-FC6D26?style=flat-square&logo=GitLab&logoColor=white"/> <img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=Jira Software&logoColor=white"/>
- <img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/>
- <img src="https://img.shields.io/badge/Discord-5865F2?style=flat-square&logo=Discord&logoColor=white"/> <img src="https://img.shields.io/badge/Mattermost-0058CC?style=flat-square&logo=Mattermost&logoColor=white"/>
- <img src="https://img.shields.io/badge/ERDCloud-000000?style=flat-square&logo=ERDCloud&logoColor=white"/> <img src="https://img.shields.io/badge/Cloudcraft-2496ED?style=flat-square&logo=Cloudcraft&logoColor=white"/>

## 팀원 역할

![TEAM](/docs/image/team.png)

## 프로젝트 구조

### ERD

![ERD](/docs/image/erd.png)

### Architecture

![Architecture](/docs/image/architecture.png)

## 산출물

- [와이어프레임](https://www.figma.com/file/xbogDS5zjIhyLq0YZKDrge/Almost-There-Wireframe?type=design&node-id=0-1)
- [기능명세서](https://www.notion.so/e533237a858f4e75a422bab0a1976715?pvs=4)
- [DB 설계서](https://www.erdcloud.com/d/xzbkWY4Q4Z6weTbCW)
- [API 명세서](https://www.notion.so/API-b25423dacdf84a7bbca10ee3980e6894?pvs=4)
- [포팅 매뉴얼](https://lab.ssafy.com/수정필요-/tree/feat/readme/exec)
- [오픈소스 Contributor Guide](https://www.notion.so/Contributor-Guide-4606f48c85104eb48ea0381b31fa2c49?pvs=4)

## 서비스 화면

### Login : 로그인

<img src="/docs/gif/login.gif" width="300"/>

- Almost There 서비스 이용 시 로그인 필요 (필수)
- Kakao 소셜 로그인 제공
- 서비스 접속 또는 서비스 사용 시 로그인 여부를 판단하여 비로그인 시 Login 페이지로 자동 Redirect

### Home : 오늘의 모임, 다가올 모임, 자주 만나는 친구

<img src="/docs/gif/home_page.gif" width="300"/>

- 'Almost There'의 첫 페이지로 오늘의 모임, 다가올 모임, 자주 만나는 친구 표시
- 오늘의 모임은 24시간 내에 잡혀있는 모임 조회
- 다가올 모임은 오늘의 모임을 제외한 한 달내에 예정되어 있는 모임 조회
  - 모임 일자까지의 D-DAY 표시
- 자주 만나는 친구는 친구 별 만난 횟수를 카운트하여 상위 9명을 표시
- 각 모임 별 상세페이지 이동 가능 및 오늘의 모임, 다가올 모임 조회 기준 설명 제공(i)

### Meeting Register : 모임 등록

<img src="/docs/gif/meeting_register.gif" width="300"/>

- 모임의 이름, 날짜, 시간, 장소를 입력하여 모임 등록 가능
- 모임 등록 시 모임 시간은 현재 시간 이후의 시간만 설정 가능
- 모임을 등록한 사용자에게 방장 권한 부여

<img src="/docs/gif/meeting_search_place.gif" width="300"/>

- 모임 장소 선택 시 카카오맵을 통한 검색 및 장소 선택 가능

### Middle Place : 중간장소 추천

<img src="/docs/gif/middle_place.gif" width="300"/>

- 여러 개의 출발장소를 선택 후 각 출발장소 간의 중간위치 판별
- 판별된 중간위치를 기준으로 반경 500m 내 있는 4가지 카테고리 별 3개의 중간 모임장소 추천
  - 카테고리 [지하철역, 음식점, 카페, 문화시설]
- 최대 10개의 출발장소 간의 중간위치 판별 가능
- 추천한 중간 모임장소 marker 클릭 시 대중교통/자동차 경로 및 소요시간 제공

### Meeting Invite : 모임 초대

<img src="/docs/gif/meeting_invite.gif" width="300"/>

- 모임 등록 시 해당 모임의 상세 페이지로 Redirect 및 모임 별 Room Code 생성
- 이를 통한 카카오톡 메신저 모임 초대 및 Link를 통한 모임 초대 가능

### Meeting Room : 모임

<img src="/docs/gif/meeting_detail.gif" width="300"/>

- 모임에 대한 상세정보 조회 가능
  - 모임의 이름, 일시, 시간, 장소, 나의 출발지, 모임에 참가하는 멤버, 지각비, 정산내역
- 모임에 참가하지 않은 멤버는 모임의 상세정보 조회 불가능 (dialog 알림창 표시)
- 멤버 별 권한에 따른 버튼 노출
  - 방장의 경우, "수정, 초대, 삭제" 가능
  - 일반 멤버의 경우, "모임 나가기" 가능

<img src="/docs/gif/meeting_info_modify.gif" width="300"/>

- 모임의 이름, 날짜, 시간, 지각비 수정 가능

<img src="/docs/gif/meeting_place_modify.gif" width="300"/>

- 모임 장소 수정 시 모임에 참여하고 있는 각 멤버의 출발지를 중간장소 찾기에 자동 반영
- 반영된 멤버 별 출발장소에 따른 중간장소 및 대중교통/자동차 경로 추천

<img src="/docs/gif/roulette.gif" width="300"/>

- 사용자가 원하는 대로 구성 가능한 돌림판(룰렛) 제공
- "모임 멤버 추가" 버튼을 통해 모임에 참석한 멤버들의 닉네임을 돌림판에 자동 세팅 가능

### Live Group Chatting : 실시간 그룹채팅

<img src="/docs/gif/live_group_chatting.gif" width="300"/>

- 모임에 참석한 멤버들간의 실시간 그룹채팅 및 이전 채팅 내용(이력) 조회 가능
  - 무한스크롤 적용
  - 채팅 접속 시 채팅 최하단으로 스크롤 자동 조정
- 채팅에 참여하고 있는 멤버들의 프로필 조회 가능
- 새로운 메세지 도착 시 하단의 알림바를 통한 알림 기능 제공

### Live Location Sharing Map : 실시간 위치공유 지도

<img src="/docs/gif/live_sharing_map.gif" width="300"/>

- 모임에 참석한 멤버들의 현재 위치를 실시간으로 반영하여 카카오맵 marker 표시
- 각 멤버 별 정보 표시
  - marker
  - 닉네임 over-lay
  - 모임장소까지의 거리(m) over-lay
  - 최근 채팅 내용 over-lay
- 우측 상단의 스위칭 버튼을 통한 2가지의 지도 배경화면 스위칭 가능
  - tile-set 배경: 모임장소로부터 2,500m 이하는 500m 간격의 원(circle) 이상은 10km 간격의 원(circle) 표시
  - map 배경: 카카오맵 배경
- 모임시간 기준 전/후 3시간 동안 실시간 위치 공유 가능 (그 이후 시간에는 실시간 위치공유 불가)
- 서비스 접속 시 잡혀있는 모임시간을 판별하여 위치공유 on/off
  - 해당 모임의 지도 페이지에 접속하는 것이 아닌 서비스에 접속하기만 해도 위치공유를 하기 위함

<img src="/docs/gif/arrive_verification.gif" width="300"/>

- 모임 장소 반경 100m 이내 도달 시 하단의 "도착 완료" 버튼을 통해 도착 인증마크 획득 가능
- 모임시간이 지난 경우 모임장소 반경 100m에 도달하였더라도 "도착 완료" 불가능 (dialog 알림창 표시)
- 모임시간이 지나도록 미도착 시 자동으로 지각 멤버에 대해 지각 마크 부여
  - 도착 마크 : 하늘색 원
  - 지각 마크 : 빨간색 원

### Automatic Calculate : 자동 정산

<img src="/docs/gif/automatic_calculate.gif" width="300"/>

- 영수증 이미지 촬영 또는 업로드를 통해 영수증 이미지 업로드 가능
- 업로드 된 영수증 이미지 내 텍스트를 감지하여 "상호명, 금액" 추출
- 총 합계 금액과 모임에 참석한 멤버의 수를 기반으로 N/1 정산 제공
  - 만약, 지각비를 설정한 경우 정산 시 지각 멤버에게 지각비 추가 정산 진행
  - 각 멤버 별 화면에는 개인이 내야하는 금액 표시
- 계산된 정산 내역은 "카카오톡 공유하기" 버튼을 통해 카카오톡 메신저 공유 가능

### MyPage : 마이페이지

<img src="/docs/gif/mypage.gif" width="300"/>

- 로그인 한 사용자의 닉네임, 프로필 이미지 표시
- 사용자의 모임 활동 이력을 기반으로 3가지의 활동 데이터 요약 표시
  - 이번달 모임 횟수
  - 이때까지의 총 모임 횟수 중 누적 지각 횟수
  - 지난달 총 소비금액
- 캘린더를 통한 모임이력 조회 제공
  - 모임이 잡혀있던 일자 하이라이트 표시
  - 선택한 일자의 모임 이력 조회 가능(무한 스크롤 적용)
  - 모임 선택 시 해당 모임의 상세페이지로 이동

### Info : 서비스 소개

<img src="/docs/gif/info.gif" width="300"/>

- Almost There 서비스 사용 설명서
- Almost There 서비스 전반에 대한 기능 사용 설명을 제공
