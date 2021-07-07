package com.qw.list.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qw.framework.core.ui.BaseFragment;
import com.qw.widget.list.BaseViewHolder;
import com.qw.widget.list.v2.BaseListAdapter;
import com.qw.widget.list.v2.PullRecyclerView;

import java.util.ArrayList;

public abstract class BaseListV2Fragment<T> extends BaseFragment implements PullRecyclerView.OnPullRecyclerViewListener {
    protected PullRecyclerView mPullRecyclerView;
    protected ListAdapter adapter;
    protected ArrayList<T> modules = new ArrayList<>();

    @Override
    protected View getCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_list_activity, container, false);
    }

    @Override
    protected void initView(@NonNull View v) {
        mPullRecyclerView = (PullRecyclerView) v.findViewById(R.id.mPullRecyclerView);
        mPullRecyclerView.setOnPullRecyclerViewListener(this);
        adapter = new ListAdapter();
        mPullRecyclerView.setLayoutManager(getLayoutManager());
        mPullRecyclerView.setItemAnimator(getItemAnimator());
        mPullRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh(int mode) {
    }


    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    public RecyclerView.ItemAnimator getItemAnimator() {
        return new DefaultItemAnimator();
    }

    public class ListAdapter extends BaseListAdapter {

        @Override
        protected BaseViewHolder onCreateHeaderItemHolder(ViewGroup parent) {
            return BaseListV2Fragment.this.onCreateHeaderViewHolder(parent);
        }


        @Override
        protected int getItemViewCount() {
            return modules.size();
        }

        @Override
        protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
            return BaseListV2Fragment.this.onCreateItemView(parent, viewType);
        }


        @Override
        protected int getItemViewTypeWithPosition(int position) {
            return BaseListV2Fragment.this.getItemViewTypeWithPosition(position);
        }

    }


    protected BaseViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    protected int getItemViewTypeWithPosition(int position) {
        return 0;
    }

    /**
     * 获取item视图
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract BaseViewHolder onCreateItemView(ViewGroup parent, int viewType);

    /**
     * 得到GridLayoutManager
     *
     * @param spanCount 列数
     * @return
     */
    public GridLayoutManager getGridLayoutManager(int spanCount) {
        final GridLayoutManager manager = new GridLayoutManager(getContext(), spanCount);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.isHeaderShow(position)) {
                    return manager.getSpanCount();
                }
                return 1;
            }
        });
        return manager;
    }

    /**
     * 得到StaggeredGridLayoutManager
     *
     * @param spanCount 列数
     * @return
     */
    public StaggeredGridLayoutManager getStaggeredGridLayoutManager(int spanCount) {
        return new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
    }
}
