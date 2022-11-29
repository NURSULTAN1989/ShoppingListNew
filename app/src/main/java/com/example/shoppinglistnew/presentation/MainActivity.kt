package com.example.shoppinglistnew.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.annotations.NotNull

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemContainer:FragmentContainerView?=null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        var count = 0
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shopItemContainer=binding.shopItemContainer
        setupRecylerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
        val buttonAddItem=binding.btnAddShopItem
        buttonAddItem.setOnClickListener{
            if(shopItemContainer !=null){
                val fragment=ShopItemFragment.newInstanceAdd()
                launchFragment(fragment)
            }else {
                val intent = ShopItemActivity.newIntenAddItem(this)
                startActivity(intent)
            }
        }
    }

    private fun setupRecylerView() {
        val rvShopList = binding.rvShopList
        shopListAdapter = ShopListAdapter()
        rvShopList.adapter = shopListAdapter
        with(rvShopList) {
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupOnLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deletShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupOnLongClickListener() {
        shopListAdapter.onLongClickListener = {
            viewModel.changeEnabledShopItem(it)
        }
    }

    private fun setupClickListener() {
        shopListAdapter.onClickListener = {
            if(shopItemContainer !=null){
                val fragment=ShopItemFragment.newInstanceEdit(it.id)
                launchFragment(fragment)
            }else{
                val intent=ShopItemActivity.newIntentEditItem(this,it.id)
                startActivity(intent)
            }

        }
    }

    private fun launchFragment(fragment: ShopItemFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onEditingFinished() {
        Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }


}