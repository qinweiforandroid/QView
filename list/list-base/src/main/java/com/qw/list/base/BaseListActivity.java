package com.qw.list.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qw.framework.core.ui.BaseActivity;
import com.qw.widget.list.BaseListAdapter;
import com.qw.widget.list.BaseViewHolder;
import com.qw.widget.list.FooterView;
import com.qw.widget.list.IFooter;
import com.qw.widget.list.PullRecyclerView;

import java.util.ArrayList;

public abstract class BaseListActivity<T> extends BaseActivity implements PullRecyclerView.OnPullRecyclerViewListener, FooterView.OnFooterViewListener {
    protected PullRecyclerView mPullRecyclerView;
    protected ListAdapter adapter;
    protected ArrayList<T> modules = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.base_list_activity, true);
    }

    @Override
    protected void initView() {
        mPullRecyclerView = (PullRecyclerView) findViewById(R.id.mPullRecyclerView);
        if (mPullRecyclerView == null) {
            throw new IllegalArgumentException("id mPullRecyclerView is not found");
        }
        mPullRecyclerView.setOnPullRecyclerViewListener(this);
        setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        adapter = new ListAdapter();
        mPullRecyclerView.setLayoutManager(getLayoutManager());
        mPullRecyclerView.setItemAnimator(getItemAnimator());
        mPullRecyclerView.setAdapter(adapter);
        setAdapter(adapter);
    }

    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        mPullRecyclerView.setColorSchemeResources(colorResIds);
    }

    @Override
    public void onRefresh(int mode) {
    }

    @Override
    public void onFooterClick() {
//        加载更多失败点击view的回调函数
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
            return BaseListActivity.this.onCreateHeaderViewHolder(parent);
        }

        @Override
        protected BaseViewHolder onCreateFooterItemHolder(ViewGroup parent) {
            return new FooterViewHolder(BaseListActivity.this.onCreateFooterView(parent));
        }

        @Override
        protected int getItemViewCount() {
            return modules.size();
        }

        @Override
        protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
            return BaseListActivity.this.onCreateItemView(parent, viewType);
        }


        @Override
        protected int getItemViewTypeWithPosition(int position) {
            return BaseListActivity.this.getItemViewTypeWithPosition(position);
        }

        public class FooterViewHolder extends BaseViewHolder {
            private IFooter iFooter;

            public FooterViewHolder(View itemView) {
                super(itemView);
                if (itemView instanceof IFooter) {
                    iFooter = (IFooter) itemView;
                } else {
                    throw new IllegalArgumentException("the view must impl IFooter interface");
                }
            }

            @Override
            public void bindData(int position) {
                onFooterChanged(state);
            }

            public void onFooterChanged(int state) {
                iFooter.onFooterChanged(state);
            }
        }
    }

    protected View onCreateFooterView(ViewGroup parent) {
        FooterView footerView = new FooterView(this);
        footerView.setOnFooterViewListener(this);
        footerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return footerView;
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
                if (adapter.isHeaderShow(position) || adapter.isFooterShow(position)) {
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
