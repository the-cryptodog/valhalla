package com.app.valhalla.ui.main.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.databinding.DialogItemsBinding
import com.app.valhalla.databinding.ItemholderBinding
import com.bumptech.glide.Glide


class ItemFragment(
    private var itemList: List<GameObject>,
    private val listener: OnDialogItemClickListener
) : DialogFragment() {

    private lateinit var binding: DialogItemsBinding

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
        binding = DialogItemsBinding.inflate(inflater, container, false)
        Log.d("TAG", "ItemFragmentCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGridRecyclerView()
    }

    private fun initGridRecyclerView() {
        binding.gridRecyclerView.apply {
            adapter = ItemAdapter(itemList, context, this@ItemFragment, listener)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemholderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.name.text = itemList[position].name
        Glide.with(context).load(itemList[position].img_url).into(holder.binding.id)
        holder.itemView.setOnClickListener {
            listener.onDialogItemClick(itemList[position].type, itemList[position].id, dialog)
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ItemViewHolder(val binding: ItemholderBinding) :
        RecyclerView.ViewHolder(binding.root)

}
