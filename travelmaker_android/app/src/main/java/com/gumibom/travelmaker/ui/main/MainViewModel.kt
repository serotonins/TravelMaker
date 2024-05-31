package com.gumibom.travelmaker.ui.main

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.constant.GOOGLE_API_KEY
import com.gumibom.travelmaker.constant.KAKAO_API_KEY
import com.gumibom.travelmaker.data.dto.mygroup.MyMeetingGroupDTOItem
import com.gumibom.travelmaker.data.dto.request.FcmGetNotifyListDTO

import com.gumibom.travelmaker.data.dto.request.MarkerCategoryPositionRequestDTO
import com.gumibom.travelmaker.data.dto.request.MarkerPositionRequestDTO
import com.gumibom.travelmaker.data.dto.request.MeetingPostRequestDTO
import com.gumibom.travelmaker.domain.meeting.GetMarkerCategoryPositionsUseCase
import com.gumibom.travelmaker.data.dto.request.FcmRequestGroupDTO
import com.gumibom.travelmaker.data.dto.request.FcmTokenRequestDTO
import com.gumibom.travelmaker.data.dto.request.FirebaseResponseRefuseAcceptDTO
import com.gumibom.travelmaker.data.dto.request.RequestDto
import com.gumibom.travelmaker.data.dto.request.SignInUserDataRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO

import com.gumibom.travelmaker.domain.firebase.FirebaseAcceptCrewUseCase
import com.gumibom.travelmaker.domain.firebase.FirebaseFcmUploadTokenUseCase
import com.gumibom.travelmaker.domain.firebase.FirebaseNotifyListUseCase
import com.gumibom.travelmaker.domain.firebase.FirebaseRefuseCrewUseCase
import com.gumibom.travelmaker.domain.firebase.FirebaseRequestGroupUseCase

