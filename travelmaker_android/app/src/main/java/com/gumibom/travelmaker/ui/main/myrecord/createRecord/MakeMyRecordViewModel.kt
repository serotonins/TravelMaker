package com.gumibom.travelmaker.ui.main.myrecord.createRecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumibom.travelmaker.data.dto.request.RecordRequestDTO
import com.gumibom.travelmaker.domain.pamphlet.MakeRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeMyRecordViewModel @Inject constructor(
    private val makeRecordUseCase: MakeRecordUseCase
) : ViewModel() {

    var emojiText = ""

    private val _myRecordImage = MutableLiveData<String>()
    val myRecordImage : LiveData<String> = _myRecordImage

    private val _isSuccessMessage = MutableLiveData<String>()
    val isSuccessMessage : LiveData<String> = _isSuccessMessage

    fun setMyRecordImage(filePath : String) {
        _myRecordImage.value = filePath
    }

    fun makeImageRecord(imageUrl : String, videoUrl : String, recordRequestDTO: RecordRequestDTO) {
        viewModelScope.launch {
            val isSuccess = makeRecordUseCase.makeRecord(imageUrl, videoUrl, recordRequestDTO)

            if (isSuccess != null && isSuccess.isSuccess) {
                _isSuccessMessage.value = "기록이 저장되었습니다."
            } else if (isSuccess != null && !isSuccess.isSuccess) {
                _isSuccessMessage.value = "기록 저장이 실패되었습니다."
            } else {
                _isSuccessMessage.value = "기록 저장이 실패되었습니다."
            }
        }
    }

}