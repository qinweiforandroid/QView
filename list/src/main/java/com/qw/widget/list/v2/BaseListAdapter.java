package com.qw.widget.list.v2;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qw.widget.list.BaseViewHolder;


/**
 * Created by qinwei on 2016/9/25 15:21
 * email:qinwei_it@163.com
 *
 * @author qinwei
 */

public abstract class BaseListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    /**
     * 控制RecyclerView 头部显示隐藏
     */
    public boolean isHeaderShow;
    public static final int TYPE_HEADER = -1;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderShow && viewType == TYPE_HEADER) {
            return onCreateHeaderItemHolder(parent);
        } else {
            return onCreateItemView(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        //数据下标重新计算
        if (isHeaderShow) {
            position--;
        }
        holder.bindData(position);
    }

    public boolean isHeaderShow(int position) {
        return isHeaderShow && position == 0;
    }


    @Override
    public int getItemCount() {
        return getItemViewCount() + (isHeaderShow ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderShow(position)) {
            return TYPE_HEADER;
        }
        if (isHeaderShow) {
            position--;
        }
        return getItemViewTypeWithPosition(position);
    }

    /**
     * 获取item视图个数
     *
     * @return
     */
    protected abstract int getItemViewCount();

    /**
     * 获取item视图
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract BaseViewHolder onCreateItemView(ViewGroup parent, int viewType);

    /**
     * 获取头部视图
     *
     * @param parent
     * @return
     */
    protected abstract BaseViewHolder onCreateHeaderItemHolder(ViewGroup parent);


    /**
     * 根据position获取视图类型
     *
     * @param position
     * @return
     */
    protected int getItemViewTypeWithPosition(int position) {
        return 0;
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(isHeaderShow(holder.getLayoutPosition()));
        }
    }
}