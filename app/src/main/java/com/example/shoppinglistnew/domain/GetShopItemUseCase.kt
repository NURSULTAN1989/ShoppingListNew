package com.example.shoppinglistnew.domain

class GetShopItemUseCase(private val shopItemRepositoty: ShopItemRepositoty) {
    fun getShopItem(shopItemid:Int):ShopItem{
        return shopItemRepositoty.getShopItem(shopItemid)
    }
}