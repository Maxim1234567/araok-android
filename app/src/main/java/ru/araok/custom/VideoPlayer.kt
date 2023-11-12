package ru.araok.custom

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.*
import ru.araok.data.dto.MarkDto
import ru.araok.data.dto.SettingsDto
import ru.araok.databinding.VideoPlayerLayoutBinding
import ru.araok.milliSecondsToTimer

class VideoPlayer
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private var playSetting: Job = startCoroutineSettings()
    private var markJob: Job? = null
    private var delayJob: Job? = null

    private var isMarkPlay = false
    private var settings: SettingsDto? = null
    private var currentMarkPosition = -1
    private var mediaPlayer: MediaPlayer? = null
    private var timer: CountDownTimer? = null
    private var changeSeekBar = false

    private val binding = VideoPlayerLayoutBinding.inflate(LayoutInflater.from(context))

    private val timerTrackLength: CountDownTimer = object: CountDownTimer(Long.MAX_VALUE, 1000) {
        override fun onTick(p0: Long) {
            binding.seekBar.max = binding.videoView.duration
            binding.seekBar.progress = binding.videoView.currentPosition
            binding.trackLength.text = milliSecondsToTimer(binding.videoView.duration)
            binding.startTrackLength.text = milliSecondsToTimer(binding.videoView.currentPosition)
        }

        override fun onFinish() {

        }
    }

    init {
        addView(binding.root)

        binding.videoView.setOnPreparedListener {
            mediaPlayer = it
        }

        binding.play.setOnClickListener {
            timer?.cancel()

            if(binding.videoView.isPlaying)
                binding.videoView.pause()
            else
                binding.videoView.start()

            timer = newTimer()
            timer?.start()
        }

        binding.next.setOnClickListener {
            timer?.cancel()
            nextMarkAndStart()
            timer = newTimer()
            timer?.start()
        }

        binding.prev.setOnClickListener {
            timer?.cancel()
            prevMarkAndStart()
            timer = newTimer()
            timer?.start()
        }

        binding.videoView.setOnTouchListener { _, _ ->
            timer?.cancel()
            timer = newTimer()
            timer?.start()

            true
        }

        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(changeSeekBar) {
                    timer?.cancel()
                    timer = newTimer()
                    timer?.start()
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                changeSeekBar = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                binding.videoView.seekTo(p0?.progress!!)
                changeSeekBar = false
            }

        })
    }

    fun setVideoPath(path: Uri) {
        binding.videoView.setVideoURI(path)
    }

    fun play() {
        binding.videoView.start()
        timerTrackLength.start()
    }

    private fun delay(end: Int) = scope.launch(start = CoroutineStart.LAZY, context = Dispatchers.IO) {
        Log.d("VideoPlayer", "work start: $end")
        while (binding.videoView.currentPosition <= end)
            ;
        Log.d("VideoPlayer", "work end: $end")
    }

    private fun startCoroutineSettings() = scope.launch(start = CoroutineStart.LAZY, context = Dispatchers.IO) {
        while (true) {
            nextMarkAndStart()

            delay(100)

            while (isMarkPlay)
            ;
        }
    }

    fun nextMarkAndStart() {
        Log.d("VideoPlayer", "currentMarkPosition: $currentMarkPosition")
        delayJob?.cancel()
        markJob?.cancel()
        nextMark()?.let { markJob = startMark(it) }
        markJob?.start()
    }

    fun prevMarkAndStart() {
        Log.d("VideoPlayer", "currentMarkPosition: $currentMarkPosition")
        delayJob?.cancel()
        markJob?.cancel()
        prevMark()?.let { markJob = startMark(it) }
        markJob?.start()
    }

    private fun nextMark() = settings?.marks?.get(nextMarkPosition())

    private fun prevMark() = settings?.marks?.get(prevMarkPosition())

    private fun nextMarkPosition(): Int {
        return if(settings?.marks?.size!! - 1 > currentMarkPosition) {
            ++currentMarkPosition
        } else {
            currentMarkPosition = 0
            0
        }
    }

    private fun prevMarkPosition(): Int {
        return if(currentMarkPosition == 0) {
            currentMarkPosition = settings?.marks?.size!! - 1
            currentMarkPosition
        } else {
            --currentMarkPosition
        }
    }

    private fun startMark(mark: MarkDto) = scope.launch(start = CoroutineStart.LAZY, context = Dispatchers.IO) {
        isMarkPlay = true

        repeat(mark.repeat) {
            binding.videoView.seekTo(mark.start)

            if(!binding.videoView.isPlaying)
                binding.videoView.start()

            Log.d("VideoPlayer", "currentPosition start: ${binding.videoView.currentPosition}")
            val segmentLen = (mark.end - mark.start).toLong()
            Log.d("VideoPlayer", "delay: ${segmentLen}")

            delayJob = delay(mark.end)
            delayJob?.start()
            delayJob?.join()

            Log.d("VideoPlayer", "currentPosition end: ${binding.videoView.currentPosition}")

            if(mark.delay != 0) {
                binding.videoView.pause()
                delay(timeMillis = mark.delay * 1000L)
                binding.videoView.start()
            }
        }

        isMarkPlay = false
    }

    private fun newTimer() = object: CountDownTimer(2500, 2500) {
        override fun onTick(p0: Long) {
            binding.play.visibility = View.VISIBLE
            binding.prev.visibility = View.VISIBLE
            binding.next.visibility = View.VISIBLE
            binding.seekBar.visibility = View.VISIBLE
            binding.trackLength.visibility = View.VISIBLE
            binding.startTrackLength.visibility = View.VISIBLE
        }

        override fun onFinish() {
            binding.play.visibility = View.GONE
            binding.prev.visibility = View.GONE
            binding.next.visibility = View.GONE
            binding.seekBar.visibility = View.GONE
            binding.trackLength.visibility = View.GONE
            binding.startTrackLength.visibility = View.GONE
            timer?.cancel()
        }
    }

    fun setSpeed(spd: Float) {
        mediaPlayer?.playbackParams = mediaPlayer?.playbackParams?.apply {
            speed = spd
        }!!
    }

    fun currentMark() = settings?.marks?.get(currentMarkPosition)

    fun startSettings() {
        currentMarkPosition = -1
        playSetting.start()
    }

    fun stop() {
        markJob?.cancel()
        playSetting.cancel()
        timer?.cancel()
        timerTrackLength.cancel()
    }

    fun stopSettings() {
        markJob?.cancel()
        playSetting.cancel()
        playSetting = startCoroutineSettings()
    }

    fun setSettings(settings: SettingsDto) {
        this.settings = settings
    }
}