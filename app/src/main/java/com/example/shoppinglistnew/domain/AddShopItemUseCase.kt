package com.example.shoppinglistnew.domain

class AddShopItemUseCase(private val shopItemRepositoty: ShopItemRepositoty) {
    fun addShopItem(shopItem: ShopItem){
        shopItemRepositoty.addShopItem(shopItem)
    }
}