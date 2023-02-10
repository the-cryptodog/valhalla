package com.app.valhalla.base

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils

open class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this,true)
        requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}