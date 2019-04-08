package com.zyc.doctor.widgets.gridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.NormImage;
import com.zyc.doctor.utils.glide.GlideHelper;

import java.util.ArrayList;

/**
 * @author dundun
 * @date 16/10/21
 */
public class AutoGridView extends RelativeLayout {
    private GridView gridView;
    private int gvWidth;
    private int gvHeight;
    private int imgWidth;
    private int imgSpace;
    private int numColumns = 5;
    private ArrayList<NormImage> bitmapList;
    private Bitmap bmpAdd;
    private Context context;
    private int maxNum = 4;
    /**
     * 是否可编辑
     */
    private boolean isAdd = false;

    public AutoGridView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AutoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        imgWidth = getResources().getDimensionPixelSize(R.dimen.img_width);
        imgSpace = getResources().getDimensionPixelSize(R.dimen.img_space);
        bmpAdd = BitmapFactory.decodeResource(getResources(), R.mipmap.publish_add_icon);
        gvWidth = numColumns * imgWidth + (numColumns + 1) * imgSpace;
        bitmapList = new ArrayList<>();
        gridView = new CustomGridView(getContext());
        gridView.setNumColumns(numColumns);
        gridView.setPadding(imgSpace, imgSpace, imgSpace, imgSpace);
        gridView.setVerticalSpacing(imgSpace);
        gridView.setHorizontalSpacing(imgSpace);
        updateImg(bitmapList, true);
        addView(gridView);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        gridView.setOnItemClickListener(listener);
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        gridView.setOnItemLongClickListener(listener);
    }

    public void initGridView() {
        if (gridView != null) {
            int rows;
            int size = bitmapList.size();
            if (size <= numColumns) {
                rows = 1;
            }
            else if (size <= (numColumns + numColumns)) {
                rows = 2;
            }
            else {
                rows = 3;
            }
            gvHeight = rows * imgWidth + (rows + 1) * imgSpace;
            gridView.setLayoutParams(new LayoutParams(gvWidth, gvHeight));
            gridView.invalidate();
        }
    }

    public void updateImg(ArrayList<NormImage> bitmaps, boolean isAdd) {
        this.isAdd = isAdd;
        this.bitmapList.clear();
        this.bitmapList.addAll(bitmaps);
        if (bitmaps.size() < maxNum && isAdd) {
            this.bitmapList.add(new NormImage().setSmallBitmap(bmpAdd));
        }
        initGridView();
        ImgAdapter adapter = new ImgAdapter(this.bitmapList);
        gridView.setAdapter(adapter);
        gridView.invalidate();
    }

    private class ImgAdapter extends BaseAdapter {
        public ArrayList<NormImage> list;

        public ImgAdapter(ArrayList<NormImage> list) {
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
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(new GridView.LayoutParams(imgWidth, imgWidth));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {
                imageView = (ImageView)convertView;
            }
            if (list.get(position).getSmallBitmap() == null) {
                Glide.with(context)
                     .load(list.get(position).getSmallImageUrl())
                     .apply(GlideHelper.getOptionsPic())
                     .into(imageView);
            }
            else {
                imageView.setImageBitmap(list.get(position).getSmallBitmap());
            }
            return imageView;
        }
    }
}
