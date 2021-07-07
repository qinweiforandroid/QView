package com.qw.list.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.qw.list.R;
import com.qw.list.base.BaseListFragment;
import com.qw.list.viewmodel.InstalledAppVM;
import com.qw.list.vo.AppInfo;
import com.qw.widget.list.BaseViewHolder;

import java.util.ArrayList;

public class InstalledAppListFragment extends BaseListFragment<AppInfo> {
    private InstalledAppVM mVM;

    public static InstalledAppListFragment newInstance(boolean isSystem) {
        InstalledAppListFragment fragment = new InstalledAppListFragment();
        Bundle args = new Bundle();
        args.putBoolean("isSystem", isSystem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLazyLoadData();
    }

    @Override
    protected void initData() {
        mVM = new ViewModelProvider(this).get(InstalledAppVM.class);
        boolean isSystem = getArguments().getBoolean("isSystem");
        mVM.getAppInfoMediatorLiveData().observe(this, this::result);
        mVM.loadInstalledApp(isSystem);
    }

    private void result(ArrayList<AppInfo> list) {
        modules.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.installed_app_item_layout, parent, false));
    }

    class Holder extends BaseViewHolder {

        private final ImageView mAppIconImg;
        private final TextView mAppNameLabel;
        private final TextView mAppPackageNameLabel;
        private final Button mAppUninstallBtn;
        private AppInfo appInfo;

        public Holder(View itemView) {
            super(itemView);
            mAppIconImg = itemView.findViewById(R.id.mAppIconImg);
            mAppNameLabel = itemView.findViewById(R.id.mAppNameLabel);
            mAppPackageNameLabel = itemView.findViewById(R.id.mAppPackageNameLabel);
            mAppUninstallBtn = itemView.findViewById(R.id.mAppUninstallBtn);
            mAppUninstallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent uninstall_intent = new Intent();
                    uninstall_intent.setAction(Intent.ACTION_DELETE);
                    uninstall_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    uninstall_intent.setData(Uri.parse("package:" + appInfo.packageName));
                    getContext().startActivity(uninstall_intent);
                }
            });
        }

        @Override
        public void bindData(int position) {
            appInfo = modules.get(position);
            mAppIconImg.setImageDrawable(appInfo.icon);
            mAppNameLabel.setText(appInfo.name);
            mAppPackageNameLabel.setText(appInfo.packageName);
        }
    }
}