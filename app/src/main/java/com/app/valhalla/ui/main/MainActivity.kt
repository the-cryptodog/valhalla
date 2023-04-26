package com.app.valhalla.ui.main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.app.valhalla.R
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.data.BaseViewModel
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.databinding.ActivityMainBinding
import com.app.valhalla.ui.drawlots.DrawLotsActivity
import com.app.valhalla.ui.main.dialog.ItemFragment
import com.app.valhalla.ui.main.dialog.MusicListDialog
import com.app.valhalla.util.Constant
import com.app.valhalla.util.GifUtil
import com.app.valhalla.util.fadeIn
import com.app.valhalla.util.fadeOut
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity<ActivityMainBinding>(), OnClickListener,
    ItemFragment.OnDialogItemClickListener,
    SensorEventListener {

    private val mainViewModel: MainViewModel by inject()
    private val bundle: Bundle = Bundle()
    private lateinit var sensorManager: SensorManager
    private var accelerometerSensor: Sensor? = null
    private var gameObjectList: MutableList<View> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initOnClick()

        // 獲取 SensorManager 實例
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // 獲取加速度計傳感器實例
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        MobileAds.initialize(this) {}

        binding.adView.loadAd(AdRequest.Builder().build())

        //切換物品替換
        mainViewModel.mainItemViewState.observe(this) {
            when (it) {
                is MainViewModel.MainItemViewState.ShowSwitchDialog -> {
                    initItemDialog(it.data)
                }
                is MainViewModel.MainItemViewState.CloseSwitchDialog -> TODO()
            }
        }

        //觀察包包鍵的變化
        mainViewModel.objectSelectedEvent.observe(this) { imgUrl ->
            Glide.with(this)
                .load(imgUrl)
                .into(binding.btnFunctionOne)
        }

        mainViewModel.mediaState.observe(this) {
            when (it) {
                is MainViewModel.MediaState.ShowDialog -> {
                    initRadioDialog()
                }
                is MainViewModel.MediaState.Playing -> TODO()
                is MainViewModel.MediaState.Pausing -> TODO()
            }
        }

        //觀察 網路請求成功後回調值 轉跳
        mainViewModel.loadingViewStatePublisher.observe(
            this
        ) {
            when (it) {
                is BaseViewModel.LoadingViewState.MainActivityImageLoadingDone -> {
                    //進入主頁踩入讀片結束
                    binding.loadingCover.fadeOut(1000)
                }
                is BaseViewModel.LoadingViewState.HideLoadingView -> {
                    TODO()
                }
                is BaseViewModel.LoadingViewState.ShowLoadingView -> {
                    TODO()
                }
            }
        }



        mainViewModel.defaultGameObjList.observe(this, Observer { list ->
            for (gameObject in list) {
                when (gameObject.id.first().toString()) {
                    Constant.OBJ_TABLE -> loadWithGlide(gameObject.img_url, binding.imgTable)
                    Constant.OBJ_INCENSE_BURNER_ID -> loadWithGlide(
                        gameObject.img_url,
                        binding.imgIncenseBurner
                    )
                    Constant.OBJ_VASE -> loadWithGlide(
                        gameObject.img_url,
                        binding.imgVaseRight
                    ).also { loadWithGlide(gameObject.img_url, binding.imgVaseLeft) }
                    Constant.OBJ_JOSS -> loadWithGlide(gameObject.img_url, binding.imgJoss)
                    Constant.OBJ_JOSS_BACKGROUND_ID -> loadWithGlide(
                        gameObject.img_url,
                        binding.imgJossBackground
                    )
                    Constant.OBJ_CANDLE_ID -> loadWithGlide(
                        gameObject.img_url,
                        binding.imgCandleRight
                    ).also { loadWithGlide(gameObject.img_url, binding.imgCandleLeft) }
                }
            }
        })
        getStepGodData()
    }

    private fun setGameObjectViewGroup() {

    }


    private fun loadWithGlide(url: String, view: ImageView) {
        Glide.with(this)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false; // Allow calling onLoadFailed on the Target.
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    mainViewModel.addLoadingCount()
                    return false; // Allow calling onResourceReady on the Target.
                }

            })
            .into(view)
    }

    private fun closeRadioDialog() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        accelerometerSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onPause() {
        super.onPause()
        // 解除註冊加速度計傳感器監聽器
        sensorManager.unregisterListener(this)
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
        binding.imgRadio.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                binding.btnFunctionOne.id -> {
                    mainViewModel.showSwitchDialog()
                }
                binding.btnFunctionTwo.id -> {
                    testMove()
                }
                binding.btnFunctionThree.id -> {
                    startActivity(
                        Intent(
                            this,
                            DrawLotsActivity::class.java
                        ).putExtra("responsestepgod", bundle)
                    )
                }

                binding.imgTable.id -> {

                }
                binding.imgIncenseBurner.id -> {

                }
                binding.imgVaseLeft.id, binding.imgVaseRight.id -> {

                }
                binding.imgCandleLeft.id, binding.imgCandleRight.id -> {

                }
                binding.imgJoss.id -> {

                }
                binding.imgJossBackground.id -> {

                }
                binding.imgRadio.id -> {
                    mainViewModel.toggleMusicDialog()
                }
            }
        }
    }

    private fun initRadioDialog() {
        MusicListDialog(viewModel = this.mainViewModel, this).show(
            supportFragmentManager,
            "MusicListDialog"
        )
    }

    override fun onDialogItemClick(itemType: String, itemId: String, dialog: DialogFragment) {
        mainViewModel.updateCurrentSelectedItem(itemType, itemId)
        dialog.dismiss()
    }


    private fun testMove() {
        //按下按鈕出現手
        if (binding.byeFix.isVisible || binding.byeGif.isVisible) {
            return
        }
        binding.byeFix.visibility = View.VISIBLE
        binding.byeFix.fadeIn(500)

    }

    private fun bye() {
        //搖晃開始拜
        if (binding.byeFix.isVisible) {
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

    private fun getStepGodData() {
        mainViewModel.get_itemStepDataList.observe(this) {
            bundle.putParcelable("stepGodData", it)
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
//            Log.d("SensorActivity", "x: $x, y: $y, z: $z")

            if (y + z >= 14) {
                bye()
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 在傳感器精度發生變化時，這個方法將被調用，這裡我們不處理
    }
}
