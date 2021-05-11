package com.android.view.demo

import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.android.view.R
import com.android.view.databinding.ViewTextFragmentBinding
import com.qw.framework.core.ui.BaseFragment

/**
 * Created by qinwei on 1/19/21 2:44 PM
 * email: qinwei_it@163.com
 */
class TextFragment : BaseFragment() {
    private lateinit var binding: ViewTextFragmentBinding
    override fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ViewTextFragmentBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    override fun initView(view: View) {
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

    override fun initData() {
    }
}