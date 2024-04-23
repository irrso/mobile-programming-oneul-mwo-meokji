# 😸🐶 반려동물 통합관리 시스템 및 제어 앱

<div align="right">
  마이크로프로세서및실험
  Date: 2022.04.23 - 2022.06.20
</div>
<br>

<!--표지 사진 추가-->

## 메인 아이디어
- 집에 혼자 있는 반려동물을 어떻게 관리할 수 있을까 ❓
- 사료 급여, 영상 시청, 온습도 확인, 녹음 기능을 통합하여 제어하자 💡
- 라즈베리파이와 여러 센서를 사용해 자동 급식기와 안드로이드 앱 제작 📲
<br>

## 역할 분담
<!--표 추가-->
|팀장|팀원|팀원|
|:---:|:---:|:---:|
|[오소영](https://github.com/irrso)|***|***|
|앱 설계 및 제작 <br> 데이터베이스 연동 <br> 발표 자료 제작 및 제출 <br> 필요 물품 리스트업 및 수령 <br> 코드 형상관리|앱 제작 <br> firebase를 통한 데이터 전송 관리 및 구현 <br> 프로젝트 제안 및 최종 발표|자동 급식기 제작 및 센서 부착 <br> 라즈베리파이 센서 회로 설계 및 코드 구현|
<br>

## 기술 스택
|분류|기술 스택|
|---|---|
|S/W|<img src="https://img.shields.io/badge/Android-34A853.svg?style=flat-square&logo=android&logoColor=white"/> <img src="https://img.shields.io/badge/Android Studio-3DDC84.svg?style=flat-square&logo=androidstudio&logoColor=white"/> <img src="https://img.shields.io/badge/Java(JDK1.8)-007396.svg?style=flat-square&logo=java&logoColor=white"/>|
|H/W|<img src="https://img.shields.io/badge/Raspbian GNU/Linux 11-A22846.svg?style=flat-square&logo=raspberrypi&logoColor=white"/> <img src="https://img.shields.io/badge/Firebase-FFCA28.svg?style=flat-square&logo=firebase&logoColor=white"/> <img src="https://img.shields.io/badge/C-A8B9CC.svg?style=flat-square&logo=c&logoColor=black"/> <img src="https://img.shields.io/badge/Python-3776AB.svg?style=flat-square&logo=python&logoColor=white"/>|
|협업|<img src="https://img.shields.io/badge/KakaoTalk-FFCD00.svg?style=flat-square&logo=kakaotalk&logoColor=black"/> <img src="https://img.shields.io/badge/Google Docs-4285F4.svg?style=flat-square&logo=googledocs&logoColor=white"/> <img src="https://img.shields.io/badge/GitHub-181717.svg?style=flat-square&logo=github&logoColor=white"/>|
<br>

## 시스템 개요
- 라즈베리파이: 자동 급식기에 부착된 센서 제어, pi카메라를 위한 웹 호스팅 역할
- 스마트폰: 앱을 통해 라즈베리파이에 데이터 전송 및 수신, 자동 급식기 논리적으로 제어하는 UI 역할
- firebase: 라즈베리파이와 스마트폰 앱 간의 전송 매개체 역할, 로컬에 국한되지 않은 제어 구현 [^id]
[^id]: 🚩 pi카메라를 통한 안심 카메라 기능은 도메인 설정 및 포워딩 등의 문제가 발생해 로컬로만 실행하도록 구상

|시스템 구상도|시스템 회로도|
|---|---|
|<img src="https://github.com/irrso/Mobile_project/assets/105829324/0198ef37-d9bd-457a-96d5-cbd92e2d47d5">|<img src="https://github.com/irrso/Mobile_project/assets/105829324/cac1aed3-de5f-4209-a72e-b3a6f99b7b2e">|
