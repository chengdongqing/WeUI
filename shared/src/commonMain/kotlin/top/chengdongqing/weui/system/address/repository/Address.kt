package top.chengdongqing.weui.system.address.repository

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "address")
data class Address(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    @ColumnInfo(name = "address_detail")
    val addressDetail: String
)