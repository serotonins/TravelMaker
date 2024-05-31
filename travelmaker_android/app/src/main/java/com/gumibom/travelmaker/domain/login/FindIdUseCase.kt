package com.gumibom.travelmaker.domain.login

import com.gumibom.travelmaker.data.repository.login.LoginRepository
import com.gumibom.travelmaker.data.repository.login.LoginRepositoryImpl
import javax.inject.Inject

class FindIdUseCase @Inject constructor(
    private val loginRepositoryImpl: LoginRepository
) {

    suspend fun findId(phoneNum : String) : String {
        val response = loginRepositoryImpl.findId(phoneNum)
        var message = ""
        if (response.isSuccessful) {

            when (response.code()) {
                204 -> message = ""
                200 -> message = response.body()!!
            }
        }
        return message
    }
}