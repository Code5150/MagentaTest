package com.vladislav.magentatest.adapters

import android.animation.LayoutTransition
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.size.Scale
import com.vladislav.magentatest.Builders
import com.vladislav.magentatest.R
import com.vladislav.magentatest.data.Photo
import com.vladislav.magentatest.network.ApiInterface
import com.vladislav.magentatest.viewmodels.PhotoList

class ImageListRecyclerAdapter(
    private var items: PhotoList,
    private val screenWidth: Int,
    private val onClickFun: (Photo, Drawable) -> Unit
) : RecyclerView.Adapter<ImageListRecyclerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {
        val imageView: ImageView = v.findViewById(R.id.image_view)
        val card: CardView = v.findViewById(R.id.card)
        val likeButton: ImageButton = v.findViewById(R.id.like_button)
        private val imageLayout: ConstraintLayout = v.findViewById(R.id.image_layout)
        var itemPosition: Int = 0

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
            imageLayout.layoutTransition.apply {
                setAnimateParentHierarchy(false)
                enableTransitionType(LayoutTransition.CHANGING)
            }
        }

        override fun onClick(v: View?) {
            items[itemPosition].liked = !items[itemPosition].liked
            likeButton.visibility = changeLikeVisibility(itemPosition)
            onClickFun(items[itemPosition], imageView.drawable)
        }

        override fun onLongClick(v: View?): Boolean {
            TODO("Not yet implemented")
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
                items[position].id, items[position].width/4, items[position].height/4
            ) /*items[position].downloadUrl*/, Builders.imageLoader
        ) {
            crossfade(true)
            placeholder(R.drawable.placeholder_24)
            memoryCachePolicy(CachePolicy.ENABLED)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            listener { req, metadata ->
                memoryCacheKey(metadata.memoryCacheKey)
                scale(Scale.FIT)
                holder.likeButton.visibility = changeLikeVisibility(position)
            }
        }
        Log.d("Holder w:", holder.card.width.toString())
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: PhotoList) {
        val diffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(items, newItems)
        )
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ItemDiffCallback(
        private val oldList: PhotoList,
        private val newList: PhotoList
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    private inline fun createImageUrl(id: Int, width: Int, height: Int) =
        ApiInterface.BASE_URL + ApiInterface.ID + "/" + id + "/" + width + "/" + height

    private inline fun changeLikeVisibility(pos: Int) =
        when(items[pos].liked){
            true -> View.VISIBLE
            false -> View.GONE
        }
}