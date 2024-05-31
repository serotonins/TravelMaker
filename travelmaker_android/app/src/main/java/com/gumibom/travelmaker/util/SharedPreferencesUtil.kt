package com.gumibom.travelmaker.util

import android.content.Context
import android.content.SharedPreferences

private const val TAG = "SharedPreferencesUtil_싸피"
class SharedPreferencesUtil (context : Context) {
    val SHARED_PREFERENCES_NAME = "travelMaker_preference"
    var preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    // sharedpreference에 로그인 정보 저장하기
    fun addToken(accessToken : String){
        val editor = preferences.edit()
        val token = "Bearer $accessToken"
        editor.putString("accessToken", token)
        editor.apply()
    }

    fun getToken(): String {
        val accessToken = preferences.getString("accessToken", "")
        return if (accessToken != ""){
            accessToken!!
        }else{
            ""
        }
    }

    /**
     * refresh 토큰을 저장하는 함수
     */
    fun addRefreshToken(refreshToken : String) {
        val editor = preferences.edit()
        editor.putString("refreshToken", refreshToken)
        editor.apply()
    }

    fun getRefreshToken() : String {
        val refreshToken = preferences.getString("refreshToken", "")
        return if (refreshToken != ""){
            refreshToken!!
        }else{
            ""
        }
    }

    fun deleteToken(){
        //preference 지우기
        val editor = preferences.edit()
        editor.remove("accessToken")
        editor.apply()
    }

    fun addLoginId(loginId : String) {
        val editor = preferences.edit()
        editor.putString("loginId", loginId)
        editor.apply()
    }

    fun getLoginId() : String {
        val loginId = preferences.getString("loginId", "")
        return if (loginId != ""){
            loginId!!
        }else{
            ""
        }
    }


    fun addGoogleEmail(email : String) {
        val editor = preferences.edit()
        editor.putString("googleEmail", email)
        editor.apply()
    }
    fun getGoogleEmail() : Boolean {
        val isEmail = preferences.getString("googleEmail", "")
        return isEmail != ""
    }
    fun deleteGoogleEmail() {
        //preference 지우기
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}