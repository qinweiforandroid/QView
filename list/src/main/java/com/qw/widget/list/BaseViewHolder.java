package com.qw.widget.list;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import com.qw.widget.list.drag.ItemDragViewHolderListener;


/**
 * Created by qinwei on 2016/9/25 14:27
 * email:qinwei_it@163.com
 *
 * @author qinwei
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements ItemDragViewHolderListener {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void setText(@IdRes int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }

    public void setText(@IdRes int id, int strId) {
        ((TextView) findViewById(id)).setText(strId);
    }

    public void setTextColor(@IdRes int id, int color) {
        ((TextView) findViewById(id)).setTextColor(color);
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    @Override
    public void onDragStart() {

    }

    @Override
    public void onDragFinished() {

    }

    public abstract void bindData(int position);
}
