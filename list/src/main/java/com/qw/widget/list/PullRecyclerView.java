package com.qw.widget.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * Created by qinwei on 2016/9/25 15:56
 * email:qinwei_it@163.com
 *
 * @author qinwei
 */

public class PullRecyclerView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {
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
    private boolean enablePullToStart;
    private boolean enablePullToEnd;
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (enablePullToEnd && currentState == STATE_IDLE && checkedIsNeedLoadMore()) {
                    if (listener != null) {
                        //上拉加载更多时设置下拉刷新不可用
                        setEnabled(false);
                        currentState = STATE_PULL_TO_END;
                        adapter.onFooterChanged(IFooter.ING);
                        listener.onRefresh(MODE_PULL_TO_END);
                    }
                }
            }
        });
        setEnablePullToStart(enablePullToStart);
        setOnRefreshListener(this);
    }

    private boolean checkedIsNeedLoadMore() {
        int lastVisiblePosition = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager sdlm = (StaggeredGridLayoutManager) layoutManager;
            lastVisiblePosition = sdlm.findLastCompletelyVisibleItemPositions(null)[sdlm.findLastCompletelyVisibleItemPositions(null).length - 1];
        }
        return adapter.getItemCount() - lastVisiblePosition <= 5 && adapter.isCanLoadMore();
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

    public void setEnablePullToStart(boolean enablePullToStart) {
        this.enablePullToStart = enablePullToStart;
        setEnabled(enablePullToStart);
    }

    public void setEnablePullToEnd(boolean enablePullToEnd) {
        this.enablePullToEnd = enablePullToEnd;
    }

    public void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        super.setColorSchemeResources(colorResIds);
    }


    public void setRefreshing() {
        currentState = STATE_PULL_TO_START;
        post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                onRefresh();
            }
        });
    }

    public void onRefreshCompleted() {
        onRefreshCompleted(IFooter.IDLE);
    }

    public void onRefreshCompleted(int footerState) {
        if (currentState == STATE_IDLE) {
            //容错处理，当前是空闲状态则无需调整ui
            return;
        }
        //保存当前的加载状态
        int state = currentState;
        currentState = STATE_IDLE;
        if (state == STATE_PULL_TO_START) {
            setRefreshing(false);
            if (enablePullToEnd) {
                adapter.onFooterChanged(footerState);
            }
        } else {
            setEnablePullToStart(enablePullToStart);
            adapter.onFooterChanged(footerState);
        }
    }

    public void setOnPullRecyclerViewListener(OnPullRecyclerViewListener listener) {
        this.listener = listener;
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRecyclerView.addOnScrollListener(listener);
    }

    @Override
    public void onRefresh() {
        if (listener != null) {
            currentState = STATE_PULL_TO_START;
            listener.onRefresh(MODE_PULL_TO_START);
        }
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
