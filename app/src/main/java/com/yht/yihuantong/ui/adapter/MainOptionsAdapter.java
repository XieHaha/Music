package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.yihuantong.R;

/**
 * Created by dundun on 2018-4-8.
 * 患者列表适配器
 *
 * @author DUNDUN
 */
public class MainOptionsAdapter extends BaseAdapter
{
    private Context context;
    private int[] optionsTxt;
    private int[] optionsIcon;

    public MainOptionsAdapter(Context context)
    {
        this.context = context;
    }

    public void setOptionsTxt(int[] optionsTxt)
    {
        this.optionsTxt = optionsTxt;
    }

    public void setOptionsIcon(int[] optionsIcon)
    {
        this.optionsIcon = optionsIcon;
    }

    @Override
    public int getCount()
    {
        return optionsTxt.length;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MainOptionsHolder holder;
        if (convertView == null)
        {
            holder = new MainOptionsHolder();
            convertView = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.item_main_options, parent, false);
            holder.ivIcon = convertView.findViewById(R.id.item_main_options_icon);
            holder.tvTitle = convertView.findViewById(R.id.item_main_options_title);
            convertView.setTag(holder);
        }
        else
        {
            holder = (MainOptionsHolder)convertView.getTag();
        }
        holder.ivIcon.setImageResource(optionsIcon[position]);
        holder.tvTitle.setText(optionsTxt[position]);
        return convertView;
    }

    public class MainOptionsHolder
    {
        private ImageView ivIcon;
        private TextView tvTitle;
    }
}
