package com.app.valhalla.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView



/**

.setDuration (5000)

透明度
.alpha(0)
.alphaBy (0)


旋转
.rotation(360)
.rotationBy (360)
.rotationX(360)
.rotationXBy (360)
.rotationY (360)
.rotationYBy (360)


缩放
.scaleX(1)
.scaleXBy (1)
.scaleY(1)
.scaleYBy (1)

平移
.translationX(100)
.translationXBy (100)
•translationY(100)
.translationYBy (100)
.translationZ(100)
.translationZBy (100)

更改在屏幕上的坐桥
.×(10)
.XBy (10)
.y (10)
.By (10)
.Z(10)
.zBy (10)

1/监听及其他设置
.setInterpolator(new BounceInterpolator ( ))
.setStartDelay (1000)
.setListener (new Animator.AnimatorListener() {
    @Override
    public void onAnimationStart (Animator animation) {
    }
    @Override
    public void onAnimationEnd (Animator animation) {
    }
    @Override
    public void onAnimationCancel (Animator animation) {
    }
    @Override
    public void onAnimationRepeat (Animator animation) {
    }
})

.setUpdateListener (new ValueAnimator.AnimatorUpdateListener() {
    @Override
    public void onAnimationUpdate (ValueAnimator animation) {
         }
    })
.withEndAction (new Runnable () {
    @Override
    public void run() {
        Log. i(TAG, "run: end");
         }
    })
.withStartAction(new Runnable () {
    @Override
    public void run() {
        Log. i(TAG, "run: start");
    }
 }
 **/


/**  **/
fun ImageView.moveInFromRight(duration:Long){
    animate().translationX(-25f).translationY(-600f).setDuration(duration)
}

/**  **/
fun ImageView.moveInFromLeft(duration:Long){
    animate().translationX(25f).translationY(-600f).setDuration(duration)
}


/** 淡出 **/
fun ImageView.fadeOut(duration:Long){
    animate().alpha(0f).setDuration(duration).start()
}

/** 淡出 **/
fun View.fadeOut(duration:Long){
    animate().alpha(0f).setDuration(duration).start()
}

/** 淡出 **/
fun ImageView.fadeIn(duration:Long){
    alpha = 0f
    animate().alpha(1f).setDuration(duration).start()
}

/** 順時針旋轉 **/
fun ImageView.rotateCW(degree:Float,duration:Long){
        animate().rotationBy(degree).setDuration(duration).start()
}

/** 逆時針旋轉 **/
fun ImageView.rotateCCW(degree:Float,duration:Long){
    animate().rotation(degree).setDuration(duration).start()
}




