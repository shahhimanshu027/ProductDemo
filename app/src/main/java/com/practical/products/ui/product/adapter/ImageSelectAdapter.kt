package com.practical.products.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.practical.products.databinding.ItemSelectedImagesBinding
import com.practical.products.ui.product.adapter.ViewPagerAdapter.ViewHolder

/**
 * This class is responsible for converting each data entry [String]
 * into [ViewHolder] that can then be added to the AdapterView for image selection recyclerView
 */
class ImageSelectAdapter(val deleteSelectedImage: DeleteSelectedImage) :
    ListAdapter<String, ImageSelectAdapter.ItemViewHolder>(
        DiffCallback(),
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemSelectedImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: ItemSelectedImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: String) = binding.apply {
            ivProduct.load(uri)
            ivDelImage.setOnClickListener {
                deleteSelectedImage.onDelete(uri)
            }
        }
    }
    override fun getItemCount(): Int = currentList.size

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }
}
interface DeleteSelectedImage {
    fun onDelete(uri: String)
}

