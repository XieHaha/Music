package custom.frame.utils;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import custom.frame.R;

public final class GlideHelper
{
    /**
     * 医生
     */
    public static RequestOptions options;
    /**
     * 患者
     */
    public static RequestOptions optionsP;
    /**
     * 图片
     */
    public static RequestOptions optionsPic;
    /**
     * 图片
     */
    public static RequestOptions optionsHospitalPic;

    public static RequestOptions getOptions()
    {
        if (options == null)
        {
            options = new RequestOptions().centerCrop()
                                          .placeholder(R.mipmap.icon_default_imgs)
                                          .error(R.mipmap.icon_default_imgs)
                                          .priority(Priority.NORMAL);
        }
        return options;
    }

    public static RequestOptions getOptionsP()
    {
        if (optionsP == null)
        {
            optionsP = new RequestOptions().centerCrop()
                                           .placeholder(R.mipmap.icon_patient_default_imgs)
                                           .error(R.mipmap.icon_patient_default_imgs)
                                           .priority(Priority.NORMAL);
        }
        return optionsP;
    }

    public static RequestOptions getOptionsPic()
    {
        if (optionsPic == null)
        {
            optionsPic = new RequestOptions().placeholder(R.mipmap.icon_loading_img)
                                             .error(R.mipmap.icon_load_faild_img)
                                             .priority(Priority.NORMAL);
        }
        return optionsPic;
    }

    public static RequestOptions getOptionsHospitalPic()
    {
        if (optionsHospitalPic == null)
        {
            optionsHospitalPic = new RequestOptions().centerCrop()
                                                     .placeholder(R.mipmap.icon_hospital_default)
                                                     .error(R.mipmap.icon_hospital_default)
                                                     .priority(Priority.NORMAL);
        }
        return optionsHospitalPic;
    }
}
