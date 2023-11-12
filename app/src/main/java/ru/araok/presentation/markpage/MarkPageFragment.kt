package ru.araok.presentation.markpage

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.araok.R
import ru.araok.data.dto.MarkDto
import ru.araok.data.dto.SettingsDto
import ru.araok.databinding.FragmentMarkPageBinding
import ru.araok.milliSecondsToTimer
import ru.araok.presentation.ViewModelFactory
import ru.araok.timerToMilliSeconds
import java.util.UUID
import javax.inject.Inject
import kotlin.streams.asStream

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

        binding.save.setOnClickListener {
            val settings = getSettings()
        }
    }

    private fun createNewMark() {
        val textViewPrefixTo = createTextView(resources.getString(R.string.prefix_to))
        val textViewPrefixFrom = createTextView(resources.getString(R.string.prefix_from))
        val textViewPrefixRepeat = createTextView(resources.getString(R.string.prefix_repeat))
        val textViewPrefixDelay = createTextView(resources.getString(R.string.prefix_delay))
        val editTextTo = createEditText(milliSecondsToTimer(binding.videoPlayer.currentPosition()))
        val editTextFrom = createEditText(milliSecondsToTimer(binding.videoPlayer.duration()))
        val editTextRepeat = createEditText("1")
        val editTextDelay = createEditText("1")
        val imageView = createImageView(R.drawable.mark_delete)
        val linearLayout = linearLayout()

        linearLayout.tag = UUID.randomUUID().toString()

        Log.d("MarkPageFragment", "uuid ${binding.llNewMarks.childCount}: ${linearLayout.tag}")

        linearLayout.addView(textViewPrefixTo)
        linearLayout.addView(editTextTo)
        linearLayout.addView(textViewPrefixFrom)
        linearLayout.addView(editTextFrom)
        linearLayout.addView(textViewPrefixRepeat)
        linearLayout.addView(editTextRepeat)
        linearLayout.addView(textViewPrefixDelay)
        linearLayout.addView(editTextDelay)
        linearLayout.addView(imageView)

        binding.llNewMarks.addView(linearLayout)

        imageView.tag = binding.llNewMarks.childCount
        imageView.setOnClickListener {
            Log.d("MarkPageFragment", "index: ${linearLayout.tag}")

            val view = binding.llNewMarks.children.asStream().filter { v -> v.tag == linearLayout.tag }.findFirst()

            if(view.isPresent) {
                binding.llNewMarks.removeView(view.get())
            }
        }
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
        textView.textSize = 20.0f
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

    private fun createImageView(image: Int): ImageView {
        val imageView = ImageView(context)
        imageView.setImageResource(image)
        imageView.layoutParams = LinearLayout.LayoutParams(
            100, 100, 1.0f
        )

        return imageView
    }

    private fun getSettings(): SettingsDto {
        val marks = ArrayList<MarkDto>()

        binding.llNewMarks.children.forEachIndexed { index, view ->
            val linearLayout = view as LinearLayout
            val editTextTo = linearLayout.getChildAt(1) as EditText
            val editTextFrom = linearLayout.getChildAt(3) as EditText
            val editTextRepeat = linearLayout.getChildAt(5) as EditText
            val editTextDelay = linearLayout.getChildAt(7) as EditText

            val mark = MarkDto(
                start = timerToMilliSeconds(editTextTo.text.toString()),
                end = timerToMilliSeconds(editTextFrom.text.toString()),
                repeat = editTextRepeat.text.toString().toInt(),
                delay = editTextDelay.text.toString().toInt()
            )

            marks[index] = mark
        }

        return SettingsDto(marks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MarkPageFragment()
    }
}