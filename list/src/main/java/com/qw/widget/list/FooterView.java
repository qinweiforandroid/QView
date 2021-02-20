package com.qw.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * @author qinwei
 */
public class FooterView extends LinearLayout implements IFooter, View.OnClickListener {
    private ProgressBar mProgressBar;
    private TextView mFooterLabel;
    private OnFooterViewListener listener;


    public interface OnFooterViewListener {
        void onFooterClick();
    }

    public FooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public FooterView(Context context) {
        super(context);
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_pulltorefresh_footer, this);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mFooterLabel = (TextView) findViewById(R.id.mFooterLabel);
    }

    @Override
    public void onClick(View v) {
        listener.onFooterClick();
    }

    @Override
    public void onFooterChanged(int state) {
        setOnClickListener(null);
        switch (state) {
            case FAIL:
                setOnClickListener(this);
                mFooterLabel.setText("点击重试");
                mProgressBar.setVisibility(View.GONE);
                break;
            case IDLE:
                break;
            case ING:
                mFooterLabel.setText("正在加载");
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case EMPTY:
                mProgressBar.setVisibility(View.GONE);
                mFooterLabel.setText("--没有更多数据了--");
                break;
            default:
                break;
        }
    }

    public void setOnFooterViewListener(OnFooterViewListener listener) {
        this.listener = listener;
    }
}
