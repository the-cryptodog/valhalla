package com.app.valhalla.ui.drawlots

import android.content.Intent
import android.graphics.Typeface
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
import com.app.valhalla.ui.webview.CommonWebViewActivity
import com.app.valhalla.util.FontUtil
import com.app.valhalla.util.GifUtil
import com.app.valhalla.util.fadeIn
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.droidsonroids.gif.GifDrawable
import kotlin.random.Random

/**
 * 求籤頁面
 */
class DrawLotsActivity : BaseActivity<ActivityDrawLotsBinding>() {
    private val drawLotsViewModel by viewModels<DrawLotsViewModel>()
    private var int_counttotal:Int=1
    private var str_stepAnswer:String =""
    private var str_stepUrl:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        intent.getBundleExtra("responsestepgod")?.let {
            drawLotsViewModel.f_DecodeStepData(it)
        }
        drawLotsViewModel.getStepGodData.observe(this,Observer{
            binding.textTitleHeart.text=it.data.name
            int_counttotal=it.data.step_count
            str_stepUrl=it.data.StepResource()
            Glide.with(this)
                .load(it.data.imgUrl())
                .into(binding.godImg)
            binding.godImg.fadeIn(2000)
        })
    }
}