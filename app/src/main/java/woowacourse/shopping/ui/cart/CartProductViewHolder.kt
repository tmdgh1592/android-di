package woowacourse.shopping.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartProductBinding
import woowacourse.shopping.model.CartProduct

class CartProductViewHolder(
    private val binding: ItemCartProductBinding,
    dateFormatter: DateFormatter,
    onClickDelete: (id: Long, position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.dateFormatter = dateFormatter
        binding.ivCartProductDelete.setOnClickListener {
            onClickDelete(binding.item!!.id, adapterPosition)
        }
    }

    fun bind(cartProduct: CartProduct) {
        binding.item = cartProduct
    }

    companion object {
        fun from(
            parent: ViewGroup,
            dateFormatter: DateFormatter,
            onClickDelete: (id: Long, position: Int) -> Unit,
        ): CartProductViewHolder {
            val binding = ItemCartProductBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return CartProductViewHolder(binding, dateFormatter, onClickDelete)
        }
    }
}
