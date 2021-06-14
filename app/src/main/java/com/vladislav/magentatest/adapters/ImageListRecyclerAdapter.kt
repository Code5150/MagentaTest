package com.vladislav.magentatest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.vladislav.magentatest.R
import com.vladislav.magentatest.viewmodels.PhotoList

class ImageListRecyclerAdapter(
    private var items: PhotoList
): RecyclerView.Adapter<ImageListRecyclerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val imageView: ImageView = v.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_view_holder, parent, false
            )
        )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.load(items[position].downloadUrl) {
            crossfade(true)
        }
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
}