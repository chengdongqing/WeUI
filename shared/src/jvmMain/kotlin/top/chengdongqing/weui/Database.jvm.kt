package top.chengdongqing.weui

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import top.chengdongqing.weui.system.address.repository.AddressDatabase
import java.io.File

actual fun getRoomDatabaseBuilder(): RoomDatabase.Builder<AddressDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    return Room.databaseBuilder<AddressDatabase>(dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
}