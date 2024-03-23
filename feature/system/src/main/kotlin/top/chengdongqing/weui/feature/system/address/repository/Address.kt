package top.chengdongqing.weui.feature.system.address.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class Address(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    @ColumnInfo(name = "address_detail")
    val addressDetail: String
)