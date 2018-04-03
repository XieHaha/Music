package custom.frame.http;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestParams;

import java.util.HashMap;
import java.util.Map;

import custom.base.entity.BannerInfo;
import custom.base.entity.BookingDetail;
import custom.base.entity.CarBaseInfo;
import custom.base.entity.CarLocationInfo;
import custom.base.entity.ChargeDetail;
import custom.base.entity.Charger;
import custom.base.entity.ChargerFriends;
import custom.base.entity.ChargerNews;
import custom.base.entity.ChargerStationDetail;
import custom.base.entity.DzFriends;
import custom.base.entity.ForeignAuth;
import custom.base.entity.FreeChargerNum;
import custom.base.entity.LabelInfo;
import custom.base.entity.NormImage;
import custom.base.entity.OngoingOperation;
import custom.base.entity.OrderDetail;
import custom.base.entity.PlugStatus;
import custom.base.entity.Rate;
import custom.base.entity.StationComment;
import custom.base.entity.Store;
import custom.base.entity.User;
import custom.base.entity.UserBalance;
import custom.base.entity.Version;
import custom.base.entity.base.BaseResponse;
import custom.base.entity.base.ChargerStation;
import custom.base.entity.base.OrderBase;
import custom.base.entity.base.UserCard;
import custom.frame.http.listener.ResponseListener;

import static custom.frame.http.data.HttpConstants.Method.DELETE;
import static custom.frame.http.data.HttpConstants.Method.GET;
import static custom.frame.http.data.HttpConstants.Method.POST;
import static custom.frame.http.data.HttpConstants.Method.PUT;

/**
 * Created by luozi on 2015/12/30.
 * baseRequest include requestString and requestObject and requestList
 */
public class IRequest extends BaseRequest {
    /**
     * 单例模式
     */
    private volatile static IRequest instance = null;
    /**
     * 公共模块
     */
    private String PUBLIC = "pub";
    private String USER = "user";
    private String STATION = "station";
    private String RESERVATION = "reservation";
    private String CHARGE = "charge";
    private String CHARGER = "charger";
    private String FUND = "fund";
    private String CAR = "car";
    private String QINIU = "qiniu";
    private String SHARE = "share";
    private String INFO = "info";

    /**
     * 单例模式
     */
    public static IRequest getInstance(Context context) {
        synchronized (IRequest.class) {
            if (instance == null) instance = new IRequest(context);
            return instance;
        }
    }

    /**
     * 一个新的单例
     */
    public static IRequest newInstance(Context context) {
        return new IRequest(context);
    }


    /**
     * 父类构造函数
     */
    private IRequest(Context context) {
        super(context);
    }

    /**
     * 得到基础连接地址
     */
    private RequestParams getBaseMap(String requestName) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("m", "app_server");
        params.addBodyParameter("c", "api");
        params.addBodyParameter("a", requestName);
        return params;
    }

//    /**
//     * 字符串URLEncoder编码
//     *
//     * @param str
//     * @return 编码后的字符串
//     */
//    private String doURLEncoder(String str) {
//        String string = "";
//        try {
//            string = URLEncoder.encode(str, HTTP.UTF_8);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return string;
//    }


    /**
     * 得到动态列表
     */
//    public Tasks getZhuangyouList(int page, final ResponseListener<BaseResponse> listener) {
//        RequestParams params = getBaseMap("zhuangyouquanlist");
//        params.addBodyParameter("page", page + "");
//
//        return requestBaseResponseList(POST, Tasks.ZHUANGYOU_LIST, DzFriends.class, params, listener);
//    }

    /**
     * token校验
     */
    public Tasks doCheckOutToken(String token, String jpushRegId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("token", token);
        params.addBodyParameter("jPushRegId", jpushRegId);

        return requestBaseResponse(PUT, USER, "checkOutToken", Tasks.CHECK_OUT_TOKEN, User.class, params, listener);
    }

    /**
     * 验证手机号是否注册
     */
    public Tasks checkPhoneExists(String phoneNumber, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phoneNumber);

        return requestBaseResponse(POST, USER, "userExists", Tasks.CHECK_PHONE_EXISTS, String.class, params, listener);
    }

    /**
     * 获取验证码
     */
    public Tasks getVerifyCode(String phoneNumber, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phoneNumber);
