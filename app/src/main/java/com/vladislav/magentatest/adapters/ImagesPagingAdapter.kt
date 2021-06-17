package com.vladislav.magentatest.adapters

import android.animation.LayoutTransition
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.size.Scale
import com.vladislav.magentatest.R
import com.vladislav.magentatest.data.Photo
import com.vladislav.magentatest.network.ApiService
import com.vladislav.magentatest.other.PhotoCallback

class ImagesPagingAdapter(
    diffCallback: PhotoCallback,
    private val onClickFun: (Photo, Drawable) -> Unit
): PagingDataAdapter<Photo, ImagesPagingAdapter.ImageViewHolder>(diffCallback) {

    inner class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        val imageView: ImageView = v.findViewById(R.id.image_view)
        val likeButton: ImageButton = v.findViewById(R.id.like_button)
        private val imageLayout: ConstraintLayout = v.findViewById(R.id.image_layout)

        var itemPosition: Int = 0

        init {
            v.setOnClickListener(this)
            imageLayout.layoutTransition.apply {
                setAnimateParentHierarchy(false)
                enableTransitionType(LayoutTransition.CHANGING)
            }
        }

        override fun onClick(v: View?) {
            peek(itemPosition)?.let {
                it.liked = !it.liked
                likeButton.visibility = changeLikeVisibility(itemPosition)
                onClickFun(it, imageView.drawable)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_view_holder, parent, false
            )
        )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.itemPosition = position
        holder.likeButton.visibility = changeLikeVisibility(position)
        holder.imageView.load(
            createImageUrl(
                getItem(position)!!.id,
                getItem(position)!!.width/4,
                getItem(position)!!.height/4
            )
        ) {
            crossfade(true)
            placeholder(R.drawable.placeholder_24)
            memoryCachePolicy(CachePolicy.ENABLED)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            listener { req, metadata ->
                memoryCacheKey(metadata.memoryCacheKey)
                scale(Scale.FIT)
                //holder.likeButton.visibility = changeLikeVisibility(position)
            }
        }
    }

    private inline fun createImageUrl(id: Int, width: Int, height: Int) =
        ApiService.BASE_URL + ApiService.ID + "/" + id + "/" + width + "/" + height

    private inline fun changeLikeVisibility(pos: Int) =
        peek(pos)?.let {
            when(it.liked){
                true -> View.VISIBLE
                false -> View.GONE
            }
        } ?: View.GONE
}