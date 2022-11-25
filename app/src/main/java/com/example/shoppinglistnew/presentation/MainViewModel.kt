package com.example.shoppinglistnew.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglistnew.data.ShopItemRepoImpl
import com.example.shoppinglistnew.domain.*

class MainViewModel : ViewModel() {

    private val repository = ShopItemRepoImpl
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)

    val shopList=getShopListUseCase.getShopList()

    fun addShopItem(shopItem: ShopItem){
        addShopItemUseCase.addShopItem(shopItem)
    }
    fun changeEnabledShopItem(shopItem: ShopItem){
        val newItem=shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)

    }

}