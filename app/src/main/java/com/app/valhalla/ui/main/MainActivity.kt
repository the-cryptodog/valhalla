package com.app.valhalla.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.app.valhalla.data.api.Network
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.databinding.ActivityMainBinding
import com.app.valhalla.ui.main.dialog.ItemFragment
import com.app.valhalla.util.Constant
import com.app.valhalla.util.FontUtil
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await


class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnFunctionOne.typeface = FontUtil.f_chinese_traditional(context = this)
        binding.btnFunctionTwo.typeface = FontUtil.f_chinese_traditional(context = this)
        binding.btnFunctionThree.typeface = FontUtil.f_chinese_traditional(context = this)
        initOnClick()

        MobileAds.initialize(this) {}

        binding.adView.loadAd(AdRequest.Builder().build())

        mainViewModel.imgIncense.observe(this, Observer {
        })

        mainViewModel.imgIncenseBurner.observe(this, Observer {
        })

        mainViewModel.imgJoss.observe(this, Observer {
        })

        mainViewModel.imgJossBackground.observe(this, Observer {
        })

        mainViewModel.imgCandleLeft.observe(this, Observer {
        })

        mainViewModel.imgCandleRight.observe(this, Observer {

        })

        mainViewModel.imgFlowerRight.observe(this, Observer {

        })

        mainViewModel.imgFlowerLeft.observe(this, Observer {

        })

        mainViewModel.imgVaseLeft.observe(this, Observer {

        })

        mainViewModel.imgVaseRight.observe(this, Observer {

        })

        mainViewModel.imgLeftCouplet.observe(this, Observer {

        })

        mainViewModel.imgRightCouplet.observe(this, Observer {

        })

        mainViewModel.imgUpperCouplet.observe(this, Observer {

        })

        mainViewModel.imgTable.observe(this, Observer {

        })

        mainViewModel.btnFucntionOne.observe(this, Observer {

        })

        mainViewModel.btnFucntionOneImgId.observe(this, Observer {
            binding.btnFunctionOne.setBackgroundResource(it)
        })

        mainViewModel.btnFunctionTwo.observe(this, Observer {

        })

        mainViewModel.btnFucntionOTwoImgId.observe(this, Observer {
            binding.btnFunctionTwo.setBackgroundResource(it)
        })

        mainViewModel.btnFunctionThree.observe(this, Observer {

        })

        mainViewModel.btnFucntionThreeImgId.observe(this, Observer {
            binding.btnFunctionThree.setBackgroundResource(it)
        })

        mainViewModel.itemDialog.observe(this, Observer {
            if (it == Constant.VIEW_OPEN) {
//                val itemList = listOf(GameObject(1, "table"))
//                initItemDialog(itemList)
            }
        })

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val call = Network.apiService.getDefault().await()
                Log.d("TAG", "call internaet " + call.data[0].img_url)
                withContext(Dispatchers.Main) {
                    Glide.with(this@MainActivity)
                        .load(call.data[0].img_url)
                        .into(binding.imgTable)
                    Glide.with(this@MainActivity)
                        .load(call.data[1].img_url)
                        .into(binding.imgIncenseBurner)
                    Glide.with(this@MainActivity)
                        .load(call.data[2].img_url)
                        .into(binding.imgIncense)
                    Glide.with(this@MainActivity)
                        .load(call.data[3].img_url)
                        .into(binding.imgVaseLeft)
                    Glide.with(this@MainActivity)
                        .load(call.data[3].img_url)
                        .into(binding.imgVaseRight)
                    Glide.with(this@MainActivity)
                        .load(call.data[4].img_url)
                        .into(binding.imgJossBackground)
                    Glide.with(this@MainActivity)
                        .load(call.data[5].img_url)
                        .into(binding.imgLeftCouplet)
                    Glide.with(this@MainActivity)
                        .load(call.data[5].img_url)
                        .into(binding.imgRightCouplet)
                    Glide.with(this@MainActivity)
                        .load(call.data[6].img_url)
                        .into(binding.imgCandleRight)
                    Glide.with(this@MainActivity)
                        .load(call.data[6].img_url)
                        .into(binding.imgCandleLeft)
                    Glide.with(this@MainActivity)
                        .load(call.data[7].img_url)
                        .into(binding.imgFlowerLeft)
                    Glide.with(this@MainActivity)
                        .load(call.data[7].img_url)
                        .into(binding.imgFlowerRight)

                }
            }
        }
    }

    private fun initItemDialog(itemList: List<GameObject>) {
        ItemFragment(itemList).show(supportFragmentManager, "ItemDialog")
    }


    //切換底部專屬功能
    private fun toggleBottomFunction(id: Int) {
        binding.btnFunctionOne.text = resources.getResourceEntryName(id)
        binding.btnFunctionTwo.text = resources.getResourceEntryName(id) + "/n專屬功能"
        binding.btnFunctionThree.text = resources.getResourceEntryName(id) + "專屬功能2"
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
        binding.imgLeftCouplet.setOnClickListener(this)
        binding.imgRightCouplet.setOnClickListener(this)
        binding.imgUpperCouplet.setOnClickListener(this)
        binding.imgJoss.setOnClickListener(this)
        binding.imgJossBackground.setOnClickListener(this)
        binding.imgVaseLeft.setOnClickListener(this)
        binding.imgVaseRight.setOnClickListener(this)
        binding.imgTable.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            mainViewModel.itemStatus(resources.getResourceEntryName(v.id))
        }
    }
}
