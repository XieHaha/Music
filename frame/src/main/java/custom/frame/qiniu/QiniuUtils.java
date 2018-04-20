package custom.frame.qiniu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import custom.frame.bean.NormImage;
import custom.frame.bean.NormImgSize;
import custom.frame.utils.DirHelper;


/**
 * Created by luozi on 2016/6/6.
 */
public class QiniuUtils {

    /**
     * 初始化桩友圈发布信息不同大小的bitmap
     */
    public static NormImage initQuanBitmaps(Uri path, Activity context) {
        Bitmap res = null;
        try {
            res = getBitmapFormUri(context, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        NormImage localImage = new NormImage();
        SoleKeyUtils soleKeyUtils = new SoleKeyUtils();

        Bitmap bigBitmap = BitmapUtil.scaleByNormSize(NormImgSize.QUAN_BIG, res);
        Bitmap middleBitmap = BitmapUtil.scaleByNormSize(NormImgSize.QUAN_MIDDLE, res);
        Bitmap smallBitmap = BitmapUtil.scaleByNormSize(NormImgSize.QUAN_SMALL, res);

        //bitmap 赋值
        localImage.setBigBitmap(bigBitmap);
        localImage.setMiddleBitmap(middleBitmap);
        localImage.setSmallBitmap(smallBitmap);

        String nameBig = soleKeyUtils.getImgKey("YHT", SoleKeyUtils.ImgType.QUAN,
                SoleKeyUtils.ImgSize.BIG);
        String nameMiddle = soleKeyUtils.getImgKey("YHT", SoleKeyUtils.ImgType.QUAN,
                SoleKeyUtils.ImgSize.MIDDLE);
        String nameSmall = soleKeyUtils.getImgKey("YHT", SoleKeyUtils.ImgType.QUAN,
                SoleKeyUtils.ImgSize.SMALL);
        localImage.setBigImageName(nameBig);
        localImage.setMiddleImageName(nameMiddle);
        localImage.setSmallImageName(nameSmall);

        String bigImgPath = DirHelper.getPathImage() + "/" + nameBig;
        String middleImgPath = DirHelper.getPathImage() + "/" + nameMiddle;
        String smallImgPath = DirHelper.getPathImage() + "/" + nameSmall;

        localImage.setBigImageUrl(bigImgPath);
        localImage.setMiddleImageUrl(middleImgPath);
        localImage.setSmallImageUrl(smallImgPath);

        File bigFile = new File(bigImgPath);
        File middleFile = new File(middleImgPath);
        File smallFile = new File(smallImgPath);

        try {
            bigFile.createNewFile();
            middleFile.createNewFile();
            smallFile.createNewFile();

            FileOutputStream fOutBig = new FileOutputStream(bigFile);
            FileOutputStream fOutMiddle = new FileOutputStream(middleFile);
            FileOutputStream fOutSmall = new FileOutputStream(smallFile);

            bigBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOutBig);
            middleBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOutMiddle);
            smallBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOutSmall);

            fOutBig.flush();
            fOutBig.close();
            fOutMiddle.flush();
            fOutMiddle.close();
            fOutSmall.flush();
            fOutSmall.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return localImage;
    }

    /**
     * 初始化用户头像不同大小的bitmap
     */
    public static NormImage initHeadBitmaps(String path, Context context) {
        Bitmap res = BitmapFactory.decodeFile(path);
        NormImage localImage = new NormImage();
        SoleKeyUtils soleKeyUtils = new SoleKeyUtils();

        Bitmap bigBitmap = BitmapUtil.scaleByNormSize(NormImgSize.HEAD_BIG, res);
        Bitmap middleBitmap = BitmapUtil.scaleByNormSize(NormImgSize.HEAD_MIDDLE, res);
        Bitmap smallBitmap = BitmapUtil.scaleByNormSize(NormImgSize.HEAD_SMALL, res);

        //bitmap 赋值
        localImage.setBigBitmap(bigBitmap);
        localImage.setMiddleBitmap(middleBitmap);
        localImage.setSmallBitmap(smallBitmap);

        String nameBig = soleKeyUtils.getImgKey("YHT", SoleKeyUtils.ImgType.HEAD,
                SoleKeyUtils.ImgSize.BIG);
        String nameMiddle = soleKeyUtils.getImgKey("YHT", SoleKeyUtils.ImgType.HEAD,
                SoleKeyUtils.ImgSize.MIDDLE);
        String nameSmall = soleKeyUtils.getImgKey("YHT", SoleKeyUtils.ImgType.HEAD,
                SoleKeyUtils.ImgSize.SMALL);
        localImage.setBigImageName(nameBig);
        localImage.setMiddleImageName(nameMiddle);
        localImage.setSmallImageName(nameSmall);

        String bigImgPath = DirHelper.getPathImage() + "/" + nameBig;
        String middleImgPath = DirHelper.getPathImage() + "/" + nameMiddle;
        String smallImgPath = DirHelper.getPathImage() + "/" + nameSmall;

        localImage.setBigImageUrl(bigImgPath);
        localImage.setMiddleImageUrl(middleImgPath);
        localImage.setSmallImageUrl(smallImgPath);

        File bigFile = new File(bigImgPath);
        File middleFile = new File(middleImgPath);
        File smallFile = new File(smallImgPath);

        try {
            bigFile.createNewFile();
            middleFile.createNewFile();
            smallFile.createNewFile();

            FileOutputStream fOutBig = new FileOutputStream(bigFile);
            FileOutputStream fOutMiddle = new FileOutputStream(middleFile);
            FileOutputStream fOutSmall = new FileOutputStream(smallFile);

            bigBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOutBig);
            middleBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOutMiddle);
            smallBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOutSmall);

            fOutBig.flush();
            fOutBig.close();
            fOutMiddle.flush();
            fOutMiddle.close();
            fOutSmall.flush();
            fOutSmall.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return localImage;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1)) {
            return null;
        }
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}