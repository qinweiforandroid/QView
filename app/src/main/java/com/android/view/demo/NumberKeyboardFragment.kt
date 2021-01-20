package com.android.view.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.view.databinding.ViewNumberKeyboardFragmentBinding
import com.qw.framework.ui.BaseFragment

/**
 * Created by qinwei on 1/19/21 3:07 PM
 * email: qinwei_it@163.com
 */
class NumberKeyboardFragment() : BaseFragment(), View.OnClickListener {
    private lateinit var binding: ViewNumberKeyboardFragmentBinding
    lateinit var listener: OnNumberKeyClickListener
    override fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ViewNumberKeyboardFragmentBinding.inflate(inflater, container, false)
            .apply {
                binding = this
            }.root
    }

    override fun initView(view: View) {
        binding.mNK0.tag = "0"
        binding.mNK1.tag = "1"
        binding.mNK2.tag = "2"
        binding.mNK3.tag = "3"
        binding.mNK4.tag = "4"
        binding.mNK5.tag = "5"
        binding.mNK6.tag = "6"
        binding.mNK7.tag = "7"
        binding.mNK8.tag = "8"
        binding.mNK9.tag = "9"
        binding.mNKDot.tag = "."
        binding.mNKClear.tag = "c"
        binding.mNK0.setOnClickListener(this)
        binding.mNK1.setOnClickListener(this)
        binding.mNK2.setOnClickListener(this)
        binding.mNK3.setOnClickListener(this)
        binding.mNK4.setOnClickListener(this)
        binding.mNK5.setOnClickListener(this)
        binding.mNK6.setOnClickListener(this)
        binding.mNK7.setOnClickListener(this)
        binding.mNK8.setOnClickListener(this)
        binding.mNK9.setOnClickListener(this)
        binding.mNKDot.setOnClickListener(this)
        binding.mNKClear.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onClick(v: View) {
        var key = v.tag.toString()
        if (key == "c") {
            binding.mKNValueLabel.text = ""
        } else {
            binding.mKNValueLabel.append(key)
        }

    }

    fun setOnNumberKeyClickListener(listener: OnNumberKeyClickListener) {
        this.listener = listener
    }

    interface OnNumberKeyClickListener {
        fun onNumberKeyClick(key: String)
        fun onClearClick()
    }
}