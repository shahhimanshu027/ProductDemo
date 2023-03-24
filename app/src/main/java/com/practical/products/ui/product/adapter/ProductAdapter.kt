package com.practical.products.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.practical.products.R
import com.practical.products.data.response.ProductData
import com.practical.products.databinding.ItemProductHorizontalBinding
import com.practical.products.databinding.ItemProductVerticalBinding

/**
 * This class is responsible for converting each data entry [ProductData]
 * into [ProductVerticalVH] and [ProductHorizontalVH] that can then be added to the AdapterView.
 */
class ProductAdapter(private var viewType: ViewType, private var list: MutableList<ProductData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == R.layout.item_product_vertical) {
            ProductVerticalVH(ItemProductVerticalBinding.inflate(layoutInflater, parent, false))
        } else {
            ProductHorizontalVH(
                ItemProductHorizontalBinding.inflate(
                    layoutInflater,
                    parent,
                    false,
                ),
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (viewType.name == ViewType.VERTICAL.name) {
            R.layout.item_product_vertical
        } else {
            R.layout.item_product_horizontal
        }
    }

    inner class ProductVerticalVH(private val binding: ItemProductVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindVerticalData(itemData: ProductData) = binding.apply {
            binding.modelData = itemData
            binding.executePendingBindings()
        }
    }

    inner class ProductHorizontalVH(private val binding: ItemProductHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindHorizontalData(itemData: ProductData) = binding.apply {
            binding.apply {
                modelData = itemData
                executePendingBindings()
                viewPager.adapter = ViewPagerAdapter(images = itemData.images.toMutableList())
                TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = list[position]
        if (holder is ProductVerticalVH) {
            holder.bindVerticalData(data)
        } else if (holder is ProductHorizontalVH) {
            holder.bindHorizontalData(data)
        }
    }

    fun updateList(viewType: ViewType, newList: MutableList<ProductData>) {
        val diffCallback = MyDiffCallback(this.list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.list = newList
        this.viewType = viewType
    }

    open class MyDiffCallback(
        private val list: MutableList<ProductData>,
        private val newList: MutableList<ProductData>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = list.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            list[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            list[oldItemPosition] == newList[newItemPosition]
    }

    enum class ViewType {
        VERTICAL, HORIZONTAL
    }
}
