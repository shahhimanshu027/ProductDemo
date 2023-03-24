package com.practical.products.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practical.products.databinding.ItemImagesBinding

/**
 * This class is responsible for converting each data entry [String]
 * into [ViewHolder] that can then be added to the AdapterView for ViewPager
 */
internal class ViewPagerAdapter(val images: MutableList<String>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(private val binding: ItemImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(itemData: String) = binding.apply {
            binding.imageUrl = itemData
            binding.executePendingBindings()
        }
    }
}
