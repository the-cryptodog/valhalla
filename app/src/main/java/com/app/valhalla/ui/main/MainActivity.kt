package com.app.valhalla.ui.main

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.app.valhalla.R
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.data.model.BaseResult
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.data.model.StepBaseResult
import com.app.valhalla.databinding.ActivityMainBinding
import com.app.valhalla.ui.drawlots.DrawLotsActivity
import com.app.valhalla.ui.main.dialog.ItemFragment
import com.app.valhalla.util.*
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity(), OnClickListener, ItemFragment.OnDialogItemClickListener,SensorEventListener  {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private val bundle:Bundle = Bundle()

    private lateinit var sensorManager: SensorManager
    private var accelerometerSensor: Sensor? = null



    override fun onResume() {
        super.onResume()
        accelerometerSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun onPause() {
        super.onPause()
        // 解除註冊加速度計傳感器監聽器
        sensorManager.unregisterListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initOnClick()

        intent.getBundleExtra("response")?.let {
            Log.d("TAGB", "bundle: $it")
            mainViewModel.loadData(it)
        }

        // 獲取 SensorManager 實例
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // 獲取加速度計傳感器實例
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        MobileAds.initialize(this) {}

        binding.adView.loadAd(AdRequest.Builder().build())

        mainViewModel.btnFunction.observe(this, Observer {
            val img = it[Constant.BUTTON_LEFT]!!.imgUrl()
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
                .load(list.find { it.type == Constant.OBJ_TABLE }?.imgUrl())
                .into(binding.imgTable)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_INCENSE_BURNER_ID }?.imgUrl())
                .into(binding.imgIncenseBurner)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_INCENSE_ID }?.imgUrl())
                .into(binding.imgIncense)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_VASE }?.imgUrl())
                .into(binding.imgVaseRight)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_VASE }?.imgUrl())
                .into(binding.imgVaseLeft)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_JOSS }?.imgUrl())
                .into(binding.imgJoss)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_JOSS_BACKGROUND_ID }?.imgUrl())
                .into(binding.imgJossBackground)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_CANDLE_ID }?.imgUrl())
                .into(binding.imgCandleRight)
            Glide.with(this)
                .load(list.find { it.type == Constant.OBJ_CANDLE_ID }?.imgUrl())
                .into(binding.imgCandleLeft)
        })
        getStepGodData()
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
                    startActivity(Intent(this, DrawLotsActivity::class.java).putExtra("responsestepgod", bundle))
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
        mainViewModel.updateCurrentSelectedItem(itemType, itemId)
        dialog.dismiss()
    }


    fun testMove() {
        //按下按鈕出現手
        if(binding.byeFix.isVisible || binding.byeGif.isVisible ){
            return
        }
        binding.byeFix.visibility =View.VISIBLE
        binding.byeFix.fadeIn(500)

    }

    fun bye(){
        //搖晃開始拜
        if(binding.byeFix.isVisible) {
            binding.byeFix.visibility = View.GONE
            binding.byeGif.setImageDrawable(
                GifUtil.f_generateGif(
                    this,
                    R.drawable.byebye_gif
                )
            )


            binding.byeGif.visibility = View.VISIBLE
            binding.byeGif.alpha = 1f

            lifecycleScope.launch {
                delay(3000)
                binding.byeGif.fadeOut(1000)
                delay(500)
                binding.byeGif.visibility = View.GONE
            }
        }
    }
    fun getStepGodData(){
        mainViewModel.get_itemStepDataList.observe(this){
            bundle.putParcelable("stepGodData",it)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // 當加速度計數據發生變化時，這個方法將被調用
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            // 獲取加速度計數據
            val x = event.values[0].toInt()
            val y = event.values[1].toInt()
            val z = event.values[2].toInt()

            // 在控制台上打印加速度計數據
            Log.d("SensorActivity", "x: $x, y: $y, z: $z")

            if(y+z >= 14){
                bye()
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 在傳感器精度發生變化時，這個方法將被調用，這裡我們不處理
    }
}
