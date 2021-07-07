package com.qw.list.repository;


import com.google.gson.Gson;
import com.qw.http.AbstractCallback;
import com.qw.http.HttpException;
import com.qw.utils.Trace;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by qinwei on 2016/4/21 14:34
 * email:qinwei_it@163.com
 */
public abstract class GankIoCallback<T> extends AbstractCallback<T> {
    @Override
    public T bindData(@NotNull String content) throws HttpException {
        Trace.e(content);
        try {
            Type type = this.getClass().getGenericSuperclass();
            type = ((ParameterizedType) type).getActualTypeArguments()[0];
            JSONObject obj = new JSONObject(content);
            int result = obj.getInt("status");
            if (result != 100) {
                throw new HttpException(HttpException.Type.SERVER, "manual");
            } else {
                return new Gson().fromJson(obj.opt("data").toString(), type);
            }
        } catch (JSONException e) {
            throw new HttpException(HttpException.Type.SERVER, e.getMessage());
        }
    }
}
