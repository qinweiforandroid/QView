package com.qw.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.view.setPadding

class AnnularView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 圓弧的描述数据
     */
    private val annulars: ArrayList<Annular> = ArrayList()

    /**
     * 弧形起始角度
     */
    private var startAngle = 0F

    /**
     * 圆弧所在的矩阵
     */
    private var mRectF: RectF = RectF()

    /**
     * 总进度
     */
    private var max = 0


    init {
        // 画线模式
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 36f
        //线头形状 ROUND 圆头
        mPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //缓存下一个圆弧的起始位置
        var curStartAngle = startAngle
        for (annular in annulars) {
            mPaint.color = annular.color
            val sweepAngle = sweep(annular.progress)
            canvas.drawArc(mRectF, curStartAngle, sweepAngle, false, mPaint)
            //更新下一個圆弧起始位置
            curStartAngle += sweepAngle
        }
    }

    /**
     * 根据进度计算划过的角度
     */
    private fun sweep(progress: Int): Float {
        return (progress / (this.max * 1.0) * 360).toInt().toFloat()
    }

    /**
     * 设置圆弧粗細
     */
    fun setStrokeWidth(width: Float): AnnularView {
        mPaint.strokeWidth = width
        return this
    }

    /**
     *  设置线头样式
     */
    fun setStrokeCap(cap: Paint.Cap): AnnularView {
        mPaint.strokeCap = cap
        return this
    }

    /**
     * 设置圆弧起始位置
     */
    fun setStartAngle(startAngle: Float): AnnularView {
        this.startAngle = startAngle
        return this
    }

    fun notifyDataChanged(annularList: java.util.ArrayList<Annular>) {
        this.annulars.clear()
        this.annulars.addAll(annularList)
        max = 0
        for (annular in annulars) {
            max += annular.progress
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setPadding((mPaint.strokeWidth/2.0).toInt())
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = width.coerceAtMost(height)
        mRectF.left = paddingLeft.toFloat()
        mRectF.top = paddingTop.toFloat()
        mRectF.right = (size - paddingRight).toFloat()
        mRectF.bottom = (size - paddingBottom).toFloat()
    }

    /**
     * 单个圆弧数据
     */
    data class Annular(var progress: Int, val color: Int)
}