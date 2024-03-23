package top.chengdongqing.weui.feature.network.screens.download.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object RetrofitManger {
    private const val BASE_URL = "https://s1.xiaomiev.com/activity-outer-assets/web/home/"

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}