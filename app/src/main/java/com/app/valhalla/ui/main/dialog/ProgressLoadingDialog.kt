package com.app.valhalla.ui.main.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.app.valhalla.R
import com.app.valhalla.databinding.ProgressDialogBinding
import com.app.valhalla.util.GifUtil

class ProgressLoadingDialog(
    context: Context
) : Dialog(context) {

    val binding = ProgressDialogBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_dialog)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.attributes?.gravity = Gravity.CENTER
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun showLoading() {
        super.show()
        try {
            GifUtil.f_generateGif(
                context,
                R.drawable.loading_logo
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*
        隱藏加載對話框，動畫停止
     */
    fun hideLoading() {
        super.dismiss()
        try {
//            if (mLoading.isAnimating) {
//                mLoading.cancelAnimation()
//            }
            //        animDrawable?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}