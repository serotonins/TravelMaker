package com.gumibom.travelmaker.data.api.firebase

import com.gumibom.travelmaker.data.dto.request.FcmGetNotifyListDTO
import com.gumibom.travelmaker.data.dto.request.FcmRequestGroupDTO
import com.gumibom.travelmaker.data.dto.request.FcmTokenRequestDTO
import com.gumibom.travelmaker.data.dto.request.FirebaseResponseRefuseAcceptDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FirebaseTokenService {

    //Token정보를 서버로 전송한다.
    @PUT("user/renewal-fcm-token")
    suspend fun uploadToken(
        @Body fcmTokenRequestDTO: FcmTokenRequestDTO
    ):Response<IsSuccessResponseDTO>

    //모임 요청
    @POST("meeting-post/request-join")
    suspend fun groupRequest(
        @Body fcmRequestGroup: FcmRequestGroupDTO
    ) : Response<IsSuccessResponseDTO>

    @POST("meeting-post/response-join/accept")
    suspend fun acceptCrew(
        @Body fcmRequestGroup: FirebaseResponseRefuseAcceptDTO
    ) : Response<IsSuccessResponseDTO>
    @POST("meeting-post/response-join/refuse")
    suspend fun refuseCrew(
        @Body fcmRequestGroup: FirebaseResponseRefuseAcceptDTO
    ) : Response<IsSuccessResponseDTO>

    @GET("meeting-post/all-request/{userId}")
    suspend fun getAllRequest(
        @Path("userId") userId : Long
    ) : Response<FcmGetNotifyListDTO>

//        @Query("userId") userId:Long,
//        @Query("fcmToken") fcmToken:String


}