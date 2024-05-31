package com.gumibom.travelmaker.ui.login.forgetIdPw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumibom.travelmaker.domain.login.FindIdUseCase
import com.gumibom.travelmaker.ui.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindIdPwViewModel @Inject constructor(
    private val findIdUseCase: FindIdUseCase
): ViewModel() {

    private val _userId = MutableLiveData<Event<String>>()
    val userId : LiveData<Event<String>> = _userId

    fun findId(phoneNum : String) {
        viewModelScope.launch {
            val event = Event(findIdUseCase.findId(phoneNum))
            _userId.value = event
        }
    }
}