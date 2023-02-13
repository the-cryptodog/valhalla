package com.app.valhalla.ui.webview

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.core.content.ContextCompat.startActivity
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.databinding.ActivityWebviewBinding
import com.app.valhalla.util.Constant

class CommonWebViewActivity:BaseActivity() {
    private lateinit var binding: ActivityWebviewBinding
    var str_stepNO:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading()
        getbundleData()
        setWebView(Constant.BASE_URL+"api_getstep.php?step_count="+str_stepNO)
    }

    private fun getbundleData(){
        val bundle = intent.extras
        if (bundle != null) {
            str_stepNO = bundle.getString("StepKey")?:"1"
        }
    }

    fun setWebView(url: String) {
        val webSettings: WebSettings = binding.webBrowser.getSettings()
        webSettings.allowContentAccess = true //允许WebView从安装在系统中的内容提供者载入内容
        webSettings.allowFileAccess = true //設定可以訪問檔案
        webSettings.builtInZoomControls = true // 讓webView可以縮放
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE //關閉webview中快取
        webSettings.databaseEnabled = true //開啟database storage API 功能
        webSettings.defaultTextEncodingName = "utf-8" //設定編碼格式
        webSettings.displayZoomControls = false // 是否要顯示右下角縮放的按鈕(false:隱藏)
        webSettings.domStorageEnabled = true //開啟DOM storage API 功能
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支援通過JS開啟新視窗
        webSettings.javaScriptEnabled = true //允许WebView使用JavaScript
        webSettings.loadsImagesAutomatically = true //支援自動載入圖片
        webSettings.loadWithOverviewMode = true // 超出螢幕範圍的網頁縮到符合螢幕寬度
        webSettings.setSupportZoom(false) // 讓webView支援手勢或按鈕進行縮放
        webSettings.useWideViewPort = true // 將圖片調整到適合webview的大小
        binding.webBrowser.webViewClient = MyWebViewClient()
        binding.webBrowser.loadUrl("https://wantkenny.000webhostapp.com/goodbye/api_getstep.php?step_count="+str_stepNO)

    }
    inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            url?.let { view?.loadUrl(it) }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            hideLoading()
        }
    }
}