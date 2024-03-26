package top.chengdongqing.weui.feature.network.request.retrofit

import retrofit2.http.GET
import retrofit2.http.Header
import top.chengdongqing.weui.feature.network.request.data.model.RecommendItem
import top.chengdongqing.weui.feature.network.request.data.model.Result

interface CartService {
    @GET("rec/cartempty")
    suspend fun fetchRecommendProducts(
        @Header("Referer") referer: String = "https://www.mi.com"
    ): Result<List<RecommendItem>>
}