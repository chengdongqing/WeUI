package top.chengdongqing.weui.feature.network.request.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class RecommendItem(
    val algorithm: Int,
    val eid: String,
    val info: Product
) {
    data class Product(
        @SerializedName("product_id")
        val productId: Long,
        @SerializedName("category_id")
        val categoryId: Long,
        val image: String,
        @SerializedName("market_price")
        val marketPrice: BigDecimal,
        val price: BigDecimal,
        val name: String,
        @SerializedName("goods_list")
        val goodsList: List<Long>
    )
}
