package com.vladislav.magentatest.adapters

import android.animation.LayoutTransition
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.size.Scale
import com.vladislav.magentatest.other.Helpers
import com.vladislav.magentatest.R
import java.io.File

class LikedImagesRecyclerAdapter(
    private var items: List<Int>?,
    private val onClickFun: (Int) -> Unit,
    private val filesDir: File
) : RecyclerView.Adapter<LikedImagesRecyclerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        val imageView: ImageView = v.findViewById(R.id.image_view)
        val likeButton: ImageButton = v.findViewById(R.id.like_button)
        private val imageLayout: ConstraintLayout = v.findViewById(R.id.image_layout)

        var item: Int = 0

        init {
            v.setOnClickListener(this)
            imageLayout.layoutTransition.apply {
                setAnimateParentHierarchy(false)
                enableTransitionType(LayoutTransition.CHANGING)
            }
        }

        override fun onClick(v: View?) {
            likeButton.visibility = View.GONE
            onClickFun(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_view_holder, parent, false
            )
        )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.item = items!![position]
        holder.likeButton.visibility = View.VISIBLE
        holder.imageView.load(
            Helpers.getImage(filesDir, items!![position])
        ) {
            crossfade(true)
            placeholder(R.drawable.placeholder_24)
            memoryCachePolicy(CachePolicy.ENABLED)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            listener { req, metadata ->
                memoryCacheKey(metadata.memoryCacheKey)
                scale(Scale.FIT)
            }
        }
    }

    fun updateItems(newItems: List<Int>) {
        val diffResult = items?.let {
            DiffUtil.calculateDiff(
                ItemDiffCallback(it, newItems)
            )
        } ?:  DiffUtil.calculateDiff(
            ItemDiffCallback(emptyList(), newItems)
        )
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ItemDiffCallback(
        private val oldList: List<Int>,
        private val newList: List<Int>
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

    override fun getItemCount(): Int = items?.size ?: 0
}