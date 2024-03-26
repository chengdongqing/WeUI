package top.chengdongqing.weui.feature.network.request.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.feature.network.request.data.model.RecommendItem
import top.chengdongqing.weui.feature.network.request.data.model.Result
import top.chengdongqing.weui.feature.network.request.retrofit.CartService
import top.chengdongqing.weui.feature.network.request.retrofit.RetrofitManger

class CartRepositoryImpl : CartRepository {
    private val cartService by lazy {
        RetrofitManger.retrofit.create(CartService::class.java)
    }

    override suspend fun fetchRecommendProducts(): Result<List<RecommendItem>> {
        return withContext(Dispatchers.IO) {
            cartService.fetchRecommendProducts()
        }
    }
}