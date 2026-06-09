package top.chengdongqing.weui.system.address.repository

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
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
