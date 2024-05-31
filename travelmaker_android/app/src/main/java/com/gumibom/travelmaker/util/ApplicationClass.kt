package com.gumibom.travelmaker.util

import android.Manifest
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ApplicationClass"
@HiltAndroidApp
class ApplicationClass : Application(){
    override fun onCreate() {
        super.onCreate()

        //shared preference 초기화
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

    }
    /* permission check */

    //FCM토큰
    private fun getTokenFCM(){
        //FCM토큰 수신
    }

    companion object {

        lateinit var sharedPreferencesUtil: SharedPreferencesUtil

        const val BASE_URL = "http://i10d202.p.ssafy.io:8080"
        const val GOOGLE_GEOCODE_URL = "https://maps.googleapis.com"
        const val NAVER_LOCATION_SEARCH_URL = "https://openapi.naver.com"
        const val KAKAO_LOCATION_SEARCH_URL = "https://dapi.kakao.com"
//        const val JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3bmRkbmpzODIzIiwiYXV0aCI6IlVTRVIiLCJleHAiOjE3MDczMTc0MjJ9.gkWnCmoVRpOrKqSk4SnxFSJ7XxxgefGsQPzyuTiOjzk"


        fun uploadToken(token:String){
            // 새로운 토큰 수신 시 서버로 전송
            //파이어베이스에 있는 현재 유저의 전화번호를 가져오고,
            //현재 토큰 값과 함께 요청
            //유저 테이블의 아이디와 토큰을 같이 보내서
            //아이디에 매칭되는 아이디에 토큰 테이블을 갱신한다,
        }

    }
}