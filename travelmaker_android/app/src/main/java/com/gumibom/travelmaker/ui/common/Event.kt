package com.gumibom.travelmaker.ui.common

/*
데이터가 한 번 소모되면 더 이상 사용되지 않도록 처리
 */
class Event<T>(private val content : T) {

    private var hasBeenHandled = false

    /*
    LiveData의 데이터가 변경될 때마다 이 메서드를 통해
    데이터가 소비되었는지 확인해야함
    */
    fun getContentIfNotHandled() : T?{
        return if (hasBeenHandled){
            null
        } else{
            hasBeenHandled = true
            content
        }
    }
}