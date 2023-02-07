package com.app.valhalla.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.databinding.ActivityMainBinding
import com.app.valhalla.ui.drawlots.DrawLotsActivity
import com.app.valhalla.ui.main.dialog.ItemFragment
import com.app.valhalla.util.*
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity(), OnClickListener, ItemFragment.OnDialogItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initOnClick()

        MobileAds.initialize(this) {}

        binding.adView.loadAd(AdRequest.Builder().build())

        mainViewModel.btnFunction.observe(this, Observer {
            val img = it[Constant.BUTTON_LEFT]!!.imgUrl
            Log.d("TAG", "in activity $img")
            Glide.with(this)
                .load(img)
                .into(binding.btnFunctionOne)
        })

        mainViewModel.dialogGameObj.observe(this, Observer {
            Log.d("TAGS", it.toString())
            initItemDialog(it)
        })

        mainViewModel.defaultGameObjList.observe(this, Observer { list ->
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_TABLE }?.img_url)
                .into(binding.imgTable)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_INCENSE_BURNER_ID }?.img_url)
                .into(binding.imgIncenseBurner)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_INCENSE_ID }?.img_url)
                .into(binding.imgIncense)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_VASE }?.img_url)
                .into(binding.imgVaseRight)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_VASE }?.img_url)
                .into(binding.imgVaseLeft)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_JOSS }?.img_url)
                .into(binding.imgJoss)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_JOSS_BACKGROUND_ID }?.img_url)
                .into(binding.imgJossBackground)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_CANDLE_ID }?.img_url)
                .into(binding.imgCandleRight)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_CANDLE_ID }?.img_url)
                .into(binding.imgCandleLeft)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_FLOWER_ID }?.img_url)
                .into(binding.imgFlowerLeft)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_FLOWER_ID }?.img_url)
                .into(binding.imgFlowerRight)
        })
    }


    private fun initItemDialog(itemList: List<GameObject>) {
        ItemFragment(itemList, this).show(supportFragmentManager, "ItemDialog")
    }


    private fun initOnClick() {
        binding.btnFunctionOne.setOnClickListener(this)
        binding.btnFunctionTwo.setOnClickListener(this)
        binding.btnFunctionThree.setOnClickListener(this)
        binding.imgIncense.setOnClickListener(this)
        binding.imgIncenseBurner.setOnClickListener(this)
        binding.imgCandleRight.setOnClickListener(this)
        binding.imgCandleLeft.setOnClickListener(this)
        binding.imgFlowerLeft.setOnClickListener(this)
        binding.imgFlowerRight.setOnClickListener(this)
        binding.imgJoss.setOnClickListener(this)
        binding.imgJossBackground.setOnClickListener(this)
        binding.imgVaseLeft.setOnClickListener(this)
        binding.imgVaseRight.setOnClickListener(this)
        binding.imgTable.setOnClickListener(this)
        binding.btnFunctionThree.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                binding.btnFunctionOne.id -> {
                    mainViewModel.leftFunctionLaunch()
                }
                binding.btnFunctionTwo.id -> {
                    testMove()
                }
                binding.btnFunctionThree.id -> {
//                    startActivity(Intent(this, DrawLotsActivity::class.java))
                }

                binding.imgTable.id -> {
                    mainViewModel.tableSelected()
                }
                binding.imgIncenseBurner.id -> {
                    mainViewModel.incenseBurnerSelected()
                }
                binding.imgIncense.id -> {
                    mainViewModel.incenseSelected()
                }
                binding.imgVaseLeft.id, binding.imgVaseRight.id -> {
                    mainViewModel.vaseSelected()
                }
                binding.imgCandleLeft.id, binding.imgCandleRight.id -> {
                    mainViewModel.candleSelected()
                }
                binding.imgFlowerLeft.id, binding.imgFlowerRight.id -> {
                    mainViewModel.flowerSelected()
                }
                binding.imgJoss.id -> {
                    mainViewModel.jossSelected()
                }
                binding.imgJossBackground.id -> {
                    mainViewModel.jossBackgroundSelected()
                }
            }
        }
    }

    override fun onDialogItemClick(itemType: String, itemId: String, dialog: DialogFragment) {
        mainViewModel.updateCurrentSelectedItem(itemType ,itemId,)
        dialog.dismiss()
    }


    fun testMove(){
        binding.leftHand.visibility = View.VISIBLE
        binding.leftHand.fadeIn(1000)
        binding.leftHand.moveInFromLeft(1000)

        binding.rightHand.visibility = View.VISIBLE
        binding.rightHand.fadeIn(1000)
        binding.rightHand.moveInFromRight(1000)
    }
}
