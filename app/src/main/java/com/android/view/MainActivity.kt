package com.android.view

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import com.android.view.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mGravityRadioGroup.setOnCheckedChangeListener { _: RadioGroup, checkedId: Int ->
            when (checkedId) {
                R.id.mGravityRadioButton1 -> {
                    binding.mQTextView.setTextAlign(Paint.Align.LEFT)
                    binding.mTextView.gravity = Gravity.START
                }
                R.id.mGravityRadioButton2 -> {
                    binding.mQTextView.setTextAlign(Paint.Align.CENTER)
                    binding.mTextView.gravity = Gravity.CENTER
                }
                R.id.mGravityRadioButton3 -> {
                    binding.mQTextView.setTextAlign(Paint.Align.RIGHT)
                    binding.mTextView.gravity = Gravity.END
                }
            }
        }
        binding.mColorRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val color = findViewById<RadioButton>(checkedId).currentTextColor
            binding.mTextView.setTextColor(color)
            binding.mQTextView.setTextColor(color)
        }
    }
}