package com.qw.list.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.qw.framework.base.viewpager.BaseViewPagerActivity;
import com.qw.list.R;

public class InstalledAppActivity extends BaseViewPagerActivity<String> {
    private TabLayout mTabLayout;

    @Override
    protected void setContentView() {
        setContentView(R.layout.list_installed_app_activity, true);
    }

    @Override
    protected void initView() {
        super.initView();
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        setTitle("软件管理");
        modules.add("用户软件");
        modules.add("系统预装");
        adapter.notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected CharSequence getPageTitleAtPosition(int position) {
        return modules.get(position);
    }

    @Override
    public Fragment getFragmentAtPosition(int position) {
        switch (position) {
            case 0:
                return InstalledAppListFragment.newInstance(false);
            case 1:
                return InstalledAppListFragment.newInstance(true);
        }
        return null;
    }
}
