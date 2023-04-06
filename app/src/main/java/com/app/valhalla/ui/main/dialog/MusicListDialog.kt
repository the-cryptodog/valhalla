package com.app.valhalla.ui.main.dialog

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.valhalla.R
import com.app.valhalla.data.model.BgmManager
import com.app.valhalla.databinding.DialogMusicListBinding
import com.app.valhalla.databinding.VhItemMusicBinding
import com.app.valhalla.ui.main.MainViewModel


class MusicListDialog(private val viewModel: MainViewModel, private val context: Context) :
    DialogFragment() {
    private lateinit var binding: DialogMusicListBinding
    private lateinit var audioManager: AudioManager
    private lateinit var volumeSeekBar: SeekBar

    init {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogMusicListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        audioManager = requireContext().getSystemService(AUDIO_SERVICE) as AudioManager
        volumeSeekBar = binding.volumeSeekbar.apply {
            this.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            this.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

            binding.rvMusicList.adapter = MusicListAdapter(BgmManager.getList())
            binding.rvMusicList.layoutManager = LinearLayoutManager(context)
        }


//        // 設置此監聽器的 SeekBar 為讓用者拖曳決定音量的進度條
//        val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
////                CustomAudioManager.getInstance()?.setVolume(progress / 10f, progress / 10f)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//        }
//
//        binding.volumeSeekbar.setOnSeekBarChangeListener(seekBarListener)

        viewModel.musicList.observe(this){
            binding.rvMusicList.adapter = MusicListAdapter(it)
        }

        binding.tvVolume.setOnClickListener {
            Log.d("ddd","tvVolume")
            viewModel.setMusic()
        }
    }

    class MusicListAdapter(private val musicList: List<BgmManager.ByeMusic>) :
        RecyclerView.Adapter<MusicListAdapter.MusicItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicItemHolder {
            // 載入佈局文件並創建對應的 View Binding 物件
            val binding =
                VhItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            // 創建 MusicItemHolder 物件並返回
            return MusicItemHolder(binding)
        }

        override fun getItemCount(): Int {
            return musicList.size
        }

        override fun onBindViewHolder(holder: MusicItemHolder, position: Int) {
            holder.binding.titleTextView.text = musicList[position].title
            holder.binding.btnPlayPause.apply {
                setImageResource(if (musicList[position].isPlaying) R.drawable.icon_pause_button else R.drawable.icon_play_button)
            }
        }

        inner class MusicItemHolder(val binding: VhItemMusicBinding) :
            RecyclerView.ViewHolder(binding.root), View.OnClickListener {
            init {
                binding.btnPlayPause.setOnClickListener(this)
                binding.btnClose.setOnClickListener(this)
            }

            override fun onClick(view: View?) {
                when (view) {
                    binding.btnPlayPause -> {
                        //新音樂
                        val music = musicList[adapterPosition]

                        if (adapterPosition == BgmManager.getCurrentMusicIndex() || adapterPosition == RecyclerView.NO_POSITION) {
                            // 非新音樂，切換暫停/播放
                            if (music.isPlaying) {
                                if (BgmManager.isPlaying()) {
                                    BgmManager.pauseMusic()
                                }
                            } else {
                                if (!BgmManager.isPlaying()) {
                                    BgmManager.resumeMusic()
                                }
                            }
                            music.isPlaying = !music.isPlaying
                            notifyItemChanged(adapterPosition)
                        } else {
                            // 新音樂，播放並停止原來的音樂
                            val oldIndex = BgmManager.getCurrentMusicIndex()
                            if (oldIndex != -1) {
                                musicList[oldIndex].isPlaying = false
                                notifyItemChanged(oldIndex)
                            }
                            BgmManager.playMusic(view.rootView.context, adapterPosition)
                            music.isPlaying = true
                            notifyItemChanged(adapterPosition)
                        }

                    }
                    binding.btnClose -> {
                        if (adapterPosition != RecyclerView.NO_POSITION) {

                        }
                    }
                }
            }
        }
    }
}



