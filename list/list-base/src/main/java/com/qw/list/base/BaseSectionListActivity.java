package com.qw.list.base;

import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.qw.widget.list.BaseViewHolder;
import com.qw.widget.list.section.SectionData;


/**
 * Created by qinwei on 2017/7/13.
 */

public abstract class BaseSectionListActivity<G, T> extends BaseListActivity<SectionData<G, T>> {
    public static final int TYPE_SECTION = 1;

    @Override
    protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        if (TYPE_SECTION == viewType) {
            return onCreateSectionHeaderView(parent);
        }
        return onCreateSectionItemView(parent, viewType);
    }

    protected abstract BaseViewHolder onCreateSectionHeaderView(ViewGroup parent);

    protected abstract BaseViewHolder onCreateSectionItemView(ViewGroup parent, int viewType);

    @Override
    protected int getItemViewTypeWithPosition(int position) {
        if (modules.get(position).isHeader) {
            return TYPE_SECTION;
        } else {
            return super.getItemViewTypeWithPosition(position);
        }
    }

    @Override
    public GridLayoutManager getGridLayoutManager(int spanCount) {
        final GridLayoutManager manager = new GridLayoutManager(this, spanCount);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.isHeaderShow(position) || adapter.isFooterShow(position) || isSectionHeader(position)) {
                    return manager.getSpanCount();
                }
                return 1;
            }
        });
        return manager;
    }

    protected boolean isSectionHeader(int position) {
        return modules.get(position).isHeader;
    }
}
