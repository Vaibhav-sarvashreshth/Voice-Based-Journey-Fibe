import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.voicebasedjourney.R
import com.example.voicebasedjourney.databinding.FragmentQuestionBinding
import java.util.Locale
import java.util.*

class QuestionFragment : Fragment(){

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private val SPEECH_REQUEST_CODE = 101
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var btnPlay: Button
    private lateinit var voiceBtn1:ImageButton
    private var isTtsInitialized = false

    companion object {
        fun newInstance(
            question: String,
            subHeading: String,
            editTextHint: String,
            ): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putString("question", question)
            args.putString("subHeading", subHeading)
            args.putString("editTextHint", editTextHint)

            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        val question = arguments?.getString("question")
        val subHeading = arguments?.getString("subHeading")
        binding.questionTv.text = question
        binding.subHeadingTv.text = subHeading
        binding.answerEt.hint = arguments?.getString("editTextHint")

        binding.nextButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.plus(1) ?: 0
        }

        btnPlay = binding.btnPlay
        voiceBtn1=binding.voiceBtn1

        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Language not supported, handle error
                } else {
                    isTtsInitialized = true
                    // Enable the button once TTS is initialized
                    btnPlay.isEnabled = true
                    if (isAdded) {
                        val text = "$subHeading\n$question"
                        speakText(text) {
                            startSpeechToText()
                        }
                    }
                }
            } else {
                // Initialization failed, handle error
            }

            textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}

                override fun onDone(utteranceId: String?) {
                    activity?.runOnUiThread(Runnable {
                        voiceBtn1.performClick()
                    })
                }

                override fun onError(utteranceId: String?) {}
            })
        }

        btnPlay.setOnClickListener {
            if (isTtsInitialized) {
                val text = "$subHeading\n$question"
                speakText(text){
                    startSpeechToText()
                }
            }
        }

        // Disable the button until TTS is initialized
        btnPlay.isEnabled = false


        binding.voiceBtn1.setOnClickListener {
            startSpeechToText()
        }


    }

    private fun speakText(text: String,onSpeechCompleted:() ->Unit) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your answer")
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0)
            binding.answerEt.setText(spokenText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        textToSpeech.stop()
        textToSpeech.shutdown()

    }



}






