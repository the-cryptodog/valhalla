package com.app.valhalla.util

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.app.valhalla.R

/**
 * 字型類
 */
object FontUtil {
    //標楷體
    fun f_chinese_traditional(context: Context): Typeface {
        return ResourcesCompat.getFont(context, R.font.chinese_font)!!
    }
}

fun TextView.toCT(){
    this.typeface = ResourcesCompat.getFont(context, R.font.chinese_font)!!
}

fun TextView.ccc(decorator:()->Unit){
    decorator
}