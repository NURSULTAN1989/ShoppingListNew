package com.example.shoppinglistnew.domain

class DeletShopItemUseCase(private val shopItemRepositoty: ShopItemRepositoty) {
    fun deleteShopItem(shopItem: ShopItem){
        shopItemRepositoty.deleteShopItem(shopItem)
    }
}