package com.android.view.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.view.databinding.ViewEditorFragmentBinding
import com.qw.framework.core.ui.BaseFragment

/**
 * Created by qinwei on 2021/6/29 11:41
 */
class EditorFragment : BaseFragment() {
    private lateinit var bind: ViewEditorFragmentBinding

    override fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ViewEditorFragmentBinding.inflate(inflater, container, false).apply {
            bind = this
        }.root
    }

    override fun initView(view: View) {

    }

    override fun initData() {
    }
}