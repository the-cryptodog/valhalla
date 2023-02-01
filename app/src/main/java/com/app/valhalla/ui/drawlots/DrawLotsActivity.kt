package com.app.valhalla.ui.drawlots

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
//import androidx.activity.viewModels
import com.app.valhalla.R
import com.app.valhalla.databinding.ActivityDrawLotsBinding
import com.app.valhalla.util.FontUtil
import com.app.valhalla.util.GifUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.droidsonroids.gif.GifDrawable
import kotlin.random.Random

/**
 * 求籤頁面
 */
class DrawLotsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawLotsBinding
    private val drawLotsViewModel by viewModels<DrawLotsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawLotsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgStepshake.setImageDrawable(GifUtil.f_generateGif(this,R.drawable.step_shake))
        binding.textTitleHeart.typeface=FontUtil.f_chinese_traditional(this)
        binding.textTitleDivinationBlocks.typeface=FontUtil.f_chinese_traditional(this)
        binding.textStepanswer.typeface=FontUtil.f_chinese_traditional(this)

        drawLotsViewModel.getStepShakeData.observe(this){
            binding.viewStepshake.isVisible = it
        }
        drawLotsViewModel.getDivinationBlocks.observe(this){
            binding.viewDivinationBlocks.isVisible = it
        }
        drawLotsViewModel.getStepThrowVisible.observe(this){
            binding.viewStepthrow.isVisible = it
        }
        drawLotsViewModel.getStepAnswer.observe(this){
            binding.imgStepthrow.setImageDrawable(GifUtil.f_generateGif(this,it))
        }
        drawLotsViewModel.getStepAnswerString.observe(this){
            binding.textStepanswer.setText(getString(it))
        }

        binding.imgDrawlots.setOnClickListener{
            binding.viewSubfunction.isVisible = true
            drawLotsViewModel.StepOne()
        }

        binding.textTitleDivinationBlocks.setOnClickListener {
            drawLotsViewModel.StepTwo()
        }
        binding.textStepanswer.setOnClickListener {
            drawLotsViewModel.StepThree()
        }

    }
}