package com.app.valhalla.ui.main.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.databinding.DialogItemsBinding
import com.app.valhalla.databinding.ItemholderBinding


class ItemFragment(
    private var itemList: List<GameObject>,
) : DialogFragment() {

    private lateinit var binding: DialogItemsBinding

    init {
        Log.d("TAG", "itemList=$itemList")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogItemsBinding.inflate(inflater, container, false)
        Log.d("TAG", "ItemFragmentonCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGridRecyclerView()
    }

    private fun initGridRecyclerView() {
        binding.gridRecyclerView.apply {
            adapter = ItemAdapter(itemList)
            layoutManager = GridLayoutManager(context, 6)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

}

class ItemAdapter(private var itemList: List<GameObject>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {


        val binding = ItemholderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.name.text = itemList[0].name
    }


    override fun getItemCount(): Int {
        return 33
    }

    inner class ItemViewHolder(val binding: ItemholderBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}
