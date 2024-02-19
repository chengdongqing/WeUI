package top.chengdongqing.weui.ui.views.network.request

import androidx.lifecycle.ViewModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartViewModel : ViewModel() {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://m.mi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val cartService by lazy { retrofit.create(CartService::class.java) }

    suspend fun fetchCount(): Response<CartCountResponse> {
        return cartService.fetchCount("https://m.mi.com/?spmref=MiShop_M.cms_19106.3814597.1&scmref=cms.0.0.0.0.0.0.0")
    }
}