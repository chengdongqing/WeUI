package top.chengdongqing.weui.feature.network.screens.request.data.repository

import retrofit2.Response
import top.chengdongqing.weui.feature.network.screens.request.data.model.CartItemCount

interface CartRepository {
    suspend fun fetchCount(referer: String): Response<CartItemCount>
}