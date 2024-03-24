package top.chengdongqing.weui.feature.system.address

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import top.chengdongqing.weui.feature.system.address.repository.Address
import top.chengdongqing.weui.feature.system.address.repository.AddressRepositoryImpl

class AddressViewModel(application: Application) : AndroidViewModel(application) {
    private val addressRepository by lazy {
        AddressRepositoryImpl(application)
    }
    val addressList by lazy { addressRepository.addressList }

    suspend fun insert(address: Address) {
        return addressRepository.insert(address)
    }

    suspend fun update(address: Address) {
        return addressRepository.update(address)
    }

    suspend fun delete(address: Address) {
        return addressRepository.delete(address)
    }

    suspend fun loadById(id: Int): Address? {
        return addressRepository.loadById(id)
    }
}