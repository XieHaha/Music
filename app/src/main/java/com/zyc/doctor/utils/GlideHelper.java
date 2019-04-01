package com.zyc.doctor.utils;

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
    public static final RequestOptions options = new RequestOptions();
    /**
     * 患者
     */
    public static final RequestOptions optionsP = new RequestOptions();
    /**
     * 图片
     */
    public static final RequestOptions optionsPic = new RequestOptions();
    /**
     * 图片
     */
    public static final RequestOptions optionsHospitalPic = new RequestOptions();

    public static RequestOptions getOptions() {
        return options.centerCrop()
                      .placeholder(R.mipmap.icon_default_imgs)
                      .error(R.mipmap.icon_default_imgs)
                      .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsP() {
        return optionsP.centerCrop()
                       .placeholder(R.mipmap.icon_patient_default_imgs)
                       .error(R.mipmap.icon_patient_default_imgs)
                       .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsPic() {
        return optionsPic.placeholder(R.mipmap.icon_loading_img)
                         .error(R.mipmap.icon_load_faild_img)
                         .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsHospitalPic() {
        return optionsHospitalPic.centerCrop()
                                 .placeholder(R.mipmap.icon_hospital_default)
                                 .error(R.mipmap.icon_hospital_default)
                                 .priority(Priority.NORMAL);
    }
}
