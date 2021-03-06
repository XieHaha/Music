package com.zyc.doctor.widgets.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyc.doctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author csy
 * @date 2017/10/10
 * 最多显示5个子菜单,如果需要显示更多,可自己做拓展
 * 微信公众号:陈守印同学
 * https://github.com/chenshouyin/SatelliteMenu
 */
public class SatelliteMenu extends RelativeLayout implements View.OnClickListener {
    private List<TextView> textViews;

    public void setNameMenuItem(List<String> nameMenuItem) {
        this.nameMenuItem = nameMenuItem;
    }

    private List<String> nameMenuItem;
    private ImageView ivAdd;
    private boolean isMenuOpen = false;
    private Context mContext;
    /**
     * 弧形半径 默认值
     */
    private float mRadius;
    /**
     * 左上,右上，左下，右下
     */
    private int mPostion;
    private float mMenuImageWidth;
    private float mMenuItemImageWidth;
    private float mMenuItemTextSize;
    private int mMenuItemTextColor;
    private RelativeLayout viewMenuItem;
    private boolean isPlayAnim = false;
    /**
     * 正还是反
     */
    private int flag = 1;
    private RelativeLayout rootView;
    private OnMenuItemClickListener mOnMenuItemClickListener;

    public Builder getmBuilder() {
        return new Builder(mContext);
    }

    public SatelliteMenu(Context context) {
        super(context);
        initView(context, null);
    }

