package com.practical.products.ui.product.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.practical.products.R
import com.practical.products.data.common.onError
import com.practical.products.data.common.onLoading
import com.practical.products.data.common.onSuccess
import com.practical.products.databinding.FragmentProductBinding
import com.practical.products.ui.product.adapter.ProductAdapter
import com.practical.products.ui.product.viewmodel.ProductViewModel
import com.practical.products.utils.isInternetAvailable
import com.practical.products.utils.showToast
import kotlinx.coroutines.launch

/**
 * Product listing screen
 */
class ProductFragment : Fragment() {

    private val productViewModel: ProductViewModel by activityViewModels()
    private lateinit var mBinding: FragmentProductBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentProductBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initClick()
        setAdapter()
        init()
    }

    /**
     * initialized coroutine and collect product flow data
     */
    private fun init() {
        lifecycleScope.launch {
            productViewModel.resultProduct.collect { handleResult ->
                handleResult.onLoading {
                    showProgress()
                }.onSuccess {
                    if (it.isNotEmpty()) {
                        productViewModel.productList = it.toMutableList()
                        setVerticalList()
                        hideProgress()
                    } else {
                        showNoDataFound()
                    }
                }.onError {
                    it.messageResource?.let { it1 ->
                        showToast(
                            getString(it1),
                            requireContext(),
                        )
                    }
                    showNoDataFound()
                }
            }
        }
        productViewModel.getProduct()
    }

    /**
     * initialized product list recyclerview adapter
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {
        adapter = ProductAdapter(ProductAdapter.ViewType.VERTICAL, arrayListOf())
        mBinding.apply {
            rvProduct.layoutManager = LinearLayoutManager(requireContext())
            rvProduct.adapter = adapter
            btnTryAgain.setOnClickListener {
                if (isInternetAvailable(requireContext())) {
                    productViewModel.getProduct()
                } else {
                    showToast(
                        getString(R.string.label_internet_not_available),
                        requireContext(),
                    )
                }
            }
            ivChangeView.setOnClickListener {
                if (productViewModel.viewType == null || (productViewModel.viewType?.name == ProductAdapter.ViewType.VERTICAL.name)) {
                    setGridList()
                } else {
                    setVerticalList()
                }
            }
        }
    }

    /**
     * recyclerview set grid layout manager
     * update list with enum [ProductAdapter.ViewType.HORIZONTAL] layout type
     * view pager data also set here
     */
    private fun setGridList() {
        mBinding.apply {
            ivChangeView.setImageResource(R.drawable.ic_list_layout)
            productViewModel.viewType = ProductAdapter.ViewType.HORIZONTAL
            rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
            rvProduct.adapter = adapter
            productViewModel.addProductList.forEachIndexed { index, productData ->
                productViewModel.productList.add(index, productData)
            }
            adapter.updateList(
                ProductAdapter.ViewType.HORIZONTAL,
                productViewModel.productList.distinct().toMutableList(),
            )
            showLocalCatchMessage()
        }
    }

    /**
     * recyclerview set linear layout manager for main screen data
     * update list with enum [ProductAdapter.ViewType.VERTICAL] layout type
     */
    private fun setVerticalList() {
        mBinding.apply {
            ivChangeView.setImageResource(R.drawable.ic_grid_layout)
            productViewModel.viewType = ProductAdapter.ViewType.VERTICAL
            rvProduct.layoutManager = LinearLayoutManager(requireContext())
            rvProduct.adapter = adapter
            productViewModel.addProductList.forEachIndexed { index, productData ->
                productViewModel.productList.add(index, productData)
            }
            adapter.updateList(
                ProductAdapter.ViewType.VERTICAL,
                productViewModel.productList.distinct().toMutableList(),
            )
            showLocalCatchMessage()
        }
    }

    private fun initClick() {
        mBinding.apply {
            fabAddProduct.setOnClickListener {
                findNavController().navigate(R.id.addProductFragment)
            }
        }
    }

    /**
     * progress layout will be gone
     * Fab button and changeView icon will be enable
     */
    private fun hideProgress() {
        mBinding.apply {
            fabAddProduct.isEnabled = true
            ivChangeView.isEnabled = true
            progressLayout.visibility = View.GONE
            clNoDataFound.visibility = View.GONE
            rvProduct.visibility = View.VISIBLE
        }
    }

    /**
     * progress layout will be show
     * Fab button and changeView icon will be enable
     */
    private fun showProgress() {
        mBinding.apply {
            fabAddProduct.isEnabled = false
            ivChangeView.isEnabled = false
            progressLayout.visibility = View.VISIBLE
            clNoDataFound.visibility = View.GONE
            rvProduct.visibility = View.GONE
        }
    }

    /**
     * No data found layout will be show if there is no data return from API
     */
    private fun showNoDataFound() {
        mBinding.apply {
            fabAddProduct.isEnabled = true
            ivChangeView.isEnabled = true
            progressLayout.visibility = View.GONE
            clNoDataFound.visibility = View.VISIBLE
            rvProduct.visibility = View.GONE
        }
    }

    private fun showLocalCatchMessage() {
        if (!isInternetAvailable(requireContext())) {
            showToast(
                getString(R.string.no_internet_connection_load_catch),
                requireContext(),
            )
        }
    }
}
