package com.app.valhalla.ui.drawlots

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
//import androidx.activity.viewModels
import com.app.valhalla.R
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.databinding.ActivityDrawLotsBinding
import com.app.valhalla.ui.stepcontent.StepContentActivity
import com.app.valhalla.util.FontUtil
import com.app.valhalla.util.GifUtil
import com.app.valhalla.util.fadeIn
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.koin.android.ext.android.inject

/**
 * 求籤頁面
 */
class DrawLotsActivity : BaseActivity<ActivityDrawLotsBinding>() {
    private val drawLotsViewModel:DrawLotsViewModel by inject()
    private var int_counttotal:Int=1
    private var str_stepAnswer:String =""
    private var str_stepUrl:String =""
    private var str_stepGod:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoading()
        f_initData()
        binding.imgStepshake.setImageDrawable(GifUtil.f_generateGif(this,R.drawable.step_shake))
        binding.textTitleHeart.typeface=FontUtil.f_chinese_traditional(this)
        binding.textStepanswer.typeface=FontUtil.f_chinese_traditional(this)
        binding.textTitleStepNumber.typeface=FontUtil.f_chinese_traditional(this)
        drawLotsViewModel.getStepShakeData.observe(this){
            binding.viewStepshake.isVisible = it
        }
        drawLotsViewModel.getDivinationBlocks.observe(this){
            binding.viewDivinationBlocks.isVisible = it
            if(it){
                str_stepAnswer = "${drawLotsViewModel.f_randomtogetStep(int_counttotal)}"
                binding.textTitleStepNumber.text = str_stepAnswer
            }
        }
        drawLotsViewModel.getStepThrowVisible.observe(this){
            binding.viewStepthrow.isVisible = it
        }
        drawLotsViewModel.getStepAnswer.observe(this){
            binding.imgStepthrow.setImageDrawable(GifUtil.f_generateGif(this,it))
        }
        drawLotsViewModel.getDecodeStepImg.observe(this){
            binding.imgStepanswerBtn.setImageDrawable(getDrawable(it))
        }
        drawLotsViewModel.getStepAnswerString.observe(this){
            binding.textStepanswer.setText(getString(it))
        }
        drawLotsViewModel.getIntentStepContent.observe(this){
            if(it){
                var bundle:Bundle = Bundle()
                bundle.putString("StepGod",str_stepGod)
                bundle.putString("StepKey",str_stepAnswer)
                bundle.putString("StepResourceUrl",str_stepUrl)
                val intent =Intent(this,StepContentActivity::class.java).apply {
                    putExtras(bundle)
                }
                startActivity(intent)
                finish()
            }
        }
        binding.viewSubfunction.setOnClickListener{
            //攔截子功能view顯示後下層view可以點擊
        }
        binding.imgDrawlots.setOnClickListener{
            binding.viewSubfunction.isVisible = true
            drawLotsViewModel.StepOne()
        }

        binding.imgTitleDivinationBlocks.setOnClickListener {
            drawLotsViewModel.StepTwo()
        }
        binding.imgStepanswerBtn.setOnClickListener {
            drawLotsViewModel.StepThree()
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun getViewBinding(): ActivityDrawLotsBinding {
        return ActivityDrawLotsBinding.inflate(layoutInflater)
    }

    fun f_initData(){
        drawLotsViewModel.f_DecodeStepData()
        drawLotsViewModel.getStepGodData.observe(this,Observer{
            binding.textTitleHeart.text=it.property_contents.GodName
            str_stepGod = it.property_contents.GodName
            int_counttotal=it.property_contents.StepCount
            str_stepUrl=it.property_contents.StepSource
            Glide.with(this)
                .load(it.property_contents.ImgUrl)
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
                        binding.godImg.fadeIn(2000)
                        return false; // Allow calling onResourceReady on the Target.
                    }

                })
                .into(binding.godImg)
        })

    }
}