package com.app.valhalla.ui.main.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.valhalla.R
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.databinding.ItemholderBinding
import com.app.valhalla.databinding.RvDialogItemsBinding
import com.app.valhalla.ui.main.MainViewModel
import com.app.valhalla.util.Constant
import com.app.valhalla.util.FontUtil
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject


class ItemFragment(
    private var itemList: List<GameObject>,
    private val listener: OnDialogItemClickListener
) : DialogFragment() {

    private lateinit var binding: RvDialogItemsBinding
    private val mainViewModel: MainViewModel by inject()

    init {
        Log.d("TAG", "itemList=$itemList")
    }

    override fun onResume() {
        super.onResume()
//        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RvDialogItemsBinding.inflate(inflater, container, false)
        Log.d("TAG", "ItemFragmentCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagerView()
        initRadioGroup()
    }

    private fun initPagerView() {
        binding.viewPager2.apply {
            adapter = ItemAdapter(itemList, context, this@ItemFragment, listener)
        }
    }

    private fun initRadioGroup() {
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, id ->
          when(id){
              binding.radioOption1.id->{
                  this.mainViewModel.getTypeList(Constant.OBJ_JOSS)
                  togglePagerView(this.mainViewModel.getTypeList(Constant.OBJ_JOSS))
              }
              binding.radioOption2.id->{
                  this.mainViewModel.getTypeList(Constant.OBJ_VASE)
                  togglePagerView(this.mainViewModel.getTypeList(Constant.OBJ_VASE))
              }
              binding.radioOption3.id->{
                  this.mainViewModel.getTypeList(Constant.OBJ_CANDLE_ID)
                  togglePagerView(this.mainViewModel.getTypeList(Constant.OBJ_CANDLE_ID))
              }
              binding.radioOption4.id->{
                  this.mainViewModel.getTypeList(Constant.OBJ_INCENSE_BURNER_ID)
                  togglePagerView(this.mainViewModel.getTypeList(Constant.OBJ_INCENSE_BURNER_ID))
              }
              binding.radioOption5.id->{
                  this.mainViewModel.getTypeList(Constant.OBJ_JOSS_BACKGROUND_ID)
                  togglePagerView(this.mainViewModel.getTypeList(Constant.OBJ_JOSS_BACKGROUND_ID))
              }
          }
        }
        binding.radioGroup.check(  binding.radioOption1.id)
    }

    private fun togglePagerView(list :List<GameObject>) {
        binding.viewPager2.apply {
            (adapter as ItemAdapter).setNewList(list)
        }
    }



    interface OnDialogItemClickListener {
        fun onDialogItemClick(itemType: String, itemId: String, dialog: DialogFragment)
    }

}

class ItemAdapter(
    private var itemList: List<GameObject>,
    val context: Context,
    private val dialog: DialogFragment,
    private val listener: ItemFragment.OnDialogItemClickListener
) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    var oldSelectedIndex :Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemholderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.layoutParams.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        return ItemViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.isUsing.text = if(itemList[position].is_default) "使用中" else "點擊以使用"
        if(itemList[position].is_default) {
            oldSelectedIndex = position
        }
        holder.binding.name.text = itemList[position].name
        Glide.with(context).load(itemList[position].imgUrl()).into(holder.binding.itemImg)
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setNewList(list :List<GameObject>) {
        itemList = list
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(val binding: ItemholderBinding, context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.name.typeface = FontUtil.f_chinese_traditional(context)
            binding.itemImg.setOnClickListener {
                itemList[layoutPosition].is_default = !itemList[layoutPosition].is_default
                itemList[oldSelectedIndex].is_default = !itemList[oldSelectedIndex].is_default
                notifyItemChanged(layoutPosition)
                notifyItemChanged(oldSelectedIndex)
                oldSelectedIndex = layoutPosition
            }
        }
    }
}
