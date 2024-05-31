package com.gumibom.travelmaker.domain.mypage

import com.gumibom.travelmaker.data.repository.myPage.MyPageRepository
import com.gumibom.travelmaker.data.repository.myPage.MyPageRepositoryImpl
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val myPageRepositoryImpl: MyPageRepository
) {
    suspend fun deleteMyInfo(){
        myPageRepositoryImpl.deleteMyInfo()
    }
}