package top.chengdongqing.weui.feature.network.screens.request

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface CartService {
    @GET("cart/count")
    suspend fun fetchCount(@Header("Referer") referer: String): Response<CartCountResponse>
}

data class CartCountResponse(
    val code: Int,
    val data: Int,
    val result: String
)