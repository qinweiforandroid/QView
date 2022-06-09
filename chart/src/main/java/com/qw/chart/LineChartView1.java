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
public class LineChartView1 extends View {

    private final boolean isDebug = true;

    public LineChartView1(Context context) {
        super(context);
        initView(context);
    }

    public LineChartView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LineChartView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
    private final ArrayList<Integer> charts = new ArrayList<>();

    /**
     * 点的数据
     */
    private final ArrayList<Point> points = new ArrayList<>();
    /**
     * 线的数据
     */
    private final ArrayList<Point[]> lines = new ArrayList<>();

    /**
     * 点的颜色
     */
    private int pointColor = Color.parseColor("#90FFFFFF");
    /**
     * 线的颜色
     */
    private int lineColor = Color.parseColor("#16ACFF");

    private Path chartPath = new Path();

    private Path graphBgPath = new Path();

    private final Paint graphBg = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        graphBg.setStrokeCap(Paint.Cap.ROUND);
        graphBg.setStyle(Paint.Style.FILL);
    }

    /**
     * 一组曲线对象
     */
    private ArrayList<Curve> curves = new ArrayList<>();

    private void initView(Context context) {

        charts.add(32);
        charts.add(31);
        charts.add(33);
        charts.add(30);
        charts.add(28);
        charts.add(29);
        charts.add(30);
        charts.add(31);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long timestamp = System.currentTimeMillis();
        if (isDebug) {
            drawXY(canvas);
        }
        drawChartLine(canvas);
        drawPoint(canvas);
        d("onDraw:" + (System.currentTimeMillis() - timestamp));
    }

    private void drawPoint(Canvas canvas) {
        paint.setColor(pointColor);
        paint.setStrokeWidth(16);
        paint.setStrokeCap(Paint.Cap.ROUND);
        for (Point point : points) {
            canvas.drawPoint(point.x, point.y, paint);
        }
    }

    private void drawXY(Canvas canvas) {
        int startX = getPaddingLeft();
        paint.setStrokeWidth(dip2px(2));
        paint.setColor(Color.BLUE);
        int startY = getPaddingTop();
        canvas.drawLine(startX, startY, startX, startY + drawHeight, paint);
        startY = getPaddingTop() + drawHeight;
        canvas.drawLine(startX, startY, startX + lines.size() * horizontalSpace, startY, paint);
    }

    private void drawChartLine(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dip2px(2));
        paint.setColor(lineColor);

        chartPath.reset();
        chartPath.moveTo(lines.get(0)[0].x, lines.get(0)[0].y);
        for (Point[] line : lines) {
            float x1 = (line[1].x + line[0].x) / 2.0f;
            float y1 = line[0].y;
            float x2 = x1;
            float y2 = line[1].y;
            chartPath.cubicTo(x1, y1,
                    x2,
                    y2,
                    line[1].x,
                    line[1].y
            );
//            canvas.drawLine(line[0].x, line[0].y, line[1].x, line[1].y, paint);
        }
        canvas.drawPath(chartPath, paint);
        graphBgPath.set(chartPath);
        graphBgPath.lineTo(lines.get(lines.size() - 1)[1].x, drawHeight + getPaddingTop());
        graphBgPath.lineTo(0, drawHeight + getPaddingTop());
        canvas.drawPath(graphBgPath, graphBg);
    }

    public LineChartView1 setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
        return this;
    }

    public LineChartView1 setLineColor(int color) {
        this.lineColor = color;
        return this;
    }

    public LineChartView1 setPointColor(int color) {
        this.pointColor = color;
        return this;
    }

    public LineChartView1 initData(ArrayList<Integer> chartData) {
        charts.clear();
        charts.addAll(chartData);
        return this;
    }

    public void notifyDataChanged() {
        requestLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        drawHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        graphBg.setShader(new LinearGradient(
                0F,
                0F,
                0F,
                getMeasuredHeight(),
                Color.parseColor("#16ACFF"),
                Color.parseColor("#0012B0FF"),
                Shader.TileMode.CLAMP
        ));
        calculationData();
    }

    private void calculationData() {
        points.clear();
        lines.clear();
        int min = charts.get(0);
        int max = charts.get(0);
        for (int temperature : charts) {
            if (temperature > max) {
                max = temperature;
            }
            if (min > temperature) {
                min = temperature;
            }
        }
        float temperatureRange = max - min;
        //计算温度点的数据
        for (int i = 0; i < charts.size(); i++) {
            float pointX = getPaddingLeft() + i * horizontalSpace;
            float pointY = drawHeight - drawHeight * ((charts.get(i) - min) / temperatureRange);
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

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void d(String msg) {
        if (isDebug) {
            Log.d("line", msg);
        }
    }
}