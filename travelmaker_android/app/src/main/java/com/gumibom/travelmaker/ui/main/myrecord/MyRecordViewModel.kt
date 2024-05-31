package com.gumibom.travelmaker.ui.main.myrecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumibom.travelmaker.domain.pamphlet.FinishTravelPamphletUseCase
import com.gumibom.travelmaker.domain.pamphlet.GetMyRecordUseCase
import com.gumibom.travelmaker.model.pamphlet.PamphletItem
import com.gumibom.travelmaker.ui.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecordViewModel @Inject constructor(
    private val getMyRecordUseCase: GetMyRecordUseCase,
    private val finishTravelPamphletUseCase: FinishTravelPamphletUseCase
) : ViewModel() {

    private val _isFinish = MutableLiveData<Boolean>()
    val isFinish : LiveData<Boolean> = _isFinish

    private val _myRecordIng = MutableLiveData<List<PamphletItem>>()
    val myRecordIng : LiveData<List<PamphletItem>> = _myRecordIng

    private val _myRecordFinish = MutableLiveData<List<PamphletItem>>()
    val myRecordFinish : LiveData<List<PamphletItem>> = _myRecordFinish

    fun getMyRecord(userId : Long) {
        viewModelScope.launch {
            val pamphletItemList = getMyRecordUseCase.getMyRecord(userId)

            setTravelList(pamphletItemList)
        }
    }

    /**
     * 여행 중, 여행 완료 리스트로 분리하는 함수
     */
    private fun setTravelList(pamphletList : List<PamphletItem>) {
        val finishTravelList = mutableListOf<PamphletItem>()
        val ingTravelList = mutableListOf<PamphletItem>()

        for (pamphlet in pamphletList) {
            if (pamphlet.isFinish) {
                finishTravelList.add(pamphlet)
            } else {
                ingTravelList.add(pamphlet)
            }
        }
        _myRecordFinish.value = finishTravelList
        _myRecordIng.value = ingTravelList
    }

    /**
     * 여행 중인 팜플렛을 완료하는 함수
     */
    fun finishTravelPamphlet(pamphletId : Long, pamphlet : PamphletItem) {
        viewModelScope.launch {
            val message = finishTravelPamphletUseCase.finishRecordMyPamphlet(pamphletId) ?: ""

            if (message.isNotEmpty()) {
                // _myRecording이 null이면 빈 리스트 반환, 아니면 그냥 리스트 반환
                val myRecordIng = _myRecordIng.value.orEmpty().toMutableList()

                myRecordIng.remove(pamphlet)
                _myRecordIng.value = myRecordIng
            }

        }
    }

}