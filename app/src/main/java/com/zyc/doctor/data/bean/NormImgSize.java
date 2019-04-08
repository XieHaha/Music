package com.zyc.doctor.data.bean;

import android.graphics.Bitmap;

/**
 * @author dundun
 */
public enum NormImgSize {
    /**
     * 头像大图
     */
    HEAD_BIG {
        @Override
        public float getNormSize() {
            return NORM_HEAD_BIG_SIZE;
        }

        @Override
        public String toString() {
            return "head_big_img";
        }
    },
    /**
     * 头像中图
     */
    HEAD_MIDDLE {
        @Override
        public float getNormSize() {
            return NORM_HEAD_BIG_SIZE / 2f;
        }

        @Override
        public String toString() {
            return "head_middle_img";
        }
    },
    /**
     * 头像小图
     */
    HEAD_SMALL {
        @Override
        public float getNormSize() {
            return NORM_HEAD_BIG_SIZE / 4f;
        }

        @Override
        public String toString() {
            return "head_small_img";
        }
    },
    /**
     * 纵向站点大图
     */
    STATION_BIG {
        @Override
        public float getNormSize() {
            return NORM_STATION_BIG_SIZE;
        }

        @Override
        public String toString() {
            return "station_big_img";
        }
    },
    /**
     * 纵向站点中图
     */
    STATION_MIDDLE {
        @Override
        public float getNormSize() {
            return NORM_STATION_BIG_SIZE / 2f;
        }

        @Override
        public String toString() {
            return "station_middle_img";
        }
    },
    /**
     * 纵向站点小图
     */
    STATION_SMALL {
        @Override
        public float getNormSize() {
            return NORM_STATION_BIG_SIZE / 4f;
        }

        @Override
        public String toString() {
            return "station_small_img";
        }
    },
    /**
     * 纵向站点大图
     */
    QUAN_BIG {
        @Override
        public float getNormSize() {
            return NORM_STATION_BIG_SIZE;
        }

        @Override
        public String toString() {
            return "quan_big_img";
        }
    },
    /**
     * 纵向站点中图
     */
    QUAN_MIDDLE {
        @Override
        public float getNormSize() {
            return NORM_STATION_BIG_SIZE / 2f;
        }

        @Override
        public String toString() {
            return "quan_middle_img";
        }
    },
    /**
     * 纵向站点小图
     */
    QUAN_SMALL {
        @Override
        public float getNormSize() {
            return NORM_STATION_BIG_SIZE / 4f;
        }

        @Override
        public String toString() {
            return "quan_small_img";
        }
    };

    /**
     * 得到标准大小
     */
    public float getNormSize() {
        return 1;
    }

    /**
     * 标准头像大图高宽度
     */
    final int NORM_HEAD_BIG_SIZE = 512;
    /**
     * 标准站点图片大小
     */
    final float NORM_STATION_BIG_SIZE = 1080f;

    NormImgSize() {
    }

    /**
     * 计算图片的缩放值
     *
     * @param bitmap
     * @return
     */
    public float getScaleSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 1f;
        }
        float min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        //是否图片最小值都大于标准值的大小，如果有则
        float scaleSize = min > getNormSize() ? getNormSize() / min : 1f;
        return scaleSize;
    }

    @Override
    public String toString() {
        return "NormImgSize{" + "NORM_HEAD_BIG_SIZE=" + NORM_HEAD_BIG_SIZE + ", NORM_STATION_BIG_SIZE=" +
               NORM_STATION_BIG_SIZE + '}';
    }
}
