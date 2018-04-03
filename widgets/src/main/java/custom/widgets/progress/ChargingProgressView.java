package custom.widgets.progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import custom.base.data.ElectricCurrent;
import custom.widgets.R;

/**
 * Created by dundun on 16/4/18.
 */
public class ChargingProgressView extends View {
    /**
     * 绿色充电进度条
     */
    private Paint paint_blue;
    /**
     * 绿色充电百分比
     */
    private Paint paint_blue_percent;
    /**
     * 文字说明
     */
    private Paint paint_gray_txt;
    /**
     * 浅灰色背景
     */
    private Paint paint_gray_light;
    /**
     * 深灰色背景
     */
    private Paint paint_gray_dark;

    /**
     * bitmap
     */
    private Paint paint_bitmap;

    /**
     * 圆角矩形
     */
    private RectF rectF;

    /**
     * 交流电充电状态图
     */
    private Bitmap powerOneBitmap, powerTwoBitmap, powerThreeBitmap, powerFourBitmap;
    /**
     * 电池状态
     */
    private int bitmapNo = 1;

    /**
     * 控件的宽高
     */
    private int viewWidth, viewHeight;

    /**
     * 圆弧的角度大小
     */
    private final int ARC_ANDLE = 270;

    /**
     * 进度条值
     */
    private float progressValue;

    /**
     * 已充电时间
     */
    private String chargedTime = "00:00:00";

    private Paint.FontMetrics fontMetrics;

    /**
     * 默认充电类型为交流电
     */
    private ElectricCurrent electricityCode = ElectricCurrent.AC;

    /**
     * percent文字高度
     */
    private float fontTotalHeight;
    /**
     * 矩形内切圆半径
     */
    private int radius;

    public ChargingProgressView(Context context) {
        super(context);
        initPaint();

        initBitmap();
    }

    public ChargingProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();

        initBitmap();
    }
//

    /**
     * 画笔初始化
     */
    private void initPaint() {
        rectF = new RectF();

        paint_gray_light = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_gray_light.setColor(Color.rgb(224, 227, 232));
        paint_gray_light.setStyle(Paint.Style.STROKE);
        paint_gray_light.setStrokeCap(Paint.Cap.ROUND);

        paint_gray_dark = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_gray_dark.setColor(Color.rgb(126, 126, 126));
        paint_gray_dark.setStyle(Paint.Style.STROKE);
        paint_gray_dark.setStrokeCap(Paint.Cap.ROUND);

        paint_blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_blue.setColor(Color.rgb(107, 207, 47));
        paint_blue.setStyle(Paint.Style.STROKE);
        paint_blue.setStrokeCap(Paint.Cap.ROUND);

        paint_blue_percent = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_blue_percent.setColor(Color.rgb(107, 207, 47));
        paint_blue_percent.setTextAlign(Paint.Align.CENTER);

        paint_gray_txt = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_gray_txt.setColor(Color.rgb(176, 178, 182));
        paint_gray_txt.setTextAlign(Paint.Align.CENTER);

        paint_bitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_bitmap.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * bitmap
     */
    private void initBitmap() {
        powerOneBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_power_one);
        powerTwoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_power_two);
        powerThreeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_power_three);
        powerFourBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_power_four);
    }


    /**
     * 更新圆弧进度条 (直流电)
     *
     * @param value
     */
    public void setProgressValue(float value, String time) {
        progressValue = value * ARC_ANDLE;
        chargedTime = time;
        invalidate();
    }

    public void setChargerType(ElectricCurrent electricityCode) {
        this.electricityCode = electricityCode;
    }

    /**
     * 更改电池状态(交流电)
     *
     * @param value
     */
    public void setPowerValue(int value, String time) {
        bitmapNo = value;
        chargedTime = time;
        progressValue = ARC_ANDLE;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int minValue = Math.min(viewWidth, viewHeight);
        //矩形内切圆半径
        radius = minValue / 2;


        //已充电时间
        paint_gray_txt.setTextSize(radius / 6);
        paint_gray_txt.setStrokeWidth(radius / 52);
        canvas.drawText(chargedTime, viewWidth / 2, viewHeight / 2 + (float) (radius * Math.sin(Math.toRadians(45))), paint_gray_txt);


        rectF.set((viewWidth - 2 * radius) / 2 + radius / 14, (viewHeight - 2 * radius) / 2 + radius / 14, radius + viewWidth / 2 - radius / 14, radius + viewHeight / 2 - radius / 14);

        //绘制外层浅灰色背景
        paint_gray_light.setStrokeWidth(radius / 7);
        canvas.drawArc(rectF, -226, 272, false, paint_gray_light);
        //内层深灰色背景
        paint_gray_dark.setStrokeWidth(radius / 14);
        canvas.drawArc(rectF, -225, 270, false, paint_gray_dark);
        //动态进度条
        paint_blue.setStrokeWidth(radius / 14);
        canvas.drawArc(rectF, -225, progressValue, false, paint_blue);

        switch (electricityCode) {
            case AC://交流
                switch (bitmapNo) {
                    case 1:
                        if (powerOneBitmap == null) return;
                        canvas.drawBitmap(powerOneBitmap, viewWidth / 2 - powerOneBitmap.getWidth() / 2 + 10, viewHeight / 2 - powerOneBitmap.getHeight() / 2, paint_bitmap);
                        break;
                    case 2:
                        if (powerTwoBitmap == null) return;
                        canvas.drawBitmap(powerTwoBitmap, viewWidth / 2 - powerTwoBitmap.getWidth() / 2 + 10, viewHeight / 2 - powerTwoBitmap.getHeight() / 2, paint_bitmap);
                        break;
                    case 3:
                        if (powerThreeBitmap == null) return;
                        canvas.drawBitmap(powerThreeBitmap, viewWidth / 2 - powerThreeBitmap.getWidth() / 2 + 10, viewHeight / 2 - powerThreeBitmap.getHeight() / 2, paint_bitmap);
                        break;
                    case 4:
                        if (powerFourBitmap == null) return;
                        canvas.drawBitmap(powerFourBitmap, viewWidth / 2 - powerFourBitmap.getWidth() / 2 + 10, viewHeight / 2 - powerFourBitmap.getHeight() / 2, paint_bitmap);
                        break;
                }
                break;
            case DC://直流
                //字体大小可自行设置,这里设置的一个字符占半径的1/3
                paint_blue_percent.setTextSize(radius / 3);
                paint_blue_percent.setStrokeWidth(radius / 26);
                fontMetrics = paint_blue_percent.getFontMetrics();
                fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
                canvas.drawText((int) (progressValue / 2.7f) + "%", viewWidth / 2, viewHeight / 2 + fontTotalHeight / 4, paint_blue_percent);
                break;
        }
    }


}
