package top.chengdongqing.weui.feature.system.address.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AddressRepositoryImpl(context: Context) : AddressRepository {
    private val database by lazy { AddressDatabase.getInstance(context) }

    override val addressList: Flow<List<Address>>
        get() = database.addressDao().addressList

    override suspend fun loadById(id: Int): Address? {
        return withContext(Dispatchers.IO) { database.addressDao().loadById(id) }
    }

    override suspend fun insert(address: Address) {
        withContext(Dispatchers.IO) { database.addressDao().insert(address) }
    }

    override suspend fun update(address: Address) {
        withContext(Dispatchers.IO) { database.addressDao().update(address) }
    }

    override suspend fun delete(address: Address) {
        withContext(Dispatchers.IO) { database.addressDao().delete(address) }
    }
}