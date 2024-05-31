package com.gumibom.travelmaker.data.repository.firebase

import com.gumibom.travelmaker.data.datasource.firebase.FirebaseFcmRemoteDataSource
import com.gumibom.travelmaker.data.dto.request.FcmGetNotifyListDTO
import com.gumibom.travelmaker.data.dto.request.FcmRequestGroupDTO
import com.gumibom.travelmaker.data.dto.request.FcmTokenRequestDTO
import com.gumibom.travelmaker.data.dto.request.FirebaseResponseRefuseAcceptDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import retrofit2.Response

class FirebaseFcmRepositoryImpl (
        private val firebaseFcmRemoteDataSource: FirebaseFcmRemoteDataSource
): FirebaseFcmRepository{


    override suspend fun uploadToken(firebaseFcmTokenRequestDTO: FcmTokenRequestDTO): Response<IsSuccessResponseDTO> {
        return firebaseFcmRemoteDataSource.uploadToken(firebaseFcmTokenRequestDTO)
    }

    override suspend fun groupRequest(fcmRequestGroup: FcmRequestGroupDTO): Response<IsSuccessResponseDTO> {
        return firebaseFcmRemoteDataSource.groupRequest(fcmRequestGroup)
    }

    override suspend fun acceptCrew(fcmRequestGroup: FirebaseResponseRefuseAcceptDTO): Response<IsSuccessResponseDTO> {
        return firebaseFcmRemoteDataSource.acceptCrew(fcmRequestGroup)
    }

    override suspend fun refuseCrew(fcmRequestGroup: FirebaseResponseRefuseAcceptDTO): Response<IsSuccessResponseDTO> {
        return firebaseFcmRemoteDataSource.refuseCrew(fcmRequestGroup)
    }

    override suspend fun getAllRequestList(userId: Long): Response<FcmGetNotifyListDTO> {
        return firebaseFcmRemoteDataSource.getAllRequest(userId)
    }



}