//        params.addBodyParameter("merchantCode", "");
        return requestBaseResponse(GET, PUBLIC, "sendVerifyCode", Tasks.GET_VERIFY_CODE, String.class, params, listener);
    }

    /**
     * 验证验证码
     */
    public Tasks checkVerifyCode(String phoneNumber, String verifyCode, String source, String merchantCode,
                                 String account, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phoneNumber);
        params.addBodyParameter("verifyCode", verifyCode);

        params.addBodyParameter("source", source);
        params.addBodyParameter("merchantCode", merchantCode);
        params.addBodyParameter("account", account);
        return requestBaseResponse(POST, PUBLIC, "validationVerifyCode", Tasks.CHECK_VERIFY_CODE, null, params, listener);
    }

    /**
     * 注册
     */
    public Tasks doRegister(String phoneNumber, String pwd, String lat,
                            String lng, String address, String areaCode,
                            final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phoneNumber);
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("latitude", lat);
        params.addBodyParameter("longitude", lng);
        params.addBodyParameter("address_detail", address);
        params.addBodyParameter("area_code", areaCode);
        return requestBaseResponse(PUT, USER, "registerUser", Tasks.REGISTER_USER, User.class, params, listener);
    }

    /**
     * 登录
     */
    public Tasks doLogin(String phoneNumber, String pwd, String jpushRegId,
                         final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phoneNumber);
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("source", "104");
        params.addBodyParameter("merchantCode", "1201601001");
        params.addBodyParameter("account", phoneNumber);
        params.addBodyParameter("jPushRegId", jpushRegId);
        return requestBaseResponse(POST, USER, "userLogin", Tasks.USER_LOGIN, User.class, params, listener);
    }

    /**
     * 第三方登录
     *
     * @param source       渠道
     * @param merchantCode 运营商
     * @param account      对应账号
     * @param jPushRegId   推送id
     * @param listener     监听
     */
    public Tasks foreignLogin(String source, String merchantCode, String account, String jPushRegId,
                              final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("source", source);
        params.addBodyParameter("merchantCode", merchantCode);
        params.addBodyParameter("account", account);
        params.addBodyParameter("jPushRegId", jPushRegId);
        return requestBaseResponse(POST, USER, "autoLogin", Tasks.FOREIGN_LOGIN, User.class, params, listener);
    }

    /**
     * 未登录绑定
     *
     * @param source       渠道
     * @param merchantCode 运营商
     * @param account      对应账号
     * @param address      地址
     * @param bindingType  绑定类型1微信2QQ3新浪微博4腾讯微博；默认微信
     * @param nickname     昵称
     * @param phone        电话
     * @param sex          性别
     * @param userImage    头像地址
     * @param listener     监听
     */
    public Tasks foreignNotLoginBind(String source, String merchantCode, String account, String phone, String verifyCode,
                                     String bindingType, String nickname, String sex, String userImage, String address,
                                     final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("source", source);
        params.addBodyParameter("merchantCode", merchantCode);
        params.addBodyParameter("account", account);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("verifyCode", verifyCode);
        params.addBodyParameter("bindingType", bindingType);
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("sex", sex);
        params.addBodyParameter("userImage", userImage);
        params.addBodyParameter("address", address);
        return requestBaseResponse(POST, USER, "noLoginBindingUser", Tasks.NOT_LOGIN_BIND_FOREIGN, null, params, listener);
    }

    /**
     * 登录后绑定
     *
     * @param source       渠道
     * @param merchantCode 运营商
     * @param account      对应账号
     * @param bindingType  绑定类型1微信2QQ3新浪微博4腾讯微博；默认微信
     * @param phone        电话
     * @param listener     监听
     * @param userId
     */
    public Tasks foreignLoginBind(String source, String merchantCode, String account, String phone, String userId,
                                  String bindingType,
                                  final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("source", source);
        params.addBodyParameter("merchantCode", merchantCode);
        params.addBodyParameter("account", account);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("bindingType", bindingType);
        return requestBaseResponse(POST, USER, "loginBindingUser", Tasks.LOGIN_BIND_FOREIGN, null, params, listener);
    }

    /**
     * 手机号快捷登录
     */
    public Tasks doQuickLogin(String phoneNumber, String verifyCcode, String jpushRegId,
                              final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phoneNumber);
        params.addBodyParameter("verifyCode", verifyCcode);
        params.addBodyParameter("jPushRegId", jpushRegId);
        return requestBaseResponse(PUT, USER, "fastLogin", Tasks.QUICK_LOGIN, User.class, params, listener);
    }

    /**
     * 后台登录
     */
    public Tasks doBackroundLogin(String phoneNumber, String token,
                                  final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phoneNumber);
        return requestBaseResponse(POST, USER, "loginInBackground", Tasks.BACKROUND_LOGIN, User.class, params, heads, listener);
    }

    /**
     * 找回密码,重置密码
     */
    public Tasks resetPassword(String verifyCode, String phoneNumber, String pwd, String confirmPwd,
                               final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", phoneNumber);
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("verifyCode", verifyCode);
        params.addBodyParameter("confirmPassword", confirmPwd);
        return requestBaseResponse(PUT, USER, "resetPassword", Tasks.RESET_PASSWORD, null, params, listener);
    }

    /**
     * 修改密码
     */
    public Tasks updatePassWord(String userId,String phone, String newPwd, String confirmPwd, String type, String merchantCode,
                                String token, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("password", newPwd);
        params.addBodyParameter("confirmPassword", confirmPwd);
        params.addBodyParameter("type", type);
        params.addBodyParameter("merchantCode", merchantCode);//所属运营商    不传，默认电桩
        return requestBaseResponse(POST, USER, "setPassword", Tasks.UPDATE_PASSWORD, null, params, heads, listener);
    }

    /**
     * 编辑个人资料
     */
    public Tasks updateUserInfo(User user, NormImage normImage, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", user.getToken());
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", user.getUserId());
        params.addBodyParameter("nickName", user.getNickName());
        params.addBodyParameter("signature", user.getSignature());
        params.addBodyParameter("sex", user.getSex());
        params.addBodyParameter("address", user.getAddress());
        params.addBodyParameter("longitude", user.getLongitude());
        params.addBodyParameter("latitude", user.getLatitude());
        params.addBodyParameter("email", user.getEmail());
        params.addBodyParameter("areaCode", "");
//        params.addBodyParameter("carTypeId", user.getCarTypeId());
        params.addBodyParameter("bigImage", normImage.getBigImageName());
        params.addBodyParameter("middleImage", normImage.getMiddleImageName());
        params.addBodyParameter("smallImage", normImage.getSmallImageName());
        return requestBaseResponse(PUT, USER, "updateUserInfo", Tasks.UPDATE_USER_INFO, User.class, params, heads, listener);
    }

    /**
     * 获取用户的卡号列表
     */
    public Tasks getUserCard(String token, String userId, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        return requestBaseResponseList(GET, USER, "getUserCard", Tasks.GET_USER_CARD, UserCard.class, params, heads, listener);
    }

    /**
     * 获取用户余额
     */
    public Tasks getUserBalance(String token, String cardNo, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("cardNo", cardNo);
        return requestBaseResponse(GET, USER, "getUserBalance", Tasks.GET_USER_BALANCE, UserBalance.class, params, heads, listener);
    }

    /**
     * 开启预约
     */
    public Tasks doReservation(String userCard, String plugNo, String chargerId, String token, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userCard", userCard);
        params.addBodyParameter("plugNo", plugNo);
        params.addBodyParameter("chargerId", chargerId);
        return requestBaseResponse(POST, RESERVATION, "reservation", Tasks.DO_RESERVATION, null, params, heads, listener);
    }

    /**
     * 取消预约
     */
    public Tasks doCancelReservation(String reservationSn, String token, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("reservationSn", reservationSn);
        return requestBaseResponse(PUT, RESERVATION, "cancelReservation", Tasks.DO_CANCEL_RESERVATION, null, params, heads, listener);
    }

    /**
     * 上传jpg格式的头像
     */
