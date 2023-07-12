package com.example.voicebasedjourney

import QuestionFragment
import QuestionsPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.voicebasedjourney.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val fragments = arrayListOf(
            QuestionFragment.newInstance("What is your name?","Let's get started!","Enter Full name"),
            QuestionFragment.newInstance("What is your date of birth?","Great","Enter it in DD-MM-YYYY order"),
            QuestionFragment.newInstance("What is your monthly income?","Let's talk about money","How are you utillizing your time?")
        )

        val adapter = QuestionsPagerAdapter(this, fragments)

        binding.viewPager.adapter = adapter

    }
}