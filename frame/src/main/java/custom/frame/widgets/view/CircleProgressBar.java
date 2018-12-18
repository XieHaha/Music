package custom.frame.widgets.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import custom.frame.R;

public class CircleProgressBar extends View
{
    private int maxProgress = 100, progress = 30, progressStrokeWidth = 4;
    private int background = Color.TRANSPARENT, txtColor = Color.rgb(0x57, 0x87, 0xb6);
    private int progressColorBackground = Color.WHITE, progressColor = Color.rgb(0x57, 0x87, 0xb6);
    RectF oval;
    Paint paint;

    public CircleProgressBar(Context context)
    {
        super(context);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        txtColor = a.getColor(R.styleable.CircleProgressBar_txtColor, Color.rgb(0x57, 0x87, 0xb6));
        background = a.getColor(R.styleable.CircleProgressBar_cpb_background, Color.TRANSPARENT);
        progressColorBackground = a.getColor(R.styleable.CircleProgressBar_progressColorBackground,
                                             Color.WHITE);
        progressColor = a.getColor(R.styleable.CircleProgressBar_progressColor,
                                   Color.rgb(0x57, 0x87, 0xb6));
        progressStrokeWidth = a.getInteger(R.styleable.CircleProgressBar_progressStrokeWidth, 4);
        maxProgress = a.getInteger(R.styleable.CircleProgressBar_max, 100);
        progress = a.getInteger(R.styleable.CircleProgressBar_progress, 0);
        a.recycle();
        init();
    }

    private void init()
    {
        oval = new RectF();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();
        if (width != height)
        {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }
        paint.setAntiAlias(true);
        paint.setColor(progressColorBackground);
        canvas.drawColor(background);
        paint.setStrokeWidth(progressStrokeWidth);
        paint.setStyle(Style.STROKE);
        oval.left = progressStrokeWidth / 2;
        oval.top = progressStrokeWidth / 2;
        oval.right = width - progressStrokeWidth / 2;
        oval.bottom = height - progressStrokeWidth / 2;
        canvas.drawArc(oval, -90, 360, false, paint);
        paint.setColor(progressColor);
        canvas.drawArc(oval, -90, ((float)progress / maxProgress) * 360, false, paint);
        paint.setColor(txtColor);
        paint.setStrokeWidth(1);
        String text = progress + "%";
        int textHeight = height / 4;
        paint.setTextSize(textHeight);
        int textWidth = (int)paint.measureText(text, 0, text.length());
        paint.setStyle(Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2, paint);
    }

    public int getMaxProgress()
    {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress)
    {
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress)
    {
        if (progress > 100) { this.progress = 100; }
        else if (progress < 0) { this.progress = 0; }
        else { this.progress = progress; }
        invalidate();
    }

    /**
     * 非ＵＩ线程调用
     */
    public void setProgressNotInUiThread(int progress)
    {
        this.progress = progress;
        postInvalidate();
    }
}