//    public Tasks uploadHead(File file, final ResponseListener<BaseResponse> listener) {
//        RequestParams params = getBaseMap("edituserphoto");
//        params.addBodyParameter("userid", "2");
//        if (file.exists())
//            params.addBodyParameter("photo", file);
//
//        return uploadFile(POST, "edituserphoto", Tasks.UPLOAD_FILE, null, params, listener);
//    }

    /**
     * 获取热题标签列表
     *
     * @param type
     * @param listener
     * @return Tasks
     */
    public Tasks getHotLabelList(int type, Tasks tasks, final ResponseListener<BaseResponse> listener) {
        RequestParams params = getBaseMap("get_label");
        params.addBodyParameter("type", type + "");
        return requestBaseResponseList(GET, "", "", tasks, LabelInfo.class, params, listener);
    }

    /**
     * 获取桩友圈列表数据
     *
     * @param page
     * @param listener
     * @return
     */
    public Tasks getChargerFriendsCircleList(String userId, String page, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(userId))
            params.addBodyParameter("userId", userId);
        if (!TextUtils.isEmpty(page))
            params.addBodyParameter("pageNo", page);
        return requestBaseResponseList(GET, SHARE, "shares", Tasks.CHARGER_FRIENDS_CIRCLE_LIST, ChargerFriends.class, params, listener);
    }

    /**
     * 发布动态
     */
    public Tasks publishDynamic(String token, String userId, String content, String shareAddress, String areaCode,
                                String smallImages, String middleImages, String bigImages, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);

        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("content", content);
        if (!TextUtils.isEmpty(shareAddress))
            params.addBodyParameter("shareAddress", shareAddress);
        if (!TextUtils.isEmpty(areaCode))
            params.addBodyParameter("areaCode", areaCode);
        params.addBodyParameter("smallImages", smallImages);
        params.addBodyParameter("middleImages", middleImages);
        params.addBodyParameter("bigImages", bigImages);

        return requestBaseResponse(POST, SHARE, "releaseShares", Tasks.PUBLISH_DYNAMIC, null, params, heads, listener);
    }


    /**
     * 获取标签分类后桩友圈数据
     *
     * @param page
     * @param listener
     * @return
     */
    public Tasks getHotTopicGroupList(String labelId, int page, final ResponseListener<BaseResponse> listener) {
        RequestParams params = getBaseMap("friendsCircleListByLabel");
        params.addBodyParameter("labelId", labelId);
        params.addBodyParameter("page", page + "");
        return requestBaseResponseList(GET, "", "", Tasks.CHARGER_FRIENDS_CIRCLE_LIST, DzFriends.class, params, listener);
    }

    /**
     * 获取服务Banner地址
     *
     * @param listener
     * @return
     */
    public Tasks getServiceBannerUrl(final ResponseListener<BaseResponse> listener) {
        return requestBaseResponseList(GET, PUBLIC, "bannerList", Tasks.SERVICE_BANNER_URL, BannerInfo.class, null, listener);
    }

    /**
     * 桩友圈点赞
     *
     * @param userId
     * @param shareId
     * @param listener
     * @return
     */
    public Tasks chargerFriendsCircleLike(String token, String userId, String shareId, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);

        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("shareId", shareId);
        return requestBaseResponse(POST, SHARE, "shareThumbUp", Tasks.CHARGER_FRIENDS_CIRCLE_LIKE, null, params, heads, listener);
    }

    /**
     * 单条桩友动态
     *
     * @param id
     * @param listener
     * @return
     */
    public Tasks getSingleDynamicInfo(int id, final ResponseListener<BaseResponse> listener) {
        RequestParams params = getBaseMap("getzhuangyouquaninfo");
        params.addBodyParameter("id", "" + id);
        return requestBaseResponse(GET, "", "", Tasks.SINGLE_DYNAMIC_INFO, DzFriends.class, params, listener);
    }


    /**
     * 删除评论
     *
     * @param id
     * @param userid
     * @param listener
     * @return
     */
    public Tasks chargerFriendsDeleteComment(String id, String userid, final ResponseListener<BaseResponse> listener) {
        RequestParams params = getBaseMap("delfenxiangpinglunbyid");
        params.addBodyParameter("pinglunid", "" + id);
        params.addBodyParameter("userid", "37776");
        return requestBaseResponse(GET, "", "", Tasks.DELETE_COMMENT, null, params, listener);
    }

    /**
     * 获取单条动态详情
     *
     * @param shareId
     * @param listener
     * @return
     */
    public Tasks getSingleDynamicDetail(String shareId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("shareId", shareId);
        return requestBaseResponse(GET, SHARE, "sharesDetail", Tasks.SINGLE_DYNAMIC_DETAIL, ChargerFriends.class, params, listener);
    }

    /**
     * 删除单条动态
     *
     * @param shareId
     * @param listener
     * @return
     */
    public Tasks deleteSingleDynamic(String shareId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("shareId", shareId);
        return requestBaseResponse(DELETE, SHARE, "deleteSharesByShareId", Tasks.DELETE_SINGLE_DYNAMIC, null, params, listener);
    }

    /**
     * 桩友圈评论
     *
     * @param userId
     * @param shareId
     * @param replyUserId
     * @param content
     * @param commentAddr
     * @param listener
     * @return
     */
    public Tasks chargerFriendsComment(String token, String userId, String shareId, String replyUserId, String content, String commentAddr, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);

        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("shareId", shareId);
        if (!TextUtils.isEmpty(replyUserId))
            params.addBodyParameter("replyUserId", replyUserId);
        params.addBodyParameter("content", content);
        if (!TextUtils.isEmpty(commentAddr))
            params.addBodyParameter("commentAddr", commentAddr);
        return requestBaseResponse(POST, SHARE, "sharesComment", Tasks.CHARGER_FRIENDS_CIRCLE_COMMENT, null, params, heads, listener);

    }

    /**
     * 获取资讯列表
     *
     * @return
     */
    public Tasks getChargerNewsList(int page, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageNo", page + "");
        return requestBaseResponseList(GET, INFO, "infoList", Tasks.CHARGER_NEWS_LIST, ChargerNews.class, params, listener);
    }


    /**
     * 获取资讯分类后数据
     *
     * @return
     */
    public Tasks getChargerNewsGroupList(String labelId, int page, final ResponseListener<BaseResponse> listener) {
        RequestParams params = getBaseMap("informationListByLabel");
        params.addBodyParameter("labelId", labelId);
        params.addBodyParameter("page", page + "");
        return requestBaseResponseList(GET, "", "", Tasks.CHARGER_NEWS_GROUP_LIST, ChargerNews.class, params, listener);
    }

    /**
     * 获取所有站点*
     */
    public Tasks requestChargerStationList(final ResponseListener<BaseResponse> listener) {

        return requestBaseResponseList(GET, STATION, "getAllStation", Tasks.CHARGER_STATION_LIST, ChargerStation.class, null, listener);
    }

    /**
     * 请求站点详情*
     */
    public Tasks requestChargerStationDetail(String userId, String stationId, String operMerchantIdfinal, ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("stationId", stationId);
        params.addBodyParameter("operMerchantIdfinal", operMerchantIdfinal);
        return requestBaseResponse(GET, STATION, "getStationDetail", Tasks.CHARGER_STATION_DETAIL, ChargerStationDetail.class, params, listener);
    }

    /**
     * 二维码开启充电
     *
     * @return
     */
    public Tasks openChargingByQrCode(String token, String qrCode, String userCard, String chargerPlugNo, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("qrCode", qrCode);
        params.addBodyParameter("userCard", userCard);
        params.addBodyParameter("chargerPlugNo", chargerPlugNo);
        return requestBaseResponse(POST, CHARGE, "scanCharge", Tasks.OPEN_CHARGE_BY_QRCODE, null, params, heads, listener);
    }

    /**
     * 开启充电
     *
     * @param token
     * @param chargerId
     * @param userCard
     * @param chargerPlugNo
     * @param listener
     * @return
     */
    public Tasks openCharging(String token, String chargerId, String userCard, String chargerPlugNo, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("chargerId", chargerId);
        params.addBodyParameter("userCard", userCard);
        params.addBodyParameter("chargerPlugNo", chargerPlugNo);//插头编号
        return requestBaseResponse(POST, CHARGE, "openCharge", Tasks.OPEN_CHARGE, null, params, heads, listener);
    }

    /**
     * 关闭充电
     *
     * @param token
     * @param chargeSn
     * @param listener
     * @return
     */
    public Tasks closeCharging(String chargeSn, String token, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("chargeSn", chargeSn);
        return requestBaseResponse(PUT, CHARGE, "closeCharge", Tasks.CLOSE_CHARGING, null, params, heads, listener);
    }

    /**
     * 查询枪头状态
     *
     * @param chargerId
     * @param plugNo
     * @param listener
     * @return
     */
    public Tasks getPlugStatus(String chargerId, String plugNo, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("chargerId", chargerId);
        params.addBodyParameter("plugNo", plugNo);
        return requestBaseResponse(GET, CHARGER, "plugStatus", Tasks.GET_PLUG_STATUS, PlugStatus.class, params, listener);
    }

    /**
     * 根据二维码查询桩详情
     */
    public Tasks getChargerDetailByDZqrCode(String qrCode, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("qrCode", qrCode);
        return requestBaseResponse(GET, CHARGER, "getChargerDetailByQrCode", Tasks.GET_CHARGER_DETAIL_BY_QR_CODE, Charger.class, params, listener);
    }

    /**
     * 获取站点评论列表
     *
     * @param pageNo
     * @param stationId
     * @param listener
     * @return
     */
    public Tasks getStationCommentList(String pageNo, String stationId, String userId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("stationId", stationId);
        params.addBodyParameter("userId", userId);
        return requestBaseResponseList(GET, STATION, "stationCommentList", Tasks.STATION_COMMENT_LIST, StationComment.class, params, listener);
    }

    /**
     * 电站评论点赞
     *
     * @param userId
     * @param commentId
     * @param listener
     * @return
     */
    public Tasks chargerCommentPraise(String userId, String commentId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("commentId", commentId);
        return requestBaseResponse(POST, STATION, "stationThumbUp", Tasks.CHAGER_COMMENT_PRAISE, null, params, listener);
    }

    /**
     * 站点评论
     *
     * @param userId
     * @param stationId
     * @param grade
     * @param address
     * @param content
     * @param listener
     * @return
     */
    public Tasks publishStationComment(String userId, String stationId, int grade, String address, String content, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("stationId", stationId);
        params.addBodyParameter("grade", grade + "");
        params.addBodyParameter("address", address);
        params.addBodyParameter("content", content);
        return requestBaseResponse(POST, STATION, "stationComment", Tasks.STATION_COMMENT, StationComment.class, params, listener);
    }

    /**
     * 站点评论回复
     *
     * @param userId
     * @param stationId
     * @param address
     * @param content
     * @param listener
     * @param commentId
     * @param replyUserId 回复的用户id
     * @return
     */
    public Tasks replyStationComment(String userId, String stationId, String address,
                                     String content, String commentId, String replyUserId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("stationId", stationId);
        params.addBodyParameter("address", address);
        params.addBodyParameter("content", content);
        params.addBodyParameter("commentId", commentId);
        params.addBodyParameter("replyUserId", replyUserId);
        return requestBaseResponse(POST, STATION, "stationCommentReply", Tasks.STATION_REPLY_COMMENT, StationComment.class, params, listener);
    }

    /**
     * 查询账单列表
     *
     * @param token
     * @param userId
     * @return
     */
    public Tasks requestFundList(String token, String userId, String status, int pageNo, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("pageNo", pageNo + "");
        params.addBodyParameter("status", status);
        return requestBaseResponseList(GET, FUND, "getFundList", Tasks.FUND_LIST, OrderBase.class, params, heads, listener);
    }

    /**
     * 获取账单详情
     *
     * @return
     */
    public Tasks getFundDetail(String token, String orderNo, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderNo", orderNo);
        return requestBaseResponse(GET, FUND, "fundDetail", Tasks.FUND_DETAIL, OrderDetail.class, params, heads, listener);
    }

    /**
     * 获取商户列表
     *
     * @return
     */
    public Tasks getStoreList(int page, String type, String lat, String lng, final ResponseListener<BaseResponse> listener) {
        RequestParams params = getBaseMap("companylist");
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("type", type);
        params.addBodyParameter("lat", lat);
        params.addBodyParameter("lng", lng);
        return requestBaseResponseList(GET, "", "", Tasks.STORE_LIST, Store.class, params, listener);
    }

    /**
     * 请求空闲桩数量
     *
     * @param stationId
     * @return
     */
    public Tasks requestFreeChargerNum(String stationId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("stationId", stationId);
        return requestBaseResponse(GET, CHARGER, "getFreeChargerByStationId", Tasks.FREE_CHARGER_NUM, FreeChargerNum.class,
                params, listener);
    }


    /**
     * 请求桩位列表
     *
     * @param stationId
     * @return
     */
    public Tasks requestChrgerList(String stationId, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("stationId", stationId);
        return requestBaseResponseList(GET, CHARGER, "getChargerListByStationId", Tasks.CHARGER_LIST, Charger.class,
                params, listener);
    }

    /**
     * 获取正在进行的业务操作（预约，充电）
     *
     * @param userCard
     * @param token
     * @param listener
     * @return
     */
    public Tasks getOngoingOperation(String userCard, String token, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userCard", userCard);
        return requestBaseResponse(GET, CHARGE, "getOngoingOperation", Tasks.ONGOING_OPERATION, OngoingOperation.class, params, heads, listener);

    }

    /**
     * 支付订单
     *
     * @param userId
     * @param token
     * @param orderNo
     * @param listener
     * @return
     */
    public Tasks payOrder(String userId, String orderNo, String cardNo, String cardType, String token, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        params.addBodyParameter("orderNo", orderNo);
        params.addBodyParameter("cardNo", cardNo);
        params.addBodyParameter("cardType", cardType);
        return requestBaseResponse(PUT, FUND, "balanceAccounts", Tasks.PAY_ORDER, null, params, heads, listener);
    }

    /**
     * 查询未支付订单
     *
     * @param userId
     * @param token
     * @param listener
     * @return
     */
    public Tasks getNonpaymentOrder(String userId, String token, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId);
        return requestBaseResponseList(GET, FUND, "getUserNeedPayOrders", Tasks.GET_NON_PAYMENT_ORDER, String.class, params, heads, listener);
    }

    /**
     * 根据预约流水号获取预约详细信息
     *
     * @return
     */
    public Tasks getReservationDetailByReservationSn(String token, String reservationSn, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("reservationSn", reservationSn);
        return requestBaseResponse(GET, RESERVATION, "reservationDetail", Tasks.GET_RESERVATION_DETAIL_BY_RESERVATIONSN, BookingDetail.class, params, heads, listener);
    }

    /**
     * 根据充电流水号获取充电详细信息
     *
     * @return
     */
    public Tasks getChargeDetailByChargeSn(String token, String chargeSn, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        RequestParams params = new RequestParams();
        params.addBodyParameter("chargeSn", chargeSn);
        return requestBaseResponse(GET, CHARGE, "chargeDetail", Tasks.GET_CHARGE_DETAIL_BY_CHARGESN, ChargeDetail.class, params, heads, listener);
    }

    public Tasks getCarLocation(String vin, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("vin", vin);
        return requestBaseResponse(GET, CAR, "getCarLocation", Tasks.CAR_LOCATION, CarLocationInfo.class, params, listener);
    }

    /**
     * 用户车列表
     */
    public Tasks userCarList(final ResponseListener<BaseResponse> listener) {
        return requestBaseResponseList(GET, CAR, "carList", Tasks.CAR_LIST, CarBaseInfo.class, null, listener);
    }

    /**
     * 寻车
     */
    public Tasks findCar(String vin, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("vin", vin);
        return requestBaseResponse(POST, CAR, "findCar", Tasks.FIND_CAR, null, params, listener);
    }

    /**
     * 车锁控制
     */
    public Tasks lockCar(String action, String vin, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        params.addBodyParameter("vin", vin);
        return requestBaseResponse(POST, CAR, "lockTheCar", Tasks.LOCK_CAR, null, params, listener);
    }

    /**
     * 得到七牛的key
     */
    public Tasks getQiNiuToken(String token, final ResponseListener<BaseResponse> listener) {
        Map<String, String> heads = new HashMap<>();
        heads.put("Authentication", token);
        return requestBaseResponse(GET, QINIU, "getQiniuyunUpToken", Tasks.QINIU_KEY, String.class, null, heads, listener);
    }


    /**
     * 获取最新版本号
     *
     * @param osType
     * @param listener
     * @return
     */
    public Tasks getVersion(String osType, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("osType", osType);
        return requestBaseResponse(GET, PUBLIC, "checkVersionUpdate", Tasks.VERSION, Version.class, params, listener);
    }

    /**
     * 获取第三方绑定列表
     *
     * @param bindType
     * @param listener
     * @param merchantCode
     * @param phone
     * @param source
     * @return
     */
    public Tasks getForeignBindList(String source, String merchantCode, String bindType, String phone
            , final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("source", source);
        params.addBodyParameter("merchantCode", merchantCode);
        params.addBodyParameter("bindType", bindType);
        params.addBodyParameter("phone", phone);
        return requestBaseResponseList(GET, USER, "bindShipList", Tasks.FOREIGN_BIND_LIST, ForeignAuth.class,
                params, listener);
    }

    /**
     * 获取桩的费用信息
     *
     * @return
     */
    public Tasks getRateByChargerId(String chargerID, final ResponseListener<BaseResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("chargerId", chargerID);
        return requestBaseResponseList(GET, CHARGER, "getRate", Tasks.CHARGER_RATE, Rate.class, params, listener);
    }

    /**
     * 下载apk文件
     */
    public Tasks downloadAPK(String url, String fileSavePath, final ResponseListener<BaseResponse> listener) {
//        url = "http://openbox.mobilem.360.cn/index/d/sid/2490097";//测试
        return downloadFile(GET, Tasks.DOWNLOAD_FILE, url, fileSavePath, false, listener);
    }
}
