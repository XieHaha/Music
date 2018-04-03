package custom.widgets.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by thl on 2016-04-16.
 */
public class BookTimeProgressView extends View {

    private Paint bgPaint;
    private Paint progressBgPaint;
    private Paint progressPaint;

//    private Shader bgLinearGradient = null;
//    private Shader progressBgLinearGradient = null;
//    private Shader progressLinearGradient = null;

    private RectF rectBg;
    private RectF rectProgressBg;
    private RectF rectProgress;

    private int Width;
    private int Height;

    private float bgPadding;
    private float bgCorner;
    private float progressCorner;

    private float progress;
    private float progressValue = 1.0f;

    public BookTimeProgressView(Context context) {
        super(context);
        init();
    }

    public BookTimeProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BookTimeProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

        //  if (isInEditMode()) return;

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.parseColor("#e0e3e8"));

        progressBgPaint = new Paint();
        progressBgPaint.setAntiAlias(true);
        progressBgPaint.setColor(Color.parseColor("#7e7e7e"));

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(Color.parseColor("#6bcf2f"));

        rectBg = new RectF();
        rectProgressBg = new RectF();
        rectProgress = new RectF();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Width = w;
        Height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // if (isInEditMode()) return;

        bgPadding = Height / 4f;
        bgCorner = Height / 2f;
        progressCorner = bgCorner / 2f;
        progress = (1 - progressValue) * (Width - 2 * bgPadding);


        float progressTotal = Width - 1 * bgPadding - bgCorner;
        float progressPadding = 0;
        if (progress > progressTotal) {
            float val = progress - progressTotal;
            progressPadding = val;
        }

//        bgLinearGradient = new LinearGradient(0, Height / 2f, Width, Height / 2f,
//                new int[]{Color.rgb(208, 211, 216), Color.rgb(205, 207, 212)},
//                new float[]{0, 1f}, Shader.TileMode.REPEAT);

//        progressBgLinearGradient = new LinearGradient(bgPadding, bgPadding, bgPadding, Height - bgPadding,
//                new int[]{Color.rgb(162, 163, 167), Color.rgb(175, 177, 181), Color.rgb(162, 163, 167)},
//                new float[]{0, 0.5f, 1f}, Shader.TileMode.REPEAT);

//        progressLinearGradient = new LinearGradient(bgPadding + progress, bgPadding, bgPadding + progress, Height - bgPadding,
//                new int[]{Color.rgb(97, 187, 41), Color.rgb(107, 205, 46), Color.rgb(97, 187, 41)},
//                new float[]{0, 0.5f, 1f}, Shader.TileMode.REPEAT);

//        bgPaint.setShader(bgLinearGradient);
//        progressBgPaint.setShader(progressBgLinearGradient);
//        progressPaint.setShader(progressLinearGradient);

        //底背景
        rectBg.set(0, 0, Width, Height);
        canvas.drawRoundRect(rectBg, bgCorner, bgCorner, bgPaint);

        //进度条背景
        rectProgressBg.set(bgPadding, bgPadding, Width - bgPadding, Height - bgPadding);
        canvas.drawRoundRect(rectProgressBg, progressCorner, progressCorner, progressBgPaint);

        //进度条
        rectProgress.set(bgPadding + progress, bgPadding + progressPadding, Width - bgPadding, Height - (bgPadding + progressPadding));
        canvas.drawRoundRect(rectProgress, progressCorner, progressCorner, progressPaint);

    }

    public void setProgressValue(float progressValue) {
        if (progressValue < 0) progressValue = 0;
        if (progressValue > 1) progressValue = 1;
        this.progressValue = progressValue;
        invalidate();
    }
}
