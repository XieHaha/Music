package com.zyc.doctor.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zyc.doctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ios样式对话框
 *
 * @author USER
 */
public class ActionSheetDialog {
    private static final int MAX_ITEM = 7;
    private Context context;
    private Dialog dialog;
    private TextView txtTitle;
    private TextView txtCancel;
    private LinearLayout lLayoutContent;
    private ScrollView sLayoutContent;
    private boolean showTitle = false;
    private List<SheetItem> sheetItemList;
    private Display display;
    private OnCancelBtnClicked onCancelBtnClicked;

    public void setOnCancelBtnClicked(OnCancelBtnClicked onCancelBtnClicked) {
        this.onCancelBtnClicked = onCancelBtnClicked;
    }

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_actionsheet, null);
        view.setMinimumWidth(display.getWidth());
        sLayoutContent = (ScrollView)view.findViewById(R.id.sLayout_content);
        lLayoutContent = (LinearLayout)view.findViewById(R.id.lLayout_content);
        txtTitle = (TextView)view.findViewById(R.id.txt_title);
        txtCancel = (TextView)view.findViewById(R.id.txt_cancel);
        txtCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancelBtnClicked != null) {
                    onCancelBtnClicked.onClick();
                }
                dialog.dismiss();
            }
        });
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    public ActionSheetDialog setTitle(String title) {
        showTitle = true;
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(title);
        return this;
    }

    public ActionSheetDialog hideCancelText(boolean bool) {
        if (bool) {
            txtCancel.setVisibility(View.GONE);
        }
        else {
            txtCancel.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public ActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color, OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(strItem, color, listener));
        return this;
    }

    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }
        int size = sheetItemList.size();
        if (size >= MAX_ITEM) {
            LayoutParams params = (LayoutParams)sLayoutContent.getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayoutContent.setLayoutParams(params);
        }
        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            final OnSheetItemClickListener listener = (OnSheetItemClickListener)sheetItem.itemClickListener;
            TextView textView = new TextView(context);
            textView.setText(strItem);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                }
                else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            }
            else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    }
                    else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                }
                else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                    }
                    else if (i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    }
                    else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                }
            }
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue.getValue()));
            }
            else {
                textView.setTextColor(Color.parseColor(color.getValue()));
            }
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int)(45 * scale + 0.5f);
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(index);
                    dialog.dismiss();
                }
            });
            lLayoutContent.addView(textView);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    /**
     * 向外暴露一个接口点击取消做相应的操作
     *
     * @author Administrator
     */
    public interface OnCancelBtnClicked {
        void onClick();
    }

    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;

        public SheetItem(String name, SheetItemColor color, OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }

    public enum SheetItemColor {
        /**
         * blue
         */
        Blue("#037BFF"),
        /**
         * red
         */
        Red("#FD4A2E");
        private String value;

        SheetItemColor(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
