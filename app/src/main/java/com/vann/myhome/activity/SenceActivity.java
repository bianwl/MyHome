package com.vann.myhome.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vann.myhome.BaseActivity;
import com.vann.myhome.R;
import com.vann.myhome.db.HomeDB;
import com.vann.myhome.model.DeviceModel;
import com.vann.myhome.model.SenceModel;
import com.vann.myhome.util.SocketUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景控制界面
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class SenceActivity extends BaseActivity {
    // 控件
    private GridView mGd;

    private HomeDB db;

    private GdAdapter mAdapter;

    private List<String> sences = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        db = HomeDB.getIntance(this);
        mAdapter = new GdAdapter();
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mGd = (GridView) findViewById(R.id.gdMain);
        TextView mTitle = (TextView) findViewById(R.id.tvTitle);
        mTitle.setText(R.string.sence);
    }

    private void initListener() {
        // 九宫格监听
        mGd.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String senceId = sences.get(position);
                Cursor cursor = db.query("sence_devices", null, "senceId=?",
                        new String[]{senceId}, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String devName = cursor.getString(cursor
                                .getColumnIndex("devName"));
                        String devState = cursor.getString(cursor
                                .getColumnIndex("devState"));
                        DeviceModel dev = db.getDevice(devName);
                        SocketUtil.send(dev, devState);
                    } while (cursor.moveToNext());
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        });

    }

    private void initData() {
        sences.clear();
        Cursor cursor = db.query("sences", null, "senceType=?",
                new String[]{"1"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("senceId"));
                sences.add(id);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        mGd.setAdapter(mAdapter);
    }

    class ViewHolder {
        ImageView imv;
        TextView title;
    }

    class GdAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sences.size();
        }

        @Override
        public Object getItem(int position) {
            return sences.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SenceActivity.this).inflate(
                        R.layout.gridview_item, null);
                holder = new ViewHolder();
                holder.imv = (ImageView) convertView
                        .findViewById(R.id.imb_sence);
                holder.title = (TextView) convertView
                        .findViewById(R.id.tvSence);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imv.setBackgroundResource(R.drawable.room0);
            String sid = sences.get(position);
            SenceModel sence = db.getSence(sid);
            if (sence != null) {
                holder.title.setText(sence.getName());
            }
            return convertView;
        }

    }
}
