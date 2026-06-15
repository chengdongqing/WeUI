package top.chengdongqing.weui.feature.system.address.repository

import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    val addressList: Flow<List<Address>>

    suspend fun loadById(id: Int): Address?

    suspend fun insert(address: Address)

    suspend fun update(address: Address)

    suspend fun delete(address: Address)
}