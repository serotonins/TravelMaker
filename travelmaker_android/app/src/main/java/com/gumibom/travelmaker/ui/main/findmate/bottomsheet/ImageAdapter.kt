package com.gumibom.travelmaker.ui.main.findmate.bottomsheet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gumibom.travelmaker.databinding.ItemBottomsheetImageBinding
class ImageAdapter(private val imageUrls:List<String>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){
    class ImageViewHolder(val binding: ItemBottomsheetImageBinding)
        : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val binding = ItemBottomsheetImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return ImageViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
//        Glide.with(holder.binding.ivPlace.context).load(imageUrl).into(holder.binding.ivPlace)
        Glide.with(holder.binding.ivPlace.context)
            .load(imageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
            .transform(CenterCrop()) // Apply center crop to maintain aspect ratio
            .into(holder.binding.ivPlace)
    }
    override fun getItemCount(): Int = imageUrls.size
    //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bottomsheet_image, parent, false)
//return ImageViewHolder(view)
//    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val imageView: ImageView = view.findViewById(R.id.iv_place)
//    }
}