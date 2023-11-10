package ru.araok.presentation.videopage

import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.araok.R
import ru.araok.custom.VideoPlayer
import ru.araok.data.BASE_URL
import ru.araok.data.dto.MarkDto
import ru.araok.data.dto.PlayerSettingsDto
import ru.araok.databinding.FragmentVideoPageBinding
import ru.araok.presentation.ViewModelFactory
import java.io.File
import javax.inject.Inject

const val CONTENT_ID = "contentId"

const val REQUEST_CODE_PERMISSION_VIDEO = 1001;

@AndroidEntryPoint
class VideoPageFragment: Fragment() {
    private var _binding: FragmentVideoPageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: VideoPageViewModel by viewModels { viewModelFactory }

    private val contentId: Long by lazy {
        arguments?.getLong(CONTENT_ID) ?: 0
    }

    private lateinit var videoPlayer: VideoPlayer

    private var timer: CountDownTimer? = null

    private val timerTrackLength: CountDownTimer = object: CountDownTimer(Long.MAX_VALUE, 1000) {
        override fun onTick(p0: Long) {
            binding.seekBar.max = binding.videoView.duration
            binding.seekBar.progress = videoPlayer.currentPosition
        }

        override fun onFinish() {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadVideo(contentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoPageBinding.inflate(inflater, container, false)

        videoPlayer = VideoPlayer(_binding!!.videoView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.video.onEach {
            if(it.isNotEmpty()) {
                val outputDir = requireActivity().cacheDir
                val file = File.createTempFile("temp", ".mp4", outputDir)
                file.writeBytes(it)

                Log.d("VideoPlayer", "file.absolutePath: ${file.absolutePath}")

                videoPlayer.setVideoPath(Uri.fromFile(file))
                videoPlayer.start()

                timerTrackLength.start()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.play.setOnClickListener {
            timer?.cancel()

            if(videoPlayer.isPlay())
                videoPlayer.pause()
            else
                videoPlayer.play()

            timer = newTimer()
            timer?.start()
        }

        binding.delete.setOnClickListener {
            videoPlayer.nextMarkAndStart()
        }

        binding.start.setOnClickListener {
            videoPlayer.prevMarkAndStart()
        }

        binding.speed.setOnClickListener {
            Log.d("VideoPlayer", "binding-speed-on-click-listener")
            videoPlayer.setSpeed(0.5f)
        }

        binding.cycles.setOnClickListener {
            Log.d("VideoPlayer", "binding-cycles-on-click-listener")
            videoPlayer.setSpeed(1.5f)
        }

        binding.videoView.setOnTouchListener { _, _ ->
            timer?.cancel()
            timer = newTimer()
            timer?.start()

            true
        }

        binding.mark.setOnClickListener {
            val mark1 = MarkDto(
                start = 5000,
                end = 8000,
                repeat = 3,
                delay = 2
            )

            val mark2 = MarkDto(
                start = 10000,
                end = 17000,
                repeat = 2,
                delay = 1
            )

            val mark3 = MarkDto(
                start = 15000,
                end = 23000,
                repeat = 4,
                delay = 3
            )

            val mark4 = MarkDto(
                start = 60000,
                end = 65000,
                repeat = 1,
                delay = 5
            )

            val playerSetting = PlayerSettingsDto(
                marks = listOf(mark1, mark2, mark3, mark4)
            )

            videoPlayer.setSettings(playerSetting)
            videoPlayer.startSettings()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE_PERMISSION_VIDEO -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()
                    viewModel.loadVideo(contentId)
                } else {
                    Toast.makeText(requireContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun newTimer() = object: CountDownTimer(2500, 2500) {
        override fun onTick(p0: Long) {
            binding.play.visibility = View.VISIBLE
            binding.seekBar.visibility = View.VISIBLE
            binding.trackLength.visibility = View.VISIBLE
        }

        override fun onFinish() {
            binding.play.visibility = View.GONE
            binding.seekBar.visibility = View.GONE
            binding.trackLength.visibility = View.GONE
            timer?.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = VideoPageFragment()
    }
}