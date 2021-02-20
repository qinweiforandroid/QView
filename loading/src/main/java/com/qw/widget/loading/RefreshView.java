package com.qw.widget.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
 * update time 2016/11/16
 */
public class RefreshView extends FrameLayout implements OnClickListener {

    private LinearLayout empty;
    private LinearLayout error;
    private LinearLayout loading;
    private State state;
    private OnRetryListener listener;

    public interface OnRetryListener {
        /**
         * 点击操作按钮回调
         */
        void onRetry();
    }

    public enum State {
        ing, error, done, empty
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public RefreshView(Context context) {
        super(context);
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_refresh_view, this);
        empty = (LinearLayout) findViewById(R.id.empty);
        loading = (LinearLayout) findViewById(R.id.loading);
        error = (LinearLayout) findViewById(R.id.error);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int childCount = getChildCount();
        if (childCount != 4) {
            throw new IllegalArgumentException("you must set one child view in RefreshView");
        }
    }

    public void notifyDataChanged(State state) {
        this.state = state;
        switch (state) {
            case ing:
                empty.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                getContentView().setVisibility(GONE);
                loading.setVisibility(View.VISIBLE);
                break;
            case empty:
                loading.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                getContentView().setVisibility(GONE);
                empty.setVisibility(View.VISIBLE);
                break;
            case error:
                loading.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                getContentView().setVisibility(GONE);
                error.setVisibility(View.VISIBLE);
                break;
            case done:
                empty.setVisibility(View.GONE);
                //进度透明度由1到0变成全透明
                loading.animate()
                        .alpha(0)
                        .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                        .start();

                getContentView().setVisibility(View.VISIBLE);
                //内容设置可见-全透明
                getContentView().setAlpha(0f);
                //内容由透明度0-1的一个动画
                getContentView().animate()
                        .alpha(1)
                        .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                //内容透明度为1时隐藏进度
                                loading.setAlpha(1);
                                loading.setVisibility(View.GONE);
                            }
                        });
                break;
            default:
                break;
        }
    }

    private View getContentView() {
        return getChildAt(getChildCount() - 1);
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
}
