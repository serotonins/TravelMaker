package com.gumibom.travelmaker.ui.main.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.R.drawable
import com.gumibom.travelmaker.domain.mypage.DeleteUserUseCase
import com.gumibom.travelmaker.domain.mypage.GetUserTrustLevelUseCase
import com.gumibom.travelmaker.ui.common.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyPageViewModel_싸피"

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val deleteUserUseCase: DeleteUserUseCase
): ViewModel() {
    var nickname = "개똥구리"
    private val _urlLiveData = MutableLiveData<String>()
    val urlLiveData : LiveData<String> = _urlLiveData
    private var _trustLevelImageId = MutableLiveData<Int>()
    val trustLevelImageId : LiveData<Int> = _trustLevelImageId
    fun updateProfilePicture(filePath:String){
        _urlLiveData.value = filePath
    }

    var selectBirthDate = ""

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
    fun deleteAccount(){
        viewModelScope.launch {
            deleteUserUseCase.deleteMyInfo()
        }
    }


}