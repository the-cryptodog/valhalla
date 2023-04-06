package com.app.valhalla.data.model

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.app.valhalla.R
import java.io.FileNotFoundException
import java.io.IOException

object BgmManager {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentSongIndex: Int = -1

    init{
        mediaPlayer.setOnPreparedListener {
            // prepare 完成後開始播放音樂
            mediaPlayer.start()
        }
    }

    private val list = listOf<ByeMusic>(
        ByeMusic("歌曲1", R.raw.dao_music_1),
        ByeMusic("歌曲2", R.raw.freeloop),
        ByeMusic("歌曲3", R.raw.launch_music)
    )


    fun playMusic(context: Context, index: Int) {
        Log.d("ddd", "index=$index")
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(context.resources.openRawResourceFd(list[index].id))
            mediaPlayer.prepareAsync()
            currentSongIndex = index

        } catch (e: FileNotFoundException) {
            // 音頻文件不存在，需要進行相應的處理
            e.printStackTrace()
        } catch (e: IOException) {
            // 文件讀取錯誤或者文件描述符被意外關閉等 IO 異常，需要進行相應的處理
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            // 資源 id 是無效的，或者設置的音頻文件格式不受支持，需要進行相應的處理
            e.printStackTrace()
        }
    }


    fun resumeMusic() {
        mediaPlayer.start()
    }

    fun pauseMusic() {
        mediaPlayer.pause()
    }


    fun stopMusic() {
        currentSongIndex= -1
        mediaPlayer.stop()
    }

    fun isPlaying() :Boolean{
        return mediaPlayer.isPlaying
    }

    fun getCurrentMusicIndex() :Int{
        return currentSongIndex
    }



    fun getList() = list


    data class ByeMusic(
        val title: String = "",
        val id: Int = 0,
        var isPlaying: Boolean = false
    )
}