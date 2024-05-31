package com.gumibom.travelmaker.ui.main.lookpamphlets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumibom.travelmaker.domain.pamphlet.GetOtherPamphletUseCase
import com.gumibom.travelmaker.model.pamphlet.PamphletItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LookPamphletViewModel @Inject constructor(
    private val getOtherPamphletUseCase: GetOtherPamphletUseCase
): ViewModel() {

    private val _otherPamphlet = MutableLiveData<List<PamphletItem>>()
    val otherPamphlet : LiveData<List<PamphletItem>> = _otherPamphlet

    fun getOtherPamphletList(userId : Long) {
        viewModelScope.launch {
            _otherPamphlet.value = getOtherPamphletUseCase.getOtherPamphlet(userId)
        }
    }
}