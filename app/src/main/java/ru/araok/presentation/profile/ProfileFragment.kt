package ru.araok.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.araok.R
import ru.araok.data.Repository
import ru.araok.databinding.FragmentProfileBinding
import ru.araok.presentation.ViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(
            Repository.getAccessToken(requireContext()).isNotBlank() &&
            Repository.getRefreshToken(requireContext()).isNotBlank()) {
            binding.authorizeUser.visibility = View.VISIBLE
            binding.unauthorizeUser.visibility = View.GONE
        } else {
            binding.authorizeUser.visibility = View.GONE
            binding.unauthorizeUser.visibility = View.VISIBLE
        }

        binding.entry.setOnClickListener {
            findNavController().navigate(R.id.authorization)
        }

        binding.registration.setOnClickListener {
            findNavController().navigate(R.id.registration)
        }

        binding.exit.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}