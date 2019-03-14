package com.yht.yihuantong.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * ViewHolder缓存模式实现
 */
public class ViewHolderHelper
{
    private final SparseArray<View> views;
    private final Context context;
    private int position;
    View convertView;

    public ViewHolderHelper(Context context, ViewGroup parent, View convertView, int position)
    {
        this.context = context;
        this.position = position;
        this.views = new SparseArray<View>();
        this.convertView = convertView;
        convertView.setTag(this);
    }

    public <T extends View> T getView(int viewId)
    {
        return retrieveView(viewId);
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to putObject in the text view.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setText(int viewId, String value)
    {
        TextView view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setText(value);
        return this;
    }

    public ViewHolderHelper setText(int viewId, String value, int drawId)
    {
        TextView view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setText(value);
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawId, 0);
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setImageResource(int viewId, int imageResId)
    {
        ImageView view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setBackgroundColor(int viewId, int color)
    {
        View view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setBackgroundRes(int viewId, int backgroundRes)
    {
        View view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseAdapterHelper for chaining.
     */
    public ViewHolderHelper setTextColor(int viewId, int textColor)
    {
        TextView view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setTextColor(textColor);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId       The view id.
     * @param textColorRes The text color resource id.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setTextColorRes(int viewId, int textColorRes)
    {
        TextView view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setTextColor(context.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setImageDrawable(int viewId, Drawable drawable)
    {
        ImageView view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public ViewHolderHelper setImageBitmap(int viewId, Bitmap bitmap)
    {
        ImageView view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public ViewHolderHelper setAlpha(int viewId, float value)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            retrieveView(viewId).setAlpha(value);
        }
        else
        {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            retrieveView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setVisible(int viewId, boolean visible)
    {
        View view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper linkify(int viewId)
    {
        TextView view = retrieveView(viewId);
        if (view == null) { return this; }
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public ViewHolderHelper setTypeface(int viewId, Typeface typeface)
    {
        TextView view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    public ViewHolderHelper setTypeface(Typeface typeface, int... viewIds)
    {
        for (int viewId : viewIds)
        {
            TextView view = retrieveView(viewId);
            if (view == null) { continue; }
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * Sets the fragment_login_progress of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The fragment_login_progress.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setProgress(int viewId, int progress)
    {
        ProgressBar view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the fragment_login_progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The fragment_login_progress.
     * @param max      The max value of a ProgressBar.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setProgress(int viewId, int progress, int max)
    {
        ProgressBar view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setMax(int viewId, int max)
    {
        ProgressBar view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setMax(max);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setRating(int viewId, float rating)
    {
        RatingBar view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setRating(int viewId, float rating, int max)
    {
        RatingBar view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setOnClickListener(int viewId, View.OnClickListener listener)
    {
        View view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setOnTouchListener(int viewId, View.OnTouchListener listener)
    {
        View view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setOnLongClickListener(int viewId, View.OnLongClickListener listener)
    {
        View view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setTag(int viewId, Object tag)
    {
        View view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setTag(tag);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setTag(int viewId, int key, Object tag)
    {
        View view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setTag(key, tag);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setChecked(int viewId, boolean checked)
    {
        Checkable view = (Checkable)retrieveView(viewId);
        if (view == null) { return this; }
        view.setChecked(checked);
        return this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The ViewHolderHelper for chaining.
     */
    public ViewHolderHelper setAdapter(int viewId, Adapter adapter)
    {
        AdapterView view = retrieveView(viewId);
        if (view == null) { return this; }
        view.setAdapter(adapter);
        return this;
    }

    /**
     * Retrieve the convertView
     */
    public View getView()
    {
        return convertView;
    }

    /**
     * Retrieve the overall position of the data in the list.
     *
     * @throws IllegalArgumentException If the position hasn't been set at the construction of the this helper.
     */
    public int getPosition()
    {
        if (position == -1)
        {
            throw new IllegalStateException(
                    "Use ViewHolderHelper constructor " + "with position if you need to retrieve the position.");
        }
        return position;
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T retrieveView(int viewId)
    {
        View view = views.get(viewId);
        if (view == null)
        {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T)view;
    }
}
