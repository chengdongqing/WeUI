package top.chengdongqing.weui.feature.network.screens.request.repository

import retrofit2.Response
import top.chengdongqing.weui.feature.network.screens.request.CartItemCount

interface CartRepository {
    suspend fun fetchCount(referer: String): Response<CartItemCount>
}