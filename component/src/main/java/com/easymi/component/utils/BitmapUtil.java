package com.easymi.component.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by xyin on 2017/3/4.
 * 处理图像相关方法.
 */

public class BitmapUtil {

    private static final String TAG = "BitmapUtil";

    /**
     * bitmap转化成字节数组.
     *
     * @param bitmap      需要转化的
     * @param needRecycle 是否需要回收原bitmap
     * @return 转化后的字节数组
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 存储bitmap到文件中.
     *
     * @param context context
     * @param path    需要存储的文件路径
     * @param picName 文件名
     * @param bm      需要存储的bitmap对象
     */
    public static void saveBitmap(Context context, String path, String picName, Bitmap bm) {
        if (context == null || TextUtils.isEmpty(path) || TextUtils.isEmpty(picName) || bm == null) {
            Log.e(TAG, "some parameter invalid");
            return;
        }

        if (!TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "没有存储空间");
            return;
        }

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "create dir fail");
                return;
            }
        }

        File f = new File(dir, picName);
        if (f.exists()) {
            if (!f.delete()) {
                Log.e(TAG, "delete fail");
                return;
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.d(TAG, "save bitmap succeed");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 生成二维码.
     *
     * @param string          需要生成的内容
     * @param bitmap          bitmap
     * @param IMAGE_HALFWIDTH IMAGE_HALFWIDTH
     * @return 生成的内容
     * @throws WriterException 异常
     */
    public static Bitmap createCode(String string, Bitmap bitmap, int IMAGE_HALFWIDTH)
            throws WriterException {
        BarcodeFormat format = BarcodeFormat.QR_CODE;
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / bitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH
                / bitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
        BitMatrix matrix = writer.encode(string, format, 400, 400, hst);//生成二维码矩阵信息
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
                    //记录图片每个像素信息
                    pixels[y * width + x] = bitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
        }
        Bitmap bitmap2 = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_4444);
        // 通过像素数组生成bitmap
        bitmap2.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap2;
    }


}
