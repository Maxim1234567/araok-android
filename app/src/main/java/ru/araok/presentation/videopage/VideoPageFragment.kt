package ru.araok.presentation.videopage

import android.content.pm.PackageManager
import android.os.Bundle
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
import ru.araok.data.BASE_URL
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
            val outputDir = requireActivity().cacheDir
            val file = File.createTempFile("temp", ".mp4", outputDir)
            Log.d("VideoPageFragment", "ByteArray.size: " + it.size)
            file.writeBytes(it)

            Log.d("VideoPageFragment", "absolutePath: " + file.absolutePath)

            binding.videoView.setVideoPath(file.absolutePath)
            binding.videoView.start()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
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