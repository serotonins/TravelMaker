package com.gumibom.travelmaker.data.api.pamphlet

import com.gumibom.travelmaker.data.dto.request.DeleteRecordRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.PamphletResponseDTO
import com.gumibom.travelmaker.data.dto.response.RecordResponseDTO
import com.gumibom.travelmaker.model.pamphlet.PamphletItem
import com.gumibom.travelmaker.model.pamphlet.Record
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PamphletService {
    @Multipart
    @POST("/personal-pamphlet")
    suspend fun makePamphlet(
        @Part image : MultipartBody.Part,
        @Part("makePPReqDto") pamphletRequestDTO : RequestBody
    ) : Response<PamphletResponseDTO>

    @GET("/personal-pamphlet/v2/{userId}")
    suspend fun getMyRecord(@Path("userId") userId : Long) : Response<List<PamphletItem>>

    @PUT("/personal-pamphlet/{pamphletId}")
    suspend fun finishRecordMyPamphlet(@Path("pamphletId") pamphletId : Long) : Response<IsSuccessResponseDTO>

    @GET("/personal-record/{pamphletId}")
    suspend fun getAllMyRecord(@Path("pamphletId") pamphletId: Long) : Response<MutableList<RecordResponseDTO>>

    @Multipart
    @POST("/personal-record")
    suspend fun makeRecord(
        @Part image : MultipartBody.Part?,
        @Part video : MultipartBody.Part?,
        @Part("sPRRDto") makeRecordRequestDTO : RequestBody
    ) : Response<IsSuccessResponseDTO>

    @HTTP(method="DELETE", hasBody=true, path="/personal-record")
    suspend fun deleteRecord(@Body deleteRecordRequestDTO: DeleteRecordRequestDTO) : Response<IsSuccessResponseDTO>

    @GET("personal-pamphlet/v3/{userId}")
    suspend fun getOtherPamphlet(@Path("userId") userId : Long) : Response<List<PamphletItem>>
}
