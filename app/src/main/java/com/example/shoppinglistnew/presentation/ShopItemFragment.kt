package com.example.shoppinglistnew.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglistnew.R
import com.example.shoppinglistnew.databinding.FragmentShopItemBinding
import com.example.shoppinglistnew.domain.ShopItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class ShopItemFragment : Fragment() {
    private lateinit var binding: FragmentShopItemBinding
    private lateinit var shopItemViewModel: ShopItemViewModel
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        textChangeListener()
        observeViewModel()
        launchRightMode()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener){
            onEditingFinishedListener=context
        }else{
            throw RuntimeException("Activity must implement OnEditingFinishListener")
        }
    }

    private fun observeViewModel() {
        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            binding.tilCount.error = message
        }
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = message
        }
    }
    private fun parseParams() {
        val args=requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id  is absent ")
            }
            shopItemId =
                args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }
    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun textChangeListener() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                shopItemViewModel.resetInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                shopItemViewModel.resetInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchEditMode() {
        shopItemViewModel.getShopItem(shopItemId)
        shopItemViewModel.shopItem.observe(viewLifecycleOwner) {
            binding.etName.setText(it.name)
            binding.etCount.setText(it.count.toString())
        }
        binding.btnSave.setOnClickListener {
            shopItemViewModel.editShopItem(binding.etName.text?.toString(),
                binding.etCount.text?.toString())
        }
        shopItemViewModel.closeWindow.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun launchAddMode() {
        binding.btnSave.setOnClickListener {
            shopItemViewModel.addShopItem(binding.etName.text?.toString(),
                binding.etCount.text?.toString())
        }
        shopItemViewModel.closeWindow.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }
    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }


    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        const val MODE_UNKNOWN = ""

        @JvmStatic
        fun newInstanceAdd() =
            ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        fun newInstanceEdit(id:Int)=ShopItemFragment().apply {
            arguments=Bundle().apply {
                putString(SCREEN_MODE, MODE_EDIT)
                putInt(SHOP_ITEM_ID,id)
            }
        }

    }
}