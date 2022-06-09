package com.qw.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * 绘制 一组温度值，使用折线图进行展示
 * 方案：
 * 1.确定绘制区域的高度,每个温度之间的间距，最高温、最低温
 * 2.确定圆点的x，y值
 * 3.y值计算公式：高度* ( (温度-最低温)/ 温差 )
 * 4.x值计算公式：1:x 2:x+space  x*space
 * draw x轴线，y轴线
 */
public class LineChartView extends View {

    private final boolean isDebug = true;


    public LineChartView(Context context) {
        super(context);
        initView(context);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 相邻两点之间的间距
     */
    private int horizontalSpace = dip2px(50);

    /**
     * 图表的高度
     */
    private int drawHeight = dip2px(60);

    /**
     * 折线图的点高度集合
     */
    private final ArrayList<ArrayList<Integer>> charts = new ArrayList<>();

    private final ArrayList<Integer[]> bgColors = new ArrayList<>();

    /**
     * 一组曲线对象
     */
    private ArrayList<Curve> curves = new ArrayList<>();

    private void initView(Context context) {
        ArrayList<Integer> chart = new ArrayList<>();
        chart.add(32);
        chart.add(31);
        chart.add(33);
        chart.add(30);
        chart.add(28);
        chart.add(29);
        chart.add(30);
        chart.add(31);
        addLineChart(chart);
        bgColors.add(new Integer[]{Color.parseColor("#FF9E16"), Color.parseColor("#FFFFFF")});

        chart = new ArrayList<>();
        chart.add(27);
        chart.add(26);
        chart.add(28);
        chart.add(25);
        chart.add(23);
        chart.add(24);
        chart.add(25);
        chart.add(26);
        bgColors.add(new Integer[]{Color.parseColor("#5012B0FF"), Color.parseColor("#12B0FF")});
        addLineChart(chart);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long timestamp = System.currentTimeMillis();
        if (isDebug) {
            drawXY(canvas);
        }
        for (Curve curve : curves) {
            curve.onDraw(canvas);
        }
        d("onDraw:" + (System.currentTimeMillis() - timestamp));
    }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void drawXY(Canvas canvas) {
        int startX = getPaddingLeft();
        paint.setStrokeWidth(dip2px(2));
        paint.setColor(Color.BLUE);
        int startY = getPaddingTop();
        canvas.drawLine(startX, startY, startX, startY + drawHeight, paint);
        startY = getPaddingTop() + drawHeight;
        canvas.drawLine(startX, startY, getWidth(), startY, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        drawHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int min = charts.get(0).get(0);
        int max = charts.get(0).get(0);
        for (ArrayList<Integer> chart : charts) {
            for (int temperature : chart) {
                if (temperature > max) {
                    max = temperature;
                }
                if (min > temperature) {
                    min = temperature;
                }
            }
        }
        for (int i = 0; i < charts.size(); i++) {
            curves.add(covert(charts.get(i), min, max, bgColors.get(i)));
        }
    }


    private Curve covert(ArrayList<Integer> chart, int min, int max, Integer[] colors) {
        Curve curve = new Curve();
        curve.setStrokeWidth(dip2px(2));
        float temperatureRange = max - min;
        //计算温度点的数据
        for (int i = 0; i < chart.size(); i++) {
            float pointX = getPaddingLeft() + i * horizontalSpace;
            float pointY = drawHeight - drawHeight * ((chart.get(i) - min) / temperatureRange);
            pointY += getPaddingTop();
            curve.points.add(new Point((int) pointX, (int) pointY));
        }
        //根据点计算线的数据
        for (int i = 0; i < curve.points.size() - 1; i++) {
            Point[] line = new Point[2];
            line[0] = curve.points.get(i);
            line[1] = curve.points.get(i + 1);
            curve.lines.add(line);
        }
        curve.setChartHeight(getHeight())
                .setBgStartColor(colors[0])
                .setBgEndColor(colors[1]);
        curve.notifyDataChanged();
        return curve;
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void d(String msg) {
        if (isDebug) {
            Log.d("line", msg);
        }
    }

    public LineChartView setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
        return this;
    }

    public LineChartView addLineChart(ArrayList<Integer> chart) {
        charts.add(chart);
        return this;
    }

    public void notifyDataChanged() {
        requestLayout();
    }
}