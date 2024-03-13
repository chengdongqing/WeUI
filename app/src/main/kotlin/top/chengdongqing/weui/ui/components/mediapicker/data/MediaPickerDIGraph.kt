package top.chengdongqing.weui.ui.components.mediapicker.data

import android.content.Context

object MediaPickerDIGraph {
    fun createMediaPickerRepository(context: Context): MediaPickerRepository {
        return MediaPickerRepositoryImpl(context)
    }
}