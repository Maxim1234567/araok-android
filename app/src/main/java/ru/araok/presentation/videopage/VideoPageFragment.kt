package ru.araok.presentation.videopage

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.araok.R
import ru.araok.data.dto.MarkDto
import ru.araok.data.dto.SettingsDto
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

                binding.videoPlayer.setVideoPath(Uri.fromFile(file))
                binding.videoPlayer.play()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.start.setOnClickListener {
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

            val playerSetting = SettingsDto(
                marks = listOf(mark1, mark2, mark3, mark4)
            )

            binding.videoPlayer.setSettings(playerSetting)
            binding.videoPlayer.startSettings()
        }

        binding.mark.setOnClickListener {
            findNavController().navigate(R.id.video_page_to_mark_page)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = VideoPageFragment()
    }
}