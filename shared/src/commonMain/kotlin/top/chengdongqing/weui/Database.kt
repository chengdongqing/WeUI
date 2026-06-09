package top.chengdongqing.weui

import androidx.room3.RoomDatabase
import top.chengdongqing.weui.system.address.repository.AddressDatabase

expect fun getRoomDatabaseBuilder(): RoomDatabase.Builder<AddressDatabase>