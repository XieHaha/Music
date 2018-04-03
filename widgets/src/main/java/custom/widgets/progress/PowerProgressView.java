package custom.widgets.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by thl on 2016-04-26.
 */
public class PowerProgressView extends View {

    private Paint paint;
    private Paint progressLinePaint;
    private Paint bgPaint;

    private int Width;
    private int Height;

    private RectF progressRectF = new RectF();
    private RectF bgRectF = new RectF();

    private float progressValue;

    public PowerProgressView(Context context) {
        super(context);
        init();
    }

    public PowerProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PowerProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(255, 255, 255));

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.argb(16, 0, 0, 0));

        progressLinePaint = new Paint();
        progressLinePaint.setAntiAlias(true);
        progressLinePaint.setColor(Color.argb(21, 0, 0, 0));


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

        //绘制进度条背景
        bgRectF.set(0, Height * (14 / 20f), Width, Height);
        canvas.drawRoundRect(bgRectF, Height * (6 / 20f), Height * (6 / 20f), bgPaint);

        //绘制进度条底线
        canvas.drawLine(Height * (3 / 20f), Height * (14 / 20f), Width - Height * (3 / 20f), Height * (14 / 20f), progressLinePaint);

        //绘制进度条
        progressRectF.set(0, Height * (12 / 20f), progressValue, Height * (16 / 20f));
        canvas.drawRoundRect(progressRectF, Height * (2 / 20f), Height * (2 / 20f), paint);

        //绘制游标
        canvas.drawLine(progressValue, 0, progressValue, Height * (14 / 20f), paint);
    }

    public void setProgress(float progress) {
        progressValue = progress * Width;
        if (onPowerProgressListener != null)
            this.onPowerProgressListener.OnPowerChange(progressValue);
        invalidate();
    }

    private OnPowerProgressListener onPowerProgressListener = null;


    public void setOnPowerProgressListener(OnPowerProgressListener onPowerProgressListener) {
        this.onPowerProgressListener = onPowerProgressListener;
    }

    public interface OnPowerProgressListener {
        void OnPowerChange(float progressValue);
    }
}
