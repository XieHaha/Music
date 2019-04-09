package com.zyc.doctor.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.SelectDocTypeAdapter;
import com.zyc.doctor.ui.base.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/4/8 17:51
 * @des
 */
public class SelectDocTypeActivity extends BaseActivity {
    @BindView(R.id.act_select_doc_type_recycler)
    RecyclerView actSelectDocTypeRecycler;
    private SelectDocTypeAdapter selectDocTypeAdapter;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_doc_type;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        selectDocTypeAdapter = new SelectDocTypeAdapter(actSelectDocTypeRecycler);
        actSelectDocTypeRecycler.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < 10; i++) {
            list.add(i + "   GO GO GO");
        }
        selectDocTypeAdapter.setList(list);
        actSelectDocTypeRecycler.setAdapter(selectDocTypeAdapter);
    }
}
