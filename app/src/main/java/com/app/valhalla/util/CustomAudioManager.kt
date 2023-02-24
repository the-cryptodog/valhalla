package com.app.valhalla.util

import android.content.Context
import android.media.MediaPlayer
import com.app.valhalla.R

object CustomAudioManager {

    private var currentMediaPlayer : MediaPlayer? = null
    private var index : Int = 0

    fun genMusic_1(context: Context) : MediaPlayer? {
        if(currentMediaPlayer == null) {
            currentMediaPlayer = MediaPlayer.create(context, R.raw.freeloop)
            index =1
        }
        return currentMediaPlayer
    }


    fun start(){
        currentMediaPlayer?.start()
    }

    fun pause() {
        currentMediaPlayer?.pause()
    }

    fun isPlaying() : Boolean? {
        return currentMediaPlayer?.isPlaying
    }

    fun getInstance(): MediaPlayer? {
        return currentMediaPlayer
    }

    fun index(): Int {
        return index
    }



}