    public SatelliteMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_satellite_menu, this);
        viewMenuItem = view.findViewById(R.id.viewMenuItem);
        rootView = view.findViewById(R.id.rootView);
        ivAdd = view.findViewById(R.id.ivAdd);
        //设置监听事件
        ivAdd.setOnClickListener(this);
        //获取xml中自定义属性的值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SatelliteMenu);
        mRadius = a.getDimension(R.styleable.SatelliteMenu_radius, 200);
        //默认左上
        mPostion = a.getInteger(R.styleable.SatelliteMenu_menu_postion, 0);
        //get dp will be convert to px
        mMenuImageWidth = a.getDimension(R.styleable.SatelliteMenu_menu_image_width, 45);
        mMenuItemImageWidth = a.getDimension(R.styleable.SatelliteMenu_menu_item_image_width, 45);
        mMenuItemTextSize = a.getDimension(R.styleable.SatelliteMenu_menu_item_text_size, 16);
        mMenuItemTextColor = a.getColor(R.styleable.SatelliteMenu_menu_item_text_color, 0x00FFFF);
        a.recycle();
    }

    /**
     * 菜单点击回调
     *
     * @param mOnMenuItemClickListener
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == ivAdd.getId()) {
            if (isPlayAnim) { return; }
            isMenuOpen = !isMenuOpen;
            perfomMenuAnim(isMenuOpen);
        }
        else {
            if (mOnMenuItemClickListener == null) { return; }
            for (int i = 0; i < textViews.size(); i++) {
                if (view == textViews.get(i)) {
                    //菜单点击回调
                    mOnMenuItemClickListener.onMenuClick(view, i);
                    menuClickAnim(view);
                    //关闭菜单
                    isMenuOpen = !isMenuOpen;
                    perfomMenuAnim(isMenuOpen);
                }
            }
        }
    }

    /**
     * 打开和关闭只是x和y不一样
     *
     * @param isMenuOpen
     */
    private void perfomMenuAnim(final boolean isMenuOpen) {
        isPlayAnim = true;
        //需要打开菜单
        if (isMenuOpen) {
            flag = 1;
        }
        //收回菜单,相反的操作
        else {
            flag = -1;
        }
        ainimMenu();
        animMenuItem();
    }

    /**
     * 菜单点击回调,提供给外部
     */
    public interface OnMenuItemClickListener {
        /**
         * 点击
         *
         * @param view
         * @param postion
         */
        void onMenuClick(View view, int postion);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < rootView.getChildCount(); i++) {
            //测量子view大小，不然下面获取到的宽高是0
            rootView.getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        //默认只有这一句 这个只是父类的measure所以getMeasuredWidth()是有值的
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //因为this直接addView的是一个layout,所以getChildCount()==1
        //循环计算子view的位置
        for (int i = 0; i < rootView.getChildCount(); i++) {
            View child = rootView.getChildAt(i);
            if (mPostion == MenuPosition.LEFT_TOP) {
                if (child instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup)child;
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        child = viewGroup.getChildAt(j);
                        l = 0;
                        t = 0;
                        r = child.getMeasuredWidth();
                        b = child.getMeasuredHeight();
                        child.layout(l, t, r, b);
                    }
                }
                else {
                    l = 0;
                    t = 0;
                    r = child.getMeasuredWidth();
                    b = child.getMeasuredHeight();
                    child.layout(l, t, r, b);
                }
            }
            else if (mPostion == MenuPosition.RIGHT_TOP) {
                if (child instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup)child;
                    //子菜单位置
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        child = viewGroup.getChildAt(j);
                        l = getMeasuredWidth() - child.getMeasuredWidth();
                        t = 0;
                        r = getMeasuredWidth();
                        b = child.getMeasuredHeight();
                        child.layout(l, t, r, b);
                    }
                }
                else {
                    l = getMeasuredWidth() - child.getMeasuredWidth();
                    t = 0;
                    r = getMeasuredWidth();
                    b = child.getMeasuredHeight();
                    child.layout(l, t, r, b);
                }
            }
            else if (mPostion == MenuPosition.LEFT_BOTTOM) {
                if (child instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup)child;
                    //子菜单位置
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        child = viewGroup.getChildAt(j);
                        l = 0;
                        t = getHeight() - child.getMeasuredHeight();
                        r = child.getMeasuredWidth();
                        b = getHeight();
                        child.layout(l, t, r, b);
                    }
                }
                else {
                    l = 0;
                    t = getHeight() - child.getMeasuredHeight();
                    r = child.getMeasuredWidth();
                    b = getHeight();
                    child.layout(l, t, r, b);
                }
            }
            else if (mPostion == MenuPosition.RIGHT_BOTTOM) {
                if (child instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup)child;
                    //子菜单位置
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        child = viewGroup.getChildAt(j);
                        l = getWidth() - child.getMeasuredWidth();
                        t = getHeight() - child.getMeasuredHeight();
                        r = getWidth();
                        b = getHeight();
                        child.layout(l, t, r, b);
                    }
                }
                else {
                    l = getWidth() - child.getMeasuredWidth();
                    t = getHeight() - child.getMeasuredHeight();
                    r = getWidth();
                    b = getHeight();
                    child.layout(l, t, r, b);
                }
            }
        }
    }

    /**
     * 子菜单动画
     */
    private void animMenuItem() {
        //子菜单 平移过程中发生变化的属性translationX,translationY
        //菜单移动的位移
        final float distance = mRadius;
        for (int i = 0; i < textViews.size(); i++) {
            //Math.PI 3.14也就是180°    Math.PI/2=90°  注意不能直接用90参与运算,因为单位不一样
            final TextView imageView = textViews.get(i);
            float mX = 0;
            float mY = 0;
            if (mPostion == MenuPosition.LEFT_TOP) {
                mX = ((float)(distance * Math.sin(Math.PI / 2 / (textViews.size() - 1) * i))) * flag;
                mY = ((float)(distance * Math.cos(Math.PI / 2 / (textViews.size() - 1) * i))) * flag;
            }
            else if (mPostion == MenuPosition.RIGHT_TOP) {
                //位移位置是相对于本身原来所在的点
                mX = (float)(-distance * Math.sin(Math.PI / 2 / (textViews.size() - 1) * i)) * flag;
                mY = ((float)(distance * Math.cos(Math.PI / 2 / (textViews.size() - 1) * i))) * flag;
            }
            else if (mPostion == MenuPosition.LEFT_BOTTOM) {
                mX = ((float)(distance * Math.sin(Math.PI / 2 / (textViews.size() - 1) * (textViews.size() - 1 - i)))) *
                     flag;
                mY = -((float)(distance *
                               Math.cos(Math.PI / 2 / (textViews.size() - 1) * (textViews.size() - 1 - i)))) * flag;
            }
            else if (mPostion == MenuPosition.RIGHT_BOTTOM) {
                //位移位置是相对于本身原来所在的点
                mX = (float)(-distance * Math.sin(Math.PI / 2 / (textViews.size() - 1) * (textViews.size() - 1 - i)));
                mY = ((float)-(distance * Math.cos(Math.PI / 2 / (textViews.size() - 1) * (textViews.size() - 1 - i))));
            }
            ObjectAnimator animatorX, animatorY;
            if (flag == -1) {
                //平移动画
                animatorX = ObjectAnimator.ofFloat(textViews.get(i), "translationX", mX, 0);
                animatorY = ObjectAnimator.ofFloat(textViews.get(i), "translationY", mY, 0);
            }
            else {
                //平移动画
                animatorX = ObjectAnimator.ofFloat(textViews.get(i), "translationX", 0, mX);
                animatorY = ObjectAnimator.ofFloat(textViews.get(i), "translationY", 0, mY);
            }
            if (!isMenuOpen) {
                ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(textViews.get(i), "alpha", 1.0f, 0f);
                animatorAlpha.setDuration(300);
                animatorAlpha.setStartDelay(100);
                animatorAlpha.start();
            }
            animatorX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    viewMenuItem.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!isMenuOpen) {
                        viewMenuItem.setVisibility(View.GONE);
                        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
                        animatorAlpha.setDuration(0);
                        animatorAlpha.start();
                    }
                    isPlayAnim = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            AnimatorSet mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(animatorX, animatorY);
            mAnimatorSet.setDuration(300);
            if (!isMenuOpen) {
                mAnimatorSet.setStartDelay(100);
            }
            mAnimatorSet.setInterpolator(new AccelerateInterpolator());
            mAnimatorSet.start();
        }
    }

    /**
     * 主菜单动画
     */
    private void ainimMenu() {
        //菜单旋转90°
        ObjectAnimator animatorMenu;
        if (isMenuOpen) {
            //旋转
            animatorMenu = ObjectAnimator.ofFloat(ivAdd, "rotation", 0, 90);
        }
        else {
            //旋转
            animatorMenu = ObjectAnimator.ofFloat(ivAdd, "rotation", 90, 0);
            animatorMenu.setStartDelay(200);
        }
        animatorMenu.setDuration(400);
        //减速
        animatorMenu.setInterpolator(new AccelerateInterpolator());
        animatorMenu.start();
    }

    /**
     * 子菜单点击渐变缩放动画
     *
     * @param view
     */
    private void menuClickAnim(final View view) {
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        //缩放Scale
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f);
        //缩放Scale
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorAlpha, scaleX, scaleY);
        set.setDuration(250);
        //加速
        set.setInterpolator(new AccelerateInterpolator());
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //复原状态
                ObjectAnimator animatorAlpha2 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                //缩放Scale
                ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(view, "scaleX", 1.5f, 1f);
                //缩放Scale
                ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(view, "scaleY", 1.5f, 1f);
                AnimatorSet set2 = new AnimatorSet();
                set2.playTogether(animatorAlpha2, scaleX2, scaleY2);
                set2.setDuration(0);
                set2.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 设置图片资源,根据传入的图片个数添加对应个数的子菜单
     *
     * @param imageResource
     */
    public void setMenuItemImage(List<Integer> imageResource) {
        if (imageResource == null) { return; }
        textViews = new ArrayList<>();
        for (int i = 0; i < imageResource.size(); i++) {
            //Dynamic add view
            TextView tv = new TextView(mContext);
            LayoutParams params = new LayoutParams((int)mMenuItemImageWidth, (int)mMenuItemImageWidth);
            tv.setLayoutParams(params);
            if (nameMenuItem == null) {
                //不设置文字
                tv.setBackgroundResource(imageResource.get(i));
            }
            else {
                Paint mPaint = new Paint();
                mPaint.setTextSize(mMenuItemTextSize);
                String name = nameMenuItem.get(i);
                Rect rect = new Rect();
                //get text width and height
                mPaint.getTextBounds(name, 0, 2, rect);
                Drawable drawable = getResources().getDrawable(imageResource.get(i));
                drawable.setBounds(0, 0, (int)mMenuItemImageWidth - Math.max(rect.width() * 2, rect.height()),
                                   (int)mMenuItemImageWidth - Math.max(rect.width(), rect.height()) * 2);
                tv.setCompoundDrawables(null, drawable, null, null);
                tv.setText(nameMenuItem.get(i));
                tv.setTextSize(mMenuItemTextSize);
                tv.setTextColor(mMenuItemTextColor);
                tv.setText(name);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            textViews.add(tv);
            viewMenuItem.addView(tv);
            tv.setOnClickListener(this);
        }
    }

    public void setMenuImage(int imageResource) {
        LayoutParams params = new LayoutParams((int)mMenuImageWidth, (int)mMenuImageWidth);
        ivAdd.setLayoutParams(params);
        ivAdd.setImageResource(imageResource);
    }

    /**
     * 构造器
     */
    public class Builder {
        private int menuImageResource, menuImageResourceGrey;
        private List<String> menuItemName;
        private List<Integer> imageMenuItemImageResource;
        private SatelliteMenu mSatelliteMenu;
        private Context mContext;
        private OnMenuItemClickListener mOnMenuItemClickListener;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setMenuImage(int menuImageResource, int menuImageResourceGrey) {
            this.menuImageResource = menuImageResource;
            this.menuImageResourceGrey = menuImageResourceGrey;
            return this;
        }

        public Builder setMenuItemImageResource(List<Integer> imageMenuItemImageResource) {
            this.imageMenuItemImageResource = imageMenuItemImageResource;
            return this;
        }

        public Builder setMenuItemNameTexts(List<String> menuItemName) {
            this.menuItemName = menuItemName;
            return this;
        }

        public Builder setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
            this.mOnMenuItemClickListener = mOnMenuItemClickListener;
            return this;
        }

        public void creat() {
            SatelliteMenu.this.setNameMenuItem(menuItemName);
            SatelliteMenu.this.setMenuImage(menuImageResource);
            SatelliteMenu.this.setMenuItemImage(imageMenuItemImageResource);
            SatelliteMenu.this.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
    }
}