package com.practical.products.ui.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practical.products.data.common.HandleResult
import com.practical.products.data.response.ProductData
import com.practical.products.domain.usecase.GetProductUseCase
import com.practical.products.ui.product.adapter.ProductAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A helper class for the UI controller that is responsible for
 * preparing data for [ProductViewModel] as the UI
 */
@HiltViewModel
class ProductViewModel @Inject constructor(private var getCarsUseCase: GetProductUseCase) : ViewModel() {

    private val _resultCars = Channel<HandleResult<List<ProductData>>>(Channel.BUFFERED)
    val resultProduct: Flow<HandleResult<List<ProductData>>> = _resultCars.receiveAsFlow()
    var productList = mutableListOf<ProductData>()
    var addProductList = mutableListOf<ProductData>()
    var productImages = mutableListOf<String>()
    var viewType: ProductAdapter.ViewType? = null

    fun getProduct() = viewModelScope.launch {
        getCarsUseCase.getProduct().collect { _resultCars.send(it) }
    }
}
