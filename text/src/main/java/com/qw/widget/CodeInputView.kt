package com.qw.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class CodeInputView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private val mStarPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 最大显示长度
     */
    private var maxLength = 4

    /**
     * 方格宽度
     */
    private var gridWidth = 100F

    /**
     * 格子之间空间
     */
    private var space = 40F

    /**
     * 方格数据
     */
    private val areas = ArrayList<RectF>()

    /**
     * 方格內容
     */
    private var text = "1234"

    /**
     * char集合
     */
    private var chars: CharArray

    /**
     * 文字是否隐藏
     */
    private var isTextHidden = true

    init {
        //背景画笔配置
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10F

        //文字画笔配置
        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = 64f

        //星画笔
        mStarPaint.strokeWidth = 20F
        mStarPaint.color = Color.BLACK

        setPadding(100, 100, 0, 0)

        var left = paddingLeft.toFloat()
        areas.clear()
        for (i in 1 until maxLength + 1) {
            val right = left + gridWidth
            areas.add(RectF(left, paddingTop.toFloat(), right, paddingTop + gridWidth))
            left = right + space
        }
        chars = text.toCharArray()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        if (isTextHidden) {
            drawStar(canvas)
        } else {
            drawText(canvas)
        }
    }


    private fun drawText(canvas: Canvas) {
        for (i in chars.indices) {
            val rectF = areas[i]
            chars[i].toString().let {
                canvas.drawText(
                    it,
                    getDrawTextX(it, rectF),
                    getDrawTextY(it, rectF), mTextPaint
                )
            }

        }
    }

    private fun drawStar(canvas: Canvas) {
        for (i in chars.indices) {
            val rectF = areas[i]
            canvas.drawPoint(
                (rectF.left + gridWidth / 2.0).toFloat(),
                (rectF.top + gridWidth / 2.0).toFloat(),
                mStarPaint
            )
        }
    }

    private fun getDrawTextX(s: String, rectF: RectF): Float {
        val marginLeft = (gridWidth - mTextPaint.measureText(s)) / 2.0
        return (rectF.left + marginLeft).toFloat()
    }

    private fun getDrawTextY(s: String, rectF: RectF): Float {
        return (baseline() + rectF.top + (gridWidth - mTextPaint.fontSpacing) / 2.0).toFloat()
    }

    /**
     * 获取基准线
     */
    private fun baseline(): Float {
        return -mTextPaint.ascent()
    }

    private fun drawBackground(canvas: Canvas) {
        for (area in areas) {
            canvas.drawRect(area, mPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}