import com.gumibom.travelmaker.domain.meeting.GetMarkerPositionsUseCase
import com.gumibom.travelmaker.domain.meeting.GetMyMeetingGroupListUseCase
import com.gumibom.travelmaker.domain.meeting.GetPostDetailUseCase
import com.gumibom.travelmaker.domain.meeting.PutActiveChattingUseCase
import com.gumibom.travelmaker.domain.mypage.GetAllUserUseCase
import com.gumibom.travelmaker.domain.pamphlet.MakePamphletUseCase
import com.gumibom.travelmaker.domain.signup.GetGoogleLocationUseCase
import com.gumibom.travelmaker.domain.signup.GetKakaoLocationUseCase
import com.gumibom.travelmaker.model.Address
import com.gumibom.travelmaker.model.BooleanResponse
import com.gumibom.travelmaker.model.MarkerPosition
import com.gumibom.travelmaker.model.PostDetail
import com.gumibom.travelmaker.model.RequestUserData
import com.gumibom.travelmaker.model.SignInUserDataRequest
import com.gumibom.travelmaker.model.User
import com.gumibom.travelmaker.ui.common.CommonViewModel
import com.gumibom.travelmaker.ui.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel_싸피"
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getGoogleLocationUseCase: GetGoogleLocationUseCase,
    private val getKakaoLocationUseCase: GetKakaoLocationUseCase,
    private val getMarkerPositionsUseCase : GetMarkerPositionsUseCase,
    private val getMarkerCategoryPositionsUseCase: GetMarkerCategoryPositionsUseCase,
    private val requestGroupUseCase: FirebaseRequestGroupUseCase,
    private val getPostDetailUseCase:GetPostDetailUseCase,
    private val firebaseNotifyListUseCase: FirebaseNotifyListUseCase,
    private val firebaseFcmUploadTokenUseCase: FirebaseFcmUploadTokenUseCase,
    private val getAllUserUseCase: GetAllUserUseCase,
    private val firebaseFcmAcceptCrewUseCase: FirebaseAcceptCrewUseCase,
    private val firebaseRefuseCrewUseCase: FirebaseRefuseCrewUseCase,
    private val makePamphletUseCase: MakePamphletUseCase,
    private val getMyMeetingGroupListUseCase: GetMyMeetingGroupListUseCase,
    private val putActiveChattingUseCase: PutActiveChattingUseCase


) : ViewModel(), CommonViewModel {

    private val _isActiveChat = MutableLiveData<Event<Boolean>>()
    val isActiveChat :LiveData<Event<Boolean>> = _isActiveChat

    fun putActiveChatting(groupId:Long){
        viewModelScope.launch {
            val dto = putActiveChattingUseCase.putGroupChat(groupId)
            _isActiveChat.value = Event(dto.isSuccess)
        }
    }

    private val _myMeetingGroupList = MutableLiveData<List<MyMeetingGroupDTOItem>>()
    val myMeetingGroupList :LiveData<List<MyMeetingGroupDTOItem>> = _myMeetingGroupList

    fun getMyMeetingGroupList(userId: Long){
        viewModelScope.launch {
            _myMeetingGroupList.value = getMyMeetingGroupListUseCase.getMyGroupList(userId)
            Log.d(TAG, "GroupList: ${myMeetingGroupList.value.toString()}")
        }
    }


    //모임 데이터들을 받아오는 로직
    private val _isGetNotifyList = MutableLiveData<FcmGetNotifyListDTO>()
    val isGetNotifyList : LiveData<FcmGetNotifyListDTO> = _isGetNotifyList
    fun getNotifyList(userId: Long){
        viewModelScope.launch {
            _isGetNotifyList.value = firebaseNotifyListUseCase.getNotifyList(userId)
        }
    }
    //모임 요청 수락에 대한 로직
    private val _isRequestAcceptCrew = MutableLiveData<Event<Boolean>>()
    val isRequestAcceptCrew : LiveData<Event<Boolean>> = _isRequestAcceptCrew
    fun acceptCrew(firebaseAcceptDTO: FirebaseResponseRefuseAcceptDTO){
        viewModelScope.launch {
            val isRequest = firebaseFcmAcceptCrewUseCase.acceptCrew(firebaseAcceptDTO)
            _isRequestAcceptCrew.value = Event(isRequest.isSuccess)
        }
    }
    //모임 요청 거절에 대한 로직
    private val _isRequestRefuseCrew = MutableLiveData<Event<Boolean>>()
    val isRequestRefuseCrew :LiveData<Event<Boolean>> = _isRequestRefuseCrew;
    fun refuseCrew(firebaseRefuseDTO:FirebaseResponseRefuseAcceptDTO){
        viewModelScope.launch {
            val isRequest = firebaseRefuseCrewUseCase.refuseCrew(firebaseRefuseDTO)
            _isRequestRefuseCrew.value = Event(isRequest.isSuccess)
        }
    }
    //모임 요청에 대한 로직
    private val _isRequestSuccess = MutableLiveData<BooleanResponse>()
    val isRequestSuccess :LiveData<BooleanResponse> = _isRequestSuccess
    fun requestGroup(firebaseDTO: FcmRequestGroupDTO){
        viewModelScope.launch {
            _isRequestSuccess.value = requestGroupUseCase.requestGroup(firebaseDTO)
        }
    }


    var address : Address? = null

    // 현재 위치의 위도 경도
    var currentLatitude = 0.0
    var currentLongitude = 0.0

    // 초기 찐 내 위치의 위도 경도
    var initLatitude = 0.0
    var initLongitude = 0.0

    private val _markerList = MutableLiveData<List<MarkerPosition>>()
    val markerList : LiveData<List<MarkerPosition>> = _markerList

    private val _markerCategoryList = MutableLiveData<List<MarkerPosition>>()
    val markerCategoryList : LiveData<List<MarkerPosition>> = _markerCategoryList


    private val _kakaoAddressList = MutableLiveData<MutableList<Address>>()
    val kakaoAddressList : LiveData<MutableList<Address>> = _kakaoAddressList

    private val _googleAddressList = MutableLiveData<MutableList<Address>>()
    val googleAddressList : LiveData<MutableList<Address>> = _googleAddressList

    private val _selectAddress = MutableLiveData<Address>()
    val selectAddress : LiveData<Address> = _selectAddress

    //근방 위치를 통해 해당 정보의 데이터를 가져와서 viewmodel에 저장함.
    private val _postDTO = MutableLiveData<PostDetail>()
    val postDTO:LiveData<PostDetail> = _postDTO

    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message

    private val _pamphletThumbnail = MutableLiveData<String>()
    val pamphletThumbnail : LiveData<String> = _pamphletThumbnail

    var pamphletTitle = ""
    var pamphletCategory = mutableListOf<String>()

    var isFinish = false

    //서버에서 마커를 클릭한 정보들을 가져옴 -> ui단에서 받은 데이터들을 저장하장
    fun getPostDetail(pos:Long){
        viewModelScope.launch {
            _postDTO.value = getPostDetailUseCase.getPostDetail(pos)
        }
    }

    private val _isUploadToken = MutableLiveData<BooleanResponse>()
    val isUploadToken:LiveData<BooleanResponse> = _isUploadToken
    fun uploadToken(firebaseFcmTokenRequestDTO: FcmTokenRequestDTO){
        viewModelScope.launch {
            _isUploadToken.value = firebaseFcmUploadTokenUseCase.uploadToken(firebaseFcmTokenRequestDTO)
        }
    }

    // 서버에서 내 근방 위치 모임들을 가져옴
    fun getMarkers(markerPositionRequestDTO : MarkerPositionRequestDTO) {
        viewModelScope.launch {
            _markerList.value = getMarkerPositionsUseCase.getMarkerPositions(markerPositionRequestDTO)
        }
    }

    // 내 근방 위치 모임 카테고리 필터링
    fun getCategoryMarkers(markerCategoryPositionRequestDTO: MarkerCategoryPositionRequestDTO) {
        viewModelScope.launch {
            // TODO 마커 리스트로 고쳐야하나?
            _markerList.value = getMarkerCategoryPositionsUseCase.getMarkerCategoryPositions(markerCategoryPositionRequestDTO)
        }
    }

    fun getKakaoLatLng(location : String) {
        viewModelScope.launch {
            _kakaoAddressList.value = getKakaoLocationUseCase.getKakaoLocation(KAKAO_API_KEY, location)
        }
    }
    fun getGoogleLatLng(location : String) {
        viewModelScope.launch{
            _googleAddressList.value = getGoogleLocationUseCase.findGoogleLocation(location, GOOGLE_API_KEY)
        }
    }

    private val _urlLiveData = MutableLiveData<String>()
    val urlLiveData : LiveData<String> = _urlLiveData
    private var _trustLevelImageId = MutableLiveData<Int>()
    val trustLevelImageId : LiveData<Int> = _trustLevelImageId
    fun updateProfilePicture(filePath:String){
        _urlLiveData.value = filePath
    }


    fun updateAndGetTrustLevelImageId(trustPoint: Int): Int {
        return when (trustPoint) {
            in 0..199 -> R.drawable.img_trust_1
            in 200..299 -> R.drawable.img_trust_2
            in 300..399 -> R.drawable.img_trust_3
            in 400..499 -> R.drawable.img_trust_4
            in 500..599 -> R.drawable.img_trust_5
            in 600..699 -> R.drawable.img_trust_6
            in 700..799 -> R.drawable.img_trust_7
            in 800..899 -> R.drawable.img_trust_8
            else -> R.drawable.img_trust_9  // 900점 이상인 경우와 그 외 모든 경우
        }
    }
    fun userSelectAddress(address : Address) {
        _selectAddress.value = address
        Log.d(TAG, "userSelectAddress: $address")
    }

    fun getAllUser() {
        viewModelScope.launch {
            _user.value = getAllUserUseCase.getMyUserInfo()
        }
    }
    override fun setAddress(address: String) {

    }

    fun setSelectAddress(address : Address) {
        _selectAddress.value = address
    }

    fun setPamphletThumbnail(imageUrl : String) {
        _pamphletThumbnail.value = imageUrl
    }

    suspend fun makePamphlet() : String {
        return viewModelScope.async {
            makePamphletUseCase.makePamphlet(
                _user.value!!.userId,
                pamphletTitle,
                pamphletCategory,
                _pamphletThumbnail.value!!
            )
        }.await()
    }

}