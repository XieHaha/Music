package com.zyc.doctor.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import com.zyc.doctor.R;
import com.zyc.doctor.utils.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dundun
 * @date 16/10/21
 */
public class FilterEmojiEditText extends AppCompatEditText {
    private int maxTextLength = 100;
    private String filterImoji = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
    private InputFilter[] emojiFilters;
    private Context context;

    public FilterEmojiEditText(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public FilterEmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public FilterEmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.FilterEmojiEditText);
        maxTextLength = styledAttrs.getInt(R.styleable.FilterEmojiEditText_textLength, 100);
        styledAttrs.recycle();
        emojiFilters = new InputFilter[] {
                emojiFilter, new InputFilter.LengthFilter(maxTextLength) };
        setFilters(emojiFilters);
    }

    InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast(getContext(), R.string.toast_limit_emoji);
                return "";
            }
            return null;
        }

        Pattern emoji = Pattern.compile(filterImoji, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    };
}
