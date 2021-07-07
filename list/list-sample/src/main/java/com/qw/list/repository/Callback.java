package com.qw.list.repository;

/**
 * Created by qinwei on 16/7/28 下午12:28
 */
public interface Callback<T> {
    void onSuccess(T data);

    void onFailure(int code, String msg);
}
