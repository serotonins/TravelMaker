package com.gumibom.travelmaker.data.datasource.firebase

import com.gumibom.travelmaker.data.api.firebase.FirebaseTokenService
import com.gumibom.travelmaker.data.dto.request.FcmGetNotifyListDTO
import com.gumibom.travelmaker.data.dto.request.FcmRequestGroupDTO
import com.gumibom.travelmaker.data.dto.request.FcmTokenRequestDTO
import com.gumibom.travelmaker.data.dto.request.FirebaseResponseRefuseAcceptDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import retrofit2.Response
import javax.inject.Inject

class FirebaseFcmRemoteDataSourceImpl @Inject constructor(
    private val firebaseTokenService: FirebaseTokenService
):FirebaseFcmRemoteDataSource{

    override suspend fun uploadToken(fcmTokenRequestDTO: FcmTokenRequestDTO): Response<IsSuccessResponseDTO> {
        return firebaseTokenService.uploadToken(fcmTokenRequestDTO)
    }

    override suspend fun groupRequest(fcmRequestGroup: FcmRequestGroupDTO): Response<IsSuccessResponseDTO> {
        return firebaseTokenService.groupRequest(fcmRequestGroup)
    }

    override suspend fun acceptCrew(fcmRequestGroup: FirebaseResponseRefuseAcceptDTO): Response<IsSuccessResponseDTO> {
        return  firebaseTokenService.acceptCrew(fcmRequestGroup)
    }

    override suspend fun refuseCrew(fcmRequestGroup: FirebaseResponseRefuseAcceptDTO): Response<IsSuccessResponseDTO> {
        return firebaseTokenService.refuseCrew(fcmRequestGroup)
    }

    override suspend fun getAllRequest(userid: Long): Response<FcmGetNotifyListDTO> {
        return firebaseTokenService.getAllRequest(userid)
    }

}