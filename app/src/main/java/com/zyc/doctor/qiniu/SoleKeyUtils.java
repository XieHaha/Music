package com.zyc.doctor.qiniu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by luozi on 2016/5/31.
 */
public class SoleKeyUtils {
    private Random random = new Random();
    private final String HEADER = "APP";

    /**
     * 得到图片名称
     *
     * @return img name
     */
    public String getImgKey(String userid, ImgType imgType,
                            ImgSize imgSize) {
        return HEADER + imgType.getValue() + imgSize.getValue() + getShortTime()
                + userid + getRandomKey(3);
    }

    private String getShortTime() {
        String pattern = "yyMMddHHmmss";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        long time = System.currentTimeMillis();
        return df.format(new Date(time));
    }

    private String getRandomKey(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public enum ImgType {
        HEAD("H"), STATION("S"), QUAN("Q");
        private final String str;

        ImgType(String str) {
            this.str = str;
        }

        public String getValue() {
            return str;
        }

        @Override
        public String toString() {
            return str;
        }

    }

    public enum ImgSize {
        BIG("B"), MIDDLE("M"), SMALL("S");
        private final String str;

        ImgSize(String str) {
            this.str = str;
        }

        public String getValue() {
            return str;
        }

        @Override
        public String toString() {
            return str;
        }

    }
}
