package com.qw.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.qw.utils.Trace
import java.util.*

/**
 * Created by qinwei on 1/4/21 1:44 PM
 * email: qinwei_it@163.com
 */
class QTextView : View {
    companion object {
        private const val CHAR_FEED_LINE = "\n"
    }

    private var texts: ArrayList<String> = ArrayList()
    private var mPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var text: String = "近日暴雨连连，天气闷热难耐，情绪也时好时坏。只是，每每想到你，想到我们在一起度过的那些天，幸福感便油然而生。"
    private var textSize = 26f.dp
    private var textColor = Color.BLACK

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    init {
        mPaint.color = Color.BLACK
        mPaint.textSize = textSize
        mPaint.textAlign = Paint.Align.LEFT
    }

    fun setText(text: String) {
        this.text = text
        invalidate()
    }

    fun setTextColor(color: Int) {
        this.textColor = color
        invalidate()
    }

    /**
     * 设置文字对齐方式
     */
    fun setTextAlign(align: Paint.Align) {
        mPaint.textAlign = align
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //处理测量部分
        val size = MeasureSpec.getSize(widthMeasureSpec)
        var w = size
        //换行处理
        texts.clear()
        for (item in text.split(CHAR_FEED_LINE)) {
            texts.addAll(splitText(mPaint, item, w - paddingLeft - paddingRight))
        }
        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                w = size
            }
            MeasureSpec.UNSPECIFIED,
            MeasureSpec.AT_MOST -> {
                w = if (texts.size > 0) {
                    size
                } else {
                    mPaint.measureText(text).toInt()
                }
            }
        }

        val h = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED,
            MeasureSpec.AT_MOST -> {
                (paddingTop + paddingBottom + getLineHeight() * texts.size).toInt()
            }
            else -> {
                MeasureSpec.getSize(heightMeasureSpec)
            }
        }
        setMeasuredDimension(w, h)
    }


    /**
     * 将字符拆分为多行
     *
     * @param paint 画笔对象
     * @param text  拆分前字符
     * @param width 一行可用的宽度
     * @return 拆分后字符集合
     */
    private fun splitText(paint: TextPaint, text: String, width: Int): ArrayList<String> {
        val texts = ArrayList<String>()
        var str = text
        var end: Int
        while (str.isNotEmpty()) {
            //width最多可显示字符数量
            end = str.length
            var subIndex = paint.breakText(
                str,
                0,
                end,
                true,
                width.toFloat(),
                null
            )

            //标点符号处理start
            //处理思路 check subIndex + 1字符是否是标点符号 如果是 end位置--
            var flag = true
            while (flag) {
                if (subIndex < str.length && isPunctuation(str[subIndex].toString())) {
                    subIndex--
                } else {
                    flag = false
                }
            }
            //end
            str = if (str.length == subIndex) {
                //字符长度
                texts.add(str)
                ""
            } else {
                texts.add(str.substring(0, subIndex))
                str.substring(subIndex)
            }
        }
        return texts
    }

    private fun isPunctuation(s: String): Boolean {
        var b = false
        var tmp = s
        tmp = tmp.replace("\\p{P}".toRegex(), "")
        if (s.length != tmp.length) {
            b = true
        }
        return b
    }

    private fun getLineHeight(): Float {
        return mPaint.fontSpacing
    }


    /**
     * 获取基准线
     */
    private fun baseline(): Float {
        return -mPaint.ascent() + paddingTop
    }

    /**
     * 文字对齐方式 drawText x值计算
     */
    private fun x(): Float =
        when (mPaint.textAlign) {
            Paint.Align.LEFT -> {
                paddingLeft.toFloat()
            }
            Paint.Align.CENTER -> {
                (measuredWidth - paddingLeft - paddingRight) / 2.0f + paddingLeft
            }
            Paint.Align.RIGHT -> {
                (measuredWidth - paddingRight).toFloat()
            }
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var baseline: Float
        for (i in 0 until texts.size) {
            baseline = baseline() + i * getLineHeight()
            mPaint.color = textColor
            canvas.drawText(texts[i], x(), baseline, mPaint)
            drawLine(canvas, baseline)
        }
    }

    private fun drawLine(canvas: Canvas, baseline: Float) {
        val fontMetrics = mPaint.fontMetrics
        val ascent = fontMetrics.ascent
        val descent = fontMetrics.descent
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        val leading = fontMetrics.leading
        Trace.d("fontMetrics\nbaseline:$baseline \nascent:$ascent\ndescent:$descent\ntop:$top\nbottom:$bottom\nleading:$leading\ntextSize:$textSize\nlineHeight:${getLineHeight()}")
        //ascent line
        mPaint.color = Color.GREEN
        canvas.drawLine(0f, baseline + ascent, width.toFloat(), baseline + ascent, mPaint)
        //baseline line
        mPaint.color = Color.BLACK
        canvas.drawLine(0f, baseline, width.toFloat(), baseline, mPaint)

        //descent line
        mPaint.color = Color.YELLOW
        canvas.drawLine(0f, baseline + descent, width.toFloat(), baseline + descent, mPaint)

        //top line
        mPaint.color = Color.BLUE
        canvas.drawLine(0f, baseline + top, width.toFloat(), baseline + top, mPaint)
        //bottom line
        mPaint.color = Color.RED
        canvas.drawLine(0f, baseline + bottom, width.toFloat(), baseline + bottom, mPaint)
    }

    private val Float.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )
}