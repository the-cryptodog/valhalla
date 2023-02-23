package com.app.valhalla.ui.main.dialog

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import com.app.valhalla.R
import com.app.valhalla.databinding.DialogMusicListBinding
import com.app.valhalla.util.CustomAudioManager


class MusicListDialog : DialogFragment() {
    private lateinit var binding: DialogMusicListBinding
    private lateinit var audioManager: AudioManager
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying: Boolean = false

    init {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogMusicListBinding.inflate(inflater, container, false)
        Log.d("TAG", "MusicListDialog")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        audioManager = requireContext().getSystemService(AUDIO_SERVICE) as AudioManager
        volumeSeekBar = binding.volumeSeekbar.apply {
            this.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            this.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        }

        syncButton()



        binding.music1.setOnClickListener {
            CustomAudioManager.genMusic_1(requireContext())

            if (CustomAudioManager.isPlaying() == true) {
                CustomAudioManager.pause()
                binding.music1Btn.setBackgroundResource(R.drawable.play_icon)

            } else {
                CustomAudioManager.start()
                binding.music1Btn.setBackgroundResource(R.drawable.pause_icon)
            }
        }

        // 設置此監聽器的 SeekBar 為讓用者拖曳決定音量的進度條
        val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                CustomAudioManager.getInstance()?.setVolume(progress / 10f, progress / 10f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

        binding.volumeSeekbar.setOnSeekBarChangeListener(seekBarListener)
    }

    fun syncButton() {
        when (CustomAudioManager.index()) {
            1 -> {
                if (CustomAudioManager.isPlaying() == true) {
                    binding.music1Btn.setBackgroundResource(R.drawable.pause_icon)
                } else {
                    binding.music1Btn.setBackgroundResource(R.drawable.play_icon)
                }
            }

        }

    }
}



