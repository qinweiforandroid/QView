package com.android.view.demo.loading

import android.os.Bundle
import android.view.*
import com.android.view.R
import com.qw.framework.core.ui.BaseFragment
import com.qw.widget.loading.LoadingHelper
import com.qw.widget.loading.State

/**
 * Created by qinwei on 12/3/20 1:28 PM
 * email: qinwei_it@163.com
 */
class LoadingViewFragment : BaseFragment() {
    private lateinit var load: LoadingHelper
    override fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.demo_loading_fragment, container, false)
    }

    override fun initView(view: View) {
        load = LoadingHelper()
        load.inject(view.findViewById(R.id.mLoadingView))
        LoadingViewStyle.init(load)
        load.setContentView(view.findViewById(R.id.mContentLabel))
        load.notifyDataChanged(State.ing)
        load.setOnRetryListener { load.notifyDataChanged(State.ing) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_loading_view, menu)
    }

    override fun initData() {
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_content -> load!!.notifyDataChanged(State.done)
            R.id.action_nodata -> load!!.notifyDataChanged(State.empty)
            R.id.action_loading -> load!!.notifyDataChanged(State.ing)
            R.id.action_err -> load!!.notifyDataChanged(State.error)
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}