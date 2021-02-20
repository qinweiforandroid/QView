package com.qw.widget.list.drag;

/**
 * item拖拽监听
 * Created by qinwei on 2018/1/29.
 */

public interface ItemDragViewHolderListener {
    /**
     * 开始拖拽
     */
    void onDragStart();

    /**
     * 结束拖拽
     */
    void onDragFinished();
}
