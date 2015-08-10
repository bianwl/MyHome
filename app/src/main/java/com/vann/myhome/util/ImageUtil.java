package com.vann.myhome.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class ImageUtil {
    public static Bitmap ImgIcon(Context context, String code) {
        int resID = context.getResources().getIdentifier(code, "drawable",
                context.getPackageName());
        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                resID);
        return photo;
    }

    public static Bitmap drawText(Context context, int imageId, float fontSize,
                                  int x, int y, String textName) {
        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                imageId);// mImageIds[position]);
        int width = photo.getWidth(), hight = photo.getHeight();
        return drawText(context, photo, fontSize, width / 2 - x, hight - y,
                textName);
    }

    public static Bitmap drawText(Context context, Bitmap photo,
                                  float fontSize, int x, int y, String textName) {
        int width = photo.getWidth(), hight = photo.getHeight();
        // System.out.println("宽" + width + "高" + hight);
        Bitmap icon = Bitmap
                .createBitmap(width, hight, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
        Canvas canvas = new Canvas(icon);// 初始化画布 绘制的图像到icon上

        Paint photoPaint = new Paint(); // 建立画笔
        photoPaint.setDither(true); // 获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);// 过滤一些

        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());// 创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);// 创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, src, dst, photoPaint);// 将photo 缩放或则扩大到
        // dst使用的填充区photoPaint

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
        textPaint.setTextSize(fontSize);// 字体大小
        // textPaint.setTypeface(Typeface.MONOSPACE);//采用默认的宽度
        textPaint.setColor(Color.WHITE);// 采用的颜色
        textPaint.setTextAlign(Paint.Align.CENTER);
        Typeface font = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);
        textPaint.setTypeface(font);
        textPaint.setShadowLayer(3f, 1, 1,
                context.getResources()
                        .getColor(android.R.color.background_dark));// 影音的设置
        canvas.drawText(textName, x, y, textPaint);// 绘制上去
        // 字，开始未知x,y采用那只笔绘制
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return icon;
    }
}
