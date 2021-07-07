package com.qw.list.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.qw.list.R;
import com.qw.list.base.BaseSectionListActivity;
import com.qw.list.vo.VideoVO;
import com.qw.widget.list.BaseViewHolder;
import com.qw.widget.list.section.SectionData;

/**
 * Created by qinwei on 2017/7/13.
 */

public class SectionListActivity extends BaseSectionListActivity<String, VideoVO> {

    private FloatConstraintLayout mFloatLinearLayout;

    @Override
    protected void setContentView() {
        setContentView(R.layout.section_list_activity, true);
    }

    @Override
    protected void initView() {
        super.initView();
        mFloatLinearLayout= (FloatConstraintLayout)findViewById(R.id.mFloatLinearLayout);
        mPullRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int touchSlop = ViewConfiguration.get(SectionListActivity.this).getScaledTouchSlop();

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    mFloatLinearLayout.show();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mFloatLinearLayout.hide();
            }
        });
    }

    @Override
    protected BaseViewHolder onCreateSectionHeaderView(ViewGroup parent) {
        return new BaseViewHolder(LayoutInflater.from(this).inflate(R.layout.layout_section_header, parent, false)) {
            @Override
            public void bindData(int position) {
                setText(R.id.mSectionLabel, modules.get(position).header);
            }
        };
    }

    @Override
    protected BaseViewHolder onCreateSectionItemView(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(this).inflate(R.layout.layout_section_item, parent, false)) {
            @Override
            public void bindData(int position) {
                setText(R.id.mSectionItemTitleLabel, modules.get(position).t.name);
            }
        };
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPullRecyclerView.setRefreshing();
    }

    @Override
    public void onRefresh(final int mode) {
        mPullRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                modules.add(new SectionData<String, VideoVO>("科幻", 0));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("哈利伯特")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("哈利伯特")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("哈利伯特")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("哈利伯特")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("哈利伯特")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("哈利伯特")));

                modules.add(new SectionData<String, VideoVO>("修仙", 1));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("仙剑奇侠传")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("仙剑奇侠传")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("仙剑奇侠传")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("仙剑奇侠传")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("仙剑奇侠传")));
                modules.add(new SectionData<String, VideoVO>("战争", 2));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("亮剑")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("亮剑")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("亮剑")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("亮剑")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("亮剑")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("亮剑")));
                modules.add(new SectionData<String, VideoVO>(new VideoVO("亮剑")));
                adapter.notifyDataSetChanged();
                mPullRecyclerView.onRefreshCompleted();
            }
        }, 2000);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return getGridLayoutManager(3);
    }
}
