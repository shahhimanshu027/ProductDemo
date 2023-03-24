package com.practical.products.domain.usecase

import com.practical.products.domain.repository.ProductRepository
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of
 * [com.practical.products.ui.product.viewmodel.ProductViewModel] (which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    fun getProduct() = productRepository.getProduct()
}
