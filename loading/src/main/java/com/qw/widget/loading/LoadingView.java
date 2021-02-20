package com.qw.widget.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * LoadingView解决了请求网络数据时ui显示的三种状态
 * 分别为加载中，加载失败，无数据
 * email: qinwei_it@163.com
 *
 * @author qinwei create by 2015/10/28
 * update time 2020/12/3
 */
public class LoadingView extends FrameLayout implements OnClickListener {

    private LinearLayout empty;
    private LinearLayout error;
    private LinearLayout loading;
    private State state;
    private OnRetryListener listener;
    /**
     * 由外部注入
     */
    private View contentView;

    public interface OnRetryListener {
        /**
         * 点击操作按钮回调
         */
        void onRetry();
    }

    public enum State {
        ing, error, done, empty
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public LoadingView(Context context) {
        super(context);
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_refresh_view, this);
        empty = (LinearLayout) findViewById(R.id.empty);
        loading = (LinearLayout) findViewById(R.id.loading);
        error = (LinearLayout) findViewById(R.id.error);
        notifyDataChanged(State.ing);
    }

    public void notifyDataChanged(State state) {
        this.state = state;
        switch (state) {
            case ing:
                empty.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                if (contentView != null) {
                    contentView.setVisibility(View.GONE);
                }
                break;
            case empty:
                loading.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                if (contentView != null) {
                    contentView.setVisibility(View.GONE);
                }
                break;
            case error:
                loading.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                if (contentView != null) {
                    contentView.setVisibility(View.GONE);
                }
                break;
            case done:
                setVisibility(View.GONE);
                if (contentView != null) {
                    contentView.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    public void setEmptyView(View view) {
        empty.removeAllViews();
        empty.addView(view, getLayoutParams());
    }

    public void setEmptyView(int layoutId) {
        View view = LayoutInflater.from(getContext()).inflate(layoutId, null);
        setEmptyView(view);
    }

    public void setLoadingView(View view) {
        loading.removeAllViews();
        loading.addView(view, getLayoutParams());
    }

    public void setErrorView(View view) {
        loading.removeAllViews();
        loading.addView(view, getLayoutParams());
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.listener = listener;
        if (listener != null) {
            View retry = findViewById(R.id.retry);
            if (retry != null) {
                retry.setOnClickListener(this);
            } else {
                setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null && state == State.error) {
            listener.onRetry();
        }
    }

    public void setUpContentView(View view) {
        this.contentView = view;
    }
}