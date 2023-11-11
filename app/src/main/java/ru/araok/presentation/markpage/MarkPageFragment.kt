package ru.araok.presentation.markpage

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.araok.R
import ru.araok.databinding.FragmentMarkPageBinding
import ru.araok.milliSecondsToTimer
import ru.araok.presentation.ViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class MarkPageFragment: Fragment() {
    private var _binding: FragmentMarkPageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MarkPageViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarkPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newMark.setOnClickListener {
            createNewMark()
        }
    }

    private fun createNewMark() {
        val textViewPrefixTo = createTextView(resources.getString(R.string.prefix_to))
        val textViewPrefixFrom = createTextView(resources.getString(R.string.prefix_from))
        val editTextTo = createEditText(milliSecondsToTimer(binding.videoPlayer.currentPosition()))
        val editTextFrom = createEditText(milliSecondsToTimer(binding.videoPlayer.duration()))
        val linearLayout = linearLayout()

        linearLayout.addView(textViewPrefixTo)
        linearLayout.addView(editTextTo)
        linearLayout.addView(textViewPrefixFrom)
        linearLayout.addView(editTextFrom)

        binding.llNewMarks.addView(linearLayout)
    }

    private fun linearLayout(): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.HORIZONTAL

        return linearLayout
    }

    private fun createTextView(prefix: String): TextView {
        val textView = TextView(context)
        textView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        textView.textSize = 25.0f
        textView.setTextColor(resources.getColor(R.color.black))
        textView.text = prefix
        textView.typeface = Typeface.DEFAULT_BOLD

        return textView
    }

    private fun createEditText(time: String): EditText {
        val editText = EditText(context)
        editText.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        editText.setText(time)

        return editText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MarkPageFragment()
    }
}