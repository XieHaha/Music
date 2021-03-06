package com.zyc.doctor.ui.activity;

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
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.chat.ChatActivity;
import com.zyc.doctor.data.BaseData;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CombineBean;
import com.zyc.doctor.data.bean.CombineChildBean;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.adapter.FragmentVpAdapter;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.ui.dialog.HintDialog;
import com.zyc.doctor.ui.dialog.listener.OnEnterClickListener;
import com.zyc.doctor.ui.fragment.HealthInfoFragment;
import com.zyc.doctor.ui.fragment.OrderInfoFragment;
import com.zyc.doctor.ui.fragment.TransferInfoFragment;
import com.zyc.doctor.utils.BaseUtils;
import com.zyc.doctor.utils.RecentContactUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.utils.glide.GlideHelper;
import com.zyc.doctor.widgets.menu.SatelliteMenu;
import com.zyc.doctor.widgets.view.SearchLabelLayout;
import com.zyc.doctor.widgets.view.ViewPrepared;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * ๆ็้กต้ข
 *
 * @author dundun
 */
public class PatientInfoActivity extends BaseActivity implements SatelliteMenu.OnMenuItemClickListener {
    @BindView(R.id.public_title_bar_more_two)
    ImageView ivTitleMore;
    @BindView(R.id.act_patient_info_headImg)
    CircleImageView ivHeadImg;
    @BindView(R.id.act_patient_info_name)
    TextView tvName;
    @BindView(R.id.act_patient_info_sex)
    TextView tvSex;
    @BindView(R.id.act_patient_info_age)
    TextView tvAge;
    @BindView(R.id.act_patient_info_company)
    TextView tvCompany;
    @BindView(R.id.act_patient_info_address)
    TextView tvAddress;
    @BindView(R.id.act_patient_info_searchLabelLayout)
    SearchLabelLayout searchLabelLayout;
    @BindView(R.id.act_patient_info_health_info)
    Button btnHealthInfo;
    @BindView(R.id.act_patient_info_order_info)
    Button btnOrderInfo;
    @BindView(R.id.act_patient_info_transfer_info)
    Button btnTransferInfo;
    @BindView(R.id.act_patient_info_indicator)
    View viewIndicator;
    @BindView(R.id.act_patient_info_viewpager)
    ViewPager viewPager;
    @BindView(R.id.mSatelliteMenuRightBottom)
    SatelliteMenu mSatelliteMenuRightBottom;
    private FragmentVpAdapter fragmentVpAdapter;
    private View viewPop;
    private PopupWindow mPopupwinow;
    private TextView tvOne, tvTwo;
    /**
     * ๅฅๅบทๆกฃๆก
     */
    private HealthInfoFragment healthInfoFragment;
    /**
     * ๅผๅ
     */
    private OrderInfoFragment orderInfoFragment;
    /**
     * ่ฝฌ่ฏ
     */
    private TransferInfoFragment transferInfoFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private PatientBean patientBean;
    private String patientId;
    /**
     * ็ปผๅ็ๅฒไฟกๆฏ
     */
    private CombineBean combineBean;
    /**
     * ๆๆฏไฟกๆฏ
     */
    private ArrayList<CombineChildBean> patientSurgeryList = new ArrayList<>();
    /**
     * ่ฏๆญไฟกๆฏ
     */
    private ArrayList<CombineChildBean> patientDiagnosisList = new ArrayList<>();
    /**
     * ่ฟๆไฟกๆฏ
     */
    private ArrayList<CombineChildBean> patientAllergyList = new ArrayList<>();
    /**
     * ไฟฎๆนๅคๆณจๅ่ฐ
     */
    public static final int REEMARK_REQUEST_CODE = 101;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_patient_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ivTitleMore.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            patientBean = (PatientBean)getIntent().getSerializableExtra(CommonData.KEY_PATIENT_BEAN);
            patientId = getIntent().getStringExtra(CommonData.KEY_PATIENT_ID);
        }
        new ViewPrepared().asyncPrepare(btnHealthInfo, (w, h) -> {
            ViewGroup.LayoutParams params = viewIndicator.getLayoutParams();
            params.width = w;
            viewIndicator.setLayoutParams(params);
        });
        healthInfoFragment = new HealthInfoFragment();
        if (patientBean == null) {
            healthInfoFragment.setPatientId(patientId);
        }
        else {
            healthInfoFragment.setPatientBean(patientBean);
        }
        orderInfoFragment = new OrderInfoFragment();
        if (patientBean == null) {
            orderInfoFragment.setPatientId(patientId);
        }
        else {
            orderInfoFragment.setPatientBean(patientBean);
        }
        transferInfoFragment = new TransferInfoFragment();
        if (patientBean == null) {
            transferInfoFragment.setPatientId(patientId);
        }
        else {
            transferInfoFragment.setPatientBean(patientBean);
        }
        fragmentList.add(healthInfoFragment);
        fragmentList.add(orderInfoFragment);
        fragmentList.add(transferInfoFragment);
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentVpAdapter);
        viewPager.setCurrentItem(0);
        initMenu();
        if (patientBean == null) {
            getPatientInfo();
        }
        else {
            patientId = patientBean.getPatientId();
            initPageData();
        }
        getPatientCombine();
    }

    private void initMenu() {
        //่ๅๅพ็,ๅฏๆ?นๆฎ้่ฆ่ฎพ็ฝฎๅญ่ๅไธชๆฐ
        List<String> nameMenuItem = new ArrayList<>();
        nameMenuItem.add(getString(R.string.txt_menu_chat));
        nameMenuItem.add(getString(R.string.txt_menu_transfer));
        nameMenuItem.add(getString(R.string.txt_menu_service));
        nameMenuItem.add(getString(R.string.txt_menu_case));
        //่ๅๅพ็,ๅฏๆ?นๆฎ้่ฆ่ฎพ็ฝฎๅญ่ๅไธชๆฐ
        List<Integer> imageResourceRightBottom = new ArrayList<>();
        imageResourceRightBottom.add(R.mipmap.icon_info_chat);
        imageResourceRightBottom.add(R.mipmap.icon_info_transfer);
        imageResourceRightBottom.add(R.mipmap.icon_info_service);
        imageResourceRightBottom.add(R.mipmap.icon_info_health_history);
        mSatelliteMenuRightBottom.getmBuilder()
                                 .setMenuItemNameTexts(nameMenuItem)
                                 .setMenuImage(R.mipmap.icon_info_more, R.mipmap.icon_info_more_close)
                                 .setMenuItemImageResource(imageResourceRightBottom)
                                 .setOnMenuItemClickListener(this)
                                 .creat();
    }

    @Override
    public void initListener() {
        super.initListener();
        ivTitleMore.setOnClickListener(this);
        btnHealthInfo.setOnClickListener(this);
        btnOrderInfo.setOnClickListener(this);
        btnTransferInfo.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int tabWidth = btnHealthInfo.getWidth();
                viewIndicator.setTranslationX((position * tabWidth) + (positionOffset * tabWidth));
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
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
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * ๅๅงๅ็้ขๆฐๆฎ
     */
    private void initPageData() {
        if (patientBean != null) {
            if (!TextUtils.isEmpty(patientBean.getNickName()) &&
                patientBean.getNickName().length() < BaseData.BASE_NICK_NAME_LENGTH) {
                tvName.setText(String.format(getString(R.string.txt_name_format), patientBean.getNickName(),
                                             patientBean.getName()));
            }
            else {
                tvName.setText(patientBean.getName());
            }
            tvSex.setText(patientBean.getSex());
            tvAge.setText(BaseUtils.getAge(patientBean.getBirthDate()) + "ๅฒ");
            if (!TextUtils.isEmpty(patientBean.getUnitName())) {
                tvCompany.setText(patientBean.getUnitName());
            }
            else {
                tvCompany.setText("ๆชๅกซๅๅไฝ");
            }
            if (!TextUtils.isEmpty(patientBean.getAddress())) {
                tvAddress.setText(patientBean.getAddress());
            }
            else {
                tvAddress.setText("ๆชๅกซๅๅฐๅ");
            }
            if (!TextUtils.isEmpty(patientBean.getPatientImgUrl())) {
                Glide.with(this).load(patientBean.getPatientImgUrl()).apply(GlideHelper.getOptionsP()).into(ivHeadImg);
            }
        }
    }

    /**
     * ๅฅๅบทๆ?็ญพ
     */
    private void initHealthData() {
        searchLabelLayout.removeAllViews();
        ArrayList<String> values = new ArrayList<>();
        values.add("ๅฅๅบทๆ?็ญพ:");
        if (patientDiagnosisList != null && patientDiagnosisList.size() > 0) {
            for (int i = 0; i < patientDiagnosisList.size(); i++) {
                values.add(patientDiagnosisList.get(i).getDiagnosisInfo());
            }
        }
        if (patientAllergyList != null && patientAllergyList.size() > 0) {
            for (int i = 0; i < patientAllergyList.size(); i++) {
                values.add(patientAllergyList.get(i).getAllergyInfo());
            }
        }
        if (patientSurgeryList != null && patientSurgeryList.size() > 0) {
            for (int i = 0; i < patientSurgeryList.size(); i++) {
                values.add(patientSurgeryList.get(i).getSurgeryName());
            }
        }
        //็ผบ็ๅผ
        if (values.size() == 1) {
            values.set(0, "ๅฅๅบทๆ?็ญพ๏ผๆชๅกซๅๅฅๅบทๆ?็ญพ๏ผ");
        }
        for (int i = 0; i < values.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_product_label, null);
            final TextView textView = view.findViewById(R.id.textView);
            if (i == 0) {
                textView.setBackground(null);
            }
            textView.setText(values.get(i));
            searchLabelLayout.addView(view);
        }
    }

    /**
     * ่ทๅๆฃ่ไธชไบบไฟกๆฏ
     */
    private void getPatientInfo() {
        RequestUtils.getPatientInfo(this, patientId, this);
    }

    /**
     * ่ทๅๆฃ่ๅบ็กไฟกๆฏ
     */
    private void getPatientCombine() {
        RequestUtils.getPatientCombine(this, patientId, this);
    }

    /**
     * ๅ?้ค็ไบบ(ๅๆถๅณๆณจ)
     */
    private void deletePatient() {
        RequestUtils.deletePatient(this, loginSuccessBean.getDoctorId(), patientId, this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
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
                if (mPopupwinow != null) {
                    mPopupwinow.dismiss();
                }
                intent = new Intent(this, EditRemarkActivity.class);
                intent.putExtra(CommonData.KEY_IS_DEAL_DOC, false);
                intent.putExtra(CommonData.KEY_PUBLIC, patientBean.getNickName());
                intent.putExtra(CommonData.KEY_ID, patientBean.getPatientId());
                startActivityForResult(intent, REEMARK_REQUEST_CODE);
                break;
            case R.id.txt_two:
                if (mPopupwinow != null) {
                    mPopupwinow.dismiss();
                }
                HintDialog hintDialog = new HintDialog(this);
                hintDialog.setContentString("็กฎๅฎๅ?้คๅฝๅๆฃ่?");
                hintDialog.setOnEnterClickListener(new OnEnterClickListener() {
                    @Override
                    public void onEnter() {
                        deletePatient();
                    }
                });
                hintDialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMenuClick(View view, int postion) {
        Intent intent;
        switch (postion) {
            //ๅฏน่ฏ
            case 0:
                if (patientBean != null) {
                    intent = new Intent(PatientInfoActivity.this, ChatActivity.class);
                    intent.putExtra(CommonData.KEY_CHAT_ID, patientBean.getPatientId());
                    //ไฟๅญๆ่ฟ่็ณปไบบ
                    RecentContactUtils.save(patientBean.getPatientId());
                    NotifyChangeListenerManager.getInstance().notifyRecentContactChange("");
                    //ๅญๅจไธดๆถๆฐๆฎ
                    YihtApplication.getInstance().setEaseName(patientBean.getName());
                    YihtApplication.getInstance().setEaseHeadImgUrl(patientBean.getPatientImgUrl());
                    startActivity(intent);
                }
                break;
            //่ฝฌ่ฏ
            case 1:
                intent = new Intent(PatientInfoActivity.this, TransferPatientActivity.class);
                intent.putExtra(CommonData.KEY_PATIENT_BEAN, patientBean);
                intent.putExtra(CommonData.KEY_PUBLIC, true);
                startActivity(intent);
                break;
            //ๆๅกๅ
            case 2:
                intent = new Intent(PatientInfoActivity.this, ServicePackActivity.class);
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientBean.getPatientId());
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "ๆๅก");
                startActivity(intent);
                break;
            //็ไพ
            case 3:
                intent = new Intent(PatientInfoActivity.this, HealthDetailActivity.class);
                intent.putExtra(CommonData.KEY_ADD_NEW_HEALTH, true);
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientBean.getPatientId());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_PATIENT_INFO:
                patientBean = (PatientBean)response.getData();
                initPageData();
                break;
            case GET_PATIENT_COMBINE:
                combineBean = (CombineBean)response.getData();
                if (combineBean != null) {
                    patientDiagnosisList = combineBean.getDiagnosisInfo();
                    patientAllergyList = combineBean.getAllergyInfo();
                    patientSurgeryList = combineBean.getSurgeryInfo();
                    initHealthData();
                }
                break;
            case DELETE_PATIENT:
                RecentContactUtils.delete(patientBean.getPatientId());
                NotifyChangeListenerManager.getInstance().notifyRecentContactChange("");
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REEMARK_REQUEST_CODE:
                setResult(RESULT_OK);
                if (data != null) {
                    String remark = data.getStringExtra(CommonData.KEY_PUBLIC);
                    if (!TextUtils.isEmpty(remark)) {
                        tvName.setText(
                                String.format(getString(R.string.txt_name_format), remark, patientBean.getName()));
                        patientBean.setNickName(remark);
                    }
                    else {
                        tvName.setText(patientBean.getName());
                        patientBean.setNickName(remark);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * ๆพ็คบpop
     */
    private void showPop() {
        viewPop = LayoutInflater.from(this).inflate(R.layout.health_pop_menu, null);
        tvOne = viewPop.findViewById(R.id.txt_one);
        tvTwo = viewPop.findViewById(R.id.txt_two);
        tvTwo.setText(R.string.txt_menu_delete);
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        if (mPopupwinow == null) {
            //ๆฐๅปบไธไธชpopwindow
            mPopupwinow = new PopupWindow(viewPop, LinearLayout.LayoutParams.WRAP_CONTENT,
                                          LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupwinow.setFocusable(true);
        mPopupwinow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupwinow.setOutsideTouchable(true);
        mPopupwinow.showAtLocation(viewPop, Gravity.TOP | Gravity.RIGHT, 0, (int)BaseUtils.dipToPx(this, 55));
    }
}
