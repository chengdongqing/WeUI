package top.chengdongqing.weui.feature.network.screens.upload.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object RetrofitManger {
    private const val BASE_URL = "https://unidemo.dcloud.net.cn/"

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}