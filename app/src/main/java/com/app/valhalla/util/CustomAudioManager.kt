package com.app.valhalla.util

import android.content.Context
import android.media.MediaPlayer
import com.app.valhalla.R

object CustomAudioManager {

    private var currentMediaPlayer : MediaPlayer? = null
    private var currentIndex : Int = 0

    fun playMusic(context: Context, index :Int){
        if(currentMediaPlayer == null || currentIndex != index) {
            currentIndex = index
            currentMediaPlayer?.stop()
            when (index) {
                0 -> {
                    currentMediaPlayer = MediaPlayer.create(context, R.raw.freeloop)
                    currentMediaPlayer?.start()
                }
                1 -> {
                    currentMediaPlayer = MediaPlayer.create(context, R.raw.dao_music_1)
                    currentMediaPlayer?.start()
                }
                2 -> {}
                3 -> {}
                4 -> {}
                5 -> {}
            }
        }
        else {
            if(currentMediaPlayer?.isPlaying == true){
                currentMediaPlayer?.pause()
            }else{
                currentMediaPlayer?.start()
            }
        }
    }


}