package com.qw.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MoreTextView extends androidx.appcompat.widget.AppCompatTextView {
    private String mMoreText = "...全文";
    private String mText;

    public MoreTextView(@NonNull Context context) {
        super(context);
    }

    public MoreTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoreTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCustomText(String text) {
        super.setText(text);
        this.mText = text;
        this.mMoreText = "...全文";
        post(() -> {
            float moreWidth = getPaint().measureText(this.mMoreText);
            int lineCount;
            if (getLayout().getLineCount() < getMaxLines()) {
                lineCount = getLineCount();
            } else {
                lineCount = getMaxLines();
            }
            int lineEnd = getLayout().getLineEnd(lineCount - 1);
            int lineStart = getLayout().getLineStart(lineCount - 1);

            String lastLineText = this.mText.substring(lineStart, lineEnd);
            int lastCharIndex = calculateLastCharIndex(lastLineText, moreWidth, lineEnd);
            setText(this.mText.substring(0, lastCharIndex));
            append("...");
            SpannableString spannableString=new SpannableString("全文");
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            append(spannableString);
//            Log.d("qinwei", "lastLineText:" + lastLineText);
//            Log.d("qinwei", "line" + lineCount + " lineStart:" + lineStart + " char:" + getText().toString().charAt(lineStart));
//            Log.d("qinwei", "line" + lineCount + " lineEnd:" + lineEnd + " char:" + getText().toString().charAt(lineEnd - 1));
        });
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);

    }

    public void setMoreText(String text) {
        this.mMoreText = text;
    }

    /**
     * 获取计算后字符长度
     */
    private int calculateLastCharIndex(String lineText, float moreWidth, int lastCharIndex) {
        if (getPaint().measureText(lineText) + moreWidth > getLayout().getWidth()) {
            return calculateLastCharIndex(lineText.substring(0, lineText.length() - 1), moreWidth, lastCharIndex - 1);
        }
        return lastCharIndex;
    }
}
