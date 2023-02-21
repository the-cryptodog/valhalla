package com.app.valhalla.base

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.valhalla.ui.main.dialog.ProgressLoadingDialog
import com.blankj.utilcode.util.BarUtils

abstract class BaseActivity : AppCompatActivity() {

    private val mLoadingDialog by lazy { ProgressLoadingDialog(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this, true)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    override fun onResume() {
        super.onResume()
    }

    /*
      顯示加載框，默認實現
   */
    fun showLoading() {
        mLoadingDialog.showLoading()
    }


    /*
        隱藏加載框，默認實現
     */
    fun hideLoading() {
        mLoadingDialog.hideLoading()
    }

    /*
        錯誤信息提示，默認實現
     */
//    fun onError(text: String) {
//        ToastUtils.show(text)
//    }


}