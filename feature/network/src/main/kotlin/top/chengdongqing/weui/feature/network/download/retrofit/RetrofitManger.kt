package top.chengdongqing.weui.feature.network.download.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object RetrofitManger {
    private const val BASE_URL = "https://s1.xiaomiev.com/activity-outer-assets/web/home/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}