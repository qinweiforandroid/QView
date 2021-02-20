package com.qw.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static android.util.DisplayMetrics.DENSITY_MEDIUM;

public class BatteryView extends View {
    private int mWidth = 45;
    private int mHeight = 23;
    private int mMargin = 2;
    private int mBorder = 2;
    private int mHeadWidth = 4;
    private int mHeadHeight = 7;
    private float mRadius = 4.0F;

    private float mPower;
    private int count = 0;
    private boolean mIsCharging;

    private PowerConnectionReceiver mReceiver;
    private Handler handler = new Handler();
    private Runnable runnable;

    private RectF mMainRect;
    private RectF mHeadRect;

    private Paint paint1 = new Paint();
    private Paint paint2 = new Paint();
    private Paint paint = new Paint();
    private RectF rect = new RectF();

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        int[] attrsArray = new int[]{android.R.attr.layout_height};
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        float maxH = ta.getDimension(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        ta.recycle();
        int dpi = (int) (context.getResources().getDisplayMetrics().xdpi / DENSITY_MEDIUM + 0.5);
        maxH = maxH / dpi;
        if (maxH > 0 && maxH != mHeight) {
            float r = maxH / mHeight;
            mWidth = (int) (mWidth * r + 0.5);
            mHeight = (int) (mHeight * r + 0.5);
            mMargin = Math.max((int) (mMargin * r + 0.5), 1);
            mBorder = Math.max((int) (mBorder * r + 0.5), 1);
            mHeadWidth = (int) (mHeadWidth * r + 0.5);
            mHeadHeight = (int) (mHeadHeight * r + 0.5);
            mRadius = mRadius * r;
        }
        this.runnable = new NamelessClass_1();
        this.initView();
    }


    private void initView() {
        this.mHeadRect = new RectF((float) (this.mWidth - this.mBorder),
                (float) ((this.mHeight - this.mHeadHeight) / 2),
                (float) (this.mWidth - this.mBorder + this.mHeadWidth),
                (float) ((this.mHeight + this.mHeadHeight) / 2));
        float left = (float) this.mBorder;
        float top = (float) this.mBorder;
        float right = (float) (this.mWidth - this.mBorder);
        float bottom = (float) (this.mHeight - this.mBorder);
        this.mMainRect = new RectF(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint1 == null) {
            paint1 = new Paint();
        }
        paint1.setStyle(Style.FILL);
        paint1.setColor(Color.WHITE);
        canvas.drawRoundRect(this.mHeadRect, 0.0F, 0.0F, paint1);

        if (paint2 == null) {
            paint2 = new Paint();
        }
        paint2.setStyle(Style.STROKE);
        paint2.setStrokeWidth((float) this.mBorder);
        paint2.setColor(Color.WHITE);
        paint2.setAntiAlias(true);
        canvas.drawRoundRect(this.mMainRect, this.mRadius, this.mRadius, paint2);

        if (paint == null) {
            paint = new Paint();
        }
        if (this.mIsCharging) {
            paint.setColor(Color.GREEN);
        } else if ((double) this.mPower < 0.2D) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.GREEN);
        }

        int width = (int) (this.mPower * (this.mMainRect.width() - (float) (this.mMargin * 2)));
        int left = (int) (this.mMainRect.left + (float) this.mMargin);
        int right = (int) (this.mMainRect.left + (float) this.mMargin + (float) width);
        int top = (int) (this.mMainRect.top + (float) this.mMargin);
        int bottom = (int) (this.mMainRect.bottom - (float) this.mMargin);
        rect.set(left, top, right, bottom);
        canvas.drawRect(rect, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(this.mWidth + this.mHeadWidth, this.mHeight);
    }

    private void setPower(float power) {
        this.mPower = power;
        this.invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        this.mReceiver = new PowerConnectionReceiver();
        IntentFilter powerValueFilter = new IntentFilter();
        powerValueFilter.addAction("android.intent.action.BATTERY_CHANGED");
        powerValueFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        powerValueFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        this.getContext().registerReceiver(this.mReceiver, powerValueFilter);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        this.getContext().unregisterReceiver(this.mReceiver);
        this.count = 0;
        super.onDetachedFromWindow();
    }

    class PowerConnectionReceiver extends BroadcastReceiver {
        PowerConnectionReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", -1);
            int scale = intent.getIntExtra("scale", -1);
            int chargePlug = intent.getIntExtra("plugged", -1);
            boolean usbCharge = chargePlug == 2;
            boolean acCharge = chargePlug == 1;
            mIsCharging = usbCharge || acCharge;
            if ("android.intent.action.ACTION_POWER_CONNECTED".equals(intent.getAction())) {
                mIsCharging = true;
            } else if ("android.intent.action.ACTION_POWER_DISCONNECTED".equals(intent.getAction())) {
                mIsCharging = false;
            }

            if (runnable != null && scale != 0) {
                float powerValue = (float) level / (float) scale;
                if (mIsCharging && (double) powerValue != 1.0D) {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 600L);
                } else {
                    handler.removeCallbacks(runnable);
                    setPower(powerValue);
                }

            } else {
                Log.d("zhj", "setPower default value 50% ");
                setPower(0.5F);
            }
        }
    }

    class NamelessClass_1 implements Runnable {
        NamelessClass_1() {
        }

        @Override
        public void run() {
            count = count + 2;
            if (count > 10) {
                count = 0;
            }

            setPower((float) ((double) count * 0.1D));
            handler.postDelayed(this, 600L);
        }
    }
}