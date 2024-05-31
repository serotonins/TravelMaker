package com.gumibom.travelmaker.domain.mypage

import android.util.Log
import com.gumibom.travelmaker.data.dto.response.UserResponseDTO
import com.gumibom.travelmaker.data.repository.myPage.MyPageRepository
import com.gumibom.travelmaker.model.User
import javax.inject.Inject

private const val TAG = "GetAllUserUseCase_싸피"
class GetAllUserUseCase @Inject constructor(
    private val myPageRepositoryImpl: MyPageRepository
) {
    // TODO 여기서 null check하고 mainViewModel로 넘기기

    suspend fun getMyUserInfo() : User? {
        val response = myPageRepositoryImpl.getMyUserInfo()
        Log.d(TAG, "getMyUserInfo: $response")
        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                return convertModel(body)
            }
        }
        return null
    }

    /**
     * DTO -> Model
     */
    private fun convertModel(body: UserResponseDTO): User {
        return User(
            body.userId,
            body.username,
            body.nickname,
            body.birth,
            body.profileImgURL ?: "",
            body.trust ?: 0.0,
            body.town,
            body.nation,
            body.categories
        )
    }
}