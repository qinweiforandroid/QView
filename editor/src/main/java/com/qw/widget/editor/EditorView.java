package com.qw.widget.editor;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class EditorView extends LinearLayout implements TextWatcher, OnClickListener, OnFocusChangeListener {
    private EditText mEditorViewContentEdt;
    private ImageView mEditorViewClearImg;
    private TextView mEditorViewLabel;
    private OnTextChangedListener listener;
    private ImageView mEditorViewIcon;

    public interface OnTextChangedListener {
        void onTextChanged(CharSequence c);
    }

    public void setOnTextChangedListener(OnTextChangedListener listener) {
        this.listener = listener;
    }

    public EditorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context, attrs);
    }

    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context, attrs);
    }

    private void initializeView(Context context, AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_editor_view, this);
        mEditorViewLabel = (TextView) findViewById(R.id.mEditorViewLabel);
        mEditorViewContentEdt = (EditText) findViewById(R.id.mEditorViewContentEdt);
        mEditorViewIcon = (ImageView) findViewById(R.id.mEditorViewIcon);
        mEditorViewClearImg = (ImageView) findViewById(R.id.mEditorViewClearImg);
        mEditorViewClearImg.setOnClickListener(this);
        mEditorViewContentEdt.setOnFocusChangeListener(this);
        mEditorViewContentEdt.addTextChangedListener(this);
        mEditorViewClearImg.setVisibility(View.GONE);
        parseAttr(context, attrs);
        if (mEditorViewLabel.getText().toString().isEmpty() || !isEnabled()) {
            mEditorViewLabel.setVisibility(View.GONE);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (listener != null) {
            listener.onTextChanged(s);
        }
        if (TextUtils.isEmpty(s) || !mEditorViewContentEdt.isEnabled() || !isFocused()) {
            mEditorViewClearImg.setVisibility(View.GONE);
        } else {
            mEditorViewClearImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        mEditorViewContentEdt.setText(null);
    }

    public CharSequence getText() {
        return mEditorViewContentEdt.getText();
    }

    public void setHint(String hintText) {
        mEditorViewContentEdt.setHint(hintText);
    }

    public void setText(String string) {
        mEditorViewContentEdt.setText(string);
        Editable etext = mEditorViewContentEdt.getText();
        Selection.setSelection(etext, etext.length());
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void parseAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditorView);
        if (a.hasValue(R.styleable.EditorView_ev_hint)) {
            mEditorViewContentEdt.setHint(a.getString(R.styleable.EditorView_ev_hint));
        }
        if (a.hasValue(R.styleable.EditorView_android_inputType)) {
            mEditorViewContentEdt.setInputType(a.getInt(R.styleable.EditorView_android_inputType, InputType.TYPE_CLASS_TEXT));
        }
        if (a.hasValue(R.styleable.EditorView_android_gravity)) {
            mEditorViewContentEdt.setGravity(a.getInt(R.styleable.EditorView_android_gravity, Gravity.LEFT));
        }
        if (a.hasValue(R.styleable.EditorView_android_minEms)) {
            mEditorViewContentEdt.setMinEms(a.getInt(R.styleable.EditorView_android_minEms, -1));
        }
        if (a.hasValue(R.styleable.EditorView_android_maxEms)) {
            mEditorViewContentEdt.setMaxEms(a.getInt(R.styleable.EditorView_android_maxEms, Integer.MAX_VALUE));
        }
        if (a.hasValue(R.styleable.EditorView_android_maxLength)) {
            mEditorViewContentEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(a.getInt(R.styleable.EditorView_android_maxLength, Integer.MAX_VALUE))});
        }
        if (a.hasValue(R.styleable.EditorView_ev_text)) {
            mEditorViewContentEdt.setText(a.getString(R.styleable.EditorView_ev_text));
        }
        if (a.hasValue(R.styleable.EditorView_android_singleLine)) {
            mEditorViewContentEdt.setSingleLine(a.getBoolean(R.styleable.EditorView_android_singleLine, true));
        }

        if (a.hasValue(R.styleable.EditorView_ev_label)) {
            mEditorViewLabel.setText(a.getString(R.styleable.EditorView_ev_label));
            int left = dip2px(80);
            int right = dip2px(40);
            mEditorViewContentEdt.setPadding(left, 0, right, 0);
        } else {
            if (a.hasValue(R.styleable.EditorView_ev_left_icon)) {
                int left = dip2px(60);
                int right = dip2px(40);
                mEditorViewContentEdt.setPadding(left, 0, right, 0);
                mEditorViewIcon.setImageResource(a.getResourceId(R.styleable.EditorView_ev_left_icon, 0));
            } else {
                int left = dip2px(8);
                int right = dip2px(40);
                mEditorViewContentEdt.setPadding(left, 0, right, 0);
                mEditorViewIcon.setVisibility(View.GONE);
            }
        }

        if (a.hasValue(R.styleable.EditorView_ev_bg_editor)) {
            mEditorViewContentEdt.setBackgroundColor(a.getInt(R.styleable.EditorView_ev_bg_editor, android.R.color.white));
        }

        a.recycle();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue dip value
     * @return px
     */
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mEditorViewContentEdt.setEnabled(enabled);
        mEditorViewClearImg.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && !TextUtils.isEmpty(mEditorViewContentEdt.getText().toString()) && isEnabled()) {
            mEditorViewClearImg.setVisibility(View.VISIBLE);
        } else {
            mEditorViewClearImg.setVisibility(View.GONE);
        }
    }
}
