package com.qw.list.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.qw.list.R
import com.qw.list.base.BaseListActivity
import com.qw.list.repository.entities.Image
import com.qw.list.viewmodel.SwipeRefreshViewModel
import com.qw.list.viewmodel.SwipeRefreshViewModel.ExposureUpload
import com.qw.utils.Trace
import com.qw.widget.list.BaseViewHolder
import com.qw.widget.list.IFooter
import com.qw.widget.list.PullRecyclerView
import java.util.*

/**
 * @author qinwei
 */
class SwipeRefreshLayoutActivity : BaseListActivity<Image>() {
    private lateinit var mListSupportSwipeRefreshViewModel: SwipeRefreshViewModel
    private lateinit var exposureUpload: ExposureUpload
    override fun initView() {
        super.initView()
        mPullRecyclerView.setItemAnimator(DefaultItemAnimator())
        mPullRecyclerView.layoutManager = getGridLayoutManager(2)
        mPullRecyclerView.setEnablePullToEnd(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        SwipeRefreshViewModel.ExposureTracker.getInstance().release()
    }

    override fun initData(savedInstanceState: Bundle?) {
        exposureUpload = ExposureUpload()
        SwipeRefreshViewModel.ExposureTracker.init(mPullRecyclerView.recyclerView)
        SwipeRefreshViewModel.ExposureTracker.getInstance().setOnExposureListener { firstPosition, lastPosition ->
            val uploadPositions = exposureUpload.getUploadPositions(firstPosition, lastPosition)
            //todo 上传曝光数据
            Trace.d("上传曝光数据position:$uploadPositions")
            exposureUpload.markUploadDone(uploadPositions)
        }
        mListSupportSwipeRefreshViewModel = ViewModelProvider(this).get(SwipeRefreshViewModel::class.java)
        mListSupportSwipeRefreshViewModel.errMsg.observe(this, { s: String -> this.error(s) })
        mListSupportSwipeRefreshViewModel.images.observe(this, { images: ArrayList<Image> -> onDataChanged(images) })
        mPullRecyclerView.setRefreshing()
    }

    private fun onDataChanged(images: ArrayList<Image>) {
        if (mListSupportSwipeRefreshViewModel.isRefresh) {
            modules.clear()
        }
        modules.addAll(images)
        adapter.notifyDataSetChanged()
        if (images.size < 20) {
            mPullRecyclerView.onRefreshCompleted(IFooter.EMPTY)
        } else {
            mPullRecyclerView.onRefreshCompleted()
        }
    }

    private fun error(s: String) {
        if (mListSupportSwipeRefreshViewModel.isRefresh) {
            mPullRecyclerView.onRefreshCompleted()
        } else {
            mPullRecyclerView.onRefreshCompleted(IFooter.FAIL)
        }
    }

    override fun onRefresh(mode: Int) {
        if (mode == PullRecyclerView.MODE_PULL_TO_START) {
            mListSupportSwipeRefreshViewModel.onRefresh()
        } else {
            mListSupportSwipeRefreshViewModel.onLoadMore()
        }
    }

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ImageHolder(LayoutInflater.from(this@SwipeRefreshLayoutActivity).inflate(R.layout.list_image_item, parent, false))
    }

    internal inner class ImageHolder(v: View) : BaseViewHolder(v), View.OnClickListener {
        private val mHomeItemIconImg: ImageView
        private lateinit var image: Image
        override fun bindData(position: Int) {
            image = modules[position]
            Glide.with(mHomeItemIconImg).load(image.url).into(mHomeItemIconImg)
        }

        override fun onClick(v: View) {
            startActivity(Intent(this@SwipeRefreshLayoutActivity, PhotoActivity::class.java).putExtra("key_image_url", image.url))
        }

        init {
            v.setOnClickListener(this)
            mHomeItemIconImg = v.findViewById<View>(R.id.mHomeItemIconImg) as ImageView
        }
    }
}