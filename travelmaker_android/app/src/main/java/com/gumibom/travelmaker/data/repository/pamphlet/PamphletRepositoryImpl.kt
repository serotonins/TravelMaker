package com.gumibom.travelmaker.data.repository.pamphlet

import com.gumibom.travelmaker.data.datasource.pamphlet.PamphletRemoteDataSource
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

class PamphletRepositoryImpl @Inject constructor(
    private val pamphletRemoteDataSourceImpl: PamphletRemoteDataSource
) : PamphletRepository{

    override suspend fun makePamphlet(
        image: MultipartBody.Part,
        pamphletRequestDTO: RequestBody
    ): Response<PamphletResponseDTO> {
        return pamphletRemoteDataSourceImpl.makePamphlet(image, pamphletRequestDTO)
    }

    override suspend fun getMyRecord(userId: Long): Response<List<PamphletItem>> {
        return pamphletRemoteDataSourceImpl.getMyRecord(userId)
    }

    override suspend fun finishRecordMyPamphlet(pamphletId: Long): Response<IsSuccessResponseDTO> {
        return pamphletRemoteDataSourceImpl.finishRecordMyPamphlet(pamphletId)
    }

    override suspend fun getAllMyRecord(pamphletId: Long): Response<MutableList<RecordResponseDTO>> {
        return pamphletRemoteDataSourceImpl.getAllMyRecord(pamphletId)
    }

    override suspend fun makeRecord(
        image: MultipartBody.Part?,
        video: MultipartBody.Part?,
        makeRecordRequestDTO: RequestBody
    ): Response<IsSuccessResponseDTO> {
        return pamphletRemoteDataSourceImpl.makeRecord(image, video, makeRecordRequestDTO)
    }

    override suspend fun deleteRecord(deleteRecordRequestDTO: DeleteRecordRequestDTO): Response<IsSuccessResponseDTO> {
        return pamphletRemoteDataSourceImpl.deleteRecord(deleteRecordRequestDTO)
    }

    override suspend fun getOtherPamphlet(userId: Long): Response<List<PamphletItem>> {
        return pamphletRemoteDataSourceImpl.getOtherPamphlet(userId)
    }
}