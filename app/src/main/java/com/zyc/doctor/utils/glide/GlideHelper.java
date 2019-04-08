package com.zyc.doctor.utils.glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.zyc.doctor.R;

/**
 * @author dundun
 */
public final class GlideHelper {
    /**
     * 医生
     */
    public static final RequestOptions OPTIONS = new RequestOptions();
    /**
     * 患者
     */
    public static final RequestOptions OPTIONS_P = new RequestOptions();
    /**
     * 图片
     */
    public static final RequestOptions OPTIONS_PIC = new RequestOptions();
    /**
     * 图片
     */
    public static final RequestOptions OPTIONS_HOSPITAL_PIC = new RequestOptions();

    public static RequestOptions getOptions() {
        return OPTIONS.centerCrop()
                      .placeholder(R.mipmap.icon_default_img)
                      .error(R.mipmap.icon_default_img)
                      .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsP() {
        return OPTIONS_P.centerCrop()
                        .placeholder(R.mipmap.icon_patient_default_img)
                        .error(R.mipmap.icon_patient_default_img)
                        .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsPic() {
        return OPTIONS_PIC.placeholder(R.mipmap.icon_loading_img)
                          .error(R.mipmap.icon_load_faild_img)
                          .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsHospitalPic() {
        return OPTIONS_HOSPITAL_PIC.centerCrop()
                                   .placeholder(R.mipmap.icon_hospital_default)
                                   .error(R.mipmap.icon_hospital_default)
                                   .priority(Priority.NORMAL);
    }
}
