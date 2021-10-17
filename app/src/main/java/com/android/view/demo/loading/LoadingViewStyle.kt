package com.android.view.demo.loading

import com.android.view.R
import com.qw.widget.loading.LoadingHelper

/**
 * Created by qinwei on 2021/8/21 5:36 下午
 * email: qinwei_it@163.com
 */
object LoadingViewStyle {
    fun init(loadingView: LoadingHelper) {
        loadingView.setEmptyView(R.layout.state_view_empty_layout)
        loadingView.setErrorView(R.layout.state_view_error_layout)
        loadingView.setLoadingView(R.layout.state_view_loading_layout)
    }
}