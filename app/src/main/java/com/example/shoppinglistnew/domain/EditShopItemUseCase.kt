package com.example.shoppinglistnew.domain

class EditShopItemUseCase(private val shopItemRepositoty: ShopItemRepositoty) {
    fun editShopItem(shopItem: ShopItem){
        shopItemRepositoty.editShopItem(shopItem)
    }
}