/*
 * Copyright (c) 2019-2019 cn.csu.software. All rights reserved.
 */

package cn.csu.software.wechat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片操作工具类
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class BitmapUtil {
    private static final String TAG = BitmapUtil.class.getSimpleName();

    private BitmapUtil() {
    }

    /**
     * 保存图片到指定路径
     *
     * @param imagePath 图片路径
     * @param bitmap 图片
     */
    public static void saveImg(String imagePath, Bitmap bitmap) throws IOException {
        File file = new File(imagePath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    /**
     * 缩放图片比例
     *
     * @param bitmap 图片
     * @param newWidth 图片缩放后的宽度
     * @param newHeight 图片缩放后的高度
     * @return Bitmap 缩放后的图片
     */
    public static Bitmap zoomImg(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 缩放图片比例
     *
     * @param bitmap 图片
     * @return Bitmap 缩放后的图片
     */
    public static Bitmap zoomImg(Bitmap bitmap) {
        int maxWidth = 100;
        int maxHeight = 500;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= maxWidth && height <= maxHeight) {
            return compressImage(bitmap);
        }
        float scaleWidth = (float) maxWidth / width;
        float scaleHeight = (float) maxHeight / height;
        float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return compressImage(Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true));
    }

    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            LogUtil.i(TAG, "compress");
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }
}