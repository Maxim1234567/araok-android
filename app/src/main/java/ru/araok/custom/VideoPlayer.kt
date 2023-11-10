package ru.araok.custom

import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.util.Log
import android.widget.VideoView
import kotlinx.coroutines.*
import ru.araok.data.dto.MarkDto
import ru.araok.data.dto.PlayerSettingsDto

private const val magicNumber = 1540L

class VideoPlayer(
    private val videoView: VideoView
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private var playSetting: Job = startCoroutineSettings()
    private var markJob: Job? = null
    private var delayJob: Job? = null

    private var isMarkPlay = false
    private var settings: PlayerSettingsDto? = null
    private var currentMarkPosition = -1
    private lateinit var mediaPlayer: MediaPlayer

    val currentPosition
        get() = videoView.currentPosition

    init {
        videoView.setOnPreparedListener {
            mediaPlayer = it
        }
    }

    fun setVideoPath(path: Uri) {
        videoView.setVideoURI(path)
    }

    fun start() {
        videoView.start()
    }

    private fun delay(end: Int) = scope.launch(start = CoroutineStart.LAZY, context = Dispatchers.IO) {
        Log.d("VideoPlayer", "work start: $end")
        while (videoView.currentPosition <= end)
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
            videoView.seekTo(mark.start)

            if(!videoView.isPlaying)
                videoView.start()

            Log.d("VideoPlayer", "currentPosition start: ${videoView.currentPosition}")
            val segmentLen = (mark.end - mark.start).toLong()
            Log.d("VideoPlayer", "delay: ${segmentLen}")

            delayJob = delay(mark.end)
            delayJob?.start()
            delayJob?.join()

            Log.d("VideoPlayer", "currentPosition end: ${videoView.currentPosition}")

            if(mark.delay != 0) {
                videoView.pause()
                delay(timeMillis = mark.delay * 1000L)
                videoView.start()
            }
        }

        isMarkPlay = false
    }


    fun setSpeed(spd: Float) {
        mediaPlayer.playbackParams = mediaPlayer.playbackParams.apply {
            speed = spd
        }
    }

    fun currentMark() = settings?.marks?.get(currentMarkPosition)

    fun startSettings() {
        currentMarkPosition = -1
        playSetting.start()
    }

    fun isPlay() = videoView.isPlaying

    fun play() {
        videoView.start()
    }

    fun pause() {
        videoView.pause()
    }

    fun stopSettings() {
        markJob?.cancel()
        playSetting.cancel()
        playSetting = startCoroutineSettings()
    }

    fun setSettings(settings: PlayerSettingsDto) {
        this.settings = settings
    }
}