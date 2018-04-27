//package com.yht.yihuantong.cache;
//
///**
// * 管理web服务器上的昵称头像
// * Created by Martin on 2017/4/26.
// */
//
//public class UserWebManager {
//
////    /**
////     * 配置Web数据存储服务
////     * @param appId 数据存储服务的AppID
////     * @param appKey 数据存储服务的AppKey
////     */
////    public static void config(Context context, String appId, String appKey){
////    }
////
////    /**
////     * 获取用户信息
////     * @param userid
////     * @param callback
////     */
////    public static void getUserInfoAync(String userid, final UserCallback callback){
////
////    }
////
////    /**
////     * 创建用户
////     * @param userId 环信ID
////     * @param nickName 昵称
////     * @param avatarUrl 头像Url
////     */
////    public static void createUser(final String userId, final String nickName, final String avatarUrl){
////        UserWebManager.getUserInfoAync(userId, new UserCallback() {
////            @Override
////            public void onCompleted(UserWebInfo info) {
////                if(info != null) return;
////
////                UserWebInfo  user = new UserWebInfo();
////
////                user.setOpenId(userId);
////                user.setNickName(nickName);
////                user.setAvatarUrl(avatarUrl);
////
////                // 如果用户目前尚未接入网络，saveEventually会缓存设备中的数据，并在网络连接恢复后上传
////                user.saveEventually();
////            }
////        });
////    }
////
////
////    /**
////     * 保存用户信息（如果已存在，则更新）
////     * @param userId 环信ID
////     * @param nickName 昵称
////     * @param avatarUrl 头像Url
////     */
////    public static void saveInfo(String userId, final String nickName, final String avatarUrl){
////        UserWebManager.getUserInfoAync(userId, new UserCallback() {
////            @Override
////            public void onCompleted(UserWebInfo info) {
////                if (info == null){
////                    info = new UserWebInfo();
////                }
////
////                info.setNickName(nickName);
////                info.setAvatarUrl(avatarUrl);
////
////                // 如果用户目前尚未接入网络，saveEventually会缓存设备中的数据，并在网络连接恢复后上传
////                info.saveEventually();
////            }
////        });
////    }
//
////    public interface UserCallback {
////        /**
////         * 数据获取完成后
////         * @param info
////         */
////        void onCompleted(UserWebInfo info);
////    }
//}
//
//
