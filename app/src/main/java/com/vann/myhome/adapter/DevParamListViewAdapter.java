package com.vann.myhome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vann.myhome.R;
import com.vann.myhome.model.DeviceModel;

import java.util.ArrayList;
import java.util.List;

/**设备列表适配器
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class DevParamListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<DeviceModel> devices = new ArrayList<DeviceModel>();

    public DevParamListViewAdapter(Context context, List<DeviceModel> devList) {
        this.mContext = context;
        this.devices = devList;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.device_list_item, null);
            holder.imv_dev = (ImageView) convertView.findViewById(R.id.imv_device);
            holder.otherName = (TextView) convertView.findViewById(R.id.tv_otherName);
            holder.tv_devName = (TextView) convertView.findViewById(R.id.tv_devName);
            holder.imb_devEdit = (ImageButton) convertView.findViewById(R.id.imb_devEdit);
            holder.imb_devDel = (ImageButton) convertView.findViewById(R.id.imb_devDel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DeviceModel dev = devices.get(position);
        if (dev.getName().startsWith("k")) {
            holder.imv_dev.setImageResource(R.drawable.k_b);
        } else if (dev.getName().startsWith("t")) {
            holder.imv_dev.setImageResource(R.drawable.t_b);
        } else if (dev.getName().startsWith("c")) {
            holder.imv_dev.setImageResource(R.drawable.c_b);
        }
        holder.tv_devName.setText(dev.getName());
        holder.otherName.setText(dev.getOtherName());

        return convertView;
    }

    class ViewHolder {
        ImageView imv_dev;
        TextView tv_devName;
        TextView otherName;
        ImageButton imb_devEdit;
        ImageButton imb_devDel;

    }
}
