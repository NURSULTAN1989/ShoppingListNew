package com.example.shoppinglistnew.domain

import androidx.lifecycle.LiveData

class GetShopListUseCase(private val shopItemRepositoty: ShopItemRepositoty) {
    fun getShopList(): LiveData<List<ShopItem>> {
       return shopItemRepositoty.getShopList()
    }
}