package custom.widgets.gridview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import custom.base.entity.base.Images;
import custom.base.entity.base.UserBase;
import custom.widgets.R;
import custom.widgets.image.PortraitImageView;

/**
 * Created by thl on 2016-06-14.
 */
public class AutoGridViews extends RelativeLayout {

    private CustomGridView customGridView;

    private int numColumns = 10;
    private int imgSpace;

    private int imageWidth;

    private int Width;

    private ImgAdapter imgAdapter;
    private ArrayList<UserBase> images = new ArrayList<>();

    public AutoGridViews(Context context) {
        super(context);
        init();
    }

    public AutoGridViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoGridViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        imgSpace = getResources().getDimensionPixelSize(R.dimen.image_space);
        customGridView = new CustomGridView(getContext());
        customGridView.setNumColumns(numColumns);
        customGridView.setVerticalSpacing(imgSpace);
        customGridView.setHorizontalSpacing(imgSpace);
        customGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        imgAdapter = new ImgAdapter(images);
        customGridView.setAdapter(imgAdapter);
        addView(customGridView);
    }

    private void initGridView(int total) {

        int row = total / numColumns;
        if (total % numColumns > 0)
            row = row + 1;

        int height = row * imageWidth + (row - 1) * imgSpace;
        customGridView.setLayoutParams(new LayoutParams(Width, height));
        customGridView.invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Width = w;
        imageWidth = (w - (numColumns - 1) * imgSpace) / numColumns;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        customGridView.setOnItemClickListener(listener);
    }

    public void updateImg(ArrayList<UserBase> images) {

        initGridView(images.size());

        this.images.clear();
        this.images.addAll(images);
        imgAdapter.notifyDataSetChanged();

    }

    private class ImgAdapter extends BaseAdapter {

        public ArrayList<UserBase> list;

        public ImgAdapter(ArrayList<UserBase> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.view_item_image, null);
                holder = new ViewHolder();
                holder.pivPortrait = (PortraitImageView) convertView.findViewById(R.id.view_item_image);
                holder.pivPortrait.setLayoutParams(new LayoutParams(imageWidth - 1, imageWidth - 1));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Images headerImgs = list.get(position).getHeadImage();
            if (headerImgs != null && !TextUtils.isEmpty(headerImgs.getSmallImage())) {
                holder.pivPortrait.displayImage(headerImgs.getSmallImage());
            } else {
                holder.pivPortrait.setImageResource(R.mipmap.ic_default_blue);
            }
            return convertView;
        }

        private class ViewHolder {
            PortraitImageView pivPortrait;
        }


    }
}
