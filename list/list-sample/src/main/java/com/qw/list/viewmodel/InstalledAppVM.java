package com.qw.list.viewmodel;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import com.qw.list.vo.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class InstalledAppVM extends AndroidViewModel {
    private MediatorLiveData<ArrayList<AppInfo>> appInfoMediatorLiveData = new MediatorLiveData<>();

    public InstalledAppVM(@NonNull Application application) {
        super(application);
    }

    public void loadInstalledApp(boolean isSystem) {
        PackageManager packageManager = getApplication().getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        ArrayList<AppInfo> list = new ArrayList<>();
        new Thread(() -> {
            for (PackageInfo packageInfo : packages) {
                if (!isSystem && ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)) {
                    list.add(AppInfo.from(packageInfo, packageManager));
                }
                if (isSystem && ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)) {
                    list.add(AppInfo.from(packageInfo, packageManager));
                }
            }
            appInfoMediatorLiveData.postValue(list);
        }
        ).start();

    }

    public MediatorLiveData<ArrayList<AppInfo>> getAppInfoMediatorLiveData() {
        return appInfoMediatorLiveData;
    }
}
