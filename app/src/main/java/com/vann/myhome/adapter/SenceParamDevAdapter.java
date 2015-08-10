package com.vann.myhome.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vann.myhome.R;
import com.vann.myhome.db.HomeDB;
import com.vann.myhome.model.DeviceModel;

import java.util.ArrayList;
import java.util.List;

/**场景设置界面设备适配器
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class SenceParamDevAdapter extends BaseAdapter {

    private Context mContext;

    private HomeDB mDb;

    private String mSenceId;

    private int width = 60;
    private int height = 60;

    private List<DeviceModel> devices = new ArrayList<DeviceModel>();
    /**
     * 返回选中的设备而信息
     */
    private List<DeviceModel> results = new ArrayList<DeviceModel>();

    /**
     * 选中设备name
     */
    private List<String> selects;

    public SenceParamDevAdapter(Context context, List<String> list, HomeDB db,
                                String senceId) {
        mContext = context;
        selects = list;
        mSenceId = senceId;
        mDb = db;
        devices.addAll(mDb.loadDevices());
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
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_senceparam_list_item, null);
        LinearLayout layout = (LinearLayout) convertView
                .findViewById(R.id.lay_view);
        final DeviceModel dev = devices.get(position);
        String type = dev.getType();
        String sql = "select * from sence_devices where senceId=? and devName=?";
        Cursor cursor = mDb.rawQuery(sql,
                new String[]{mSenceId, dev.getName()});
        String devState = null;
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                devState = cursor.getString(cursor.getColumnIndex("devState"));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        if (TextUtils.equals(type, "k")) {
            createKDevView(layout, dev, devState);
        } else if (TextUtils.equals(type, "t")) {
            createTDevView(layout, dev, devState);
        } else if (TextUtils.equals(type, "c")) {
            createCDevView(layout, dev, devState);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_devName);
        tvName.setText(dev.getName());
        CheckBox check = (CheckBox) convertView.findViewById(R.id.ckb_checked);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    if (!selects.contains(dev.getName()))
                        selects.add(dev.getName());
                } else {
                    selects.remove(dev.getName());
                }
                dev.setChecked(isChecked);
            }
        });
        check.setChecked(selects.contains(dev.getName()));
        return convertView;
    }

    private void createKDevView(LinearLayout layout, final DeviceModel dev, String devState) {
        final Button btnOn = new Button(mContext);
        final Button btnOff = new Button(mContext);

        btnOn.setWidth(45);
        btnOn.setHeight(45);
        btnOn.setTextColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                height);
        if (TextUtils.equals(devState, "001")) {
            btnOn.setBackgroundResource(R.drawable.on_b);
            btnOff.setBackgroundResource(R.drawable.off);
        } else {
            btnOn.setBackgroundResource(R.drawable.on);
            btnOff.setBackgroundResource(R.drawable.off_b);
        }
        btnOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOn.setBackgroundResource(R.drawable.on_b);
                btnOff.setBackgroundResource(R.drawable.off);
                dev.setState("001");
            }
        });
        layout.addView(btnOn, params);

        btnOff.setWidth(45);
        btnOff.setHeight(45);
        btnOff.setTextColor(Color.TRANSPARENT);
        params = new LinearLayout.LayoutParams(width, height);
        params.leftMargin = 30;
        layout.addView(btnOff, params);
        btnOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOn.setBackgroundResource(R.drawable.on);
                btnOff.setBackgroundResource(R.drawable.off_b);
                dev.setState("000");
            }
        });

    }

    private void createTDevView(LinearLayout layout, final DeviceModel dev, String devState) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height);
        params.weight = 1;
        SeekBar sb = new SeekBar(mContext);
        sb.setMax(100);
        sb.setMinimumHeight(24);
        sb.setSecondaryProgress(5);
        sb.setThumbOffset(1);
        final TextView textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        int value = 0;
        if (devState == null || devState == "") {
            devState = "0";
        }
        value = Integer.valueOf(devState);
        textView.setText(value + "");
        sb.setProgress(value);
        layout.addView(sb, params);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
        layout.addView(textView, params);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int val = seekBar.getProgress();
                textView.setText(val + "");
                dev.setState(val + "");
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

    private void createCDevView(LinearLayout layout, final DeviceModel dev, String devState) {
        final Button btnOn = new Button(mContext);
        final Button btnOff = new Button(mContext);
        final Button btnStop = new Button(mContext);
        if (TextUtils.equals(devState, "001")) {
            btnOn.setBackgroundResource(R.drawable.on_b);
            btnOff.setBackgroundResource(R.drawable.off);
            btnStop.setBackgroundResource(R.drawable.stop);
        } else if (TextUtils.equals(devState, "002")) {
            btnOn.setBackgroundResource(R.drawable.on);
            btnOff.setBackgroundResource(R.drawable.off);
            btnStop.setBackgroundResource(R.drawable.stop_b);
        } else {
            btnOn.setBackgroundResource(R.drawable.on);
            btnOff.setBackgroundResource(R.drawable.off_b);
            btnStop.setBackgroundResource(R.drawable.stop);
        }
        btnOn.setWidth(45);
        btnOn.setHeight(45);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                height);
        layout.addView(btnOn, params);
        btnOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOn.setBackgroundResource(R.drawable.on_b);
                btnOff.setBackgroundResource(R.drawable.off);
                btnStop.setBackgroundResource(R.drawable.stop);
                dev.setState("001");
            }
        });
        btnOff.setWidth(45);
        btnOff.setHeight(45);
        params = new LinearLayout.LayoutParams(width, height);
        params.leftMargin = 30;
        layout.addView(btnOff, params);
        btnOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOn.setBackgroundResource(R.drawable.on);
                btnOff.setBackgroundResource(R.drawable.off_b);
                btnStop.setBackgroundResource(R.drawable.stop);
                dev.setState("000");
            }
        });

        btnStop.setWidth(45);
        btnStop.setHeight(45);
        layout.addView(btnStop, params);
        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnOn.setBackgroundResource(R.drawable.on);
                btnOff.setBackgroundResource(R.drawable.off);
                btnStop.setBackgroundResource(R.drawable.stop_b);
                dev.setState("002");
            }
        });

    }

    public List<DeviceModel> getResults() {
        results.clear();
        for (DeviceModel dev : devices) {
            if (selects.contains(dev.getName())) {
                results.add(dev);
            }
        }
        return results;
    }

}
