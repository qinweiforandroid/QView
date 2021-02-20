package com.qw.widget.list.drag;

/**
 * Created by qinwei on 2018/1/29.
 */

public interface ItemDragListener {
    /**
     * 移动回调
     *
     * @param fromPosition
     * @param toPosition
     */
    void onMove(int fromPosition, int toPosition);

    /**
     * 左滑动回调
     *
     * @param position
     */
    void onSwiped(int position);

    boolean isSwipe();
}
