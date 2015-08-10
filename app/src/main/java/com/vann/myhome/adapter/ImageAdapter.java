package com.vann.myhome.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.vann.myhome.R;
import com.vann.myhome.model.AreaModel;
import com.vann.myhome.util.ImageUtil;
import com.vann.myhome.widget.GalleryFlow;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class ImageAdapter extends BaseAdapter {

    private int showWidht =250;

    private int showHeight =350;

    public int getShowWidht() {
        return showWidht;
    }

    public void setShowWidht(int showWidht) {
        this.showWidht = showWidht;
    }

    public int getShowHeight() {
        return showHeight;
    }

    public void setShowHeight(int showHeight) {
        this.showHeight = showHeight;
    }

    int mGalleryItemBackground;
    private Context mContext;
    private Object[] datas;
    private ImageView[] mImages;

    public ImageAdapter(Context c, Object[] _datas) {
        mContext = c;
        datas = _datas;
        mImages = new ImageView[_datas.length];
    }

    public boolean createReflectedImages() {
        return true;
    }



    private Resources getResources() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getCount() {
        return datas.length;//mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image = new ImageView(mContext);
        Object data = datas[position];
        int imageId =0;
        String showName ="";
        String id="";
        String areaImg="";
        Bitmap photo =null;
        if(data instanceof AreaModel){
            showName =  ((AreaModel) data).getName();
            id =  ((AreaModel) data).getAreaId();
        }
        if(photo==null){
            photo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.room0);//mImageIds[position]);
        }
        int width = photo.getWidth(), hight = photo.getHeight();
        Bitmap icon = ImageUtil.drawText(mContext, photo, 30.0f, width / 2, hight - 35, showName);

        image.setImageBitmap(icon);
        image.setLayoutParams(new GalleryFlow.LayoutParams(this.getShowWidht(), this.getShowHeight()));
        image.setScaleType(ImageView.ScaleType.CENTER);
        image.setTag(data);
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        drawable.setAntiAlias(true);
        return image;//mImages[position];
    }

    public float getScale(boolean focused, int offset) {
        return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
    }
}
