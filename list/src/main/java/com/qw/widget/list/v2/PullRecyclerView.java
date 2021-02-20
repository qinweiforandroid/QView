package com.qw.widget.list.v2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

/**
 * 基于SmartRefreshLayout库封装
 * Created by qinwei on 2019/5/30
 * email:qinwei_it@163.com
 *
 * @author qinwei
 */
public class PullRecyclerView extends SmartRefreshLayout {
    protected RecyclerView mRecyclerView;

    /**
     * 下拉刷新
     */
    public static final int MODE_PULL_TO_START = 0;
    /**
     * 底部加载更多
     */
    public static final int MODE_PULL_TO_END = 1;
    private OnPullRecyclerViewListener listener;
    private RecyclerView.LayoutManager layoutManager;
    private BaseListAdapter adapter;

    public static final int STATE_IDLE = 1;
    public static final int STATE_PULL_TO_START = 2;
    public static final int STATE_PULL_TO_END = 3;
    private int currentState = STATE_PULL_TO_START;


    public interface OnPullRecyclerViewListener {
        void onRefresh(int mode);
    }

    public PullRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public PullRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mRecyclerView = new RecyclerView(context);
        addView(mRecyclerView, new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        super.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentState = STATE_PULL_TO_START;
                listener.onRefresh(MODE_PULL_TO_START);
            }
        });
        super.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentState = STATE_PULL_TO_END;
                listener.onRefresh(MODE_PULL_TO_END);
            }
        });
    }

    public void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
        mRecyclerView.setAdapter(adapter);
    }


    public void setRefreshing() {
        super.autoRefresh();
    }

    public void onRefreshCompleted() {
        onRefreshCompleted(true, false);
    }

    public void onRefreshCompleted(boolean success, boolean noMoreData) {
        if (currentState == STATE_PULL_TO_START) {
            super.finishRefresh(300, success, noMoreData);
        } else {
            super.finishLoadMore(300, success, noMoreData);
        }
        currentState = STATE_IDLE;
    }

    public void setOnPullRecyclerViewListener(OnPullRecyclerViewListener listener) {
        this.listener = listener;
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRecyclerView.addOnScrollListener(listener);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        mRecyclerView.setItemAnimator(itemAnimator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }

    /**
     * 是否开启下拉刷新
     *
     * @param enablePullToStart
     */
    public void setEnablePullToStart(boolean enablePullToStart) {
        super.setEnableRefresh(enablePullToStart);
    }

    /**
     * 是否开启加载更多
     *
     * @param enablePullToEnd
     */
    public void setEnablePullToEnd(boolean enablePullToEnd) {
        super.setEnableLoadMore(enablePullToEnd);
    }

    /**
     * 是否开启自动加载更多
     *
     * @param enableAutoPullToEnd
     */
    public void setEnableAutoPullToEnd(boolean enableAutoPullToEnd) {
        super.setEnableAutoLoadMore(enableAutoPullToEnd);
    }
}