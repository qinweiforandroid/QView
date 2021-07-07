package com.qw.list.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qw.http.HttpException;
import com.qw.http.Request;
import com.qw.http.RequestManager;
import com.qw.list.repository.entities.Image;

import java.util.ArrayList;

/**
 * Created by qinwei on 2020/6/29 4:45 PM
 * email: qinwei_it@163.com
 */
public class GankRepository {
    public static void loadGankMeizhiByPage(int pageNum, final Callback<ArrayList<Image>> callback) {
        Request request = new Request("https://gank.io/api/v2/data/category/Girl/type/Girl/page/" + pageNum + "/count/20");
        request.setCallback(new GankIoCallback<ArrayList<Image>>() {
            @Nullable
            @Override
            public ArrayList<Image> preRequest() {
                return null;
            }

            @Nullable
            @Override
            public ArrayList<Image> postRequest(@NonNull ArrayList<Image> images) {
                return null;
            }

            @Override
            public void onSuccess(ArrayList<Image> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(HttpException e) {
                callback.onFailure(e.getStatusCode(), e.getMessage());
            }
        });
        RequestManager.INSTANCE.preformRequest(request);
    }
}
