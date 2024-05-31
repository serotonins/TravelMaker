package com.gumibom.travelmaker.ui.main.myrecord.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumibom.travelmaker.data.dto.request.DeleteRecordRequestDTO
import com.gumibom.travelmaker.domain.pamphlet.DeleteRecordUseCase
import com.gumibom.travelmaker.domain.pamphlet.GetAllMyRecordUseCase
import com.gumibom.travelmaker.model.pamphlet.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecordDetailViewModel @Inject constructor(
    private val getAllMyRecordUseCase: GetAllMyRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase
) : ViewModel() {

    private val _myAllRecord = MutableLiveData<MutableList<Record>>()
    val myAllRecord : LiveData<MutableList<Record>> = _myAllRecord

    private val _record = MutableLiveData<Record>()
    val record : LiveData<Record> = _record

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess : LiveData<Boolean> = _isSuccess
    fun getMyAllRecord(pamphletId : Long) {
        viewModelScope.launch {
            _myAllRecord.value = getAllMyRecordUseCase.getAllMyRecord(pamphletId)
        }
    }

    fun setRecord(record : Record) {
        _record.value = record
    }

    fun deleteRecord(deleteRecordRequestDTO: DeleteRecordRequestDTO)  {
        viewModelScope.launch {
            val isSuccess = deleteRecordUseCase.deleteRecord(deleteRecordRequestDTO)
            _isSuccess.value = isSuccess

            if (isSuccess) {
                val myAllRecord = _myAllRecord.value.orEmpty().toMutableList()

                myAllRecord.remove(_record.value!!)
                _myAllRecord.value = myAllRecord
            }
        }
    }

}