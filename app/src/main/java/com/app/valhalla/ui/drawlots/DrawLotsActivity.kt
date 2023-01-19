package com.app.valhalla.ui.drawlots

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
//import androidx.activity.viewModels
import com.app.valhalla.R
import com.app.valhalla.databinding.ActivityDrawLotsBinding
import com.app.valhalla.util.FontUtil
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
        binding.titleText.typeface=FontUtil.f_chinese_traditional(this)
    }
}