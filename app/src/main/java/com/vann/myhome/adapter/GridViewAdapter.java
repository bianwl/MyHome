package com.vann.myhome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**网格控件适配器
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class GridViewAdapter extends BaseAdapter {
    private Integer[] gvDatas;
    private Context mContext;

    public GridViewAdapter(Context context, Integer[] datas) {
        this.mContext = context;
        this.gvDatas = datas;
    }

    @Override
    public int getCount() {
        return gvDatas.length;
    }

    @Override
    public Object getItem(int position) {
        return gvDatas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgView;
        if (convertView == null) {
            imgView = new ImageView(mContext);
            imgView.setLayoutParams(new GridView.LayoutParams(260, 325));
            imgView.setAdjustViewBounds(false);
            imgView.setPadding(20, 15, 20, 10);
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imgView = (ImageView) convertView;
        }
        imgView.setImageResource(gvDatas[position]);
        return imgView;
    }
}
