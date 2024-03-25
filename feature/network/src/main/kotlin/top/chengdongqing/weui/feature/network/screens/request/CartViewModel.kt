package top.chengdongqing.weui.feature.network.screens.request

import androidx.lifecycle.ViewModel
import retrofit2.Response
import top.chengdongqing.weui.feature.network.screens.request.data.model.CartItemCount
import top.chengdongqing.weui.feature.network.screens.request.data.repository.CartRepositoryImpl

class CartViewModel : ViewModel() {
    private val cartRepository by lazy {
        CartRepositoryImpl()
    }

    suspend fun fetchCount(): Response<CartItemCount> {
        return cartRepository.fetchCount("https://m.mi.com/?spmref=MiShop_M.cms_19106.3814597.1&scmref=cms.0.0.0.0.0.0.0")
    }
}