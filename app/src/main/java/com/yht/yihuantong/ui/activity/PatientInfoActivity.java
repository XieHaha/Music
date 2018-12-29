package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.api.notify.NotifyChangeListenerServer;
import com.yht.yihuantong.chat.ChatActivity;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.FragmentVpAdapter;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.ui.fragment.HealthInfoFragment;
import com.yht.yihuantong.ui.fragment.OrderInfoFragment;
import com.yht.yihuantong.ui.fragment.TransferInfoFragment;
import com.yht.yihuantong.utils.AllUtils;
import com.yht.yihuantong.utils.RecentContactUtils;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CombineBean;
import custom.frame.bean.CombineChildBean;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.GlideHelper;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.menu.SatelliteMenu;
import custom.frame.widgets.view.SearchLabelLayout;
import custom.frame.widgets.view.ViewPrepared;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的页面
 */
public class PatientInfoActivity extends BaseActivity
        implements SatelliteMenu.OnMenuItemClickListener
{
    private Button btnHealthInfo, btnOrderInfo, btnTransferInfo;
    private ImageView ivTitleMore;
    private CircleImageView ivHeadImg;
    private TextView tvName, tvSex, tvAge, tvCompany, tvAddress;
    private SatelliteMenu mSatelliteMenuRightBottom;
    private SearchLabelLayout searchLabelLayout;
    private View viewIndicator;
    private ViewPager viewPager;
    private FragmentVpAdapter fragmentVpAdapter;
    private View view_pop;
    private PopupWindow mPopupwinow;
    private TextView tvOne, tvTwo;
    /**
     * 健康档案
     */
    private HealthInfoFragment healthInfoFragment;
    /**
     * 开单
     */
    private OrderInfoFragment orderInfoFragment;
    /**
     * 转诊
     */
    private TransferInfoFragment transferInfoFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private PatientBean patientBean;
    /**
     * 综合病史信息
     */
    private CombineBean combineBean;
    /**
     * 手术信息
     */
    private ArrayList<CombineChildBean> patientSurgeryList = new ArrayList<>();
    /**
     * 诊断信息
     */
    private ArrayList<CombineChildBean> patientDiagnosisList = new ArrayList<>();
    /**
     * 过敏信息
     */
    private ArrayList<CombineChildBean> patientAllergyList = new ArrayList<>();
    /**
     * 修改备注回调
     */
    public static final int REEMARK_REQUEST_CODE = 101;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_patient_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("患者信息");
        ivTitleMore = (ImageView)findViewById(R.id.public_title_bar_more_two);
        ivTitleMore.setVisibility(View.VISIBLE);
        ivHeadImg = (CircleImageView)findViewById(R.id.act_patient_info_headImg);
        tvName = (TextView)findViewById(R.id.act_patient_info_name);
        tvSex = (TextView)findViewById(R.id.act_patient_info_sex);
        tvAge = (TextView)findViewById(R.id.act_patient_info_age);
        tvCompany = (TextView)findViewById(R.id.act_patient_info_company);
        tvAddress = (TextView)findViewById(R.id.act_patient_info_address);
        searchLabelLayout = (SearchLabelLayout)findViewById(
                R.id.act_patient_info_searchLabelLayout);
        btnHealthInfo = (Button)findViewById(R.id.act_patient_info_health_info);
        btnOrderInfo = (Button)findViewById(R.id.act_patient_info_order_info);
        btnTransferInfo = (Button)findViewById(R.id.act_patient_info_transfer_info);
        viewPager = (ViewPager)findViewById(R.id.act_patient_info_viewpager);
        viewIndicator = findViewById(R.id.act_patient_info_indicator);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (getIntent() != null)
        {
            patientBean = (PatientBean)getIntent().getSerializableExtra(
                    CommonData.KEY_PATIENT_BEAN);
        }
        new ViewPrepared().asyncPrepare(btnHealthInfo, (w, h) ->
        {
            ViewGroup.LayoutParams params = viewIndicator.getLayoutParams();
            params.width = w;
            viewIndicator.setLayoutParams(params);
        });
        healthInfoFragment = new HealthInfoFragment();
        healthInfoFragment.setPatientBean(patientBean);
        orderInfoFragment = new OrderInfoFragment();
        orderInfoFragment.setPatientBean(patientBean);
        transferInfoFragment = new TransferInfoFragment();
        transferInfoFragment.setPatientBean(patientBean);
        fragmentList.add(healthInfoFragment);
        fragmentList.add(orderInfoFragment);
        fragmentList.add(transferInfoFragment);
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentVpAdapter);
        viewPager.setCurrentItem(0);
        initPageData();
        getPatientCombine();
        mSatelliteMenuRightBottom = (SatelliteMenu)findViewById(R.id.mSatelliteMenuRightBottom);
        List<String> nameMenuItem = new ArrayList<>();//菜单图片,可根据需要设置子菜单个数
        nameMenuItem.add("对话");
        nameMenuItem.add("转诊");
        nameMenuItem.add("服务包");
        nameMenuItem.add("病历");
        List<Integer> imageResourceRightBottom = new ArrayList<>();//菜单图片,可根据需要设置子菜单个数
        imageResourceRightBottom.add(R.mipmap.icon_info_chat);
        imageResourceRightBottom.add(R.mipmap.icon_info_transfer);
        imageResourceRightBottom.add(R.mipmap.icon_info_service);
        imageResourceRightBottom.add(R.mipmap.icon_info_health_history);
        mSatelliteMenuRightBottom.getmBuilder()
                                 .setMenuItemNameTexts(nameMenuItem)
                                 .setMenuImage(R.mipmap.icon_info_more,
                                               R.mipmap.icon_info_more_close)
                                 .setMenuItemImageResource(imageResourceRightBottom)
                                 .setOnMenuItemClickListener(this)
                                 .creat();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        ivTitleMore.setOnClickListener(this);
        btnHealthInfo.setOnClickListener(this);
        btnOrderInfo.setOnClickListener(this);
        btnTransferInfo.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int tabWidth = btnHealthInfo.getWidth();
                viewIndicator.setTranslationX((position * tabWidth) + (positionOffset * tabWidth));
            }

            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        btnHealthInfo.setSelected(true);
                        btnOrderInfo.setSelected(false);
                        btnTransferInfo.setSelected(false);
                        break;
                    case 1:
                        btnHealthInfo.setSelected(false);
                        btnOrderInfo.setSelected(true);
                        btnTransferInfo.setSelected(false);
                        break;
                    case 2:
                        btnHealthInfo.setSelected(false);
                        btnOrderInfo.setSelected(false);
                        btnTransferInfo.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    /**
     * 初始化界面数据
     */
    private void initPageData()
    {
        if (patientBean != null)
        {
            if (!TextUtils.isEmpty(patientBean.getNickname()) &&
                patientBean.getNickname().length() < 20)
            {
                tvName.setText(patientBean.getNickname() + "(" + patientBean.getName() + ")");
            }
            else
            {
                tvName.setText(patientBean.getName());
            }
            tvSex.setText(patientBean.getSex());
            tvAge.setText(AllUtils.formatDateByAge(patientBean.getBirthDate()) + "岁");
            if (!TextUtils.isEmpty(patientBean.getUnitName()))
            {
                tvCompany.setText(patientBean.getUnitName());
            }
            else
            {
                tvCompany.setText("未填写单位");
            }
            if (!TextUtils.isEmpty(patientBean.getAddress()))
            {
                tvAddress.setText(patientBean.getAddress());
            }
            else
            {
                tvAddress.setText("未填写地址");
            }
            if (!TextUtils.isEmpty(patientBean.getPatientImgUrl()))
            {
                Glide.with(this)
                     .load(patientBean.getPatientImgUrl())
                     .apply(GlideHelper.getOptionsP())
                     .into(ivHeadImg);
            }
        }
    }

    /**
     * 健康标签
     */
    private void initHealthData()
    {
        searchLabelLayout.removeAllViews();
        ArrayList<String> values = new ArrayList<>();
        values.add("健康标签:");
        if (patientDiagnosisList != null && patientDiagnosisList.size() > 0)
        {
            for (int i = 0; i < patientDiagnosisList.size(); i++)
            {
                values.add(patientDiagnosisList.get(i).getDiagnosisInfo());
            }
        }
        if (patientAllergyList != null && patientAllergyList.size() > 0)
        {
            for (int i = 0; i < patientAllergyList.size(); i++)
            {
                values.add(patientAllergyList.get(i).getAllergyInfo());
            }
        }
        if (patientSurgeryList != null && patientSurgeryList.size() > 0)
        {
            for (int i = 0; i < patientSurgeryList.size(); i++)
            {
                values.add(patientSurgeryList.get(i).getSurgeryName());
            }
        }
        if (values.size() == 1)//缺省值
        {
            values.set(0, "健康标签：未填写健康标签！");
        }
        for (int i = 0; i < values.size(); i++)
        {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_product_label, null);
            final TextView textView = view.findViewById(R.id.textView);
            if (i == 0)
            {
                textView.setBackground(null);
            }
            textView.setText(values.get(i));
            searchLabelLayout.addView(view);
        }
    }

    /**
     * 获取患者基础信息
     */
    private void getPatientCombine()
    {
        mIRequest.getPatientCombine(patientBean.getPatientId(), this);
    }

    /**
     * 删除病人(取消关注)
     */
    private void deletePatient()
    {
        mIRequest.deletePatient(loginSuccessBean.getDoctorId(), patientBean.getPatientId(), this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.act_patient_info_health_info:
                viewPager.setCurrentItem(0);
                btnHealthInfo.setSelected(true);
                btnOrderInfo.setSelected(false);
                btnTransferInfo.setSelected(false);
                break;
            case R.id.act_patient_info_order_info:
                viewPager.setCurrentItem(1);
                btnHealthInfo.setSelected(false);
                btnOrderInfo.setSelected(true);
                btnTransferInfo.setSelected(false);
                break;
            case R.id.act_patient_info_transfer_info:
                viewPager.setCurrentItem(2);
                btnHealthInfo.setSelected(false);
                btnOrderInfo.setSelected(false);
                btnTransferInfo.setSelected(true);
                break;
            case R.id.public_title_bar_more_two:
                showPop();
                break;
            case R.id.txt_one:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                intent = new Intent(this, EditRemarkActivity.class);
                intent.putExtra(CommonData.KEY_IS_DEAL_DOC, false);
                intent.putExtra(CommonData.KEY_PUBLIC, patientBean.getNickname());
                intent.putExtra(CommonData.KEY_ID, patientBean.getPatientId());
                startActivityForResult(intent, REEMARK_REQUEST_CODE);
                break;
            case R.id.txt_two:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                new SimpleDialog(this, "确定删除当前患者?", (dialog, which) -> deletePatient(),
                                 (dialog, which) -> dialog.dismiss()).show();
                break;
        }
    }

    @Override
    public void onMenuClick(View view, int postion)
    {
        Intent intent;
        switch (postion)
        {
            case 0://对话
                if (patientBean != null)
                {
                    intent = new Intent(PatientInfoActivity.this, ChatActivity.class);
                    intent.putExtra(CommonData.KEY_CHAT_ID, patientBean.getPatientId());
                    //保存最近联系人
                    RecentContactUtils.save(patientBean.getPatientId());
                    NotifyChangeListenerServer.getInstance().notifyRecentContactChange("");
                    //存储临时数据
                    YihtApplication.getInstance().setEaseName(patientBean.getName());
                    YihtApplication.getInstance().setEaseHeadImgUrl(patientBean.getPatientImgUrl());
                    startActivity(intent);
                }
                break;
            case 1://转诊
                intent = new Intent(PatientInfoActivity.this, TransferPatientActivity.class);
                intent.putExtra(CommonData.KEY_PATIENT_BEAN, patientBean);
                intent.putExtra(CommonData.KEY_PUBLIC, true);
                startActivity(intent);
                break;
            case 2://服务包
                intent = new Intent(PatientInfoActivity.this, ServicePackActivity.class);
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientBean.getPatientId());
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "服务");
                startActivity(intent);
                break;
            case 3://病例
                intent = new Intent(PatientInfoActivity.this, HealthDetailActivity.class);
                intent.putExtra(CommonData.KEY_ADD_NEW_HEALTH, true);
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientBean.getPatientId());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_PATIENT_COMBINE:
                combineBean = response.getData();
                if (combineBean != null)
                {
                    patientDiagnosisList = combineBean.getDiagnosisInfo();
                    patientAllergyList = combineBean.getAllergyInfo();
                    patientSurgeryList = combineBean.getSurgeryInfo();
                    initHealthData();
                }
                break;
            case DELETE_PATIENT:
                RecentContactUtils.delete(patientBean.getPatientId());
                NotifyChangeListenerServer.getInstance().notifyRecentContactChange("");
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case REEMARK_REQUEST_CODE:
                setResult(RESULT_OK);
                if (data != null)
                {
                    String remark = data.getStringExtra(CommonData.KEY_PUBLIC);
                    if (!TextUtils.isEmpty(remark))
                    {
                        tvName.setText(remark + "(" + patientBean.getName() + ")");
                        patientBean.setNickname(remark);
                    }
                    else
                    {
                        tvName.setText(patientBean.getName());
                        patientBean.setNickname(remark);
                    }
                }
                break;
        }
    }

    /**
     * 显示pop
     */
    private void showPop()
    {
        view_pop = LayoutInflater.from(this).inflate(R.layout.health_pop_menu, null);
        tvOne = view_pop.findViewById(R.id.txt_one);
        tvTwo = view_pop.findViewById(R.id.txt_two);
        tvTwo.setText("删除好友");
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        if (mPopupwinow == null)
        {
            //新建一个popwindow
            mPopupwinow = new PopupWindow(view_pop, LinearLayout.LayoutParams.WRAP_CONTENT,
                                          LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupwinow.setFocusable(true);
        mPopupwinow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupwinow.setOutsideTouchable(true);
        mPopupwinow.showAtLocation(view_pop, Gravity.TOP | Gravity.RIGHT, 0,
                                   (int)AllUtils.dipToPx(this, 55));
    }
}
