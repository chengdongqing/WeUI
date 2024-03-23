package top.chengdongqing.weui.feature.system.address.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @get:Query("SELECT * FROM address")
    val addressList: Flow<List<Address>>

    @Query("select * from address where id = :id")
    suspend fun loadById(id: Int): Address?

    @Insert
    suspend fun insert(address: Address)

    @Update
    suspend fun update(address: Address)

    @Delete
    suspend fun delete(address: Address)
}
