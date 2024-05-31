package com.gumibom.travelmaker.domain.pamphlet

import com.gumibom.travelmaker.data.repository.pamphlet.PamphletRepository
import com.gumibom.travelmaker.model.pamphlet.PamphletItem
import javax.inject.Inject

class GetMyRecordUseCase @Inject constructor(
    private val pamphletRepositoryImpl: PamphletRepository
) {

    suspend fun getMyRecord(userId : Long) : List<PamphletItem> {
        val response = pamphletRepositoryImpl.getMyRecord(userId)
        var pamphletList = listOf<PamphletItem>()
        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                pamphletList = convertData(body)
            }
        }

        return pamphletList
    }

    private fun convertData(pamphlets: List<PamphletItem>) : List<PamphletItem> {
        val pamphletList = pamphlets
        var newPamphletList = mutableListOf<PamphletItem>()

        for (pamphlet in pamphletList) {
            val time = convertTime(pamphlet.createTime)

            val newPamphlet = PamphletItem(
                pamphlet.pamphletId,
                pamphlet.nickname,
                pamphlet.title,
                pamphlet.love,
                time,
                pamphlet.isFinish,
                pamphlet.repreImgUrl,
                pamphlet.categories
            )

            newPamphletList.add(newPamphlet)
        }

        return newPamphletList
    }

    /**
     * "2024-02-09T07:07:25.672Z" -> 2024-02-09
     */
    private fun convertTime(time : String) : String {
        return time.split("T")[0]
    }

}