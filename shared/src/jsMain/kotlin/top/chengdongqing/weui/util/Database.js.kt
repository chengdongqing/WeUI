package top.chengdongqing.weui.util

import androidx.room3.Room
import androidx.room3.RoomDatabase
import top.chengdongqing.weui.feature.system.address.repository.AddressDatabase

actual fun getRoomDatabaseBuilder(): RoomDatabase.Builder<AddressDatabase> {
    return Room.databaseBuilder<AddressDatabase>("my_room.db")
}