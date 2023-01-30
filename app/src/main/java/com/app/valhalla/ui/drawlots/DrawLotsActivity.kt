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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.droidsonroids.gif.GifDrawable

class DrawLotsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawLotsBinding
    private val drawLotsViewModel by viewModels<DrawLotsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawLotsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var gifDrawable: GifDrawable = GifDrawable(resources, R.drawable.step_shake)
        binding.imgStepshake.setImageDrawable(gifDrawable)
        binding.textTitleHeart.typeface=FontUtil.f_chinese_traditional(this)
        binding.textTitleDivinationBlocks.typeface=FontUtil.f_chinese_traditional(this)

        binding.imgDrawlots.setOnClickListener{
            binding.viewDrawlots.isVisible = true
            drawLotsViewModel.StepVisible()
            drawLotsViewModel.getStepData.observe(this){
                binding.imgStepshake.isVisible = drawLotsViewModel.getStepData.value == true
                binding.imgStep.isVisible = drawLotsViewModel.getStepData.value != true
                binding.textTitleDivinationBlocks.isVisible = drawLotsViewModel.getStepData.value != true
            }
        }

        binding.textTitleDivinationBlocks.setOnClickListener {
            
        }
    }
}