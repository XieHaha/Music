package custom.widgets.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import custom.widgets.R;


/**
 * Created by dundun_299 on 2016/10/15.
 */
public class CustomRatingBar extends LinearLayout {
    private Context mContext;
    /**
     * 是否可点击
     */
    private boolean mClickable;
    /**
     * 星星数量
     */
    private int starCount;
    /**
     * 当前星级
     */
    private float starCurrentValue;
    /**
     * 星星间距
     */
    private float starImagePadding;
    /**
     * 空白星星
     */
    private Drawable starEmpty;
    /**
     * 充满星星
     */
    private Drawable starFill;

    private OnRatingChangeListener onRatingChangeListener;


    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    public void setmClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public void setStarEmpty(Drawable starEmpty) {
        this.starEmpty = starEmpty;
    }

    public void setStarFill(Drawable starFill) {
        this.starFill = starFill;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public void setImagePadding(float starImagePadding) {
        this.starImagePadding = starImagePadding;
    }


    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRatingBar);

        starEmpty = mTypedArray.getDrawable(R.styleable.CustomRatingBar_starEmpty);
        starFill = mTypedArray.getDrawable(R.styleable.CustomRatingBar_starFill);
        starImagePadding = mTypedArray.getDimension(R.styleable.CustomRatingBar_starImagePadding, 5);
        starCount = mTypedArray.getInteger(R.styleable.CustomRatingBar_starCount, 5);
        starCurrentValue = mTypedArray.getInteger(R.styleable.CustomRatingBar_starNum, 5);
        mClickable = mTypedArray.getBoolean(R.styleable.CustomRatingBar_clickable, false);

        for (int i = 0; i < starCount; i++) {
            if (i < starCurrentValue) {
                addView(getStarImageView(true));
            } else {
                addView(getStarImageView(false));
            }
        }
    }

    private ImageView getStarImageView(boolean isValue) {
        ImageView imageView = new ImageView(mContext);
        imageView.setPadding(0, 0, Math.round(starImagePadding), 0);
        if (isValue) {
            imageView.setImageDrawable(starFill);
        } else {
            imageView.setImageDrawable(starEmpty);
        }
        return imageView;
    }

    /**
     * 星级设置
     *
     * @param starCount
     */
    public void setCurrentStar(float starCount) {

    }

    /**
     * change start listener
     */
    public interface OnRatingChangeListener {

        void onRatingChange(float RatingCount);

    }

}
