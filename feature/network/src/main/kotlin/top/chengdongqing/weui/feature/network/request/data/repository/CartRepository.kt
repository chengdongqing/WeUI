package top.chengdongqing.weui.feature.network.request.data.repository

import top.chengdongqing.weui.feature.network.request.data.model.RecommendItem
import top.chengdongqing.weui.feature.network.request.data.model.Result

interface CartRepository {
    suspend fun fetchRecommendProducts(): Result<List<RecommendItem>>
}