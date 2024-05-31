package com.gumibom.travelmaker.constant

import com.gumibom.travelmaker.BuildConfig


// 네이버, 구글 API 키
const val NAVER_ID_KEY = BuildConfig.NAVER_ID_KEY
const val NAVER_SECRET_KEY = BuildConfig.NAVER_SECRET_KEY
const val GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY
const val KAKAO_API_KEY = BuildConfig.KAKAO_API_KEY

// 네이버 장소 parameter
const val DISPLAY = 5

// 정규식
const val KOREAN_PATTERN = "^[가-힣0-9 ]+$"
const val ENGLISH_PATTERN = "^[a-zA-Z0-9 ]+$"

// Toast 메시지
const val NO_SEARCH_LOCATION = "검색하신 장소가 없습니다."
const val WRONG_INPUT = "잘못된 입력입니다. 다시 검색해주세요."

// 기타 상수
const val KOREAN = "korean"
const val ENGLISH = "english"

// 아이디 찾기 상수
const val EMPTY_NICKNAME = 0
const val WRONG_NICKNAME = 1
const val CORRECT_NICKNAME = 2

// 로그인 상수
const val SUCCESS_LOGIN = "로그인에 성공하였습니다."
const val NO_LOGIN = "아이디나 비밀번호를 다시 입력해주세요."

// 구글 로그인 상수
const val FAIL_GOOGLE_LOGIN = "구글 로그인에 실패하였습니다."

// 퍼미션 메시지
const val NOT_ALLOW_PERMISSION = "위치 권한이 필요합니다."
const val DENIED_LOCATION_PERMISSION = "위치 권한이 필요합니다. 지도를 종료합니다."

// 카테고리 String

// 캘린더 메시지
const val NO_DATE = "현재 날짜 이전은 선택할 수 없습니다."
const val WRONG_DATE = "잘못된 날짜 형식입니다."
const val BEFORE_DATE = "모임 시작 날짜보다\n이전 날짜를 선택해주세요."
const val AFTER_DATE = "모임 시작 날짜 이후를 선택해주세요."

// 모임 생성 메시지
const val NOT_ENOUGH_INPUT = "빠진 입력이 있어요\n다시 한번 확인해주세요."

// 팜플렛 생성 메시지
const val NOT_ALLOW_TITLE = "제목을 확인해주세요."
const val SUCCESS_CREATE_MEETING_POST = "모임 생성에 성공하였습니다."
const val FAIL_CREATE_MEETING_POST = "모임 생성에 실패하였습니다."

// 내 기록 메시지
const val NO_RECORD = "아직 기록물이 없습니다.\n우측 상단에 아이콘을 눌러 기록을 추가해보세요."

// 문자 인증 메시지
const val NOT_ALLOW_SMS = "문자 인증을 다시 확인해주세요."