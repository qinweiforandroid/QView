package com.qw.list.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.qw.list.R;
import com.qw.list.base.BaseListV2Activity;
import com.qw.utils.TimeHelper;
import com.qw.widget.list.BaseViewHolder;
import com.qw.widget.list.drag.ItemDragListener;
import com.qw.widget.list.drag.ItemTouchCallback;
import com.qw.widget.list.v2.PullRecyclerView;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import java.util.Collections;

/**
 * Created by qinwei on 2019-05-30 10:59
 * email: qinwei_it@163.com
 */
public class SmartRefreshLayoutActivity extends BaseListV2Activity<String> {
    static {
//        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(android.R.color.black, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        setMenuId(R.menu.menu_recyclerview);
//        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
//        mLinearSnapHelper.attachToRecyclerView(mPullRecyclerView.getRecyclerView());
        mPullRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.isHeaderShow = false;
        mPullRecyclerView.setEnablePullToEnd(true);
        mPullRecyclerView.setRefreshing();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchCallback(new ItemDragListener() {
            @Override
            public void onMove(int fromPosition, int toPosition) {
                Collections.swap(modules, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onSwiped(int position) {
                modules.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public boolean isSwipe() {
                return true;
            }
        }));
        itemTouchHelper.attachToRecyclerView(mPullRecyclerView.getRecyclerView());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add) {
            modules.add(0, "新增time=" + TimeHelper.getTime());
            adapter.notifyItemInserted(1);
        } else if (itemId == R.id.action_delete) {
            modules.remove(0);
            adapter.notifyItemRemoved(1);
        } else if (itemId == R.id.action_linearLayout) {
            mPullRecyclerView.setLayoutManager(getLayoutManager());
        } else if (itemId == R.id.action_gridLayout) {
            mPullRecyclerView.setLayoutManager(getGridLayoutManager(2));
        } else if (itemId == R.id.action_staggeredGridLayout) {
            mPullRecyclerView.setLayoutManager(getStaggeredGridLayoutManager(2));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh(final int mode) {
        if (mode == PullRecyclerView.MODE_PULL_TO_START) {
            mPullRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    modules.clear();
                    for (int i = 0; i < 30; i++) {
                        modules.add("你好 RecyclerView start" + i);
                    }
                    mPullRecyclerView.onRefreshCompleted();
                    adapter.notifyDataSetChanged();
                }
            }, 2000);
        } else {
            mPullRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int start = modules.size();
                    for (int i = start; i < start + 12; i++) {
                        modules.add("你好 RecyclerView END" + i);
                    }
                    if (modules.size() > 80) {
                        mPullRecyclerView.onRefreshCompleted(true, true);
                    } else {
                        mPullRecyclerView.onRefreshCompleted();
                    }
                    adapter.notifyDataSetChanged();

                }
            }, 2000);
        }
    }

    @Override
    protected BaseViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new BaseViewHolder(LayoutInflater.from(this).inflate(R.layout.message_header, parent, false)) {
            @Override
            public void bindData(int position) {

            }
        };
    }

    @Override
    protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(this).inflate(R.layout.layout_message_list_item, parent, false)) {
            @Override
            public void bindData(int position) {
                setText(R.id.mMessageItemTitleLabel, modules.get(position));
            }

            @Override
            public void onDragStart() {
                super.onDragStart();
                itemView.findViewById(R.id.mMessageItemTitleLabel).setBackgroundColor(Color.LTGRAY);
            }

            @Override
            public void onDragFinished() {
                super.onDragFinished();
                itemView.findViewById(R.id.mMessageItemTitleLabel).setBackgroundColor(Color.WHITE);
            }
        };
    }
}
