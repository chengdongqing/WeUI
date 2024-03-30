package top.chengdongqing.weui.feature.network.request

import androidx.lifecycle.ViewModel
import top.chengdongqing.weui.feature.network.request.data.model.RecommendItem
import top.chengdongqing.weui.feature.network.request.data.model.Result
import top.chengdongqing.weui.feature.network.request.data.repository.CartRepositoryImpl

class CartViewModel : ViewModel() {
    private val cartRepository by lazy {
        CartRepositoryImpl()
    }

    suspend fun fetchRecommendProducts(): Result<List<RecommendItem>>? {
        return cartRepository.fetchRecommendProducts()
    }
}