package com.qw.chart.v2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * create by qinwei at 2022/3/10 16:23
 */
public class Day45WeatherView extends View {

    private ArrayList<Curve> curves = new ArrayList<>();


    /**
     * 6行高
     */
    private int rows = 6;

    private ArrayList<RectF> rowsRect = new ArrayList<>();

    private String[] axisYLabels = {"日期", "降水", "-12°", "3°", "18°", "33°"};

    private String[] axisYDates = {"11/27", "12/2", "12/11", "12/20", "12/29", "1/7"};

    private int contentStartX = dp2px(32);

    public Day45WeatherView(Context context) {
        super(context);
    }

    public Day45WeatherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        Curve curve = new Curve();
        ArrayList<Integer> chart = new ArrayList<>();
        chart.add(32);
        chart.add(31);
        chart.add(33);
        chart.add(30);
        chart.add(28);
        chart.add(29);
        chart.add(30);
        chart.add(31);
        curve.setList(chart);
        curves.add(curve);
        chart = new ArrayList<>();
        chart.add(27);
        chart.add(26);
        chart.add(28);
        chart.add(25);
        chart.add(23);
        chart.add(24);
        chart.add(25);
        chart.add(26);
        curves.add(curve);
    }

    public void notifyDataChanged(ArrayList<Curve> curves) {
        curves.clear();
        curves.addAll(curves);
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#8016ACFF"));
        drawGuides(canvas);
        drawAxisYTemperature(canvas);
        drawAxisXDate(canvas);
        drawWater(canvas);
        drawLineChart(canvas);
        drawMessage(canvas);
    }

    /**
     * 点的数据
     */
    public final ArrayList<Point> points = new ArrayList<>();
    /**
     * 线的数据
     */
    public final ArrayList<Point[]> lines = new ArrayList<>();

    /**
     * 线画笔
     */
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void drawLineChart(Canvas canvas) {
        points.clear();
        lines.clear();
        float startX = contentStartX;
        float startY = rowsRect.get(2).bottom;
        int min = curves.get(0).getList().get(0);
        int max = curves.get(0).getList().get(0);
        for (Curve curve : curves) {
            for (int temperature : curve.getList()) {
                if (temperature > max) {
                    max = temperature;
                }
                if (min > temperature) {
                    min = temperature;
                }
            }
        }
        float temperatureRange = max - min;
        float drawHeight = dp2px(40);
        for (Curve curve : curves) {
            ArrayList<Integer> list = curve.getList();
            //计算温度点的数据
            for (int i = 0; i < list.size(); i++) {
                float pointX = getPaddingLeft() + i * dp2px(8);
                float pointY = drawHeight - drawHeight * ((list.get(i) - min) / temperatureRange);
                pointY += getPaddingTop();
                points.add(new Point((int) pointX, (int) pointY));
            }
            //根据点计算线的数据
            for (int i = 0; i < points.size() - 1; i++) {
                Point[] line = new Point[2];
                line[0] = points.get(i);
                line[1] = points.get(i + 1);
                lines.add(line);
            }
        }
        //todo 绘制曲线图表



    }

    private Paint axisYPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        axisYPaint.setTextSize(dp2px(12));
        axisYPaint.setColor(Color.parseColor("#B0B0B0"));
    }

    /**
     * 绘制Y轴温度刻度
     * 第一个刻度绘制降水label
     * 其他刻度绘制温度label
     *
     * @param canvas
     */
    private void drawAxisYTemperature(Canvas canvas) {
        for (int i = 0; i < rowsRect.size(); i++) {
            RectF rect = rowsRect.get(i);
            canvas.drawText(axisYLabels[i], rect.left, rect.bottom - dp2px(4), axisYPaint);
        }
    }

    private Paint guidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        PathEffect effect = new DashPathEffect(new float[]{5, 5, 5, 5}, 0);
        guidePaint.setPathEffect(effect);
        guidePaint.setColor(Color.parseColor("#B5C5EA"));
        guidePaint.setStrokeWidth(dp2px(2));
        guidePaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 绘制参考线
     *
     * @param canvas
     */
    private void drawGuides(Canvas canvas) {
        for (RectF rect : rowsRect) {
            float startX = rect.left;
            float startY = rect.bottom;
            float stopX = rect.right;
            float stopY = rect.bottom;
            canvas.drawLine(startX, startY, stopX, stopY, guidePaint);
        }
    }

    private Paint mWaterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        mWaterPaint.setStyle(Paint.Style.FILL);
        mWaterPaint.setColor(Color.parseColor("#12B0FF"));
    }

    private void drawWater(Canvas canvas) {
        int size = 45;
        int waterWidth = dp2px(6);
        float waterStartX = contentStartX;
        float space = (getWidth() - waterStartX - waterWidth * 45 - getPaddingRight()) / 44.0f;
        float bottom = rowsRect.get(1).bottom;
        for (int i = 0; i < size; i++) {
            canvas.drawArc(waterStartX, bottom - dp2px(7), waterStartX + waterWidth, bottom,
                    0, 360, false, mWaterPaint);
            waterStartX += waterWidth + space;
        }
    }


    private Paint axisXPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        axisXPaint.setTextSize(dp2px(12));
        axisXPaint.setColor(Color.parseColor("#4A4A4A"));
    }

    /**
     * 绘制日期刻度
     *
     * @param canvas
     */
    private void drawAxisXDate(Canvas canvas) {
        StringBuilder builder = new StringBuilder();
        for (String axisYDate : axisYDates) {
            builder.append(axisYDate);
        }
        //测量文字长度
        float length = axisXPaint.measureText(builder.toString());
        //计算空格
        float space = (getWidth() - contentStartX - length - getPaddingRight()) / 5.0f;
        float textStartX = contentStartX;
        for (int i = 0; i < axisYDates.length; i++) {
            canvas.drawText(axisYDates[i], textStartX, rowsRect.get(0).bottom - dp2px(4), axisXPaint);
            textStartX += axisXPaint.measureText(axisYDates[i] + space);
        }
    }

    private void drawMessage(Canvas canvas) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int startY = getHeight() - getPaddingBottom();
        int startX = getPaddingLeft();
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        float itemHeight = (float) ((getHeight() - getPaddingBottom() - getPaddingTop()) / (rows * 1.0));
        for (int i = 0; i < rows; i++) {
            rowsRect.add(new RectF(startX, startY, startX + width, startY - itemHeight * i));
        }
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}