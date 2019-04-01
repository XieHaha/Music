package com.zyc.doctor.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.ui.dialog.listener.OnCancelClickListener;
import com.zyc.doctor.ui.dialog.listener.OnEnterClickListener;

public class HintDialog extends Dialog implements OnClickListener {
    private Context context;
    private Button enter, cancel;
    private View lineView;
    private TextView content;
    private String contentString = "确认合格？";
    private String enterString = "确定";
    private int enterColor = 0;
    private String cancleString = "取消";
    private boolean isShowCancelBtn = true;
    private boolean isShow = false;

    public HintDialog(Context context) {
        super(context, R.style.normal_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//状态栏透明
        setContentView(R.layout.dialog_hint);
        initWidget();
        init();
    }

    private void initWidget() {
        enter = (Button)findViewById(R.id.dialog_simple_hint_enter);
        cancel = (Button)findViewById(R.id.dialog_simple_hint_cancel);
        content = (TextView)findViewById(R.id.dialog_simple_hint_content);
        lineView = findViewById(R.id.dialog_simple_hint_line);
        setCanceledOnTouchOutside(true);
        cancel.setOnClickListener(this);
        enter.setOnClickListener(this);
    }

    private void init() {
        setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        if (v == enter) {
            if (onEnterClickListener != null) {
                onEnterClickListener.onEnter();
            }
            dismiss();
        }
        else if (v == cancel) {
            if (onCancelClickListener != null) {
                onCancelClickListener.onCancel();
            }
            dismiss();
        }
    }

    /**
     * 设置提示语的文本
     *
     * @param contentString
     */
    public HintDialog setContentString(String contentString) {
        this.contentString = contentString;
        return this;
    }

    /**
     * 设置确定按钮的文本
     */
    public HintDialog setEnterBtnTxt(String str) {
        this.enterString = str;
        return this;
    }

    /**
     * 设置确定按钮的颜色
     */
    public HintDialog setEnterTxtColor(int color) {
        this.enterColor = color;
        return this;
    }

    /**
     * 设置取消Btn显示/隐藏
     *
     * @param isShowCancelBtn
     */
    public void isShowCancelBtn(boolean isShowCancelBtn) {
        this.isShowCancelBtn = isShowCancelBtn;
    }

    /**
     * 设置取消按钮的文本
     */
    public HintDialog setCancleBtnTxt(String str) {
        this.cancleString = str;
        return this;
    }

    @Override
    public void show() {
        if (!isShow) {
            super.show();
            content.setText(contentString);
            enter.setText(enterString);
            if (enterColor != 0) { enter.setTextColor(content.getResources().getColor(enterColor)); }
            if (!isShowCancelBtn) {
                cancel.setVisibility(View.GONE);
                lineView.setVisibility(View.GONE);
            }
            else {
                cancel.setText(cancleString);
            }
            isShow = true;
        }
    }

    private OnEnterClickListener onEnterClickListener = null;
    private OnCancelClickListener onCancelClickListener = null;

    public HintDialog setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
        return this;
    }

    public HintDialog setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
        return this;
    }
}
