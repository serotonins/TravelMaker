package com.gumibom.travelmaker.domain.pamphlet

import android.util.Log
import com.gumibom.travelmaker.data.dto.response.RecordResponseDTO
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepository
import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepositoryImpl
import com.gumibom.travelmaker.model.pamphlet.Record
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "GetAllMyRecordUseCase_싸피"
class GetAllMyRecordUseCase @Inject constructor(
    private val pamphletRepositoryImpl: PamphletRepository
) {

    suspend fun getAllMyRecord(pamphletId: Long) : MutableList<Record> {
        val response = pamphletRepositoryImpl.getAllMyRecord(pamphletId)
        var recordList = mutableListOf<Record>()

        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                recordList = recordNullCheck(body)
            }
        }
        Log.d(TAG, "getAllMyRecord: $recordList")
        return recordList
    }

    private fun recordNullCheck(recordResponseDTO: MutableList<RecordResponseDTO>) : MutableList<Record>{
        val recordList = mutableListOf<Record>()

        if (recordResponseDTO.isEmpty()) {
            return recordList // 빈 리스트 반환
        }

        recordResponseDTO.mapTo(recordList) { dto ->
            Record(
                recordId = dto.recordId,
                title = dto.title,
                createTime = convertTime(dto.createTime),
                imgUrl = dto.imgUrl ?: "",
                videoUrl = dto.videoUrl ?: "",
                videoThumbnailUrl = dto.videoThumbnailUrl ?: "",
                text = dto.text,
                emoji = dto.emoji
            )
        }

        return recordList
    }

    /**
     * "2024-02-09T07:07:25.672Z" -> 2024-02-09
     */
    private fun convertTime(time : String) : String {
        return time.split("T")[0]
    }
}