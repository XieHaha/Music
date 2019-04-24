package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.DepartmentTypeBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.adapter.SelectDocTypeAdapter;
import com.zyc.doctor.ui.base.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/4/8 17:51
 * @des
 */
public class SelectDocTypeActivity extends BaseActivity implements SelectDocTypeAdapter.OnSelectListener {
    @BindView(R.id.act_select_doc_type_recycler)
    RecyclerView actSelectDocTypeRecycler;
    private SelectDocTypeAdapter selectDocTypeAdapter;
    private ArrayList<DepartmentTypeBean> list = new ArrayList<>();

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
        actSelectDocTypeRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        selectDocTypeAdapter.setList(list);
        selectDocTypeAdapter.setOnSelectListener(this);
        actSelectDocTypeRecycler.setAdapter(selectDocTypeAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        RequestUtils.getDepartmentType(this, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_DEPARTMENT_TYPE:
                list = (ArrayList<DepartmentTypeBean>)response.getData();
                if (list == null) {
                    list = new ArrayList<>();
                }
                selectDocTypeAdapter.setList(list);
                selectDocTypeAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSelect(String name) {
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_PUBLIC_STRING, name);
        setResult(RESULT_OK, intent);
        finish();
    }
}
