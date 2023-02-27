package com.app.valhalla.ui.stepcontent

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.databinding.ActivityStepContentBinding
import com.app.valhalla.util.FontUtil
import com.app.valhalla.util.fadeIn
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class StepContentActivity : BaseActivity<ActivityStepContentBinding>() {
    private val stepContentViewModel by viewModels<StepContentViewModel>()
    var str_stepNO:String = ""
    var str_stepUrl:String = ""
    var str_stepGod:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoading()
        binding.textStepcontent.typeface= FontUtil.f_chinese_traditional(this)

        stepContentViewModel.getIsImgCanVisible.observe(this){
            binding.stepImg.isVisible = it
        }
        stepContentViewModel.getIsContentCanVisible.observe(this){
            binding.blockStepContent.isVisible = it
        }

        stepContentViewModel.f_controlvisible(true,false)
        getbundleData()
        Glide.with(this)
            .load(str_stepUrl+str_stepNO+".jpg")
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
                    hideLoading()
                    binding.stepImg.fadeIn(2000)
                    return false; // Allow calling onResourceReady on the Target.
                }

            })
            .into(binding.stepImg)
        stepContentViewModel.getStepContentData.observe(this){
            binding.textStepcontent.text=stepContentViewModel.getStepContentData.value
        }
        Log.d("step",str_stepGod)
        stepContentViewModel.f_getContent(str_stepGod,str_stepNO)

        binding.stepImg.setOnClickListener{
            stepContentViewModel.f_controlvisible(false,true)
        }
        binding.btnBack.setOnClickListener{
            stepContentViewModel.f_controlvisible(true,false)
        }
    }

    override fun getViewBinding(): ActivityStepContentBinding {
        return ActivityStepContentBinding.inflate(layoutInflater)
    }

    private fun getbundleData(){
        val bundle = intent.extras
        if (bundle != null) {
            str_stepGod = bundle.getString("StepGod")?:""
            str_stepNO = bundle.getString("StepKey")?:"1"
            str_stepUrl = bundle.getString("StepResourceUrl")?:""
        }
    }
}