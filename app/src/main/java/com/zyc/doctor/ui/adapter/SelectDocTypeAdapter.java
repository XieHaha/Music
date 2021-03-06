package com.zyc.doctor.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.DepartmentTypeBean;
import com.zyc.doctor.widgets.FullListView;
import com.zyc.doctor.widgets.expandable.ExpandableLayout;

import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/4/9 16:28
 * @des
 */
public class SelectDocTypeAdapter extends RecyclerView.Adapter<SelectDocTypeAdapter.ViewHolder> {
    private static final int UNSELECTED = -1;
    private RecyclerView recyclerView;
    private int selectedItem = UNSELECTED;
    private ArrayList<DepartmentTypeBean> list;

    public SelectDocTypeAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setList(ArrayList<DepartmentTypeBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_doc_type, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ExpandableLayout expandableLayout;
        private LinearLayout expandLayout;
        private TextView expandTxt;
        private FullListView listView;

        public ViewHolder(View itemView) {
            super(itemView);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandLayout = itemView.findViewById(R.id.expand_layout);
            expandTxt = itemView.findViewById(R.id.expand_txt);
            expandLayout.setOnClickListener(this);
            listView = itemView.findViewById(R.id.list);
        }

        public void bind(int position) {
            //            int position = getAdapterPosition();
            boolean isSelected = position == selectedItem;
            expandTxt.setText(list.get(position).getLabel());
            expandableLayout.setExpanded(isSelected, false);
            SelectDocTypeChildAdapter adapter = new SelectDocTypeChildAdapter();
            adapter.setList(list.get(position).getChildren());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    if (onSelectListener != null) {
                        onSelectListener.onSelect(list.get(position).getChildren().get(pos).getLabel());
                    }
                }
            });
            listView.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {
            ViewHolder holder = (ViewHolder)recyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (holder != null) {
                holder.expandLayout.setSelected(false);
                holder.expandableLayout.collapse();
            }
            int position = getAdapterPosition();
            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            }
            else {
                expandLayout.setSelected(true);
                expandableLayout.expand();
                selectedItem = position;
            }
        }
    }

    public interface OnSelectListener {
        void onSelect(String name);
    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }
}
