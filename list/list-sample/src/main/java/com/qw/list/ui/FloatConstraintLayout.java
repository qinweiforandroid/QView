package com.qw.list.ui;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewPropertyAnimator;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.qw.utils.Trace;

/**
 * Created by qinwei on 2021/5/18 11:04
 */
public class FloatConstraintLayout extends ConstraintLayout {
    private static final String TAG = "FloatLinearLayout";
    private ViewPropertyAnimator viewPropertyAnimator;
    private boolean isAnimatorPlaying;

    private int hide_mode = 0;
    private final int HIDE_MODE_LEFT = 0;
    private final int HIDE_MODE_RIGHT = 1;

    private int moveX;

    public FloatConstraintLayout(Context context) {
        super(context);
    }

    public FloatConstraintLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatConstraintLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void show() {
        Trace.d(TAG, "show TranslationX:" + getTranslationX());
        if (isAnimatorPlaying && getTranslationX() == 0) {
            return;
        }
        viewPropertyAnimator = animate().translationX(0);
        viewPropertyAnimator.setListener(animatorListener);
    }

    public void hide() {
        if (isAnimatorPlaying) {
            return;
        }
        if (getTranslationX() == getMoveX()) {
            return;
        }
        Trace.d(TAG, "hide start");
        viewPropertyAnimator = animate().translationX(getMoveX());
        viewPropertyAnimator.setListener(animatorListener);
    }

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isAnimatorPlaying = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimatorPlaying = false;
            Trace.d(TAG, "onAnimationEnd:" + getTranslationX());
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isAnimatorPlaying = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private float getMoveX() {
        if (hide_mode == HIDE_MODE_LEFT) {
            return -getWidth();
        } else {
            return getWidth();
        }
    }
}