package com.qw.list.base;

import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qw.framework.core.ui.BaseActivity;
import com.qw.widget.list.BaseViewHolder;
import com.qw.widget.list.v2.BaseListAdapter;
import com.qw.widget.list.v2.PullRecyclerView;

import java.util.ArrayList;

public abstract class BaseListV2Activity<T> extends BaseActivity implements PullRecyclerView.OnPullRecyclerViewListener {
    protected PullRecyclerView mPullRecyclerView;
    protected ListAdapter adapter;
    protected ArrayList<T> modules = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.base_list_v2_activity, true);
    }

    @Override
    protected void initView() {
        mPullRecyclerView = (PullRecyclerView) findViewById(R.id.mPullRecyclerView);
        mPullRecyclerView.setOnPullRecyclerViewListener(this);
        adapter = new ListAdapter();
        mPullRecyclerView.setLayoutManager(getLayoutManager());
        mPullRecyclerView.setItemAnimator(getItemAnimator());
        mPullRecyclerView.setAdapter(adapter);
        setAdapter(adapter);
    }

    @Override
    public void onRefresh(int mode) {

    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    public RecyclerView.ItemAnimator getItemAnimator() {
        return new DefaultItemAnimator();
    }

    protected void setAdapter(BaseListAdapter adapter) {
        mPullRecyclerView.setAdapter(adapter);
    }

    public class ListAdapter extends BaseListAdapter {

        @Override
        protected BaseViewHolder onCreateHeaderItemHolder(ViewGroup parent) {
            return BaseListV2Activity.this.onCreateHeaderViewHolder(parent);
        }


        @Override
        protected int getItemViewCount() {
            return modules.size();
        }

        @Override
        protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
            return BaseListV2Activity.this.onCreateItemView(parent, viewType);
        }


        @Override
        protected int getItemViewTypeWithPosition(int position) {
            return BaseListV2Activity.this.getItemViewTypeWithPosition(position);
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
        final GridLayoutManager manager = new GridLayoutManager(this, spanCount);
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
