package woowacourse.shopping.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.common.di.qualifier.InMemoryCartRepositoryQualifier
import woowacourse.shopping.ui.common.di.qualifier.InMemoryProductRepositoryQualifier

class MainViewModel(
    @InMemoryProductRepositoryQualifier
    private val productRepository: ProductRepository,
    @InMemoryCartRepositoryQualifier
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> get() = _products

    private val _onProductAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    val onProductAdded: LiveData<Boolean> get() = _onProductAdded

    fun addCartProduct(product: Product) {
        viewModelScope.launch {
            cartRepository.addCartProduct(product)
            _onProductAdded.value = true
        }
    }

    fun fetchAllProducts() {
        _products.value = productRepository.getAllProducts()
    }
}
