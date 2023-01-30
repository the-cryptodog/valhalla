package com.app.valhalla.util

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.content.res.ResourcesCompat
import com.app.valhalla.R
import pl.droidsonroids.gif.GifDrawable

object GifUtil {
    //generate gif
    fun f_generateGif(context: Context, @DrawableRes @RawRes res_id:Int): GifDrawable {
        return GifDrawable(context.resources, res_id)!!
    }
}