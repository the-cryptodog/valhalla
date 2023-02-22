package com.app.valhalla.ui.stepcontent

import android.os.Bundle
import androidx.activity.viewModels
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.databinding.ActivityStepContentBinding
import com.app.valhalla.util.FontUtil

class StepContentActivity : BaseActivity() {
    private lateinit var binding: ActivityStepContentBinding
    private val stepContentViewModel by viewModels<StepContentViewModel>()
    var str_stepNO:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textStepcontent.typeface= FontUtil.f_chinese_traditional(this)
        getbundleData()

    }
    private fun getbundleData(){
        val bundle = intent.extras
        if (bundle != null) {
            str_stepNO = bundle.getString("StepKey")?:"1"
        }
    }
}