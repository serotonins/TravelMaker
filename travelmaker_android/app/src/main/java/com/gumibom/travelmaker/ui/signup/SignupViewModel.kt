package com.gumibom.travelmaker.ui.signup

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumibom.travelmaker.data.dto.request.PhoneCertificationRequestDTO
import com.gumibom.travelmaker.data.dto.request.PhoneNumberRequestDTO
import com.gumibom.travelmaker.data.dto.request.RequestDto
import com.gumibom.travelmaker.data.dto.request.SignInUserDataRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.SignInResponseDTO
import com.gumibom.travelmaker.domain.signup.CheckCertificationUseCase
import com.gumibom.travelmaker.domain.signup.CheckDuplicatedIdUseCase
import com.gumibom.travelmaker.domain.signup.CheckDuplicatedNicknameUseCase
import com.gumibom.travelmaker.domain.signup.GetGoogleLocationUseCase

import com.gumibom.travelmaker.domain.signup.GetKakaoLocationUseCase
import com.gumibom.travelmaker.domain.signup.CheckSecretNumberUseCase
import com.gumibom.travelmaker.domain.signup.SaveUserInfoUseCase

import com.gumibom.travelmaker.domain.signup.SendPhoneNumberUseCase
import com.gumibom.travelmaker.model.Address
import com.gumibom.travelmaker.model.BooleanResponse
import com.gumibom.travelmaker.model.GoogleUser
import com.gumibom.travelmaker.model.RequestUserData
import com.gumibom.travelmaker.model.SignInUserDataRequest
import com.gumibom.travelmaker.ui.common.CommonViewModel
import com.gumibom.travelmaker.ui.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "SignupViewModel_싸피"
@HiltViewModel
class SignupViewModel @Inject constructor(
    private val getGoogleLocationUseCase: GetGoogleLocationUseCase,
    private val sendPhoneNumberUseCase: SendPhoneNumberUseCase,
    private val checkDuplicatedIdUseCase: CheckDuplicatedIdUseCase,
    private val checkDuplicatedNicknameUseCase: CheckDuplicatedNicknameUseCase,
    private val getKakaoLocationUseCase: GetKakaoLocationUseCase,
    private val saveUserInfoUseCase :SaveUserInfoUseCase,
    private val checkCertificationUseCase: CheckCertificationUseCase
) : ViewModel(), CommonViewModel {

    /*
        변수 사용하는 공간 시작
     */
    init {

    }

    // 우건
    var bundle : Bundle? = null
    // 가변형 변수 자리

    var loginId : String = ""
    var password : String = ""
    var nickname : String = ""
    var email : String? = null
    var phoneNumber = ""
    var selectNation = ""
    var selectTown = ""
    var selectGender = ""
    var selectBirthDate = ""
    var profileImage = ""
    var categoryList = mutableListOf<String>()

    // 가변형 변수 자리
    // 가변형 변수 자리

    //인호
    private val _favoriteList = MutableLiveData<List<String>>()
    val favoriteList: LiveData<List<String>> = _favoriteList
    fun updateFavoriteList(newList: List<String>) {
        _favoriteList.value = newList
    }


    private val _isSignup = MutableLiveData<IsSuccessResponseDTO>()
    val isSignup : LiveData<IsSuccessResponseDTO> = _isSignup


    private val _isDupNick = MutableLiveData<SignInResponseDTO>()
    val isDupNick:LiveData<SignInResponseDTO> = _isDupNick


    fun checkDupNickName(nickName:String){
        viewModelScope.launch {
            _isDupNick.value = checkDuplicatedNicknameUseCase.checkDuplicatedNick(nickName)
        }
    }
    //인호 끝

// 우건


    // 가변형 변수 자리

    private val _address = MutableLiveData<String>()
    val address : LiveData<String> = _address

    private val _kakaoAddressList = MutableLiveData<MutableList<Address>>()
    val kakaoAddressList : LiveData<MutableList<Address>> = _kakaoAddressList

    private val _googleAddressList = MutableLiveData<MutableList<Address>>()
    val googleAddressList : LiveData<MutableList<Address>> = _googleAddressList

    private val _isSendPhoneSuccess = MutableLiveData<Event<Boolean>>()
    val isSendPhoneSuccess : LiveData<Event<Boolean>> = _isSendPhoneSuccess

    private val _isCertificationSuccess = MutableLiveData<Event<Boolean>>()
    val isCertificationSuccess : LiveData<Event<Boolean>> = _isCertificationSuccess

    // 타이머의 코루틴을 추적하는 변수
    private var timerJob : Job? = null

    // 지원


    // 인호

    /*
        변수 사용하는 공간 끝
    */


    /*
        함수 사용하는 공간 시작
     */
// 우건

    // 카카오 장소 검색 리스트 받기
    fun getKakaoLocation(apiKey : String, location : String) {
        viewModelScope.launch {
            _kakaoAddressList.value = getKakaoLocationUseCase.getKakaoLocation(apiKey, location)
        }
    }
    
    // 구글 장소 검색 리스트 받기
    fun getGoogleLocation(location : String, apiKey : String) {
        viewModelScope.launch { 
            val googleAddressList = getGoogleLocationUseCase.findGoogleLocation(location, apiKey)
            
            _googleAddressList.value = googleAddressList
            Log.d(TAG, "getGoogleLocation: $googleAddressList")
        }
    }

    override fun setAddress(address : String) {
        _address.value = address
        Log.d(TAG, "setAddress: ${_address.value}")
    }
    fun sendPhoneNumber(phoneNumberRequestDTO : PhoneNumberRequestDTO) {
        viewModelScope.launch {
            val event = Event(sendPhoneNumberUseCase.sendPhoneNumber(phoneNumberRequestDTO))
            _isSendPhoneSuccess.value = event
        }
    }
    fun isCertification(phoneCertificationRequestDTO: PhoneCertificationRequestDTO) {
        viewModelScope.launch {
            val event = Event(checkCertificationUseCase.isCertificationNumber(phoneCertificationRequestDTO))
            _isCertificationSuccess.value = event
        }
    }

    // 코루틴으로 3분 타이머를 동작하는 함수
    fun startTimer(textView : TextView) {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            val totalTime = 3 * 60 // 3분

            for (time in totalTime downTo 1) {
                val mins = time / 60
                val secs = time % 60

                val timeFormat = "${mins}:${"%02d".format(secs)}" // 3:00 형태로 변환
                updateTimerUI(timeFormat, textView)
                delay(1000)
            }
        }
    }

    fun endTimer() {
        timerJob?.cancel()
    }

    private fun updateTimerUI(timeFormat : String, timerText : TextView) {
        timerText.text = timeFormat
    }

    private val _isDuplicatedId = MutableLiveData<SignInResponseDTO>()
    val isDuplicatedId : LiveData<SignInResponseDTO> = _isDuplicatedId

    // 지원
    fun checkId(id: String) {
        viewModelScope.launch {
        // '중복된 아이디' 인 지 의 기본값 = false: '중복이 아닌 아이디' 입니다.

        // '중복된 아이디'여부의 기본값 = false ==> '중복이 아닌 아이디' 입니다.
            _isDuplicatedId.value = checkDuplicatedIdUseCase.checkDuplicatedId(id)
        }
    }

    fun signup(requestUserData: RequestUserData) {
        viewModelScope.launch {
            _isSignup.value = saveUserInfoUseCase.saveUserInfo(requestUserData, profileImage)
        }
    }
}

