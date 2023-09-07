package woowacourse.shopping.data.repository

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return dao.getAll().toDomain()
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
