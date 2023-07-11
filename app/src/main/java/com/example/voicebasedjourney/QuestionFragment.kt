import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.voicebasedjourney.R
import com.example.voicebasedjourney.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(question: String,subHeading: String, editTextHint: String): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putString("question", question)
            args.putString("subHeading", subHeading)
            args.putString("editTextHint", editTextHint)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.questionTv.text = arguments?.getString("question")
        binding.subHeadingTv.text = arguments?.getString("subHeading")
        binding.answerEt.hint = arguments?.getString("editTextHint")

        binding.nextButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.plus(1) ?: 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
