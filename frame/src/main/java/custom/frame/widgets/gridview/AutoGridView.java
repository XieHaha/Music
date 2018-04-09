package custom.frame.widgets.gridview;

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

import java.util.ArrayList;

import custom.frame.R;
import custom.frame.bean.NormImage;

/**
 * Created by thl on 2016/2/28.
 */
public class AutoGridView extends RelativeLayout
{
    private GridView gridView;
    private int gvWidth;
    private int gvHeight;
    private int imgWidth;
    private int imgSpace;
    private int numColumns = 4;
    private ArrayList<NormImage> bitmapList;
    private Bitmap bmpAdd;

    public AutoGridView(Context context)
    {
        super(context);
        init();
    }

    public AutoGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public void init()
    {
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
        updateImg(bitmapList);
        addView(gridView);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener)
    {
        gridView.setOnItemClickListener(listener);
    }

    public void initGridView()
    {
        if (gridView != null)
        {
            int rows;
            int size = bitmapList.size();
            if (size <= 4)
            {
                rows = 1;
            }
            else if (size <= 8)
            {
                rows = 2;
            }
            else
            {
                rows = 3;
            }
            gvHeight = rows * imgWidth + (rows + 1) * imgSpace;
            gridView.setLayoutParams(new LayoutParams(gvWidth, gvHeight));
            gridView.invalidate();
        }
    }

    public void updateImg(ArrayList<NormImage> bitmaps)
    {
        this.bitmapList.clear();
        this.bitmapList.addAll(bitmaps);
        if (bitmaps.size() < 9)
        {
            this.bitmapList.add(new NormImage().setSmallBitmap(bmpAdd));
        }
        initGridView();
        ImgAdapter adapter = new ImgAdapter(this.bitmapList);
        gridView.setAdapter(adapter);
        gridView.invalidate();
    }

    private class ImgAdapter extends BaseAdapter
    {
        public ArrayList<NormImage> list;

        public ImgAdapter(ArrayList<NormImage> list)
        {
            this.list = list;
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public Object getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            if (convertView == null)
            {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(new GridView.LayoutParams(imgWidth, imgWidth));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else
            {
                imageView = (ImageView)convertView;
            }
            imageView.setImageBitmap(list.get(position).getSmallBitmap());
            return imageView;
        }
    }
}
