package top.chengdongqing.weui.feature.network.screens.request.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import top.chengdongqing.weui.feature.network.screens.request.CartItemCount

class CartRepositoryImpl : CartRepository {
    private val cartService by lazy {
        RetrofitManger.retrofit.create(CartService::class.java)
    }

    override suspend fun fetchCount(referer: String): Response<CartItemCount> {
        return withContext(Dispatchers.IO) { cartService.fetchCount(referer) }
    }
}