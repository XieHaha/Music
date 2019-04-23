package com.zyc.doctor.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.DepartmentTypeChildBean;

import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/4/9 17:12
 * @des
 */
public class SelectDocTypeChildAdapter extends BaseAdapter {
    private ArrayList<DepartmentTypeChildBean> list;
    private ViewHolder holder;

    public void setList(ArrayList<DepartmentTypeChildBean> list) {
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
            holder.textView = convertView.findViewById(R.id.item_file_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(list.get(position).getLabel());
        return convertView;
    }

    class ViewHolder {
        private TextView textView;
    }
}
