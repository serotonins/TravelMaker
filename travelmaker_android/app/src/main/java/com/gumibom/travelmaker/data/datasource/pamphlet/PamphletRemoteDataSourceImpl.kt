package com.gumibom.travelmaker.data.datasource.pamphlet

import com.gumibom.travelmaker.data.api.pamphlet.PamphletService
import com.gumibom.travelmaker.data.dto.request.DeleteRecordRequestDTO
import com.gumibom.travelmaker.data.dto.response.IsSuccessResponseDTO
import com.gumibom.travelmaker.data.dto.response.PamphletResponseDTO
import com.gumibom.travelmaker.data.dto.response.RecordResponseDTO
import com.gumibom.travelmaker.model.pamphlet.PamphletItem
import com.gumibom.travelmaker.model.pamphlet.Record
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class PamphletRemoteDataSourceImpl @Inject constructor(
    private val pamphletService: PamphletService
) : PamphletRemoteDataSource {

    override suspend fun makePamphlet(
        image: MultipartBody.Part,
        pamphletRequestDTO: RequestBody
    ): Response<PamphletResponseDTO> {
        return pamphletService.makePamphlet(image, pamphletRequestDTO)
    }

    override suspend fun getMyRecord(userId: Long): Response<List<PamphletItem>> {
        return pamphletService.getMyRecord(userId)
    }

    override suspend fun finishRecordMyPamphlet(pamphletId: Long): Response<IsSuccessResponseDTO> {
        return pamphletService.finishRecordMyPamphlet(pamphletId)
    }

    override suspend fun getAllMyRecord(pamphletId: Long): Response<MutableList<RecordResponseDTO>> {
        return pamphletService.getAllMyRecord(pamphletId)
    }

    override suspend fun makeRecord(
        image: MultipartBody.Part?,
        video: MultipartBody.Part?,
        makeRecordRequestDTO: RequestBody
    ): Response<IsSuccessResponseDTO> {
        return pamphletService.makeRecord(image, video, makeRecordRequestDTO)
    }

    override suspend fun deleteRecord(deleteRecordRequestDTO: DeleteRecordRequestDTO): Response<IsSuccessResponseDTO> {
        return pamphletService.deleteRecord(deleteRecordRequestDTO)
    }

    override suspend fun getOtherPamphlet(userId: Long): Response<List<PamphletItem>> {
        return pamphletService.getOtherPamphlet(userId)
    }
}