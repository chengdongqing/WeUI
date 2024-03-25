package top.chengdongqing.weui.feature.network.screens.request.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import top.chengdongqing.weui.feature.network.screens.request.data.model.CartItemCount

interface CartService {
    @GET("cart/count")
    suspend fun fetchCount(
        @Header("Referer") referer: String
    ): Response<CartItemCount>
}