package com.android.view.demo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.view.databinding.AnnularFragmentBinding
import com.qw.framework.core.ui.BaseFragment
import com.qw.widget.AnnularView

class AnnularViewFragment : BaseFragment() {
    private lateinit var binding: AnnularFragmentBinding

    override fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AnnularFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView(view: View) {
        binding.mAnnularView.setStartAngle(30F)
        binding.mAnnularView.notifyDataChanged(ArrayList<AnnularView.Annular>().apply {
            //mock数据
            add(AnnularView.Annular(100, Color.parseColor("#FECE55")))
            add(AnnularView.Annular(100, Color.parseColor("#14D48B")))
            add(AnnularView.Annular(100, Color.parseColor("#FF5E77")))
        })
    }

    override fun initData() {

    }
}