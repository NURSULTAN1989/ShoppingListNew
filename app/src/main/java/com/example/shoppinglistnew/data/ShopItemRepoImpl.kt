package com.example.shoppinglistnew.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglistnew.domain.ShopItem
import com.example.shoppinglistnew.domain.ShopItemRepositoty
import java.lang.RuntimeException
import kotlin.random.Random

object ShopItemRepoImpl : ShopItemRepositoty {
    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    init {
        for (i in 0 until 300) {
            val item = ShopItem("Name", i, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        val shopItem_n= getShopItem(shopItem.id)
        shopList.remove(shopItem_n)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        var oldItem = shopItem
        deleteShopItem(shopItem)
        addShopItem(oldItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Not found Shopitem with id ${shopItemId}")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    fun updateList() {

        shopListLD.value = shopList.toList().sortedBy { it.id }

    }
}