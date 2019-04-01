package com.zyc.doctor.widgets.imagePreview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zyc.doctor.R;

/**
 * @author dundun
 * @date 16/10/21
 */
public class ImageLoadingView extends View {
    private Context mContext;
    private Paint mCirclePaint;
    private float width, height;
    /**
     * 加载动画圆点的半径
     */
    private float pointRadius;
    /**
     * 加载动画圆点颜色
     */
    private int pointColor;
    /**
     * 小圆点个数
     */
    private int pointNum;
    /**
     * 小圆点间距
     */
    private int pointDis = 5;
    /**
     * 加载动画半径
     */
    private float loadingViewRadius;
    /**
     * 加载动画速度
     */
    private int loadingViewSpeed;
    /**
     * 画布转动角度
     */
    private float canvasAngle;

    public ImageLoadingView(Context context) {
        this(context, null);
    }

    public ImageLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.ImageLoadingView);
        pointRadius = array.getDimension(R.styleable.ImageLoadingView_pointRadius, 6);
        pointColor = array.getColor(R.styleable.ImageLoadingView_pointColor, Color.parseColor("#ffffff"));
        loadingViewRadius = array.getDimension(R.styleable.ImageLoadingView_loadingVIewRadius, 15);
        loadingViewSpeed = array.getInteger(R.styleable.ImageLoadingView_loadingSpeed, 5);
        array.recycle();
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(pointColor);
        startAnim();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        calcPointNum();
    }

    /**
     * 根据小圆点大小，以及加载动画圈的大小，计算出最多可容纳的圆点个数
     */
    private void calcPointNum() {
        //加载动画最大半径
        float radiusMax = Math.min(width, height) / 2 - dip2px(pointRadius);
        //动画最大半径不能超过限制范围
        float radiusReal = Math.min(radiusMax, dip2px(loadingViewRadius));
        loadingViewRadius = radiusReal;
        //圆周长
        float circum = (float)Math.PI * radiusReal * 2;
        pointNum = (int)(circum / (dip2px(pointRadius) * 2 + dip2px(pointDis)));
    }

    private void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 360);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                canvasAngle = Float.parseFloat(animation.getAnimatedValue().toString());
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float angle = 360f / pointNum;
        canvas.save();
        canvas.rotate(-canvasAngle, width / 2, height / 2);
        for (int i = 0; i < pointNum; i++) {
            float y = height / 2 + (float)(Math.sin(Math.toRadians(angle * i)) * loadingViewRadius);
            float x = width / 2 + (float)(Math.cos(Math.toRadians(angle * i)) * loadingViewRadius);
            int alpha = (int)(255 - 255f / pointNum * i);
            mCirclePaint.setAlpha(alpha);
            canvas.drawCircle(x, y, pointRadius, mCirclePaint);
        }
        canvas.restore();
        super.onDraw(canvas);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}

