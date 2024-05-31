package com.gumibom.travelmaker.data.repository.pamphlet

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
import retrofit2.http.Path

interface PamphletRepository {
    suspend fun makePamphlet(image : MultipartBody.Part, pamphletRequestDTO: RequestBody) : Response<PamphletResponseDTO>
    suspend fun getMyRecord(userId: Long) : Response<List<PamphletItem>>
    suspend fun finishRecordMyPamphlet(pamphletId : Long) : Response<IsSuccessResponseDTO>

    suspend fun getAllMyRecord(pamphletId: Long) : Response<MutableList<RecordResponseDTO>>

    suspend fun makeRecord(
        image : MultipartBody.Part?,
        video : MultipartBody.Part?,
        makeRecordRequestDTO : RequestBody ) : Response<IsSuccessResponseDTO>

    suspend fun deleteRecord(deleteRecordRequestDTO: DeleteRecordRequestDTO) : Response<IsSuccessResponseDTO>

    suspend fun getOtherPamphlet(userId : Long) : Response<List<PamphletItem>>
}