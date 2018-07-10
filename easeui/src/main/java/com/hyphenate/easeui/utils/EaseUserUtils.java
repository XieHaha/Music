package com.hyphenate.easeui.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.UserInfoCallback;
import com.hyphenate.easeui.domain.EaseUser;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    private static RequestOptions optionsRect;
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username,UserInfoCallback callback){
        if(userProvider != null)
            return userProvider.getUser(username,callback);
        
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(final Context context, String username, final ImageView imageView){
    	EaseUser user = getUserInfo(username,new UserInfoCallback(){
            @Override
            public void onSuccess(EaseUser user)
            {
                if(user != null && !TextUtils.isEmpty(user.getAvatar())){
                    try {
                        int avatarResId = Integer.parseInt(user.getAvatar());
                        Glide.with(context).load(avatarResId).apply(getOptionsRect()).into(imageView);
                    } catch (Exception e) {
                        //use default avatar
                        Glide.with(context).load(user.getAvatar()).apply(getOptionsRect()).into(imageView);
                    }
                }else{
                    Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
                }
            }
        });

    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(final String username, final TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username, new UserInfoCallback() {
                @Override
                public void onSuccess(EaseUser user)
                {
                    if(user != null && user.getNick() != null){
                        textView.setText(user.getNick());
                    }else{
                        textView.setText(username);
                    }
                }
            });

        }
    }

    public  static RequestOptions getOptionsRect() {
        if (optionsRect == null) {
            optionsRect = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ease_default_avatar)
                    .error(R.drawable.ease_default_avatar)
                    .priority(Priority.NORMAL);
        }
        return optionsRect;
    }
    
}
