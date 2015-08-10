package com.vann.myhome.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vann.myhome.R;
import com.vann.myhome.model.DeviceModel;
import com.vann.myhome.util.SocketUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**区域界面设备列表适配器
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class AreaDevAdapter extends BaseAdapter {
    private List<DeviceModel> devices = new ArrayList<DeviceModel>();

    private Context mContext;

    private int width = 72;
    private int height = 72;

    public AreaDevAdapter(Context context, List<DeviceModel> list) {
        mContext = context;
        devices = list;
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
        convertView = LayoutInflater.from(mContext).inflate(
                R.layout.area_dev_list_item, null);
        ImageButton devimb = (ImageButton) convertView.findViewById(R.id.imb);
        TextView devName = (TextView) convertView.findViewById(R.id.tv_devName);
        LinearLayout layout = (LinearLayout) convertView
                .findViewById(R.id.lay_dev);
        DeviceModel device = devices.get(position);
        devName.setText(device.getName());
        String type = device.getType();
        if (TextUtils.equals(type, "k")) {
            createKView(layout, device, devimb);
        } else if (TextUtils.equals(type, "t")) {
            createTView(layout, device, devimb);
        } else if (TextUtils.equals(type, "c")) {
            createCView(layout, device, devimb);
        }
        return convertView;
    }

    /**
     * 创建窗帘控件
     *
     * @param layout
     * @param dev
     */
    private void createCView(LinearLayout layout, final DeviceModel dev,
                             final ImageButton devimb) {
        final Button btnOn = new Button(mContext);
        final Button btnOff = new Button(mContext);
        final Button btnStop = new Button(mContext);
        btnOn.setWidth(55);
        btnOn.setHeight(55);
        btnOn.setTextColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                height);
        layout.addView(btnOn, params);
        if (TextUtils.equals("000", dev.getState())) {
            btnOff.setBackgroundResource(R.drawable.off_b);
            btnOn.setBackgroundResource(R.drawable.on);
            btnStop.setBackgroundResource(R.drawable.stop);
            devimb.setBackgroundResource(R.drawable.c);
        } else if (TextUtils.equals("001", dev.getState())) {
            btnOff.setBackgroundResource(R.drawable.off);
            btnOn.setBackgroundResource(R.drawable.on_b);
            btnStop.setBackgroundResource(R.drawable.stop);
            devimb.setBackgroundResource(R.drawable.c_b);
        } else {
            btnOff.setBackgroundResource(R.drawable.off);
            btnOn.setBackgroundResource(R.drawable.on);
            btnStop.setBackgroundResource(R.drawable.stop_b);
            devimb.setBackgroundResource(R.drawable.c_t);
        }

        btnOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOff.setBackgroundResource(R.drawable.off);
                btnOn.setBackgroundResource(R.drawable.on_b);
                btnStop.setBackgroundResource(R.drawable.stop);
                devimb.setBackgroundResource(R.drawable.c_b);
                SocketUtil.send(dev, "001");
            }
        });
        btnOff.setWidth(55);
        btnOff.setHeight(55);
        btnOff.setTextColor(Color.TRANSPARENT);
        params.leftMargin = 45;
        layout.addView(btnOff, params);
        btnOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOff.setBackgroundResource(R.drawable.off_b);
                btnOn.setBackgroundResource(R.drawable.on);
                btnStop.setBackgroundResource(R.drawable.stop);
                devimb.setBackgroundResource(R.drawable.c_c);
                SocketUtil.send(dev, "000");
            }
        });
        btnStop.setWidth(55);
        btnStop.setHeight(55);
        btnStop.setTextColor(Color.TRANSPARENT);
        layout.addView(btnStop, params);
        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOff.setBackgroundResource(R.drawable.off);
                btnOn.setBackgroundResource(R.drawable.on);
                btnStop.setBackgroundResource(R.drawable.stop_b);
                devimb.setBackgroundResource(R.drawable.c_t);
                SocketUtil.send(dev, "002");
            }
        });

    }

    /**
     * 创建亮度调整
     *
     * @param layout
     * @param dev
     */
    private void createTView(LinearLayout layout, final DeviceModel dev,
                             final ImageButton devimb) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, height);
        params.weight = 1;
        SeekBar bar = new SeekBar(mContext);
        bar.setMax(100);
        bar.setMinimumHeight(24);
        bar.setSecondaryProgress(5);
        bar.setThumbOffset(1);
        bar.setProgress(0);
        layout.addView(bar, params);
        devimb.setBackgroundResource(R.drawable.t);

        final TextView tvSeek = new TextView(mContext);
        tvSeek.setTextSize(20);
        tvSeek.setTextColor(Color.BLACK);
        tvSeek.setGravity(Gravity.CENTER_VERTICAL);
        tvSeek.setText("0");
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                height);
        layout.addView(tvSeek, params);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int val = seekBar.getProgress();
                dev.setState(String.valueOf(val));
                tvSeek.setText(String.valueOf(val));
                if (val == 0) {
                    devimb.setBackgroundResource(R.drawable.t);
                } else {
                    devimb.setBackgroundResource(R.drawable.t_b);
                }
                DecimalFormat df = new DecimalFormat("000");
                String value = df.format(val);
                SocketUtil.send(dev, value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

            }
        });

    }

    /**
     * 创建开关控件
     *
     * @param layout
     * @param dev
     */
    private void createKView(LinearLayout layout, final DeviceModel dev,
                             final ImageButton devimb) {
        final Button btnOn = new Button(mContext);
        final Button btnOff = new Button(mContext);
        btnOn.setWidth(55);
        btnOn.setHeight(55);
        btnOn.setTextColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                height);

        layout.addView(btnOn, params);
        if (TextUtils.equals("000", dev.getState())) {
            btnOn.setBackgroundResource(R.drawable.on);
            btnOff.setBackgroundResource(R.drawable.off_b);
            devimb.setBackgroundResource(R.drawable.k);
        } else {
            btnOn.setBackgroundResource(R.drawable.on_b);
            btnOff.setBackgroundResource(R.drawable.off);
            devimb.setBackgroundResource(R.drawable.k_b);
        }
        btnOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOn.setBackgroundResource(R.drawable.on_b);
                btnOff.setBackgroundResource(R.drawable.off);
                devimb.setBackgroundResource(R.drawable.k_b);
                SocketUtil.send(dev, "001");
            }
        });
        btnOff.setWidth(45);
        btnOff.setHeight(45);
        btnOff.setTextColor(Color.TRANSPARENT);
        params.leftMargin = 45;
        layout.addView(btnOff, params);
        btnOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOn.setBackgroundResource(R.drawable.on);
                btnOff.setBackgroundResource(R.drawable.off_b);
                devimb.setBackgroundResource(R.drawable.k);
                SocketUtil.send(dev, "000");
            }
        });
    }
}
