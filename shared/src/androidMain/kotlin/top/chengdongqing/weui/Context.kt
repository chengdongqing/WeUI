package top.chengdongqing.weui

import android.app.Application

lateinit var androidAppInstance: Application

fun initializeAndroidApp(app: Application) {
    androidAppInstance = app
}