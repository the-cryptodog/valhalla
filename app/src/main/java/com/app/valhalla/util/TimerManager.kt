package com.app.valhalla.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NotificationUtils.cancel
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

object TimerManager {

    private var timer: Timer? = null

    val countdownTime = MutableLiveData<String>()

    val isCounting = MutableLiveData<Boolean>()

    fun startCountDown(inputDateTime: String?) {
        timer?.cancel() // cancel any previous timer

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(inputDateTime, formatter)
        val targetDateTime = dateTime.minusMinutes(0)

        timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                val remainingTime = LocalDateTime.now().until(targetDateTime, ChronoUnit.SECONDS)
                val timeString = if (remainingTime <= 0) {
                    timer?.cancel()
                    isCounting.postValue(false)
                    "香燒完啦幹！"
                } else {
                    val hours = remainingTime / 3600
                    val minutes = (remainingTime % 3600) / 60
                    val seconds = remainingTime % 60
                    String.format("%02d:%02d:%02d", hours, minutes, seconds)
                }
                countdownTime.postValue(timeString)
            }
        }
        isCounting.postValue(true)
        timer?.scheduleAtFixedRate(task, 0, 1000)
    }

    suspend fun mainKotlin() {
        val inputDateTime = "2023-07-18 12:37"

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(inputDateTime, formatter)

        val targetDateTime = dateTime.minusMinutes(1)

        val task = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                val remainingTime = ChronoUnit.SECONDS.between(LocalDateTime.now(), targetDateTime)
                if (remainingTime <= 0) {
                    println("倒计时结束！")
                    cancel()
                } else {
                    val hours = remainingTime / 3600
                    val minutes = (remainingTime % 3600) / 60
                    val seconds = remainingTime % 60
                    println(String.format("%02d:%02d:%02d", hours, minutes, seconds))
                }
                delay(1000)
            }
        }
        task.join()
    }

    fun countdownToTime(targetTime: String): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val targetDateTime = LocalDateTime.parse(targetTime, formatter)

        val duration = ChronoUnit.SECONDS.between(currentDateTime, targetDateTime)
        if (duration < 0) {
            return "指定时间已过去"
        }

        val days = duration / (24 * 3600)
        val hours = (duration % (24 * 3600)) / 3600
        val minutes = (duration % 3600) / 60
        val seconds = duration % 60

        return String.format("%02d-%02d-%02d %02d:%02d", days, hours, minutes, seconds)
    }
}