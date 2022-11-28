package com.example.shoppinglistnew.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {
    private var screenMode = ShopItemActivity.MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        launchRightMode()


    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEdit(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAdd()
            else ->throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(ShopItemActivity.EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(ShopItemActivity.EXTRA_SCREEN_MODE)
        if (mode != ShopItemActivity.MODE_EDIT && mode != ShopItemActivity.MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == ShopItemActivity.MODE_EDIT) {
            if (!intent.hasExtra(ShopItemActivity.EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id  is absent ")
            }
            shopItemId =
                intent.getIntExtra(ShopItemActivity.EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        const val MODE_UNKNOWN = ""


        fun newIntenAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, id: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, id)
            return intent
        }
    }
}