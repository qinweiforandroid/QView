package com.qw.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;

import java.util.ArrayList;

/**
 * create by qinwei at 2022/3/10 14:14
 */
public class Curve {
    private final Path chartPath = new Path();

    private final Path graphBgPath = new Path();

    private float strokeWidth = 2;
    /**
     * 点的数据
     */
    public final ArrayList<Point> points = new ArrayList<>();
    /**
     * 线的数据
     */
    public final ArrayList<Point[]> lines = new ArrayList<>();

    /**
     * 点的颜色
     */
    private final int pointColor = Color.parseColor("#90FFFFFF");
    /**
     * 线的颜色
     */
    private final int lineColor = Color.parseColor("#16ACFF");

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 曲线封闭的背景
     */
    private final Paint bg = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int bgStartColor = Color.parseColor("#16ACFF");

    private int bgEndColor = Color.parseColor("#0012B0FF");

    public Curve setBgStartColor(int bgStartColor) {
        this.bgStartColor = bgStartColor;
        return this;
    }

    public Curve setBgEndColor(int bgEndColor) {
        this.bgEndColor = bgEndColor;
        return this;
    }

    private int mHeight;

    public Curve setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public void onDraw(Canvas canvas) {
        drawPoint(canvas);
        drawChartLine(canvas);
    }

    private void drawPoint(Canvas canvas) {
        paint.setColor(pointColor);
        paint.setStrokeWidth(16);
        paint.setStrokeCap(Paint.Cap.ROUND);
        for (Point point : points) {
            canvas.drawPoint(point.x, point.y, paint);
        }
    }

    private void drawChartLine(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
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
        graphBgPath.lineTo(lines.get(lines.size() - 1)[1].x, mHeight);
        graphBgPath.lineTo(0, mHeight);
        canvas.drawPath(graphBgPath, bg);
    }

    public Curve setChartHeight(int height) {
        this.mHeight = height;
        return this;
    }

    public void notifyDataChanged() {
        bg.setStrokeCap(Paint.Cap.ROUND);
        bg.setStyle(Paint.Style.FILL);
        bg.setShader(new LinearGradient(
                0F,
                0F,
                0F,
                mHeight,
                bgStartColor,
                bgEndColor,
                Shader.TileMode.CLAMP
        ));
    }
}
