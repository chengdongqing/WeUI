package top.chengdongqing.weui.util

import androidx.room3.Room
import androidx.room3.RoomDatabase
import top.chengdongqing.weui.androidAppInstance
import top.chengdongqing.weui.system.address.repository.AddressDatabase

actual fun getRoomDatabaseBuilder(): RoomDatabase.Builder<AddressDatabase> {
    val dbFile = androidAppInstance.getDatabasePath("my_room.db")
    return Room.databaseBuilder<AddressDatabase>(dbFile.absolutePath)
}