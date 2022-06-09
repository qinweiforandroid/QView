package com.qw.chart.v2;


import java.util.ArrayList;

/**
 * 曲线对象
 * create by qinwei at 2022/3/10 14:14
 */
public class Curve {
    /**
     * 线粗细
     */
    private float strokeWidth;

    /**
     * 线颜色
     */
    private int strokeColor;

    /**
     * 背景色
     */
    private int startColorBg;

    private int endColorBg;

    private boolean isDrawCurveBg = false;

    /**
     * 图形数据
     */
    private ArrayList<Integer> list = new ArrayList<>();

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getStartColorBg() {
        return startColorBg;
    }

    public void setStartColorBg(int startColorBg) {
        this.startColorBg = startColorBg;
    }

    public int getEndColorBg() {
        return endColorBg;
    }

    public void setEndColorBg(int endColorBg) {
        this.endColorBg = endColorBg;
    }

    public boolean isDrawCurveBg() {
        return isDrawCurveBg;
    }

    public void setDrawCurveBg(boolean drawCurveBg) {
        isDrawCurveBg = drawCurveBg;
    }

    public ArrayList<Integer> getList() {
        return list;
    }

    public void setList(ArrayList<Integer> list) {
        this.list = list;
    }
}
