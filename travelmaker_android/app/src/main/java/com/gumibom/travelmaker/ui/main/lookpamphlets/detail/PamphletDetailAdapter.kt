package com.gumibom.travelmaker.ui.main.lookpamphlets.detail

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.gumibom.travelmaker.R
import com.gumibom.travelmaker.databinding.ItemPamphletDetailBinding
import com.gumibom.travelmaker.model.pamphlet.Record
import com.gumibom.travelmaker.ui.main.myrecord.detail.MyRecordDetailDiffUtil
import com.gumibom.travelmaker.ui.main.myrecord.detail.MyRecordDetailFragment
import com.gumibom.travelmaker.ui.main.myrecord.detail.MyRecordDetailViewModel

class PamphletDetailAdapter(private val context : Context, private val viewModel : MyRecordDetailViewModel) : ListAdapter<Record, PamphletDetailAdapter.PamphletDetailViewHolder>(MyRecordDetailDiffUtil()) {

    private val items: List<Record>
        get() = currentList
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private var player : SimpleExoPlayer? = null

    inner class PamphletDetailViewHolder(private val binding : ItemPamphletDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Record, position : Int, listSize : Int) {
            // 이미지 일 경우
            if (item.imgUrl.isNotEmpty() && item.videoUrl.isEmpty()) {
                binding.playerViewPamphlet.visibility = View.INVISIBLE

                setImageItem(item)
            }
            // 비디오 일 경우
            else if (item.imgUrl.isNotEmpty() && item.videoUrl.isNotEmpty()) {
                binding.ivPamphletDetail.visibility = View.INVISIBLE
                binding.playerViewPamphlet.visibility = View.VISIBLE
                initializePlayer(item.videoUrl)
            }

            binding.tvPamphletDetailText.text = item.text
            binding.tvItemPamphletDetailPage.text = "${position+1} / $listSize"
        }

        /**
         * 이미지를 렌더링 하는 함수
         */
        private fun setImageItem(record : Record) {
            Glide.with(context)
                .load(record.imgUrl)
                .into(binding.ivPamphletDetail)

            Glide.with(context)
                .load(MyRecordDetailFragment.emojiDrawableId[record.emoji])
                .into(binding.ivPamphletDetailEmoji)

        }

        /**
         * 여기부터  Exoplayer 설정 함수
         */
        fun initializePlayer(uri: String) {
            player = SimpleExoPlayer.Builder(context)
                .build()
                .also { exoPlayer ->
                    binding.playerViewPamphlet.player = exoPlayer

                    val mediaItem = MediaItem.fromUri(uri)
                    exoPlayer.setMediaItem(mediaItem)

                    exoPlayer.playWhenReady = playWhenReady
                    exoPlayer.seekTo(currentWindow, playbackPosition)
                    exoPlayer.prepare()
                    exoPlayer.play()
                }
        }

        fun releasePlayer() {
            player?.run {
//                playbackPosition = this.currentPosition
//                currentWindow = this.currentWindowIndex
//                playWhenReady = this.playWhenReady
                stop() // 플레이어를 정지합니다.
                seekTo(0) // 재생 위치를 0으로 설정합니다.
                playbackPosition = 0 // 재생 위치 변수를 0으로 재설정합니다.
                currentWindow = 0 // 현재 윈도우 인덱스를 0으로 재설정합니다.
                release()
            }
            player = null
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PamphletDetailViewHolder {
        val binding = ItemPamphletDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PamphletDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PamphletDetailViewHolder, position: Int) {
        holder.bind(getItem(position), position, itemCount)
    }

    override fun onViewRecycled(holder: PamphletDetailViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer()
    }

    fun getItemAtPosition(position: Int): Record? = items.getOrNull(position)


    companion object {
        val emojiDrawableId = mapOf<String, Int>("HAPPY" to R.drawable.happy, "SMILE" to R.drawable.smile, "SOSO" to R.drawable.soso,
            "SAD" to R.drawable.sad, "ANGRY" to R.drawable.angry)
    }
}